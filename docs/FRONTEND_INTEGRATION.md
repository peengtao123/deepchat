# 前端与 Spring AI 集成指南

## 📋 概述

本项目提供了完整的前端页面与 Spring AI 和 LangChain4j 的集成，用户可以通过 Web 界面与 AI 进行智能对话。

## 🌐 可用页面

### 1. 首页 - `/index.html`
美观的入口页面，可以选择使用 LangChain4j 或 Spring AI 进行聊天。

**访问地址**: http://localhost:8080/

### 2. Spring AI 专用页面 - `/spring-ai-chat.html`
专门为 Spring AI 设计的现代化聊天界面。

**特性**:
- ✨ 现代化的渐变UI设计
- 💬 支持流式响应（实时显示）
- ⚙️ 可配置系统提示
- 🗑️ 清空对话功能
- 📱 响应式设计

**访问地址**: http://localhost:8080/spring-ai-chat.html

### 3. 双后端切换页面 - `/chat.html`
支持在 LangChain4j 和 Spring AI 之间切换的聊天界面。

**特性**:
- 🔄 实时切换后端框架
- 💬 支持流式响应
- 🎯 两种后端对比测试

**访问地址**: http://localhost:8080/chat.html

## 🎨 页面功能

### Spring AI 聊天页面功能

#### 1. 基础聊天
- 输入消息并发送给 AI
- 支持普通响应和流式响应两种模式
- 自动滚动到最新消息

#### 2. 系统提示设置
- 勾选"显示系统提示设置"
- 自定义 AI 的角色和行为
- 默认提示："你是一个有用的AI助手"

#### 3. 流式响应
- 启用后，AI 响应会逐字显示
- 提供更好的用户体验
- 带有闪烁光标效果

#### 4. 清空对话
- 点击"清空对话"按钮清除所有消息
- 开始新的对话 session

## 🔧 技术实现

### API 调用

#### 普通聊天
```javascript
const response = await fetch('/api/spring-ai/chat', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ message: message })
});

const data = await response.json();
console.log(data.response);
```

#### 带系统提示的聊天
```javascript
const response = await fetch('/api/spring-ai/chat-with-system', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ 
        systemMessage: systemPrompt,
        userMessage: message 
    })
});

const data = await response.json();
console.log(data.response);
```

#### 流式响应
```javascript
const response = await fetch('/api/spring-ai/chat-stream', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ message: message })
});

const reader = response.body.getReader();
const decoder = new TextDecoder('utf-8');

while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    
    const chunk = decoder.decode(value, { stream: true });
    // 处理 SSE 格式的数据
    const lines = chunk.split('\n');
    for (const line of lines) {
        if (line.startsWith('data: ')) {
            const data = line.slice(6);
            // 更新 UI
        }
    }
}
```

### 后端切换逻辑

在 `chat.html` 中实现了后端切换功能：

```javascript
// API 路径配置
const API_CONFIG = {
    langchain4j: {
        chat: '/api/ai/chat',
        chatStream: '/api/ai/chat-stream'
    },
    springai: {
        chat: '/api/spring-ai/chat',
        chatStream: '/api/spring-ai/chat-stream'
    }
};

// 切换后端
function switchBackend() {
    // 获取选中的后端
    const radios = document.getElementsByName('backend');
    for (const radio of radios) {
        if (radio.checked) {
            currentBackend = radio.value;
            break;
        }
    }
    
    // 动态更新 API 调用
    const url = getCurrentAPI('chat');
}
```

## 🎯 使用示例

### 场景 1: 编程助手

1. 打开 Spring AI 聊天页面
2. 勾选"显示系统提示设置"
3. 输入系统提示："你是一个专业的编程专家，精通 Java、Python 等编程语言"
4. 发送问题："如何实现一个快速排序算法？"

### 场景 2: 翻译助手

1. 设置系统提示："你是一个专业的翻译助手，擅长中英互译"
2. 发送："请将以下中文翻译成英文：人工智能正在改变世界"

### 场景 3: 创意写作

1. 启用流式响应
2. 发送："请写一首关于春天的诗"
3. 观察文字逐字显示的流畅体验

## 🆚 两个框架对比

| 特性 | LangChain4j | Spring AI |
|------|-------------|-----------|
| UI 页面 | chat.html (切换模式) | spring-ai-chat.html |
| API 路径 | /api/ai/* | /api/spring-ai/* |
| 官方支持 | 社区项目 | Spring 官方 |
| 生态系统 | 独立框架 | Spring 生态 |
| 学习曲线 | 中等 | 较低（对 Spring 用户） |
| 功能丰富度 | 非常高 | 高 |

## 📱 移动端适配

所有页面都采用了响应式设计，可以在移动设备上正常使用：

- 自适应布局
- 触摸友好的按钮大小
- 优化的字体大小
- 流畅的滚动体验

## 🎨 UI 特色

### Spring AI 页面设计
- **渐变背景**: 紫色渐变背景，现代感十足
- **卡片式布局**: 圆角卡片，阴影效果
- **动画效果**: 消息淡入动画，按钮悬停效果
- **流式光标**: 闪烁的光标指示正在接收数据

### 交互体验
- **实时反馈**: 发送按钮状态变化
- **加载提示**: "AI正在思考..." 提示
- **错误处理**: 友好的错误消息显示
- **自动滚动**: 新消息自动滚动到可见区域

## 🔍 调试技巧

### 浏览器控制台
打开浏览器开发者工具（F12），可以查看：

1. **Console 标签**: 查看日志和错误信息
2. **Network 标签**: 监控 API 请求和响应
3. **Elements 标签**: 检查 DOM 结构

### 常见问题

**问题 1**: 消息发送后没有响应
- 检查后端服务是否启动
- 检查浏览器控制台的错误信息
- 确认 API 密钥配置正确

**问题 2**: 流式响应不工作
- 确认后端支持 SSE (Server-Sent Events)
- 检查浏览器是否支持 Fetch API 和 ReadableStream
- 查看 Network 标签中的响应类型

**问题 3**: 切换后端后无法使用
- 确认两个后端的服务都已正确配置
- 检查对应的 API 端点是否可用
- 查看 application.properties 配置

## 🚀 性能优化建议

1. **消息历史管理**: 对于长时间对话，可以考虑限制显示的消息数量
2. **防抖处理**: 在输入框添加防抖，避免频繁发送
3. **缓存机制**: 可以缓存常见问题的回答
4. **懒加载**: 对于大量消息，可以实现虚拟滚动

## 📝 扩展开发

### 添加新功能

如果想在前端添加新功能，可以：

1. **添加新的 API 调用**: 在后端创建新的 endpoint
2. **增强 UI**: 修改 CSS 样式或添加新的 HTML 元素
3. **添加插件**: 例如代码高亮、Markdown 渲染等
4. **国际化**: 添加多语言支持

### 示例：添加 Markdown 支持

```html
<!-- 引入 marked.js -->
<script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

<!-- 修改消息显示 -->
function addMessage(text, isUser) {
    const messagesDiv = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isUser ? 'user-message' : 'ai-message'}`;
    
    if (isUser) {
        messageDiv.textContent = text;
    } else {
        messageDiv.innerHTML = marked.parse(text); // 渲染 Markdown
    }
    
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
    return messageDiv;
}
```

## 📚 相关文档

- [Spring AI 集成文档](./SPRING_AI_INTEGRATION.md)
- [LangChain4j 集成文档](./LANGCHAIN4J_README.md)
- [API 使用示例](./API_EXAMPLES.md)

## ✅ 测试清单

启动应用后，测试以下功能：

- [ ] 访问 http://localhost:8080/ 能看到首页
- [ ] 点击 LangChain4j 卡片能跳转到 chat.html
- [ ] 点击 Spring AI 卡片能跳转到 spring-ai-chat.html
- [ ] 在 Spring AI 页面发送消息能得到响应
- [ ] 启用流式响应能看到逐字显示效果
- [ ] 设置系统提示能影响 AI 的回答风格
- [ ] 清空对话按钮能正常工作
- [ ] 在 chat.html 中能切换不同的后端
- [ ] 移动端页面显示正常

## 🎉 总结

本项目提供了完整的前端与 Spring AI 集成方案：

- ✅ 3 个精心设计的页面
- ✅ 支持流式响应和系统提示
- ✅ 现代化的 UI/UX 设计
- ✅ 响应式布局，支持移动端
- ✅ 完整的错误处理和用户反馈
- ✅ 可在 LangChain4j 和 Spring AI 之间切换

您现在可以通过 Web 界面轻松使用 Spring AI 进行智能对话！
