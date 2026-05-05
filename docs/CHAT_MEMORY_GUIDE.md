# 多轮对话历史 - ChatMemory 使用指南

## 📋 概述

本项目现已支持多轮对话历史功能，使用 Spring AI 的 ChatMemory 来保持对话上下文。AI 可以记住之前的对话内容，提供更连贯和智能的交互体验。

## ✨ 核心功能

### 1. 会话管理
- **自动会话ID生成**：每个用户会话都有唯一的会话ID
- **会话持久化**：会话ID保存在浏览器 localStorage 中
- **多会话支持**：可以同时管理多个独立的对话会话

### 2. 对话记忆
- **上下文保持**：AI 可以记住之前的对话内容
- **流式响应支持**：记忆功能同时支持普通响应和流式响应
- **可配置开关**：用户可以随时启用或禁用记忆功能

### 3. 记忆管理
- **清除记忆**：可以清除当前会话的记忆但保持会话ID
- **新会话**：创建全新的会话，开始新的对话

## 🔧 技术实现

### 后端架构

#### 1. ChatMemoryService (`ChatMemoryService.java`)
```java
@Service
public class ChatMemoryService {
    // 存储每个会话的记忆实例
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();
    
    // 获取或创建会话的聊天记忆
    public ChatMemory getOrCreateMemory(String sessionId)
    
    // 清除指定会话的聊天记忆
    public void clearMemory(String sessionId)
    
    // 删除指定会话的聊天记忆
    public void deleteMemory(String sessionId)
}
```

#### 2. SpringAIService 新增方法
```java
// 带记忆的聊天方法
public String chatWithMemory(String sessionId, String message)

// 带记忆和系统提示的聊天方法
public String chatWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage)

// 带记忆的流式聊天方法
public Flux<String> chatStreamWithMemory(String sessionId, String message)

// 带记忆和系统提示的流式聊天方法
public Flux<String> chatStreamWithMemoryAndSystem(String sessionId, String systemMessage, String userMessage)

// 清除指定会话的记忆
public void clearSessionMemory(String sessionId)
```

#### 3. SpringAIController 新增端点
```
POST /api/spring-ai/chat-with-memory                    # 带记忆的聊天
POST /api/spring-ai/chat-with-memory-and-system         # 带记忆和系统提示的聊天
POST /api/spring-ai/chat-stream-with-memory             # 带记忆的流式聊天
POST /api/spring-ai/chat-stream-with-memory-and-system  # 带记忆和系统提示的流式聊天
POST /api/spring-ai/clear-memory                        # 清除会话记忆
```

### 前端实现

#### 1. 会话ID管理
```javascript
// 从 localStorage 获取或生成新的会话ID
let currentSessionId = localStorage.getItem('chat_session_id');
if (!currentSessionId) {
    currentSessionId = 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    localStorage.setItem('chat_session_id', currentSessionId);
}
```

#### 2. 启用/禁用记忆
用户可以通过界面上的"启用多轮对话记忆"复选框来控制是否使用记忆功能。

#### 3. 记忆操作
- **清除记忆按钮**：调用 `/api/spring-ai/clear-memory` 清除当前会话的记忆
- **新会话按钮**：生成新的会话ID并清空对话界面

## 🚀 使用方法

### 方式一：通过 Web 界面（推荐）

1. **访问聊天页面**
   ```
   http://localhost:8080/spring-ai-chat.html
   ```

2. **查看会话ID**
   - 页面顶部会显示当前的会话ID
   - 会话ID在浏览器刷新后保持不变

3. **启用多轮对话记忆**
   - 确保勾选"启用多轮对话记忆"选项（默认已勾选）
   - 可选：勾选"启用流式响应"以获得更好的用户体验

4. **进行多轮对话**
   ```
   用户：我叫张三
   AI：你好，张三！很高兴认识你。
   
   用户：我今年25岁
   AI：好的，张三，你今年25岁。
   
   用户：我叫什么名字？
   AI：你叫张三。
   
   用户：我多大了？
   AI：你今年25岁。
   ```

5. **管理会话**
   - **清除记忆**：点击"清除记忆"按钮，清除当前会话的历史但保持会话ID
   - **新会话**：点击"新会话"按钮，创建全新的会话
   - **清空对话**：点击"清空对话"按钮，仅清空界面显示

### 方式二：通过 API 调用

#### 1. 带记忆的普通聊天
```bash
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "my-session-123",
    "message": "我叫张三"
  }'
```

#### 2. 带记忆和系统提示的聊天
```bash
curl -X POST http://localhost:8080/api/spring-ai/chat-with-memory-and-system \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "my-session-123",
    "systemMessage": "你是一个专业的编程助手",
    "userMessage": "我想学习Java"
  }'
```

#### 3. 带记忆的流式聊天
```bash
curl -X POST http://localhost:8080/api/spring-ai/chat-stream-with-memory \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "my-session-123",
    "message": "请介绍一下Spring Boot"
  }'
```

#### 4. 清除会话记忆
```bash
curl -X POST http://localhost:8080/api/spring-ai/clear-memory \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "my-session-123"
  }'
```

## 📝 测试示例

### 测试场景 1：基本信息记忆
```
第一轮：
用户：我的名字是李四

第二轮：
用户：我是一名软件工程师

第三轮：
用户：我叫什么名字？做什么工作？
期望回答：你叫李四，是一名软件工程师。
```

### 测试场景 2：上下文理解
```
第一轮：
用户：推荐一本好书

第二轮：
用户：这本书的作者是谁？
期望回答：能够理解"这本书"指的是上一轮推荐的书籍
```

### 测试场景 3：多轮代码讨论
```
第一轮：
用户：如何用Java实现快速排序？

第二轮：
用户：这个算法的时间复杂度是多少？
期望回答：能够理解"这个算法"指的是快速排序

第三轮：
用户：能优化一下吗？
期望回答：能够提供优化的快速排序实现
```

## ⚙️ 配置说明

### 依赖配置 (pom.xml)
```xml
<!-- Spring AI Chat Memory -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-client-chat</artifactId>
</dependency>
```

### 记忆存储
- **默认实现**：InMemoryChatMemory（内存存储）
- **存储位置**：服务端内存中的 ConcurrentHashMap
- **生命周期**：应用重启后记忆会丢失

### 扩展建议
如需持久化存储，可以实现自定义的 ChatMemory：
```java
// 基于数据库的实现
public class DatabaseChatMemory implements ChatMemory {
    // 使用 JPA 或其他持久化方案
}

// 基于 Redis 的实现
public class RedisChatMemory implements ChatMemory {
    // 使用 Redis 存储对话历史
}
```

## 🎯 最佳实践

### 1. 会话ID管理
- 使用有意义的会话ID（如用户ID、订单号等）
- 定期清理不活跃的会话以释放内存
- 考虑实现会话超时机制

### 2. 记忆控制
- 对于长对话，适时清除记忆避免上下文过长
- 在话题转换时，可以考虑创建新会话
- 提供用户控制记忆的选项

### 3. 性能优化
- 监控内存使用情况
- 考虑设置最大消息数量限制
- 对于高并发场景，考虑使用分布式缓存

## 🔍 故障排查

### 问题1：记忆功能不生效
**检查项：**
- 确认"启用多轮对话记忆"复选框已勾选
- 检查请求中是否包含 sessionId
- 查看后端日志确认使用了正确的 API 端点

### 问题2：会话ID变化
**原因：**
- 清除了浏览器数据
- 使用了隐私浏览模式
- localStorage 被手动清除

**解决方案：**
- 会话ID会自动重新生成
- 如需保持会话，不要清除浏览器数据

### 问题3：内存占用过高
**原因：**
- 大量活跃会话
- 单个会话历史过长

**解决方案：**
- 定期清除不需要的会话记忆
- 实现会话超时自动清理
- 考虑使用外部存储（Redis等）

## 📊 API 端点总结

| 端点 | 方法 | 功能 | 需要 sessionId |
|------|------|------|----------------|
| `/api/spring-ai/chat` | POST | 简单聊天 | ❌ |
| `/api/spring-ai/chat-with-system` | POST | 带系统提示的聊天 | ❌ |
| `/api/spring-ai/chat-stream` | POST | 流式聊天 | ❌ |
| `/api/spring-ai/chat-stream-with-system` | POST | 带系统提示的流式聊天 | ❌ |
| `/api/spring-ai/chat-with-memory` | POST | 带记忆的聊天 | ✅ |
| `/api/spring-ai/chat-with-memory-and-system` | POST | 带记忆和系统提示的聊天 | ✅ |
| `/api/spring-ai/chat-stream-with-memory` | POST | 带记忆的流式聊天 | ✅ |
| `/api/spring-ai/chat-stream-with-memory-and-system` | POST | 带记忆和系统提示的流式聊天 | ✅ |
| `/api/spring-ai/clear-memory` | POST | 清除会话记忆 | ✅ |

## 🎉 完成状态

✅ 添加 Spring AI ChatMemory 依赖  
✅ 创建 ChatMemoryService 服务类  
✅ 更新 SpringAIService 支持记忆功能  
✅ 更新控制器添加新的 API 端点  
✅ 更新前端页面支持会话管理  
✅ 测试多轮对话功能  

---

**更新时间**: 2026-05-05  
**版本**: 1.0.0
