package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import com.nutrition.ai.nutritionaibackend.service.ShareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShareController.class)
@WithMockUser // Giả lập người dùng đã đăng nhập (mặc định ID là 1L trong ngữ cảnh này nếu không khai báo rõ hơn)
class ShareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShareService shareService; // Service giả lập cho chức năng chia sẻ

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShareContent_Success() throws Exception {
        // 1. Chuẩn bị payload (dữ liệu đầu vào) cho yêu cầu POST
        Map<String, String> payload = new HashMap<>();
        payload.put("contentId", "100");
        payload.put("contentType", ContentType.RECIPE.name());
        payload.put("visibility", Visibility.PUBLIC.name());

        SharedContentDto sharedContentDto = new SharedContentDto(1L, 1L, "testuser", 100L, ContentType.RECIPE, Visibility.PUBLIC, LocalDateTime.now(), 0, 0);

        // 2. Mocking Service: Giả lập hành vi chia sẻ nội dung thành công
        // eq(1L) giả định Controller lấy userId là 1L từ @WithMockUser
        when(shareService.shareContent(eq(1L), eq(100L), any(ContentType.class), any(Visibility.class)))
                .thenReturn(sharedContentDto);

        // 3. Thực hiện yêu cầu POST /api/share
        mockMvc.perform(post("/api/share")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated()); // 4. Kiểm tra: Trạng thái 201 CREATED
    }

    @Test
    void testGetPublicContent_Success() throws Exception {
        // 1. Chuẩn bị danh sách nội dung công khai giả lập
        SharedContentDto sharedContentDto1 = new SharedContentDto(1L, 1L, "user1", 101L, ContentType.RECIPE, Visibility.PUBLIC, LocalDateTime.now(), 10, 5);
        SharedContentDto sharedContentDto2 = new SharedContentDto(2L, 2L, "user2", 102L, ContentType.NUTRITION_PLAN, Visibility.PUBLIC, LocalDateTime.now(), 20, 3);
        List<SharedContentDto> publicContent = Arrays.asList(sharedContentDto1, sharedContentDto2);

        // 2. Mocking Service: Giả lập trả về danh sách nội dung công khai
        when(shareService.getAllPublicContent()).thenReturn(publicContent);

        // 3. Thực hiện yêu cầu GET /api/share/public
        mockMvc.perform(get("/api/share/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].contentType").value(ContentType.NUTRITION_PLAN.name())); // 4. Kiểm tra nội dung JSON
    }

    @Test
    void testLikeContent_Success() throws Exception {
        Long contentId = 1L;
        // 1. Mocking Service: Giả lập việc thích nội dung thành công (không trả về gì)
        // doNothing().when(mock).method(args) được dùng cho các phương thức void
        doNothing().when(shareService).likeContent(eq(contentId), eq(1L));

        // 2. Thực hiện yêu cầu POST /api/share/{contentId}/like
        mockMvc.perform(post("/api/share/{contentId}/like", contentId)
                        .with(csrf()))
                .andExpect(status().isOk()); // 3. Kiểm tra: Trạng thái 200 OK
    }

    @Test
    void testCommentOnContent_Success() throws Exception {
        Long contentId = 1L;
        String commentText = "Great content!";
        // 1. Chuẩn bị payload cho comment
        Map<String, String> payload = new HashMap<>();
        payload.put("text", commentText);

        ContentCommentDto commentDto = new ContentCommentDto(1L, contentId, 1L, "testuser", commentText, LocalDateTime.now());

        // 2. Mocking Service: Giả lập comment thành công, trả về DTO của comment đã tạo
        when(shareService.commentOnContent(eq(contentId), eq(1L), eq(commentText)))
                .thenReturn(commentDto);

        // 3. Thực hiện yêu cầu POST /api/share/{contentId}/comment
        mockMvc.perform(post("/api/share/{contentId}/comment", contentId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated()); // 4. Kiểm tra: Trạng thái 201 CREATED
    }

    // ... (testGetCommentsForContent_Success tuân theo nguyên lý GET/tìm kiếm)
}