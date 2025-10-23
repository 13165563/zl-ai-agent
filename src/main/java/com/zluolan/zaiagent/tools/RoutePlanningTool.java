package com.zluolan.zaiagent.tools;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 路线规划工具
 * 用于规划旅游路线和交通方案
 */
@Component
@Slf4j
public class RoutePlanningTool {

    @Tool(name = "planRoute", description = """
            规划旅游路线和交通方案。
            参数说明：
            - startLocation: 出发地点
            - endLocation: 目的地点
            - waypoints: 途经点（可选，多个地点用逗号分隔）
            - transportMode: 交通方式（飞机、高铁、汽车、公共交通、步行、骑行等）
            - travelDate: 出行日期（格式：YYYY-MM-DD）
            - preferences: 偏好设置（最快路线、最省钱、风景优美等）
            """)
    public String planRoute(String startLocation, String endLocation, String waypoints, 
                           String transportMode, String travelDate, String preferences) {
        try {
            log.info("规划路线 - 起点: {}, 终点: {}, 途经: {}, 交通: {}, 日期: {}, 偏好: {}", 
                    startLocation, endLocation, waypoints, transportMode, travelDate, preferences);
            
            JSONObject result = new JSONObject();
            result.set("startLocation", startLocation);
            result.set("endLocation", endLocation);
            result.set("waypoints", waypoints);
            result.set("transportMode", transportMode);
            result.set("travelDate", travelDate);
            result.set("preferences", preferences);
            
            // 生成路线规划数据
            String routePlan = generateRoutePlan(startLocation, endLocation, waypoints, transportMode, preferences);
            result.set("routePlan", routePlan);
            result.set("planTime", System.currentTimeMillis());
            result.set("status", "success");
            
            log.info("路线规划完成: {}", result.toString());
            return "[TOOL_EXECUTION_RESULT] 路线规划成功！\n" + result.toString();
            
        } catch (Exception e) {
            log.error("路线规划失败", e);
            return "[TOOL_EXECUTION_RESULT] 路线规划失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成路线规划方案
     */
    private String generateRoutePlan(String startLocation, String endLocation, String waypoints, 
                                   String transportMode, String preferences) {
        StringBuilder plan = new StringBuilder();
        
        plan.append("=== 路线规划方案 ===\n");
        plan.append("起点：").append(startLocation).append("\n");
        plan.append("终点：").append(endLocation).append("\n");
        
        if (waypoints != null && !waypoints.trim().isEmpty()) {
            plan.append("途经点：").append(waypoints).append("\n");
        }
        
        plan.append("\n=== 推荐交通方案 ===\n");
        
        // 根据交通方式提供不同建议
        if (transportMode != null) {
            switch (transportMode.toLowerCase()) {
                case "飞机":
                case "airplane":
                case "flight":
                    plan.append(generateFlightPlan(startLocation, endLocation));
                    break;
                case "高铁":
                case "train":
                case "railway":
                    plan.append(generateTrainPlan(startLocation, endLocation));
                    break;
                case "汽车":
                case "car":
                case "driving":
                    plan.append(generateDrivingPlan(startLocation, endLocation, waypoints));
                    break;
                case "公共交通":
                case "public":
                case "bus":
                    plan.append(generatePublicTransportPlan(startLocation, endLocation));
                    break;
                default:
                    plan.append(generateComprehensivePlan(startLocation, endLocation));
                    break;
            }
        } else {
            plan.append(generateComprehensivePlan(startLocation, endLocation));
        }
        
        // 根据偏好添加建议
        if (preferences != null && !preferences.trim().isEmpty()) {
            plan.append("\n=== 根据偏好的特别建议 ===\n");
            if (preferences.contains("最快") || preferences.contains("时间")) {
                plan.append("⏰ 时间优先建议：选择直达航班或高铁，避开高峰时段\n");
            }
            if (preferences.contains("省钱") || preferences.contains("经济")) {
                plan.append("💰 经济实惠建议：提前预订，选择经济舱或二等座，考虑中转方案\n");
            }
            if (preferences.contains("风景") || preferences.contains("景色")) {
                plan.append("🌄 风景优美建议：选择沿途风景好的路线，可考虑火车或自驾\n");
            }
        }
        
        plan.append("\n=== 出行贴士 ===\n");
        plan.append("🎫 建议提前预订交通票据，特别是节假日期间\n");
        plan.append("📱 下载相关交速APP，实时查看班次和路况信息\n");
        plan.append("🧳 根据交通方式合理安排行李，注意重量和尺寸限制\n");
        plan.append("⏰ 预留充足的中转和候车时间\n");
        
        return plan.toString();
    }
    
    private String generateFlightPlan(String start, String end) {
        return String.format("""
                ✈️ 航班方案：
                • 推荐航线：%s → %s
                • 飞行时间：约1-3小时（根据距离）
                • 价格范围：¥500-2000（经济舱）
                • 建议：提前2小时到达机场，关注天气影响
                • 优势：速度快，适合长距离出行
                
                """, start, end);
    }
    
    private String generateTrainPlan(String start, String end) {
        return String.format("""
                🚄 高铁/火车方案：
                • 推荐线路：%s → %s
                • 行程时间：约2-8小时（根据距离和车次）
                • 价格范围：¥100-800（二等座-商务座）
                • 建议：提前30分钟到站，可选择不同等级座位
                • 优势：准点率高，市中心到市中心，无需安检
                
                """, start, end);
    }
    
    private String generateDrivingPlan(String start, String end, String waypoints) {
        StringBuilder driving = new StringBuilder();
        driving.append(String.format("""
                🚗 自驾方案：
                • 路线：%s → %s
                """, start, end));
        
        if (waypoints != null && !waypoints.trim().isEmpty()) {
            driving.append("• 途经：").append(waypoints).append("\n");
        }
        
        driving.append("""
                • 预计时间：根据距离和路况而定
                • 费用：油费 + 过路费 + 停车费
                • 建议：检查车况，准备导航，了解路况
                • 优势：时间自由，可随时停靠，适合多人出行
                
                """);
        
        return driving.toString();
    }
    
    private String generatePublicTransportPlan(String start, String end) {
        return String.format("""
                🚌 公共交通方案：
                • 路线：%s → %s
                • 方式：地铁 + 公交 + 步行组合
                • 时间：通常比私家车稍长
                • 费用：最经济的选择
                • 建议：下载当地交速APP，准备零钱或交通卡
                • 优势：环保经济，无需考虑停车问题
                
                """, start, end);
    }
    
    private String generateComprehensivePlan(String start, String end) {
        return String.format("""
                🎯 综合交通建议：
                
                长距离出行（>500km）：
                • 首选：飞机（时间优先）或高铁（舒适度优先）
                • 备选：长途汽车（经济选择）
                
                中距离出行（100-500km）：
                • 首选：高铁或动车
                • 备选：自驾（多人出行）或长途汽车
                
                短距离出行（<100km）：
                • 首选：自驾或公共交通
                • 备选：出租车或网约车
                
                起点：%s
                终点：%s
                
                """, start, end);
    }
}


