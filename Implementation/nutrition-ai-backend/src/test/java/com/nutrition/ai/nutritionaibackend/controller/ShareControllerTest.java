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

/**
 * Lớp kiểm thử `ShareControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến chức năng chia sẻ nội dung
 * (chia sẻ, xem nội dung công khai, thích và bình luận) trong `ShareController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `ShareController`.
 * `@WithMockUser` giả lập một người dùng đã đăng nhập (với ID mặc định là 1L trong ngữ cảnh này nếu không khai báo rõ hơn),
 * cho phép kiểm thử các điểm cuối yêu cầu xác thực.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `ShareService`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc và cô lập logic của controller để kiểm thử.
 */
@WebMvcTest(ShareController.class)
@WithMockUser // Giả lập người dùng đã đăng nhập (mặc định ID là 1L trong ngữ cảnh này nếu không khai báo rõ hơn)
class ShareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShareService shareService; // Service giả lập cho chức năng chia sẻ

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Kiểm thử kịch bản thành công khi chia sẻ nội dung.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `payload` (Map) chứa `contentId`, `contentType` và `visibility`.
     * 2. Chuẩn bị một `SharedContentDto` mong đợi sau khi chia sẻ.
     * 3. Giả lập `shareService.shareContent()` để trả về `SharedContentDto` đã chia sẻ, mô phỏng việc chia sẻ thành công.
     *    `eq(1L)` được sử dụng để khớp với `userId` mặc định của `@WithMockUser`.
     * 4. Thực hiện yêu cầu POST đến `/api/share` với `payload` dưới dạng JSON và token CSRF.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 201 CREATED.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi lấy tất cả nội dung công khai.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một danh sách `SharedContentDto` giả lập đại diện cho nội dung công khai.
     * 2. Giả lập `shareService.getAllPublicContent()` để trả về danh sách giả lập.
     * 3. Thực hiện yêu cầu GET đến `/api/share/public`.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và nội dung JSON chứa dữ liệu nội dung công khai mong muốn.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi thích một nội dung.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `contentId`.
     * 2. Giả lập `shareService.likeContent()` để không làm gì (doNothing) khi được gọi, vì đây là phương thức `void`.
     * 3. Thực hiện yêu cầu POST đến `/api/share/{contentId}/like` với token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi bình luận về một nội dung.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị `contentId` và `commentText`.
     * 2. Chuẩn bị một `payload` (Map) chứa `text` của bình luận.
     * 3. Chuẩn bị một `ContentCommentDto` mong đợi sau khi bình luận.
     * 4. Giả lập `shareService.commentOnContent()` để trả về `ContentCommentDto` đã tạo, mô phỏng việc bình luận thành công.
     * 5. Thực hiện yêu cầu POST đến `/api/share/{contentId}/comment` với `payload` dưới dạng JSON và token CSRF.
     * 6. Xác minh rằng phản hồi có trạng thái HTTP 201 CREATED.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi lấy tất cả các bình luận cho một nội dung cụ thể.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị `contentId`.
     * 2. Chuẩn bị một danh sách `ContentCommentDto` giả lập đại diện cho các bình luận.
     * 3. Giả lập `shareService.getCommentsForContent()` để trả về danh sách bình luận giả lập.
     * 4. Thực hiện yêu cầu GET đến `/api/share/{contentId}/comments`.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và nội dung JSON chứa dữ liệu bình luận mong muốn.
     */
    @Test
    void testGetCommentsForContent_Success() throws Exception {
        Long contentId = 1L;
        // 1. Chuẩn bị danh sách bình luận giả lập
        ContentCommentDto commentDto1 = new ContentCommentDto(1L, contentId, 1L, "user1", "Comment 1", LocalDateTime.now());
        ContentCommentDto commentDto2 = new ContentCommentDto(2L, contentId, 2L, "user2", "Comment 2", LocalDateTime.now());
        List<ContentCommentDto> comments = Arrays.asList(commentDto1, commentDto2);

        // 2. Mocking Service: Giả lập trả về danh sách bình luận
        when(shareService.getCommentsForContent(eq(contentId))).thenReturn(comments);

        // 3. Thực hiện yêu cầu GET /api/share/{contentId}/comments
        mockMvc.perform(get("/api/share/{contentId}/comments", contentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].text").value("Comment 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].text").value("Comment 2"));
    }
}