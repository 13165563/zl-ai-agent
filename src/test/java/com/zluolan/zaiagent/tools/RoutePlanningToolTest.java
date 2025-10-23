package com.zluolan.zaiagent.tools;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoutePlanningToolTest {
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Test
    void testPlanRoute() {
        RoutePlanningTool routeTool = new RoutePlanningTool();
        
        // 测试路线规划
        String result = routeTool.planRoute("北京", "上海", "天津,济南", "高铁", "2024-01-15", "最快路线");
        
        assertNotNull(result);
        assertTrue(result.contains("北京"));
        assertTrue(result.contains("上海"));
        System.out.println("路线规划结果: " + result);
    }
    
    @Test
    void testPlanRouteWithDifferentModes() {
        RoutePlanningTool routeTool = new RoutePlanningTool();
        
        // 测试不同交通方式的路线规划
        String result1 = routeTool.planRoute("北京", "广州", "", "飞机", "2024-02-01", "最省钱");
        String result2 = routeTool.planRoute("上海", "杭州", "苏州", "自驾", "2024-03-01", "风景优美");
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.contains("北京"));
        assertTrue(result2.contains("上海"));
        
        System.out.println("北京到广州飞机路线: " + result1);
        System.out.println("上海到杭州自驾路线: " + result2);
    }
}
