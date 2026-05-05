#!/bin/bash

echo "========================================"
echo "测试多轮对话记忆功能"
echo "========================================"
echo ""

SESSION_ID="test-session-$RANDOM"
echo "会话ID: $SESSION_ID"
echo ""

echo "=== 第一轮对话：介绍自己 ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\", \"message\": \"我叫张三，今年25岁\"}"
echo ""
echo ""

sleep 2

echo "=== 第二轮对话：提供更多信息 ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\", \"message\": \"我是一名软件工程师，喜欢编程\"}"
echo ""
echo ""

sleep 2

echo "=== 第三轮对话：测试记忆（应该记得名字和年龄） ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\", \"message\": \"我叫什么名字？我多大了？\"}"
echo ""
echo ""

sleep 2

echo "=== 第四轮对话：测试记忆（应该记得职业） ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\", \"message\": \"我的职业是什么？我喜欢什么？\"}"
echo ""
echo ""

sleep 2

echo "=== 清除会话记忆 ==="
curl -X POST http://localhost:8080/api/spring-ai/clear-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\"}"
echo ""
echo ""

sleep 2

echo "=== 第五轮对话：记忆已清除（不应该记得之前的信息） ==="
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\": \"$SESSION_ID\", \"message\": \"我叫什么名字？\"}"
echo ""
echo ""

echo "========================================"
echo "测试完成！"
echo "========================================"
