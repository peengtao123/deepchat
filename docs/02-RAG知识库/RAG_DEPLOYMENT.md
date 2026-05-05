# RAG 系统部署和运维指南

## 🚀 本地开发部署

### 前置要求
- Java 17+
- Maven 3.6+
- DeepSeek API Key（已配置）

### 启动步骤
```bash
# 1. 克隆项目
cd deepchat

# 2. 安装依赖
mvn clean install

# 3. 启动应用
mvn spring-boot:run

# 4. 访问应用
# http://localhost:8080
```

## 📦 生产环境部署

### 方案 1: JAR 包部署

#### 1. 打包
```bash
mvn clean package -DskipTests
```

#### 2. 运行
```bash
java -jar target/deepchat-0.0.1-SNAPSHOT.jar
```

#### 3. 后台运行
```bash
nohup java -jar target/deepchat-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### 方案 2: Docker 部署

#### 1. 创建 Dockerfile
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/deepchat-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

#### 2. 构建镜像
```bash
docker build -t deepchat-rag .
```

#### 3. 运行容器
```bash
docker run -d \
  -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key \
  --name deepchat \
  deepchat-rag
```

### 方案 3: Docker Compose

创建 `docker-compose.yml`:
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
    volumes:
      - ./data:/app/data
    restart: unless-stopped
```

运行：
```bash
docker-compose up -d
```

## 🔧 配置管理

### application.properties 配置项

```properties
# 应用配置
spring.application.name=deepchat-rag
server.port=8080

# AI 配置
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.base-url=https://api.deepseek.com
spring.ai.openai.chat.options.model=deepseek-chat
spring.ai.openai.chat.options.temperature=0.7

# 日志配置
logging.level.com.example.demo=INFO
logging.file.name=logs/deepchat.log
```

### 环境变量方式
```bash
export OPENAI_API_KEY=sk-your-api-key
java -jar app.jar
```

## 🗄️ 向量数据库升级

### 从 SimpleVectorStore 迁移到 Chroma

#### 1. 添加依赖
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-chroma-store-spring-boot-starter</artifactId>
</dependency>
```

#### 2. 配置 Chroma
```properties
spring.ai.vectorstore.chroma.client.host=http://localhost
spring.ai.vectorstore.chroma.client.port=8000
spring.ai.vectorstore.chroma.collection-name=rag-docs
```

#### 3. 启动 Chroma
```bash
docker run -d \
  -p 8000:8000 \
  ghcr.io/chroma-core/chroma:latest
```

### 其他向量数据库选项

| 数据库 | 适用场景 | 特点 |
|--------|---------|------|
| **Chroma** | 中小规模 | 开源、易用 |
| **Pinecone** | 生产环境 | 托管服务、高性能 |
| **Milvus** | 大规模 | 分布式、高可用 |
| **Weaviate** | 企业级 | 云原生、GraphQL |

## 📊 监控和日志

### 1. 添加 Actuator
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 2. 配置监控端点
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

### 3. 查看健康状态
```bash
curl http://localhost:8080/actuator/health
```

### 4. 日志配置
```properties
# application.properties
logging.level.root=INFO
logging.level.com.example.demo=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.file.name=logs/deepchat.log
logging.file.max-size=10MB
logging.file.max-history=30
```

## 🔐 安全加固

### 1. 添加 Spring Security
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. 基础认证配置
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/rag/**").authenticated()
                .requestMatchers("/*.html").permitAll()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

### 3. API 速率限制
使用 Bucket4j 实现限流：
```xml
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.7.0</version>
</dependency>
```

## 🔄 备份和恢复

### 文档数据备份

由于使用 SimpleVectorStore（内存存储），需要定期导出：

```java
// 导出文档
@RestController
public class BackupController {
    @Autowired
    private VectorStore vectorStore;
    
    @GetMapping("/api/backup/export")
    public void exportDocuments(HttpServletResponse response) {
        // 实现导出逻辑
        response.setContentType("application/json");
        // 写入文档数据
    }
}
```

### 使用持久化向量库
生产环境建议使用支持持久化的向量数据库，自动保存数据。

## 📈 性能优化

### 1. JVM 参数调优
```bash
java -Xms512m -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar app.jar
```

### 2. 连接池配置
```properties
# HTTP 客户端连接池
spring.ai.openai.connection-pool.size=10
spring.ai.openai.connection-pool.timeout=30s
```

### 3. 缓存策略
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

```java
@Cacheable(value = "searchResults", key = "#query")
public List<Document> searchSimilarDocuments(String query, int topK) {
    // ...
}
```

## 🐛 故障排查

### 常见问题

#### 1. 启动失败
```bash
# 检查端口占用
netstat -ano | findstr :8080

# 查看详细日志
mvn spring-boot:run -X
```

#### 2. API 调用失败
```bash
# 测试 API 连通性
curl https://api.deepseek.com/v1/chat/completions \
  -H "Authorization: Bearer YOUR_API_KEY"
```

#### 3. 内存不足
```bash
# 增加 JVM 堆内存
java -Xmx4g -jar app.jar
```

#### 4. 向量搜索慢
- 减少 topK 值
- 优化文档分割
- 使用更快的向量数据库

## 📋 运维检查清单

### 日常检查
- [ ] 应用健康状态
- [ ] API 响应时间
- [ ] 错误日志
- [ ] 内存使用情况
- [ ] 磁盘空间

### 每周检查
- [ ] 备份数据
- [ ] 清理旧日志
- [ ] 更新文档索引
- [ ] 性能指标分析

### 每月检查
- [ ] 安全更新
- [ ] 依赖升级
- [ ] 容量规划
- [ ] 灾难恢复演练

## 🚨 应急响应

### 服务宕机
```bash
# 1. 检查进程
ps aux | grep deepchat

# 2. 查看最后日志
tail -n 100 logs/deepchat.log

# 3. 重启服务
systemctl restart deepchat
```

### API 限流
```bash
# 检查 API 调用次数
curl http://localhost:8080/actuator/metrics/http.client.requests
```

### 数据丢失
```bash
# 从备份恢复
curl http://localhost:8080/api/backup/import \
  -F "file=@backup.json"
```

## 📞 技术支持

### 资源链接
- 📖 [Spring AI 文档](https://spring.io/projects/spring-ai)
- 🔧 [问题反馈](https://github.com/spring-projects/spring-ai/issues)
- 💬 [社区论坛](https://spring.io/community)

### 联系方式
- Email: support@example.com
- 文档: docs/RAG_INTEGRATION.md

---

**祝部署顺利！** 🎉
