/**
 * Định nghĩa không gian tên (namespace) cho class này.
 * Giúp tổ chức code và tránh xung đột tên trong dự án lớn.
 */
package com.nutrition.ai.nutritionaibackend;

// Import class SpringApplication, class này chứa phương thức tĩnh để khởi chạy ứng dụng Spring Boot.
import org.springframework.boot.SpringApplication;
// Import annotation @SpringBootApplication.
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Đây là class chính (main class) của ứng dụng Spring Boot.
 * Class này đóng vai trò là điểm vào (entry point) của toàn bộ ứng dụng backend,
 * chịu trách nhiệm khởi tạo và cấu hình môi trường cần thiết cho các module khác
 * (như controllers, services, repositories) hoạt động. Nó là nền tảng để các chức năng
 * backend như quản lý dữ liệu người dùng, logic nghiệp vụ và tích hợp AI được thực thi.
 *
 * Annotation @SpringBootApplication là sự kết hợp của:
 * 1. @Configuration: Đánh dấu class này là nguồn định nghĩa bean (đối tượng Spring).
 * 2. @EnableAutoConfiguration: Kích hoạt cơ chế tự động cấu hình của Spring Boot (ví dụ: tự cấu hình Tomcat nếu là ứng dụng web).
 * 3. @ComponentScan: Quét tìm các components (như @Controller, @Service, @Repository) trong package hiện tại
 * (com.nutrition.ai.nutritionaibackend) và các package con để đăng ký chúng dưới dạng beans.
 */
@SpringBootApplication
public class NutritionAiBackendApplication {

	/**
	 * Phương thức main là điểm vào (entry point) tiêu chuẩn của ứng dụng Java.
	 * Khi chạy file .jar, JVM sẽ gọi phương thức này.
	 *
	 * @param args Các đối số dòng lệnh truyền vào khi khởi chạy.
	 */
	public static void main(String[] args) {
		/**
		 * Dòng lệnh cốt lõi khởi động toàn bộ ứng dụng Spring Boot.
		 * Nó thực hiện các công việc sau:
		 * 1. Tạo và refresh Application Context (nơi quản lý tất cả beans).
		 * 2. Thực hiện Tự động cấu hình (Auto-Configuration).
		 * 3. Khởi động Web Server nhúng (Embedded Web Server) nếu cần.
		 */
		SpringApplication.run(NutritionAiBackendApplication.class, args);
	}

}