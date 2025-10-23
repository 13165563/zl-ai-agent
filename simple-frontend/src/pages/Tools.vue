<template>
  <div class="tools-page">
    <div class="header">
      <h1>🛠️ 旅游工具集</h1>
      <div class="actions">
        <router-link to="/" class="btn">返回首页</router-link>
      </div>
    </div>

    <div class="tools-container">
      <div class="tools-grid">
        <div class="tool-card" @click="testTool('attraction')">
          <div class="tool-icon">🎯</div>
          <h3>景点搜索工具</h3>
          <p>搜索指定目的地的旅游景点，提供详细的景点信息和推荐</p>
          <div class="tool-features">
            <span class="feature-tag">热门景点</span>
            <span class="feature-tag">小众推荐</span>
            <span class="feature-tag">详细信息</span>
          </div>
        </div>

        <div class="tool-card" @click="testTool('hotel')">
          <div class="tool-icon">🏨</div>
          <h3>酒店搜索工具</h3>
          <p>根据预算和偏好推荐最适合的住宿选择</p>
          <div class="tool-features">
            <span class="feature-tag">价格筛选</span>
            <span class="feature-tag">位置推荐</span>
            <span class="feature-tag">用户评价</span>
          </div>
        </div>

        <div class="tool-card" @click="testTool('route')">
          <div class="tool-icon">🛣️</div>
          <h3>路线规划工具</h3>
          <p>优化交通路线，节省时间和成本</p>
          <div class="tool-features">
            <span class="feature-tag">最优路径</span>
            <span class="feature-tag">多种交通</span>
            <span class="feature-tag">时间估算</span>
          </div>
        </div>

        <div class="tool-card" @click="testTool('weather')">
          <div class="tool-icon">🌤️</div>
          <h3>天气查询工具</h3>
          <p>实时天气信息，为出行提供参考</p>
          <div class="tool-features">
            <span class="feature-tag">实时天气</span>
            <span class="feature-tag">多天预报</span>
            <span class="feature-tag">出行建议</span>
          </div>
        </div>
      </div>

      <div class="demo-section">
        <h2>工具演示</h2>
        <div class="demo-area">
          <div v-if="!selectedTool" class="demo-placeholder">
            <p>点击上方工具卡片来测试功能</p>
          </div>
          <div v-else class="demo-content">
            <h3>{{ getToolTitle(selectedTool) }}</h3>
            <div class="demo-result">
              <div v-html="demoResult"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const selectedTool = ref<string>('')
const demoResult = ref('')

const testTool = async (toolType: string) => {
  selectedTool.value = toolType
  demoResult.value = '<div class="loading">正在测试工具...</div>'

  try {
    const response = await fetch(`/api/travel/tools/${toolType}?demo=true`)
    const data = await response.text()
    demoResult.value = data
  } catch (error) {
    demoResult.value = generateMockToolResult(toolType)
  }
}

const generateMockToolResult = (toolType: string): string => {
  const results = {
    attraction: `
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
    `,
    hotel: `
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
    `,
    route: `
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
    `,
    weather: `
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
    `
  }
  
  return results[toolType as keyof typeof results] || '<p>工具演示结果</p>'
}

const getToolTitle = (toolType: string): string => {
  const titles = {
    attraction: '景点搜索工具演示',
    hotel: '酒店搜索工具演示',
    route: '路线规划工具演示',
    weather: '天气查询工具演示'
  }
  return titles[toolType as keyof typeof titles] || '工具演示'
}
</script>

<style scoped>
.tools-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.header {
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  border-radius: 15px;
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.header h1 {
  color: white;
  font-size: 2rem;
  margin: 0;
}

.btn {
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s;
}

.btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.tools-container {
  max-width: 1200px;
  margin: 0 auto;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.tool-card {
  background: rgba(255, 255, 255, 0.1);
  padding: 25px;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.tool-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.tool-icon {
  font-size: 3rem;
  margin-bottom: 15px;
}

.tool-card h3 {
  color: white;
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.tool-card p {
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
  margin-bottom: 15px;
}

.tool-features {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.feature-tag {
  background: rgba(34, 197, 94, 0.2);
  color: #22c55e;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.demo-section {
  background: rgba(255, 255, 255, 0.1);
  padding: 30px;
  border-radius: 15px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.demo-section h2 {
  color: white;
  font-size: 1.8rem;
  margin-bottom: 20px;
}

.demo-area {
  min-height: 300px;
}

.demo-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 1.2rem;
}

.demo-content h3 {
  color: white;
  margin-bottom: 20px;
}

.demo-result {
  background: rgba(0, 0, 0, 0.2);
  padding: 20px;
  border-radius: 10px;
  color: white;
}

.tool-result h4 {
  color: #22c55e;
  margin-bottom: 15px;
}

.result-item {
  margin-bottom: 15px;
}

.result-item strong {
  color: #06b6d4;
}

.result-item ul {
  margin: 10px 0;
  padding-left: 20px;
}

.result-item li {
  margin: 5px 0;
}

.loading {
  text-align: center;
  color: #f59e0b;
  font-style: italic;
}
</style>
