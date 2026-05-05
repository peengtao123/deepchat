# Maven Site 集成 docs 文档 - 最终方案

## ✅ 配置完成

已成功将 `docs` 目录的所有 Markdown 文档集成到 Maven Site，并自动转换为格式化的 HTML。

## 🎯 核心原理

```
docs/*.md (源文件)
    ↓ generate-sources 阶段
src/site/markdown/docs/*.md (复制)
    ↓ site 阶段 (Doxia 引擎)
target/site/docs/*.html (转换后的 HTML)
```

## 📝 关键配置

### pom.xml

```xml
<!-- 在 generate-sources 阶段复制 docs 到 src/site/markdown -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.3.1</version>
    <executions>
        <execution>
            <id>copy-docs-to-markdown</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.basedir}/src/site/markdown/docs</outputDirectory>
                <resources>
                    <resource>
                        <directory>${project.basedir}/docs</directory>
                        <filtering>false</filtering>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 为什么这样做？

| 方案 | 优点 | 缺点 |
|------|------|------|
| ❌ 直接复制到 target/site | 简单快速 | Markdown 不被处理，保持 .md 格式 |
| ✅ 复制到 src/site/markdown | Doxia 处理为 HTML，应用样式 | 需要额外配置 |

## 🚀 使用方法

### 生成站点

```bash
# 清理并重新生成（推荐）
mvn clean site

# 或直接生成
mvn site
```

### 查看站点

```bash
# Windows
start target/site/index.html

# Mac
open target/site/index.html

# Linux
xdg-open target/site/index.html
```

### 更新文档

```bash
# 1. 编辑 docs/ 目录的 Markdown 文件
# 2. 重新生成站点
mvn clean site
# 3. 刷新浏览器
```

## 📊 生成结果

### 文件统计

- **源文件**: 32 个 Markdown 文件（docs 目录）
- **生成文件**: 32 个 HTML 文件（target/site/docs）
- **处理引擎**: Apache Maven Doxia Site Renderer 1.11.1
- **皮肤主题**: Apache Maven Fluido Skin 1.11.1

### 目录结构

```
target/site/
├── index.html                    # 首页（由 src/site/markdown/index.md 生成）
├── summary.html                  # 项目摘要
├── dependencies.html             # 依赖信息
├── plugins.html                  # 插件信息
└── docs/                         # 所有文档（已转换为 HTML）
    ├── 01-快速开始/             (4 个 HTML)
    │   ├── HELP.html
    │   ├── QUICKSTART_CHAT_MEMORY.html
    │   ├── QUICKSTART_DEEPSEEK.html
    │   └── RAG_QUICKSTART.html
    ├── 02-RAG知识库/            (9 个 HTML)
    ├── 03-聊天记忆/             (3 个 HTML)
    ├── 04-AI模型集成/           (3 个 HTML)
    ├── 05-前端集成/             (2 个 HTML)
    ├── 06-问题修复/             (4 个 HTML)
    ├── 07-构建与部署/           (3 个 HTML)
    ├── 08-测试脚本/             (6 个脚本，保持原样)
    ├── Maven_Site_使用说明.html
    ├── README.html
    └── 文档整理报告.html
```

## ✨ 特性

### 自动转换的功能

✅ **标题层级**: `# H1` → `<h1>`, `## H2` → `<h2>`, etc.  
✅ **列表**: 无序列表、有序列表  
✅ **代码块**: 带语法高亮的代码块  
✅ **链接**: 内部链接、外部链接  
✅ **表格**: 美化的表格样式  
✅ **加粗/斜体**: 文本格式化  
✅ **图片**: 图片引用（需放到 resources/images）  

### 应用的样式

✅ **统一主题**: Fluido Skin 主题  
✅ **导航菜单**: 左侧边栏导航  
✅ **顶部栏**: 固定顶部导航  
✅ **代码高亮**: PrettyPrint 语法高亮  
✅ **响应式**: 支持移动端浏览  
✅ **打印友好**: 专门的打印样式  

## 🔍 验证方法

### 检查构建日志

```
[INFO] Rendering 32 Doxia documents: 32 markdown
```

看到这条日志说明 Markdown 已被 Doxia 处理。

### 检查生成的 HTML

```bash
# 查看 HTML 文件
ls target/site/docs/01-快速开始/*.html

# 检查 HTML 内容（应包含完整的 HTML 结构）
head -50 target/site/docs/01-快速开始/RAG_QUICKSTART.html
```

### 浏览器验证

打开 `target/site/index.html`，检查：
- ✅ 页面布局正常
- ✅ 导航菜单可用
- ✅ 文档链接可点击
- ✅ 代码块有语法高亮
- ✅ 表格样式美观

## ⚠️ 注意事项

### 1. 不要手动修改 src/site/markdown/docs

```bash
# ❌ 错误做法
编辑 src/site/markdown/docs/xxx.md

# ✅ 正确做法
编辑 docs/xxx.md
然后运行 mvn clean site
```

原因：
- `src/site/markdown/docs` 是自动生成的
- 每次 `mvn clean` 会被删除
- 应该编辑源目录 `docs/`

### 2. 图片资源处理

如果 Markdown 中引用了图片：

```markdown
![图片描述](images/example.png)
```

需要将图片放到：
```
src/site/resources/images/example.png
```

或者使用绝对 URL：
```markdown
![图片描述](https://example.com/image.png)
```

### 3. 内部链接

Markdown 中的内部链接会自动转换：

```markdown
[查看 RAG 架构](../02-RAG知识库/RAG_ARCHITECTURE.md)
```

转换为：
```html
<a href="../02-RAG知识库/RAG_ARCHITECTURE.html">查看 RAG 架构</a>
```

### 4. 测试脚本

`.bat`, `.sh`, `.ps1` 等脚本文件不会被转换，保持原样下载到 `target/site/docs/08-测试脚本/`。

## 💡 最佳实践

### 1. 定期重新生成

每次更新 docs 后：
```bash
mvn clean site
```

### 2. 版本控制

只提交源文件：
```bash
git add docs/
git add src/site/markdown/index.md
git add src/site/site.xml
# 不提交 src/site/markdown/docs/（自动生成）
# 不提交 target/（构建产物）
```

### 3. CI/CD 集成

在流水线中添加：
```yaml
build_docs:
  script:
    - mvn clean site
  artifacts:
    paths:
      - target/site/
```

### 4. 部署站点

```bash
# 部署到 Web 服务器
scp -r target/site/* user@server:/var/www/html/deepchat-docs/

# 或使用 GitHub Pages
# 将 target/site 推送到 gh-pages 分支
```

## 📚 相关文档

- [Maven_Site_使用说明.md](docs/Maven_Site_使用说明.md) - 详细使用指南
- [README.md](docs/README.md) - 文档导航索引
- [MVN_SITE_QUICKREF.md](MVN_SITE_QUICKREF.md) - 快速参考卡片
- [Maven_Site配置完成报告_修正版.md](Maven_Site配置完成报告_修正版.md) - 技术细节

## 🎉 总结

通过正确的配置，实现了：

✅ **自动化**: docs Markdown 自动转换为 HTML  
✅ **标准化**: 使用 Maven Doxia 引擎处理  
✅ **美观化**: 应用统一的站点主题和样式  
✅ **易用性**: 一键生成，浏览器即可查看  
✅ **可维护**: 只需编辑 docs 源文件  

**这是 Maven Site 集成 Markdown 文档的标准做法！** 🚀

---

**配置版本**: v2.0 (修正版)  
**完成时间**: 2026-05-05  
**构建状态**: ✅ SUCCESS  
**文档数量**: 32 个 Markdown → 32 个 HTML  
**处理引擎**: Maven Doxia 1.11.1  
**皮肤主题**: Fluido Skin 1.11.1
