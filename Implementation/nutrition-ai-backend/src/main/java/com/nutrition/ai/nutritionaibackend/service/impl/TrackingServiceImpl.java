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
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link TrackingService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các hoạt động liên quan đến việc theo dõi tiến độ của người dùng,
 * bao gồm ghi lại nhật ký thức ăn đã tiêu thụ, các hoạt động thể chất đã thực hiện, và tạo các báo cáo tiến độ tổng hợp.
 * Nó tương tác với nhiều Repository khác nhau để truy cập và thao tác dữ liệu liên quan
 * đến nhật ký thức ăn, nhật ký hoạt động, báo cáo và gợi ý dinh dưỡng.
 * Nguyên tắc Tiêm phụ thuộc (Dependency Injection) được sử dụng thông qua constructor (@RequiredArgsConstructor của Lombok).
 */
@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    /**
     * {@code foodLogRepository} là giao diện repository để truy cập dữ liệu của {@link FoodLog}.
     * Được tiêm thông qua constructor để quản lý các nhật ký thức ăn của người dùng.
     */
    private final FoodLogRepository foodLogRepository;
    /**
     * {@code activityLogRepository} là giao diện repository để truy cập dữ liệu của {@link ActivityLog}.
     * Được tiêm thông qua constructor để quản lý các nhật ký hoạt động của người dùng.
     */
    private final ActivityLogRepository activityLogRepository;
    /**
     * {@code reportRepository} là giao diện repository để truy cập dữ liệu của {@link Report}.
     * Được tiêm thông qua constructor để lưu trữ và quản lý các báo cáo tiến độ đã tạo.
     */
    private final ReportRepository reportRepository;
    /**
     * {@code suggestionRepository} là giao diện repository để truy cập dữ liệu của {@link Suggestion}.
     * Được tiêm thông qua constructor để lấy các gợi ý dinh dưỡng liên quan đến người dùng.
     */
    private final SuggestionRepository suggestionRepository;
    /**
     * {@code userRepository} là giao diện repository để truy cập dữ liệu của {@link User}.
     * Được tiêm thông qua constructor để lấy thông tin người dùng liên quan đến các nhật ký và báo cáo.
     */
    private final UserRepository userRepository;
    /**
     * {@code modelMapper} được sử dụng để chuyển đổi giữa các đối tượng Entity (Domain Model)
     * và DTO (Data Transfer Object) một cách tự động và linh hoạt.
     * Được tiêm thông qua constructor.
     */
    private final ModelMapper modelMapper;

    /**
     * Ghi lại một nhật ký thức ăn (Food Log) cho một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm Người dùng:</b> Parse {@code userId} (String) thành Long và truy vấn {@code userRepository}
     *         để tìm đối tượng {@link User}. Nếu không tìm thấy người dùng, ném {@link RuntimeException}.</li>
     *     <li><b>Chuyển đổi DTO sang Entity:</b> Ánh xạ {@link FoodLogDto} từ request sang đối tượng {@link FoodLog} Entity
     *         sử dụng {@code modelMapper}.</li>
     *     <li><b>Thiết lập thuộc tính:</b> Gắn đối tượng {@link User} đã tìm thấy vào {@code FoodLog} Entity
     *         và thiết lập {@code logDate} là thời điểm hiện tại ({@code LocalDateTime.now()}).</li>
     *     <li><b>Lưu FoodLog:</b> Gọi {@code foodLogRepository.save()} để lưu đối tượng {@code FoodLog} vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi và Trả về:</b> Ánh xạ đối tượng {@code FoodLog} đã lưu thành {@link FoodLogDto} và trả về.</li>
     * </ol>
     *
     * @param userId ID của người dùng (dạng String) thực hiện ghi nhật ký thức ăn.
     * @param foodLogDto {@link FoodLogDto} chứa thông tin chi tiết về món ăn đã tiêu thụ.
     * @return {@link FoodLogDto} của nhật ký thức ăn đã được ghi thành công.
     * @throws RuntimeException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Override
    public FoodLogDto logFood(String userId, FoodLogDto foodLogDto) {
        // 1. Tìm User bằng cách parse userId từ String sang Long
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        // 2. Chuyển đổi FoodLogDto sang FoodLog Entity
        FoodLog foodLog = modelMapper.map(foodLogDto, FoodLog.class);
        // 3. Thiết lập User và thời gian ghi nhật ký là thời điểm hiện tại
        foodLog.setUser(user);
        foodLog.setLogDate(LocalDateTime.now());
        // 4. Lưu FoodLog vào cơ sở dữ liệu và 5. Chuyển đổi sang DTO và trả về
        FoodLog savedLog = foodLogRepository.save(foodLog);
        return modelMapper.map(savedLog, FoodLogDto.class);
    }

    /**
     * Ghi lại một nhật ký hoạt động (Activity Log) cho một người dùng cụ thể.
     * Luồng hoạt động tương tự như {@link #logFood(String, FoodLogDto)}.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm Người dùng:</b> Parse {@code userId} (String) thành Long và truy vấn {@code userRepository}
     *         để tìm đối tượng {@link User}. Nếu không tìm thấy người dùng, ném {@link RuntimeException}.</li>
     *     <li><b>Chuyển đổi DTO sang Entity:</b> Ánh xạ {@link ActivityLogDto} từ request sang đối tượng {@link ActivityLog} Entity
     *         sử dụng {@code modelMapper}.</li>
     *     <li><b>Thiết lập thuộc tính:</b> Gắn đối tượng {@link User} đã tìm thấy vào {@code ActivityLog} Entity
     *         và thiết lập {@code logDate} là thời điểm hiện tại ({@code LocalDateTime.now()}).</li>
     *     <li><b>Lưu ActivityLog:</b> Gọi {@code activityLogRepository.save()} để lưu đối tượng {@code ActivityLog} vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi và Trả về:</b> Ánh xạ đối tượng {@code ActivityLog} đã lưu thành {@link ActivityLogDto} và trả về.</li>
     * </ol>
     *
     * @param userId ID của người dùng (dạng String) thực hiện ghi nhật ký hoạt động.
     * @param activityLogDto {@link ActivityLogDto} chứa thông tin chi tiết về hoạt động đã thực hiện.
     * @return {@link ActivityLogDto} của nhật ký hoạt động đã được ghi thành công.
     * @throws RuntimeException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Override
    public ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto) {
        // 1. Tìm User bằng cách parse userId từ String sang Long
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        // 2. Chuyển đổi ActivityLogDto sang ActivityLog Entity
        ActivityLog activityLog = modelMapper.map(activityLogDto, ActivityLog.class);
        // 3. Thiết lập User và thời gian ghi nhật ký là thời điểm hiện tại
        activityLog.setUser(user);
        activityLog.setLogDate(LocalDateTime.now());
        // 4. Lưu ActivityLog vào cơ sở dữ liệu và 5. Chuyển đổi sang DTO và trả về
        ActivityLog savedLog = activityLogRepository.save(activityLog);
        return modelMapper.map(savedLog, ActivityLogDto.class);
    }

    /**
     * Tạo báo cáo tiến độ (Progress Report) cho một người dùng dựa trên các nhật ký thức ăn,
     * hoạt động và các gợi ý dinh dưỡng trong một khoảng thời gian cụ thể (tuần hoặc tháng).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm Người dùng:</b> Parse {@code userId} (String) thành Long và truy vấn {@code userRepository}
     *         để tìm đối tượng {@link User}. Nếu không tìm thấy người dùng, ném {@link RuntimeException}.</li>
     *     <li><b>Xác định Phạm vi Ngày:</b> Dựa trên {@code reportType} ("weekly" hoặc "monthly"), tính toán
     *         {@code startDate} và {@code endDate} để giới hạn dữ liệu trong báo cáo.</li>
     *     <li><b>Truy vấn Dữ liệu Log:</b>
     *         <ul>
     *             <li>Truy vấn {@code foodLogRepository} để lấy tất cả {@link FoodLog} của người dùng trong phạm vi ngày.</li>
     *             <li>Truy vấn {@code activityLogRepository} để lấy tất cả {@link ActivityLog} của người dùng trong phạm vi ngày.</li>
     *         </ul>
     *     </li>
     *     <li><b>Truy vấn Gợi ý:</b> Truy vấn {@code suggestionRepository} để lấy tất cả {@link Suggestion} hiện có cho người dùng.</li>
     *     <li><b>Tạo và Lưu Report Entity:</b>
     *         <ul>
     *             <li>Tạo một đối tượng {@link Report} mới.</li>
     *             <li>Thiết lập {@code user}, {@code reportType} (chuyển từ String sang {@link EReportType} Enum),
     *                 {@code generatedDate} (thời điểm hiện tại), và một {@code content} tóm tắt đơn giản cho báo cáo.</li>
     *             <li>Lưu đối tượng {@link Report} vào cơ sở dữ liệu thông qua {@code reportRepository}.</li>
     *         </ul>
     *     </li>
     *     <li><b>Chuyển đổi Entities sang DTOs:</b> Ánh xạ các danh sách {@link FoodLog}, {@link ActivityLog},
     *         và {@link Suggestion} Entities sang các danh sách DTO tương ứng ({@link FoodLogDto},
     *         {@link ActivityLogDto}, {@link SuggestionDto}) sử dụng {@code modelMapper}.</li>
     *     <li><b>Tạo và Trả về ProgressReportDto:</b> Tạo một đối tượng {@link ProgressReportDto} mới,
     *         tổng hợp thông tin từ {@code Report} đã tạo và các danh sách DTO đã chuyển đổi, sau đó trả về.</li>
     * </ol>
     *
     * @param userId ID của người dùng (dạng String) để tạo báo cáo tiến độ.
     * @param reportType Loại báo cáo muốn tạo ("weekly" cho hàng tuần, "monthly" cho hàng tháng).
     * @return {@link ProgressReportDto} chứa dữ liệu báo cáo tiến độ, bao gồm các nhật ký và gợi ý liên quan.
     * @throws RuntimeException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Override
    public ProgressReportDto generateProgressReport(String userId, String reportType) {
        // 1. Tìm User bằng cách parse userId từ String sang Long
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime endDate = LocalDateTime.now();
        // 2. Xác định phạm vi ngày dựa trên reportType
        LocalDateTime startDate = reportType.equalsIgnoreCase("weekly") ? endDate.minusWeeks(1) : endDate.minusMonths(1);

        // 3. Truy vấn FoodLog và ActivityLog trong phạm vi ngày cho người dùng
        List<FoodLog> foodLogs = foodLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        List<ActivityLog> activityLogs = activityLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        // 4. Truy vấn các Suggestion hiện có cho người dùng
        List<Suggestion> suggestions = suggestionRepository.findByUser(user);

        // 5. Tạo và lưu Report Entity (lưu bản tóm tắt báo cáo)
        Report report = new Report();
        report.setUser(user);
        // Chuyển đổi reportType (String) thành enum EReportType
        report.setReportType(EReportType.valueOf(reportType.toUpperCase() + "_PROGRESS"));
        report.setGeneratedDate(LocalDateTime.now());
        report.setContent("Summary for " + reportType + " progress."); // Nội dung báo cáo đơn giản
        reportRepository.save(report);

        // 6. Chuyển đổi các Entity (FoodLog, ActivityLog, Suggestion) thành DTOs
        List<FoodLogDto> foodLogDtos = foodLogs.stream().map(log -> modelMapper.map(log, FoodLogDto.class)).collect(Collectors.toList());
        List<ActivityLogDto> activityLogDtos = activityLogs.stream().map(log -> modelMapper.map(log, ActivityLogDto.class)).collect(Collectors.toList());
        List<SuggestionDto> suggestionDtos = suggestions.stream().map(sug -> modelMapper.map(sug, SuggestionDto.class)).collect(Collectors.toList());

        // 7. Tạo và trả về ProgressReportDto, bao gồm thông tin báo cáo và các log/suggestion liên quan
        return new ProgressReportDto(report.getId(), report.getReportType(), report.getGeneratedDate(), report.getContent(), foodLogDtos, activityLogDtos, suggestionDtos);
    }
}