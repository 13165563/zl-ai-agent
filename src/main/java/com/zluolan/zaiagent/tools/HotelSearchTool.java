package com.zluolan.zaiagent.tools;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 酒店搜索工具
 * 用于搜索指定目的地的酒店住宿信息
 */
@Component
@Slf4j
public class HotelSearchTool {

    @Tool(name = "searchHotels", description = """
            搜索指定目的地的酒店住宿信息。
            参数说明：
            - destination: 目的地名称（如：北京、上海、巴黎等）
            - checkInDate: 入住日期（格式：YYYY-MM-DD）
            - checkOutDate: 退房日期（格式：YYYY-MM-DD）
            - guests: 入住人数（默认2人）
            - priceRange: 价格范围（经济型、舒适型、豪华型、奢华型）
            - hotelType: 酒店类型（商务酒店、度假酒店、精品酒店、民宿等）
            """)
    public String searchHotels(String destination, String checkInDate, String checkOutDate, 
                              Integer guests, String priceRange, String hotelType) {
        try {
            log.info("搜索酒店信息 - 目的地: {}, 入住: {}, 退房: {}, 人数: {}, 价格: {}, 类型: {}", 
                    destination, checkInDate, checkOutDate, guests, priceRange, hotelType);
            
            if (guests == null || guests <= 0) {
                guests = 2;
            }
            
            JSONObject result = new JSONObject();
            result.set("destination", destination);
            result.set("checkInDate", checkInDate);
            result.set("checkOutDate", checkOutDate);
            result.set("guests", guests);
            result.set("priceRange", priceRange);
            result.set("hotelType", hotelType);
            
            // 生成酒店推荐数据
            String hotels = generateHotelData(destination, priceRange, hotelType);
            result.set("hotels", hotels);
            result.set("searchTime", System.currentTimeMillis());
            result.set("status", "success");
            
            log.info("酒店搜索完成: {}", result.toString());
            return "[TOOL_EXECUTION_RESULT] 酒店搜索成功！\n" + result.toString();
            
        } catch (Exception e) {
            log.error("酒店搜索失败", e);
            return "[TOOL_EXECUTION_RESULT] 酒店搜索失败: " + e.getMessage();
        }
    }
    
    /**
     * 根据目的地和条件生成酒店数据
     */
    private String generateHotelData(String destination, String priceRange, String hotelType) {
        StringBuilder hotels = new StringBuilder();
        
        // 根据不同目的地提供不同的酒店信息
        if (destination.contains("北京")) {
            if ("豪华型".equals(priceRange) || "奢华型".equals(priceRange)) {
                hotels.append("1. 北京王府井希尔顿酒店 - 五星级豪华酒店，位置优越\n");
                hotels.append("   - 价格：¥800-1200/晚\n");
                hotels.append("   - 设施：健身房、游泳池、商务中心\n");
                hotels.append("   - 距离天安门：步行10分钟\n\n");
                
                hotels.append("2. 北京饭店 - 历史悠久的奢华酒店\n");
                hotels.append("   - 价格：¥1000-1500/晚\n");
                hotels.append("   - 设施：中西餐厅、会议室、礼宾服务\n");
                hotels.append("   - 距离故宫：步行5分钟\n\n");
            } else if ("舒适型".equals(priceRange)) {
                hotels.append("1. 如家酒店（王府井店） - 连锁商务酒店\n");
                hotels.append("   - 价格：¥300-500/晚\n");
                hotels.append("   - 设施：免费WiFi、24小时前台\n");
                hotels.append("   - 距离地铁：步行3分钟\n\n");
                
                hotels.append("2. 汉庭酒店（前门店） - 经济型连锁酒店\n");
                hotels.append("   - 价格：¥250-400/晚\n");
                hotels.append("   - 设施：自助早餐、商务中心\n");
                hotels.append("   - 距离前门大街：步行2分钟\n\n");
            } else {
                hotels.append("1. 7天连锁酒店 - 经济型住宿\n");
                hotels.append("   - 价格：¥150-250/晚\n");
                hotels.append("   - 设施：基础设施齐全\n");
                hotels.append("   - 交通便利，性价比高\n\n");
            }
        } else if (destination.contains("上海")) {
            if ("豪华型".equals(priceRange) || "奢华型".equals(priceRange)) {
                hotels.append("1. 上海外滩华尔道夫酒店 - 奢华地标酒店\n");
                hotels.append("   - 价格：¥1500-2500/晚\n");
                hotels.append("   - 设施：米其林餐厅、SPA、江景客房\n");
                hotels.append("   - 位置：外滩核心位置\n\n");
                
                hotels.append("2. 上海浦东丽思卡尔顿酒店 - 国际奢华品牌\n");
                hotels.append("   - 价格：¥1200-2000/晚\n");
                hotels.append("   - 设施：高层江景、行政酒廊\n");
                hotels.append("   - 距离陆家嘴：步行5分钟\n\n");
            } else if ("舒适型".equals(priceRange)) {
                hotels.append("1. 锦江之星（南京路店） - 知名连锁酒店\n");
                hotels.append("   - 价格：¥400-600/晚\n");
                hotels.append("   - 设施：商务设施、健身房\n");
                hotels.append("   - 距离南京路步行街：步行1分钟\n\n");
            }
        } else {
            // 通用酒店信息
            hotels.append("推荐酒店类型：\n");
            if ("豪华型".equals(priceRange) || "奢华型".equals(priceRange)) {
                hotels.append("1. 国际连锁五星级酒店 - 希尔顿、万豪、洲际等\n");
                hotels.append("2. 当地知名豪华酒店 - 提供高端服务和设施\n");
                hotels.append("3. 精品设计酒店 - 独特设计和个性化服务\n");
            } else if ("舒适型".equals(priceRange)) {
                hotels.append("1. 商务连锁酒店 - 如家、汉庭、锦江之星\n");
                hotels.append("2. 当地三四星级酒店 - 性价比较高\n");
                hotels.append("3. 公寓式酒店 - 适合长期住宿\n");
            } else {
                hotels.append("1. 经济型连锁酒店 - 7天、格林豪泰\n");
                hotels.append("2. 青年旅社 - 适合背包客\n");
                hotels.append("3. 民宿客栈 - 体验当地文化\n");
            }
        }
        
        // 根据酒店类型添加特殊推荐
        if (hotelType != null && !hotelType.trim().isEmpty()) {
            hotels.append("\n特别推荐（").append(hotelType).append("）：\n");
            if (hotelType.contains("度假")) {
                hotels.append("- 选择带有度假设施的酒店（游泳池、SPA、花园等）\n");
            } else if (hotelType.contains("商务")) {
                hotels.append("- 选择交通便利、商务设施完善的酒店\n");
            } else if (hotelType.contains("民宿")) {
                hotels.append("- 推荐当地特色民宿，体验本土文化\n");
            }
        }
        
        return hotels.toString();
    }
}


