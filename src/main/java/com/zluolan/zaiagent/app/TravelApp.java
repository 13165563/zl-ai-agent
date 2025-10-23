package com.zluolan.zaiagent.app;

import com.zluolan.zaiagent.advisor.MyLoggerAdvisor;
import com.zluolan.zaiagent.agent.TravelPlanningAgent;
import com.zluolan.zaiagent.chatmemeory.FileBasedChatMemoryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Component
@Slf4j
public class TravelApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            你是一位专业的AI旅游规划大师，专门帮助用户规划各种类型的旅行。
            
            你的专业领域包括：
            - 为不同人群推荐合适的旅游目的地（包括情侣旅行、家庭旅行、商务旅行等）
            - 制定详细的行程安排和路线规划
            - 推荐景点、酒店、餐厅和交通方式
            - 提供旅游预算建议和费用估算
            - 分享旅游贴士、注意事项和当地文化信息
            
            请始终以专业、友好的态度为用户提供旅游规划服务。
            当用户询问任何旅游相关问题时，请提供详细、实用的建议。
            """;

    public TravelApp(ChatModel ollamaChatModel, ChatMemoryRepository chatMemoryRepository) {
        // 使用提供的记忆存储（可能是MySQL或内存存储）
        int MAX_MESSAGES = 15; // 旅游规划可能需要更多上下文
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(MAX_MESSAGES)
                .build();

        chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                                .build()
                )
                .build();
    }

    /**
     * 同步对话
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("TravelApp response: {}", content);
        return content;
    }

        /**
         * 结构化输出 - 旅游计划报告
         */
        public record TravelPlan(
                String destination,
                String duration,
                String budget,
                List<String> attractions,
                List<String> recommendations,
                List<String> itinerary
        ) {}

    public TravelPlan doChatWithPlan(String message, String chatId) {
        TravelPlan travelPlan = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + """
                        
                        每次对话后都要生成结构化的旅游计划，包括：
                        - destination: 目的地
                        - duration: 建议游玩时长
                        - budget: 预算建议
                        - attractions: 推荐景点列表
                        - recommendations: 旅游建议列表
                        - itinerary: 详细行程安排列表
                        """)
                .user(message)
                .advisors(spec -> spec.param(CONVERSATION_ID, chatId))
                .call()
                .entity(TravelPlan.class);
        log.info("TravelPlan: {}", travelPlan);
        return travelPlan;
    }

    /**
     * 使用工具的旅游规划
     */
    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CONVERSATION_ID, chatId))
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("TravelApp with tools response: {}", content);
        return content;
    }

    /**
     * 使用MCP工具的旅游规划
     */
    public String doChatWithMcp(String message, String chatId) {
        // 由于MCP服务被禁用，回退到普通工具调用
        log.info("MCP服务已禁用，使用普通工具调用");
        return doChatWithTools(message, chatId);
    }

    /**
     * 智能体对话 - 多步骤旅游规划
     */
    @Resource
    private TravelPlanningAgent travelPlanningAgent;

    public String doChatWithAgent(String message, String chatId) {
        try {
            log.info("开始智能体旅游规划对话 - 消息: {}, 会话ID: {}", message, chatId);
            
            // 使用智能体的run方法执行多步骤规划
            String result = travelPlanningAgent.run(message);
            log.info("智能体执行结果: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("智能体对话失败", e);
            return "抱歉，智能体遇到了一些问题，请稍后再试。错误信息: " + e.getMessage();
        }
    }

    /**
     * 流式输出
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CONVERSATION_ID, chatId))
                .stream()
                .content()
                .buffer(10) // 将更多小的chunk合并成更大的块
                .map(chunks -> String.join("", chunks))
                .filter(text -> !text.isEmpty()); // 过滤空字符串
    }

    /**
     * 流式输出（带工具调用）
     */
    public Flux<String> doChatByStreamWithTools(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CONVERSATION_ID, chatId))
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(allTools)
                .stream()
                .content()
                .buffer(10) // 将更多小的chunk合并成更大的块
                .map(chunks -> String.join("", chunks))
                .filter(text -> !text.isEmpty()); // 过滤空字符串
    }
}


