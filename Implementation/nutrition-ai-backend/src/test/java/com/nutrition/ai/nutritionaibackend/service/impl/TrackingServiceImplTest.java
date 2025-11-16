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

/**
 * {@code TrackingServiceImplTest} là một lớp kiểm thử đơn vị cho {@link TrackingServiceImpl}.
 * Lớp này sử dụng JUnit 5 và Mockito để kiểm thử các phương thức logic nghiệp vụ của dịch vụ theo dõi,
 * cô lập {@link TrackingServiceImpl} khỏi các phụ thuộc bên ngoài như cơ sở dữ liệu thông qua các repository mock
 * ({@link FoodLogRepository}, {@link ActivityLogRepository}, {@link ReportRepository}, {@link SuggestionRepository}, {@link UserRepository})
 * và ánh xạ đối tượng thông qua {@link ModelMapper}.
 * Mục tiêu chính là đảm bảo rằng các chức năng ghi nhật ký thực phẩm, ghi nhật ký hoạt động và tạo báo cáo tiến độ
 * hoạt động chính xác và xử lý các trường hợp biên như không tìm thấy người dùng hoặc không có dữ liệu.
 */
@ExtendWith(MockitoExtension.class)
class TrackingServiceImplTest {

    /**
     * Mô phỏng {@link FoodLogRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link FoodLog}.
     */
    @Mock
    private FoodLogRepository foodLogRepository;
    /**
     * Mô phỏng {@link ActivityLogRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link ActivityLog}.
     */
    @Mock
    private ActivityLogRepository activityLogRepository;
    /**
     * Mô phỏng {@link ReportRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link Report}.
     */
    @Mock
    private ReportRepository reportRepository;
    /**
     * Mô phỏng {@link SuggestionRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link Suggestion}.
     */
    @Mock
    private SuggestionRepository suggestionRepository;
    /**
     * Mô phỏng {@link UserRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link User}.
     */
    @Mock
    private UserRepository userRepository;
    /**
     * Mô phỏng {@link ModelMapper} để kiểm soát hành vi ánh xạ giữa các đối tượng DTO và Domain.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Tiêm {@link TrackingServiceImpl} và tự động tiêm các đối tượng {@code @Mock} vào các trường tương ứng của nó.
     * Đây là đối tượng thực sẽ được kiểm thử.
     */
    @InjectMocks
    private TrackingServiceImpl trackingService;

    private User user;
    private FoodLog foodLog;
    private ActivityLog activityLog;
    private FoodLogDto foodLogDto;
    private ActivityLogDto activityLogDto;
    private Suggestion suggestion;
    private SuggestionDto suggestionDto;

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (phương thức được chú thích bởi {@code @Test}).
     * Phương thức này chịu trách nhiệm khởi tạo các đối tượng dữ liệu giả định như {@link User}, {@link FoodLog},
     * {@link ActivityLog}, {@link Suggestion} và các DTO tương ứng để sử dụng trong các kiểm thử.
     */
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
     * Kiểm thử phương thức {@code logFood} của {@link TrackingServiceImpl} khi người dùng tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng người dùng tồn tại,
     *    ánh xạ {@link FoodLogDto} thành {@link FoodLog}, lưu {@link FoodLog} và ánh xạ ngược lại thành {@link FoodLogDto}.
     * 2. Gọi phương thức {@code logFood} thực tế của {@code trackingService}.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link FoodLogDto} trả về không null và các thuộc tính khớp với đối tượng giả định.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById}, {@code modelMapper.map(FoodLogDto, FoodLog.class)},
     *    {@code foodLogRepository.save} và {@code modelMapper.map(FoodLog, FoodLogDto.class)} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code logFood} của {@link TrackingServiceImpl} khi người dùng không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code logFood} thực tế của {@code trackingService} với ID người dùng không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link RuntimeException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác là "User not found" hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng các phương thức {@code map} và {@code save} KHÔNG BAO GIỜ được gọi.
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
     * Kiểm thử phương thức {@code logActivity} của {@link TrackingServiceImpl} khi người dùng tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng người dùng tồn tại,
     *    ánh xạ {@link ActivityLogDto} thành {@link ActivityLog}, lưu {@link ActivityLog} và ánh xạ ngược lại thành {@link ActivityLogDto}.
     * 2. Gọi phương thức {@code logActivity} thực tế của {@code trackingService}.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link ActivityLogDto} trả về không null và các thuộc tính khớp với đối tượng giả định.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById}, {@code modelMapper.map(ActivityLogDto, ActivityLog.class)},
     *    {@code activityLogRepository.save} và {@code modelMapper.map(ActivityLog, ActivityLogDto.class)} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code logActivity} của {@link TrackingServiceImpl} khi người dùng không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code logActivity} thực tế của {@code trackingService} với ID người dùng không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link RuntimeException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác là "User not found" hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng các phương thức {@code map} và {@code save} KHÔNG BAO GIỜ được gọi.
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
     * Kiểm thử phương thức {@code generateProgressReport} của {@link TrackingServiceImpl} với báo cáo hàng tuần và dữ liệu đầy đủ.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng người dùng tồn tại,
     *    trả về nhật ký thực phẩm, nhật ký hoạt động và gợi ý, sau đó ánh xạ chúng thành các DTO tương ứng.
     * 2. Thiết lập hành vi giả lập cho {@code reportRepository.save} để mô phỏng việc lưu báo cáo và thiết lập ID.
     * 3. Gọi phương thức {@code generateProgressReport} thực tế của {@code trackingService} với loại báo cáo "weekly".
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link ProgressReportDto} trả về không null, loại báo cáo đúng,
     *    và các danh sách nhật ký/gợi ý không trống và có kích thước chính xác.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById}, {@code foodLogRepository.findByUserAndLogDateBetween},
     *    {@code activityLogRepository.findByUserAndLogDateBetween}, {@code suggestionRepository.findByUser},
     *    {@code reportRepository.save} và các phương thức {@code modelMapper.map} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code generateProgressReport} của {@link TrackingServiceImpl} với báo cáo hàng tháng và dữ liệu trống.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository để mô phỏng người dùng tồn tại, nhưng không có nhật ký hoặc gợi ý nào.
     * 2. Thiết lập hành vi giả lập cho {@code reportRepository.save} để mô phỏng việc lưu báo cáo và thiết lập ID.
     * 3. Gọi phương thức {@code generateProgressReport} thực tế của {@code trackingService} với loại báo cáo "monthly".
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link ProgressReportDto} trả về không null, loại báo cáo đúng,
     *    và các danh sách nhật ký/gợi ý là trống.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById}, {@code foodLogRepository.findByUserAndLogDateBetween},
     *    {@code activityLogRepository.findByUserAndLogDateBetween}, {@code suggestionRepository.findByUser}
     *    và {@code reportRepository.save} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng các phương thức {@code modelMapper.map} KHÔNG BAO GIỜ được gọi cho nhật ký hoặc gợi ý,
     *    vì không có dữ liệu để ánh xạ.
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
     * Kiểm thử phương thức {@code generateProgressReport} của {@link TrackingServiceImpl} khi người dùng không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code generateProgressReport} thực tế của {@code trackingService} với ID người dùng không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link RuntimeException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác là "User not found" hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng các phương thức repository và mapper khác KHÔNG BAO GIỜ được gọi.
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
