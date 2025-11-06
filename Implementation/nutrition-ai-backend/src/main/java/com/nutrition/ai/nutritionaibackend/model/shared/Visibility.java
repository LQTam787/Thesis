package com.nutrition.ai.nutritionaibackend.model.shared;

/**
 * Enum representing the visibility level of shared content.
 *
 * NGUYÊN LÝ HOẠT ĐỘNG:
 * Định nghĩa tập hợp cố định các phạm vi hiển thị cho một bài chia sẻ.
 * SharedContent sử dụng Enum này để kiểm soát ai có thể xem nội dung.
 */
public enum Visibility {
    PUBLIC,  // Công khai, bất cứ ai cũng xem được.
    FRIENDS  // Chỉ bạn bè của người chia sẻ xem được.
}