@echo off
echo Testing DeepSeek Integration...
echo.

echo Test 1: Simple Chat
curl -X POST http://localhost:8080/api/deepseek/chat ^
  -H "Content-Type: application/json" ^
  -d "{\"message\": \"你好，请简单介绍一下你自己\"}"
echo.
echo.

echo Test 2: Chat with System Prompt
curl -X POST http://localhost:8080/api/deepseek/chat-with-system ^
  -H "Content-Type: application/json" ^
  -d "{\"systemMessage\": \"你是一个专业的编程助手\", \"userMessage\": \"如何用Java实现Hello World?\"}"
echo.
echo.

echo Tests completed!
pause
