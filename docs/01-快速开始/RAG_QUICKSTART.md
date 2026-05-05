# RAG 集成方案 - 快速开始指南

## 🎯 什么是 RAG？

**RAG (Retrieval-Augmented Generation)** 检索增强生成，是一种结合信息检索和文本生成的 AI 技术架构。

### 核心优势
- ✅ **减少幻觉** - 基于真实文档生成回答
- ✅ **可追溯性** - 提供回答来源和依据
- ✅ **实时更新** - 无需重新训练即可更新知识
- ✅ **高准确性** - 检索+生成双重保障

## 🚀 快速启动

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. 访问界面
打开浏览器访问：http://localhost:8080

你会看到三个选项：
- 🧠 **RAG 智能问答**（新功能）
- 🌱 Spring AI 聊天
- 💬 简洁聊天

点击 **RAG 智能问答** 开始体验！

## 📋 功能特性

### ✨ 核心功能

1. **文档管理**
   - 添加单个/批量文档
   - 加载文本文件
   - 加载整个目录
   - 自动向量化索引

2. **智能搜索**
   - 向量相似度搜索
   - 返回最相关的 Top-K 文档
   - 显示元数据（来源、标题等）

3. **RAG 问答**
   - 基于知识库的智能问答
   - 支持流式和非流式响应
   - 准确引用文档内容

4. **示例数据**
   - 启动时自动加载 5 个示例文档
   - 涵盖 Spring Boot、RAG、Java、微服务、Docker

## 🧪 测试方法

### 方法 1: Web 界面（推荐）
访问 http://localhost:8080/rag-chat.html

### 方法 2: 测试脚本
```bash
docs\test-rag.bat
```

### 方法 3: API 测试

#### 添加文档
```bash
curl -X POST http://localhost:8080/api/rag/document/add \
  -H "Content-Type: application/json" \
  -d "{\"text\":\"你的文档内容\",\"title\":\"标题\",\"source\":\"来源\"}"
```

#### 搜索文档
```bash
curl "http://localhost:8080/api/rag/search?query=关键词&topK=3"
```

#### RAG 问答
```bash
curl -X POST http://localhost:8080/api/rag/chat \
  -H "Content-Type: application/json" \
  -d "{\"query\":\"你的问题\"}"
```

## 📁 项目结构

```
src/main/java/com/example/demo/
├── config/
│   └── RagConfig.java              # RAG 配置类
├── controller/
│   └── RagController.java          # REST API 控制器
├── service/
│   ├── RagService.java             # RAG 核心服务
│   ├── RagChatService.java         # RAG 聊天服务
│   └── SampleDataInitializer.java  # 示例数据初始化
└── ...

src/main/resources/static/
└── rag-chat.html                   # RAG Web 界面

docs/
├── RAG_INTEGRATION.md              # 详细文档
└── test-rag.bat                    # 测试脚本
```

## 🔧 技术栈

- **Spring AI** - AI 应用开发框架
- **SimpleVectorStore** - 内存向量存储（开发用）
- **DeepSeek Chat** - 大语言模型
- **Embedding API** - 文本向量化
- **React + Vanilla JS** - 前端界面

## 📊 工作流程

```
用户提问
   ↓
1. 问题向量化（Embedding Model）
   ↓
2. 向量相似度搜索（Vector Store）
   ↓
3. 检索相关文档（Top-K）
   ↓
4. 构建增强 Prompt
   （系统提示 + 上下文 + 问题）
   ↓
5. LLM 生成回答（DeepSeek）
   ↓
6. 返回答案给用户
```

## 🎓 使用示例

### 示例 1: 添加技术文档
```json
POST /api/rag/document/add
{
  "text": "Microservices architecture is an approach to developing a single application as a suite of small services...",
  "title": "微服务架构",
  "source": "tech-docs"
}
```

### 示例 2: 提问
```json
POST /api/rag/chat
{
  "query": "微服务架构有什么优势？"
}
```

系统会：
1. 检索与"微服务"相关的文档
2. 将相关文档作为上下文
3. 生成基于文档的准确回答

## ⚙️ 配置说明

当前配置在 `application.properties`：
```properties
spring.ai.openai.api-key=your-api-key
spring.ai.openai.base-url=https://api.deepseek.com
spring.ai.openai.chat.options.model=deepseek-chat
```

## 🔄 生产环境升级建议

### 向量数据库选项

当前使用 **SimpleVectorStore**（内存存储），适合开发测试。

生产环境建议替换为：

1. **Chroma** - 开源向量数据库
2. **Pinecone** - 托管向量数据库服务
3. **Milvus** - 高性能向量数据库
4. **Weaviate** - 云原生向量搜索引擎

只需更换依赖和配置，代码无需修改！

## ❓ 常见问题

### Q1: 为什么检索不到相关文档？
- 检查文档是否成功添加
- 尝试调整 topK 参数（增加返回数量）
- 确保查询词与文档内容相关

### Q2: 如何提高回答质量？
- 添加更多高质量文档
- 优化文档内容和结构
- 调整系统提示词

### Q3: 响应速度慢怎么办？
- 减少 topK 值
- 使用更快的向量数据库
- 优化文档分割策略

## 📚 更多资源

- [完整文档](docs/RAG_INTEGRATION.md)
- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [RAG 技术论文](https://arxiv.org/abs/2005.11401)

## 🎉 开始使用

现在你已经了解了 RAG 集成方案的所有内容！

**立即体验：**
1. 运行 `mvn spring-boot:run`
2. 访问 http://localhost:8080/rag-chat.html
3. 开始智能问答！

祝你使用愉快！🚀
