package com.zluolan.zaiagent.tools;

import com.zluolan.zaiagent.mcp.AmapMcpToolWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 增强的旅游工具类
 * 集成高德地图MCP服务，提供更强大的旅游规划功能
 */
@Component
@Slf4j
public class EnhancedTravelTools {
    
    @Autowired
    private AmapMcpToolWrapper amapWrapper;
    
    /**
     * 综合旅游规划工具
     * 集成景点、酒店、路线、天气等全方位信息
     */
    @Tool(name = "comprehensiveTravelPlanning", description = """
            综合旅游规划工具，提供一站式旅游规划服务。
            参数说明：
            - destination: 目的地（如：北京、上海、杭州等）
            - travelDate: 出行日期（格式：YYYY-MM-DD）
            - budget: 预算范围（经济型、舒适型、豪华型）
            - preferences: 偏好设置（文化、自然、美食、购物等）
            - days: 旅行天数（默认3天）
            """)
    public String comprehensiveTravelPlanning(String destination, String travelDate, 
                                             String budget, String preferences, Integer days) {
        try {
            log.info("综合旅游规划 - 目的地: {}, 日期: {}, 预算: {}, 偏好: {}, 天数: {}", 
                    destination, travelDate, budget, preferences, days);
            
            if (days == null || days <= 0) {
                days = 3;
            }
            
            StringBuilder result = new StringBuilder();
            result.append("🎯 ").append(destination).append(" 综合旅游规划方案\n");
            result.append("=".repeat(50)).append("\n\n");
            
            // 1. 景点推荐
            result.append("🏛️ 景点推荐\n");
            result.append("-".repeat(20)).append("\n");
            String attractions = amapWrapper.searchAttractions(destination, preferences, 5);
            result.append(attractions).append("\n\n");
            
            // 2. 酒店推荐
            result.append("🏨 住宿推荐\n");
            result.append("-".repeat(20)).append("\n");
            String hotels = amapWrapper.searchHotels(destination, travelDate, 
                    LocalDate.parse(travelDate).plusDays(days - 1).toString(), 
                    2, budget, "商务酒店");
            result.append(hotels).append("\n\n");
            
            // 3. 路线规划
            result.append("🛣️ 路线规划\n");
            result.append("-".repeat(20)).append("\n");
            String routes = amapWrapper.planRoute("市中心", "主要景点", null, "公交", travelDate, "风景优美");
            result.append(routes).append("\n\n");
            
            // 4. 天气信息
            result.append("🌤️ 天气信息\n");
            result.append("-".repeat(20)).append("\n");
            String weather = amapWrapper.queryWeather(destination, travelDate, days);
            result.append(weather).append("\n\n");
            
            // 5. 旅游建议
            result.append("💡 旅游建议\n");
            result.append("-".repeat(20)).append("\n");
            result.append(generateTravelAdvice(destination, budget, preferences, days));
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("综合旅游规划失败", e);
            return "旅游规划失败: " + e.getMessage();
        }
    }
    
    /**
     * 智能景点推荐工具
     */
    @Tool(name = "smartAttractionRecommendation", description = """
            智能景点推荐工具，根据用户偏好推荐最合适的景点。
            参数说明：
            - destination: 目的地
            - category: 景点类型（文化古迹、自然风光、主题乐园、博物馆等）
            - budget: 预算范围
            - timeAvailable: 可用时间（半天、全天、多天）
            """)
    public String smartAttractionRecommendation(String destination, String category, 
                                               String budget, String timeAvailable) {
        try {
            log.info("智能景点推荐 - 目的地: {}, 类型: {}, 预算: {}, 时间: {}", 
                    destination, category, budget, timeAvailable);
            
            StringBuilder result = new StringBuilder();
            result.append("🎯 ").append(destination).append(" 智能景点推荐\n");
            result.append("=".repeat(40)).append("\n\n");
            
            // 调用高德地图MCP服务搜索景点
            String attractions = amapWrapper.searchAttractions(destination, category, 8);
            result.append(attractions).append("\n");
            
            // 根据预算和时间提供建议
            result.append("💡 个性化建议\n");
            result.append("-".repeat(20)).append("\n");
            result.append(generatePersonalizedAdvice(category, budget, timeAvailable));
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("智能景点推荐失败", e);
            return "景点推荐失败: " + e.getMessage();
        }
    }
    
    /**
     * 智能酒店推荐工具
     */
    @Tool(name = "smartHotelRecommendation", description = """
            智能酒店推荐工具，根据预算和位置推荐最合适的酒店。
            参数说明：
            - destination: 目的地
            - checkInDate: 入住日期
            - checkOutDate: 退房日期
            - budget: 预算范围
            - locationPreference: 位置偏好（市中心、景区附近、交通便利等）
            """)
    public String smartHotelRecommendation(String destination, String checkInDate, 
                                          String checkOutDate, String budget, String locationPreference) {
        try {
            log.info("智能酒店推荐 - 目的地: {}, 入住: {}, 退房: {}, 预算: {}, 位置: {}", 
                    destination, checkInDate, checkOutDate, budget, locationPreference);
            
            StringBuilder result = new StringBuilder();
            result.append("🏨 ").append(destination).append(" 智能酒店推荐\n");
            result.append("=".repeat(40)).append("\n\n");
            
            // 调用高德地图MCP服务搜索酒店
            String hotels = amapWrapper.searchHotels(destination, checkInDate, checkOutDate, 
                    2, budget, "商务酒店");
            result.append(hotels).append("\n");
            
            // 根据位置偏好提供建议
            result.append("💡 位置建议\n");
            result.append("-".repeat(20)).append("\n");
            result.append(generateLocationAdvice(locationPreference, destination));
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("智能酒店推荐失败", e);
            return "酒店推荐失败: " + e.getMessage();
        }
    }
    
    /**
     * 智能路线规划工具
     */
    @Tool(name = "smartRoutePlanning", description = """
            智能路线规划工具，根据交通方式和偏好规划最优路线。
            参数说明：
            - startLocation: 起点
            - endLocation: 终点
            - transportMode: 交通方式（驾车、公交、步行、骑行）
            - preferences: 偏好（最快、最省钱、风景优美）
            - waypoints: 途经点（可选）
            """)
    public String smartRoutePlanning(String startLocation, String endLocation, 
                                   String transportMode, String preferences, String waypoints) {
        try {
            log.info("智能路线规划 - 起点: {}, 终点: {}, 交通: {}, 偏好: {}, 途经: {}", 
                    startLocation, endLocation, transportMode, preferences, waypoints);
            
            StringBuilder result = new StringBuilder();
            result.append("🛣️ 智能路线规划\n");
            result.append("=".repeat(40)).append("\n\n");
            
            // 调用高德地图MCP服务规划路线
            String routes = amapWrapper.planRoute(startLocation, endLocation, waypoints, 
                    transportMode, LocalDate.now().toString(), preferences);
            result.append(routes).append("\n");
            
            // 根据偏好提供额外建议
            result.append("💡 路线建议\n");
            result.append("-".repeat(20)).append("\n");
            result.append(generateRouteAdvice(transportMode, preferences));
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("智能路线规划失败", e);
            return "路线规划失败: " + e.getMessage();
        }
    }
    
    /**
     * 智能天气查询工具
     */
    @Tool(name = "smartWeatherQuery", description = """
            智能天气查询工具，提供详细的天气信息和出行建议。
            参数说明：
            - location: 查询地点
            - date: 查询日期（可选，默认今天）
            - days: 查询天数（1-7天）
            - activityType: 活动类型（户外、室内、运动等）
            """)
    public String smartWeatherQuery(String location, String date, Integer days, String activityType) {
        try {
            log.info("智能天气查询 - 地点: {}, 日期: {}, 天数: {}, 活动: {}", 
                    location, date, days, activityType);
            
            if (date == null) {
                date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            
            StringBuilder result = new StringBuilder();
            result.append("🌤️ ").append(location).append(" 智能天气查询\n");
            result.append("=".repeat(40)).append("\n\n");
            
            // 调用高德地图MCP服务查询天气
            String weather = amapWrapper.queryWeather(location, date, days);
            result.append(weather).append("\n");
            
            // 根据活动类型提供建议
            result.append("💡 活动建议\n");
            result.append("-".repeat(20)).append("\n");
            result.append(generateActivityAdvice(activityType, location));
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("智能天气查询失败", e);
            return "天气查询失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成旅游建议
     */
    private String generateTravelAdvice(String destination, String budget, String preferences, int days) {
        StringBuilder advice = new StringBuilder();
        
        advice.append("📋 行程安排建议：\n");
        advice.append("• 建议安排").append(days).append("天行程，每天2-3个主要景点\n");
        advice.append("• 预留充足的交通和用餐时间\n");
        advice.append("• 根据天气情况调整户外活动安排\n\n");
        
        advice.append("💰 预算建议：\n");
        if ("经济型".equals(budget)) {
            advice.append("• 选择公共交通出行，节省交通费用\n");
            advice.append("• 选择经济型酒店或民宿\n");
            advice.append("• 品尝当地特色小吃，体验地道美食\n");
        } else if ("舒适型".equals(budget)) {
            advice.append("• 可选择网约车或租车出行\n");
            advice.append("• 选择三星级酒店，舒适度较高\n");
            advice.append("• 可体验当地特色餐厅\n");
        } else if ("豪华型".equals(budget)) {
            advice.append("• 可选择专车服务或豪华租车\n");
            advice.append("• 选择五星级酒店，享受优质服务\n");
            advice.append("• 可体验米其林餐厅和高端娱乐\n");
        }
        
        advice.append("\n🎯 偏好建议：\n");
        if (preferences != null) {
            if (preferences.contains("文化")) {
                advice.append("• 重点安排博物馆、古迹、文化街区\n");
            }
            if (preferences.contains("自然")) {
                advice.append("• 安排公园、湖泊、山景等自然景观\n");
            }
            if (preferences.contains("美食")) {
                advice.append("• 安排当地特色餐厅和美食街\n");
            }
            if (preferences.contains("购物")) {
                advice.append("• 安排商业街、购物中心、特色市场\n");
            }
        }
        
        return advice.toString();
    }
    
    /**
     * 生成个性化建议
     */
    private String generatePersonalizedAdvice(String category, String budget, String timeAvailable) {
        StringBuilder advice = new StringBuilder();
        
        advice.append("根据您的偏好，为您推荐：\n");
        
        if ("文化古迹".equals(category)) {
            advice.append("• 建议安排充足时间参观，每个景点2-3小时\n");
            advice.append("• 可考虑请导游讲解，了解更多历史文化\n");
        } else if ("自然风光".equals(category)) {
            advice.append("• 建议选择天气晴朗的日子出行\n");
            advice.append("• 准备舒适的步行鞋和防晒用品\n");
        } else if ("主题乐园".equals(category)) {
            advice.append("• 建议选择工作日出行，避开人流高峰\n");
            advice.append("• 提前了解游乐设施开放时间\n");
        }
        
        if ("半天".equals(timeAvailable)) {
            advice.append("• 选择1-2个距离较近的景点\n");
            advice.append("• 合理安排交通时间\n");
        } else if ("全天".equals(timeAvailable)) {
            advice.append("• 可安排3-4个景点，注意劳逸结合\n");
            advice.append("• 预留用餐和休息时间\n");
        }
        
        return advice.toString();
    }
    
    /**
     * 生成位置建议
     */
    private String generateLocationAdvice(String locationPreference, String destination) {
        StringBuilder advice = new StringBuilder();
        
        if ("市中心".equals(locationPreference)) {
            advice.append("• 选择市中心酒店，交通便利，购物方便\n");
            advice.append("• 推荐").append(destination).append("市中心商务区\n");
        } else if ("景区附近".equals(locationPreference)) {
            advice.append("• 选择主要景点附近酒店，节省交通时间\n");
            advice.append("• 可享受景区周边配套设施\n");
        } else if ("交通便利".equals(locationPreference)) {
            advice.append("• 选择地铁站或公交站附近酒店\n");
            advice.append("• 便于前往各个景点\n");
        }
        
        return advice.toString();
    }
    
    /**
     * 生成路线建议
     */
    private String generateRouteAdvice(String transportMode, String preferences) {
        StringBuilder advice = new StringBuilder();
        
        if ("驾车".equals(transportMode)) {
            advice.append("• 注意交通规则和停车位信息\n");
            advice.append("• 提前了解路况和限行政策\n");
        } else if ("公交".equals(transportMode)) {
            advice.append("• 下载当地公交APP，实时查看班次\n");
            advice.append("• 准备零钱或交通卡\n");
        } else if ("步行".equals(transportMode)) {
            advice.append("• 选择舒适的步行鞋\n");
            advice.append("• 注意天气情况，准备雨具\n");
        }
        
        if ("最快".equals(preferences)) {
            advice.append("• 选择直达路线，避开拥堵路段\n");
        } else if ("最省钱".equals(preferences)) {
            advice.append("• 选择公共交通，避开收费路段\n");
        } else if ("风景优美".equals(preferences)) {
            advice.append("• 选择沿江、沿湖或公园路线\n");
        }
        
        return advice.toString();
    }
    
    /**
     * 生成活动建议
     */
    private String generateActivityAdvice(String activityType, String location) {
        StringBuilder advice = new StringBuilder();
        
        if ("户外".equals(activityType)) {
            advice.append("• 关注天气变化，准备防晒和雨具\n");
            advice.append("• 选择空气质量良好的时段出行\n");
        } else if ("室内".equals(activityType)) {
            advice.append("• 可安排博物馆、展览馆等室内活动\n");
            advice.append("• 不受天气影响，适合全天安排\n");
        } else if ("运动".equals(activityType)) {
            advice.append("• 选择温度适宜的时间段\n");
            advice.append("• 注意补充水分，避免中暑\n");
        }
        
        return advice.toString();
    }
}
