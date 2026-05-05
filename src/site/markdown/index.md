# DeepChat 项目文档站点

## 🎯 项目简介

DeepChat 是一个基于 Spring AI 框架构建的智能聊天应用示例项目。它展示了如何利用 Spring Boot 和 Spring AI 快速集成大语言模型（LLM）能力，特别是针对 OpenAI 模型的集成以及检索增强生成（RAG）技术的实现。

### 核心功能

- **基础聊天**: 基于 OpenAI 模型的通用对话功能
- **RAG 聊天**: 支持上传或读取 PDF、TXT 等文档，基于文档内容进行智能问答
- **聊天记忆**: 维护多轮对话的上下文历史
- **Web 界面**: 提供简单的 HTML 前端页面用于交互测试

## 📚 文档导航

本站点集成了完整的项目文档，按功能分类组织：

### 🚀 [快速开始](docs/01-快速开始/)
新手入门指南，帮助你快速上手 DeepChat 核心功能

### 📖 [RAG知识库](docs/02-RAG知识库/)
检索增强生成（RAG）技术的完整文档，包括架构设计、集成指南、故障排除等

### 💬 [聊天记忆](docs/03-聊天记忆/)
多轮对话上下文管理相关文档

### 🤖 [AI模型集成](docs/04-AI模型集成/)
AI 模型集成与配置文档，包括 DeepSeek 和 Spring AI

### 🎨 [前端集成](docs/05-前端集成/)
前端界面开发与集成指南

### 🔧 [问题修复](docs/06-问题修复/)
常见问题与解决方案记录

### 🏗️ [构建与部署](docs/07-构建与部署/)
应用构建、打包与部署指南

### 🧪 [测试脚本](docs/08-测试脚本/)
自动化测试与诊断脚本

## 🛠️ 技术栈

- **后端框架**: Spring Boot 3.4.5
- **AI 框架**: Spring AI 1.0.0-M6
- **Java 版本**: JDK 17
- **构建工具**: Maven 3.6+

## 📦 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6+
- OpenAI API Key

### 运行项目

```bash
# 克隆项目
git clone <repository-url>

# 配置 API Key
# 在 src/main/resources/application.properties 中配置：
# spring.ai.openai.api-key=YOUR_OPENAI_API_KEY

# 运行项目
mvn spring-boot:run

# 访问应用
# http://localhost:8080
```

### 生成文档站点

```bash
# 生成 Maven Site
mvn site

# 查看生成的站点
# 打开 target/site/index.html
```

## 📝 推荐阅读路径

### 新手入门
1. 阅读 [帮助文档](docs/01-快速开始/HELP.html) 了解项目概况
2. 根据兴趣选择快速开始指南：
   - 想体验 RAG？→ [RAG 快速开始](docs/01-快速开始/RAG_QUICKSTART.html)
   - 想使用 DeepSeek？→ [DeepSeek 快速开始](docs/01-快速开始/QUICKSTART_DEEPSEEK.html)
   - 想了解聊天记忆？→ [聊天记忆快速开始](docs/01-快速开始/QUICKSTART_CHAT_MEMORY.html)

### RAG 开发
1. 快速开始：[RAG_QUICKSTART.html](docs/01-快速开始/RAG_QUICKSTART.html)
2. 理解架构：[RAG_ARCHITECTURE.html](docs/02-RAG知识库/RAG_ARCHITECTURE.html)
3. 日常参考：[RAG_CHEATSHEET.html](docs/02-RAG知识库/RAG_CHEATSHEET.html) ⭐
4. 遇到问题：[RAG_TROUBLESHOOTING.html](docs/02-RAG知识库/RAG_TROUBLESHOOTING.html)

### 生产部署
1. 构建指南：[AOT_GUIDE.html](docs/07-构建与部署/AOT_GUIDE.html)
2. 部署配置：[RAG_DEPLOYMENT.html](docs/02-RAG知识库/RAG_DEPLOYMENT.html)
3. 故障排查：[NATIVE_BUILD_TROUBLESHOOTING.html](docs/07-构建与部署/NATIVE_BUILD_TROUBLESHOOTING.html)

## 🔗 相关链接

- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [项目待办事项](../todos.md)

---

**最后更新**: 2026-05-05  
**文档版本**: 1.0  
**构建工具**: Maven Site Plugin
