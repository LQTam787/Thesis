package com.nutrition.ai.nutritionaibackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class kiểm thử cho ứng dụng Spring Boot chính.
 *
 * Annotation @SpringBootTest sẽ tải toàn bộ ngữ cảnh ứng dụng Spring Boot,
 * cho phép kiểm tra xem ứng dụng có khởi tạo thành công hay không.
 */
@SpringBootTest
class NutritionAiBackendApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Phương thức kiểm thử đơn giản để xác nhận rằng ngữ cảnh ứng dụng đã được tải thành công.
     * Nếu không có ngoại lệ nào được ném ra, điều đó có nghĩa là ngữ cảnh đã tải đúng cách.
     */
    @Test
    void contextLoads() {
        // Phương thức này chỉ cần tồn tại và không ném ra lỗi để kiểm tra việc tải ngữ cảnh thành công.
        // Mặc dù không có assert rõ ràng ở đây, @SpringBootTest sẽ tự động kiểm tra việc tải ngữ cảnh.
    }

    /**
     * Phương thức kiểm thử để xác nhận rằng ApplicationContext không null sau khi khởi tạo.
     * Điều này cung cấp một kiểm tra tường minh hơn về việc tải ngữ cảnh thành công.
     */
    @Test
    void applicationContextShouldLoad() {
        assertThat(applicationContext).isNotNull();
    }
}
