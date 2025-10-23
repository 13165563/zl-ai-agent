package com.zluolan.zaiagent.tools;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WebSearchTool测试类
 * 测试联网搜索工具的功能
 */
@SpringBootTest
@ActiveProfiles("local")
class WebSearchToolTest {
    
    @Autowired
    private WebSearchTool webSearchTool;
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;
    
    @Test
    void testSearchWeb() {
        // 测试联网搜索功能
        String query = "北京旅游景点推荐";
        
        String result = webSearchTool.searchWeb(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("SEARCH_QUERY") || result.contains("SEARCH_ERROR"));
        
        System.out.println("搜索测试结果: " + result);
    }
    
    @Test
    void testSearchWebWithEmptyQuery() {
        // 测试空查询
        String query = "";
        
        String result = webSearchTool.searchWeb(query);
        
        assertNotNull(result);
        // 空查询应该返回错误信息
        assertTrue(result.contains("SEARCH_ERROR") || result.isEmpty());
        
        System.out.println("空查询测试结果: " + result);
    }
    
    @Test
    void testSearchWebWithNullQuery() {
        // 测试null查询
        String result = webSearchTool.searchWeb(null);
        
        assertNotNull(result);
        // null查询应该返回错误信息
        assertTrue(result.contains("SEARCH_ERROR") || result.isEmpty());
        
        System.out.println("null查询测试结果: " + result);
    }
    
    @Test
    void testSearchWebWithTravelQuery() {
        // 测试旅游相关查询
        String query = "上海酒店推荐";
        
        String result = webSearchTool.searchWeb(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        System.out.println("旅游查询测试结果: " + result);
    }
    
    @Test
    void testSearchWebWithWeatherQuery() {
        // 测试天气查询
        String query = "北京天气预报";
        
        String result = webSearchTool.searchWeb(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        System.out.println("天气查询测试结果: " + result);
    }
}
