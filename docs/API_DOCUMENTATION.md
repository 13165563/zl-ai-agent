# API接口文档

## 📋 接口概览

AI旅游规划大师系统提供完整的REST API接口，支持同步和异步调用，集成多种AI工具和MCP服务。

### 基础信息
- **Base URL**: `http://localhost:8123`
- **Content-Type**: `application/json`
- **字符编码**: `UTF-8`

## 🔄 旅游规划接口

### 1. 同步对话接口

**接口地址**: `GET /api/travel/chat/sync`

**功能描述**: 提供同步的AI旅游规划对话服务

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| message | String | 是 | 用户输入的旅游需求 | "我想去北京旅游3天" |
| chatId | String | 是 | 会话ID，用于记忆管理 | "user-123-session-456" |

**响应示例**:
```json
{
  "content": "您好！我很乐意为您规划北京3天的旅游行程。\n\n**推荐行程安排：**\n\n**第1天：历史文化游**\n- 上午：天安门广场 → 故宫博物院\n- 下午：景山公园 → 北海公园\n- 晚上：王府井步行街\n\n**第2天：皇家园林游**\n- 上午：颐和园\n- 下午：圆明园遗址公园\n- 晚上：三里屯\n\n**第3天：现代北京游**\n- 上午：798艺术区\n- 下午：奥林匹克公园\n- 晚上：前门大街\n\n**预算建议：**\n- 住宿：300-500元/晚\n- 餐饮：150-300元/天\n- 门票：200-400元\n- 交通：100-200元\n\n**总预算：** 约2000-3500元/人"
}
```

### 2. 流式对话接口

**接口地址**: `GET /api/travel/chat/stream`

**功能描述**: 提供流式输出的AI旅游规划对话服务

**请求参数**: 同同步对话接口

**响应格式**: `text/event-stream`

**响应示例**:
```
data: 您好！我很乐意为您规划北京3天的旅游行程。

data: 

data: **推荐行程安排：**

data: 

data: **第1天：历史文化游**
data: - 上午：天安门广场 → 故宫博物院
data: - 下午：景山公园 → 北海公园
data: - 晚上：王府井步行街
```

### 3. 智能体对话接口

**接口地址**: `GET /api/travel/agent/chat`

**功能描述**: 使用AI智能体进行多步骤旅游规划

**请求参数**: 同同步对话接口

**响应示例**:
```json
{
  "content": "🤖 AI智能体正在为您进行多步骤旅游规划...\n\n**第1步：需求分析**\n✅ 目的地：北京\n✅ 时长：3天\n✅ 类型：文化历史游\n\n**第2步：景点推荐**\n🏛️ 故宫博物院 - 必游景点\n🏔️ 八达岭长城 - 世界文化遗产\n🏯 天坛公园 - 明清建筑\n\n**第3步：行程优化**\n📅 第1天：故宫+天安门\n📅 第2天：长城一日游\n📅 第3天：天坛+798艺术区\n\n**第4步：预算规划**\n💰 总预算：2500-3500元/人\n\n**规划完成！** 为您提供了一份完整的北京3天旅游计划。"
}
```

### 4. MCP工具对话接口

**接口地址**: `GET /api/travel/chat/mcp`

**功能描述**: 使用MCP（Model Context Protocol）工具进行旅游规划

**请求参数**: 同同步对话接口

**响应示例**:
```json
{
  "content": "🗺️ 使用高德地图为您规划北京旅游...\n\n**基于高德地图的推荐：**\n\n**热门景点：**\n1. 故宫博物院 - 评分4.8，距离市中心2km\n2. 八达岭长城 - 评分4.7，距离市中心60km\n3. 天坛公园 - 评分4.6，距离市中心5km\n\n**交通路线：**\n- 地铁1号线：天安门东站 → 故宫\n- 地铁2号线：积水潭站 → 德胜门 → 八达岭\n- 地铁5号线：天坛东门站 → 天坛\n\n**实时信息：**\n- 当前天气：晴，15-25°C\n- 交通状况：良好\n- 推荐出行时间：上午9-11点"
}
```

## 🛠️ 工具演示接口

### 1. 景点搜索工具

**接口地址**: `GET /api/travel/tools/attraction`

**功能描述**: 景点搜索工具演示

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 默认值 |
|--------|------|------|------|--------|
| demo | Boolean | 否 | 是否返回演示数据 | false |

**响应示例** (demo=true):
```html
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
```

### 2. 酒店搜索工具

**接口地址**: `GET /api/travel/tools/hotel`

**功能描述**: 酒店搜索工具演示

**请求参数**: 同景点搜索工具

**响应示例** (demo=true):
```html
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
```

### 3. 路线规划工具

**接口地址**: `GET /api/travel/tools/route`

**功能描述**: 路线规划工具演示

**请求参数**: 同景点搜索工具

**响应示例** (demo=true):
```html
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
```

### 4. 天气查询工具

**接口地址**: `GET /api/travel/tools/weather`

**功能描述**: 天气查询工具演示

**请求参数**: 同景点搜索工具

**响应示例** (demo=true):
```html
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
```

## 🔧 高级接口

### 1. SSE流式输出接口

**接口地址**: `GET /api/travel/chat/sse`

**功能描述**: 使用Server-Sent Events进行流式输出

**请求参数**: 同同步对话接口

**响应格式**: `text/event-stream`

**响应示例**:
```
event: message
data: {"content": "您好！我很乐意为您规划旅游行程。"}

event: message
data: {"content": "正在分析您的需求..."}

event: message
data: {"content": "为您推荐以下景点..."}

event: complete
data: {"status": "success"}
```

### 2. SseEmitter接口

**接口地址**: `GET /api/travel/chat/emitter`

**功能描述**: 使用Spring SseEmitter进行流式输出

**请求参数**: 同同步对话接口

**响应格式**: `text/event-stream`

**响应示例**:
```
data: 您好！我很乐意为您规划旅游行程。

data: 正在分析您的需求...

data: 为您推荐以下景点...
```

## 📊 响应状态码

| 状态码 | 描述 | 说明 |
|--------|------|------|
| 200 | 成功 | 请求处理成功 |
| 400 | 请求错误 | 参数错误或格式不正确 |
| 404 | 未找到 | 接口不存在 |
| 500 | 服务器错误 | 内部服务器错误 |
| 503 | 服务不可用 | AI服务暂时不可用 |

## 🔒 安全说明

### 1. 跨域配置
- 支持CORS跨域请求
- 允许的Origin: `http://localhost:3000`
- 允许的Methods: `GET, POST, PUT, DELETE, OPTIONS`

### 2. 请求限制
- 单次请求最大长度: 10MB
- 请求超时时间: 30秒
- 流式输出超时时间: 5分钟

### 3. 错误处理
- 所有错误都会返回标准HTTP状态码
- 错误信息包含在响应体中
- 支持详细的错误日志记录

## 🧪 测试接口

### 健康检查

**接口地址**: `GET /actuator/health`

**功能描述**: 检查应用健康状态

**响应示例**:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

### 应用信息

**接口地址**: `GET /actuator/info`

**功能描述**: 获取应用基本信息

**响应示例**:
```json
{
  "app": {
    "name": "AI旅游规划大师",
    "version": "1.0.0",
    "description": "基于Spring AI的智能旅游规划系统"
  }
}
```

## 📝 使用示例

### JavaScript调用示例

```javascript
// 同步对话
async function chatSync(message, chatId) {
  const response = await fetch(
    `http://localhost:8123/api/travel/chat/sync?message=${encodeURIComponent(message)}&chatId=${chatId}`
  );
  return await response.text();
}

// 流式对话
async function chatStream(message, chatId) {
  const response = await fetch(
    `http://localhost:8123/api/travel/chat/stream?message=${encodeURIComponent(message)}&chatId=${chatId}`
  );
  
  const reader = response.body.getReader();
  const decoder = new TextDecoder();
  
  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    
    const chunk = decoder.decode(value);
    console.log('Received:', chunk);
  }
}

// 工具演示
async function getToolDemo(toolType) {
  const response = await fetch(
    `http://localhost:8123/api/travel/tools/${toolType}?demo=true`
  );
  return await response.text();
}
```

### cURL调用示例

```bash
# 同步对话
curl "http://localhost:8123/api/travel/chat/sync?message=我想去北京旅游&chatId=test-123"

# 流式对话
curl "http://localhost:8123/api/travel/chat/stream?message=我想去北京旅游&chatId=test-123"

# 工具演示
curl "http://localhost:8123/api/travel/tools/attraction?demo=true"
```

这个API文档提供了完整的接口说明，包括请求参数、响应格式、使用示例等，为前端开发和系统集成提供了详细的参考。
