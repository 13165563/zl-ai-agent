package com.zluolan.zaiagent.tools;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttractionSearchToolTest {
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Test
    void testSearchAttractions() {
        AttractionSearchTool attractionTool = new AttractionSearchTool();
        
        // 测试景点搜索
        String result = attractionTool.searchAttractions("北京", "文化历史", 5);
        
        assertNotNull(result);
        assertTrue(result.contains("北京"));
        System.out.println("景点搜索结果: " + result);
    }
    
    @Test
    void testSearchAttractionsWithDifferentTypes() {
        AttractionSearchTool attractionTool = new AttractionSearchTool();
        
        // 测试不同类型的景点搜索
        String result1 = attractionTool.searchAttractions("上海", "现代都市", 3);
        String result2 = attractionTool.searchAttractions("杭州", "自然风光", 4);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.contains("上海"));
        assertTrue(result2.contains("杭州"));
        
        System.out.println("上海现代都市景点: " + result1);
        System.out.println("杭州自然风光景点: " + result2);
    }
}
