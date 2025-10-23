package com.zluolan.zaiagent.mcp;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AmapMcpToolWrapper测试类
 * 测试高德地图MCP工具包装器的功能
 */
@SpringBootTest
@ActiveProfiles("local")
class AmapMcpToolWrapperTest {
    
    @Autowired
    private AmapMcpToolWrapper amapWrapper;
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;
    
    @Test
    void testSearchAttractions() {
        // 测试景点搜索功能
        String destination = "北京";
        String category = "文化古迹";
        Integer limit = 5;
        
        String result = amapWrapper.searchAttractions(destination, category, limit);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("北京"));
        assertTrue(result.contains("景点"));
        
        System.out.println("景点搜索测试结果: " + result);
    }
    
    @Test
    void testSearchHotels() {
        // 测试酒店搜索功能
        String destination = "上海";
        String checkInDate = "2024-12-01";
        String checkOutDate = "2024-12-03";
        Integer guests = 2;
        String priceRange = "舒适型";
        String hotelType = "商务酒店";
        
        String result = amapWrapper.searchHotels(destination, checkInDate, checkOutDate, guests, priceRange, hotelType);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("上海"));
        assertTrue(result.contains("酒店"));
        
        System.out.println("酒店搜索测试结果: " + result);
    }
    
    @Test
    void testPlanRoute() {
        // 测试路线规划功能
        String startLocation = "北京站";
        String endLocation = "天安门广场";
        String waypoints = null;
        String transportMode = "公交";
        String travelDate = "2024-12-01";
        String preferences = "最快";
        
        String result = amapWrapper.planRoute(startLocation, endLocation, waypoints, transportMode, travelDate, preferences);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("北京站"));
        assertTrue(result.contains("天安门广场"));
        
        System.out.println("路线规划测试结果: " + result);
    }
    
    @Test
    void testQueryWeather() {
        // 测试天气查询功能
        String location = "北京";
        String date = "2024-12-01";
        Integer days = 3;
        
        String result = amapWrapper.queryWeather(location, date, days);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("北京"));
        assertTrue(result.contains("天气"));
        
        System.out.println("天气查询测试结果: " + result);
    }
    
    @Test
    void testSearchAttractionsWithNullParameters() {
        // 测试空参数处理
        String result = amapWrapper.searchAttractions(null, null, null);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        System.out.println("空参数景点搜索测试结果: " + result);
    }
    
    @Test
    void testSearchHotelsWithEmptyParameters() {
        // 测试空字符串参数处理
        String result = amapWrapper.searchHotels("", "", "", 0, "", "");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        System.out.println("空字符串酒店搜索测试结果: " + result);
    }
}