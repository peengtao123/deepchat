package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RAG 增强聊天服务
 * 结合检索到的文档和LLM生成回答
 */
@Service
public class RagChatService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private RagService ragService;

    /**
     * 基于RAG的聊天问答
     * @param query 用户问题
     * @return AI生成的答案
     */
    public String chatWithRag(String query) {
        // 1. 检索相关文档
        RagService.RagResponse ragResponse = ragService.answerWithRag(query);
        
        String context = ragResponse.getContext();
        
        // 2. 如果没有检索到相关文档，直接返回提示
        if (context.isEmpty()) {
            return "抱歉，我没有找到相关的信息来回答您的问题。";
        }
        
        // 3. 构建增强的prompt
        String systemPrompt = "你是一个智能助手，请基于以下提供的上下文信息来回答用户的问题。\n" +
                "如果上下文中的信息不足以回答问题，请诚实地告知用户。\n" +
                "回答时要准确引用上下文中的信息，不要编造事实。";
        
        String userPrompt = "以下是相关的参考信息：\n\n" + 
                context + 
                "\n\n用户问题：" + query + 
                "\n\n请基于上述参考信息回答问题：";
        
        // 4. 调用LLM生成回答
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
        
        return response;
    }

    /**
     * 基于RAG的流式聊天问答
     * @param query 用户问题
     * @return 流式响应
     */
    public reactor.core.publisher.Flux<String> chatStreamWithRag(String query) {
        // 1. 检索相关文档
        RagService.RagResponse ragResponse = ragService.answerWithRag(query);
        
        String context = ragResponse.getContext();
        
        // 2. 如果没有检索到相关文档，直接返回提示
        if (context.isEmpty()) {
            return reactor.core.publisher.Flux.just("抱歉，我没有找到相关的信息来回答您的问题。");
        }
        
        // 3. 构建增强的prompt
        String systemPrompt = "你是一个智能助手，请基于以下提供的上下文信息来回答用户的问题。\n" +
                "如果上下文中的信息不足以回答问题，请诚实地告知用户。\n" +
                "回答时要准确引用上下文中的信息，不要编造事实。";
        
        String userPrompt = "以下是相关的参考信息：\n\n" + 
                context + 
                "\n\n用户问题：" + query + 
                "\n\n请基于上述参考信息回答问题：";
        
        // 4. 流式调用LLM生成回答
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .stream()
                .content();
    }
}
