package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.*;
import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import com.nutrition.ai.nutritionaibackend.repository.*;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the TrackingService interface.
 * Handles the business logic for logging food intake, activities, and generating progress reports.
 * Lớp này triển khai giao diện TrackingService, xử lý logic nghiệp vụ cho việc ghi nhật ký
 * thức ăn, hoạt động và tạo báo cáo tiến độ.
 */
@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    // Inject các Repository và ModelMapper
    private final FoodLogRepository foodLogRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ReportRepository reportRepository;
    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Ghi nhật ký thức ăn (Food Log).
     * 1. Tìm User bằng userId. Ném RuntimeException nếu không tìm thấy.
     * 2. Chuyển FoodLogDto sang FoodLog Entity.
     * 3. Thiết lập User và LogDate (thời điểm hiện tại) cho FoodLog.
     * 4. Lưu FoodLog vào cơ sở dữ liệu.
     * 5. Chuyển đổi và trả về FoodLogDto của nhật ký đã lưu.
     *
     * @param userId ID của người dùng (dạng String, sau đó được parse thành Long).
     * @param foodLogDto DTO chứa thông tin nhật ký thức ăn.
     * @return FoodLogDto của nhật ký đã được ghi.
     */
    @Override
    public FoodLogDto logFood(String userId, FoodLogDto foodLogDto) {
        // 1. Tìm User
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        // 2. Chuyển đổi sang Entity
        FoodLog foodLog = modelMapper.map(foodLogDto, FoodLog.class);
        // 3. Thiết lập User và LogDate
        foodLog.setUser(user);
        foodLog.setLogDate(LocalDateTime.now());
        // 4. Lưu và 5. Trả về
        FoodLog savedLog = foodLogRepository.save(foodLog);
        return modelMapper.map(savedLog, FoodLogDto.class);
    }

    /**
     * Ghi nhật ký hoạt động (Activity Log).
     * Logic tương tự như logFood.
     *
     * @param userId ID của người dùng.
     * @param activityLogDto DTO chứa thông tin nhật ký hoạt động.
     * @return ActivityLogDto của nhật ký đã được ghi.
     */
    @Override
    public ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto) {
        // 1. Tìm User
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        // 2. Chuyển đổi sang Entity
        ActivityLog activityLog = modelMapper.map(activityLogDto, ActivityLog.class);
        // 3. Thiết lập User và LogDate
        activityLog.setUser(user);
        activityLog.setLogDate(LocalDateTime.now());
        // 4. Lưu và 5. Trả về
        ActivityLog savedLog = activityLogRepository.save(activityLog);
        return modelMapper.map(savedLog, ActivityLogDto.class);
    }

    /**
     * Tạo báo cáo tiến độ (Progress Report) cho người dùng.
     * 1. Tìm User bằng userId. Ném RuntimeException nếu không tìm thấy.
     * 2. Xác định phạm vi ngày (tuần hoặc tháng) dựa trên reportType.
     * 3. Truy vấn FoodLog và ActivityLog trong phạm vi ngày đã xác định cho người dùng.
     * 4. Truy vấn các Suggestion (Gợi ý) hiện có cho người dùng.
     * 5. Tạo và lưu Report Entity mới (lưu bản tóm tắt báo cáo).
     * 6. Chuyển đổi các Entity (FoodLog, ActivityLog, Suggestion) thành DTO.
     * 7. Tạo và trả về ProgressReportDto, bao gồm thông tin báo cáo và các log/suggestion liên quan.
     *
     * @param userId ID của người dùng.
     * @param reportType Loại báo cáo ("weekly" hoặc "monthly").
     * @return ProgressReportDto chứa dữ liệu báo cáo và các log/suggestion.
     */
    @Override
    public ProgressReportDto generateProgressReport(String userId, String reportType) {
        // 1. Tìm User
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime endDate = LocalDateTime.now();
        // 2. Xác định phạm vi ngày
        LocalDateTime startDate = reportType.equalsIgnoreCase("weekly") ? endDate.minusWeeks(1) : endDate.minusMonths(1);

        // 3. Truy vấn FoodLog và ActivityLog
        List<FoodLog> foodLogs = foodLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        List<ActivityLog> activityLogs = activityLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        // 4. Truy vấn Suggestions
        List<Suggestion> suggestions = suggestionRepository.findByUser(user);

        // 5. Tạo và lưu Report Entity (lưu bản tóm tắt)
        Report report = new Report();
        report.setUser(user);
        // Chuyển đổi reportType (string) thành enum EReportType
        report.setReportType(EReportType.valueOf(reportType.toUpperCase() + "_PROGRESS"));
        report.setGeneratedDate(LocalDateTime.now());
        report.setContent("Summary for " + reportType + " progress."); // Nội dung báo cáo đơn giản
        reportRepository.save(report);

        // 6. Chuyển đổi Entities sang DTOs
        List<FoodLogDto> foodLogDtos = foodLogs.stream().map(log -> modelMapper.map(log, FoodLogDto.class)).collect(Collectors.toList());
        List<ActivityLogDto> activityLogDtos = activityLogs.stream().map(log -> modelMapper.map(log, ActivityLogDto.class)).collect(Collectors.toList());
        List<SuggestionDto> suggestionDtos = suggestions.stream().map(sug -> modelMapper.map(sug, SuggestionDto.class)).collect(Collectors.toList());

        // 7. Tạo và trả về ProgressReportDto
        return new ProgressReportDto(report.getId(), report.getReportType(), report.getGeneratedDate(), report.getContent(), foodLogDtos, activityLogDtos, suggestionDtos);
    }
}