package com.zluolan.zaiagent.tools;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 景点搜索工具
 * 用于搜索指定城市或地区的旅游景点信息
 */
@Component
@Slf4j
public class AttractionSearchTool {

    @Tool(name = "searchAttractions", description = """
            搜索指定目的地的旅游景点信息。
            参数说明：
            - destination: 目的地名称（如：北京、上海、巴黎等）
            - category: 景点类型（可选：文化古迹、自然风光、主题乐园、博物馆、购物、美食等）
            - limit: 返回结果数量限制（默认10个）
            """)
    public String searchAttractions(String destination, String category, Integer limit) {
        try {
            log.info("搜索景点信息 - 目的地: {}, 类型: {}, 限制: {}", destination, category, limit);
            
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            
            // 构建搜索查询
            StringBuilder query = new StringBuilder();
            query.append(destination).append(" 旅游景点");
            if (category != null && !category.trim().isEmpty()) {
                query.append(" ").append(category);
            }
            query.append(" 推荐 攻略");
            
            // 模拟景点搜索结果（实际项目中可以接入真实的旅游API）
            JSONObject result = new JSONObject();
            result.set("destination", destination);
            result.set("category", category);
            result.set("searchQuery", query.toString());
            
            // 根据目的地返回模拟数据
            String attractions = generateAttractionData(destination, category, limit);
            result.set("attractions", attractions);
            result.set("searchTime", System.currentTimeMillis());
            result.set("status", "success");
            
            log.info("景点搜索完成: {}", result.toString());
            return "[TOOL_EXECUTION_RESULT] 景点搜索成功！\n" + result.toString();
            
        } catch (Exception e) {
            log.error("景点搜索失败", e);
            return "[TOOL_EXECUTION_RESULT] 景点搜索失败: " + e.getMessage();
        }
    }
    
    /**
     * 根据目的地生成景点数据（模拟数据，实际应用中应该调用真实API）
     */
    private String generateAttractionData(String destination, String category, int limit) {
        StringBuilder attractions = new StringBuilder();
        
        // 根据不同目的地提供不同的景点信息
        if (destination.contains("北京")) {
            attractions.append("1. 故宫博物院 - 明清皇家宫殿，世界文化遗产\n");
            attractions.append("2. 天安门广场 - 世界最大的城市广场\n");
            attractions.append("3. 长城（八达岭段） - 世界七大奇迹之一\n");
            attractions.append("4. 颐和园 - 清朝皇家园林\n");
            attractions.append("5. 天坛公园 - 明清皇帝祭天的场所\n");
            if (limit > 5) {
                attractions.append("6. 圆明园 - 清朝皇家园林遗址\n");
                attractions.append("7. 北海公园 - 中国现存最古老的皇家园林\n");
                attractions.append("8. 雍和宫 - 北京最大的藏传佛教寺院\n");
            }
        } else if (destination.contains("上海")) {
            attractions.append("1. 外滩 - 上海标志性景观带\n");
            attractions.append("2. 东方明珠塔 - 上海地标建筑\n");
            attractions.append("3. 豫园 - 明代私人花园\n");
            attractions.append("4. 南京路步行街 - 中华商业第一街\n");
            attractions.append("5. 田子坊 - 文艺创意园区\n");
            if (limit > 5) {
                attractions.append("6. 上海迪士尼乐园 - 世界级主题乐园\n");
                attractions.append("7. 朱家角古镇 - 江南水乡古镇\n");
                attractions.append("8. 上海博物馆 - 中国古代艺术博物馆\n");
            }
        } else if (destination.contains("杭州")) {
            attractions.append("1. 西湖 - 世界文化遗产，人间天堂\n");
            attractions.append("2. 灵隐寺 - 江南著名古刹\n");
            attractions.append("3. 千岛湖 - 国家级风景名胜区\n");
            attractions.append("4. 宋城 - 大型历史文化主题公园\n");
            attractions.append("5. 西溪湿地 - 国家湿地公园\n");
        } else {
            // 通用景点信息
            attractions.append("1. ").append(destination).append("市中心/老城区 - 体验当地文化和历史\n");
            attractions.append("2. ").append(destination).append("博物馆 - 了解当地历史文化\n");
            attractions.append("3. ").append(destination).append("公园/广场 - 休闲散步的好去处\n");
            attractions.append("4. 当地特色街区 - 购物和品尝美食\n");
            attractions.append("5. ").append(destination).append("地标建筑 - 拍照留念\n");
        }
        
        // 根据类别过滤
        if (category != null && !category.trim().isEmpty()) {
            attractions.append("\n特别推荐（").append(category).append("类）：\n");
            if (category.contains("文化") || category.contains("古迹")) {
                attractions.append("- 重点关注历史文化景点和古建筑\n");
            } else if (category.contains("自然") || category.contains("风光")) {
                attractions.append("- 重点关注自然景观和户外活动\n");
            } else if (category.contains("美食")) {
                attractions.append("- 推荐当地特色餐厅和小吃街\n");
            }
        }
        
        return attractions.toString();
    }
}


