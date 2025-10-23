# 故障排除指南

## 🚨 常见问题及解决方案

本指南提供了AI旅游规划大师系统常见问题的诊断和解决方案。

## 🔧 后端问题

### 1. 应用启动失败

**问题**: 应用无法启动，出现端口占用错误

**错误信息**:
```
Port 8123 was already in use
```

**解决方案**:
```bash
# 查找占用端口的进程
netstat -ano | findstr :8123

# 杀死占用端口的进程
taskkill /PID <进程ID> /F

# 或者使用不同的端口
java -jar app.jar --server.port=8124
```

**问题**: 数据库连接失败

**错误信息**:
```
Could not create connection to database server
```

**解决方案**:
```bash
# 检查MySQL服务状态
systemctl status mysql

# 启动MySQL服务
sudo systemctl start mysql

# 检查数据库配置
mysql -u root -p
SHOW DATABASES;
```

### 2. 依赖注入问题

**问题**: Bean创建失败

**错误信息**:
```
No qualifying bean of type 'ChatModel' available
```

**解决方案**:
```java
// 使用@Qualifier指定具体的Bean
@MockBean
@Qualifier("dashscopeChatModel")
private ChatModel dashscopeChatModel;
```

**问题**: 循环依赖

**错误信息**:
```
Circular dependency detected
```

**解决方案**:
```java
// 使用@Lazy注解延迟加载
@Lazy
@Autowired
private SomeService someService;
```

### 3. AI服务问题

**问题**: DashScope API调用失败

**错误信息**:
```
API key is invalid or expired
```

**解决方案**:
```yaml
# 检查API密钥配置
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY:your-api-key}
```

**问题**: AI响应超时

**错误信息**:
```
TimeoutException: Did not observe any item or terminal signal within 30000ms
```

**解决方案**:
```yaml
# 增加超时时间
spring:
  ai:
    dashscope:
      timeout: 60000
```

### 4. 内存问题

**问题**: 内存溢出

**错误信息**:
```
OutOfMemoryError: Java heap space
```

**解决方案**:
```bash
# 增加JVM堆内存
java -Xmx2g -Xms1g -jar app.jar

# 或设置环境变量
export JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC"
```

## 🎨 前端问题

### 1. 构建失败

**问题**: Vite构建失败

**错误信息**:
```
Failed to resolve import
```

**解决方案**:
```bash
# 清理依赖并重新安装
rm -rf node_modules
rm pnpm-lock.yaml
pnpm install

# 检查TypeScript配置
npx tsc --noEmit
```

**问题**: 端口占用

**错误信息**:
```
Port 3000 is already in use
```

**解决方案**:
```bash
# 查找占用端口的进程
netstat -ano | findstr :3000

# 杀死进程或使用不同端口
pnpm dev --port 3001
```

### 2. 路由问题

**问题**: 页面刷新后404

**错误信息**:
```
Cannot GET /travel
```

**解决方案**:
```typescript
// 配置Vite开发服务器
export default defineConfig({
  server: {
    historyApiFallback: true
  }
})
```

**问题**: 路由跳转失败

**错误信息**:
```
Navigation failed
```

**解决方案**:
```typescript
// 检查路由配置
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/travel', component: TravelChat },
    { path: '/agent', component: TravelAgent },
    { path: '/tools', component: Tools }
  ]
})
```

### 3. API调用问题

**问题**: CORS错误

**错误信息**:
```
Access to fetch at 'http://localhost:8123/api/travel/chat/sync' from origin 'http://localhost:3000' has been blocked by CORS policy
```

**解决方案**:
```typescript
// 配置代理
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8123',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
```

**问题**: 网络请求失败

**错误信息**:
```
NetworkError when attempting to fetch resource
```

**解决方案**:
```typescript
// 添加错误处理
const fetchWithRetry = async (url: string, options: RequestInit, retries = 3) => {
  for (let i = 0; i < retries; i++) {
    try {
      const response = await fetch(url, options);
      if (response.ok) return response;
    } catch (error) {
      if (i === retries - 1) throw error;
      await new Promise(resolve => setTimeout(resolve, 1000 * (i + 1)));
    }
  }
};
```

## 🗄️ 数据库问题

### 1. 连接问题

**问题**: 数据库连接超时

**错误信息**:
```
Connection timed out
```

**解决方案**:
```yaml
# 增加连接超时时间
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/travel_planner?connectTimeout=60000&socketTimeout=60000
    hikari:
      connection-timeout: 60000
      idle-timeout: 300000
      max-lifetime: 1200000
```

**问题**: 连接池耗尽

**错误信息**:
```
HikariPool-1 - Connection is not available
```

**解决方案**:
```yaml
# 调整连接池配置
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      leak-detection-threshold: 60000
```

### 2. 数据问题

**问题**: 数据不一致

**错误信息**:
```
Data integrity constraint violation
```

**解决方案**:
```sql
-- 检查数据完整性
SELECT * FROM chat_memory WHERE conversation_id = 'test-123';

-- 清理无效数据
DELETE FROM chat_memory WHERE conversation_id IS NULL;
```

**问题**: 字符编码问题

**错误信息**:
```
Incorrect string value
```

**解决方案**:
```sql
-- 检查数据库字符集
SHOW VARIABLES LIKE 'character_set%';

-- 修改数据库字符集
ALTER DATABASE travel_planner CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 🔄 集成问题

### 1. 前后端通信问题

**问题**: API响应格式错误

**错误信息**:
```
Unexpected token < in JSON at position 0
```

**解决方案**:
```typescript
// 检查响应格式
const response = await fetch(url);
const contentType = response.headers.get('content-type');

if (contentType?.includes('application/json')) {
  return await response.json();
} else {
  return await response.text();
}
```

**问题**: 流式输出中断

**错误信息**:
```
Connection closed unexpectedly
```

**解决方案**:
```typescript
// 添加重连机制
const connectWithRetry = async (url: string, retries = 3) => {
  for (let i = 0; i < retries; i++) {
    try {
      const response = await fetch(url);
      const reader = response.body?.getReader();
      return reader;
    } catch (error) {
      if (i === retries - 1) throw error;
      await new Promise(resolve => setTimeout(resolve, 1000 * (i + 1)));
    }
  }
};
```

### 2. MCP服务问题

**问题**: MCP服务连接失败

**错误信息**:
```
MCP service connection failed
```

**解决方案**:
```yaml
# 检查MCP配置
spring:
  ai:
    mcp:
      client:
        enabled: true
        timeout: 30000
```

**问题**: MCP工具调用失败

**错误信息**:
```
MCP tool execution failed
```

**解决方案**:
```java
// 添加错误处理
@Retryable(value = {Exception.class}, maxAttempts = 3)
public String executeMcpTool(String toolName, Map<String, Object> params) {
    try {
        return mcpClient.execute(toolName, params);
    } catch (Exception e) {
        log.error("MCP tool execution failed: {}", e.getMessage());
        throw e;
    }
}
```

## 📊 性能问题

### 1. 响应慢

**问题**: API响应时间过长

**症状**: 请求响应时间超过5秒

**解决方案**:
```yaml
# 优化数据库查询
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

**问题**: 内存使用过高

**症状**: 内存使用率超过80%

**解决方案**:
```bash
# 调整JVM参数
java -Xmx4g -Xms2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar app.jar
```

### 2. 并发问题

**问题**: 高并发下系统不稳定

**症状**: 在高并发请求下出现错误

**解决方案**:
```yaml
# 调整线程池配置
server:
  tomcat:
    threads:
      max: 200
      min-spare: 10
    max-connections: 8192
    accept-count: 100
```

## 🔍 调试技巧

### 1. 日志调试

**启用详细日志**:
```yaml
logging:
  level:
    com.zluolan.zaiagent: DEBUG
    org.springframework.web: DEBUG
    org.springframework.ai: DEBUG
```

**日志分析**:
```bash
# 查看应用日志
tail -f /var/log/travel-planner/application.log

# 过滤错误日志
grep "ERROR" /var/log/travel-planner/application.log

# 分析性能日志
grep "took" /var/log/travel-planner/application.log
```

### 2. 监控工具

**应用监控**:
```bash
# 检查应用状态
curl http://localhost:8123/actuator/health

# 查看应用指标
curl http://localhost:8123/actuator/metrics

# 查看JVM信息
curl http://localhost:8123/actuator/info
```

**系统监控**:
```bash
# 检查系统资源
top
htop
iostat -x 1

# 检查网络连接
netstat -tulpn | grep :8123
ss -tulpn | grep :8123
```

### 3. 数据库调试

**查询分析**:
```sql
-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- 分析查询性能
EXPLAIN SELECT * FROM chat_memory WHERE conversation_id = 'test-123';

-- 查看连接状态
SHOW PROCESSLIST;
```

## 🛠️ 工具和脚本

### 1. 健康检查脚本

```bash
#!/bin/bash
# health-check.sh

# 检查后端服务
if curl -f http://localhost:8123/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend is healthy"
else
    echo "❌ Backend is down"
    exit 1
fi

# 检查前端服务
if curl -f http://localhost:3000 > /dev/null 2>&1; then
    echo "✅ Frontend is healthy"
else
    echo "❌ Frontend is down"
    exit 1
fi

# 检查数据库
if mysql -u travel_user -p$DB_PASSWORD -e "SELECT 1" > /dev/null 2>&1; then
    echo "✅ Database is healthy"
else
    echo "❌ Database is down"
    exit 1
fi

echo "🎉 All services are healthy"
```

### 2. 性能测试脚本

```bash
#!/bin/bash
# performance-test.sh

# 测试API响应时间
echo "Testing API response time..."
for i in {1..10}; do
    time curl -s "http://localhost:8123/api/travel/chat/sync?message=test&chatId=perf-test-$i" > /dev/null
done

# 测试并发性能
echo "Testing concurrent requests..."
for i in {1..50}; do
    curl -s "http://localhost:8123/api/travel/chat/sync?message=test&chatId=concurrent-test-$i" > /dev/null &
done
wait
echo "Performance test completed"
```

### 3. 数据备份脚本

```bash
#!/bin/bash
# backup.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups"
DB_NAME="travel_planner"

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u travel_user -p$DB_PASSWORD $DB_NAME > $BACKUP_DIR/travel_planner_$DATE.sql

# 压缩备份
gzip $BACKUP_DIR/travel_planner_$DATE.sql

# 清理旧备份
find $BACKUP_DIR -name "travel_planner_*.sql.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_DIR/travel_planner_$DATE.sql.gz"
```

## 📞 获取帮助

### 1. 日志收集

```bash
# 收集系统信息
uname -a > system-info.txt
java -version >> system-info.txt
mysql --version >> system-info.txt

# 收集应用日志
cp /var/log/travel-planner/application.log ./application.log

# 收集配置文件
cp src/main/resources/application.yml ./application.yml
```

### 2. 问题报告模板

```
## 问题描述
[详细描述遇到的问题]

## 环境信息
- 操作系统: [Windows/Linux/macOS]
- Java版本: [java -version输出]
- 应用版本: [版本号]
- 数据库版本: [MySQL版本]

## 错误信息
[完整的错误堆栈信息]

## 重现步骤
1. [步骤1]
2. [步骤2]
3. [步骤3]

## 预期结果
[期望的正常行为]

## 实际结果
[实际发生的情况]

## 附加信息
[其他相关信息]
```

这个故障排除指南提供了常见问题的诊断和解决方案，帮助快速定位和解决问题，确保系统的稳定运行。
