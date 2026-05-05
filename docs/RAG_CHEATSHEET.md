# RAG 快速参考卡片

## 🚀 一键启动
```bash
mvn spring-boot:run
```
访问：http://localhost:8080/rag-chat.html

## 📡 常用 API

### 添加文档
```bash
curl -X POST http://localhost:8080/api/rag/document/add \
  -H "Content-Type: application/json" \
  -d '{"text":"内容","title":"标题","source":"来源"}'
```

### 搜索文档
```bash
curl "http://localhost:8080/api/rag/search?query=关键词&topK=3"
```

### RAG 问答
```bash
curl -X POST http://localhost:8080/api/rag/chat \
  -H "Content-Type: application/json" \
  -d '{"query":"你的问题"}'
```

## 📂 核心文件

| 文件 | 说明 |
|------|------|
| `RagService.java` | RAG 核心服务 |
| `RagChatService.java` | RAG 聊天服务 |
| `RagController.java` | REST API |
| `rag-chat.html` | Web 界面 |

## 🎯 功能特性

✅ 文档管理和索引  
✅ 向量相似度搜索  
✅ 智能问答（流式/非流式）  
✅ 自动加载示例数据  
✅ 完整的 REST API  
✅ 现代化 Web 界面  

## 📊 工作流程

```
提问 → 向量化 → 检索文档 → 构建Prompt → LLM生成 → 回答
```

## 🔧 技术栈

- Spring AI
- SimpleVectorStore
- DeepSeek Chat
- Embedding API

## 📖 文档

- [详细文档](docs/RAG_INTEGRATION.md)
- [快速开始](RAG_QUICKSTART.md)
- [架构说明](docs/RAG_ARCHITECTURE.md)
- [完整总结](docs/RAG_SUMMARY.md)

## 💡 提示

- 应用启动时自动加载 5 个示例文档
- 可以直接在 Web 界面操作
- 支持批量添加和文件加载
- 生产环境建议替换向量数据库
