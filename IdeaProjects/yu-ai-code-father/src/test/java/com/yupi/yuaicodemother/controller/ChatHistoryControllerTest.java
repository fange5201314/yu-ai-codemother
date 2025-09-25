package com.yupi.yuaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.yupi.yuaicodemother.common.BaseResponse;
import com.yupi.yuaicodemother.constant.UserConstant;
import com.yupi.yuaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.yupi.yuaicodemother.model.entity.ChatHistory;
import com.yupi.yuaicodemother.model.entity.User;
import com.yupi.yuaicodemother.service.ChatHistoryService;
import com.yupi.yuaicodemother.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ChatHistoryController 单元测试
 *
 * 使用 Mockito 进行纯单元测试，不依赖 Spring 上下文
 */
@ExtendWith(MockitoExtension.class)
class ChatHistoryControllerTest {

    @Mock
    private ChatHistoryService chatHistoryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatHistoryController chatHistoryController;

    // 测试数据
    private User adminUser;
    private User normalUser;
    private ChatHistory testChatHistory;
    private ChatHistoryQueryRequest queryRequest;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        adminUser = User.builder()
                .id(1L)
                .userAccount("admin")
                .userRole(UserConstant.ADMIN_ROLE)
                .build();

        normalUser = User.builder()
                .id(2L)
                .userAccount("user")
                .userRole(UserConstant.DEFAULT_ROLE)
                .build();

        // 初始化测试对话历史
        testChatHistory = ChatHistory.builder()
                .id(1L)
                .message("测试消息")
                .messageType("user")
                .appId(100L)
                .userId(2L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDelete(0)
                .build();

        // 初始化查询请求
        queryRequest = new ChatHistoryQueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(10);

        // 模拟 HttpServletRequest
        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    void testListAllChatHistoryByPageForAdmin_Success() {
        // 准备测试数据
        Page<ChatHistory> expectedPage = new Page<>();
        expectedPage.setRecords(List.of(testChatHistory));
        expectedPage.setTotalRow(1L);

        QueryWrapper queryWrapper = mock(QueryWrapper.class);
        when(chatHistoryService.getQueryWrapper(any(ChatHistoryQueryRequest.class)))
                .thenReturn(queryWrapper);
        when(chatHistoryService.page(any(Page.class), any(QueryWrapper.class)))
                .thenReturn(expectedPage);

        // 执行测试
        BaseResponse<Page<ChatHistory>> result =
                chatHistoryController.listAllChatHistoryByPageForAdmin(queryRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());
        assertEquals(testChatHistory.getId(), result.getData().getRecords().get(0).getId());

        // 验证服务调用
        verify(chatHistoryService, times(1)).getQueryWrapper(queryRequest);
        verify(chatHistoryService, times(1)).page(any(Page.class), eq(queryWrapper));
    }

    @Test
    void testListAllChatHistoryByPageForAdmin_NullRequest() {
        // 测试空请求参数，应该抛出异常
        assertThrows(Exception.class, () -> {
            chatHistoryController.listAllChatHistoryByPageForAdmin(null);
        });
    }

    @Test
    void testListAppChatHistory_Success() {
        // 准备测试数据
        Page<ChatHistory> expectedPage = new Page<>();
        expectedPage.setRecords(List.of(testChatHistory));
        expectedPage.setTotalRow(1L);

        when(userService.getLoginUser(mockRequest)).thenReturn(normalUser);
        when(chatHistoryService.listAppChatHistoryByPage(eq(100L), eq(10), any(), eq(normalUser)))
                .thenReturn(expectedPage);

        // 执行测试
        BaseResponse<Page<ChatHistory>> result =
                chatHistoryController.listAppChatHistory(100L, 10, null, mockRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());

        // 验证服务调用
        verify(userService, times(1)).getLoginUser(mockRequest);
        verify(chatHistoryService, times(1))
                .listAppChatHistoryByPage(eq(100L), eq(10), isNull(), eq(normalUser));
    }

    @Test
    void testListAppChatHistory_WithLastCreateTime() {
        // 准备测试数据
        LocalDateTime lastCreateTime = LocalDateTime.now().minusHours(1);
        Page<ChatHistory> expectedPage = new Page<>();
        expectedPage.setRecords(List.of(testChatHistory));

        when(userService.getLoginUser(mockRequest)).thenReturn(normalUser);
        when(chatHistoryService.listAppChatHistoryByPage(eq(100L), eq(10), eq(lastCreateTime), eq(normalUser)))
                .thenReturn(expectedPage);

        // 执行测试
        BaseResponse<Page<ChatHistory>> result =
                chatHistoryController.listAppChatHistory(100L, 10, lastCreateTime, mockRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());

        // 验证服务调用
        verify(userService, times(1)).getLoginUser(mockRequest);
        verify(chatHistoryService, times(1))
                .listAppChatHistoryByPage(eq(100L), eq(10), eq(lastCreateTime), eq(normalUser));
    }

    @Test
    void testSaveChatHistory_Success() {
        // 准备测试数据
        when(chatHistoryService.save(testChatHistory)).thenReturn(true);

        // 执行测试
        boolean result = chatHistoryController.save(testChatHistory);

        // 验证结果
        assertTrue(result);
        verify(chatHistoryService, times(1)).save(testChatHistory);
    }

    @Test
    void testSaveChatHistory_Failed() {
        // 准备测试数据
        when(chatHistoryService.save(testChatHistory)).thenReturn(false);

        // 执行测试
        boolean result = chatHistoryController.save(testChatHistory);

        // 验证结果
        assertFalse(result);
        verify(chatHistoryService, times(1)).save(testChatHistory);
    }

    @Test
    void testRemoveChatHistory_Success() {
        // 准备测试数据
        when(chatHistoryService.removeById(1L)).thenReturn(true);

        // 执行测试
        boolean result = chatHistoryController.remove(1L);

        // 验证结果
        assertTrue(result);
        verify(chatHistoryService, times(1)).removeById(1L);
    }

    @Test
    void testRemoveChatHistory_Failed() {
        // 准备测试数据
        when(chatHistoryService.removeById(1L)).thenReturn(false);

        // 执行测试
        boolean result = chatHistoryController.remove(1L);

        // 验证结果
        assertFalse(result);
        verify(chatHistoryService, times(1)).removeById(1L);
    }

    @Test
    void testUpdateChatHistory_Success() {
        // 准备测试数据
        when(chatHistoryService.updateById(testChatHistory)).thenReturn(true);

        // 执行测试
        boolean result = chatHistoryController.update(testChatHistory);

        // 验证结果
        assertTrue(result);
        verify(chatHistoryService, times(1)).updateById(testChatHistory);
    }

    @Test
    void testUpdateChatHistory_Failed() {
        // 准备测试数据
        when(chatHistoryService.updateById(testChatHistory)).thenReturn(false);

        // 执行测试
        boolean result = chatHistoryController.update(testChatHistory);

        // 验证结果
        assertFalse(result);
        verify(chatHistoryService, times(1)).updateById(testChatHistory);
    }

    @Test
    void testListAllChatHistory_Success() {
        // 准备测试数据
        List<ChatHistory> expectedList = new ArrayList<>();
        expectedList.add(testChatHistory);
        when(chatHistoryService.list()).thenReturn(expectedList);

        // 执行测试
        List<ChatHistory> result = chatHistoryController.list();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testChatHistory.getId(), result.get(0).getId());
        assertEquals(testChatHistory.getMessage(), result.get(0).getMessage());
        verify(chatHistoryService, times(1)).list();
    }

    @Test
    void testListAllChatHistory_EmptyResult() {
        // 准备测试数据
        List<ChatHistory> emptyList = new ArrayList<>();
        when(chatHistoryService.list()).thenReturn(emptyList);

        // 执行测试
        List<ChatHistory> result = chatHistoryController.list();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chatHistoryService, times(1)).list();
    }

    @Test
    void testGetChatHistoryInfo_Success() {
        // 准备测试数据
        when(chatHistoryService.getById(1L)).thenReturn(testChatHistory);

        // 执行测试
        ChatHistory result = chatHistoryController.getInfo(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testChatHistory.getId(), result.getId());
        assertEquals(testChatHistory.getMessage(), result.getMessage());
        assertEquals(testChatHistory.getMessageType(), result.getMessageType());
        assertEquals(testChatHistory.getAppId(), result.getAppId());
        assertEquals(testChatHistory.getUserId(), result.getUserId());
        verify(chatHistoryService, times(1)).getById(1L);
    }

    @Test
    void testGetChatHistoryInfo_NotFound() {
        // 准备测试数据
        when(chatHistoryService.getById(999L)).thenReturn(null);

        // 执行测试
        ChatHistory result = chatHistoryController.getInfo(999L);

        // 验证结果
        assertNull(result);
        verify(chatHistoryService, times(1)).getById(999L);
    }

    @Test
    void testPageChatHistory_Success() {
        // 准备测试数据
        Page<ChatHistory> inputPage = new Page<>(1, 10);
        Page<ChatHistory> expectedPage = new Page<>();
        expectedPage.setRecords(List.of(testChatHistory));
        expectedPage.setTotalRow(1L);

        when(chatHistoryService.page(inputPage)).thenReturn(expectedPage);

        // 执行测试
        Page<ChatHistory> result = chatHistoryController.page(inputPage);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(testChatHistory.getId(), result.getRecords().get(0).getId());
        verify(chatHistoryService, times(1)).page(inputPage);
    }

    @Test
    void testPageChatHistory_EmptyResult() {
        // 准备测试数据
        Page<ChatHistory> inputPage = new Page<>(1, 10);
        Page<ChatHistory> emptyPage = new Page<>();
        emptyPage.setRecords(new ArrayList<>());
        emptyPage.setTotalRow(0L);

        when(chatHistoryService.page(inputPage)).thenReturn(emptyPage);

        // 执行测试
        Page<ChatHistory> result = chatHistoryController.page(inputPage);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getRecords().size());
        assertEquals(0L, result.getTotalRow());
        verify(chatHistoryService, times(1)).page(inputPage);
    }

    // 边界测试和异常情况测试

    @Test
    void testListAppChatHistory_DifferentPageSizes() {
        // 测试不同页面大小
        int[] pageSizes = {1, 5, 10, 20, 50};

        for (int pageSize : pageSizes) {
            reset(userService, chatHistoryService); // 重置mock

            Page<ChatHistory> expectedPage = new Page<>();
            expectedPage.setRecords(List.of(testChatHistory));

            when(userService.getLoginUser(mockRequest)).thenReturn(normalUser);
            when(chatHistoryService.listAppChatHistoryByPage(eq(100L), eq(pageSize), any(), eq(normalUser)))
                    .thenReturn(expectedPage);

            BaseResponse<Page<ChatHistory>> result =
                    chatHistoryController.listAppChatHistory(100L, pageSize, null, mockRequest);

            assertNotNull(result);
            assertEquals(0, result.getCode());
            verify(chatHistoryService, times(1))
                    .listAppChatHistoryByPage(eq(100L), eq(pageSize), any(), eq(normalUser));
        }
    }

    @Test
    void testQueryRequest_DifferentParameters() {
        // 测试不同查询参数
        ChatHistoryQueryRequest request1 = new ChatHistoryQueryRequest();
        request1.setPageNum(1);
        request1.setPageSize(10);
        request1.setAppId(100L);
        request1.setUserId(2L);

        ChatHistoryQueryRequest request2 = new ChatHistoryQueryRequest();
        request2.setPageNum(2);
        request2.setPageSize(20);
        request2.setMessage("test");
        request2.setMessageType("ai");

        Page<ChatHistory> expectedPage = new Page<>();
        expectedPage.setRecords(List.of(testChatHistory));
        QueryWrapper queryWrapper = mock(QueryWrapper.class);

        when(chatHistoryService.getQueryWrapper(any(ChatHistoryQueryRequest.class)))
                .thenReturn(queryWrapper);
        when(chatHistoryService.page(any(Page.class), any(QueryWrapper.class)))
                .thenReturn(expectedPage);

        // 测试请求1
        BaseResponse<Page<ChatHistory>> result1 =
                chatHistoryController.listAllChatHistoryByPageForAdmin(request1);
        assertNotNull(result1);
        assertEquals(0, result1.getCode());

        // 测试请求2
        BaseResponse<Page<ChatHistory>> result2 =
                chatHistoryController.listAllChatHistoryByPageForAdmin(request2);
        assertNotNull(result2);
        assertEquals(0, result2.getCode());

        // 验证服务调用次数
        verify(chatHistoryService, times(2)).getQueryWrapper(any(ChatHistoryQueryRequest.class));
        verify(chatHistoryService, times(2)).page(any(Page.class), any(QueryWrapper.class));
    }
}