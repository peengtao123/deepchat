# DeepSeek 集成指南

本项目已成功集成 DeepSeek AI 模型，通过 LangChain4j 框架实现。

## 配置说明

### 1. 获取 DeepSeek API Key
- 访问 [DeepSeek 官网](https://platform.deepseek.com/) 注册账号
- 在控制台生成 API Key

### 2. 配置环境变量
设置环境变量 `DEEPSEEK_API_KEY` 为你的 DeepSeek API Key：

**Windows:**
```powershell
$env:DEEPSEEK_API_KEY="your-deepseek-api-key-here"
```

**Linux/Mac:**
```bash
export DEEPSEEK_API_KEY="your-deepseek-api-key-here"
```

或者直接在 `application.properties` 中修改：
```properties
langchain4j.openai.api-key=your-deepseek-api-key-here
```

### 3. 配置参数说明

在 `application.properties` 中的配置项：

```properties
# DeepSeek API Key
langchain4j.openai.api-key=${DEEPSEEK_API_KEY:your-deepseek-api-key-here}

# DeepSeek API 基础 URL（OpenAI 兼容接口）
langchain4j.openai.base-url=https://api.deepseek.com/v1

# 模型名称
langchain4j.openai.model-name=deepseek-chat

# 温度参数 (0.0-1.0，越高越随机)
langchain4j.openai.temperature=0.7
```

## API 接口

### 1. 简单对话
**POST** `/api/deepseek/chat`

请求体：
```json
{
  "message": "你好，请介绍一下你自己"
}
```

### 2. 带系统提示的对话
**POST** `/api/deepseek/chat-with-system`

请求体：
```json
{
  "systemMessage": "你是一个专业的编程助手",
  "userMessage": "如何用 Java 实现快速排序？"
}
```

## 测试示例

使用 curl 测试：

```bash
# 简单对话
curl -X POST http://localhost:8080/api/deepseek/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好"}'

# 带系统提示的对话
curl -X POST http://localhost:8080/api/deepseek/chat-with-system \
  -H "Content-Type: application/json" \
  -d '{
    "systemMessage": "你是一个专业的Java开发者",
    "userMessage": "什么是Spring Boot?"
  }'
```

## 可用的 DeepSeek 模型

- `deepseek-chat`: 通用对话模型（默认）
- `deepseek-coder`: 代码专用模型

如需切换模型，修改 `application.properties` 中的 `langchain4j.openai.model-name` 即可。

## 注意事项

1. DeepSeek 使用 OpenAI 兼容的 API 格式，因此可以复用 LangChain4j 的 OpenAI 模块
2. 确保网络连接正常，能够访问 `https://api.deepseek.com`
3. API 调用会产生费用，请注意控制使用量
4. 建议在生产环境中使用环境变量管理 API Key，不要硬编码在代码中

## 故障排查

### 问题：API 调用失败
- 检查 API Key 是否正确
- 确认网络连接正常
- 验证账户余额是否充足

### 问题：响应速度慢
- 检查网络延迟
- 考虑降低 temperature 参数值
- 简化输入的 prompt

## 更多信息

- [DeepSeek 官方文档](https://platform.deepseek.com/docs)
- [LangChain4j 文档](https://docs.langchain4j.dev/)
