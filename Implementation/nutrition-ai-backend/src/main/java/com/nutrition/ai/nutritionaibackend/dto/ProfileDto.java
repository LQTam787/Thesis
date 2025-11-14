package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Profile entity.
 * Nguyên lý hoạt động: Đóng gói thông tin chi tiết về hồ sơ cá nhân và thể chất của người dùng.
 * Luồng hoạt động: Được sử dụng khi người dùng cập nhật hồ sơ hoặc khi hệ thống cần tính toán mục tiêu/nhu cầu dinh dưỡng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id; // ID duy nhất của hồ sơ. Nguyên lý hoạt động: Dùng để tham chiếu đến hồ sơ cụ thể.
    private String fullName; // Tên đầy đủ của người dùng. Nguyên lý hoạt động: Thông tin định danh cơ bản.
    private LocalDate dateOfBirth; // Ngày sinh. Nguyên lý hoạt động: Dùng để tính toán tuổi, một yếu tố quan trọng trong công thức tính toán nhu cầu calo (BMR).
    private EGender gender; // Giới tính (ví dụ: MALE, FEMALE). Nguyên lý hoạt động: Sử dụng enum để chuẩn hóa. Luồng hoạt động: Yếu tố đầu vào quan trọng cho công thức tính BMR (Basal Metabolic Rate).
    private Double height; // Chiều cao (ví dụ: cm). Nguyên lý hoạt động: Yếu tố đầu vào quan trọng cho công thức tính BMR.
    private Double weight; // Cân nặng hiện tại. Nguyên lý hoạt động: Cân nặng hiện tại của người dùng. Luồng hoạt động: Yếu tố đầu vào quan trọng cho công thức tính BMR và theo dõi tiến độ.
    private String activityLevel; // Mức độ hoạt động (ví dụ: "Ít vận động", "Vận động nhẹ"). Nguyên lý hoạt động: Dùng để tính toán TDEE (Total Daily Energy Expenditure) từ BMR. Luồng hoạt động: Cần thiết để xác định tổng calo tiêu thụ hàng ngày.
    private String allergies; // Thông tin về các dị ứng thực phẩm. Nguyên lý hoạt động: Dữ liệu ràng buộc để loại trừ các thực phẩm không an toàn khỏi kế hoạch dinh dưỡng.
    private String medicalConditions; // Các tình trạng sức khỏe/bệnh lý. Nguyên lý hoạt động: Dữ liệu ràng buộc quan trọng có thể ảnh hưởng đến khuyến nghị dinh dưỡng.
    private Long userId; // ID của người dùng mà hồ sơ này thuộc về. Nguyên lý hoạt động: Liên kết hồ sơ này với một người dùng cụ thể.
}