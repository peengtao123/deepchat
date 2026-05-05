package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AIServiceTest {

    @Autowired
    private AIService aiService;

    @Test
    void testChat() {
        // 注意：这个测试需要有效的OpenAI API密钥
        // 如果没有配置API密钥，测试将会失败
        String response = aiService.chat("你好");
        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("AI响应: " + response);
    }

    @Test
    void testChatWithSystem() {
        // 注意：这个测试需要有效的OpenAI API密钥
        String response = aiService.chatWithSystem(
            "你是一个专业的编程助手", 
            "什么是Spring Boot?"
        );
        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("AI响应: " + response);
    }
}
