# Maven Site 智能菜单折叠功能

## 📋 功能说明

实现了智能菜单折叠功能：
- ✅ **当前页面所在菜单** → 保持展开状态
- ✅ **其他所有菜单** → 默认折叠
- ✅ **点击菜单标题** → 可以手动展开/折叠

## 🔧 实现细节

### 1. 文件结构

```
src/site/
├── resources/
│   └── js/
│       └── menu-collapse.js    # 智能折叠脚本
├── markdown/
│   └── docs/                   # 文档内容
└── site.xml                    # 站点配置
```

### 2. 配置文件修改

#### site.xml
添加了 `<custom>` 配置以启用侧边栏功能：

```xml
<custom>
    <fluidoSkin>
        <topBarEnabled>false</topBarEnabled>
        <sideBarEnabled>true</sideBarEnabled>
    </fluidoSkin>
</custom>
```

### 3. JavaScript 逻辑

`menu-collapse.js` 实现以下功能：

1. **检测当前页面**：通过 `window.location.pathname` 获取当前页面文件名
2. **查找活动菜单**：遍历所有菜单，找到包含当前页面链接的父菜单
3. **智能折叠**：
   - 活动菜单 → 展开（`slideDown`）
   - 其他菜单 → 折叠（`hide`）
4. **交互增强**：
   - 点击菜单标题可切换展开/折叠状态
   - 高亮当前页面的链接
   - 添加鼠标悬停效果

## 🚀 使用方法

### 生成站点

```bash
mvn clean site
```

### 查看效果

在浏览器中打开：
```
target/site/index.html
```

### 测试智能折叠

1. 访问任意文档页面（如 `docs/README.html`）
2. 观察左侧导航菜单：
   - "概览"菜单应该自动展开
   - 其他菜单（快速开始、RAG知识库等）应该折叠
3. 点击任意折叠的菜单标题，可以展开查看内容
4. 切换到另一个菜单的文档，原菜单折叠，新菜单展开

## 🎨 样式说明

maven-fluido-skin 会自动为菜单添加以下 CSS 类：

- `.collapsed` - 折叠状态的菜单标题
- `.expanded` - 展开状态的菜单标题
- `.active` - 当前页面的链接

可以通过自定义 CSS 进一步美化折叠图标和动画效果。

## 🔍 调试技巧

打开浏览器的开发者工具（F12），在 Console 中可以看到：

```
当前页面: README.html
展开活动菜单: 概览
菜单折叠初始化完成
```

这些日志可以帮助确认脚本是否正确执行。

## ⚠️ 注意事项

1. **jQuery 依赖**：maven-fluido-skin 已内置 jQuery，无需额外引入
2. **资源复制**：`src/site/resources` 下的文件会被 maven-site-plugin 自动复制到 `target/site`
3. **浏览器兼容**：支持所有现代浏览器（Chrome, Firefox, Safari, Edge）
4. **响应式设计**：在小屏幕设备上也能正常工作

## 📝 维护建议

如果需要调整折叠行为，可以修改 `menu-collapse.js` 文件：

- **改变默认行为**：修改第 15-50 行的折叠逻辑
- **添加动画效果**：调整 `slideUp/slideDown` 的时间参数（默认 200ms）
- **自定义样式**：在 `src/site/resources/css/` 下添加自定义 CSS

## ✨ 版本历史

- **v1.0** (2026-05-06) - 初始实现智能菜单折叠功能
