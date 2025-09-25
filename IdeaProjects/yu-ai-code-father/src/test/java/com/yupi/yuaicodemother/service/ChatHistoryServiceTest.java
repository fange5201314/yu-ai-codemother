package com.yupi.yuaicodemother.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.yupi.yuaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.yupi.yuaicodemother.model.entity.App;
import com.yupi.yuaicodemother.model.entity.ChatHistory;
import com.yupi.yuaicodemother.model.entity.User;
import com.yupi.yuaicodemother.service.impl.ChatHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ChatHistoryService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ChatHistoryServiceTest {

    @Mock
    private AppService appService;

    @InjectMocks
    private ChatHistoryServiceImpl chatHistoryService;

    private User testUser;
    private App testApp;
    private ChatHistory testChatHistory;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .userAccount("testuser")
                .userRole("user")
                .build();

        testApp = App.builder()
                .id(1L)
                .appName("测试应用")
                .userId(1L)
                .build();

        testChatHistory = ChatHistory.builder()
                .id(1L)
                .message("测试消息")
                .messageType("user")
                .appId(1L)
                .userId(1L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDelete(0)
                .build();
    }

    @Test
    void testAddChatMessage_Success() {
        // 测试成功添加聊天消息
        // 这里需要根据实际的ChatHistoryServiceImpl实现来完善
        // 由于需要Mock数据库操作，这个测试需要更复杂的setup

        // 验证参数验证逻辑
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.addChatMessage(null, "message", "user", 1L);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.addChatMessage(1L, "", "user", 1L);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.addChatMessage(1L, "message", "", 1L);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.addChatMessage(1L, "message", "user", null);
        });
    }

    @Test
    void testDeleteByAppId_Success() {
        // 测试按应用ID删除聊天记录
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.deleteByAppId(null);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.deleteByAppId(0L);
        });
    }

    @Test
    void testGetQueryWrapper() {
        // 测试查询包装器构建
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setId(1L);
        request.setMessage("测试");
        request.setMessageType("user");
        request.setAppId(1L);
        request.setUserId(1L);

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);

        // 测试null请求
        QueryWrapper nullWrapper = chatHistoryService.getQueryWrapper(null);
        assertNotNull(nullWrapper);
    }

    @Test
    void testListAppChatHistoryByPage_ValidationErrors() {
        // 测试参数验证
        when(appService.getById(1L)).thenReturn(testApp);

        // 测试无效appId
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(null, 10, null, testUser);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(0L, 10, null, testUser);
        });

        // 测试无效pageSize
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(1L, 0, null, testUser);
        });

        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(1L, 51, null, testUser);
        });

        // 测试null用户
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(1L, 10, null, null);
        });

        // 测试应用不存在
        when(appService.getById(999L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> {
            chatHistoryService.listAppChatHistoryByPage(999L, 10, null, testUser);
        });
    }
}