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

    private static final String RESOURCE_NAME = "User";
    private static final String FIELD_NAME = "id";
    private static final Object FIELD_VALUE = 1L;
    private ResourceNotFoundException exception;

    /**
     * Thiết lập môi trường kiểm thử trước mỗi test.
     * Khởi tạo một đối tượng ResourceNotFoundException.
     */
    @BeforeEach
    void setUp() {
        exception = new ResourceNotFoundException(RESOURCE_NAME, FIELD_NAME, FIELD_VALUE);
    }

    /**
     * Kiểm tra constructor của ResourceNotFoundException.
     * Đảm bảo rằng ngoại lệ được tạo với thông báo lỗi đúng định dạng.
     */
    @Test
    @DisplayName("Test constructor with formatted message")
    void testConstructorWithMessageFormatting() {
        String expectedMessage = String.format("%s not found with %s : '%s'", RESOURCE_NAME, FIELD_NAME, FIELD_VALUE);
        assertEquals(expectedMessage, exception.getMessage(), "Thông báo lỗi phải được định dạng chính xác.");
    }

    /**
     * Kiểm tra phương thức getResourceName().
     * Đảm bảo rằng nó trả về đúng tên tài nguyên đã được truyền vào.
     */
    @Test
    @DisplayName("Test getResourceName()")
    void testGetResourceName() {
        assertEquals(RESOURCE_NAME, exception.getResourceName(), "getResourceName() phải trả về tên tài nguyên chính xác.");
    }

    /**
     * Kiểm tra phương thức getFieldName().
     * Đảm bảo rằng nó trả về đúng tên trường đã được truyền vào.
     */
    @Test
    @DisplayName("Test getFieldName()")
    void testGetFieldName() {
        assertEquals(FIELD_NAME, exception.getFieldName(), "getFieldName() phải trả về tên trường chính xác.");
    }

    /**
     * Kiểm tra phương thức getFieldValue().
     * Đảm bảo rằng nó trả về đúng giá trị trường đã được truyền vào.
     */
    @Test
    @DisplayName("Test getFieldValue()")
    void testGetFieldValue() {
        assertEquals(FIELD_VALUE, exception.getFieldValue(), "getFieldValue() phải trả về giá trị trường chính xác.");
    }

    /**
     * Kiểm tra rằng ResourceNotFoundException là một RuntimeException.
     * Điều này xác nhận rằng nó là một unchecked exception.
     */
    @Test
    @DisplayName("Test exception type")
    void testExceptionType() {
        assertTrue(exception instanceof RuntimeException, "ResourceNotFoundException phải kế thừa từ RuntimeException.");
    }
}
