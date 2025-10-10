### **I. Giai đoạn Phân tích và Thiết kế (Analysis & Design)**

- [x] **Thiết kế Biểu đồ Use Case (Use Case Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thực thể (Entity Class Diagram - Analysis & Design)**
- [x] **Thiết kế Biểu đồ Lớp CSDL (Database Entity Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Phân tích (Analysis Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thiết kế (Design Class Diagram)**
- [x] **Thiết kế Giao diện Tĩnh (Static Interface Design)**
- [x] **Thiết kế Động (Dynamic Design - Scenarios, Activity & Sequence Diagrams)**
- [x] **Thiết kế Biểu đồ Triển khai (Deployment Diagram)**

*Ghi chú: Toàn bộ các hạng mục trong giai đoạn I đã hoàn thành và tài liệu được lưu trữ trong thư mục `Analysis` và `Design`.*

### **II. Giai đoạn Khởi tạo và Cài đặt (Setup & Initialization)**

- [x] **Khởi tạo dự án Backend**: Thiết lập dự án Spring Boot với Maven, cấu trúc thư mục và dependencies.
- [x] **Cấu hình Cơ sở dữ liệu**: Cấu hình kết nối MySQL và khởi tạo schema.
- [ ] **Thiết lập Module/Interface AI**: Chuẩn bị giao diện để tích hợp với dịch vụ AI/NLP (hiện tại là mock service).

### **III. Giai đoạn Phát triển (Implementation)**

- [x] **Xây dựng Model (Entities)**: Triển khai các lớp entity Java.
- [x] **Xây dựng Repository (JPA)**: Triển khai các interface repository.
- [x] **Xây dựng DTO (Data Transfer Objects)**: Tạo các lớp DTO để truyền dữ liệu.
- [x] **Phát triển tầng Service**:
    - [x] `UserService`: Quản lý người dùng.
    - [x] `FoodService`: Quản lý thực phẩm.
    - [x] `NutritionPlanService`: Quản lý kế hoạch dinh dưỡng.
    - [x] `SuggestionService`: Cung cấp gợi ý (một phần của module AI).
    - [x] `ShareService`: Quản lý chức năng chia sẻ nội dung.
- [x] **Phát triển tầng Controller (API Endpoints)**:
    - [x] `AuthController`: Xử lý xác thực, đăng ký, đăng nhập.
    - [x] `UserController`: Quản lý thông tin người dùng.
    - [x] `FoodController`: Quản lý dữ liệu thực phẩm.
    - [x] `NutritionPlanController`: Quản lý kế hoạch dinh dưỡng của người dùng.
    - [x] `TrackingController`: Quản lý việc theo dõi tiến độ, báo cáo.
    - [x] `ShareController`: Quản lý chức năng chia sẻ, thích và bình luận.

### **IV. Giai đoạn Bảo mật (Security)**

- [x] **Tích hợp Spring Security**: Cấu hình cơ bản cho JWT (JSON Web Token).
- [x] **Phân quyền (Authorization)**: Triển khai phân quyền chi tiết cho các vai trò (USER, ADMIN).
- [x] **Hoàn thiện xử lý ngoại lệ bảo mật**: Tùy chỉnh các response cho lỗi 401 (Unauthorized) và 403 (Forbidden).

### **VI. Chức năng Quản trị viên (Admin Functions)**

- [x] **Xây dựng cơ sở hạ tầng cho Admin**:
    - [x] Tạo `AdminController`, `AdminService`, và `AdminServiceImpl`.
    - [x] Cấu hình Spring Security với JWT và `@EnableMethodSecurity`.
    - [x] Thêm các lớp tiện ích và exception cần thiết (`JwtUtils`, `AuthEntryPointJwt`, `ResourceNotFoundException`).
- [x] **Quản lý Người dùng (User Management)**:
    - [x] API lấy danh sách tất cả người dùng.
    - [x] API lấy thông tin chi tiết một người dùng.
    - [x] API cập nhật thông tin người dùng (bao gồm cả vai trò).
    - [x] API xóa người dùng.
- [x] **Quản lý Công thức (Recipe Management)**:
    - [x] API tạo công thức mới (bao gồm cả `FoodItem`).
    - [x] API lấy danh sách tất cả công thức.
    - [x] API lấy thông tin chi tiết một công thức.
    - [x] API cập nhật công thức.
    - [x] API xóa công thức.
- [x] **Quản lý Kế hoạch (Plan Management)**:
    - [x] API tạo kế hoạch mới.
    - [x] API lấy danh sách tất cả kế hoạch.
    - [x] API lấy thông tin chi tiết một kế hoạch.
    - [x] API cập nhật kế hoạch.
    - [x] API xóa kế hoạch.
- [x] **Quản lý Mục tiêu (Goal Management)**:
    - [x] API tạo mục tiêu mới.
    - [x] API lấy danh sách tất cả mục tiêu.
    - [x] API lấy thông tin chi tiết một mục tiêu.
    - [x] API cập nhật mục tiêu.
    - [x] API xóa mục tiêu.

### **V. Giai đoạn Kiểm thử (Testing)**

- [ ] **Unit Tests**:
    - [ ] Viết Unit Test cho tầng `Service`.
    - [ ] Viết Unit Test cho tầng `Controller`.
- [ ] **Integration Tests**:
    - [ ] Viết Integration Test cho luồng xác thực (Authentication Flow).
    - [ ] Viết Integration Test cho các luồng nghiệp vụ chính (VD: tạo và quản lý kế hoạch dinh dưỡng).

### **VI. Giai đoạn Hoàn thiện (Finalization)**

- [ ] **Tài liệu hóa API**: Sử dụng Swagger/OpenAPI để tạo tài liệu cho các endpoint.
- [ ] **Cập nhật README.md**: Bổ sung hướng dẫn cài đặt, chạy dự án và mô tả API.
- [ ] **Dọn dẹp mã nguồn (Code Cleanup)**: Refactor và tối ưu hóa code.