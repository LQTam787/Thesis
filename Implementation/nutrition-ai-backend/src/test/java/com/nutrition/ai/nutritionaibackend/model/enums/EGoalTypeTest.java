package com.nutrition.ai.nutritionaibackend.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lớp kiểm thử đơn vị cho {@link EGoalType}.
 * Đảm bảo rằng tất cả các giá trị enum được định nghĩa chính xác và các phương thức cơ bản hoạt động như mong đợi.
 */
public class EGoalTypeTest {

    /**
     * Kiểm tra rằng tất cả các giá trị của enum EGoalType tồn tại.
     * Xác minh rằng phương thức values() trả về tất cả các giá trị được mong đợi.
     */
    @Test
    void testAllEGoalTypeValuesExist() {
        EGoalType[] expectedValues = {EGoalType.WEIGHT_LOSS, EGoalType.WEIGHT_GAIN, EGoalType.MUSCLE_GAIN, EGoalType.MAINTAIN_WEIGHT, EGoalType.RECOVERY};
        EGoalType[] actualValues = EGoalType.values();

        assertNotNull(actualValues, "Giá trị của EGoalType không được null.");
        assertEquals(expectedValues.length, actualValues.length, "Số lượng giá trị enum không khớp.");
        assertArrayEquals(expectedValues, actualValues, "Các giá trị enum không khớp với thứ tự dự kiến.");
    }

    /**
     * Kiểm tra phương thức valueOf() cho từng giá trị enum.
     * Đảm bảo rằng việc chuyển đổi từ chuỗi sang enum hoạt động chính xác.
     */
    @Test
    void testValueOfEGoalType() {
        assertEquals(EGoalType.WEIGHT_LOSS, EGoalType.valueOf("WEIGHT_LOSS"), "valueOf() cho WEIGHT_LOSS không đúng.");
        assertEquals(EGoalType.WEIGHT_GAIN, EGoalType.valueOf("WEIGHT_GAIN"), "valueOf() cho WEIGHT_GAIN không đúng.");
        assertEquals(EGoalType.MUSCLE_GAIN, EGoalType.valueOf("MUSCLE_GAIN"), "valueOf() cho MUSCLE_GAIN không đúng.");
        assertEquals(EGoalType.MAINTAIN_WEIGHT, EGoalType.valueOf("MAINTAIN_WEIGHT"), "valueOf() cho MAINTAIN_WEIGHT không đúng.");
        assertEquals(EGoalType.RECOVERY, EGoalType.valueOf("RECOVERY"), "valueOf() cho RECOVERY không đúng.");
    }

    /**
     * Kiểm tra các trường hợp cạnh của phương thức valueOf() với đầu vào không hợp lệ.
     * Đảm bảo rằng nó xử lý các giá trị không tồn tại một cách thích hợp.
     */
    @Test
    void testValueOfEGoalTypeInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> EGoalType.valueOf("INVALID_GOAL"),
                "valueOf() nên ném IllegalArgumentException cho đầu vào không hợp lệ.");
        assertThrows(IllegalArgumentException.class,
                () -> EGoalType.valueOf("weight_loss"), // Kiểm tra với chữ thường
                "valueOf() nên ném IllegalArgumentException cho đầu vào không khớp chữ hoa chữ thường.");
    }
}
