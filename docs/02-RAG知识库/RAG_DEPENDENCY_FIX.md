# RAG 依赖问题修复说明

## ❌ 问题描述

在添加 RAG 功能后，Maven 构建失败，报错：

```
[ERROR] 'dependencies.dependency.version' for org.springframework.ai:spring-ai-spring-boot-starter-vector-store-simple:jar is missing.
```

## 🔍 问题原因

Spring AI 1.0.0-M6 版本中：
- **SimpleVectorStore** 已经包含在 `spring-ai-core` 依赖中
- **不需要**单独的 `spring-ai-spring-boot-starter-vector-store-simple` starter
- 该 starter 在当前版本的 Spring AI BOM 中不存在

## ✅ 解决方案

### 修复前的 pom.xml（错误）
```xml
<!-- ❌ 这个依赖不存在 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-spring-boot-starter-vector-store-simple</artifactId>
</dependency>
```

### 修复后的 pom.xml（正确）
```xml
<!-- ✅ SimpleVectorStore 已包含在 core 中 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-core</artifactId>
</dependency>

<!-- ✅ PDF 文档读取器（可选） -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-pdf-document-reader</artifactId>
</dependency>
```

## 📦 当前依赖清单

项目所需的完整依赖：

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring AI OpenAI Starter (包含 DeepSeek 支持) -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    </dependency>

    <!-- Spring AI Core (包含 SimpleVectorStore) -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-core</artifactId>
    </dependency>

    <!-- Spring AI PDF Document Reader (可选) -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-pdf-document-reader</artifactId>
    </dependency>
</dependencies>
```

## 🔧 SimpleVectorStore 使用说明

### 自动配置
SimpleVectorStore 会通过 `RagConfig.java` 自动配置：

```java
@Configuration
public class RagConfig {
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
```

### 使用示例
```java
@Autowired
private VectorStore vectorStore;

// 添加文档
Document doc = new Document("文本内容");
vectorStore.add(List.of(doc));

// 搜索文档
List<Document> results = vectorStore.similaritySearch(
    SearchRequest.builder()
        .query("查询文本")
        .topK(3)
        .build()
);
```

## 🚀 生产环境升级

如果需要升级到生产级向量数据库，只需：

### 1. Chroma
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-chroma-store-spring-boot-starter</artifactId>
</dependency>
```

```properties
spring.ai.vectorstore.chroma.client.host=http://localhost
spring.ai.vectorstore.chroma.client.port=8000
```

### 2. PGVector
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
</dependency>
```

### 3. Redis
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-vector-store-redis</artifactId>
</dependency>
```

**注意**：更换向量数据库时，代码无需修改，只需更改配置！

## 📝 关键要点

1. ✅ **SimpleVectorStore 不需要单独的 starter**
2. ✅ 它已经包含在 `spring-ai-core` 中
3. ✅ 通过 `@Bean` 手动配置即可使用
4. ✅ 适合开发和测试环境
5. ⚠️ 生产环境建议使用专业向量数据库

## 🎯 验证修复

运行以下命令验证：

```bash
mvn clean compile -DskipTests
```

应该看到：
```
[INFO] BUILD SUCCESS
```

## 📚 相关资源

- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [SimpleVectorStore API](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
- [RAG 集成文档](RAG_INTEGRATION.md)

---

**修复完成时间**: 2026-05-05  
**Spring AI 版本**: 1.0.0-M6
