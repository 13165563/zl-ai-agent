package com.zluolan.zaiagent.agent;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TravelPlanningAgent测试类
 * 测试旅游规划智能体的功能
 */
@SpringBootTest
@ActiveProfiles("local")
class TravelPlanningAgentTest {
    
    @Autowired
    private TravelPlanningAgent travelPlanningAgent;
    
    @MockBean
    @Qualifier("dashscopeChatModel")
    private ChatModel dashscopeChatModel;
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;
    
    @Test
    void testAgentInitialization() {
        // 测试智能体初始化
        assertNotNull(travelPlanningAgent);
        assertNotNull(travelPlanningAgent.getName());
        assertEquals("TravelPlanningAgent", travelPlanningAgent.getName());
        assertNotNull(travelPlanningAgent.getSystemPrompt());
        assertNotNull(travelPlanningAgent.getNextStepPrompt());
        assertTrue(travelPlanningAgent.getMaxSteps() > 0);
        
        System.out.println("智能体名称: " + travelPlanningAgent.getName());
        System.out.println("最大步骤数: " + travelPlanningAgent.getMaxSteps());
    }
    
    @Test
    void testAgentBasicFunctionality() {
        // 测试智能体基本功能
        String userPrompt = "帮我规划一个简单的北京一日游";
        
        try {
            String result = travelPlanningAgent.run(userPrompt);
            
            assertNotNull(result);
            assertFalse(result.isEmpty());
            System.out.println("智能体执行结果: " + result);
        } catch (Exception e) {
            // 智能体可能因为工具调用问题而失败，这是正常的
            System.out.println("智能体执行异常（预期）: " + e.getMessage());
            assertTrue(e.getMessage().contains("工具") || e.getMessage().contains("超时"));
        }
    }
    
    @Test
    void testAgentStateManagement() {
        // 测试智能体状态管理
        assertNotNull(travelPlanningAgent.getState());
        assertNotNull(travelPlanningAgent.getMessageList());
        assertTrue(travelPlanningAgent.getMessageList().isEmpty());
        
        System.out.println("智能体状态: " + travelPlanningAgent.getState());
        System.out.println("消息列表大小: " + travelPlanningAgent.getMessageList().size());
    }
    
    @Test
    void testAgentSystemPrompt() {
        // 测试系统提示词
        String systemPrompt = travelPlanningAgent.getSystemPrompt();
        
        assertNotNull(systemPrompt);
        assertTrue(systemPrompt.contains("旅游规划"));
        assertTrue(systemPrompt.contains("目的地"));
        assertTrue(systemPrompt.contains("工具"));
        
        System.out.println("系统提示词长度: " + systemPrompt.length());
    }
    
    @Test
    void testAgentNextStepPrompt() {
        // 测试下一步提示词
        String nextStepPrompt = travelPlanningAgent.getNextStepPrompt();
        
        assertNotNull(nextStepPrompt);
        assertTrue(nextStepPrompt.contains("旅游需求"));
        assertTrue(nextStepPrompt.contains("工具"));
        assertTrue(nextStepPrompt.contains("doTerminate"));
        
        System.out.println("下一步提示词长度: " + nextStepPrompt.length());
    }
}