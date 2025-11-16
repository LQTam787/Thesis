package com.nutrition.ai.nutritionaibackend.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lớp kiểm thử đơn vị cho ResourceNotFoundException.
 * Đảm bảo rằng ngoại lệ hoạt động như mong đợi, bao gồm việc truyền thông báo lỗi
 * và lưu trữ thông tin tài nguyên một cách chính xác.
 */
@DisplayName("ResourceNotFoundException Unit Tests")
class ResourceNotFoundExceptionTest {

    // Tên tài nguyên được sử dụng trong các bài kiểm thử (ví dụ: "User").
    private static final String RESOURCE_NAME = "User";
    // Tên trường được sử dụng trong các bài kiểm thử (ví dụ: "id").
    private static final String FIELD_NAME = "id";
    // Giá trị trường được sử dụng trong các bài kiểm thử (ví dụ: 1L).
    private static final Object FIELD_VALUE = 1L;
    // Đối tượng ResourceNotFoundException sẽ được kiểm thử.
    private ResourceNotFoundException exception;

    /**
     * Thiết lập môi trường kiểm thử trước mỗi test.
     * Phương thức này khởi tạo một đối tượng {@link ResourceNotFoundException}
     * với các giá trị cố định để đảm bảo mỗi bài kiểm thử bắt đầu với một trạng thái sạch.
     */
    @BeforeEach
    void setUp() {
        exception = new ResourceNotFoundException(RESOURCE_NAME, FIELD_NAME, FIELD_VALUE);
    }

    /**
     * Kiểm tra constructor của {@link ResourceNotFoundException}.
     * Bài kiểm thử này xác nhận rằng ngoại lệ được tạo ra với một thông báo lỗi
     * được định dạng chính xác, chứa tên tài nguyên, tên trường và giá trị trường.
     */
    @Test
    @DisplayName("Test constructor with formatted message")
    void testConstructorWithMessageFormatting() {
        // Tạo thông báo dự kiến theo định dạng mà constructor nên tạo ra.
        String expectedMessage = String.format("%s not found with %s : '%s'", RESOURCE_NAME, FIELD_NAME, FIELD_VALUE);
        // So sánh thông báo của ngoại lệ với thông báo dự kiến.
        assertEquals(expectedMessage, exception.getMessage(), "Thông báo lỗi phải được định dạng chính xác.");
    }

    /**
     * Kiểm tra phương thức {@code getResourceName()}.
     * Bài kiểm thử này đảm bảo rằng phương thức trả về chính xác tên tài nguyên
     * đã được cung cấp khi ngoại lệ được khởi tạo.
     */
    @Test
    @DisplayName("Test getResourceName()")
    void testGetResourceName() {
        // Xác nhận rằng tên tài nguyên trả về khớp với tên tài nguyên đã thiết lập.
        assertEquals(RESOURCE_NAME, exception.getResourceName(), "getResourceName() phải trả về tên tài nguyên chính xác.");
    }

    /**
     * Kiểm tra phương thức {@code getFieldName()}.
     * Bài kiểm thử này đảm bảo rằng phương thức trả về chính xác tên trường
     * đã được cung cấp khi ngoại lệ được khởi tạo.
     */
    @Test
    @DisplayName("Test getFieldName()")
    void testGetFieldName() {
        // Xác nhận rằng tên trường trả về khớp với tên trường đã thiết lập.
        assertEquals(FIELD_NAME, exception.getFieldName(), "getFieldName() phải trả về tên trường chính xác.");
    }

    /**
     * Kiểm tra phương thức {@code getFieldValue()}.
     * Bài kiểm thử này đảm bảo rằng phương thức trả về chính xác giá trị trường
     * đã được cung cấp khi ngoại lệ được khởi tạo.
     */
    @Test
    @DisplayName("Test getFieldValue()")
    void testGetFieldValue() {
        // Xác nhận rằng giá trị trường trả về khớp với giá trị trường đã thiết lập.
        assertEquals(FIELD_VALUE, exception.getFieldValue(), "getFieldValue() phải trả về giá trị trường chính xác.");
    }

    /**
     * Kiểm tra rằng {@link ResourceNotFoundException} là một {@link RuntimeException}.
     * Bài kiểm thử này xác nhận rằng ngoại lệ này là một unchecked exception,
     * theo thiết kế để tránh yêu cầu bắt buộc xử lý tại compile-time.
     */
    @Test
    @DisplayName("Test exception type")
    void testExceptionType() {
        // Xác nhận rằng đối tượng ngoại lệ là một thể hiện của RuntimeException.
        assertTrue(exception instanceof RuntimeException, "ResourceNotFoundException phải kế thừa từ RuntimeException.");
    }
}
