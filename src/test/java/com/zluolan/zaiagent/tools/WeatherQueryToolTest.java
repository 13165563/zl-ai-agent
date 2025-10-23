package com.zluolan.zaiagent.tools;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "amap.api.key=c35d3d557f6de34f7ecae355b99d9147"
})
class WeatherQueryToolTest {
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Test
    void testWeatherQuery() {
        WeatherQueryTool weatherTool = new WeatherQueryTool();
        
        // 测试天气查询
        String result = weatherTool.queryWeather("北京", "2024-01-15", 1);
        
        assertNotNull(result);
        assertTrue(result.contains("北京"));
        assertTrue(result.contains("天气"));
        System.out.println("天气查询结果: " + result);
    }
    
    @Test
    void testWeatherQueryWithMultipleDays() {
        WeatherQueryTool weatherTool = new WeatherQueryTool();
        
        // 测试多天天气查询
        String result = weatherTool.queryWeather("上海", "2024-01-15", 3);
        
        assertNotNull(result);
        assertTrue(result.contains("上海"));
        assertTrue(result.contains("天气"));
        System.out.println("多天天气查询结果: " + result);
    }
}
