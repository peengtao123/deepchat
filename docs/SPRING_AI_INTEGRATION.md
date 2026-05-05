# Spring AI 集成指南

## 概述

本项目已成功集成 Spring AI，提供了与AI模型交互的完整功能。Spring AI 是 Spring 生态系统中的官方 AI 框架，提供了简洁的 API 来与大语言模型进行交互。

## 依赖配置

在 `pom.xml` 中添加了以下依赖：

```xml
<!-- Spring AI OpenAI Starter -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>

<!-- Spring AI Core -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-core</artifactId>
</dependency>
```

## 配置说明

在 `application.properties` 中配置 Spring AI：

```properties
# Spring AI OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY:sk-053eb2d4f0d84801a184058fc63ea470}
spring.ai.openai.base-url=https://api.deepseek.com/v1
spring.ai.openai.chat.options.model=deepseek-chat
spring.ai.openai.chat.options.temperature=0.7
```

## 核心组件

### 1. SpringAIConfig.java
配置类，创建 ChatClient Bean：

```java
@Configuration
public class SpringAIConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
```

### 2. SpringAIService.java
服务层，提供AI聊天功能：

- `chat(String message)` - 简单聊天
- `chatWithSystem(String systemMessage, String userMessage)` - 带系统提示的聊天
- `chatStream(String message)` - 流式聊天
- `chatStreamWithSystem(String systemMessage, String userMessage)` - 带系统提示的流式聊天

### 3. SpringAIController.java
控制器层，提供REST API接口：

- `POST /api/spring-ai/chat` - 简单聊天
- `GET /api/spring-ai/chat` - GET方式测试
- `POST /api/spring-ai/chat-with-system` - 带系统提示的聊天
- `POST /api/spring-ai/chat-stream` - 流式聊天 (SSE)
- `POST /api/spring-ai/chat-stream-with-system` - 带系统提示的流式聊天 (SSE)

## API 使用示例

### 1. 简单聊天 (POST)

```bash
curl -X POST http://localhost:8080/api/spring-ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
```

**响应：**
```json
{
  "response": "你好！我是一个AI助手..."
}
```

### 2. 简单聊天 (GET)

```bash
curl "http://localhost:8080/api/spring-ai/chat?message=什么是Spring AI?"
```

### 3. 带系统提示的聊天

```bash
curl -X POST http://localhost:8080/api/spring-ai/chat-with-system \
  -H "Content-Type: application/json" \
  -d '{
    "systemMessage": "你是一个专业的编程助手",
    "userMessage": "如何学习Java？"
  }'
```

### 4. 流式聊天

```bash
curl -X POST http://localhost:8080/api/spring-ai/chat-stream \
  -H "Content-Type: application/json" \
  -d '{"message": "请写一首诗"}'
```

## Java 代码示例

### 注入服务

```java
@Autowired
private SpringAIService springAIService;
```

### 简单聊天

```java
String response = springAIService.chat("你好");
System.out.println(response);
```

### 带系统提示的聊天

```java
String response = springAIService.chatWithSystem(
    "你是一个专业的编程专家",
    "什么是Spring Boot？"
);
System.out.println(response);
```

### 流式聊天

```java
springAIService.chatStream("请写一首诗")
    .subscribe(chunk -> System.out.print(chunk));
```

## 测试

### 运行测试脚本

Windows:
```bash
docs\test-spring-ai.bat
```

Linux/Mac:
```bash
chmod +x docs/test-spring-ai.sh
./docs/test-spring-ai.sh
```

### 运行单元测试

```bash
mvn test -Dtest=SpringAIServiceTest
```

## 与 LangChain4j 的对比

| 特性 | Spring AI | LangChain4j |
|------|-----------|-------------|
| 官方支持 | Spring 官方 | 社区项目 |
| 生态系统 | Spring 生态集成 | 独立框架 |
| API 设计 | 简洁直观 | 功能丰富 |
| 学习曲线 | 较低 | 中等 |
| 灵活性 | 高 | 非常高 |

## 注意事项

1. **API密钥安全**: 不要将API密钥提交到版本控制系统
2. **网络连接**: 需要能够访问AI API端点
3. **费用**: 使用AI API会产生费用，请注意控制使用量
4. **速率限制**: API有速率限制，生产环境需要考虑限流

## 扩展建议

1. **对话历史管理** - 实现多轮对话上下文保持
2. **向量数据库集成** - 集成RAG功能
3. **函数调用** - 让AI调用业务逻辑
4. **结构化输出** - 提取特定格式的数据
5. **监控与日志** - 添加API调用监控

## 参考资源

- Spring AI 官方文档: https://spring.io/projects/spring-ai
- Spring AI GitHub: https://github.com/spring-projects/spring-ai
- OpenAI API 文档: https://platform.openai.com/docs

## 总结

本项目成功集成了 Spring AI，提供了完整的 AI 聊天功能。您现在可以：

- ✅ 使用 Spring AI 进行 AI 对话
- ✅ 支持流式响应 (SSE)
- ✅ 支持系统提示定制
- ✅ 提供 REST API 接口
- ✅ 完整的测试和文档

Spring AI 与现有的 LangChain4j 可以共存，您可以根据需求选择使用哪个框架。
