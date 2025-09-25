#!/bin/bash

# 会话历史功能自动化测试脚本
# Chat History Function Automated Test Script

echo "🚀 开始执行会话历史功能测试..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_DIR="C:\Users\孙\IdeaProjects\yu-ai-code-father"

echo -e "${BLUE}📁 项目目录: $PROJECT_DIR${NC}"

# 步骤1: 编译项目
echo -e "${YELLOW}🔨 步骤1: 编译项目...${NC}"
cd "$PROJECT_DIR"
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 编译成功${NC}"
else
    echo -e "${RED}❌ 编译失败，退出测试${NC}"
    exit 1
fi

# 步骤2: 运行单元测试
echo -e "${YELLOW}🧪 步骤2: 运行单元测试...${NC}"
mvn test -Dtest=ChatHistoryControllerTest -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 单元测试通过${NC}"
else
    echo -e "${RED}❌ 单元测试失败${NC}"
fi

# 步骤3: 运行服务层测试
echo -e "${YELLOW}🔧 步骤3: 运行服务层测试...${NC}"
mvn test -Dtest=ChatHistoryServiceTest -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 服务层测试通过${NC}"
else
    echo -e "${RED}❌ 服务层测试失败${NC}"
fi

# 步骤4: 生成测试报告
echo -e "${YELLOW}📊 步骤4: 生成测试报告...${NC}"
mvn surefire-report:report -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 测试报告生成成功${NC}"
    echo -e "${BLUE}📄 报告位置: target/site/surefire-report.html${NC}"
else
    echo -e "${RED}❌ 测试报告生成失败${NC}"
fi

# 步骤5: 检查应用是否运行
echo -e "${YELLOW}🔍 步骤5: 检查应用运行状态...${NC}"
curl -s http://localhost:8123/api/chatHistory/list > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 应用正在运行，可以进行API测试${NC}"
    echo -e "${BLUE}💡 提示: 使用 chat-history-test.http 进行API测试${NC}"
else
    echo -e "${YELLOW}⚠️  应用未运行，启动应用后可进行API测试${NC}"
    echo -e "${BLUE}🚀 启动命令: mvn spring-boot:run${NC}"
fi

# 显示测试文件位置
echo -e "${BLUE}"
echo "📁 测试文件位置:"
echo "   单元测试: src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryControllerTest.java"
echo "   服务测试: src/test/java/com/yupi/yuaicodemother/service/ChatHistoryServiceTest.java"
echo "   集成测试: src/test/java/com/yupi/yuaicodemother/controller/ChatHistoryIntegrationTest.java"
echo "   API测试: chat-history-test.http"
echo "   Postman集合: ChatHistory-Postman-Collection.json"
echo "   测试指南: CHAT_HISTORY_TEST_GUIDE.md"
echo -e "${NC}"

echo -e "${GREEN}🎉 会话历史功能测试完成！${NC}"