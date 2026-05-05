#!/bin/bash

echo "========================================"
echo "测试 Spring AI 集成"
echo "========================================"
echo ""

echo "请确保应用正在运行在 http://localhost:8080"
echo ""
read -p "按回车键继续..."

echo "=== 测试1: 简单聊天 (POST) ==="
curl -X POST http://localhost:8080/api/spring-ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
echo -e "\n"

echo "=== 测试2: 简单聊天 (GET) ==="
curl "http://localhost:8080/api/spring-ai/chat?message=什么是Spring AI?"
echo -e "\n"

echo "=== 测试3: 带系统提示的聊天 ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-system \
  -H "Content-Type: application/json" \
  -d '{"systemMessage": "你是一个专业的编程助手", "userMessage": "如何学习Java？"}'
echo -e "\n"

echo "=== 测试完成 ==="
echo ""
echo "Spring AI API端点列表:"
echo "- POST /api/spring-ai/chat - 简单聊天"
echo "- GET  /api/spring-ai/chat - GET方式测试"
echo "- POST /api/spring-ai/chat-with-system - 带系统提示的聊天"
echo "- POST /api/spring-ai/chat-stream - 流式聊天"
echo "- POST /api/spring-ai/chat-stream-with-system - 带系统提示的流式聊天"
