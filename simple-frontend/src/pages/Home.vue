<template>
  <div class="home">
    <div class="hero">
      <h1>🌍 AI旅游规划大师</h1>
      <p>智能规划 · 个性定制 · 专业推荐 · 一站式旅游服务</p>
      <div class="buttons">
        <router-link to="/travel" class="btn primary">开始规划旅程</router-link>
        <router-link to="/agent" class="btn secondary">AI智能体助手</router-link>
        <router-link to="/tools" class="btn tertiary">旅游工具集</router-link>
      </div>
    </div>

    <div class="features">
      <h2>核心功能</h2>
      <div class="feature-grid">
        <div class="feature-item">
          <div class="icon">🏨</div>
          <h3>酒店推荐</h3>
          <p>根据预算和偏好推荐最适合的住宿选择</p>
        </div>
        <div class="feature-item">
          <div class="icon">🎯</div>
          <h3>景点搜索</h3>
          <p>智能推荐热门景点和小众目的地</p>
        </div>
        <div class="feature-item">
          <div class="icon">🛣️</div>
          <h3>路线规划</h3>
          <p>优化交通路线，节省时间和成本</p>
        </div>
        <div class="feature-item">
          <div class="icon">🌤️</div>
          <h3>天气预报</h3>
          <p>实时天气信息，为出行提供参考</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.home {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 60px 0;
  color: white;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  background: linear-gradient(45deg, #22c55e, #06b6d4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero p {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

.buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  display: inline-block;
  padding: 15px 30px;
  background: linear-gradient(45deg, #22c55e, #06b6d4);
  color: white;
  text-decoration: none;
  border-radius: 10px;
  font-weight: bold;
  transition: transform 0.2s;
}

.btn:hover {
  transform: translateY(-2px);
}

.btn.secondary {
  background: linear-gradient(45deg, #f59e0b, #ef4444);
}

.btn.tertiary {
  background: linear-gradient(45deg, #8b5cf6, #ec4899);
}

.features {
  margin-top: 80px;
}

.features h2 {
  text-align: center;
  color: white;
  font-size: 2.5rem;
  margin-bottom: 50px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-item {
  background: rgba(255, 255, 255, 0.1);
  padding: 30px;
  border-radius: 15px;
  text-align: center;
  color: white;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-item h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
}

.feature-item p {
  opacity: 0.9;
  line-height: 1.6;
}
</style>

