# Spring Boot + LangChain4j 集成示例

这个项目展示了如何在Spring Boot应用中集成LangChain4j来实现AI聊天功能。

## 功能特性

- ✅ 集成LangChain4j框架
- ✅ 支持OpenAI API调用
- ✅ RESTful API接口
- ✅ Web聊天界面
- ✅ 可配置的模型参数

## 前置要求

1. Java 17+
2. Maven 3.6+
3. OpenAI API密钥

## 配置说明

### 1. 设置OpenAI API密钥

在 `src/main/resources/application.properties` 文件中配置：

```properties
langchain4j.openai.api-key=your-openai-api-key-here
```

或者通过环境变量设置：

```bash
export OPENAI_API_KEY=your-openai-api-key-here
```

### 2. 可选配置

```properties
langchain4j.openai.model-name=gpt-3.5-turbo
langchain4j.openai.temperature=0.7
```

## 运行项目

```bash
mvn spring-boot:run
```

## API接口

### 1. POST /api/ai/chat
简单聊天接口

**请求示例：**
```json
{
  "message": "你好，请介绍一下你自己"
}
```

**响应示例：**
```json
{
  "response": "你好！我是一个AI助手..."
}
```

### 2. POST /api/ai/chat-with-system
带系统提示的聊天接口

**请求示例：**
```json
{
  "systemMessage": "你是一个专业的编程助手",
  "userMessage": "如何学习Java？"
}
```

### 3. GET /api/ai/chat?message=你好
GET方式的测试接口

## Web界面

访问 `http://localhost:8080/chat.html` 使用Web聊天界面。

## 项目结构

```
src/main/java/com/example/demo/
├── config/
│   └── LangChain4jConfig.java    # LangChain4j配置类
├── controller/
│   └── AIController.java         # REST API控制器
├── service/
│   └── AIService.java            # AI服务层
└── DemoApplication.java          # 主应用类

src/main/resources/
├── static/
│   └── chat.html                 # Web聊天界面
└── application.properties        # 配置文件
```

## 核心组件说明

### LangChain4jConfig
配置ChatLanguageModel Bean，用于与AI模型交互。

### AIService
提供聊天功能的业务逻辑层。

### AIController
提供REST API接口，支持多种聊天方式。

## 扩展建议

1. **添加记忆功能**：实现对话历史管理
2. **支持多模型**：添加对其他LLM模型的支持
3. **流式响应**：实现SSE流式输出
4. **向量数据库**：集成RAG功能
5. **用户认证**：添加API访问控制

## 注意事项

⚠️ **重要**：请不要将OpenAI API密钥提交到版本控制系统中。

## 依赖说明

- `langchain4j-spring-boot-starter`: LangChain4j Spring Boot集成
- `langchain4j-open-ai-spring-boot-starter`: OpenAI模型支持

## 许可证

MIT License
