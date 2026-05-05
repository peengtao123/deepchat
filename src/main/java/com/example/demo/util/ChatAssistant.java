package com.example.demo.util;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AI对话助手工具类
 * 支持多轮对话和上下文管理
 */
@Component
public class ChatAssistant {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    /**
     * 单轮对话
     * @param userMessage 用户消息
     * @return AI响应
     */
    public String chat(String userMessage) {
        return chatLanguageModel.chat(userMessage);
    }

    /**
     * 带系统提示的单轮对话
     * @param systemMessage 系统提示
     * @param userMessage 用户消息
     * @return AI响应
     */
    public String chatWithSystem(String systemMessage, String userMessage) {
        String fullPrompt = systemMessage + "\n\n" + userMessage;
        return chatLanguageModel.chat(fullPrompt);
    }

    /**
     * 多轮对话（带历史记录）
     * @param messages 聊天历史
     * @param newUserMessage 新的用户消息
     * @return AI响应
     */
    public String chatWithHistory(List<ChatMessage> messages, String newUserMessage) {
        // 简单实现：将所有消息合并为一个prompt
        StringBuilder prompt = new StringBuilder();
        for (ChatMessage msg : messages) {
            prompt.append(msg.toString()).append("\n");
        }
        prompt.append(newUserMessage);
        return chatLanguageModel.chat(prompt.toString());
    }

    /**
     * 创建系统消息
     * @param content 系统提示内容
     * @return SystemMessage对象
     */
    public static SystemMessage createSystemMessage(String content) {
        return SystemMessage.from(content);
    }

    /**
     * 创建用户消息
     * @param content 用户消息内容
     * @return UserMessage对象
     */
    public static UserMessage createUserMessage(String content) {
        return UserMessage.from(content);
    }

    /**
     * 构建预设的系统提示
     * @return 常用的系统提示模板
     */
    public static class SystemPrompts {
        public static final String DEFAULT_ASSISTANT = "你是一个有用的AI助手。";
        public static final String PROGRAMMING_EXPERT = "你是一个专业的编程专家，精通各种编程语言和技术。";
        public static final String TRANSLATOR = "你是一个专业的翻译助手，能够准确翻译多种语言。";
        public static final String CODE_REVIEWER = "你是一个代码审查专家，能够发现代码中的问题并提供改进建议。";
        public static final String TUTOR = "你是一个耐心的教师，擅长用简单易懂的方式解释复杂概念。";
    }
}
