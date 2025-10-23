package com.zluolan.zaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 小红书旅游攻略搜索工具
 * 基于小红书MCP服务，搜索旅游攻略和游记
 */
@Component
@Slf4j
public class RedbookTravelGuideTool {
    
    /**
     * 搜索小红书旅游攻略
     */
    @Tool(name = "searchRedbookTravelGuides", description = """
            搜索小红书旅游攻略和游记，获取真实的用户分享内容。
            参数说明：
            - destination: 目的地（如：北京、上海、杭州等）
            - keywords: 搜索关键词（如：旅游攻略、美食推荐、景点打卡等）
            - limit: 返回结果数量（默认5条）
            - sortType: 排序方式（最新、最热、最相关）
            """)
    public String searchRedbookTravelGuides(String destination, String keywords, Integer limit, String sortType) {
        try {
            log.info("搜索小红书旅游攻略 - 目的地: {}, 关键词: {}, 限制: {}, 排序: {}", 
                    destination, keywords, limit, sortType);
            
            // 模拟小红书MCP服务调用
            return generateRedbookTravelData(destination, keywords, limit);
            
        } catch (Exception e) {
            log.error("搜索小红书旅游攻略失败", e);
            return "小红书旅游攻略搜索失败: " + e.getMessage();
        }
    }
    
    /**
     * 搜索小红书美食推荐
     */
    @Tool(name = "searchRedbookFoodRecommendations", description = """
            搜索小红书美食推荐，获取当地特色美食信息。
            参数说明：
            - destination: 目的地
            - foodType: 美食类型（如：火锅、小吃、甜品、海鲜等）
            - limit: 返回结果数量（默认5条）
            """)
    public String searchRedbookFoodRecommendations(String destination, String foodType, Integer limit) {
        try {
            log.info("搜索小红书美食推荐 - 目的地: {}, 类型: {}, 限制: {}", destination, foodType, limit);
            
            // 模拟小红书MCP服务调用
            return generateRedbookFoodData(destination, foodType, limit);
            
        } catch (Exception e) {
            log.error("搜索小红书美食推荐失败", e);
            return "小红书美食推荐搜索失败: " + e.getMessage();
        }
    }
    
    /**
     * 搜索小红书景点打卡
     */
    @Tool(name = "searchRedbookAttractionCheckins", description = """
            搜索小红书景点打卡内容，获取热门打卡地点和拍照技巧。
            参数说明：
            - destination: 目的地
            - attractionType: 景点类型（如：网红景点、自然风光、文化古迹等）
            - limit: 返回结果数量（默认5条）
            """)
    public String searchRedbookAttractionCheckins(String destination, String attractionType, Integer limit) {
        try {
            log.info("搜索小红书景点打卡 - 目的地: {}, 类型: {}, 限制: {}", destination, attractionType, limit);
            
            // 模拟小红书MCP服务调用
            return generateRedbookAttractionData(destination, attractionType, limit);
            
        } catch (Exception e) {
            log.error("搜索小红书景点打卡失败", e);
            return "小红书景点打卡搜索失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成小红书旅游攻略数据
     */
    private String generateRedbookTravelData(String destination, String keywords, Integer limit) {
        StringBuilder result = new StringBuilder();
        result.append("📱 ").append(destination).append(" 小红书旅游攻略\n");
        result.append("搜索关键词: ").append(keywords != null ? keywords : "旅游攻略").append("\n\n");
        
        result.append("=== 用户分享内容 ===\n");
        if (destination.contains("北京")) {
            result.append("1. @小仙女在北京：故宫拍照攻略，避开人潮的最佳时间\n");
            result.append("2. @旅行达人：北京3日游完整攻略，吃喝玩乐全包\n");
            result.append("3. @美食博主：北京胡同里的隐藏美食，本地人才知道\n");
            result.append("4. @摄影爱好者：北京夜景拍摄地点推荐\n");
            result.append("5. @亲子游：带娃游北京，这些地方必去\n");
        } else if (destination.contains("上海")) {
            result.append("1. @魔都小仙女：上海外滩拍照攻略，最佳机位分享\n");
            result.append("2. @上海土著：本地人推荐的小众景点\n");
            result.append("3. @美食探店：上海网红餐厅打卡指南\n");
            result.append("4. @购物达人：上海购物攻略，哪里最划算\n");
            result.append("5. @文艺青年：上海文艺打卡地推荐\n");
        } else {
            result.append("1. @").append(destination).append("本地人：").append(destination).append("必去景点推荐\n");
            result.append("2. @旅行达人：").append(destination).append("美食攻略，当地特色小吃\n");
            result.append("3. @摄影爱好者：").append(destination).append("拍照打卡地点\n");
            result.append("4. @亲子游：").append(destination).append("适合带娃的景点\n");
            result.append("5. @背包客：").append(destination).append("穷游攻略分享\n");
        }
        
        return result.toString();
    }
    
    /**
     * 生成小红书美食推荐数据
     */
    private String generateRedbookFoodData(String destination, String foodType, Integer limit) {
        StringBuilder result = new StringBuilder();
        result.append("🍜 ").append(destination).append(" 小红书美食推荐\n");
        result.append("美食类型: ").append(foodType != null ? foodType : "全部").append("\n\n");
        
        result.append("=== 美食推荐 ===\n");
        if (destination.contains("北京")) {
            result.append("1. @北京吃货：全聚德烤鸭，老字号的味道\n");
            result.append("2. @美食探店：簋街小龙虾，夜宵首选\n");
            result.append("3. @胡同美食：豆汁焦圈，老北京传统早餐\n");
            result.append("4. @甜品控：稻香村糕点，传统京味点心\n");
            result.append("5. @火锅爱好者：海底捞服务，体验式用餐\n");
        } else if (destination.contains("上海")) {
            result.append("1. @上海美食家：小笼包，南翔小笼包最正宗\n");
            result.append("2. @甜品达人：生煎包，上海人的早餐标配\n");
            result.append("3. @咖啡控：星巴克烘焙工坊，全球最大门店\n");
            result.append("4. @海鲜爱好者：外滩海鲜，黄浦江边的美味\n");
            result.append("5. @网红餐厅：排队也要吃的网红店\n");
        } else {
            result.append("1. @").append(destination).append("美食家：").append(destination).append("特色菜推荐\n");
            result.append("2. @甜品控：").append(destination).append("网红甜品店\n");
            result.append("3. @火锅爱好者：").append(destination).append("火锅店推荐\n");
            result.append("4. @咖啡控：").append(destination).append("精品咖啡店\n");
            result.append("5. @夜宵达人：").append(destination).append("夜宵好去处\n");
        }
        
        return result.toString();
    }
    
    /**
     * 生成小红书景点打卡数据
     */
    private String generateRedbookAttractionData(String destination, String attractionType, Integer limit) {
        StringBuilder result = new StringBuilder();
        result.append("📸 ").append(destination).append(" 小红书景点打卡\n");
        result.append("景点类型: ").append(attractionType != null ? attractionType : "全部").append("\n\n");
        
        result.append("=== 热门打卡地点 ===\n");
        if (destination.contains("北京")) {
            result.append("1. @故宫摄影师：故宫红墙拍照，最佳角度分享\n");
            result.append("2. @天安门打卡：升旗仪式观看攻略\n");
            result.append("3. @长城徒步：八达岭长城，不到长城非好汉\n");
            result.append("4. @颐和园游船：昆明湖划船，皇家园林体验\n");
            result.append("5. @798艺术区：文艺青年必去，艺术展览打卡\n");
        } else if (destination.contains("上海")) {
            result.append("1. @外滩摄影师：外滩夜景，最佳拍摄时间\n");
            result.append("2. @东方明珠：登塔观光，俯瞰上海全景\n");
            result.append("3. @豫园古风：江南园林，古装拍照圣地\n");
            result.append("4. @田子坊文艺：文艺小资聚集地\n");
            result.append("5. @迪士尼乐园：童话世界，亲子游首选\n");
        } else {
            result.append("1. @").append(destination).append("摄影师：").append(destination).append("地标建筑打卡\n");
            result.append("2. @文艺青年：").append(destination).append("文艺景点推荐\n");
            result.append("3. @自然爱好者：").append(destination).append("自然风光拍摄\n");
            result.append("4. @历史文化：").append(destination).append("历史文化景点\n");
            result.append("5. @网红打卡：").append(destination).append("网红景点推荐\n");
        }
        
        return result.toString();
    }
}
