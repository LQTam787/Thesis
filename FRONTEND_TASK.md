# **I. Giai đoạn Phân tích và Thiết kế (Analysis & Design)**

- [x] **Thiết kế Biểu đồ Use Case (Use Case Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thực thể (Entity Class Diagram - Analysis & Design)**
- [x] **Thiết kế Biểu đồ Lớp CSDL (Database Entity Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Phân tích (Analysis Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thiết kế (Design Class Diagram)**
- [x] **Thiết kế Giao diện Tĩnh (Static Interface Design)**
- [x] **Thiết kế Động (Dynamic Design - Scenarios, Activity & Sequence Diagrams)**
- [x] **Thiết kế Biểu đồ Triển khai (Deployment Diagram)**

*Ghi chú: Toàn bộ các hạng mục trong giai đoạn I đã hoàn thành và tài liệu được lưu trữ trong thư mục `FrontendRequirements`, `FrontendAnalysis` và `FrontendDesign`.*

### **II. Giai đoạn Khởi tạo và Cài đặt (Setup & Initialization)**

- [ ] **Khởi tạo dự án ReactJS**: Sử dụng Vite hoặc Create React App để tạo cấu trúc dự án.
- [ ] **Cài đặt thư viện Routing**: Cài đặt React Router DOM.
- [ ] **Cài đặt thư viện Styling**: Cài đặt và cấu hình Tailwind CSS hoặc Material UI/Bootstrap.
- [ ] **Thiết lập Cấu trúc Thư mục Frontend**: Tạo các thư mục `src/components`, `src/pages`, `src/services`, `src/context` (hoặc `src/store`).
- [ ] **Cài đặt thư viện Quản lý API**: Cài đặt Axios và thiết lập cấu hình cơ bản (Base URL cho Backend và AI Service).
- [ ] **Thiết lập Quản lý State**: Cấu hình Redux Toolkit để quản lý trạng thái Đăng nhập (User, Token JWT).

### **III. Giai đoạn Phát triển (Implementation)**

#### A. Module Xác thực

- [ ] **Tạo Component `LoginPage.js` và `RegisterPage.js`**: Dựa trên các tệp HTML tĩnh đã thiết kế.
- [ ] **Triển khai Service Xác thực (`authService.js`)**:
    - [ ] Hàm `login(username, password)`: Gọi API Backend (`POST /api/auth/login`), lưu Token JWT vào Local Storage và Redux State.
    - [ ] Hàm `register(userData)`: Gọi API Backend (`POST /api/auth/register`).
- [ ] **Triển khai Auth Guard/Private Routes**: Sử dụng React Router DOM để bảo vệ các route cần đăng nhập.

#### B. Module Tư vấn Dinh dưỡng

- [ ] **Tạo Component `NutritionAdvice.js`**: Giao diện Chatbox.
- [ ] **Triển khai Service AI (`aiService.js`)**:
    - [ ] Hàm `getAdvice(prompt, history)`: Gọi API AI Service (`POST http://localhost:5000/api/nlp/consult`).
    - [ ] Xử lý phản hồi từ AI.

#### C. Module Lập Kế hoạch Dinh dưỡng

- [ ] **Tạo Component `MealPlanCreator.js`**: Cho phép người dùng tạo/chỉnh sửa kế hoạch.
- [ ] **Triển khai Service Kế hoạch (`planService.js`)**: Gọi API Backend để lưu/lấy kế hoạch.

#### D. Module Theo dõi và Đánh giá

- [ ] **Tạo Component `DailyLogInput.js`**: Cho phép người dùng nhập/tải ảnh bữa ăn và hoạt động.
- [ ] **Tạo Component `ProgressReport.js`**: Hiển thị biểu đồ và báo cáo.
- [ ] **Thử nghiệm gọi API Vision Service (`aiService.js`)**:
    - [ ] Hàm `identifyFood(imageFile)`: Gọi API AI Service (`POST http://localhost:5000/api/vision/recognize`).
    - [ ] Sử dụng FormData để gửi tệp hình ảnh và xử lý phản hồi.

#### E. Module Chia sẻ

- [ ] **Tạo Component `SharingOptions.js`**: Quản lý chế độ công khai/chia sẻ.
- [ ] **Triển khai Service Chia sẻ (`sharingService.js`)**: Gọi API Backend để quản lý các nội dung chia sẻ.

#### F. Module Quản lý Hồ sơ Cá nhân

- [ ] **Tạo Component `UserProfile.js`**: Cho phép người dùng cập nhật thông tin cá nhân và mục tiêu.
- [ ] **Triển khai Service Người dùng (`userService.js`)**: Gọi API Backend để cập nhật/lấy thông tin người dùng.

#### G. Module Quản trị viên (Admin)

- [ ] **Tạo các Component quản lý riêng biệt**: Ví dụ: `UserManagement.js`, `FoodManagement.js`, `SharedContentManagement.js`, `AdminDashboardPage.js`.
- [ ] **Triển khai Service Quản trị (`adminService.js`)**: Gọi API Backend để thực hiện các thao tác thêm/sửa/xóa và kích hoạt huấn luyện lại AI.

### **IV. Giai đoạn Bảo mật (Security)**

- [ ] **Cấu hình phân quyền (Authorization) frontend**: Đảm bảo các route/component chỉ hiển thị/truy cập được bởi người dùng có vai trò phù hợp (USER, ADMIN).

### **V. Giai đoạn Kiểm thử (Testing)**

- [ ] **Unit Tests**: Viết Unit Test cho các Component và Service.
- [ ] **Integration Tests**: Viết Integration Test cho các luồng nghiệp vụ chính và tích hợp API.

### **VI. Giai đoạn Hoàn thiện (Finalization)**

- [ ] **Tối ưu hóa hiệu suất**: Tối ưu hóa các React Component và việc gọi API.
- [ ] **Cập nhật FRONTEND_README.md**: Bổ sung hướng dẫn cài đặt, chạy dự án và mô tả API sau khi triển khai.
- [ ] **Dọn dẹp mã nguồn (Code Cleanup)**: Refactor và tối ưu hóa code.