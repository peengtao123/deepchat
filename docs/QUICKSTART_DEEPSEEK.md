# 快速启动指南 - DeepSeek 集成

## 前置要求

1. Java 17 或更高版本
2. Maven 3.6+
3. DeepSeek API Key（从 https://platform.deepseek.com/ 获取）

## 启动步骤

### 1. 配置 API Key

**方式一：使用环境变量（推荐）**

Windows PowerShell:
```powershell
$env:DEEPSEEK_API_KEY="sk-your-api-key-here"
```

Linux/Mac:
```bash
export DEEPSEEK_API_KEY="sk-your-api-key-here"
```

**方式二：直接修改配置文件**

编辑 `src/main/resources/application.properties`：
```properties
langchain4j.openai.api-key=sk-your-api-key-here
```

### 2. 构建项目

```bash
mvn clean package
```

### 3. 运行应用

```bash
mvn spring-boot:run
```

或者运行打包后的 JAR：
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 4. 测试 API

应用启动后，访问：
- http://localhost:8080/chat.html - Web 聊天界面
- http://localhost:8080/api/deepseek/chat - DeepSeek API 端点

**使用测试脚本：**

Windows:
```powershell
.\test-deepseek.bat
```

Linux/Mac:
```bash
chmod +x test-deepseek.sh
./test-deepseek.sh
```

**手动测试：**

```bash
curl -X POST http://localhost:8080/api/deepseek/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好"}'
```

## 验证集成成功

如果看到类似以下响应，说明 DeepSeek 集成成功：

```json
{
  "response": "你好！我是由DeepSeek开发的人工智能助手..."
}
```

## 常见问题

### Q: 如何切换回 OpenAI？
A: 修改 `application.properties`：
```properties
langchain4j.openai.base-url=https://api.openai.com/v1
langchain4j.openai.model-name=gpt-3.5-turbo
```

### Q: 如何使用 DeepSeek Coder 模型？
A: 修改模型名称：
```properties
langchain4j.openai.model-name=deepseek-coder
```

### Q: API 调用失败怎么办？
A: 检查以下几点：
1. API Key 是否正确
2. 网络连接是否正常
3. DeepSeek 账户是否有余额
4. 查看应用日志获取详细错误信息

## 下一步

- 查看 [DEEPSEEK_INTEGRATION.md](DEEPSEEK_INTEGRATION.md) 了解更多配置选项
- 查看 [API_EXAMPLES.md](API_EXAMPLES.md) 了解所有可用的 API
- 访问 [LangChain4j 文档](https://docs.langchain4j.dev/) 学习更多功能
