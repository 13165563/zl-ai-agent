# 部署指南

## 🚀 部署概览

本指南提供了AI旅游规划大师系统的完整部署方案，包括开发环境、测试环境和生产环境的部署配置。

## 📋 系统要求

### 基础环境
- **Java**: 21+ (推荐使用OpenJDK 21)
- **Node.js**: 18+ (推荐使用LTS版本)
- **Maven**: 3.8+ (用于后端构建)
- **pnpm**: 最新版本 (用于前端构建)

### 数据库
- **MySQL**: 8.0+ (用于聊天记忆存储)
- **PostgreSQL**: 13+ (用于向量存储，可选)

### 服务器配置
- **CPU**: 2核心以上
- **内存**: 4GB以上
- **存储**: 20GB以上可用空间
- **网络**: 稳定的互联网连接

## 🛠️ 开发环境部署

### 1. 环境准备

```bash
# 检查Java版本
java -version

# 检查Node.js版本
node -v

# 检查Maven版本
mvn -v

# 安装pnpm
npm install -g pnpm
```

### 2. 后端部署

```bash
# 克隆项目
git clone <repository-url>
cd zl-ai-agent

# 配置数据库
# 创建MySQL数据库
mysql -u root -p
CREATE DATABASE travel_planner;
CREATE USER 'travel_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON travel_planner.* TO 'travel_user'@'localhost';
FLUSH PRIVILEGES;

# 配置应用
cp src/main/resources/application.yml.example src/main/resources/application.yml
# 编辑配置文件，设置数据库连接信息

# 启动应用
mvn spring-boot:run
```

### 3. 前端部署

```bash
# 进入前端目录
cd simple-frontend

# 安装依赖
pnpm install

# 启动开发服务器
pnpm dev
```

### 4. 验证部署

```bash
# 检查后端服务
curl http://localhost:8123/actuator/health

# 检查前端服务
curl http://localhost:3000

# 测试API接口
curl "http://localhost:8123/api/travel/chat/sync?message=hello&chatId=test"
```

## 🧪 测试环境部署

### 1. Docker部署

**Dockerfile (后端)**:
```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

# 复制Maven文件
COPY pom.xml .
COPY src ./src

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 构建应用
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8123

# 启动应用
CMD ["java", "-jar", "target/zl-ai-agent-1.0.0.jar"]
```

**Dockerfile (前端)**:
```dockerfile
FROM node:18-alpine

WORKDIR /app

# 复制package文件
COPY package.json pnpm-lock.yaml ./

# 安装pnpm
RUN npm install -g pnpm

# 安装依赖
RUN pnpm install

# 复制源代码
COPY . .

# 构建应用
RUN pnpm build

# 安装serve
RUN npm install -g serve

# 暴露端口
EXPOSE 3000

# 启动应用
CMD ["serve", "-s", "dist", "-l", "3000"]
```

**docker-compose.yml**:
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: travel_planner
      MYSQL_USER: travel_user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: .
    ports:
      - "8123:8123"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/travel_planner
      SPRING_DATASOURCE_USERNAME: travel_user
      SPRING_DATASOURCE_PASSWORD: password
    depends_on:
      - mysql

  frontend:
    build: ./simple-frontend
    ports:
      - "3000:3000"
    depends_on:
      - backend

volumes:
  mysql_data:
```

### 2. 启动测试环境

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
docker-compose logs -f frontend
```

## 🏭 生产环境部署

### 1. 服务器配置

**系统要求**:
- Ubuntu 20.04+ / CentOS 8+ / RHEL 8+
- 4核心CPU, 8GB内存, 50GB存储
- 稳定的网络连接

**安全配置**:
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装防火墙
sudo ufw enable
sudo ufw allow 22    # SSH
sudo ufw allow 80    # HTTP
sudo ufw allow 443   # HTTPS
sudo ufw allow 8123  # 后端API
```

### 2. 数据库部署

**MySQL配置**:
```bash
# 安装MySQL
sudo apt install mysql-server -y

# 安全配置
sudo mysql_secure_installation

# 创建数据库和用户
sudo mysql -u root -p
CREATE DATABASE travel_planner CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'travel_user'@'localhost' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON travel_planner.* TO 'travel_user'@'localhost';
FLUSH PRIVILEGES;
```

**MySQL优化配置**:
```ini
# /etc/mysql/mysql.conf.d/mysqld.cnf
[mysqld]
innodb_buffer_pool_size = 2G
innodb_log_file_size = 256M
max_connections = 200
query_cache_size = 64M
query_cache_type = 1
```

### 3. 后端部署

**构建应用**:
```bash
# 克隆代码
git clone <repository-url>
cd zl-ai-agent

# 构建JAR包
mvn clean package -DskipTests

# 创建部署目录
sudo mkdir -p /opt/travel-planner
sudo cp target/zl-ai-agent-1.0.0.jar /opt/travel-planner/
```

**创建systemd服务**:
```ini
# /etc/systemd/system/travel-planner.service
[Unit]
Description=Travel Planner Application
After=network.target mysql.service

[Service]
Type=simple
User=travel
Group=travel
WorkingDirectory=/opt/travel-planner
ExecStart=/usr/bin/java -Xmx2g -Xms1g -jar zl-ai-agent-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**启动服务**:
```bash
# 创建用户
sudo useradd -r -s /bin/false travel

# 设置权限
sudo chown -R travel:travel /opt/travel-planner

# 启动服务
sudo systemctl daemon-reload
sudo systemctl enable travel-planner
sudo systemctl start travel-planner

# 检查状态
sudo systemctl status travel-planner
```

### 4. 前端部署

**构建前端**:
```bash
# 进入前端目录
cd simple-frontend

# 安装依赖
pnpm install

# 构建生产版本
pnpm build
```

**Nginx配置**:
```nginx
# /etc/nginx/sites-available/travel-planner
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /opt/travel-planner/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://localhost:8123;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

**启用Nginx配置**:
```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/travel-planner /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重启Nginx
sudo systemctl restart nginx
```

### 5. SSL证书配置

**使用Let's Encrypt**:
```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取SSL证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 🔧 配置管理

### 1. 环境变量配置

**生产环境配置**:
```yaml
# application-prod.yml
server:
  port: 8123
  servlet:
    context-path: /

spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/travel_planner
    username: ${DB_USERNAME:travel_user}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
    ollama:
      base-url: ${OLLAMA_BASE_URL:http://localhost:11434}

logging:
  level:
    com.zluolan.zaiagent: INFO
  file:
    name: /var/log/travel-planner/application.log
```

### 2. 监控配置

**Prometheus监控**:
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'travel-planner'
    static_configs:
      - targets: ['localhost:8123']
    metrics_path: '/actuator/prometheus'
```

**Grafana仪表板**:
```json
{
  "dashboard": {
    "title": "Travel Planner Dashboard",
    "panels": [
      {
        "title": "Request Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(http_requests_total[5m])"
          }
        ]
      }
    ]
  }
}
```

## 📊 性能优化

### 1. JVM优化

```bash
# JVM参数配置
JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication"
```

### 2. 数据库优化

```sql
-- 创建索引
CREATE INDEX idx_chat_memory_conversation_id ON chat_memory(conversation_id);
CREATE INDEX idx_chat_memory_timestamp ON chat_memory(timestamp);

-- 优化查询
EXPLAIN SELECT * FROM chat_memory WHERE conversation_id = 'test-123';
```

### 3. 缓存配置

```yaml
# Redis缓存配置
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD:}
    database: 0
```

## 🔒 安全配置

### 1. 防火墙配置

```bash
# UFW防火墙规则
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow ssh
sudo ufw allow 80
sudo ufw allow 443
sudo ufw enable
```

### 2. 应用安全

```yaml
# 安全配置
spring:
  security:
    user:
      name: admin
      password: ${ADMIN_PASSWORD:strong_password}
  
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      timeout: 30000
```

### 3. 数据加密

```bash
# 数据库连接加密
spring.datasource.url=jdbc:mysql://localhost:3306/travel_planner?useSSL=true&requireSSL=true
```

## 📈 监控和日志

### 1. 应用监控

```yaml
# 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
```

### 2. 日志管理

```yaml
# 日志配置
logging:
  level:
    root: INFO
    com.zluolan.zaiagent: DEBUG
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: /var/log/travel-planner/application.log
    max-size: 100MB
    max-history: 30
```

### 3. 日志轮转

```bash
# logrotate配置
# /etc/logrotate.d/travel-planner
/var/log/travel-planner/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 travel travel
}
```

## 🚀 自动化部署

### 1. CI/CD流水线

**GitHub Actions**:
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          
      - name: Build Backend
        run: mvn clean package -DskipTests
        
      - name: Build Frontend
        run: |
          cd simple-frontend
          npm install -g pnpm
          pnpm install
          pnpm build
          
      - name: Deploy to Server
        uses: appleboy/ssh-action@v0.1.5
        with:
          host: ${{ secrets.HOST }}
          username: ${{ secrets.USERNAME }}
          key: ${{ secrets.SSH_KEY }}
          script: |
            sudo systemctl stop travel-planner
            # 部署新版本
            sudo systemctl start travel-planner
```

### 2. 健康检查

```bash
#!/bin/bash
# health-check.sh

# 检查后端服务
if curl -f http://localhost:8123/actuator/health > /dev/null 2>&1; then
    echo "Backend is healthy"
else
    echo "Backend is down"
    exit 1
fi

# 检查前端服务
if curl -f http://localhost:3000 > /dev/null 2>&1; then
    echo "Frontend is healthy"
else
    echo "Frontend is down"
    exit 1
fi
```

## 🔄 备份和恢复

### 1. 数据库备份

```bash
#!/bin/bash
# backup-db.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups"
DB_NAME="travel_planner"

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u travel_user -p$DB_PASSWORD $DB_NAME > $BACKUP_DIR/travel_planner_$DATE.sql

# 压缩备份
gzip $BACKUP_DIR/travel_planner_$DATE.sql

# 删除7天前的备份
find $BACKUP_DIR -name "travel_planner_*.sql.gz" -mtime +7 -delete
```

### 2. 应用备份

```bash
#!/bin/bash
# backup-app.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups"
APP_DIR="/opt/travel-planner"

# 备份应用文件
tar -czf $BACKUP_DIR/travel-planner-app_$DATE.tar.gz -C $APP_DIR .

# 删除30天前的备份
find $BACKUP_DIR -name "travel-planner-app_*.tar.gz" -mtime +30 -delete
```

这个部署指南提供了从开发环境到生产环境的完整部署方案，包括Docker部署、服务器配置、监控设置、安全配置等，确保系统能够稳定、安全地运行。
