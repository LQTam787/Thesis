# **I. Giai đoạn Phân tích và Thiết kế (Analysis & Design)**

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
- [x] **Thiết lập Module/Interface AI**: Chuẩn bị giao diện để tích hợp với dịch vụ AI/NLP (hiện tại là mock service).

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

### **V. Chức năng Quản trị viên (Admin Functions)**

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

### **VI. Giai đoạn Phát triển Module AI (AI Module Development)**

- [x] **Thiết kế API cho AI Service**: Định nghĩa các endpoint REST/gRPC cho việc tương tác với các mô hình AI/ML.
- [x] **Triển khai Mô hình NLP**: Phát triển và tích hợp mô hình xử lý ngôn ngữ tự nhiên để phân tích yêu cầu người dùng.
- [x] **Triển khai Mô hình Vision**: Phát triển và tích hợp mô hình thị giác máy tính để nhận dạng thực phẩm từ hình ảnh.
- [x] **Triển khai Mô hình Recommendation**: Phát triển và tích hợp mô hình gợi ý để tạo thực đơn và lời khuyên dinh dưỡng.
- [x] **Tích hợp Backend Java với AI Service**: Gọi các API của AI Service từ Backend Spring Boot.
- [x] **Cấu hình Môi trường AI/ML**: Cập nhật `requirements.txt` và thiết lập môi trường chạy cho các mô hình AI.
- [x] **Tạo và quản lý Dataset cho AI**: Xây dựng các script để thu thập, tiền xử lý và quản lý dữ liệu cho việc huấn luyện và đánh giá mô hình AI.

### **VII. Giai đoạn Kiểm thử (Testing)**

- [x] **Unit Tests**:
    - [x] Viết Unit Test cho tầng `Service`.
    - [x] Viết Unit Test cho tầng `Controller`.
    - [x] Viết Unit Test cho các thành phần của module AI (đã viết lại bằng Pytest).
- [x] **Integration Tests**:
    - [x] Soạn thảo Kế hoạch Kiểm thử Tích hợp (Integration Test Plan).
    - [x] Viết Integration Test cho luồng xác thực (Authentication Flow).
    - [x] Viết Integration Test cho các luồng nghiệp vụ chính (VD: tạo và quản lý kế hoạch dinh dưỡng).
    - [x] Viết Integration Test cho các luồng tương tác với module AI trong giai đoạn kiểm thử một hệ thống ứng dụng ứng dụng dinh dưỡng có tích hợp AI.

### **VIII. Giai đoạn Hoàn thiện (Finalization)**

- [x] **Tài liệu hóa API**: Sử dụng Swagger/OpenAPI để tạo tài liệu cho các endpoint, bao gồm cả các API của AI Service.
- [x] **Cập nhật README.md**: Bổ sung hướng dẫn cài đặt, chạy dự án và mô tả API, đặc biệt là hướng dẫn cài đặt và chạy module AI.
- [x] **Dọn dẹp mã nguồn (Code Cleanup)**: Refactor và tối ưu hóa code.
    - [x] **Backend**: Tối ưu hóa `NutritionPlanService` bằng cách thay thế phương thức lọc dữ liệu trong bộ nhớ bằng truy vấn trực tiếp tới CSDL (`findByUser`), giúp cải thiện hiệu suất.
    - [x] **AI Service**: Tích hợp `Pydantic` để xác thực dữ liệu đầu vào cho các API endpoint, giúp mã nguồn an toàn, dễ đọc và dễ bảo trì hơn.

### **IX. Giai đoạn Phát triển Frontend (Frontend Development - ReactJS)**

- [ ] **Khởi tạo Dự án Frontend**: Thiết lập dự án ReactJS với TypeScript, cấu trúc thư mục và quản lý dependencies (npm/yarn).
- [ ] **Thiết kế Giao diện Người dùng (UI/UX)**:
    - [ ] Phân tích các yêu cầu UI/UX từ `BusinessModel.md` và `PLANNING.md`.
    - [ ] Lên kế hoạch cấu trúc các component React (vd: `Auth`, `UserProfile`, `NutritionPlan`, `Chatbox`, `Tracking`, `Admin`).
    - [ ] Lựa chọn và tích hợp thư viện UI component (vd: Material-UI, Ant Design).
- [ ] **Phát triển Module Xác thực (Authentication Module)**:
    - [ ] Xây dựng các component `Login`, `Register`.
    - [ ] Tích hợp với `AuthController` của Backend để xử lý đăng nhập/đăng ký (JWT).
    - [ ] Quản lý trạng thái xác thực và phân quyền người dùng trong Frontend.
- [ ] **Phát triển Module Quản lý Hồ sơ Người dùng (User Profile Management)**:
    - [ ] Xây dựng các component để hiển thị và cập nhật thông tin cá nhân.
    - [ ] Tương tác với `UserController` để lấy và cập nhật dữ liệu người dùng.
- [ ] **Phát triển Module Tư vấn Dinh dưỡng (Nutrition Consultation Module)**:
    - [ ] Xây dựng giao diện `Chatbox` để người dùng tương tác với AI.
    - [ ] Tích hợp với API của AI Service (qua Backend) để gửi câu hỏi và nhận gợi ý.
    - [ ] Hiển thị thực đơn, công thức và lý do đề xuất.
- [ ] **Phát triển Module Lập Kế hoạch Dinh dưỡng (Nutrition Plan Management)**:
    - [ ] Xây dựng các component để tạo, chỉnh sửa và hiển thị kế hoạch dinh dưỡng.
    - [ ] Tương tác với `NutritionPlanController` để quản lý kế hoạch.
- [ ] **Phát triển Module Theo dõi và Đánh giá (Tracking & Evaluation Module)**:
    - [ ] Xây dựng giao diện để người dùng ghi nhật ký ăn uống và hoạt động thể chất.
    - [ ] Tích hợp chức năng tải ảnh (Vision AI).
    - [ ] Hiển thị biểu đồ tiến độ và báo cáo dinh dưỡng.
    - [ ] Tương tác với `TrackingController`.
- [ ] **Phát triển Module Chia sẻ Cộng đồng (Community Sharing Module)**:
    - [ ] Xây dựng các component để hiển thị nội dung chia sẻ (công thức, kế hoạch).
    - [ ] Tích hợp các chức năng thích, bình luận, lưu.
    - [ ] Tương tác với `ShareController`.
- [ ] **Phát triển Module Quản trị (Admin Module)**:
    - [ ] Xây dựng các component quản lý người dùng, công thức, kế hoạch, mục tiêu.
    - [ ] Tương tác với `AdminController`.

### **X. Giai đoạn Tích hợp Hệ thống (System Integration)**

- [ ] **Tích hợp Backend và Frontend**: Kết nối hoàn chỉnh API của Spring Boot Backend với ứng dụng ReactJS Frontend.
- [ ] **Tích hợp Frontend với AI Service**: Đảm bảo luồng dữ liệu thông suốt từ Frontend -> Backend -> AI Service và ngược lại.
- [ ] **Kiểm thử Tích hợp End-to-End**: Thực hiện kiểm thử toàn bộ các luồng nghiệp vụ trên hệ thống đã tích hợp.

### **XI. Giai đoạn Triển khai (Deployment)**

- [ ] **Chuẩn bị Môi trường Triển khai**: Cấu hình server, container (Docker) cho Backend, Frontend và AI Service.
- [ ] **Triển khai Hệ thống**: Đưa ứng dụng lên môi trường production.