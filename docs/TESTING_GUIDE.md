# 测试指南

## 🧪 测试概览

本指南提供了AI旅游规划大师系统的完整测试方案，包括单元测试、集成测试、端到端测试和性能测试。

## 📋 测试类型

### 1. 单元测试 (Unit Tests)
- **目标**: 测试单个组件或方法的功能
- **工具**: JUnit 5, Mockito, Spring Boot Test
- **覆盖率**: 80%+

### 2. 集成测试 (Integration Tests)
- **目标**: 测试组件间的交互
- **工具**: Spring Boot Test, TestContainers
- **范围**: API接口、数据库交互、外部服务

### 3. 端到端测试 (E2E Tests)
- **目标**: 测试完整的用户流程
- **工具**: Playwright, Cypress
- **场景**: 用户注册、旅游规划、支付流程

### 4. 性能测试 (Performance Tests)
- **目标**: 测试系统性能和负载能力
- **工具**: JMeter, Gatling
- **指标**: 响应时间、吞吐量、并发用户数

## 🔧 测试环境设置

### 1. 依赖配置

**pom.xml (测试依赖)**:
```xml
<dependencies>
    <!-- 测试框架 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- TestContainers -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**package.json (前端测试依赖)**:
```json
{
  "devDependencies": {
    "vitest": "^1.0.0",
    "@vue/test-utils": "^2.4.0",
    "@playwright/test": "^1.40.0",
    "cypress": "^13.0.0"
  }
}
```

### 2. 测试配置

**application-test.yml**:
```yaml
spring:
  profiles:
    active: test
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

logging:
  level:
    com.zluolan.zaiagent: DEBUG
    org.springframework.test: DEBUG
```

## 🧪 后端测试

### 1. 单元测试示例

**TravelAppTest.java**:
```java
@SpringBootTest
@ActiveProfiles("test")
class TravelAppTest {
    
    @Autowired
    private TravelApp travelApp;
    
    @MockBean
    @Qualifier("dashscopeChatModel")
    private ChatModel dashscopeChatModel;
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;
    
    @Test
    void testDoChat() {
        // Given
        String message = "我想去北京旅游";
        String chatId = "test-chat-123";
        
        // When
        String result = travelApp.doChat(message, chatId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("北京");
    }
    
    @Test
    void testDoChatWithTools() {
        // Given
        String message = "推荐北京的景点";
        String chatId = "test-chat-456";
        
        // When
        String result = travelApp.doChatWithTools(message, chatId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("景点");
    }
}
```

**TravelControllerTest.java**:
```java
@SpringBootTest
@AutoConfigureMockMvc
class TravelControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TravelApp travelApp;
    
    @Test
    void testChatSync() throws Exception {
        // Given
        String message = "hello";
        String chatId = "test-123";
        String expectedResponse = "Hello! How can I help you?";
        
        when(travelApp.doChat(message, chatId)).thenReturn(expectedResponse);
        
        // When & Then
        mockMvc.perform(get("/api/travel/chat/sync")
                .param("message", message)
                .param("chatId", chatId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
    
    @Test
    void testAgentChat() throws Exception {
        // Given
        String message = "规划北京3天旅游";
        String chatId = "agent-test-123";
        String expectedResponse = "AI智能体正在为您规划...";
        
        when(travelApp.doChatWithAgent(message, chatId)).thenReturn(expectedResponse);
        
        // When & Then
        mockMvc.perform(get("/api/travel/agent/chat")
                .param("message", message)
                .param("chatId", chatId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
```

### 2. 集成测试示例

**TravelAppIntegrationTest.java**:
```java
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TravelAppIntegrationTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("travel_planner_test")
            .withUsername("test")
            .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
    
    @Autowired
    private TravelApp travelApp;
    
    @Test
    void testFullChatFlow() {
        // Given
        String message = "我想去上海旅游3天";
        String chatId = "integration-test-123";
        
        // When
        String result = travelApp.doChat(message, chatId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("上海");
        assertThat(result).contains("3天");
    }
}
```

### 3. 工具测试示例

**AttractionSearchToolTest.java**:
```java
@SpringBootTest
class AttractionSearchToolTest {
    
    @Autowired
    private AttractionSearchTool attractionSearchTool;
    
    @MockBean
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;
    
    @Test
    void testSearchAttractions() {
        // Given
        String destination = "北京";
        String category = "历史文化";
        int limit = 5;
        
        // When
        String result = attractionSearchTool.searchAttractions(destination, category, limit);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("北京");
        assertThat(result).contains("景点");
    }
    
    @Test
    void testSearchAttractionsWithInvalidInput() {
        // Given
        String destination = "";
        String category = "历史文化";
        int limit = 5;
        
        // When & Then
        assertThatThrownBy(() -> attractionSearchTool.searchAttractions(destination, category, limit))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
```

## 🎨 前端测试

### 1. 单元测试示例

**Home.vue测试**:
```typescript
// tests/Home.test.ts
import { mount } from '@vue/test-utils';
import { describe, it, expect } from 'vitest';
import Home from '@/pages/Home.vue';

describe('Home.vue', () => {
  it('renders correctly', () => {
    const wrapper = mount(Home);
    expect(wrapper.find('h1').text()).toBe('AI旅游规划大师');
  });
  
  it('has four navigation cards', () => {
    const wrapper = mount(Home);
    const cards = wrapper.findAll('.card');
    expect(cards).toHaveLength(4);
  });
  
  it('navigates to travel chat when clicked', async () => {
    const wrapper = mount(Home);
    const travelCard = wrapper.find('.card');
    await travelCard.trigger('click');
    // 验证导航逻辑
  });
});
```

**TravelChat.vue测试**:
```typescript
// tests/TravelChat.test.ts
import { mount } from '@vue/test-utils';
import { describe, it, expect, vi } from 'vitest';
import TravelChat from '@/pages/TravelChat.vue';

describe('TravelChat.vue', () => {
  it('sends message correctly', async () => {
    const wrapper = mount(TravelChat);
    
    // 模拟用户输入
    const input = wrapper.find('input');
    await input.setValue('我想去北京旅游');
    
    // 模拟发送按钮点击
    const sendButton = wrapper.find('button[type="submit"]');
    await sendButton.trigger('click');
    
    // 验证消息发送
    expect(wrapper.vm.messages).toHaveLength(1);
  });
  
  it('displays streaming response', async () => {
    const wrapper = mount(TravelChat);
    
    // 模拟流式响应
    vi.spyOn(global, 'fetch').mockResolvedValue({
      text: () => Promise.resolve('Hello from AI')
    } as Response);
    
    await wrapper.vm.sendMessage('test message');
    
    // 验证响应显示
    expect(wrapper.find('.assistant-message')).toBeTruthy();
  });
});
```

### 2. 组件测试示例

**useSEO.ts测试**:
```typescript
// tests/useSEO.test.ts
import { describe, it, expect } from 'vitest';
import { useSEO } from '@/composables/useSEO';

describe('useSEO', () => {
  it('updates page title', () => {
    const { updateSEO } = useSEO();
    
    updateSEO('New Title');
    
    expect(document.title).toBe('New Title');
  });
  
  it('updates meta description', () => {
    const { updateSEO } = useSEO();
    
    updateSEO('New Title', 'New Description');
    
    const metaDescription = document.querySelector('meta[name="description"]');
    expect(metaDescription?.getAttribute('content')).toBe('New Description');
  });
});
```

## 🎭 端到端测试

### 1. Playwright测试

**playwright.config.ts**:
```typescript
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests/e2e',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:3000',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
  ],
});
```

**旅游规划流程测试**:
```typescript
// tests/e2e/travel-planning.spec.ts
import { test, expect } from '@playwright/test';

test.describe('旅游规划流程', () => {
  test('完整的旅游规划流程', async ({ page }) => {
    // 访问主页
    await page.goto('/');
    await expect(page).toHaveTitle(/AI旅游规划大师/);
    
    // 点击旅游聊天
    await page.click('text=旅游聊天');
    await expect(page).toHaveURL('/travel');
    
    // 输入旅游需求
    await page.fill('input[placeholder="请输入您的旅游需求"]', '我想去北京旅游3天');
    await page.click('button[type="submit"]');
    
    // 验证AI响应
    await expect(page.locator('.assistant-message')).toBeVisible();
    await expect(page.locator('.assistant-message')).toContainText('北京');
    
    // 测试工具功能
    await page.goto('/tools');
    await page.click('text=景点搜索');
    await expect(page.locator('.tool-result')).toBeVisible();
  });
  
  test('智能体助手功能', async ({ page }) => {
    await page.goto('/agent');
    
    // 输入复杂需求
    await page.fill('input[placeholder="请输入您的旅游需求"]', '帮我规划一个浪漫的巴黎之旅');
    await page.click('button[type="submit"]');
    
    // 验证智能体响应
    await expect(page.locator('.assistant-message')).toBeVisible();
    await expect(page.locator('.assistant-message')).toContainText('巴黎');
  });
});
```

### 2. Cypress测试

**cypress.config.ts**:
```typescript
import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:3000',
    supportFile: 'cypress/support/e2e.ts',
    specPattern: 'cypress/e2e/**/*.cy.ts',
  },
});
```

**API集成测试**:
```typescript
// cypress/e2e/api.cy.ts
describe('API接口测试', () => {
  it('测试旅游规划API', () => {
    cy.request({
      method: 'GET',
      url: 'http://localhost:8123/api/travel/chat/sync',
      qs: {
        message: '我想去北京旅游',
        chatId: 'cypress-test-123'
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.contain('北京');
    });
  });
  
  it('测试工具演示API', () => {
    cy.request({
      method: 'GET',
      url: 'http://localhost:8123/api/travel/tools/attraction',
      qs: { demo: true }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.contain('景点搜索工具演示');
    });
  });
});
```

## 📊 性能测试

### 1. JMeter测试计划

**旅游规划API性能测试**:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
  <hashTree>
    <TestPlan testname="Travel Planner Performance Test">
      <elementProp name="TestPlan.arguments" elementType="Arguments" guiclass="ArgumentsPanel">
        <collectionProp name="Arguments.arguments"/>
      </elementProp>
      <stringProp name="TestPlan.user_define_classpath"></stringProp>
      <boolProp name="TestPlan.functional_mode">false</boolProp>
      <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
    </TestPlan>
    
    <ThreadGroup testname="API Load Test">
      <stringProp name="ThreadGroup.num_threads">100</stringProp>
      <stringProp name="ThreadGroup.ramp_time">60</stringProp>
      <stringProp name="ThreadGroup.duration">300</stringProp>
      
      <HTTPSamplerProxy testname="Chat API">
        <stringProp name="HTTPSampler.domain">localhost</stringProp>
        <stringProp name="HTTPSampler.port">8123</stringProp>
        <stringProp name="HTTPSampler.path">/api/travel/chat/sync</stringProp>
        <stringProp name="HTTPSampler.method">GET</stringProp>
        <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
      </HTTPSamplerProxy>
    </ThreadGroup>
  </hashTree>
</jmeterTestPlan>
```

### 2. Gatling测试脚本

**负载测试脚本**:
```scala
// src/test/scala/TravelPlannerSimulation.scala
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TravelPlannerSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8123")
    .acceptHeader("application/json")
    .userAgentHeader("Gatling Performance Test")

  val scn = scenario("Travel Planner Load Test")
    .exec(http("Chat API")
      .get("/api/travel/chat/sync")
      .queryParam("message", "我想去北京旅游")
      .queryParam("chatId", "gatling-test-${randomUUID()}")
      .check(status.is(200))
      .check(bodyString.exists))
    .pause(1)
    .exec(http("Tools API")
      .get("/api/travel/tools/attraction")
      .queryParam("demo", "true")
      .check(status.is(200))
      .check(bodyString.exists))
    .pause(1)

  setUp(
    scn.inject(
      rampUsers(50) during (60 seconds),
      constantUsers(50) during (300 seconds)
    )
  ).protocols(httpProtocol)
}
```

## 🔍 测试覆盖率

### 1. 后端覆盖率配置

**JaCoCo配置**:
```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.8</version>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>test</phase>
      <goals>
        <goal>report</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

### 2. 前端覆盖率配置

**Vitest覆盖率配置**:
```typescript
// vitest.config.ts
import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'dist/',
        'coverage/',
        '**/*.d.ts',
        '**/*.config.*'
      ],
      thresholds: {
        global: {
          branches: 80,
          functions: 80,
          lines: 80,
          statements: 80
        }
      }
    }
  }
});
```

## 🚀 持续集成测试

### 1. GitHub Actions配置

**.github/workflows/test.yml**:
```yaml
name: Test Suite

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  backend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          
      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          
      - name: Run backend tests
        run: mvn test
        
      - name: Generate coverage report
        run: mvn jacoco:report
        
      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v3

  frontend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
          
      - name: Install pnpm
        run: npm install -g pnpm
        
      - name: Install dependencies
        run: cd simple-frontend && pnpm install
        
      - name: Run frontend tests
        run: cd simple-frontend && pnpm test
        
      - name: Run E2E tests
        run: cd simple-frontend && pnpm test:e2e
```

### 2. 测试报告

**测试报告配置**:
```yaml
# 测试报告生成
reports:
  junit:
    - target/surefire-reports/*.xml
  coverage:
    - target/site/jacoco/index.html
  e2e:
    - simple-frontend/playwright-report/
```

## 📝 测试最佳实践

### 1. 测试命名规范

```java
// 测试方法命名：test + 方法名 + 测试场景
@Test
void testDoChatWithValidInput() { }

@Test
void testDoChatWithInvalidInput() { }

@Test
void testDoChatWithNullInput() { }
```

### 2. 测试数据管理

```java
// 使用Builder模式创建测试数据
public class TravelRequestBuilder {
    private String message = "我想去北京旅游";
    private String chatId = "test-chat-123";
    
    public TravelRequestBuilder withMessage(String message) {
        this.message = message;
        return this;
    }
    
    public TravelRequestBuilder withChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }
    
    public TravelRequest build() {
        return new TravelRequest(message, chatId);
    }
}
```

### 3. 测试隔离

```java
// 使用@DirtiesContext确保测试隔离
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TravelAppTest {
    // 测试方法
}
```

这个测试指南提供了完整的测试方案，包括单元测试、集成测试、端到端测试和性能测试，确保系统的质量和稳定性。
