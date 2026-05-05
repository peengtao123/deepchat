package com.example.demo.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG 配置类
 * 配置向量存储和相关组件
 */
@Configuration
public class RagConfig {

    /**
     * 配置向量存储（使用内存存储，适合开发测试）
     * 生产环境建议使用：Chroma、Pinecone、Weaviate、Milvus等
     */
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
