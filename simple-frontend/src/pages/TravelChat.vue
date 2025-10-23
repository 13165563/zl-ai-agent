<template>
  <div class="travel-chat">
    <div class="header">
      <h1>🌍 AI旅游规划大师</h1>
      <div class="actions">
        <button @click="clearChat" class="btn">清空对话</button>
        <router-link to="/" class="btn">返回首页</router-link>
      </div>
    </div>

    <div class="chat-container">
      <div class="messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome">
          <h2>欢迎使用AI旅游规划大师！</h2>
          <p>我是您的专属旅游规划助手，可以帮您：</p>
          <ul>
            <li>🎯 推荐最适合的旅游目的地</li>
            <li>📅 制定详细的行程安排</li>
            <li>🏨 查找优质的酒店住宿</li>
            <li>🚗 规划最佳的交通路线</li>
            <li>🌤️ 提供实时天气信息</li>
            <li>💰 估算旅游预算费用</li>
          </ul>
          <div class="quick-start">
            <p>快速开始：</p>
            <button @click="sendMessage('我想去日本旅游，帮我规划一下')" class="quick-btn">
              🗾 日本旅游
            </button>
            <button @click="sendMessage('推荐一些国内的旅游景点')" class="quick-btn">
              🏔️ 国内景点
            </button>
            <button @click="sendMessage('帮我规划一个周末短途旅行')" class="quick-btn">
              🚗 周末短途
            </button>
            <button @click="sendMessageWithMcp('我想去北京旅游，请使用高德地图帮我规划')" class="quick-btn mcp-btn">
              🗺️ 高德地图规划
            </button>
          </div>
        </div>

        <div v-for="(message, index) in messages" :key="index" class="message" :class="message.type">
          <div class="message-content">
            <div v-if="message.type === 'user'" class="user-message">
              <div class="avatar">👤</div>
              <div class="text">{{ message.content }}</div>
            </div>
            <div v-else class="ai-message">
              <div class="avatar">🤖</div>
              <div class="text" v-html="formatMessage(message.content)"></div>
            </div>
          </div>
        </div>

        <div v-if="isLoading" class="message ai">
          <div class="message-content">
            <div class="ai-message">
              <div class="avatar">🤖</div>
              <div class="text">
                <div class="typing">AI正在思考中...</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="input-area">
        <div class="input-container">
          <input 
            v-model="inputMessage" 
            @keyup.enter="sendMessage()" 
            placeholder="请输入您的旅游需求..."
            :disabled="isLoading"
          />
          <button @click="sendMessage()" :disabled="isLoading || !inputMessage.trim()" class="send-btn">
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'

interface Message {
  type: 'user' | 'ai'
  content: string
}

const messages = ref<Message[]>([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref<HTMLElement>()

// 使用MCP工具发送消息
const sendMessageWithMcp = async (message?: string) => {
  const content = message || inputMessage.value.trim()
  if (!content) return

  // 添加用户消息
  messages.value.push({ type: 'user', content })
  inputMessage.value = ''
  isLoading.value = true

  await nextTick()
  scrollToBottom()

  try {
    // 使用MCP API
    const response = await fetch(`/api/travel/chat/mcp?message=${encodeURIComponent(content)}&chatId=${Date.now()}`)
    if (response.ok) {
      const fullResponse = await response.text()
      
      // 添加AI消息占位符
      messages.value.push({ type: 'ai', content: '' })
      const aiMessageIndex = messages.value.length - 1
      
      // 模拟流式输出效果
      let currentText = ''
      for (let i = 0; i < fullResponse.length; i++) {
        currentText += fullResponse[i]
        messages.value[aiMessageIndex].content = currentText
        await nextTick()
        scrollToBottom()
        
        // 控制输出速度，让用户看到打字效果
        await new Promise(resolve => setTimeout(resolve, 20))
      }
    } else {
      throw new Error('MCP请求失败')
    }
  } catch (error) {
    console.error('发送MCP消息失败:', error)
    messages.value.push({ type: 'ai', content: '抱歉，MCP服务遇到了一些问题，请稍后再试。' })
  } finally {
    isLoading.value = false
  }
}

const sendMessage = async (message?: string) => {
  const content = message || inputMessage.value.trim()
  if (!content) return

  // 添加用户消息
  messages.value.push({ type: 'user', content })
  inputMessage.value = ''
  isLoading.value = true

  await nextTick()
  scrollToBottom()

  try {
    // 使用同步API，然后在前端模拟流式效果
    const response = await fetch(`/api/travel/chat/sync?message=${encodeURIComponent(content)}&chatId=${Date.now()}`)
    if (response.ok) {
      const fullResponse = await response.text()
      
      // 添加AI消息占位符
      messages.value.push({ type: 'ai', content: '' })
      const aiMessageIndex = messages.value.length - 1
      
      // 模拟流式输出效果
      let currentText = ''
      for (let i = 0; i < fullResponse.length; i++) {
        currentText += fullResponse[i]
        messages.value[aiMessageIndex].content = currentText
        await nextTick()
        scrollToBottom()
        
        // 控制输出速度，让用户看到打字效果
        await new Promise(resolve => setTimeout(resolve, 20))
      }
    } else {
      throw new Error(`API调用失败: ${response.status}`)
    }
  } catch (error) {
    console.error('API调用错误:', error)
    // 如果API调用失败，显示模拟回复
    const mockResponse = generateMockResponse(content)
    messages.value.push({ type: 'ai', content: mockResponse })
  }

  isLoading.value = false
  await nextTick()
  scrollToBottom()
}

const generateMockResponse = (userMessage: string): string => {
  const responses = [
    `感谢您的咨询！关于"${userMessage}"，我为您推荐以下方案：\n\n1. **目的地推荐**：根据您的需求，我推荐几个热门目的地\n2. **行程安排**：为您制定3-5天的详细行程\n3. **住宿建议**：推荐性价比高的酒店\n4. **交通规划**：提供最优的交通路线\n\n请告诉我您的具体偏好，我会为您制定更详细的计划！`,
    `好的！我来为您规划"${userMessage}"的旅游方案。\n\n**推荐行程：**\n- 第一天：抵达目的地，入住酒店，市区游览\n- 第二天：主要景点游览\n- 第三天：深度体验当地文化\n\n**预算估算：**\n- 住宿：¥300-500/晚\n- 餐饮：¥200-400/天\n- 交通：¥100-300/天\n\n需要我为您提供更详细的信息吗？`,
    `关于"${userMessage}"，我为您提供以下建议：\n\n**热门景点：**\n- 景点1：历史文化丰富，适合拍照\n- 景点2：自然风光优美，适合放松\n- 景点3：美食聚集地，适合品尝当地特色\n\n**最佳时间：**\n- 春季（3-5月）：气候宜人，花季美景\n- 秋季（9-11月）：天气凉爽，景色优美\n\n**注意事项：**\n- 提前预订酒店和门票\n- 关注天气预报\n- 准备必要的证件\n\n还有什么具体问题吗？`
  ]
  
  return responses[Math.floor(Math.random() * responses.length)]
}

const formatMessage = (content: string): string => {
  return content.replace(/\n/g, '<br>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
}

const clearChat = () => {
  messages.value = []
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}
</script>

<style scoped>
.travel-chat {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header {
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header h1 {
  color: white;
  font-size: 1.5rem;
}

.actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s;
}

.btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 800px;
  margin: 0 auto;
  width: 100%;
  padding: 20px;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px 0;
}

.welcome {
  text-align: center;
  color: white;
  padding: 40px 20px;
}

.welcome h2 {
  font-size: 2rem;
  margin-bottom: 20px;
}

.welcome ul {
  text-align: left;
  max-width: 400px;
  margin: 20px auto;
}

.welcome li {
  margin: 10px 0;
}

.quick-start {
  margin-top: 30px;
}

.quick-btn {
  display: inline-block;
  margin: 5px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s;
}

.quick-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.mcp-btn {
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  border: 1px solid rgba(255, 255, 255, 0.5);
  font-weight: bold;
}

.mcp-btn:hover {
  background: linear-gradient(45deg, #ff5252, #26a69a);
  transform: translateY(-2px) scale(1.05);
}

.message {
  margin: 20px 0;
}

.message-content {
  display: flex;
  align-items: flex-start;
}

.user-message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  justify-content: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  flex-shrink: 0;
}

.user-message .avatar {
  background: linear-gradient(45deg, #22c55e, #06b6d4);
}

.ai-message .avatar {
  background: linear-gradient(45deg, #f59e0b, #ef4444);
}

.text {
  max-width: 70%;
  padding: 15px 20px;
  border-radius: 15px;
  color: white;
  line-height: 1.6;
}

.user-message .text {
  background: rgba(34, 197, 94, 0.2);
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.ai-message .text {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.typing {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 0.5; }
  50% { opacity: 1; }
}

.input-area {
  padding: 20px 0;
}

.input-container {
  display: flex;
  gap: 10px;
  background: rgba(255, 255, 255, 0.1);
  padding: 15px;
  border-radius: 25px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.input-container input {
  flex: 1;
  background: transparent;
  border: none;
  color: white;
  font-size: 1rem;
  outline: none;
  padding: 5px 10px;
}

.input-container input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.send-btn {
  padding: 10px 20px;
  background: linear-gradient(45deg, #22c55e, #06b6d4);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.2s;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-1px);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>

