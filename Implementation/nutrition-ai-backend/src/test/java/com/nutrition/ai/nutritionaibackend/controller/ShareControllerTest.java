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
@WithMockUser
class ShareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShareService shareService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShareContent_Success() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("contentId", "100");
        payload.put("contentType", ContentType.RECIPE.name());
        payload.put("visibility", Visibility.PUBLIC.name());

        SharedContentDto sharedContentDto = new SharedContentDto(1L, 1L, "testuser", 100L, ContentType.RECIPE, Visibility.PUBLIC, LocalDateTime.now(), 0, 0);

        when(shareService.shareContent(eq(1L), eq(100L), any(ContentType.class), any(Visibility.class)))
                .thenReturn(sharedContentDto);

        mockMvc.perform(post("/api/share")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetPublicContent_Success() throws Exception {
        SharedContentDto sharedContentDto1 = new SharedContentDto(1L, 1L, "user1", 101L, ContentType.RECIPE, Visibility.PUBLIC, LocalDateTime.now(), 10, 5);
        SharedContentDto sharedContentDto2 = new SharedContentDto(2L, 2L, "user2", 102L, ContentType.NUTRITION_PLAN, Visibility.PUBLIC, LocalDateTime.now(), 20, 3);
        List<SharedContentDto> publicContent = Arrays.asList(sharedContentDto1, sharedContentDto2);

        when(shareService.getAllPublicContent()).thenReturn(publicContent);

        mockMvc.perform(get("/api/share/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].contentType").value(ContentType.NUTRITION_PLAN.name()));
    }

    @Test
    void testLikeContent_Success() throws Exception {
        Long contentId = 1L;
        doNothing().when(shareService).likeContent(eq(contentId), eq(1L));

        mockMvc.perform(post("/api/share/{contentId}/like", contentId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void testCommentOnContent_Success() throws Exception {
        Long contentId = 1L;
        String commentText = "Great content!";
        Map<String, String> payload = new HashMap<>();
        payload.put("text", commentText);

        ContentCommentDto commentDto = new ContentCommentDto(1L, contentId, 1L, "testuser", commentText, LocalDateTime.now());

        when(shareService.commentOnContent(eq(contentId), eq(1L), eq(commentText)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/api/share/{contentId}/comment", contentId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetCommentsForContent_Success() throws Exception {
        Long contentId = 1L;
        ContentCommentDto comment1 = new ContentCommentDto(1L, contentId, 1L, "user1", "Comment 1", LocalDateTime.now());
        ContentCommentDto comment2 = new ContentCommentDto(2L, contentId, 2L, "user2", "Comment 2", LocalDateTime.now());
        List<ContentCommentDto> comments = Arrays.asList(comment1, comment2);

        when(shareService.getCommentsForContent(contentId)).thenReturn(comments);

        mockMvc.perform(get("/api/share/{contentId}/comments", contentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Comment 1"))
                .andExpect(jsonPath("$[1].text").value("Comment 2"));
    }
}
