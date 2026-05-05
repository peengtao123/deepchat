@echo off
chcp 65001 >nul
echo ========================================
echo   RAG 功能测试脚本
echo ========================================
echo.

set BASE_URL=http://localhost:8080/api/rag

echo [1] 测试添加文档...
curl -X POST %BASE_URL%/document/add ^
  -H "Content-Type: application/json" ^
  -d "{\"text\":\"Spring Boot是一个用于构建生产级应用的框架，具有自动配置、嵌入式服务器等特点。\",\"title\":\"Spring Boot测试\",\"source\":\"test\"}"
echo.
echo.

timeout /t 2 /nobreak >nul

echo [2] 测试搜索文档...
curl "%BASE_URL%/search?query=Spring+Boot&topK=3"
echo.
echo.

timeout /t 2 /nobreak >nul

echo [3] 测试RAG问答...
curl -X POST %BASE_URL%/chat ^
  -H "Content-Type: application/json" ^
  -d "{\"query\":\"Spring Boot有什么特点？\"}"
echo.
echo.

timeout /t 2 /nobreak >nul

echo [4] 获取使用指南...
curl "%BASE_URL%/guide"
echo.
echo.

echo ========================================
echo   测试完成！
echo ========================================
echo.
echo 访问 http://localhost:8080/rag-chat.html 体验完整功能
pause
