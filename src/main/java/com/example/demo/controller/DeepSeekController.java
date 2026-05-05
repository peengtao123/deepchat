package com.example.demo.controller;

import com.example.demo.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return aiService.chat(request.getMessage());
    }

    @PostMapping("/chat-with-system")
    public String chatWithSystem(@RequestBody ChatWithSystemRequest request) {
        return aiService.chatWithSystem(request.getSystemMessage(), request.getUserMessage());
    }

    // 内部类用于接收请求体
    static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    static class ChatWithSystemRequest {
        private String systemMessage;
        private String userMessage;

        public String getSystemMessage() {
            return systemMessage;
        }

        public void setSystemMessage(String systemMessage) {
            this.systemMessage = systemMessage;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }
    }
}
