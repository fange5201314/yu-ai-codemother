@echo off
REM 会话历史功能自动化测试脚本 - Windows版本
REM Chat History Function Automated Test Script for Windows

echo 🚀 开始执行会话历史功能测试...

set PROJECT_DIR=C:\Users\孙\IdeaProjects\yu-ai-code-father

echo 📁 项目目录: %PROJECT_DIR%

REM 切换到项目目录
cd /d "%PROJECT_DIR%"

REM 步骤1: 编译项目
echo.
echo 🔨 步骤1: 编译项目...
call mvn clean compile -q

if %errorlevel% equ 0 (
    echo ✅ 编译成功
) else (
    echo ❌ 编译失败，退出测试
    pause
    exit /b 1
)

REM 步骤2: 运行单元测试
echo.
echo 🧪 步骤2: 运行单元测试...
call mvn test -Dtest=ChatHistoryControllerTest

if %errorlevel% equ 0 (
    echo ✅ 单元测试通过
) else (
    echo ❌ 单元测试失败
)

REM 步骤3: 运行服务层测试
echo.
echo 🔧 步骤3: 运行服务层测试...
call mvn test -Dtest=ChatHistoryServiceTest

if %errorlevel% equ 0 (
    echo ✅ 服务层测试通过
) else (
    echo ❌ 服务层测试失败
)

REM 步骤4: 检查应用是否运行
echo.
echo 🔍 步骤4: 检查应用运行状态...
curl -s http://localhost:8123/api/chatHistory/list >nul 2>&1

if %errorlevel% equ 0 (
    echo ✅ 应用正在运行，可以进行API测试
    echo 💡 提示: 使用 chat-history-test.http 进行API测试
) else (
    echo ⚠️  应用未运行，启动应用后可进行API测试
    echo 🚀 启动命令: mvn spring-boot:run
)

REM 显示测试文件位置
echo.
echo 📁 测试文件位置:
echo    单元测试: src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryControllerTest.java
echo    服务测试: src/test/java/com/yupi/yuaicodemother/service/ChatHistoryServiceTest.java
echo    集成测试: src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryIntegrationTest.java
echo    API测试: chat-history-test.http
echo    Postman集合: ChatHistory-Postman-Collection.json
echo    测试指南: CHAT_HISTORY_TEST_GUIDE.md

echo.
echo 🎉 会话历史功能测试完成！
pause