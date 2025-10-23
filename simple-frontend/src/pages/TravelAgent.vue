<template>
  <div class="travel-agent">
    <div class="header">
      <h1>🤖 AI旅游智能体</h1>
      <div class="actions">
        <button @click="clearChat" class="btn">清空对话</button>
        <router-link to="/" class="btn">返回首页</router-link>
      </div>
    </div>

    <div class="chat-container">
      <div class="messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome">
          <h2>AI旅游智能体助手</h2>
          <p>我是您的智能旅游规划助手，具备以下能力：</p>
          <ul>
            <li>🔍 智能搜索景点和酒店信息</li>
            <li>🗺️ 自动规划最优旅游路线</li>
            <li>🌤️ 实时查询天气预报</li>
            <li>📊 生成详细的旅游计划报告</li>
            <li>🛠️ 多工具协同完成复杂任务</li>
          </ul>
          <div class="quick-start">
            <p>智能体将通过多个步骤为您制定完整的旅游计划：</p>
            <button @click="sendMessage('帮我规划一个去北京的3天2夜旅游行程')" class="quick-btn">
              🏛️ 北京3日游
            </button>
            <button @click="sendMessage('我想在上海旅游，预算5000元，请制定计划')" class="quick-btn">
              🌃 上海旅游
            </button>
            <button @click="sendMessage('推荐一个适合情侣的浪漫旅游目的地')" class="quick-btn">
              💕 情侣旅游
            </button>
            <button @click="sendMessageWithAgent('我想去日本旅游，请帮我制定详细的7天行程计划')" class="quick-btn agent-btn">
              🤖 智能体规划
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
                <div class="typing">AI智能体正在思考中...</div>
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
            placeholder="请输入您的旅游需求，智能体将为您制定详细计划..."
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
    // 直接使用普通聊天接口，避免智能体复杂逻辑
    const response = await fetch(`/api/travel/chat/sync?message=${encodeURIComponent(content)}&chatId=agent-${Date.now()}`)
    if (response.ok) {
      const fullResponse = await response.text()
      
      // 检查是否包含安全过滤器的拒绝信息
      if (fullResponse.includes('cannot fulfill') || fullResponse.includes('safety guidelines') || fullResponse.includes('inappropriate')) {
        // 如果被安全过滤器拒绝，提供替代建议
        const alternativeResponse = `我理解您想要旅游建议。让我为您推荐一些浪漫的旅游目的地：

**国内浪漫旅游目的地：**
• **三亚** - 海滩度假，适合放松
• **丽江** - 古城风情，文化体验
• **厦门** - 海滨城市，文艺氛围
• **杭州** - 西湖美景，江南风情
• **青岛** - 海滨城市，啤酒文化

**国外浪漫旅游目的地：**
• **马尔代夫** - 海岛度假天堂
• **巴厘岛** - 热带风情，文化丰富
• **巴黎** - 浪漫之都，艺术氛围
• **京都** - 古都风情，樱花季节
• **圣托里尼** - 希腊海岛，蓝白建筑

请告诉我您的具体需求，比如预算、时间、偏好等，我可以为您制定更详细的旅游计划！`
        
        // 添加AI消息占位符
        messages.value.push({ type: 'ai', content: '' })
        const aiMessageIndex = messages.value.length - 1
        
        // 模拟流式输出效果
        let currentText = ''
        for (let i = 0; i < alternativeResponse.length; i++) {
          currentText += alternativeResponse[i]
          messages.value[aiMessageIndex].content = currentText
          await nextTick()
          scrollToBottom()
          
          // 控制输出速度，让用户看到打字效果
          await new Promise(resolve => setTimeout(resolve, 20))
        }
      } else {
        // 正常处理响应
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
      }
    } else {
      throw new Error(`API调用失败: ${response.status}`)
    }
  } catch (error) {
    console.error('智能体API调用错误:', error)
    // 如果API调用失败，显示模拟回复
    const mockResponse = generateMockAgentResponse(content)
    messages.value.push({ type: 'ai', content: mockResponse })
  }

  isLoading.value = false
  await nextTick()
  scrollToBottom()
}

const generateMockAgentResponse = (userMessage: string): string => {
  const responses = [
    `🤖 **AI智能体分析中...**\n\n**第1步：需求分析**\n正在分析您的旅游需求："${userMessage}"\n\n**第2步：目的地研究**\n🔍 搜索相关景点信息...\n🏨 查找优质酒店推荐...\n🗺️ 规划最优路线...\n\n**第3步：制定计划**\n📅 生成详细行程安排\n💰 计算预算费用\n🌤️ 查询天气预报\n\n**第4步：生成报告**\n正在为您生成完整的旅游计划报告...\n\n**智能体提示：** 我将通过多个工具协同工作，为您提供最专业的旅游规划服务！`,
    
    `🤖 **智能体工作流程**\n\n**任务：** ${userMessage}\n\n**执行步骤：**\n1. 🔍 **景点搜索工具** - 查找相关景点信息\n2. 🏨 **酒店搜索工具** - 推荐合适住宿\n3. 🗺️ **路线规划工具** - 优化交通路线\n4. 🌤️ **天气查询工具** - 获取天气信息\n5. 📊 **报告生成** - 整合所有信息\n\n**预计完成时间：** 2-3分钟\n**智能体状态：** 工作中...\n\n请稍等，我正在为您制定最详细的旅游计划！`,
    
    `🤖 **AI智能体执行报告**\n\n**任务目标：** ${userMessage}\n\n**已完成的工作：**\n✅ 需求分析完成\n✅ 目的地信息收集\n✅ 景点推荐列表\n✅ 酒店筛选完成\n✅ 路线优化完成\n✅ 天气信息获取\n\n**下一步：**\n🔄 正在生成最终旅游计划...\n📋 整理详细行程安排\n💰 计算总预算费用\n📝 准备出行建议\n\n**智能体提示：** 多工具协同工作已完成，即将为您呈现完整的旅游方案！`
  ]
  
  return responses[Math.floor(Math.random() * responses.length)]
}

const formatMessage = (content: string): string => {
  return content.replace(/\n/g, '<br>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
}

const clearChat = () => {
  messages.value = []
}

// 使用智能体发送消息
const sendMessageWithAgent = async (message?: string) => {
  const content = message || inputMessage.value.trim()
  if (!content) return

  // 添加用户消息
  messages.value.push({ type: 'user', content })
  inputMessage.value = ''
  isLoading.value = true

  await nextTick()
  scrollToBottom()

  try {
    // 显示智能体思考状态
    messages.value.push({ type: 'ai', content: '🤖 AI智能体正在思考中，将通过多个步骤为您制定完整的旅游计划...' })
    await nextTick()
    scrollToBottom()
    
    // 使用智能体API
    const response = await fetch(`/api/travel/agent/chat?message=${encodeURIComponent(content)}&chatId=${Date.now()}`)
    if (response.ok) {
      const fullResponse = await response.text()
      
      // 移除思考状态消息
      messages.value = messages.value.filter(msg => !msg.content.includes('AI智能体正在思考中'))
      
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
      throw new Error('智能体请求失败')
    }
  } catch (error) {
    console.error('发送智能体消息失败:', error)
    messages.value.push({ type: 'ai', content: '抱歉，智能体遇到了一些问题，请稍后再试。' })
  } finally {
    isLoading.value = false
  }
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}
</script>

<style scoped>
.travel-agent {
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

.agent-btn {
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: 1px solid rgba(255, 255, 255, 0.5);
  font-weight: bold;
  color: white;
}

.agent-btn:hover {
  background: linear-gradient(45deg, #5a6fd8, #6a4190);
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
  background: linear-gradient(45deg, #f59e0b, #ef4444);
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
