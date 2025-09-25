# 会话历史功能测试指南

## 📁 测试文件清单

### 1. 单元测试
- **文件位置**: `src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryControllerTest.java`
- **测试类型**: 纯单元测试，使用Mock对象
- **覆盖范围**: 所有Controller方法
- **运行命令**: `mvn test -Dtest=ChatHistoryControllerTest`

### 2. 集成测试
- **文件位置**: `src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryIntegrationTest.java`
- **测试类型**: 使用真实数据库的集成测试
- **覆盖范围**: 完整的API工作流
- **运行命令**: `mvn test -Dtest=ChatHistoryIntegrationTest`

### 3. HTTP接口测试
- **文件位置**: `chat-history-test.http`
- **测试类型**: 手动API测试
- **使用方法**: 在IntelliJ IDEA中点击运行按钮
- **覆盖范围**: 所有REST API接口

### 4. Postman测试集合
- **文件位置**: `ChatHistory-Postman-Collection.json`
- **测试类型**: Postman API测试
- **使用方法**: 导入Postman后执行
- **覆盖范围**: 主要API接口

## 🧪 测试执行步骤

### 第一步：准备环境
```bash
# 1. 启动数据库
# 2. 确保application.yml配置正确
# 3. 启动应用
mvn spring-boot:run
```

### 第二步：运行自动化测试
```bash
# 运行所有会话历史相关测试
mvn test -Dtest=*ChatHistory*

# 查看测试报告
# 报告位置: target/surefire-reports/
```

### 第三步：手动API测试
1. **使用HTTP文件**: 打开 `chat-history-test.http`，逐个执行请求
2. **使用Postman**: 导入集合文件，批量执行测试

## 📊 测试结果验证

### 成功指标
- ✅ 单元测试：18/18 通过
- ✅ 集成测试：数据库操作成功
- ✅ API测试：所有接口返回正确状态码
- ✅ 数据验证：查询结果符合预期

### 测试场景覆盖
- ✅ 正常流程测试
- ✅ 边界条件测试
- ✅ 异常情况测试
- ✅ 权限控制测试
- ✅ 参数验证测试

## 🔧 故障排除

### 常见问题
1. **数据库连接失败**: 检查数据库服务是否启动
2. **权限不足**: 确保测试用户有相应权限
3. **端口冲突**: 确认8123端口未被占用
4. **依赖缺失**: 运行 `mvn clean install`

### 调试技巧
- 查看日志输出
- 使用断点调试
- 验证数据库数据变化
- 检查HTTP响应状态和内容

## 📈 测试报告

测试完成后，查看以下位置的详细报告：
- `target/surefire-reports/` - Maven测试报告
- `target/site/jacoco/` - 代码覆盖率报告（如果配置了JaCoCo）

## 🚀 下一步

测试通过后，可以考虑：
1. 添加性能测试
2. 增加更多边界条件测试
3. 配置持续集成(CI)
4. 添加API文档测试