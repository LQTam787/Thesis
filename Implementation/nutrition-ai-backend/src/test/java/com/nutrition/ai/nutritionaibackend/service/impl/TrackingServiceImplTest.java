package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.*;
import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import com.nutrition.ai.nutritionaibackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingServiceImplTest {

    @Mock
    private FoodLogRepository foodLogRepository;
    @Mock
    private ActivityLogRepository activityLogRepository;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrackingServiceImpl trackingService;

    private User user;
    private FoodLog foodLog;
    private ActivityLog activityLog;
    private FoodLogDto foodLogDto;
    private ActivityLogDto activityLogDto;
    private Suggestion suggestion;
    private SuggestionDto suggestionDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        foodLogDto = new FoodLogDto();
        foodLogDto.setQuantity(200.0);
        foodLogDto.setUnit("g");

        foodLog = new FoodLog();
        foodLog.setId(1L);
        foodLog.setQuantity(200.0);
        foodLog.setUnit("g");
        foodLog.setUser(user);
        foodLog.setLogDate(LocalDateTime.now());

        activityLogDto = new ActivityLogDto();
        activityLogDto.setDurationInMinutes(30);
        activityLogDto.setCaloriesBurned(300.0);

        activityLog = new ActivityLog();
        activityLog.setId(1L);
        activityLog.setDurationInMinutes(30);
        activityLog.setCaloriesBurned(300.0);
        activityLog.setUser(user);
        activityLog.setLogDate(LocalDateTime.now());

        suggestion = new Suggestion();
        suggestion.setId(1L);
        suggestion.setSuggestionText("Eat more vegetables.");
        suggestion.setUser(user);

        suggestionDto = new SuggestionDto();
        suggestionDto.setId(1L);
        suggestionDto.setSuggestionText("Eat more vegetables.");
    }

    /**
     * Test case cho phương thức logFood khi người dùng tồn tại.
     * Đảm bảo rằng FoodLog được lưu trữ và FoodLogDto được trả về chính xác.
     */
    @Test
    void logFood_UserExists_ReturnsFoodLogDto() {
        // Mocking
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(foodLogDto, FoodLog.class)).thenReturn(foodLog);
        when(foodLogRepository.save(any(FoodLog.class))).thenReturn(foodLog);
        when(modelMapper.map(foodLog, FoodLogDto.class)).thenReturn(foodLogDto);

        // Call the method
        FoodLogDto result = trackingService.logFood("1", foodLogDto);

        // Verification
        assertNotNull(result);
        assertEquals(foodLogDto.getQuantity(), result.getQuantity());
        assertEquals(foodLogDto.getUnit(), result.getUnit());

        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(foodLogDto, FoodLog.class);
        verify(foodLogRepository, times(1)).save(any(FoodLog.class));
        verify(modelMapper, times(1)).map(foodLog, FoodLogDto.class);
    }

    /**
     * Test case cho phương thức logFood khi người dùng không tồn tại.
     * Đảm bảo rằng RuntimeException được ném ra.
     */
    @Test
    void logFood_UserNotFound_ThrowsRuntimeException() {
        // Mocking
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trackingService.logFood("99", foodLogDto));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(FoodLogDto.class), eq(FoodLog.class));
        verify(foodLogRepository, never()).save(any(FoodLog.class));
    }

    /**
     * Test case cho phương thức logActivity khi người dùng tồn tại.
     * Đảm bảo rằng ActivityLog được lưu trữ và ActivityLogDto được trả về chính xác.
     */
    @Test
    void logActivity_UserExists_ReturnsActivityLogDto() {
        // Mocking
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(activityLogDto, ActivityLog.class)).thenReturn(activityLog);
        when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(activityLog);
        when(modelMapper.map(activityLog, ActivityLogDto.class)).thenReturn(activityLogDto);

        // Call the method
        ActivityLogDto result = trackingService.logActivity("1", activityLogDto);

        // Verification
        assertNotNull(result);
        assertEquals(activityLogDto.getDurationInMinutes(), result.getDurationInMinutes());
        assertEquals(activityLogDto.getCaloriesBurned(), result.getCaloriesBurned());

        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(activityLogDto, ActivityLog.class);
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
        verify(modelMapper, times(1)).map(activityLog, ActivityLogDto.class);
    }

    /**
     * Test case cho phương thức logActivity khi người dùng không tồn tại.
     * Đảm bảo rằng RuntimeException được ném ra.
     */
    @Test
    void logActivity_UserNotFound_ThrowsRuntimeException() {
        // Mocking
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trackingService.logActivity("99", activityLogDto));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(ActivityLogDto.class), eq(ActivityLog.class));
        verify(activityLogRepository, never()).save(any(ActivityLog.class));
    }

    /**
     * Test case cho phương thức generateProgressReport với báo cáo hàng tuần và dữ liệu đầy đủ.
     * Đảm bảo rằng ProgressReportDto được tạo và trả về chính xác.
     */
    @Test
    void generateProgressReport_WeeklyReport_ReturnsProgressReportDto() {
        // Mocking
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodLogRepository.findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(foodLog));
        when(activityLogRepository.findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(activityLog));
        when(suggestionRepository.findByUser(user)).thenReturn(Collections.singletonList(suggestion));
        when(modelMapper.map(foodLog, FoodLogDto.class)).thenReturn(foodLogDto);
        when(modelMapper.map(activityLog, ActivityLogDto.class)).thenReturn(activityLogDto);
        when(modelMapper.map(suggestion, SuggestionDto.class)).thenReturn(suggestionDto);
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(1L); // Simulate ID being set after saving
            return report;
        });


        // Call the method
        ProgressReportDto result = trackingService.generateProgressReport("1", "weekly");

        // Verification
        assertNotNull(result);
        assertEquals(EReportType.WEEKLY_PROGRESS, result.getReportType());
        assertFalse(result.getFoodLogs().isEmpty());
        assertEquals(1, result.getFoodLogs().size());
        assertEquals(foodLogDto.getQuantity(), result.getFoodLogs().get(0).getQuantity());
        assertFalse(result.getActivityLogs().isEmpty());
        assertEquals(1, result.getActivityLogs().size());
        assertEquals(activityLogDto.getDurationInMinutes(), result.getActivityLogs().get(0).getDurationInMinutes());
        assertFalse(result.getSuggestions().isEmpty());
        assertEquals(1, result.getSuggestions().size());
        assertEquals(suggestionDto.getSuggestionText(), result.getSuggestions().get(0).getSuggestionText());

        verify(userRepository, times(1)).findById(1L);
        verify(foodLogRepository, times(1)).findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(activityLogRepository, times(1)).findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(suggestionRepository, times(1)).findByUser(user);
        verify(reportRepository, times(1)).save(any(Report.class));
        verify(modelMapper, times(1)).map(foodLog, FoodLogDto.class);
        verify(modelMapper, times(1)).map(activityLog, ActivityLogDto.class);
        verify(modelMapper, times(1)).map(suggestion, SuggestionDto.class);
    }

    /**
     * Test case cho phương thức generateProgressReport với báo cáo hàng tháng và dữ liệu trống.
     * Đảm bảo rằng ProgressReportDto được tạo và trả về chính xác, ngay cả khi không có log hoặc suggestion nào.
     */
    @Test
    void generateProgressReport_MonthlyReport_NoLogsOrSuggestions_ReturnsEmptyProgressReportDto() {
        // Mocking
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodLogRepository.findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(activityLogRepository.findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(suggestionRepository.findByUser(user)).thenReturn(Collections.emptyList());
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(1L);
            return report;
        });

        // Call the method
        ProgressReportDto result = trackingService.generateProgressReport("1", "monthly");

        // Verification
        assertNotNull(result);
        assertEquals(EReportType.MONTHLY_PROGRESS, result.getReportType());
        assertTrue(result.getFoodLogs().isEmpty());
        assertTrue(result.getActivityLogs().isEmpty());
        assertTrue(result.getSuggestions().isEmpty());

        verify(userRepository, times(1)).findById(1L);
        verify(foodLogRepository, times(1)).findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(activityLogRepository, times(1)).findByUserAndLogDateBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(suggestionRepository, times(1)).findByUser(user);
        verify(reportRepository, times(1)).save(any(Report.class));
        verify(modelMapper, never()).map(any(FoodLog.class), eq(FoodLogDto.class));
        verify(modelMapper, never()).map(any(ActivityLog.class), eq(ActivityLogDto.class));
        verify(modelMapper, never()).map(any(Suggestion.class), eq(SuggestionDto.class));
    }

    /**
     * Test case cho phương thức generateProgressReport khi người dùng không tồn tại.
     * Đảm bảo rằng RuntimeException được ném ra.
     */
    @Test
    void generateProgressReport_UserNotFound_ThrowsRuntimeException() {
        // Mocking
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trackingService.generateProgressReport("99", "weekly"));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(99L);
        verify(foodLogRepository, never()).findByUserAndLogDateBetween(any(), any(), any());
        verify(activityLogRepository, never()).findByUserAndLogDateBetween(any(), any(), any());
        verify(suggestionRepository, never()).findByUser(any());
        verify(reportRepository, never()).save(any(Report.class));
    }
}
