package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天记忆服务类（简化版）
 * 管理多轮对话的上下文历史
 */
@Service
public class ChatMemoryService {

    /**
     * 存储每个会话的对话历史
     * key: sessionId, value: 消息列表
     */
    private final Map<String, List<Map<String, String>>> sessionHistories = new ConcurrentHashMap<>();

    /**
     * 获取或创建会话的对话历史
     * @param sessionId 会话ID
     * @return 消息列表
     */
    public List<Map<String, String>> getOrCreateHistory(String sessionId) {
        return sessionHistories.computeIfAbsent(sessionId, id -> new ArrayList<>());
    }

    /**
     * 添加用户消息到会话历史
     * @param sessionId 会话ID
     * @param message 用户消息
     */
    public void addUserMessage(String sessionId, String message) {
        List<Map<String, String>> history = getOrCreateHistory(sessionId);
        Map<String, String> userMsg = new java.util.HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        history.add(userMsg);
    }

    /**
     * 添加AI响应到会话历史
     * @param sessionId 会话ID
     * @param response AI响应
     */
    public void addAssistantMessage(String sessionId, String response) {
        List<Map<String, String>> history = getOrCreateHistory(sessionId);
        Map<String, String> assistantMsg = new java.util.HashMap<>();
        assistantMsg.put("role", "assistant");
        assistantMsg.put("content", response);
        history.add(assistantMsg);
    }

    /**
     * 获取格式化的对话历史文本
     * @param sessionId 会话ID
     * @return 格式化的历史文本
     */
    public String getFormattedHistory(String sessionId) {
        List<Map<String, String>> history = getOrCreateHistory(sessionId);
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> msg : history) {
            sb.append(msg.get("role")).append(": ").append(msg.get("content")).append("\n");
        }
        return sb.toString();
    }

    /**
     * 清除指定会话的聊天记忆
     * @param sessionId 会话ID
     */
    public void clearMemory(String sessionId) {
        sessionHistories.remove(sessionId);
    }

    /**
     * 删除指定会话的聊天记忆
     * @param sessionId 会话ID
     */
    public void deleteMemory(String sessionId) {
        sessionHistories.remove(sessionId);
    }

    /**
     * 获取所有活跃会话数量
     * @return 会话数量
     */
    public int getActiveSessionCount() {
        return sessionHistories.size();
    }

    /**
     * 获取指定会话的完整历史
     * @param sessionId 会话ID
     * @return 消息列表
     */
    public List<Map<String, String>> getSessionHistory(String sessionId) {
        return new ArrayList<>(getOrCreateHistory(sessionId));
    }
}
