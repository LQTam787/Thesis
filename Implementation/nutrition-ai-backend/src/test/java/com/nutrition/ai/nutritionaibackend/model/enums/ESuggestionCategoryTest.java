package com.nutrition.ai.nutritionaibackend.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Lớp kiểm thử đơn vị cho {@link ESuggestionCategory}.
 * Lớp này đảm bảo rằng tất cả các giá trị enum được định nghĩa chính xác
 * và có thể được truy xuất.
 */
class ESuggestionCategoryTest {

    /**
     * Kiểm tra xem tất cả các giá trị enum có thể được truy xuất và không rỗng.
     * Đảm bảo rằng mỗi giá trị enum là duy nhất và có tên phù hợp với định nghĩa.
     */
    @Test
    @DisplayName("Kiểm tra tất cả các giá trị ESuggestionCategory")
    void testAllESuggestionCategories() {
        // Lấy tất cả các giá trị enum
        ESuggestionCategory[] categories = ESuggestionCategory.values();

        // Đảm bảo rằng có đúng số lượng giá trị enum
        assertEquals(2, categories.length, "Số lượng giá trị ESuggestionCategory không khớp.");

        // Kiểm tra từng giá trị enum
        assertNotNull(ESuggestionCategory.DIET_ADJUSTMENT, "ESuggestionCategory.DIET_ADJUSTMENT không được rỗng.");
        assertEquals("DIET_ADJUSTMENT", ESuggestionCategory.DIET_ADJUSTMENT.name(), "Tên enum DIET_ADJUSTMENT không khớp.");

        assertNotNull(ESuggestionCategory.ACTIVITY_INCREASE, "ESuggestionCategory.ACTIVITY_INCREASE không được rỗng.");
        assertEquals("ACTIVITY_INCREASE", ESuggestionCategory.ACTIVITY_INCREASE.name(), "Tên enum ACTIVITY_INCREASE không khớp.");
    }

    /**
     * Kiểm tra chuyển đổi từ chuỗi sang enum bằng phương thức valueOf.
     * Đảm bảo rằng các chuỗi hợp lệ có thể được phân giải thành các giá trị enum tương ứng.
     */
    @Test
    @DisplayName("Kiểm tra chuyển đổi từ chuỗi sang enum")
    void testValueOf() {
        assertEquals(ESuggestionCategory.DIET_ADJUSTMENT, ESuggestionCategory.valueOf("DIET_ADJUSTMENT"), "Chuyển đổi DIET_ADJUSTMENT từ chuỗi không thành công.");
        assertEquals(ESuggestionCategory.ACTIVITY_INCREASE, ESuggestionCategory.valueOf("ACTIVITY_INCREASE"), "Chuyển đổi ACTIVITY_INCREASE từ chuỗi không thành công.");
    }
}
