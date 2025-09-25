[根目录](../CLAUDE.md) > **yu-ai-code-mother-frontend**

# 前端模块 - AI代码生成平台前端

> Vue 3 + TypeScript + Vite + Ant Design Vue

## 变更记录 (Changelog)

- **2025-09-14**: 初始化前端模块文档

## 当前待办事项 (Current TODO)

> 更新时间：2025-09-23

### 功能测试与完善
- [x] ~~修复后端启动问题~~
- [ ] 测试用户登录注册功能
- [ ] 测试主页功能展示
- [ ] 测试应用生成对话功能
- [ ] 测试应用管理功能
- [ ] 测试应用编辑功能
- [ ] 测试权限控制和安全性
- [ ] 代码审查和优化

### 已完成功能模块
- [x] 分析项目结构和现有代码风格
- [x] 研究后端接口文档和API目录
- [x] 设计主页组件结构和功能
- [x] 设计应用生成对话页组件结构和功能
- [x] 设计应用管理页组件结构和功能
- [x] 设计应用信息修改页组件结构和功能
- [x] 创建主页Vue组件和相关样式
- [x] 创建应用生成对话页Vue组件和相关样式
- [x] 创建应用管理页Vue组件和相关样式
- [x] 创建应用信息修改页Vue组件和相关样式
- [x] 实现主页逻辑和与后端API交互
- [x] 实现应用生成对话页逻辑和与后端API交互
- [x] 实现应用管理页逻辑和与后端API交互
- [x] 实现应用信息修改页逻辑和与后端API交互
- [x] 集成SSE实时消息功能
- [x] 集成网站展示功能
- [x] 集成部署功能

## 重要开发原则和经验教训

### 核心原则
1. **不要过度复杂化**：如果后端已经提供了所需功能，就直接使用，不要自己重新实现
2. **分离关注点**：预览和部署是两个不同的功能，应该有不同的实现方式
3. **保持简单**：SSE的实时显示功能已经工作得很好，不要为了其他功能而破坏它
4. **遵循现有模式**：使用项目中已经建立的模式和接口，不要自创方案

### 技术要点
- **SSE实时显示**：保持简单直接，每次消息只做必要的更新，避免复杂的状态管理
- **预览功能**：区分部署预览（使用deployKey）和生成预览（使用codeGenType_appId格式）
- **性能优化**：不要添加不必要的计算和状态更新，这会影响实时显示效果
- **错误处理**：保持现有的错误处理机制，不要添加复杂的恢复逻辑

### 常见陷阱避免
- 不要为了预览功能而保存SSE接收的内容，应该直接使用后端提供的路径
- 不要在SSE处理中添加过多的业务逻辑，保持专注于内容显示
- 不要混淆部署和预览的访问路径，它们应该完全分离

---

## 模块职责

提供AI代码生成平台的用户界面，包括用户认证、应用管理、代码生成交互和结果展示等核心功能。

## 入口与启动

### 主入口
- **应用入口**：`src/main.ts`
- **路由配置**：`src/router/index.ts`
- **根组件**：`src/App.vue`
- **布局组件**：`src/layouts/BasicLayout.vue`

### 开发启动
```bash
# 安装依赖
npm install

# 开发服务器
npm run dev              # 启动开发服务器 (http://localhost:5173)

# 构建
npm run build           # 生产环境构建
npm run preview         # 预览构建结果

# 代码质量
npm run lint           # ESLint检查并修复
npm run type-check     # TypeScript类型检查
npm run format         # Prettier代码格式化
```

## 对外接口

### API集成
- **API基础配置**：`src/request.ts` - Axios实例配置
- **API接口定义**：`src/api/` 目录
  - `userController.ts` - 用户相关API
  - `healthController.ts` - 健康检查API
  - `index.ts` - API统一导出

### 路由结构
```typescript
/                    # 首页
/user/login         # 用户登录
/user/register      # 用户注册
/admin/user         # 用户管理（管理员）
```

### 主要页面组件
- `src/pages/HomePage.vue` - 平台首页
- `src/pages/user/UserLoginPage.vue` - 用户登录
- `src/pages/user/UserRegisterPage.vue` - 用户注册
- `src/pages/admin/UserManagePage.vue` - 用户管理

## 关键依赖与配置

### 核心依赖
```json
{
  "vue": "^3.5.17",                    // Vue3框架
  "ant-design-vue": "^4.2.6",         // UI组件库
  "vue-router": "^4.5.1",             // 路由管理
  "pinia": "^3.0.3",                  // 状态管理
  "axios": "^1.11.0"                  // HTTP客户端
}
```

### 开发工具配置
- **Vite配置**：`vite.config.ts`
- **TypeScript配置**：`tsconfig.json`, `tsconfig.app.json`, `tsconfig.node.json`
- **ESLint配置**：`eslint.config.ts`
- **OpenAPI生成**：`openapi2ts.config.ts`

### 构建与部署配置
```typescript
// vite.config.ts 关键配置
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) }
  }
})
```

## 数据模型

### 状态管理（Pinia）
- `src/stores/loginUser.ts` - 登录用户状态管理

### API类型定义
- `src/api/typings.d.ts` - TypeScript类型定义
- 自动生成的API接口类型（通过openapi2ts）

## 测试与质量

### 代码质量工具
```bash
# ESLint - 代码规范检查
npm run lint

# TypeScript - 类型检查
npm run type-check

# Prettier - 代码格式化
npm run format
```

### 开发规范
- **组件规范**：使用 `<script setup>` 语法
- **类型安全**：严格TypeScript，避免使用any
- **命名规范**：组件PascalCase，文件名kebab-case
- **状态管理**：使用Pinia进行状态管理

## 常见问题 (FAQ)

**Q: 如何添加新的API接口？**
A:
1. 在`src/api/`目录下添加对应的controller文件
2. 使用`npm run openapi2ts`自动生成类型定义
3. 在组件中导入并使用

**Q: 如何添加新页面？**
A:
1. 在`src/pages/`对应目录下创建Vue组件
2. 在`src/router/index.ts`中添加路由配置
3. 更新导航菜单（如需要）

**Q: 如何调试网络请求？**
A: 打开浏览器开发者工具，检查Network面板，request.ts中已配置请求/响应拦截器便于调试

## 相关文件清单

### 核心配置文件
- `package.json` - 项目依赖和脚本
- `vite.config.ts` - Vite构建配置
- `tsconfig.*.json` - TypeScript配置
- `eslint.config.ts` - ESLint规则配置

### 源码结构
```
src/
├── main.ts              # 应用入口
├── App.vue             # 根组件
├── request.ts          # HTTP请求配置
├── access.ts           # 权限控制
├── api/                # API接口定义
├── components/         # 公共组件
├── layouts/           # 布局组件
├── pages/             # 页面组件
├── router/            # 路由配置
├── stores/            # 状态管理
└── assets/            # 静态资源
```

---

*最后更新：2025-09-14*