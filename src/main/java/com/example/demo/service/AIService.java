package com.example.demo.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    /**
     * 发送消息到AI模型并获取响应
     * @param message 用户输入的消息
     * @return AI的响应
     */
    public String chat(String message) {
        return chatLanguageModel.chat(message);
    }

    /**
     * 带系统提示的聊天
     * @param systemMessage 系统提示信息
     * @param userMessage 用户消息
     * @return AI的响应
     */
    public String chatWithSystem(String systemMessage, String userMessage) {
        String fullPrompt = systemMessage + "\n\n用户: " + userMessage + "\n助手: ";
        return chatLanguageModel.chat(fullPrompt);
    }
}
