# 贡献指南

## 🤝 欢迎贡献

感谢您对AI旅游规划大师系统的关注！我们欢迎各种形式的贡献，包括代码、文档、测试、问题报告等。

## 📋 贡献方式

### 1. 报告问题
- 使用GitHub Issues报告bug
- 提供详细的问题描述和重现步骤
- 包含环境信息和错误日志

### 2. 功能建议
- 使用GitHub Issues提出新功能建议
- 描述功能的使用场景和价值
- 提供详细的功能设计

### 3. 代码贡献
- Fork项目到您的GitHub账户
- 创建功能分支
- 提交代码并创建Pull Request

## 🔧 开发环境设置

### 1. 环境要求
- Java 21+
- Node.js 18+
- Maven 3.8+
- MySQL 8.0+
- Git

### 2. 项目设置
```bash
# 克隆项目
git clone https://github.com/your-username/zl-ai-agent.git
cd zl-ai-agent

# 设置上游仓库
git remote add upstream https://github.com/original-owner/zl-ai-agent.git

# 安装后端依赖
mvn clean install

# 安装前端依赖
cd simple-frontend
pnpm install
```

### 3. 数据库设置
```sql
-- 创建数据库
CREATE DATABASE travel_planner CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'travel_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON travel_planner.* TO 'travel_user'@'localhost';
FLUSH PRIVILEGES;
```

## 📝 代码规范

### 1. Java代码规范

**命名规范**:
```java
// 类名使用PascalCase
public class TravelPlanningAgent { }

// 方法名使用camelCase
public String doChat(String message, String chatId) { }

// 常量使用UPPER_SNAKE_CASE
private static final String SYSTEM_PROMPT = "...";

// 变量名使用camelCase
private String currentMessage;
```

**注释规范**:
```java
/**
 * 执行旅游规划对话
 * 
 * @param message 用户输入的消息
 * @param chatId 会话ID
 * @return AI生成的回复
 */
public String doChat(String message, String chatId) {
    // 实现逻辑
}
```

**代码格式**:
- 使用4个空格缩进
- 行长度不超过120字符
- 使用空行分隔逻辑块
- 方法之间用空行分隔

### 2. TypeScript代码规范

**命名规范**:
```typescript
// 接口使用PascalCase
interface TravelPlan {
  destination: string;
  duration: string;
}

// 函数名使用camelCase
const sendMessage = async (message: string) => { };

// 常量使用UPPER_SNAKE_CASE
const MAX_MESSAGES = 100;
```

**类型定义**:
```typescript
// 使用接口定义数据结构
interface Message {
  id: string;
  content: string;
  timestamp: Date;
  type: 'user' | 'assistant';
}

// 使用类型别名
type ChatStatus = 'idle' | 'loading' | 'error';
```

### 3. Vue组件规范

**组件结构**:
```vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
// 导入
import { ref, computed } from 'vue';

// 接口定义
interface Props {
  message: string;
}

// 属性定义
const props = defineProps<Props>();

// 响应式数据
const loading = ref(false);

// 计算属性
const messageLength = computed(() => props.message.length);

// 方法
const handleSubmit = () => {
  // 处理逻辑
};
</script>

<style scoped>
/* 样式 */
</style>
```

## 🧪 测试规范

### 1. 单元测试

**测试命名**:
```java
// 测试方法命名：test + 方法名 + 测试场景
@Test
void testDoChatWithValidInput() { }

@Test
void testDoChatWithInvalidInput() { }

@Test
void testDoChatWithNullInput() { }
```

**测试结构**:
```java
@Test
void testMethodName() {
    // Given - 准备测试数据
    String input = "test input";
    String expected = "expected output";
    
    // When - 执行被测试的方法
    String result = methodUnderTest(input);
    
    // Then - 验证结果
    assertThat(result).isEqualTo(expected);
}
```

### 2. 集成测试

**测试配置**:
```java
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class IntegrationTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("travel_planner_test");
    
    @Test
    void testFullWorkflow() {
        // 测试完整工作流程
    }
}
```

### 3. 前端测试

**组件测试**:
```typescript
import { mount } from '@vue/test-utils';
import { describe, it, expect } from 'vitest';
import TravelChat from '@/pages/TravelChat.vue';

describe('TravelChat.vue', () => {
  it('renders correctly', () => {
    const wrapper = mount(TravelChat);
    expect(wrapper.find('h1').text()).toBe('旅游聊天');
  });
  
  it('sends message correctly', async () => {
    const wrapper = mount(TravelChat);
    await wrapper.find('input').setValue('test message');
    await wrapper.find('button').trigger('click');
    expect(wrapper.vm.messages).toHaveLength(1);
  });
});
```

## 📚 文档规范

### 1. API文档

**接口文档格式**:
```markdown
### 接口名称

**接口地址**: `GET /api/travel/chat/sync`

**功能描述**: 提供同步的AI旅游规划对话服务

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| message | String | 是 | 用户输入的旅游需求 | "我想去北京旅游3天" |
| chatId | String | 是 | 会话ID | "user-123-session-456" |

**响应示例**:
```json
{
  "content": "您好！我很乐意为您规划北京3天的旅游行程..."
}
```
```

### 2. 代码文档

**类文档**:
```java
/**
 * 旅游规划应用核心类
 * 
 * 提供AI旅游规划对话服务，包括：
 * - 同步对话
 * - 流式对话
 * - 工具调用
 * - 智能体交互
 * 
 * @author Your Name
 * @version 1.0.0
 * @since 2024-01-01
 */
public class TravelApp {
    // 类实现
}
```

**方法文档**:
```java
/**
 * 执行旅游规划对话
 * 
 * @param message 用户输入的消息，不能为null或空字符串
 * @param chatId 会话ID，用于记忆管理，不能为null
 * @return AI生成的回复，不会为null
 * @throws IllegalArgumentException 当message或chatId为null时
 * @throws RuntimeException 当AI服务调用失败时
 */
public String doChat(String message, String chatId) {
    // 方法实现
}
```

## 🔄 工作流程

### 1. 分支管理

**分支命名**:
- `feature/功能名称` - 新功能开发
- `bugfix/问题描述` - Bug修复
- `hotfix/紧急修复` - 紧急修复
- `docs/文档更新` - 文档更新

**分支创建**:
```bash
# 从main分支创建功能分支
git checkout main
git pull upstream main
git checkout -b feature/new-feature

# 推送分支到远程仓库
git push origin feature/new-feature
```

### 2. 提交规范

**提交信息格式**:
```
<类型>(<范围>): <描述>

[可选的正文]

[可选的脚注]
```

**类型说明**:
- `feat`: 新功能
- `fix`: Bug修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

**提交示例**:
```bash
# 新功能
git commit -m "feat(chat): 添加流式对话功能"

# Bug修复
git commit -m "fix(api): 修复聊天接口超时问题"

# 文档更新
git commit -m "docs(api): 更新API接口文档"
```

### 3. Pull Request流程

**PR标题格式**:
```
<类型>: <简短描述>
```

**PR描述模板**:
```markdown
## 变更描述
[描述本次变更的内容]

## 变更类型
- [ ] Bug修复
- [ ] 新功能
- [ ] 文档更新
- [ ] 代码重构
- [ ] 性能优化

## 测试
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动测试通过

## 检查清单
- [ ] 代码符合项目规范
- [ ] 添加了必要的测试
- [ ] 更新了相关文档
- [ ] 没有引入新的警告

## 相关Issue
Closes #123
```

## 🚀 发布流程

### 1. 版本号规范

**语义化版本**:
- `MAJOR.MINOR.PATCH`
- `1.0.0` - 主版本号（不兼容的API修改）
- `1.1.0` - 次版本号（向下兼容的功能性新增）
- `1.1.1` - 修订号（向下兼容的问题修正）

### 2. 发布检查清单

**代码质量**:
- [ ] 所有测试通过
- [ ] 代码覆盖率达标
- [ ] 静态代码分析通过
- [ ] 安全扫描通过

**文档更新**:
- [ ] README.md更新
- [ ] API文档更新
- [ ] 部署文档更新
- [ ] 变更日志更新

**发布准备**:
- [ ] 版本号更新
- [ ] 标签创建
- [ ] 发布说明准备
- [ ] 回滚计划准备

## 📞 获取帮助

### 1. 社区支持

**讨论渠道**:
- GitHub Discussions - 功能讨论
- GitHub Issues - 问题报告
- 邮件列表 - 技术讨论

**联系方式**:
- 项目维护者: [维护者邮箱]
- 技术问题: [技术支持邮箱]
- 商务合作: [商务邮箱]

### 2. 贡献者认可

**贡献者列表**:
- 代码贡献者
- 文档贡献者
- 测试贡献者
- 问题报告者

**贡献者权益**:
- 项目贡献者徽章
- 优先技术支持
- 新功能预览
- 社区活动邀请

## 📄 许可证

本项目采用MIT许可证，详情请参阅[LICENSE](LICENSE)文件。

## 🙏 致谢

感谢所有为项目做出贡献的开发者们！

---

如果您有任何问题或建议，请随时联系我们。我们期待您的贡献！
