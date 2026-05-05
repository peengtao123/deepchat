# Maven Site 使用说明

## 📖 概述

本项目已配置 Maven Site 功能，可以自动生成项目文档站点，并将 `docs` 目录下的所有文档集成到站点中。

## 🚀 快速开始

### 生成站点

```bash
# 生成 Maven Site
mvn site

# 或者清理后重新生成
mvn clean site
```

### 查看站点

生成的站点位于 `target/site` 目录：

```bash
# Windows
start target/site/index.html

# Linux/Mac
open target/site/index.html
# 或
xdg-open target/site/index.html
```

## 📁 站点结构

```
target/site/
├── index.html                    # 站点首页
├── summary.html                  # 项目摘要
├── dependencies.html             # 依赖信息
├── plugins.html                  # 插件信息
├── docs/                         # 集成的文档目录
│   ├── 01-快速开始/
│   ├── 02-RAG知识库/
│   ├── 03-聊天记忆/
│   ├── 04-AI模型集成/
│   ├── 05-前端集成/
│   ├── 06-问题修复/
│   ├── 07-构建与部署/
│   └── 08-测试脚本/
├── css/                          # 样式文件
└── ...                           # 其他资源
```

## ⚙️ 配置说明

### pom.xml 配置

项目已在 `pom.xml` 中配置了以下插件：

1. **maven-site-plugin** (3.12.1)
   - 用于生成 Maven Site
   - 配置了中文 locale

2. **maven-project-info-reports-plugin** (3.5.0)
   - 生成项目报告（摘要、依赖、插件等）

3. **maven-resources-plugin** (3.3.1)
   - 在 `pre-site` 阶段自动将 `docs` 目录复制到 `target/site/docs`

### src/site 目录结构

```
src/site/
├── site.xml                      # 站点导航配置
├── markdown/
│   └── index.md                  # 站点首页内容
└── resources/
    └── css/
        └── site.css              # 自定义样式
```

### site.xml 配置

站点导航包含以下菜单：

- **项目信息**: 首页、项目摘要、依赖信息、插件信息
- **文档分类**: 8个文档分类菜单（对应 docs 目录）
- **快速链接**: 常用文档的快速访问链接
- **相关链接**: Spring AI、Spring Boot 等外部链接

## 🎨 自定义样式

站点使用了自定义 CSS 样式（`src/site/resources/css/site.css`），包括：

- 渐变色顶部导航栏
- 优化的代码块显示
- 美化的表格样式
- 响应式设计支持

如需修改样式，编辑 `src/site/resources/css/site.css` 文件后重新生成站点即可。

## 📝 添加新文档

### 方法 1：直接添加到 docs 目录

1. 将新的 Markdown 文档放到 `docs` 对应的子目录中
2. 运行 `mvn site` 重新生成站点
3. 新文档会自动出现在站点中

### 方法 2：更新导航菜单

如果需要在导航中添加新的菜单项，编辑 `src/site/site.xml`：

```xml
<menu name="新分类" ref="docs/新目录"/>
```

或在现有菜单中添加链接：

```xml
<item name="新文档" href="docs/路径/文件名.html"/>
```

## 🔧 常见问题

### Q: 生成的 HTML 文件中文乱码？

A: 确保系统编码为 UTF-8，可以在命令行设置：
```bash
# Windows
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
mvn site

# Linux/Mac
export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
mvn site
```

### Q: 如何部署站点到服务器？

A: 将 `target/site` 目录的全部内容上传到 Web 服务器即可：
```bash
# 示例：使用 SCP 上传
scp -r target/site/* user@server:/var/www/html/deepchat-docs/
```

### Q: 如何自定义站点主题？

A: 修改 `src/site/site.xml` 中的 `<skin>` 配置，或使用其他 Maven Skin：
- maven-fluido-skin（当前使用）
- maven-default-skin
- 自定义 skin

## 📊 构建生命周期

Maven Site 的构建流程：

1. **pre-site**: 复制 docs 目录到 target/site/docs
2. **site**: 生成站点页面（处理 site.xml 和 markdown）
3. **post-site**: 后续处理（如有配置）
4. **site-deploy**: 部署站点（如配置了部署插件）

## 💡 最佳实践

1. **定期更新**: 每次更新 docs 文档后，重新生成站点
2. **版本管理**: 可以为不同版本生成不同的站点目录
3. **持续集成**: 在 CI/CD 流程中自动构建和部署站点
4. **备份归档**: 定期备份生成的站点文件

## 🔗 相关资源

- [Maven Site Plugin 官方文档](https://maven.apache.org/plugins/maven-site-plugin/)
- [Maven Fluido Skin](https://maven.apache.org/skins/maven-fluido-skin/)
- [Maven Doxia（Markdown 处理器）](https://maven.apache.org/doxia/)

---

**最后更新**: 2026-05-05
