package com.example.demo.controller;

import com.example.demo.service.AIService;
import com.example.demo.util.ChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private ChatAssistant chatAssistant;

    /**
     * 简单的聊天接口
     * @param message 用户消息
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

        String response = aiService.chat(message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * 带系统提示的聊天接口
     * @param request 包含systemMessage和userMessage的请求
     * @return AI响应
     */
    @PostMapping("/chat-with-system")
    public Map<String, String> chatWithSystem(@RequestBody Map<String, String> request) {
        String systemMessage = request.getOrDefault("systemMessage", "你是一个有用的助手。");
        String userMessage = request.get("userMessage");
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户消息不能为空");
            return error;
        }

        String response = aiService.chatWithSystem(systemMessage, userMessage);
        
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

        String response = aiService.chat(message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * 使用ChatAssistant的聊天接口（推荐）
     * @param request 包含message和可选的systemPrompt
     * @return AI响应
     */
    @PostMapping("/advanced-chat")
    public Map<String, String> advancedChat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String systemPrompt = request.getOrDefault("systemPrompt", 
            ChatAssistant.SystemPrompts.DEFAULT_ASSISTANT);
        
        if (message == null || message.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "消息不能为空");
            return error;
        }

        String response = chatAssistant.chatWithSystem(systemPrompt, message);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }
}
