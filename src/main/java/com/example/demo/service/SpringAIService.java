package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * 基于 Spring AI 的 AI 服务类
 */
@Service
public class SpringAIService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatMemoryService chatMemoryService;

    /**
     * 简单聊天方法
     * @param message 用户消息
     * @return AI响应
     */
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * 带系统提示的聊天方法
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return AI响应
     */
    public String chatWithSystem(String systemMessage, String userMessage) {
        return chatClient.prompt()
                .system(systemMessage)
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * 流式聊天方法
     * @param message 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    /**
     * 带系统提示的流式聊天方法
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStreamWithSystem(String systemMessage, String userMessage) {
        return chatClient.prompt()
                .system(systemMessage)
                .user(userMessage)
                .stream()
                .content();
    }

    /**
     * 带记忆的聊天方法（支持多轮对话）
     * @param sessionId 会话ID
     * @param message 用户消息
     * @return AI响应
     */
    public String chatWithMemory(String sessionId, String message) {
        ChatMemory memory = chatMemoryService.getOrCreateMemory(sessionId);
        
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }

    /**
     * 带记忆和系统提示的聊天方法
     * @param sessionId 会话ID
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return AI响应
     */
    public String chatWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage) {
        ChatMemory memory = chatMemoryService.getOrCreateMemory(sessionId);
        
        return chatClient.prompt()
                .system(systemMessage)
                .user(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }

    /**
     * 带记忆的流式聊天方法
     * @param sessionId 会话ID
     * @param message 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStreamWithMemory(String sessionId, String message) {
        ChatMemory memory = chatMemoryService.getOrCreateMemory(sessionId);
        
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream()
                .content();
    }

    /**
     * 带记忆和系统提示的流式聊天方法
     * @param sessionId 会话ID
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStreamWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage) {
        ChatMemory memory = chatMemoryService.getOrCreateMemory(sessionId);
        
        return chatClient.prompt()
                .system(systemMessage)
                .user(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream()
                .content();
    }

    /**
     * 清除指定会话的记忆
     * @param sessionId 会话ID
     */
    public void clearSessionMemory(String sessionId) {
        chatMemoryService.clearMemory(sessionId);
    }
}
