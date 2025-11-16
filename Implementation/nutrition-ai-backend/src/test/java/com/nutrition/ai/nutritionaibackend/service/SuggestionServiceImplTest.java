package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.Suggestion;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.SuggestionRepository;
import com.nutrition.ai.nutritionaibackend.service.ai.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * {@code SuggestionServiceImplTest} là một lớp kiểm thử đơn vị cho {@link SuggestionServiceImpl}.
 * Lớp này sử dụng Mockito để mô phỏng các dependencies ({@link SuggestionRepository}, {@link AiService}, và {@link ModelMapper})
 * và JUnit 5 để thực thi các trường hợp kiểm thử.
 * Mục tiêu là đảm bảo rằng {@link SuggestionServiceImpl} hoạt động chính xác trong các tình huống khác nhau,
 * bao gồm lưu gợi ý, tìm kiếm tất cả gợi ý theo người dùng, và nhận đề xuất từ AI.
 */
@ExtendWith(MockitoExtension.class)
class SuggestionServiceImplTest {

    /**
     * Mô phỏng {@link SuggestionRepository} để kiểm soát hành vi lưu trữ của {@link Suggestion}.
     */
    @Mock
    private SuggestionRepository suggestionRepository;

    /**
     * Mô phỏng {@link AiService} để kiểm soát hành vi gọi các dịch vụ AI.
     */
    @Mock
    private AiService aiService;

    /**
     * Mô phỏng {@link ModelMapper} để kiểm soát hành vi ánh xạ giữa các đối tượng DTO và Domain.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Tiêm {@link SuggestionServiceImpl} và tiêm các mock đã tạo ở trên vào đó.
     */
    @InjectMocks
    private SuggestionServiceImpl suggestionService;

    private User user;
    private Suggestion suggestion1;
    private Suggestion suggestion2;
    private SuggestionDto suggestionDto1;
    private SuggestionDto suggestionDto2;

    /**
     * Thiết lập dữ liệu kiểm thử chung trước mỗi phương thức kiểm thử.
     * Khởi tạo các đối tượng {@link User}, {@link Suggestion} và {@link SuggestionDto} để sử dụng trong các kiểm thử.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        suggestion1.setUser(user);
        suggestion1.setSuggestionText("Eat more fruits");

        suggestion2 = new Suggestion();
        suggestion2.setId(2L);
        suggestion2.setUser(user);
        suggestion2.setSuggestionText("Drink more water");

        suggestionDto1 = new SuggestionDto();
        suggestionDto1.setId(1L);
        suggestionDto1.setSuggestionText("Eat more fruits");

        suggestionDto2 = new SuggestionDto();
        suggestionDto2.setId(2L);
        suggestionDto2.setSuggestionText("Drink more water");
    }

    /**
     * Kiểm tra phương thức {@code save} của {@link SuggestionServiceImpl}.
     * Xác minh rằng {@link SuggestionDto} được ánh xạ thành {@link Suggestion}, được lưu vào kho lưu trữ,
     * và sau đó được ánh xạ trở lại thành {@link SuggestionDto} và trả về.
     */
    @Test
    void testSave() {
        when(modelMapper.map(suggestionDto1, Suggestion.class)).thenReturn(suggestion1);
        when(suggestionRepository.save(suggestion1)).thenReturn(suggestion1);
        when(modelMapper.map(suggestion1, SuggestionDto.class)).thenReturn(suggestionDto1);

        SuggestionDto result = suggestionService.save(suggestionDto1);

        assertNotNull(result);
        assertEquals(suggestionDto1.getId(), result.getId());
        assertEquals(suggestionDto1.getSuggestionText(), result.getSuggestionText());
        verify(modelMapper, times(1)).map(suggestionDto1, Suggestion.class);
        verify(suggestionRepository, times(1)).save(suggestion1);
        verify(modelMapper, times(1)).map(suggestion1, SuggestionDto.class);
    }

    /**
     * Kiểm tra phương thức {@code findAllByUser} khi người dùng có các gợi ý.
     * Xác minh rằng tất cả các gợi ý thuộc về người dùng được trả về và được ánh xạ chính xác sang {@link SuggestionDto}.
     */
    @Test
    void testFindAllByUser() {
        when(suggestionRepository.findAll()).thenReturn(Arrays.asList(suggestion1, suggestion2));
        when(modelMapper.map(suggestion1, SuggestionDto.class)).thenReturn(suggestionDto1);
        when(modelMapper.map(suggestion2, SuggestionDto.class)).thenReturn(suggestionDto2);

        List<SuggestionDto> results = suggestionService.findAllByUser(user);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(suggestionDto1.getId(), results.get(0).getId());
        assertEquals(suggestionDto2.getId(), results.get(1).getId());
        verify(suggestionRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Suggestion.class), eq(SuggestionDto.class));
    }

    /**
     * Kiểm tra phương thức {@code findAllByUser} khi có các gợi ý từ người dùng khác trong kho lưu trữ.
     * Xác minh rằng chỉ các gợi ý thuộc về người dùng được chỉ định được trả về.
     */
    @Test
    void testFindAllByUser_WithOtherUsersSuggestions() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");

        Suggestion suggestionFromOtherUser = new Suggestion();
        suggestionFromOtherUser.setId(3L);
        suggestionFromOtherUser.setUser(otherUser);
        suggestionFromOtherUser.setSuggestionText("Other user's recommendation");

        // Mocking findAll to return suggestions for both the test user and another user
        when(suggestionRepository.findAll()).thenReturn(Arrays.asList(suggestion1, suggestion2, suggestionFromOtherUser));
        when(modelMapper.map(suggestion1, SuggestionDto.class)).thenReturn(suggestionDto1);
        when(modelMapper.map(suggestion2, SuggestionDto.class)).thenReturn(suggestionDto2);

        List<SuggestionDto> results = suggestionService.findAllByUser(user);

        assertNotNull(results);
        assertEquals(2, results.size());
        // Ensure only suggestions for 'user' are returned
        assertEquals(suggestionDto1.getId(), results.get(0).getId());
        assertEquals(suggestionDto2.getId(), results.get(1).getId());
        // Verify that map was called only for the suggestions belonging to 'user'
        verify(modelMapper, times(2)).map(any(Suggestion.class), eq(SuggestionDto.class));
        verify(suggestionRepository, times(1)).findAll();
    }

    /**
     * Kiểm tra phương thức {@code findAllByUser} khi không có gợi ý nào cho người dùng được chỉ định.
     * Xác minh rằng một danh sách trống được trả về.
     */
    @Test
    void testFindAllByUser_NoSuggestions() {
        when(suggestionRepository.findAll()).thenReturn(Collections.emptyList());

        List<SuggestionDto> results = suggestionService.findAllByUser(user);

        assertNotNull(results);
        assertEquals(0, results.size());
        verify(suggestionRepository, times(1)).findAll();
        verify(modelMapper, times(0)).map(any(Suggestion.class), eq(SuggestionDto.class));
    }

    /**
     * Kiểm tra phương thức {@code getAiRecommendations} để đảm bảo nó gọi {@link AiService} và trả về kết quả.
     * Xác minh rằng phương thức {@link AiService#getNutritionRecommendations} được gọi một lần
     * với các đối số chính xác và phản hồi được trả về nguyên vẹn.
     */
    @Test
    void testGetAiRecommendations() {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("age", 30);
        Map<String, Object> dietaryPreferences = new HashMap<>();
        dietaryPreferences.put("vegan", true);
        Map<String, Object> healthGoals = new HashMap<>();
        healthGoals.put("weightLoss", true);

        Map<String, Object> aiResponse = new HashMap<>();
        aiResponse.put("recommendation", "Eat less meat");

        when(aiService.getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals)).thenReturn(aiResponse);

        Map<String, Object> result = suggestionService.getAiRecommendations(userProfile, dietaryPreferences, healthGoals);

        assertNotNull(result);
        assertEquals("Eat less meat", result.get("recommendation"));
        verify(aiService, times(1)).getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals);
    }
}
