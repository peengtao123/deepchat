package com.example.demo.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG (Retrieval-Augmented Generation) 服务
 * 提供文档索引、检索和问答功能
 */
@Service
public class RagService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 添加文本文档到向量存储
     * @param text 文本内容
     * @param metadata 元数据（如来源、标题等）
     */
    public void addTextDocument(String text, String... metadata) {
        Document document = new Document(text);
        
        // 添加元数据
        if (metadata != null && metadata.length >= 2) {
            for (int i = 0; i < metadata.length; i += 2) {
                if (i + 1 < metadata.length) {
                    document.getMetadata().put(metadata[i], metadata[i + 1]);
                }
            }
        }
        
        List<Document> documents = List.of(document);
        vectorStore.add(documents);
    }

    /**
     * 批量添加文档
     * @param documents 文档列表
     */
    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }

    /**
     * 从文本文件加载文档
     * @param filePath 文件路径
     * @param source 来源标识
     */
    public void loadTextFile(String filePath, String source) {
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            
            // 可以按段落分割文档
            String[] paragraphs = content.split("\\n\\s*\\n");
            
            List<Document> documents = new ArrayList<>();
            for (int i = 0; i < paragraphs.length; i++) {
                String paragraph = paragraphs[i].trim();
                if (!paragraph.isEmpty()) {
                    Document doc = new Document(paragraph);
                    doc.getMetadata().put("source", source);
                    doc.getMetadata().put("paragraph", i);
                    doc.getMetadata().put("filename", path.getFileName().toString());
                    documents.add(doc);
                }
            }
            
            vectorStore.add(documents);
            System.out.println("成功加载文件: " + filePath + ", 共 " + documents.size() + " 个段落");
            
        } catch (Exception e) {
            throw new RuntimeException("加载文件失败: " + filePath, e);
        }
    }

    /**
     * 从目录加载所有文本文件
     * @param directoryPath 目录路径
     * @param fileExtension 文件扩展名（如 .txt, .md）
     */
    public void loadDirectory(String directoryPath, String fileExtension) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("目录不存在: " + directoryPath);
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(fileExtension));
        if (files == null || files.length == 0) {
            System.out.println("目录中没有找到 " + fileExtension + " 文件");
            return;
        }

        for (File file : files) {
            loadTextFile(file.getAbsolutePath(), file.getName());
        }
    }

    /**
     * 检索相关文档
     * @param query 查询文本
     * @param topK 返回最相关的K个文档
     * @return 相关文档列表
     */
    public List<Document> searchSimilarDocuments(String query, int topK) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();
        
        return vectorStore.similaritySearch(searchRequest);
    }

    /**
     * 检索相关文档（默认返回3个）
     * @param query 查询文本
     * @return 相关文档列表
     */
    public List<Document> searchSimilarDocuments(String query) {
        return searchSimilarDocuments(query, 3);
    }

    /**
     * 基于RAG的问答
     * @param query 用户问题
     * @return 包含引用来源的答案
     */
    public RagResponse answerWithRag(String query) {
        // 1. 检索相关文档
        List<Document> relevantDocs = searchSimilarDocuments(query, 3);
        
        if (relevantDocs.isEmpty()) {
            return new RagResponse(
                "抱歉，我没有找到相关的信息来回答您的问题。",
                query,
                new ArrayList<>()
            );
        }
        
        // 2. 构建上下文
        StringBuilder context = new StringBuilder();
        List<String> sources = new ArrayList<>();
        
        for (int i = 0; i < relevantDocs.size(); i++) {
            Document doc = relevantDocs.get(i);
            context.append("[文档 ").append(i + 1).append("]\n");
            context.append(doc.getText()).append("\n\n");
            
            // 收集来源信息
            String source = doc.getMetadata().getOrDefault("source", "未知").toString();
            String filename = doc.getMetadata().getOrDefault("filename", "").toString();
            if (!filename.isEmpty()) {
                sources.add(filename);
            } else {
                sources.add(source);
            }
        }
        
        // 3. 返回结果（实际的LLM调用在Controller中完成）
        return new RagResponse(context.toString(), query, sources);
    }

    /**
     * 清除所有文档
     */
    public void clearAllDocuments() {
        // SimpleVectorStore 不支持直接清空，需要重新创建
        // 这里可以通过删除所有文档的ID来实现
        System.out.println("注意：SimpleVectorStore 不支持直接清空，建议重启应用或使用其他VectorStore");
    }

    /**
     * 获取已索引的文档数量（近似）
     */
    public int getDocumentCount() {
        // SimpleVectorStore 没有直接的计数方法
        // 可以通过其他方式追踪
        return 0;
    }

    /**
     * RAG 响应对象
     */
    public static class RagResponse {
        private String context;      // 检索到的上下文
        private String query;        // 原始查询
        private List<String> sources; // 来源列表

        public RagResponse(String context, String query, List<String> sources) {
            this.context = context;
            this.query = query;
            this.sources = sources;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }
    }
}
