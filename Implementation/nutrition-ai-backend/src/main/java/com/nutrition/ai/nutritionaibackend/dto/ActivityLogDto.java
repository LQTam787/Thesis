package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ActivityLog entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về một hoạt động thể chất đã được người dùng ghi lại.
 * Luồng hoạt động: Được sử dụng để truyền dữ liệu ghi lại hoạt động giữa tầng Service/Repository và tầng Controller
 * hoặc giữa các microservice.
 */
@Data // Tự động tạo getters, setters, toString, equals, và hashCode. Nguyên lý hoạt động: Giảm boilerplate code.
@NoArgsConstructor // Tự động tạo constructor không tham số. Nguyên lý hoạt động: Cần thiết cho các framework như JPA/Jackson.
@AllArgsConstructor // Tự động tạo constructor với tất cả các trường. Nguyên lý hoạt động: Giúp dễ dàng tạo đối tượng với đầy đủ dữ liệu.
public class ActivityLogDto {
    private Long id; // ID duy nhất của bản ghi ActivityLog.
    private LocalDateTime logDate; // Ngày và thời gian ghi lại hoạt động.
    private Integer durationInMinutes; // Thời lượng hoạt động tính bằng phút.
    private Double caloriesBurned; // Lượng calo đã đốt cháy trong hoạt động đó.
    private Long userId; // ID của người dùng thực hiện hoạt động.
    private Long activityId; // ID của loại hoạt động (ví dụ: Chạy bộ, Yoga).
}