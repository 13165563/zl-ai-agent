package com.zluolan.zaiagent.tools;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelSearchToolTest {
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Test
    void testSearchHotels() {
        HotelSearchTool hotelTool = new HotelSearchTool();
        
        // 测试酒店搜索
        String result = hotelTool.searchHotels("北京", "2024-01-15", "2024-01-17", 2, "中等", "星级酒店");
        
        assertNotNull(result);
        assertTrue(result.contains("北京"));
        System.out.println("酒店搜索结果: " + result);
    }
    
    @Test
    void testSearchHotelsWithDifferentTypes() {
        HotelSearchTool hotelTool = new HotelSearchTool();
        
        // 测试不同类型的酒店搜索
        String result1 = hotelTool.searchHotels("上海", "2024-02-01", "2024-02-03", 1, "经济型", "民宿/客栈");
        String result2 = hotelTool.searchHotels("杭州", "2024-03-01", "2024-03-05", 2, "高端", "度假村");
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.contains("上海"));
        assertTrue(result2.contains("杭州"));
        
        System.out.println("上海经济型民宿: " + result1);
        System.out.println("杭州高端度假村: " + result2);
    }
}
