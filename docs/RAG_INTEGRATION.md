# RAG (检索增强生成) 集成方案

## 📋 概述

本项目已成功集成 RAG (Retrieval-Augmented Generation) 功能，结合了信息检索和文本生成技术，能够基于知识库提供准确、有依据的回答。

## 🏗️ 架构组成

### 核心组件

1. **RagService** - RAG 核心服务
   - 文档管理和索引
   - 向量相似度搜索
   - 文件加载功能

2. **RagChatService** - RAG 聊天服务
   - 结合检索结果和 LLM 生成回答
   - 支持流式和非流式响应

3. **RagController** - REST API 控制器
   - 提供完整的 HTTP API 接口
   - 支持文档管理、搜索、问答

4. **SimpleVectorStore** - 向量存储
   - 内存向量数据库（开发测试用）
   - 生产环境可替换为 Chroma、Pinecone 等

## 🚀 快速开始

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 访问前端界面

打开浏览器访问：http://localhost:8080/rag-chat.html

### 3. 自动加载示例数据

应用启动时会自动加载 5 个示例文档到知识库：
- Spring Boot 介绍
- RAG 技术介绍
- Java 语言特性
- 微服务架构介绍
- Docker 容器技术

## 📡 API 接口

### 文档管理

#### 添加单个文档
```http
POST /api/rag/document/add
Content-Type: application/json

{
  "text": "文档内容...",
  "title": "文档标题",
  "source": "来源标识"
}
```

#### 批量添加文档
```http
POST /api/rag/document/batch-add
Content-Type: application/json

[
  {
    "text": "文档1内容...",
    "title": "文档1标题",
    "source": "source1"
  },
  {
    "text": "文档2内容...",
    "title": "文档2标题",
    "source": "source2"
  }
]
```

#### 加载文本文件
```http
POST /api/rag/document/load-file?filePath=/path/to/file.txt&source=my-source
```

#### 加载目录
```http
POST /api/rag/document/load-directory?directoryPath=/path/to/docs&fileExtension=.txt
```

### 搜索功能

#### 搜索相关文档
```http
GET /api/rag/search?query=关键词&topK=5
```

返回最相关的 K 个文档及其元数据。

### 问答功能

#### RAG 问答（非流式）
```http
POST /api/rag/chat
Content-Type: application/json

{
  "query": "你的问题是什么？"
}
```

响应：
```json
{
  "success": true,
  "query": "你的问题是什么？",
  "answer": "基于知识库生成的答案..."
}
```

#### RAG 问答（流式）
```http
POST /api/rag/chat/stream
Content-Type: application/json

{
  "query": "你的问题是什么？"
}
```

返回 Server-Sent Events (SSE) 流式响应。

### 获取使用指南
```http
GET /api/rag/guide
```

## 💡 使用示例

### 示例 1: 添加文档并问答

```bash
# 1. 添加文档
curl -X POST http://localhost:8080/api/rag/document/add \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Spring AI 是 Spring 生态系统中的 AI 应用开发框架...",
    "title": "Spring AI 介绍",
    "source": "spring-ai-docs"
  }'

# 2. 进行问答
curl -X POST http://localhost:8080/api/rag/chat \
  -H "Content-Type: application/json" \
  -d '{
    "query": "Spring AI 有什么特点？"
  }'
```

### 示例 2: 搜索相关文档

```bash
curl "http://localhost:8080/api/rag/search?query=Spring+Boot&topK=3"
```

### 示例 3: 加载本地文件

```bash
# 准备一个文本文件
echo "这是测试文档的内容..." > test.txt

# 加载文件
curl -X POST "http://localhost:8080/api/rag/document/load-file?filePath=test.txt&source=test"
```

## 🔧 配置说明

### 当前配置

项目使用以下配置：

- **向量存储**: SimpleVectorStore（内存存储）
- **嵌入模型**: 使用 OpenAI/DeepSeek 的嵌入 API
- **LLM**: DeepSeek Chat 模型

### 生产环境建议

对于生产环境，建议替换为以下组件：

#### 向量数据库选项

1. **Chroma**
   ```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-chroma-store-spring-boot-starter</artifactId>
   </dependency>
   ```

2. **Pinecone**
   ```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-pinecone-store-spring-boot-starter</artifactId>
   </dependency>
   ```

3. **Milvus**
   ```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-milvus-store-spring-boot-starter</artifactId>
   </dependency>
   ```

4. **Weaviate**
   ```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-weaviate-store-spring-boot-starter</artifactId>
   </dependency>
   ```

## 📊 RAG 工作流程

```
用户提问
   ↓
1. 问题向量化（Embedding）
   ↓
2. 向量相似度搜索
   ↓
3. 检索相关文档（Top-K）
   ↓
4. 构建增强 Prompt
   （上下文 + 问题）
   ↓
5. LLM 生成回答
   ↓
6. 返回答案给用户
```

## 🎯 优势特点

✅ **减少幻觉** - 基于真实文档生成回答  
✅ **可追溯** - 提供回答的来源依据  
✅ **实时更新** - 无需重新训练即可更新知识  
✅ **成本效益** - 利用现有知识库  
✅ **准确性高** - 结合检索和生成双重保障  

## 📝 注意事项

1. **文档分割**: 长文档会自动按段落分割，便于精确检索
2. **元数据**: 建议为文档添加来源、标题等元数据
3. **向量维度**: 确保嵌入模型的维度与向量存储匹配
4. **性能优化**: 大量文档时建议使用专业向量数据库
5. **内存限制**: SimpleVectorStore 适合小规模测试，生产环境需使用外部向量库

## 🔍 故障排查

### 问题 1: 检索不到相关文档

**解决方案**:
- 检查文档是否成功添加到向量存储
- 尝试调整 `topK` 参数
- 检查查询语句是否与文档内容相关

### 问题 2: 回答不准确

**解决方案**:
- 增加更多相关文档
- 优化文档质量和内容
- 调整系统提示词（system prompt）

### 问题 3: 响应速度慢

**解决方案**:
- 减少 `topK` 值
- 使用更快的向量数据库
- 优化文档分割策略

## 📚 扩展阅读

- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [RAG 技术详解](https://arxiv.org/abs/2005.11401)
- [向量数据库对比](https://www.pinecone.io/learn/vector-database/)

## 🎉 总结

本 RAG 集成方案提供了完整的检索增强生成功能，包括：
- ✅ 文档管理和索引
- ✅ 向量相似度搜索
- ✅ 智能问答（流式/非流式）
- ✅ 友好的 Web 界面
- ✅ 完整的 REST API
- ✅ 示例数据和初始化

可以直接用于企业知识库、智能客服、文档搜索等场景！
