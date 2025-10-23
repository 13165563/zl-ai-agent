package com.zluolan.zaiagent.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 天气查询工具
 * 用于查询目的地天气信息，为旅游规划提供参考
 */
@Component
@Slf4j
public class WeatherQueryTool {

    @Value("${amap.api.key:c35d3d557f6de34f7ecae355b99d9147}")
    private String amapApiKey;

    @Tool(name = "queryWeather", description = """
            查询指定地点的天气信息。
            参数说明：
            - location: 查询地点（如：北京、上海、巴黎等）
            - date: 查询日期（格式：YYYY-MM-DD，可选，默认查询今天）
            - days: 查询天数（1-7天，默认1天）
            """)
    public String queryWeather(String location, String date, Integer days) {
        try {
            log.info("查询天气信息 - 地点: {}, 日期: {}, 天数: {}", location, date, days);
            
            if (days == null || days <= 0 || days > 7) {
                days = 1;
            }
            
            if (date == null || date.trim().isEmpty()) {
                date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            
            JSONObject result = new JSONObject();
            result.set("location", location);
            result.set("queryDate", date);
            result.set("days", days);
            
            // 使用高德地图API查询天气
            String weatherInfo = queryWeatherFromAmap(location, date, days);
            result.set("weatherInfo", weatherInfo);
            result.set("queryTime", System.currentTimeMillis());
            result.set("status", "success");
            
            log.info("天气查询完成: {}", result.toString());
            return "[TOOL_EXECUTION_RESULT] 天气查询成功！\n" + result.toString();
            
        } catch (Exception e) {
            log.error("天气查询失败", e);
            return "[TOOL_EXECUTION_RESULT] 天气查询失败: " + e.getMessage();
        }
    }
    
    /**
     * 使用高德地图API查询天气
     */
    private String queryWeatherFromAmap(String location, String date, int days) {
        try {
            // 首先获取城市编码
            String cityCode = getCityCode(location);
            if (cityCode == null) {
                log.warn("无法获取城市编码，使用模拟数据: {}", location);
                return generateWeatherData(location, date, days);
            }

            // 查询天气信息
            String weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo";
            HttpResponse response = HttpRequest.get(weatherUrl)
                    .form("key", amapApiKey)
                    .form("city", cityCode)
                    .form("extensions", days > 1 ? "all" : "base")
                    .execute();

            if (response.getStatus() == 200) {
                JSONObject weatherData = JSONUtil.parseObj(response.body());
                return formatAmapWeatherData(weatherData, location, date, days);
            } else {
                log.warn("高德地图API调用失败，状态码: {}, 使用模拟数据", response.getStatus());
                return generateWeatherData(location, date, days);
            }
        } catch (Exception e) {
            log.error("调用高德地图API失败，使用模拟数据", e);
            return generateWeatherData(location, date, days);
        }
    }

    /**
     * 获取城市编码
     */
    private String getCityCode(String location) {
        try {
            String geoUrl = "https://restapi.amap.com/v3/geocode/geo";
            HttpResponse response = HttpRequest.get(geoUrl)
                    .form("key", amapApiKey)
                    .form("address", location)
                    .execute();

            if (response.getStatus() == 200) {
                JSONObject geoData = JSONUtil.parseObj(response.body());
                if (geoData.getJSONArray("geocodes") != null && 
                    geoData.getJSONArray("geocodes").size() > 0) {
                    JSONObject geocode = geoData.getJSONArray("geocodes").getJSONObject(0);
                    return geocode.getStr("adcode");
                }
            }
        } catch (Exception e) {
            log.error("获取城市编码失败", e);
        }
        return null;
    }

    /**
     * 格式化高德地图天气数据
     */
    private String formatAmapWeatherData(JSONObject weatherData, String location, String date, int days) {
        StringBuilder result = new StringBuilder();
        result.append("📍 ").append(location).append(" 天气预报\n");
        result.append("查询日期：").append(date).append("\n\n");

        if (weatherData.getStr("status").equals("1")) {
            if (days > 1 && weatherData.getJSONArray("forecasts") != null) {
                // 多天预报
                weatherData.getJSONArray("forecasts").forEach(forecast -> {
                    JSONObject forecastObj = (JSONObject) forecast;
                    result.append("📅 ").append(forecastObj.getStr("date")).append("\n");
                    result.append("🌡️ 温度：").append(forecastObj.getStr("temp")).append("°C\n");
                    result.append("☁️ 天气：").append(forecastObj.getStr("weather")).append("\n");
                    result.append("💨 风向：").append(forecastObj.getStr("winddirection")).append("\n");
                    result.append("💨 风力：").append(forecastObj.getStr("windpower")).append("\n");
                    result.append("🌅 日出：").append(forecastObj.getStr("sunrise")).append(" | ");
                    result.append("🌇 日落：").append(forecastObj.getStr("sunset")).append("\n");
                    result.append("🎒 出行建议：").append(generateTravelAdviceFromWeather(forecastObj.getStr("weather"))).append("\n\n");
                });
            } else if (weatherData.getJSONArray("lives") != null && weatherData.getJSONArray("lives").size() > 0) {
                // 实时天气
                JSONObject live = weatherData.getJSONArray("lives").getJSONObject(0);
                result.append("📅 ").append(date).append(" (实时)\n");
                result.append("🌡️ 温度：").append(live.getStr("temperature")).append("°C\n");
                result.append("☁️ 天气：").append(live.getStr("weather")).append("\n");
                result.append("💨 风向：").append(live.getStr("winddirection")).append("\n");
                result.append("💨 风力：").append(live.getStr("windpower")).append("\n");
                result.append("💧 湿度：").append(live.getStr("humidity")).append("%\n");
                result.append("🎒 出行建议：").append(generateTravelAdviceFromWeather(live.getStr("weather"))).append("\n");
            }
        } else {
            result.append("⚠️ 天气数据获取失败，请稍后重试\n");
        }

        return result.toString();
    }

    /**
     * 根据天气状况生成旅游建议
     */
    private String generateTravelAdviceFromWeather(String weather) {
        if (weather == null) return "天气信息不明确，建议关注最新天气预报";
        
        if (weather.contains("雨")) {
            return "建议携带雨具，注意防滑，室内活动为主";
        } else if (weather.contains("晴")) {
            return "天气晴朗，适合户外活动，注意防晒";
        } else if (weather.contains("多云") || weather.contains("阴")) {
            return "天气适宜，适合各种户外活动";
        } else if (weather.contains("雪")) {
            return "雪天出行，注意保暖和防滑，谨慎驾驶";
        } else if (weather.contains("雾")) {
            return "能见度较低，注意交通安全，谨慎出行";
        } else {
            return "天气状况一般，建议关注最新天气预报";
        }
    }

    /**
     * 生成天气数据（模拟数据，作为备用方案）
     */
    private String generateWeatherData(String location, String date, int days) {
        StringBuilder weather = new StringBuilder();
        
        weather.append("📍 ").append(location).append(" 天气预报 (模拟数据)\n");
        weather.append("查询日期：").append(date).append("\n\n");
        weather.append("⚠️ 注意：当前使用模拟天气数据，实际部署时请配置高德地图API\n\n");
        
        LocalDate startDate = LocalDate.parse(date);
        
        for (int i = 0; i < days; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            String dayOfWeek = getDayOfWeek(currentDate.getDayOfWeek().getValue());
            
            weather.append("📅 ").append(currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                   .append(" (").append(dayOfWeek).append(")\n");
            
            // 根据地点和季节生成合理的天气数据
            WeatherData weatherData = generateReasonableWeather(location, currentDate);
            
            weather.append("🌡️ 温度：").append(weatherData.minTemp).append("°C - ")
                   .append(weatherData.maxTemp).append("°C\n");
            weather.append("☁️ 天气：").append(weatherData.condition).append("\n");
            weather.append("💨 风力：").append(weatherData.windLevel).append("\n");
            weather.append("💧 湿度：").append(weatherData.humidity).append("%\n");
            weather.append("🌅 日出：").append(weatherData.sunrise).append(" | ");
            weather.append("🌇 日落：").append(weatherData.sunset).append("\n");
            
            // 旅游建议
            weather.append("🎒 出行建议：").append(generateTravelAdvice(weatherData)).append("\n");
            
            if (i < days - 1) {
                weather.append("\n");
            }
        }
        
        return weather.toString();
    }
    
    private String getDayOfWeek(int dayOfWeek) {
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return days[dayOfWeek - 1];
    }
    
    /**
     * 根据地点和日期生成合理的天气数据
     */
    private WeatherData generateReasonableWeather(String location, LocalDate date) {
        WeatherData data = new WeatherData();
        
        // 根据月份确定季节
        int month = date.getMonthValue();
        String season = getSeason(month);
        
        // 根据地点和季节设置基础温度范围（使用固定值模拟）
        if (location.contains("北京") || location.contains("天津")) {
            switch (season) {
                case "春季" -> { data.minTemp = 8; data.maxTemp = 18; }
                case "夏季" -> { data.minTemp = 22; data.maxTemp = 32; }
                case "秋季" -> { data.minTemp = 10; data.maxTemp = 20; }
                case "冬季" -> { data.minTemp = -2; data.maxTemp = 8; }
            }
        } else if (location.contains("上海") || location.contains("杭州")) {
            switch (season) {
                case "春季" -> { data.minTemp = 12; data.maxTemp = 22; }
                case "夏季" -> { data.minTemp = 26; data.maxTemp = 34; }
                case "秋季" -> { data.minTemp = 18; data.maxTemp = 26; }
                case "冬季" -> { data.minTemp = 4; data.maxTemp = 12; }
            }
        } else {
            // 通用温度范围
            data.minTemp = 15;
            data.maxTemp = 25;
        }
        
        // 天气状况（固定选择）
        String[] conditions = {"晴", "多云", "阴", "小雨", "中雨", "雷阵雨", "雾", "霾"};
        data.condition = conditions[Math.abs(location.hashCode()) % conditions.length];
        
        // 风力（固定选择）
        String[] windLevels = {"微风1-2级", "轻风3-4级", "和风5-6级", "强风7-8级"};
        data.windLevel = windLevels[Math.abs(location.hashCode()) % windLevels.length];
        
        // 湿度（固定值）
        data.humidity = 50 + (Math.abs(location.hashCode()) % 30);
        
        // 日出日落时间（固定时间）
        data.sunrise = "06:30";
        data.sunset = "18:30";
        
        return data;
    }
    
    private String getSeason(int month) {
        if (month >= 3 && month <= 5) return "春季";
        if (month >= 6 && month <= 8) return "夏季";
        if (month >= 9 && month <= 11) return "秋季";
        return "冬季";
    }
    
    private String generateTravelAdvice(WeatherData data) {
        StringBuilder advice = new StringBuilder();
        
        if (data.condition.contains("雨")) {
            advice.append("携带雨具，注意防滑；");
        }
        if (data.maxTemp > 30) {
            advice.append("注意防晒，多补充水分；");
        }
        if (data.minTemp < 5) {
            advice.append("注意保暖，穿着厚衣物；");
        }
        if (data.condition.contains("雾") || data.condition.contains("霾")) {
            advice.append("能见度较低，注意交通安全；");
        }
        if (data.condition.equals("晴")) {
            advice.append("天气晴朗，适合户外活动；");
        }
        
        if (advice.length() == 0) {
            advice.append("天气适宜，注意适当增减衣物");
        } else {
            // 移除最后的分号
            advice.setLength(advice.length() - 1);
        }
        
        return advice.toString();
    }
    
    /**
     * 天气数据内部类
     */
    private static class WeatherData {
        int minTemp;
        int maxTemp;
        String condition;
        String windLevel;
        int humidity;
        String sunrise;
        String sunset;
    }
}


