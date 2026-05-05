# 🎉 Spring Boot + LangChain4j 集成项目

## 📋 项目简介

这是一个集成了LangChain4j的Spring Boot应用，提供了完整的AI聊天功能。您可以使用它与OpenAI的GPT模型进行交互，实现智能对话、编程助手、翻译等多种AI应用场景。

## ✨ 核心特性

- ✅ **完整的API接口** - 提供8个REST API端点
- ✅ **Web聊天界面** - 美观的浏览器聊天界面
- ✅ **多角色支持** - 预设5种专业角色
- ✅ **灵活配置** - 支持环境变量和配置文件
- ✅ **完整文档** - 详细的使用说明和示例
- ✅ **测试脚本** - 一键测试所有API

## 🚀 快速开始

### 1️⃣ 配置API密钥

编辑 `src/main/resources/application.properties`:
```properties
langchain4j.openai.api-key=sk-your-openai-api-key-here
```

或者设置环境变量:
```bash
export OPENAI_API_KEY=sk-your-openai-api-key-here
```

### 2️⃣ 运行应用

```bash
mvn spring-boot:run
```

### 3️⃣ 访问应用

- **Web聊天**: http://localhost:8080/chat.html
- **API测试**: 运行 `test-api.bat` (Windows) 或 `test-api.sh` (Linux/Mac)

## 📁 项目结构

```
demo/
├── src/main/java/com/example/demo/
│   ├── config/
│   │   └── LangChain4jConfig.java          # LangChain4j配置
│   ├── controller/
│   │   ├── AIController.java               # 基础聊天API
│   │   └── ExampleController.java          # 场景化API
│   ├── service/
│   │   └── AIService.java                  # AI服务层
│   ├── util/
│   │   └── ChatAssistant.java              # AI助手工具类
│   └── DemoApplication.java                # 主应用
├── src/main/resources/
│   ├── static/
│   │   └── chat.html                       # Web聊天界面
│   └── application.properties              # 配置文件
├── src/test/java/com/example/demo/service/
│   └── AIServiceTest.java                  # 单元测试
├── pom.xml                                 # Maven配置
├── LANGCHAIN4J_README.md                   # 详细文档
├── API_EXAMPLES.md                         # API示例
├── INTEGRATION_SUMMARY.md                  # 集成总结
├── test-api.bat                            # Windows测试脚本
└── test-api.sh                             # Linux/Mac测试脚本
```

## 🎯 API端点

### 基础聊天API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/ai/chat` | POST | 简单聊天 |
| `/api/ai/chat` | GET | GET方式测试 |
| `/api/ai/chat-with-system` | POST | 带系统提示 |
| `/api/ai/advanced-chat` | POST | 高级聊天 ⭐推荐 |

### 场景化API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/ai/examples/programming` | POST | 编程助手 |
| `/api/ai/examples/translate` | POST | 翻译助手 |
| `/api/ai/examples/code-review` | POST | 代码审查 |
| `/api/ai/examples/tutor` | POST | 学习导师 |

## 💡 使用示例

### JavaScript
```javascript
const response = await fetch('/api/ai/advanced-chat', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({
    message: "什么是Spring Boot？",
    systemPrompt: "你是一个专业的编程专家"
  })
});

const data = await response.json();
console.log(data.response);
```

### Python
```python
import requests

response = requests.post(
    'http://localhost:8080/api/ai/advanced-chat',
    json={
        'message': '什么是Spring Boot？',
        'systemPrompt': '你是一个专业的编程专家'
    }
)

print(response.json()['response'])
```

### Java
```java
@Autowired
private ChatAssistant chatAssistant;

String response = chatAssistant.chatWithSystem(
    ChatAssistant.SystemPrompts.PROGRAMMING_EXPERT,
    "什么是Spring Boot？"
);
```

## 🎭 预设角色

在 `ChatAssistant.SystemPrompts` 中定义了以下角色：

1. **DEFAULT_ASSISTANT** - 通用AI助手
2. **PROGRAMMING_EXPERT** - 编程专家
3. **TRANSLATOR** - 翻译助手
4. **CODE_REVIEWER** - 代码审查专家
5. **TUTOR** - 学习导师

您也可以自定义角色：
```json
{
  "message": "你的问题",
  "systemPrompt": "你自定义的角色描述..."
}
```

## 📚 文档

- **[LANGCHAIN4J_README.md](LANGCHAIN4J_README.md)** - 完整的使用说明
- **[API_EXAMPLES.md](API_EXAMPLES.md)** - API调用示例
- **[INTEGRATION_SUMMARY.md](INTEGRATION_SUMMARY.md)** - 集成总结

## 🔧 技术栈

- **Spring Boot**: 4.0.6
- **Java**: 17
- **LangChain4j**: 1.0.0-beta3
- **OpenAI API**: GPT-3.5-turbo

## ⚠️ 重要提示

1. **API密钥安全**: 请勿将API密钥提交到版本控制系统
2. **费用控制**: 使用OpenAI API会产生费用，请监控使用情况
3. **网络要求**: 需要能够访问OpenAI API服务器
4. **速率限制**: OpenAI API有请求频率限制

## 🎨 扩展建议

- [ ] 添加对话历史管理
- [ ] 实现流式响应（SSE）
- [ ] 集成向量数据库（RAG）
- [ ] 支持更多LLM模型
- [ ] 添加用户认证
- [ ] 实现API限流
- [ ] 添加监控和日志
- [ ] 实现回答缓存

## 📞 支持

如有问题，请参考文档或查看示例代码。

## 📄 许可证

MIT License

---

**祝您使用愉快！** 🎊
