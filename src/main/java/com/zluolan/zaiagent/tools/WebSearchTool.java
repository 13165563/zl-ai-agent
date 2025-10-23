package com.zluolan.zaiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 联网搜索工具
 * 参考z-ai-agent项目的WebSearchTool，使用SearchAPI进行百度搜索
 */
@Component
public class WebSearchTool {

    // SearchAPI 的搜索接口地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    @Value("${search-api.api-key}")
    private String apiKey;

    public WebSearchTool() {
    }
    
    // 带参数的构造器（供手动创建实例使用）
    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "使用百度搜索引擎搜索实时旅游信息，包括景点、酒店、交通、天气等")
    public String searchWeb(
            @ToolParam(description = "搜索查询关键词，如：北京旅游攻略、上海酒店推荐、杭州天气等") String query) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");
        try {
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);

            // 解析返回结果
            JSONObject jsonObject = JSONUtil.parseObj(response);

            // 检查是否存在错误信息
            if (jsonObject.containsKey("error")) {
                return "[TOOL_EXECUTION_RESULT][SEARCH_ERROR] Search API error: " + jsonObject.getStr("error");
            }

            // 检查搜索结果是否为空
            if (!jsonObject.containsKey("organic_results")) {
                return "[TOOL_EXECUTION_RESULT][SEARCH_ERROR] No search results found. Full response: " + response;
            }

            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            if (organicResults == null || organicResults.isEmpty()) {
                return "[TOOL_EXECUTION_RESULT][SEARCH_ERROR] No search results found.";
            }

            // 取出返回结果的前 5 条
            int resultCount = Math.min(5, organicResults.size());
            JSONArray topResults = new JSONArray();
            for (int i = 0; i < resultCount; i++) {
                topResults.add(organicResults.get(i));
            }

            // 格式化结果
            StringBuilder result = new StringBuilder("[TOOL_EXECUTION_RESULT][SEARCH_SUCCESS]\n");
            result.append("[SEARCH_QUERY]: ").append(query).append("\n");
            result.append("[SEARCH_RESULTS]:\n");
            for (int i = 0; i < topResults.size(); i++) {
                if (i > 0) result.append("\n---\n");
                JSONObject item = topResults.getJSONObject(i);
                result.append("Title: ").append(item.getStr("title", "N/A")).append("\n");
                result.append("Link: ").append(item.getStr("link", "N/A")).append("\n");
                result.append("Snippet: ").append(item.getStr("snippet", "N/A"));
            }

            return result.toString();
        } catch (Exception e) {
            return "[TOOL_EXECUTION_RESULT][SEARCH_ERROR] Error searching Baidu: " + e.getMessage();
        }
    }
}
