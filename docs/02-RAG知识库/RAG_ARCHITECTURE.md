# RAG 系统架构说明

## 🏗️ 整体架构

```
┌─────────────────────────────────────────────────────────────┐
│                     用户界面层                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ rag-chat.html│  │spring-ai-    │  │ chat.html    │      │
│  │ (RAG界面)     │  │chat.html     │  │ (简洁界面)    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            ↓ HTTP Requests
┌─────────────────────────────────────────────────────────────┐
│                     REST API 层                              │
│  ┌──────────────────────────────────────────────────┐       │
│  │          RagController                           │       │
│  │  - POST /api/rag/document/add                    │       │
│  │  - POST /api/rag/document/batch-add              │       │
│  │  - GET  /api/rag/search                          │       │
│  │  - POST /api/rag/chat                            │       │
│  │  - POST /api/rag/chat/stream                     │       │
│  └──────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     业务服务层                                │
│  ┌──────────────────────┐  ┌──────────────────────┐        │
│  │   RagService         │  │  RagChatService      │        │
│  │                      │  │                      │        │
│  │ • addTextDocument()  │  │ • chatWithRag()      │        │
│  │ • addDocuments()     │  │ • chatStreamWithRag()│        │
│  │ • loadTextFile()     │  └──────────────────────┘        │
│  │ • loadDirectory()    │                                  │
│  │ • searchSimilar()    │                                  │
│  │ • answerWithRag()    │                                  │
│  └──────────────────────┘                                  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     AI 组件层                                │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │  VectorStore     │  │  ChatClient      │                │
│  │                  │  │                  │                │
│  │ • 向量存储       │  │ • LLM 调用       │                │
│  │ • 相似度搜索     │  │ • Prompt 构建    │                │
│  │ • 文档管理       │  │ • 流式响应       │                │
│  └──────────────────┘  └──────────────────┘                │
│           ↓                       ↓                         │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │ EmbeddingModel   │  │ DeepSeek Chat    │                │
│  │                  │  │                  │                │
│  │ • 文本向量化     │  │ • 文本生成       │                │
│  └──────────────────┘  └──────────────────┘                │
└─────────────────────────────────────────────────────────────┘
```

## 🔄 RAG 工作流程详解

### 阶段 1: 文档索引（离线）

```
原始文档
   ↓
┌─────────────────┐
│  文档加载器      │ ← 读取 TXT/PDF/MD 等文件
└─────────────────┘
   ↓
┌─────────────────┐
│  文档分割器      │ ← 按段落/句子分割
└─────────────────┘
   ↓
┌─────────────────┐
│ Embedding Model  │ ← 转换为向量（如 1536 维）
└─────────────────┘
   ↓
┌─────────────────┐
│  Vector Store    │ ← 存储向量 + 元数据
└─────────────────┘
```

### 阶段 2: 检索增强问答（在线）

```
用户问题
   ↓
┌─────────────────┐
│ Embedding Model  │ ← 问题向量化
└─────────────────┘
   ↓
┌─────────────────┐
│  Vector Store    │ ← 相似度搜索（Top-K）
└─────────────────┘
   ↓
相关文档片段
   ↓
┌─────────────────────────────────┐
│   Prompt 构建                   │
│                                 │
│  System: 你是智能助手...        │
│  Context: [文档1] [文档2] ...   │
│  User: 用户问题                 │
└─────────────────────────────────┘
   ↓
┌─────────────────┐
│  LLM (DeepSeek) │ ← 生成回答
└─────────────────┘
   ↓
最终答案（带引用）
```

## 📦 核心组件说明

### 1. RagService
**职责**: 文档管理和向量检索

**主要方法**:
- `addTextDocument()` - 添加单个文档
- `loadTextFile()` - 加载文本文件
- `searchSimilarDocuments()` - 相似度搜索
- `answerWithRag()` - 检索相关文档

**技术细节**:
- 使用 Spring AI 的 VectorStore 接口
- 支持元数据（source, title, filename）
- 自动文档分割和向量化

### 2. RagChatService
**职责**: RAG 增强的聊天功能

**主要方法**:
- `chatWithRag()` - 非流式问答
- `chatStreamWithRag()` - 流式问答

**工作流程**:
1. 调用 RagService 检索文档
2. 构建增强 Prompt
3. 调用 LLM 生成回答
4. 返回答案

### 3. RagController
**职责**: 提供 REST API 接口

**API 端点**:
- `POST /api/rag/document/add` - 添加文档
- `GET /api/rag/search` - 搜索文档
- `POST /api/rag/chat` - 问答
- `POST /api/rag/chat/stream` - 流式问答

### 4. RagConfig
**职责**: 配置 RAG 组件

**Bean 配置**:
- `VectorStore` - 向量存储（SimpleVectorStore）
- 依赖注入 EmbeddingModel

## 🔧 关键技术点

### 1. 向量相似度搜索
```java
SearchRequest request = SearchRequest.builder()
    .query(query)
    .topK(3)
    .build();
List<Document> results = vectorStore.similaritySearch(request);
```

**相似度算法**: Cosine Similarity（余弦相似度）

### 2. 文档元数据
```java
Document doc = new Document(text);
doc.getMetadata().put("source", "spring-docs");
doc.getMetadata().put("title", "Spring Boot介绍");
doc.getMetadata().put("filename", "spring.txt");
```

### 3. Prompt 工程
```java
String systemPrompt = 
    "你是一个智能助手，请基于以下提供的上下文信息来回答用户的问题。\n" +
    "如果上下文中的信息不足以回答问题，请诚实地告知用户。";

String userPrompt = 
    "以下是相关的参考信息：\n\n" + context + 
    "\n\n用户问题：" + query;
```

## 🎯 性能优化策略

### 1. 文档分割
- **策略**: 按段落分割（`\n\n`）
- **优势**: 精确检索，减少噪声
- **可调**: 可改为按句子、固定长度等

### 2. Top-K 选择
- **默认值**: 3
- **权衡**: 
  - K 太小 → 信息不足
  - K 太大 → 噪声增加，成本高
- **建议**: 根据场景调整（3-10）

### 3. 向量数据库选择
| 方案 | 适用场景 | 优势 |
|------|---------|------|
| SimpleVectorStore | 开发测试 | 简单、无需额外部署 |
| Chroma | 中小规模 | 开源、易用 |
| Pinecone | 生产环境 | 托管服务、高性能 |
| Milvus | 大规模 | 高性能、分布式 |

## 🔐 安全考虑

### 1. API 认证
当前未实现，生产环境建议添加：
- JWT Token 认证
- API Key 验证
- Spring Security 集成

### 2. 输入验证
- 检查空输入
- 限制文本长度
- 防止注入攻击

### 3. 速率限制
- 限制 API 调用频率
- 防止滥用

## 📊 监控和日志

### 关键指标
- 文档数量
- 平均检索时间
- LLM 响应时间
- API 调用次数

### 日志记录
```java
System.out.println("成功加载文件: " + filePath + ", 共 " + documents.size() + " 个段落");
```

生产环境建议使用：
- SLF4J + Logback
- Prometheus + Grafana
- ELK Stack

## 🚀 扩展方向

### 1. 多模态支持
- PDF 文档解析
- 图片 OCR
- 视频字幕提取

### 2. 高级检索
- 混合检索（关键词 + 向量）
- 重排序（Re-ranking）
- 查询改写

### 3. 对话历史
- 结合 ChatMemory
- 多轮对话理解
- 上下文感知

### 4. 知识图谱
- 实体识别
- 关系抽取
- 图数据库集成

## 📝 总结

本 RAG 系统采用分层架构设计：
- ✅ 清晰的责任分离
- ✅ 易于扩展和维护
- ✅ 支持多种向量数据库
- ✅ 完整的 REST API
- ✅ 友好的用户界面

适合快速构建企业知识库、智能客服、文档搜索等应用！
