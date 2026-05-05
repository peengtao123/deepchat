# LangChain4j API 使用示例

## 基础聊天API

### 1. 简单聊天 (POST)
```bash
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
```

**响应：**
```json
{
  "response": "你好！我是一个AI助手..."
}
```

### 2. GET方式测试
```bash
curl "http://localhost:8080/api/ai/chat?message=你好"
```

### 3. 带系统提示的聊天
```bash
curl -X POST http://localhost:8080/api/ai/chat-with-system \
  -H "Content-Type: application/json" \
  -d '{
    "systemMessage": "你是一个专业的编程助手",
    "userMessage": "如何学习Java？"
  }'
```

### 4. 高级聊天（推荐）
```bash
curl -X POST http://localhost:8080/api/ai/advanced-chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "什么是Spring Boot？",
    "systemPrompt": "你是一个专业的编程专家，精通各种编程语言和技术。"
  }'
```

## 场景化API示例

### 编程助手
```bash
curl -X POST http://localhost:8080/api/ai/examples/programming \
  -H "Content-Type: application/json" \
  -d '{"question": "如何创建一个Spring Boot项目？"}'
```

### 翻译助手
```bash
curl -X POST http://localhost:8080/api/ai/examples/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello, World!",
    "targetLanguage": "中文"
  }'
```

### 代码审查
```bash
curl -X POST http://localhost:8080/api/ai/examples/code-review \
  -H "Content-Type: application/json" \
  -d '{
    "code": "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello\");\n    }\n}"
  }'
```

### 学习导师
```bash
curl -X POST http://localhost:8080/api/ai/examples/tutor \
  -H "Content-Type: application/json" \
  -d '{"question": "请解释什么是面向对象编程？"}'
```

## JavaScript/_fetch 示例

```javascript
// 简单聊天
async function chat(message) {
  const response = await fetch('/api/ai/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ message: message })
  });
  
  const data = await response.json();
  return data.response;
}

// 使用示例
chat('你好').then(response => {
  console.log(response);
});
```

## Python 示例

```python
import requests
import json

# 简单聊天
def chat(message):
    url = "http://localhost:8080/api/ai/chat"
    payload = {"message": message}
    headers = {"Content-Type": "application/json"}
    
    response = requests.post(url, json=payload, headers=headers)
    return response.json()["response"]

# 使用示例
result = chat("你好")
print(result)

# 编程助手
def programming_help(question):
    url = "http://localhost:8080/api/ai/examples/programming"
    payload = {"question": question}
    headers = {"Content-Type": "application/json"}
    
    response = requests.post(url, json=payload, headers=headers)
    return response.json()["response"]

# 使用示例
answer = programming_help("什么是REST API？")
print(answer)
```

## Java 示例

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class AIChatExample {
    
    public static String chat(String message) throws Exception {
        String json = "{\"message\": \"" + message + "\"}";
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/ai/chat"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        return response.body();
    }
    
    public static void main(String[] args) throws Exception {
        String result = chat("你好");
        System.out.println(result);
    }
}
```

## 可用的系统提示模板

在 `ChatAssistant.SystemPrompts` 中预定义了以下模板：

- `DEFAULT_ASSISTANT`: 通用AI助手
- `PROGRAMMING_EXPERT`: 编程专家
- `TRANSLATOR`: 翻译助手
- `CODE_REVIEWER`: 代码审查专家
- `TUTOR`: 学习导师

你也可以自定义系统提示：

```json
{
  "message": "你的问题",
  "systemPrompt": "你是一个自定义角色的AI助手..."
}
```

## 错误处理

所有API在出错时都会返回包含error字段的JSON：

```json
{
  "error": "错误信息"
}
```

## 注意事项

1. 确保已配置有效的OpenAI API密钥
2. API调用需要网络连接
3. 注意API调用频率限制
4. 生产环境建议添加认证和限流机制
