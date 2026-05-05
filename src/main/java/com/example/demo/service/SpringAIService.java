package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
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
        // 添加用户消息到历史
        chatMemoryService.addUserMessage(sessionId, message);
        
        // 获取格式化的历史
        String history = chatMemoryService.getFormattedHistory(sessionId);
        
        // 构建包含历史的prompt
        String prompt = "以下是之前的对话历史：\n" + history + "\n\n用户当前问题：" + message;
        
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        
        // 添加AI响应到历史
        chatMemoryService.addAssistantMessage(sessionId, response);
        
        return response;
    }

    /**
     * 带记忆和系统提示的聊天方法
     * @param sessionId 会话ID
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return AI响应
     */
    public String chatWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage) {
        // 添加用户消息到历史
        chatMemoryService.addUserMessage(sessionId, userMessage);
        
        // 获取格式化的历史
        String history = chatMemoryService.getFormattedHistory(sessionId);
        
        // 构建包含历史的prompt
        String prompt = "以下是之前的对话历史：\n" + history + "\n\n用户当前问题：" + userMessage;
        
        String response = chatClient.prompt()
                .system(systemMessage)
                .user(prompt)
                .call()
                .content();
        
        // 添加AI响应到历史
        chatMemoryService.addAssistantMessage(sessionId, response);
        
        return response;
    }

    /**
     * 带记忆的流式聊天方法
     * @param sessionId 会话ID
     * @param message 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStreamWithMemory(String sessionId, String message) {
        // 添加用户消息到历史
        chatMemoryService.addUserMessage(sessionId, message);
        
        // 获取格式化的历史
        String history = chatMemoryService.getFormattedHistory(sessionId);
        
        // 构建包含历史的prompt
        String prompt = "以下是之前的对话历史：\n" + history + "\n\n用户当前问题：" + message;
        
        // 使用 StringBuilder 累积完整响应
        final StringBuilder fullResponse = new StringBuilder();
        
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> fullResponse.append(chunk))
                .doOnComplete(() -> {
                    // 流完成后保存完整响应到历史
                    chatMemoryService.addAssistantMessage(sessionId, fullResponse.toString());
                });
    }

    /**
     * 带记忆和系统提示的流式聊天方法
     * @param sessionId 会话ID
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return 流式AI响应
     */
    public Flux<String> chatStreamWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage) {
        // 添加用户消息到历史
        chatMemoryService.addUserMessage(sessionId, userMessage);
        
        // 获取格式化的历史
        String history = chatMemoryService.getFormattedHistory(sessionId);
        
        // 构建包含历史的prompt
        String prompt = "以下是之前的对话历史：\n" + history + "\n\n用户当前问题：" + userMessage;
        
        // 使用 StringBuilder 累积完整响应
        final StringBuilder fullResponse = new StringBuilder();
        
        return chatClient.prompt()
                .system(systemMessage)
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> fullResponse.append(chunk))
                .doOnComplete(() -> {
                    // 流完成后保存完整响应到历史
                    chatMemoryService.addAssistantMessage(sessionId, fullResponse.toString());
                });
    }

    /**
     * 清除指定会话的记忆
     * @param sessionId 会话ID
     */
    public void clearSessionMemory(String sessionId) {
        chatMemoryService.clearMemory(sessionId);
    }
}
