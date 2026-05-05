#!/bin/bash
# LangChain4j API 测试脚本
# 使用前请确保应用已启动：mvn spring-boot:run

echo "=== 测试1: 简单聊天 ==="
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
echo -e "\n"

echo "=== 测试2: GET方式测试 ==="
curl "http://localhost:8080/api/ai/chat?message=什么是Spring Boot?"
echo -e "\n"

echo "=== 测试3: 带系统提示的聊天 ==="
curl -X POST http://localhost:8080/api/ai/chat-with-system \
  -H "Content-Type: application/json" \
  -d '{"systemMessage": "你是一个专业的编程助手", "userMessage": "如何学习Java？"}'
echo -e "\n"

echo "=== 测试4: 高级聊天 ==="
curl -X POST http://localhost:8080/api/ai/advanced-chat \
  -H "Content-Type: application/json" \
  -d '{"message": "什么是REST API？", "systemPrompt": "你是一个专业的编程专家，精通各种编程语言和技术。"}'
echo -e "\n"

echo "=== 测试5: 编程助手 ==="
curl -X POST http://localhost:8080/api/ai/examples/programming \
  -H "Content-Type: application/json" \
  -d '{"question": "如何创建一个Spring Boot项目？"}'
echo -e "\n"

echo "=== 测试6: 翻译助手 ==="
curl -X POST http://localhost:8080/api/ai/examples/translate \
  -H "Content-Type: application/json" \
  -d '{"text": "Hello, World!", "targetLanguage": "中文"}'
echo -e "\n"

echo "=== 测试7: 代码审查 ==="
curl -X POST http://localhost:8080/api/ai/examples/code-review \
  -H "Content-Type: application/json" \
  -d '{"code": "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello\");\n    }\n}"}'
echo -e "\n"

echo "=== 测试8: 学习导师 ==="
curl -X POST http://localhost:8080/api/ai/examples/tutor \
  -H "Content-Type: application/json" \
  -d '{"question": "请解释什么是面向对象编程？"}'
echo -e "\n"

echo "=== 测试完成 ==="
echo ""
echo "Web聊天界面: http://localhost:8080/chat.html"
