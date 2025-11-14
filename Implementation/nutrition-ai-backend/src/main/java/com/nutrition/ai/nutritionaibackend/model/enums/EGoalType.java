package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The EGoalType enum defines the types of health and nutrition goals a user can set.
 *
 * Nguyên lý hoạt động:
 * Enum này cung cấp các loại mục tiêu cố định cho người dùng. 
 * Mục tiêu này là thông số đầu vào chính để thuật toán AI tính toán và đưa ra 
 * các khuyến nghị dinh dưỡng và hoạt động phù hợp (ví dụ: đối với WEIGHT_LOSS, 
 * AI sẽ đề xuất thâm hụt calo).
 */
public enum EGoalType {
    /**
     * Mục tiêu giảm cân.
     * Vai trò: Thiết lập trạng thái thâm hụt calo và hướng dẫn chế độ ăn kiêng.
     */
    WEIGHT_LOSS,
    /**
     * Mục tiêu tăng cân.
     * Vai trò: Thiết lập trạng thái dư thừa calo và hướng dẫn chế độ ăn.
     */
    WEIGHT_GAIN,
    /**
     * Mục tiêu tăng cơ.
     * Vai trò: Thiết lập trạng thái dư thừa calo và tăng cường lượng protein.
     */
    MUSCLE_GAIN,
    /**
     * Mục tiêu duy trì cân nặng hiện tại.
     * Vai trò: Thiết lập trạng thái calo duy trì và cân bằng dinh dưỡng.
     */
    MAINTAIN_WEIGHT,
    /**
     * Mục tiêu phục hồi sức khỏe sau ốm.
     * Vai trò: Tập trung vào cung cấp dinh dưỡng đầy đủ và hỗ trợ hệ miễn dịch.
     */
    RECOVERY
}