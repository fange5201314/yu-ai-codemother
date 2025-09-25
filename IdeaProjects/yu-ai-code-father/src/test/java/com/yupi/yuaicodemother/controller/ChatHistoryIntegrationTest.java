package com.yupi.yuaicodemother.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.yuaicodemother.model.entity.ChatHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ChatHistory 集成测试
 * 使用真实数据库进行测试
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test") // 使用测试配置
@Transactional // 每个测试方法结束后回滚数据库操作
class ChatHistoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testChatHistoryWorkflow() throws Exception {
        // 1. 保存对话历史
        ChatHistory chatHistory = ChatHistory.builder()
                .message("集成测试消息")
                .messageType("user")
                .appId(1L)
                .userId(1L)
                .build();

        String chatHistoryJson = objectMapper.writeValueAsString(chatHistory);

        mockMvc.perform(post("/api/chatHistory/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(chatHistoryJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 2. 查询对话历史列表
        mockMvc.perform(get("/api/chatHistory/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 3. 分页查询
        mockMvc.perform(get("/api/chatHistory/page")
                .param("pageNumber", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }
}