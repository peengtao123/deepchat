package com.example.demo.service;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天记忆服务类
 * 管理多轮对话的上下文历史
 */
@Service
public class ChatMemoryService {

    /**
     * 存储每个会话的记忆实例
     * key: sessionId, value: ChatMemory
     */
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();

    /**
     * 获取或创建会话的聊天记忆
     * @param sessionId 会话ID
     * @return ChatMemory 实例
     */
    public ChatMemory getOrCreateMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId, id -> new InMemoryChatMemory());
    }

    /**
     * 清除指定会话的聊天记忆
     * @param sessionId 会话ID
     */
    public void clearMemory(String sessionId) {
        ChatMemory memory = sessionMemories.get(sessionId);
        if (memory != null) {
            memory.clear();
        }
    }

    /**
     * 删除指定会话的聊天记忆
     * @param sessionId 会话ID
     */
    public void deleteMemory(String sessionId) {
        sessionMemories.remove(sessionId);
    }

    /**
     * 获取所有活跃会话数量
     * @return 会话数量
     */
    public int getActiveSessionCount() {
        return sessionMemories.size();
    }
}
