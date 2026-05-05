package com.example.demo.controller;

import com.example.demo.service.SpringAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 Spring AI 的控制器
 */
@RestController
@RequestMapping("/api/spring-ai")
public class SpringAIController {

    @Autowired
    private SpringAIService springAIService;

    @Autowired
    private com.example.demo.service.ChatMemoryService chatMemoryService;

    /**
     * 简单聊天接口
     * @param request 包含message的请求体
     * @return AI响应
     */
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "消息不能为空");
            return error;
        }

        String response = springAIService.chat(message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * GET方式的简单测试接口
     * @param message 用户消息
     * @return AI响应
     */
    @GetMapping("/chat")
    public Map<String, String> chatGet(@RequestParam String message) {
        if (message == null || message.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "消息不能为空");
            return error;
        }

        String response = springAIService.chat(message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * 带系统提示的聊天接口
     * @param request 包含systemMessage和userMessage的请求体
     * @return AI响应
     */
    @PostMapping("/chat-with-system")
    public Map<String, String> chatWithSystem(@RequestBody Map<String, String> request) {
        String systemMessage = request.get("systemMessage");
        String userMessage = request.get("userMessage");
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户消息不能为空");
            return error;
        }

        if (systemMessage == null || systemMessage.trim().isEmpty()) {
            systemMessage = "你是一个有用的AI助手";
        }

        String response = springAIService.chatWithSystem(systemMessage, userMessage);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * 流式聊天接口
     * @param request 包含message的请求体
     * @return 流式AI响应
     */
    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return Flux.just(ServerSentEvent.<String>builder()
                    .data("消息不能为空")
                    .build());
        }

        return springAIService.chatStream(message)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 带系统提示的流式聊天接口
     * @param request 包含systemMessage和userMessage的请求体
     * @return 流式AI响应
     */
    @PostMapping(value = "/chat-stream-with-system", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStreamWithSystem(@RequestBody Map<String, String> request) {
        String systemMessage = request.get("systemMessage");
        String userMessage = request.get("userMessage");
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Flux.just(ServerSentEvent.<String>builder()
                    .data("用户消息不能为空")
                    .build());
        }

        if (systemMessage == null || systemMessage.trim().isEmpty()) {
            systemMessage = "你是一个有用的AI助手";
        }

        return springAIService.chatStreamWithSystem(systemMessage, userMessage)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 带记忆的聊天接口（支持多轮对话）
     * @param request 包含sessionId和message的请求体
     * @return AI响应
     */
    @PostMapping("/chat-with-memory")
    public Map<String, String> chatWithMemory(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        String message = request.get("message");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            // 如果没有提供sessionId，生成一个默认的
            sessionId = "default-session";
        }
        
        if (message == null || message.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "消息不能为空");
            return error;
        }

        String response = springAIService.chatWithMemory(sessionId, message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        result.put("sessionId", sessionId);
        return result;
    }

    /**
     * 带记忆和系统提示的聊天接口
     * @param request 包含sessionId、systemMessage和userMessage的请求体
     * @return AI响应
     */
    @PostMapping("/chat-with-memory-and-system")
    public Map<String, String> chatWithMemoryAndSystem(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        String systemMessage = request.get("systemMessage");
        String userMessage = request.get("userMessage");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "default-session";
        }
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户消息不能为空");
            return error;
        }

        if (systemMessage == null || systemMessage.trim().isEmpty()) {
            systemMessage = "你是一个有用的AI助手";
        }

        String response = springAIService.chatWithMemoryAndSystem(sessionId, systemMessage, userMessage);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        result.put("sessionId", sessionId);
        return result;
    }

    /**
     * 带记忆的流式聊天接口
     * @param request 包含sessionId和message的请求体
     * @return 流式AI响应
     */
    @PostMapping(value = "/chat-stream-with-memory", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStreamWithMemory(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        String message = request.get("message");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "default-session";
        }
        
        if (message == null || message.trim().isEmpty()) {
            return Flux.just(ServerSentEvent.<String>builder()
                    .data("消息不能为空")
                    .build());
        }

        return springAIService.chatStreamWithMemory(sessionId, message)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .event("message")
                        .build());
    }

    /**
     * 带记忆和系统提示的流式聊天接口
     * @param request 包含sessionId、systemMessage和userMessage的请求体
     * @return 流式AI响应
     */
    @PostMapping(value = "/chat-stream-with-memory-and-system", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStreamWithMemoryAndSystem(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        String systemMessage = request.get("systemMessage");
        String userMessage = request.get("userMessage");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "default-session";
        }
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Flux.just(ServerSentEvent.<String>builder()
                    .data("用户消息不能为空")
                    .build());
        }

        if (systemMessage == null || systemMessage.trim().isEmpty()) {
            systemMessage = "你是一个有用的AI助手";
        }

        return springAIService.chatStreamWithMemoryAndSystem(sessionId, systemMessage, userMessage)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .event("message")
                        .build());
    }

    /**
     * 清除会话记忆接口
     * @param request 包含sessionId的请求体
     * @return 操作结果
     */
    @PostMapping("/clear-memory")
    public Map<String, Object> clearMemory(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "default-session";
        }

        springAIService.clearSessionMemory(sessionId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "会话记忆已清除");
        result.put("sessionId", sessionId);
        return result;
    }

    /**
     * 获取会话历史接口
     * @param request 包含sessionId的请求体
     * @return 会话历史列表
     */
    @PostMapping("/get-history")
    public Map<String, Object> getHistory(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "default-session";
        }

        // 获取会话历史
        java.util.List<java.util.Map<String, String>> history = 
            chatMemoryService.getSessionHistory(sessionId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", sessionId);
        result.put("history", history);
        result.put("count", history.size());
        return result;
    }

    /**
     * 获取所有活跃会话列表接口
     * @return 会话列表（包含ID和标题）
     */
    @GetMapping("/sessions")
    public Map<String, Object> getAllSessions() {
        java.util.List<String> sessionIds = chatMemoryService.getAllSessionIds();
        java.util.List<Map<String, String>> sessions = new ArrayList<>();
        
        for (String id : sessionIds) {
            Map<String, String> sessionInfo = new HashMap<>();
            sessionInfo.put("id", id);
            sessionInfo.put("title", chatMemoryService.getSessionTitle(id));
            sessions.add(sessionInfo);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("sessions", sessions);
        result.put("count", sessions.size());
        return result;
    }
}
