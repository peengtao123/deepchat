# 对话历史查看功能

## 📋 功能概述

新增了**对话历史查看**功能，用户可以随时查看当前会话的完整对话历史记录，包括所有用户消息和AI响应。

## ✨ 主要特性

### 1. 后端API
- **新增端点**: `POST /api/spring-ai/get-history`
- **功能**: 返回指定会话的完整对话历史
- **请求参数**: 
  ```json
  {
    "sessionId": "session_123456"
  }
  ```
- **响应格式**:
  ```json
  {
    "sessionId": "session_123456",
    "history": [
      {"role": "user", "content": "你好"},
      {"role": "assistant", "content": "你好！有什么可以帮助你的吗？"}
    ],
    "count": 2
  }
  ```

### 2. 前端界面

#### 查看历史按钮
在聊天界面底部添加了**"查看历史"**按钮（粉黄渐变色）：

```
[清空对话] [查看历史] [清除记忆] [新会话]
```

#### 历史弹窗
点击"查看历史"后弹出模态窗口，显示：
- 📜 标题和关闭按钮
- 消息总数统计
- 完整的对话历史列表
  - 👤 用户消息（蓝色背景）
  - 🤖 AI助手消息（绿色背景）

### 3. 交互功能
- ✅ 点击"查看历史"按钮打开弹窗
- ✅ 点击关闭按钮或弹窗外部关闭
- ✅ 滚动查看所有历史记录
- ✅ 区分用户和AI消息（不同颜色）
- ✅ HTML转义防止XSS攻击

## 🎨 界面展示

### 按钮位置
```
┌─────────────────────────────────────┐
│  聊天消息区域                        │
├─────────────────────────────────────┤
│  [消息输入框]          [发送]       │
├─────────────────────────────────────┤
│  ☑ 流式  ☑ 记忆                     │
│  [清空] [查看历史] [清除] [新会话]  │
└─────────────────────────────────────┘
```

### 历史弹窗
```
┌──────────────────────────────────────┐
│  📜 对话历史                    [×]  │
├──────────────────────────────────────┤
│  共 6 条消息                         │
│                                      │
│  ┌────────────────────────────────┐ │
│  │ 👤 用户                        │ │
│  │ 我叫张三                       │ │
│  └────────────────────────────────┘ │
│                                      │
│  ┌────────────────────────────────┐ │
│  │ 🤖 AI助手                      │ │
│  │ 你好，张三！很高兴认识你。     │ │
│  └────────────────────────────────┘ │
│                                      │
│  ...更多历史...                      │
└──────────────────────────────────────┘
```

## 🔧 技术实现

### 后端实现

#### 1. ChatMemoryService 新增方法
```java
/**
 * 获取指定会话的完整历史
 * @param sessionId 会话ID
 * @return 消息列表
 */
public List<Map<String, String>> getSessionHistory(String sessionId) {
    return new ArrayList<>(getOrCreateHistory(sessionId));
}
```

#### 2. SpringAIController 新增端点
```java
@Autowired
private ChatMemoryService chatMemoryService;

@PostMapping("/get-history")
public Map<String, Object> getHistory(@RequestBody Map<String, String> request) {
    String sessionId = request.get("sessionId");
    
    if (sessionId == null || sessionId.trim().isEmpty()) {
        sessionId = "default-session";
    }

    // 获取会话历史
    List<Map<String, String>> history = 
        chatMemoryService.getSessionHistory(sessionId);
    
    Map<String, Object> result = new HashMap<>();
    result.put("sessionId", sessionId);
    result.put("history", history);
    result.put("count", history.size());
    return result;
}
```

### 前端实现

#### 1. CSS样式
```css
/* 历史查看弹窗样式 */
.history-modal {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    justify-content: center;
    align-items: center;
}

.history-item.user {
    background-color: #e3f2fd;
    border-left-color: #2196f3;
}

.history-item.assistant {
    background-color: #f1f8e9;
    border-left-color: #4caf50;
}
```

#### 2. JavaScript函数
```javascript
// 查看对话历史
async function viewHistory() {
    const response = await fetch(`${API_BASE}/get-history`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ sessionId: currentSessionId })
    });
    
    const data = await response.json();
    // 渲染历史列表
    // 显示弹窗
}

// 关闭历史弹窗
function closeHistory() {
    document.getElementById('historyModal').classList.remove('active');
}

// HTML转义（防止XSS）
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
```

## 📊 数据结构

### 历史消息格式
```javascript
[
  {
    "role": "user",           // 角色：user 或 assistant
    "content": "你好"         // 消息内容
  },
  {
    "role": "assistant",
    "content": "你好！很高兴见到你。"
  },
  {
    "role": "user",
    "content": "我叫张三"
  },
  ...
]
```

## 🚀 使用流程

### 步骤1：进行对话
1. 访问 http://localhost:8080/spring-ai-chat.html
2. 启用"多轮对话记忆"
3. 发送多条消息进行对话

### 步骤2：查看历史
1. 点击底部的**"查看历史"**按钮
2. 弹窗显示完整的对话历史
3. 滚动查看所有消息

### 步骤3：关闭弹窗
- 点击右上角的 **×** 按钮
- 或点击弹窗外部的灰色区域

## 💡 应用场景

### 场景1：回顾对话内容
用户在进行长对话后，想回顾之前讨论的内容：
```
用户：[查看历史]
→ 看到之前的所有问题和回答
→ 可以快速找到重要信息
```

### 场景2：检查记忆功能
验证多轮对话记忆是否正常工作：
```
用户：我叫张三
用户：我今年25岁
用户：[查看历史]
→ 确认两条消息都已保存
→ 再问"我叫什么"，验证AI能记住
```

### 场景3：调试和测试
开发者测试对话功能时：
```
→ 查看实际发送的消息
→ 检查历史格式是否正确
→ 验证消息顺序
```

## ⚙️ 配置选项

### 自定义样式
可以在 `<style>` 标签中修改：

```css
/* 修改弹窗大小 */
.history-content {
    max-width: 800px;    /* 最大宽度 */
    max-height: 80vh;    /* 最大高度 */
}

/* 修改消息颜色 */
.history-item.user {
    background-color: #e3f2fd;     /* 用户消息背景 */
    border-left-color: #2196f3;    /* 左边框颜色 */
}

/* 修改按钮颜色 */
button[onclick="viewHistory()"] {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}
```

## 🔍 注意事项

### 1. 数据安全
- ✅ 已实现HTML转义，防止XSS攻击
- ✅ 只显示当前会话的历史
- ⚠️ 历史存储在服务器内存中，重启后丢失

### 2. 性能考虑
- 对于超长对话（几百条消息），建议添加分页
- 当前实现一次性加载所有历史
- 可以优化为懒加载或虚拟滚动

### 3. 隐私保护
- 历史包含完整的对话内容
- 建议在生产环境中添加用户认证
- 考虑添加历史记录过期机制

## 🎯 未来改进

### 短期优化
1. **搜索功能** - 在历史中搜索关键词
2. **导出功能** - 导出对话历史为文本/JSON
3. **复制功能** - 一键复制某条消息
4. **时间戳** - 显示每条消息的时间

### 长期规划
1. **持久化存储** - 将历史保存到数据库
2. **历史记录列表** - 查看所有会话的历史
3. **恢复功能** - 从历史恢复之前的对话
4. **统计分析** - 对话次数、字数等统计

## 📝 相关文件

### 后端文件
- `ChatMemoryService.java` - 添加 `getSessionHistory()` 方法
- `SpringAIController.java` - 添加 `/get-history` 端点

### 前端文件
- `spring-ai-chat.html` - 添加按钮、弹窗和JavaScript函数

### 文档文件
- `docs/VIEW_CHAT_HISTORY.md` - 本文档

## ✨ 总结

对话历史查看功能让用户能够：
- ✅ 随时查看完整的对话记录
- ✅ 回顾之前的重要信息
- ✅ 验证记忆功能是否正常工作
- ✅ 方便调试和测试

界面美观、操作简单，是对话系统的重要辅助功能！

---

**添加日期**: 2026-05-05  
**版本**: 1.0.0  
**状态**: ✅ 已完成并测试
