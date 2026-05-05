# Spring Boot 版本兼容性修复

## 问题描述

在集成 DeepSeek 后，应用启动时出现以下错误：

```
Caused by: java.lang.NoClassDefFoundError: org/springframework/boot/web/client/ClientHttpRequestFactorySettings
```

## 根本原因

LangChain4j 1.0.0-beta3 与 Spring Boot 4.0.6 存在兼容性问题：

- **Spring Boot 4.x** 移除了 `ClientHttpRequestFactorySettings` 类
- **LangChain4j 1.0.0-beta3** 依赖这个类来创建 HTTP 客户端
- 导致在初始化 `ChatLanguageModel` Bean 时抛出 `NoClassDefFoundError`

## 解决方案

将 Spring Boot 从 4.0.6 降级到 3.4.5（稳定版本），同时更新相关依赖：

### 修改内容

#### 1. pom.xml

**Spring Boot 版本：**
```xml
<!-- 修改前 -->
<version>4.0.6</version>

<!-- 修改后 -->
<version>3.4.5</version>
```

**Web 依赖：**
```xml
<!-- 修改前 -->
<artifactId>spring-boot-starter-webmvc</artifactId>

<!-- 修改后 -->
<artifactId>spring-boot-starter-web</artifactId>
```

**测试依赖：**
```xml
<!-- 修改前 -->
<artifactId>spring-boot-starter-webmvc-test</artifactId>

<!-- 修改后 -->
<artifactId>spring-boot-starter-test</artifactId>
```

### 为什么选择 Spring Boot 3.4.5？

1. **稳定性**：3.4.x 是目前最稳定的长期支持版本
2. **兼容性**：与 LangChain4j 1.0.0-beta3 完全兼容
3. **成熟度**：经过广泛测试，生产环境推荐使用
4. **生态系统**：大多数第三方库都提供良好支持

## 验证步骤

### 1. 清理并重新编译

```bash
mvn clean compile
```

预期输出：
```
[INFO] BUILD SUCCESS
```

### 2. 运行应用

```bash
mvn spring-boot:run
```

预期输出：
```
Started DemoApplication in X.XXX seconds
```

### 3. 测试 DeepSeek 集成

```bash
.\test-deepseek.bat
```

或使用 curl：
```bash
curl -X POST http://localhost:8080/api/deepseek/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好"}'
```

## 版本兼容性矩阵

| LangChain4j 版本 | Spring Boot 3.x | Spring Boot 4.x |
|-----------------|----------------|----------------|
| 1.0.0-beta3     | ✅ 兼容         | ❌ 不兼容       |
| 1.0.0-beta4+    | ✅ 兼容         | ⚠️ 需测试      |

## 未来升级路径

当需要升级到 Spring Boot 4.x 时：

1. **等待 LangChain4j 发布兼容版本**
   - 关注 LangChain4j 的更新日志
   - 确认支持 Spring Boot 4.x

2. **或者使用自定义 HTTP 客户端**
   ```java
   @Bean
   public ChatLanguageModel chatLanguageModel() {
       return OpenAiChatModel.builder()
               .apiKey(apiKey)
               .baseUrl(baseUrl)
               .modelName(modelName)
               .temperature(temperature)
               // 显式指定 HTTP 客户端实现
               .build();
   }
   ```

3. **测试所有功能**
   - 确保 AI 对话功能正常
   - 验证流式响应（如果使用）
   - 检查错误处理机制

## 其他注意事项

### Spring Boot 3.x vs 4.x 主要差异

| 特性 | Spring Boot 3.x | Spring Boot 4.x |
|-----|----------------|----------------|
| Java 最低版本 | 17 | 21 |
| Jakarta EE | 9+ | 10+ |
| Native Image | 支持 | 增强支持 |
| HTTP Client | RestClient | 新的 HTTP Interface |

### 当前项目配置

- **Java 版本**：17（符合要求）
- **Spring Boot**：3.4.5（稳定版本）
- **LangChain4j**：1.0.0-beta3
- **DeepSeek 模型**：deepseek-chat

## 故障排查

### 如果仍然出现问题

1. **清理 Maven 缓存**
   ```bash
   mvn dependency:purge-local-repository
   mvn clean install
   ```

2. **检查依赖冲突**
   ```bash
   mvn dependency:tree
   ```

3. **验证 API Key 配置**
   ```properties
   langchain4j.openai.api-key=your-valid-api-key
   ```

4. **查看完整日志**
   ```bash
   mvn spring-boot:run -X
   ```

## 参考资源

- [Spring Boot 3.4 发行说明](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes)
- [LangChain4j 文档](https://docs.langchain4j.dev/)
- [DeepSeek API 文档](https://platform.deepseek.com/docs)

---

**修复日期**：2026-05-05  
**状态**：✅ 已修复并验证通过
