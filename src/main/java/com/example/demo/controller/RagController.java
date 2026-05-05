package com.example.demo.controller;

import com.example.demo.service.RagChatService;
import com.example.demo.service.RagService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RAG (检索增强生成) Controller
 * 提供文档管理、检索和问答功能
 */
@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "*")
public class RagController {

    @Autowired
    private RagService ragService;

    @Autowired
    private RagChatService ragChatService;

    /**
     * 添加文本文档
     * @param request 包含文本内容和元数据
     * @return 操作结果
     */
    @PostMapping("/document/add")
    public Map<String, Object> addDocument(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String source = request.getOrDefault("source", "manual");
        String title = request.getOrDefault("title", "未命名文档");
        
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("文本内容不能为空");
        }
        
        ragService.addTextDocument(text, "source", source, "title", title);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "文档添加成功");
        return response;
    }

    /**
     * 批量添加文档
     * @param documents 文档列表，每个文档包含 text, source, title
     * @return 操作结果
     */
    @PostMapping("/document/batch-add")
    public Map<String, Object> batchAddDocuments(@RequestBody List<Map<String, String>> documents) {
        int count = 0;
        for (Map<String, String> doc : documents) {
            String text = doc.get("text");
            String source = doc.getOrDefault("source", "manual");
            String title = doc.getOrDefault("title", "未命名文档");
            
            if (text != null && !text.trim().isEmpty()) {
                ragService.addTextDocument(text, "source", source, "title", title);
                count++;
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "成功添加 " + count + " 个文档");
        response.put("count", count);
        return response;
    }

    /**
     * 加载文本文件到向量存储
     * @param filePath 文件路径
     * @param source 来源标识
     * @return 操作结果
     */
    @PostMapping("/document/load-file")
    public Map<String, Object> loadFile(@RequestParam String filePath, 
                                        @RequestParam(defaultValue = "file") String source) {
        ragService.loadTextFile(filePath, source);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "文件加载成功: " + filePath);
        return response;
    }

    /**
     * 加载目录下所有文本文件
     * @param directoryPath 目录路径
     * @param fileExtension 文件扩展名（如 .txt, .md）
     * @return 操作结果
     */
    @PostMapping("/document/load-directory")
    public Map<String, Object> loadDirectory(@RequestParam String directoryPath,
                                             @RequestParam(defaultValue = ".txt") String fileExtension) {
        ragService.loadDirectory(directoryPath, fileExtension);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "目录加载成功: " + directoryPath);
        return response;
    }

    /**
     * 搜索相关文档
     * @param query 查询文本
     * @param topK 返回最相关的K个文档（默认3）
     * @return 相关文档列表
     */
    @GetMapping("/search")
    public Map<String, Object> searchDocuments(@RequestParam String query,
                                               @RequestParam(defaultValue = "3") int topK) {
        List<Document> documents = ragService.searchSimilarDocuments(query, topK);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("query", query);
        response.put("count", documents.size());
        response.put("documents", documents.stream().map(doc -> {
            Map<String, Object> docMap = new HashMap<>();
            docMap.put("text", doc.getText());
            docMap.put("metadata", doc.getMetadata());
            return docMap;
        }).toList());
        
        return response;
    }

    /**
     * RAG 问答（非流式）
     * @param query 用户问题
     * @return AI生成的答案
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("查询内容不能为空");
        }
        
        String answer = ragChatService.chatWithRag(query);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("query", query);
        response.put("answer", answer);
        return response;
    }

    /**
     * RAG 问答（流式）
     * @param query 用户问题
     * @return 流式响应
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        
        if (query == null || query.trim().isEmpty()) {
            return Flux.just("错误：查询内容不能为空");
        }
        
        return ragChatService.chatStreamWithRag(query);
    }

    /**
     * 获取RAG使用说明
     * @return 使用指南
     */
    @GetMapping("/guide")
    public Map<String, Object> getGuide() {
        Map<String, Object> guide = new HashMap<>();
        guide.put("title", "RAG (检索增强生成) 使用指南");
        guide.put("description", "RAG结合了信息检索和文本生成，能够基于知识库提供准确的回答");
        
        Map<String, String> steps = new HashMap<>();
        steps.put("step1", "添加文档：使用 POST /api/rag/document/add 添加文本内容");
        steps.put("step2", "加载文件：使用 POST /api/rag/document/load-file 加载本地文件");
        steps.put("step3", "搜索文档：使用 GET /api/rag/search?query=xxx 搜索相关文档");
        steps.put("step4", "RAG问答：使用 POST /api/rag/chat 进行智能问答");
        
        guide.put("steps", steps);
        
        Map<String, Object> example = new HashMap<>();
        example.put("addDocument", """
                {
                  "text": "Spring Boot是一个用于构建生产级应用的框架...",
                  "source": "spring-docs",
                  "title": "Spring Boot介绍"
                }
                """);
        example.put("chat", """
                {
                  "query": "Spring Boot有什么特点？"
                }
                """);
        
        guide.put("examples", example);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("guide", guide);
        return response;
    }
}
