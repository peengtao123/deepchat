# 404 错误修复指南

## ❌ 问题描述

运行应用时出现以下错误：

```
org.springframework.web.reactive.function.client.WebClientResponseException$NotFound: 
404 Not Found from POST https://api.deepseek.com/v1/v1/chat/completions
```

## 🔍 问题分析

错误信息显示 URL 路径中出现了**重复的 `/v1`**：

```
https://api.deepseek.com/v1/v1/chat/completions
                            ^^^ ^^^
                           重复了!
```

### 原因

Spring AI 的 OpenAI 客户端会**自动**在 `base-url` 后添加 `/v1/chat/completions` 路径。

如果配置文件中 `base-url` 已经包含了 `/v1`，就会导致路径重复：

```properties
# ❌ 错误配置
spring.ai.openai.base-url=https://api.deepseek.com/v1
# Spring AI 会自动添加 /v1/chat/completions
# 最终 URL: https://api.deepseek.com/v1/v1/chat/completions  ← 错误!
```

## ✅ 解决方案

修改 `application.properties` 配置文件：

```properties
# ✅ 正确配置 - 不包含 /v1
spring.ai.openai.base-url=https://api.deepseek.com
```

Spring AI 会自动构建完整的 URL：
```
https://api.deepseek.com + /v1/chat/completions
= https://api.deepseek.com/v1/chat/completions  ← 正确!
```

## 📝 完整配置示例

```properties
spring.application.name=demo

# Spring AI OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY:sk-053eb2d4f0d84801a184058fc63ea470}
spring.ai.openai.base-url=https://api.deepseek.com
spring.ai.openai.chat.options.model=deepseek-chat
spring.ai.openai.chat.options.temperature=0.7
```

## 🔧 验证步骤

1. **修改配置文件** - 移除 `base-url` 中的 `/v1`
2. **重启应用** - 停止并重新启动 Spring Boot 应用
3. **测试 API** - 发送聊天请求验证是否正常

```bash
# 重启应用
mvn spring-boot:run

# 测试 API
curl -X POST http://localhost:8080/api/spring-ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好"}'
```

## 📚 其他常见 API Base URL

不同的 AI 提供商有不同的 base-url 配置方式：

### DeepSeek (OpenAI 兼容)
```properties
spring.ai.openai.base-url=https://api.deepseek.com
```

### OpenAI 官方
```properties
spring.ai.openai.base-url=https://api.openai.com
```

### Azure OpenAI
```properties
spring.ai.openai.base-url=https://{resource-name}.openai.azure.com
```

### 本地 Ollama
```properties
spring.ai.openai.base-url=http://localhost:11434
```

## ⚠️ 注意事项

1. **不要手动添加 API 路径** - Spring AI 会自动处理
2. **只配置域名部分** - base-url 应该只包含协议和域名
3. **检查 trailing slash** - 确保 base-url 末尾没有 `/`

## 🎯 总结

| 配置项 | 错误值 | 正确值 |
|--------|--------|--------|
| base-url | `https://api.deepseek.com/v1` | `https://api.deepseek.com` |

**核心原则**：`base-url` 只配置到域名，让 Spring AI 自动添加 API 路径。

---

**修复时间**: 2026-05-05  
**问题状态**: ✅ 已解决  
**影响范围**: application.properties 配置
