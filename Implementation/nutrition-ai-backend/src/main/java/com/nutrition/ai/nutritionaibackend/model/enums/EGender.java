package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The EGender enum represents the gender of a user.
 *
 * Nguyên lý hoạt động:
 * Enum này cung cấp một tập hợp các giá trị cố định và hợp lệ cho thuộc tính 
 * giới tính của người dùng. Điều này rất quan trọng cho các tính toán dinh dưỡng 
 * và mục tiêu sức khỏe (ví dụ: tính toán TDEE/BMR) vì các công thức thường phụ 
 * thuộc vào giới tính.
 */
public enum EGender {
    /**
     * Giới tính nam.
     * Vai trò: Được sử dụng để tính toán các yêu cầu dinh dưỡng, mục tiêu và cá nhân hóa.
     */
    MALE,
    /**
     * Giới tính nữ.
     * Vai trò: Được sử dụng để tính toán các yêu cầu dinh dưỡng, mục tiêu và cá nhân hóa.
     */
    FEMALE,
    /**
     * Các giới tính khác.
     * Vai trò: Cung cấp sự bao hàm cho người dùng không xác định là nam hay nữ.
     */
    OTHER
}