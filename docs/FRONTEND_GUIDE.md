# 前端开发指南

## 🎯 项目概述

AI旅游规划大师前端采用Vue 3 + TypeScript + Vite技术栈，提供现代化的用户界面和流畅的用户体验。

## 🏗️ 技术栈

- **Vue 3.4.0** - 渐进式JavaScript框架
- **TypeScript 5.3.0** - 类型安全的JavaScript超集
- **Vite 5.0.0** - 快速的前端构建工具
- **Vue Router 4.2.0** - Vue.js官方路由管理器
- **pnpm** - 高效的包管理器

## 📁 项目结构

```
simple-frontend/
├── index.html                 # HTML入口文件
├── package.json              # 项目配置和依赖
├── vite.config.ts            # Vite配置文件
├── tsconfig.json             # TypeScript配置
└── src/
    ├── main.ts               # 应用入口文件
    ├── App.vue               # 根组件
    ├── pages/                # 页面组件
    │   ├── Home.vue          # 主页
    │   ├── TravelChat.vue    # 旅游聊天页
    │   ├── TravelAgent.vue   # 智能体助手页
    │   └── Tools.vue         # 旅游工具集页
    ├── composables/          # 组合式函数
    │   └── useSEO.ts         # SEO管理
    └── styles/               # 样式文件
        └── global.css        # 全局样式
```

## 🎨 页面组件详解

### 1. 主页 (Home.vue)

**功能**: 系统入口页面，提供四卡片导航

**特性**:
- 响应式设计
- 统一的设计风格
- 快速功能入口

**核心代码结构**:
```vue
<template>
  <div class="home">
    <div class="hero">
      <h1>AI旅游规划大师</h1>
      <p>智能规划您的完美旅程</p>
    </div>
    
    <div class="cards">
      <div class="card" @click="navigateTo('/travel')">
        <h3>🗺️ 旅游聊天</h3>
        <p>AI智能旅游规划对话</p>
      </div>
      
      <div class="card" @click="navigateTo('/agent')">
        <h3>🤖 智能体助手</h3>
        <p>多步骤智能旅游规划</p>
      </div>
      
      <div class="card" @click="navigateTo('/tools')">
        <h3>🛠️ 旅游工具集</h3>
        <p>景点、酒店、路线、天气工具</p>
      </div>
      
      <div class="card" @click="navigateTo('/about')">
        <h3>ℹ️ 关于系统</h3>
        <p>了解更多系统信息</p>
      </div>
    </div>
  </div>
</template>
```

### 2. 旅游聊天页 (TravelChat.vue)

**功能**: AI旅游规划对话界面

**特性**:
- 流式对话显示
- 消息历史管理
- 快速开始按钮
- 用户头像右对齐

**核心功能**:
```typescript
// 发送消息
const sendMessage = async (message: string) => {
  const response = await fetch(
    `/api/travel/chat/sync?message=${encodeURIComponent(message)}&chatId=${chatId}`
  );
  const content = await response.text();
  
  // 模拟流式输出
  simulateStreaming(content);
};

// 流式输出模拟
const simulateStreaming = (content: string) => {
  let index = 0;
  const interval = setInterval(() => {
    if (index < content.length) {
      setCurrentMessage(prev => prev + content[index]);
      index++;
    } else {
      clearInterval(interval);
      setCurrentMessage('');
      addMessage('assistant', content);
    }
  }, 20);
};
```

### 3. 智能体助手页 (TravelAgent.vue)

**功能**: AI智能体多步骤旅游规划

**特性**:
- 智能体交互
- 安全过滤器处理
- 浪漫推荐功能
- 多步骤规划展示

**核心功能**:
```typescript
// 智能体对话
const sendMessageWithAgent = async (message: string) => {
  setCurrentMessage('AI智能体正在思考中...');
  
  const response = await fetch(
    `/api/travel/agent/chat?message=${encodeURIComponent(message)}&chatId=${chatId}`
  );
  const content = await response.text();
  
  // 检测AI安全过滤器
  if (isSafetyFilterRejected(content)) {
    showRomanticDestinations();
  } else {
    simulateStreaming(content);
  }
};

// 安全过滤器检测
const isSafetyFilterRejected = (content: string) => {
  const rejectionKeywords = ['cannot fulfill', 'safety guidelines', 'inappropriate'];
  return rejectionKeywords.some(keyword => content.toLowerCase().includes(keyword));
};
```

### 4. 旅游工具集页 (Tools.vue)

**功能**: AI工具功能演示

**特性**:
- 工具功能展示
- 实时API调用
- 结果可视化
- 工具说明

**核心功能**:
```typescript
// 测试工具
const testTool = async (toolType: string) => {
  setLoading(true);
  try {
    const response = await fetch(`/api/travel/tools/${toolType}?demo=true`);
    const result = await response.text();
    setToolResult(result);
  } catch (error) {
    setToolResult(`<div class="error">工具测试失败: ${error.message}</div>`);
  } finally {
    setLoading(false);
  }
};
```

## 🎨 样式系统

### 全局样式 (global.css)

```css
/* 基础样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

/* 卡片样式 */
.card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

/* 聊天界面样式 */
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
}

.message {
  display: flex;
  margin: 1rem;
  align-items: flex-start;
}

.user-message {
  flex-direction: row-reverse;
  justify-content: flex-start;
}

.assistant-message {
  flex-direction: row;
  justify-content: flex-start;
}
```

## 🔧 组合式函数

### SEO管理 (useSEO.ts)

```typescript
import { ref, watch } from 'vue';

export function useSEO() {
  const title = ref('AI旅游规划大师');
  const description = ref('智能规划您的完美旅程');
  const keywords = ref('AI旅游,旅游规划,智能助手,旅游推荐');

  const updateSEO = (newTitle: string, newDescription?: string, newKeywords?: string) => {
    title.value = newTitle;
    if (newDescription) description.value = newDescription;
    if (newKeywords) keywords.value = newKeywords;
    
    // 更新页面标题
    document.title = title.value;
    
    // 更新meta标签
    updateMetaTag('description', description.value);
    updateMetaTag('keywords', keywords.value);
  };

  const updateMetaTag = (name: string, content: string) => {
    let meta = document.querySelector(`meta[name="${name}"]`);
    if (!meta) {
      meta = document.createElement('meta');
      meta.setAttribute('name', name);
      document.head.appendChild(meta);
    }
    meta.setAttribute('content', content);
  };

  return {
    title,
    description,
    keywords,
    updateSEO
  };
}
```

## 🚀 开发指南

### 1. 环境准备

```bash
# 安装Node.js 18+
# 安装pnpm
npm install -g pnpm

# 进入项目目录
cd simple-frontend

# 安装依赖
pnpm install
```

### 2. 开发命令

```bash
# 启动开发服务器
pnpm dev

# 构建生产版本
pnpm build

# 预览生产版本
pnpm preview

# 类型检查
pnpm type-check
```

### 3. 项目配置

**vite.config.ts**:
```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8123',
        changeOrigin: true,
      },
    },
  },
})
```

**tsconfig.json**:
```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "preserve",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true
  },
  "include": ["src/**/*.ts", "src/**/*.d.ts", "src/**/*.tsx", "src/**/*.vue"],
  "references": [{ "path": "./tsconfig.node.json" }]
}
```

## 🎯 最佳实践

### 1. 组件设计原则

- **单一职责**: 每个组件只负责一个功能
- **可复用性**: 设计通用的可复用组件
- **类型安全**: 使用TypeScript确保类型安全
- **响应式设计**: 支持不同屏幕尺寸

### 2. 状态管理

```typescript
// 使用ref管理简单状态
const messages = ref<Message[]>([]);
const loading = ref(false);

// 使用reactive管理复杂状态
const state = reactive({
  currentMessage: '',
  chatId: generateChatId(),
  isStreaming: false
});
```

### 3. 错误处理

```typescript
const handleError = (error: Error) => {
  console.error('Error:', error);
  setToolResult(`<div class="error">操作失败: ${error.message}</div>`);
};
```

### 4. 性能优化

- 使用`v-show`和`v-if`合理控制DOM渲染
- 使用`computed`缓存计算结果
- 使用`watch`监听数据变化
- 合理使用`key`属性

## 🧪 测试

### 1. 单元测试

```bash
# 安装测试依赖
pnpm add -D vitest @vue/test-utils

# 运行测试
pnpm test
```

### 2. 组件测试示例

```typescript
import { mount } from '@vue/test-utils';
import Home from '@/pages/Home.vue';

describe('Home.vue', () => {
  it('renders correctly', () => {
    const wrapper = mount(Home);
    expect(wrapper.find('h1').text()).toBe('AI旅游规划大师');
  });
});
```

## 📱 响应式设计

### 断点设置

```css
/* 移动端 */
@media (max-width: 768px) {
  .cards {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}

/* 平板端 */
@media (min-width: 769px) and (max-width: 1024px) {
  .cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* 桌面端 */
@media (min-width: 1025px) {
  .cards {
    grid-template-columns: repeat(4, 1fr);
  }
}
```

## 🔧 调试技巧

### 1. Vue DevTools

安装Vue DevTools浏览器扩展，可以：
- 查看组件状态
- 调试Vuex/Pinia状态
- 性能分析
- 时间旅行调试

### 2. 控制台调试

```typescript
// 开发环境调试
if (import.meta.env.DEV) {
  console.log('Debug info:', { messages, loading });
}
```

### 3. 网络请求调试

```typescript
// 添加请求日志
const fetchWithLogging = async (url: string, options?: RequestInit) => {
  console.log('Request:', { url, options });
  const response = await fetch(url, options);
  console.log('Response:', { status: response.status, ok: response.ok });
  return response;
};
```

这个前端开发指南提供了完整的开发指导，包括项目结构、组件设计、样式系统、最佳实践等，为前端开发提供了详细的参考。
