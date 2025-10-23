# 系统架构文档

## 🏗️ 整体架构

AI旅游规划大师系统采用前后端分离的架构设计，结合Spring AI和Vue3技术栈，提供智能化的旅游规划服务。

### 架构层次

```mermaid
graph TB
    subgraph "用户层"
        U1[Web浏览器]
        U2[移动端浏览器]
    end
    
    subgraph "前端层 (Vue3 + TypeScript)"
        F1[Home.vue<br/>主页]
        F2[TravelChat.vue<br/>旅游聊天]
        F3[TravelAgent.vue<br/>智能体助手]
        F4[Tools.vue<br/>旅游工具集]
    end
    
    subgraph "路由层 (Vue Router)"
        R1["/ → Home.vue"]
        R2["/travel → TravelChat.vue"]
        R3["/agent → TravelAgent.vue"]
        R4["/tools → Tools.vue"]
    end
    
    subgraph "后端层 (Spring Boot)"
        B1[TravelController<br/>控制器]
        B2[TravelApp<br/>核心应用]
        B3[TravelPlanningAgent<br/>智能体]
    end
    
    subgraph "工具层 (AI Tools)"
        T1[AttractionSearchTool<br/>景点搜索工具]
        T2[HotelSearchTool<br/>酒店搜索工具]
        T3[RoutePlanningTool<br/>路线规划工具]
        T4[WeatherQueryTool<br/>天气查询工具]
    end
    
    subgraph "AI服务层 (Spring AI + Alibaba)"
        A1[DashScope ChatModel<br/>阿里云AI模型]
        A2[Ollama ChatModel<br/>本地AI模型]
    end
    
    U1 --> F1
    U2 --> F1
    F1 --> R1
    F2 --> R2
    F3 --> R3
    F4 --> R4
    
    R1 --> B1
    R2 --> B1
    R3 --> B1
    R4 --> B1
    
    B1 --> B2
    B1 --> B3
    
    B2 --> T1
    B2 --> T2
    B2 --> T3
    B2 --> T4
    
    B3 --> T1
    B3 --> T2
    B3 --> T3
    B3 --> T4
    
    B2 --> A1
    B3 --> A1
    T1 --> A1
    T2 --> A1
    T3 --> A1
    T4 --> A1
```

## 🔄 数据流架构

### 用户交互流程

```mermaid
sequenceDiagram
    participant U as 用户
    participant F as 前端界面
    participant C as TravelController
    participant A as TravelApp
    participant T as AI Tools
    participant AI as DashScope AI
    
    U->>F: 输入旅游需求
    F->>C: HTTP API请求
    C->>A: 调用TravelApp
    A->>AI: 发送对话请求
    AI->>A: 返回AI响应
    A->>T: 调用相关工具
    T->>A: 返回工具结果
    A->>C: 返回完整响应
    C->>F: 流式数据返回
    F->>U: 显示结果
```

### 智能体交互流程

```mermaid
sequenceDiagram
    participant U as 用户
    participant F as 前端界面
    participant C as TravelController
    participant A as TravelPlanningAgent
    participant T as AI Tools
    participant AI as DashScope AI
    
    U->>F: 输入复杂旅游需求
    F->>C: HTTP API请求
    C->>A: 启动智能体
    A->>AI: 分析用户需求
    AI->>A: 返回分析结果
    A->>T: 调用多个工具
    T->>A: 返回工具结果
    A->>AI: 整合结果
    AI->>A: 返回最终方案
    A->>C: 返回完整规划
    C->>F: 流式输出
    F->>U: 显示旅游规划
```

## 🧩 核心组件

### 前端组件架构

```mermaid
graph TB
    subgraph "Vue3应用"
        A[App.vue<br/>根组件]
        
        subgraph "页面组件"
            H[Home.vue<br/>主页]
            TC[TravelChat.vue<br/>旅游聊天]
            TA[TravelAgent.vue<br/>智能体助手]
            T[Tools.vue<br/>工具集]
        end
        
        subgraph "组合式函数"
            S[useSEO.ts<br/>SEO管理]
        end
        
        subgraph "样式系统"
            G[global.css<br/>全局样式]
        end
    end
    
    A --> H
    A --> TC
    A --> TA
    A --> T
    
    H --> S
    TC --> S
    TA --> S
    T --> S
    
    H --> G
    TC --> G
    TA --> G
    T --> G
```

### 后端组件架构

```mermaid
graph TB
    subgraph "Spring Boot应用"
        subgraph "控制器层"
            TC[TravelController<br/>REST API]
        end
        
        subgraph "应用层"
            TA[TravelApp<br/>核心业务逻辑]
        end
        
        subgraph "智能体层"
            TPA[TravelPlanningAgent<br/>多步骤规划]
            TCA[ToolCallAgent<br/>工具调用基类]
        end
        
        subgraph "工具层"
            AST[AttractionSearchTool<br/>景点搜索]
            HST[HotelSearchTool<br/>酒店搜索]
            RPT[RoutePlanningTool<br/>路线规划]
            WQT[WeatherQueryTool<br/>天气查询]
            WST[WebSearchTool<br/>网络搜索]
        end
        
        subgraph "MCP集成层"
            AMW[AmapMcpToolWrapper<br/>高德地图MCP]
            RTG[RedbookTravelGuideTool<br/>小红书MCP]
        end
        
        subgraph "配置层"
            TCfg[ToolConfig<br/>工具配置]
            MCfg[McpConfig<br/>MCP配置]
        end
    end
    
    TC --> TA
    TA --> TPA
    TPA --> TCA
    TCA --> AST
    TCA --> HST
    TCA --> RPT
    TCA --> WQT
    TCA --> WST
    TCA --> AMW
    TCA --> RTG
    
    TCfg --> AST
    TCfg --> HST
    TCfg --> RPT
    TCfg --> WQT
    TCfg --> WST
    
    MCfg --> AMW
    MCfg --> RTG
```

## 🗄️ 数据存储架构

### 聊天记忆存储

```mermaid
graph TB
    subgraph "记忆存储层"
        MCR[MysqlChatMemoryRepository<br/>MySQL记忆存储]
        FCR[FileBasedChatMemoryRepository<br/>文件记忆存储]
        ICR[InMemoryChatMemoryRepository<br/>内存记忆存储]
    end
    
    subgraph "Spring AI记忆管理"
        MWCM[MessageWindowChatMemory<br/>消息窗口记忆]
        MCMA[MessageChatMemoryAdvisor<br/>记忆顾问]
    end
    
    subgraph "应用层"
        TA[TravelApp<br/>旅游应用]
        TPA[TravelPlanningAgent<br/>智能体]
    end
    
    MCR --> MWCM
    FCR --> MWCM
    ICR --> MWCM
    
    MWCM --> MCMA
    MCMA --> TA
    MCMA --> TPA
```

## 🔧 配置架构

### Spring Boot配置

```mermaid
graph TB
    subgraph "应用配置"
        AC[application.yml<br/>主配置文件]
        LC[application-local.yml<br/>本地配置]
        PC[application-prod.yml<br/>生产配置]
    end
    
    subgraph "Bean配置"
        TC[ToolConfig<br/>工具Bean配置]
        MC[McpConfig<br/>MCP Bean配置]
        CC[ChatModelConfig<br/>AI模型配置]
    end
    
    subgraph "自动配置"
        SAA[SpringAIAutoConfiguration<br/>Spring AI自动配置]
        ACA[AlibabaCloudAIAutoConfiguration<br/>阿里云AI自动配置]
    end
    
    AC --> TC
    LC --> TC
    PC --> TC
    
    AC --> MC
    LC --> MC
    PC --> MC
    
    AC --> CC
    LC --> CC
    PC --> CC
    
    SAA --> TC
    ACA --> CC
```

## 🚀 部署架构

### 开发环境

```mermaid
graph LR
    subgraph "开发机器"
        D[开发者]
        FE[Vite开发服务器<br/>localhost:3000]
        BE[Spring Boot应用<br/>localhost:8123]
        DB[MySQL数据库<br/>localhost:3306]
    end
    
    D --> FE
    FE --> BE
    BE --> DB
```

### 生产环境

```mermaid
graph TB
    subgraph "用户端"
        U[用户浏览器]
    end
    
    subgraph "CDN/负载均衡"
        LB[Nginx/CloudFlare]
    end
    
    subgraph "前端服务器"
        FS[Vue3静态文件<br/>Nginx托管]
    end
    
    subgraph "后端服务器"
        BS[Spring Boot应用<br/>JVM运行]
    end
    
    subgraph "数据库服务器"
        DB[MySQL数据库]
        PG[PostgreSQL数据库]
    end
    
    subgraph "AI服务"
        DS[DashScope API<br/>阿里云AI服务]
    end
    
    U --> LB
    LB --> FS
    LB --> BS
    BS --> DB
    BS --> PG
    BS --> DS
```

## 🔒 安全架构

### 安全层次

```mermaid
graph TB
    subgraph "网络安全"
        FW[防火墙]
        WAF[Web应用防火墙]
    end
    
    subgraph "应用安全"
        CORS[跨域资源共享控制]
        API[API访问控制]
        AUTH[身份认证]
    end
    
    subgraph "数据安全"
        ENC[数据加密]
        BACKUP[数据备份]
    end
    
    subgraph "AI安全"
        FILTER[内容过滤器]
        SAFETY[AI安全机制]
    end
    
    FW --> CORS
    WAF --> API
    CORS --> ENC
    API --> FILTER
    ENC --> SAFETY
```

## 📊 监控架构

### 监控体系

```mermaid
graph TB
    subgraph "应用监控"
        AM[应用性能监控<br/>APM]
        LM[日志监控]
        MM[指标监控]
    end
    
    subgraph "基础设施监控"
        SM[服务器监控]
        DM[数据库监控]
        NM[网络监控]
    end
    
    subgraph "业务监控"
        UM[用户行为监控]
        PM[性能监控]
        EM[错误监控]
    end
    
    AM --> UM
    LM --> PM
    MM --> EM
    SM --> UM
    DM --> PM
    NM --> EM
```

## 🔄 扩展架构

### 微服务扩展

```mermaid
graph TB
    subgraph "API网关"
        GW[Spring Cloud Gateway]
    end
    
    subgraph "核心服务"
        TS[旅游服务<br/>Travel Service]
        AS[AI服务<br/>AI Service]
        US[用户服务<br/>User Service]
    end
    
    subgraph "支撑服务"
        NS[通知服务<br/>Notification Service]
        PS[支付服务<br/>Payment Service]
        RS[推荐服务<br/>Recommendation Service]
    end
    
    subgraph "数据服务"
        DS[数据服务<br/>Data Service]
        CS[缓存服务<br/>Cache Service]
    end
    
    GW --> TS
    GW --> AS
    GW --> US
    
    TS --> NS
    AS --> PS
    US --> RS
    
    TS --> DS
    AS --> CS
    US --> DS
```

这个架构文档提供了系统的完整技术视图，包括组件关系、数据流、部署方案等，为系统的开发、维护和扩展提供了清晰的指导。
