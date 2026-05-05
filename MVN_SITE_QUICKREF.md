# Maven Site 快速参考

## 🚀 一行命令生成站点

```bash
mvn site
```

## 📂 生成的站点位置

```
target/site/index.html
```

## 🔍 快速查看

```bash
# Windows
start target/site/index.html

# Mac
open target/site/index.html

# Linux
xdg-open target/site/index.html
```

## 📋 包含内容

- ✅ 项目信息（摘要、依赖、插件）
- ✅ 所有 docs 文档（8个分类，34个文件）
- ✅ 自定义导航菜单
- ✅ 美化的 UI 样式

## 🎯 常用场景

### 本地查阅文档
```bash
mvn clean site && start target/site/index.html
```

### 团队共享
将 `target/site` 目录部署到内部服务器或共享网盘

### CI/CD 集成
在流水线中添加 `mvn site` 步骤，自动生成最新文档站点

## 💡 提示

- 每次更新 docs 文档后，重新运行 `mvn site` 即可
- 站点是纯静态 HTML，可直接在任何 Web 服务器上运行
- 支持离线浏览，无需网络连接

---

**更多详情**: 查看 [Maven_Site_使用说明.md](docs/Maven_Site_使用说明.md)
