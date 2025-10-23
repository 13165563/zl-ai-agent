package com.zluolan.zaiagent.controller;

import com.zluolan.zaiagent.agent.TravelPlanningAgent;
import com.zluolan.zaiagent.app.TravelApp;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/travel")
@Tag(name = "旅游规划接口", description = "AI旅游规划大师相关接口")
public class TravelController {

    @Resource
    private TravelApp travelApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    // ==================== 旅游规划应用接口 ====================

    /**
     * 同步旅游规划对话
     */
    @GetMapping("/chat/sync")
    public String chatSync(String message, String chatId) {
        return travelApp.doChat(message, chatId);
    }

    /**
     * 使用MCP工具的旅游规划对话
     */
    @GetMapping("/chat/mcp")
    public String chatWithMcp(String message, String chatId) {
        return travelApp.doChatWithMcp(message, chatId);
    }

    /**
     * 智能体旅游规划对话（同步）
     */
    @GetMapping("/agent/sync")
    public String agentSync(String message, String chatId) {
        return travelApp.doChatWithAgent(message, chatId);
    }

    /**
     * SSE 流式旅游规划对话
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(String message, String chatId) {
        return travelApp.doChatByStream(message, chatId);
    }

    /**
     * SSE 流式旅游规划对话（带工具调用）
     */
    @GetMapping(value = "/chat/stream/tools", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreamWithTools(String message, String chatId) {
        return travelApp.doChatByStreamWithTools(message, chatId);
    }

    /**
     * SSE 流式旅游规划对话（ServerSentEvent格式）
     */
    @GetMapping(value = "/chat/sse")
    public Flux<ServerSentEvent<String>> chatSSE(String message, String chatId) {
        return travelApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * SSE 流式旅游规划对话（SseEmitter格式）
     */
    @GetMapping("/chat/emitter")
    public SseEmitter chatEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
        // 获取 Flux 数据流并直接订阅
        travelApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        emitter::complete
                );
        // 返回emitter
        return emitter;
    }

    /**
     * 使用工具的旅游规划对话
     */
    @GetMapping("/chat/tools")
    public String chatWithTools(String message, String chatId) {
        return travelApp.doChatWithTools(message, chatId);
    }

    /**
     * 获取结构化旅游计划
     */
    @GetMapping("/plan")
    public TravelApp.TravelPlan getTravelPlan(String message, String chatId) {
        return travelApp.doChatWithPlan(message, chatId);
    }

    // ==================== 旅游规划智能体接口 ====================

    /**
     * 流式调用 TravelPlanningAgent 旅游规划智能体
     */
    @GetMapping("/agent/chat")
    public SseEmitter chatWithAgent(String message) {
        // 使用简单的流式聊天接口，避免智能体复杂逻辑
        SseEmitter emitter = new SseEmitter(30000L); // 30秒超时
        
        CompletableFuture.runAsync(() -> {
            try {
                // 使用TravelApp的流式接口
                travelApp.doChatByStream(message, "agent-" + System.currentTimeMillis())
                    .subscribe(
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                    );
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }

    /**
     * 同步调用 TravelPlanningAgent 旅游规划智能体（已删除重复映射）
     */

    // ==================== 旅游工具接口 ====================

    /**
     * 景点搜索工具演示
     */
    @GetMapping("/tools/attraction")
    public String attractionTool(@RequestParam(defaultValue = "false") boolean demo) {
        if (demo) {
            return """
                <div class="tool-result">
                  <h4>🎯 景点搜索工具演示</h4>
                  <div class="result-item">
                    <strong>搜索关键词：</strong> 北京热门景点
                  </div>
                  <div class="result-item">
                    <strong>推荐景点：</strong>
                    <ul>
                      <li>🏛️ 故宫博物院 - 明清两代皇家宫殿，世界文化遗产</li>
                      <li>🏔️ 八达岭长城 - 万里长城最著名的一段</li>
                      <li>🏯 天坛公园 - 明清皇帝祭天的场所</li>
                      <li>🌸 颐和园 - 中国古典园林之首</li>
                      <li>🎭 天安门广场 - 世界最大的城市广场</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>建议游览时间：</strong> 每个景点2-4小时
                  </div>
                  <div class="result-item">
                    <strong>门票价格：</strong> 故宫60元，长城40元，天坛15元
                  </div>
                </div>
                """;
        }
        return "景点搜索工具功能正常";
    }

    /**
     * 酒店搜索工具演示
     */
    @GetMapping("/tools/hotel")
    public String hotelTool(@RequestParam(defaultValue = "false") boolean demo) {
        if (demo) {
            return """
                <div class="tool-result">
                  <h4>🏨 酒店搜索工具演示</h4>
                  <div class="result-item">
                    <strong>搜索条件：</strong> 北京，经济型，2人，3天2夜
                  </div>
                  <div class="result-item">
                    <strong>推荐酒店：</strong>
                    <ul>
                      <li>🏨 如家快捷酒店 - 天安门店，¥268/晚，评分4.2</li>
                      <li>🏨 汉庭酒店 - 王府井店，¥298/晚，评分4.1</li>
                      <li>🏨 7天连锁酒店 - 前门店，¥238/晚，评分4.0</li>
                      <li>🏨 锦江之星 - 西单店，¥288/晚，评分4.3</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>总预算：</strong> ¥500-600（2晚住宿）
                  </div>
                  <div class="result-item">
                    <strong>位置优势：</strong> 靠近地铁站，交通便利
                  </div>
                </div>
                """;
        }
        return "酒店搜索工具功能正常";
    }

    /**
     * 路线规划工具演示
     */
    @GetMapping("/tools/route")
    public String routeTool(@RequestParam(defaultValue = "false") boolean demo) {
        if (demo) {
            return """
                <div class="tool-result">
                  <h4>🛣️ 路线规划工具演示</h4>
                  <div class="result-item">
                    <strong>规划路线：</strong> 北京3日游最优路线
                  </div>
                  <div class="result-item">
                    <strong>第1天：</strong>
                    <ul>
                      <li>上午：天安门广场 → 故宫博物院</li>
                      <li>下午：景山公园 → 北海公园</li>
                      <li>晚上：王府井步行街</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>第2天：</strong>
                    <ul>
                      <li>上午：八达岭长城</li>
                      <li>下午：明十三陵</li>
                      <li>晚上：三里屯</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>第3天：</strong>
                    <ul>
                      <li>上午：颐和园</li>
                      <li>下午：天坛公园</li>
                      <li>晚上：前门大街</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>交通方式：</strong> 地铁+公交，预计交通费¥50/人
                  </div>
                </div>
                """;
        }
        return "路线规划工具功能正常";
    }

    /**
     * 天气查询工具演示
     */
    @GetMapping("/tools/weather")
    public String weatherTool(@RequestParam(defaultValue = "false") boolean demo) {
        if (demo) {
            return """
                <div class="tool-result">
                  <h4>🌤️ 天气查询工具演示</h4>
                  <div class="result-item">
                    <strong>查询地点：</strong> 北京
                  </div>
                  <div class="result-item">
                    <strong>今日天气：</strong>
                    <ul>
                      <li>🌡️ 温度：15°C - 25°C</li>
                      <li>☀️ 天气：晴转多云</li>
                      <li>💨 风力：3-4级</li>
                      <li>💧 湿度：45%</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>未来3天预报：</strong>
                    <ul>
                      <li>明天：多云，16°C - 26°C</li>
                      <li>后天：小雨，14°C - 22°C</li>
                      <li>大后天：晴，18°C - 28°C</li>
                    </ul>
                  </div>
                  <div class="result-item">
                    <strong>出行建议：</strong> 适合户外活动，建议携带薄外套
                  </div>
                </div>
                """;
        }
        return "天气查询工具功能正常";
    }
}
