# Hệ thống Tư vấn Dinh dưỡng AI (Frontend)

## **I. Giai đoạn Phân tích và Thiết kế (Analysis & Design)**

- [x] **Thiết kế Biểu đồ Use Case (Use Case Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thực thể (Entity Class Diagram - Analysis & Design)**
- [x] **Thiết kế Biểu đồ Lớp CSDL (Database Entity Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Phân tích (Analysis Class Diagram)**
- [x] **Thiết kế Biểu đồ Lớp Thiết kế (Design Class Diagram)**
- [x] **Thiết kế Giao diện Tĩnh (Static Interface Design)**
- [x] **Thiết kế Động (Dynamic Design - Scenarios, Activity & Sequence Diagrams)**
- [x] **Thiết kế Biểu đồ Triển khai (Deployment Diagram)**

*Ghi chú: Toàn bộ các hạng mục trong giai đoạn I đã hoàn thành và tài liệu được lưu trữ trong thư mục `FrontendRequirements`, `FrontendAnalysis` và `FrontendDesign`.*

## **II. Giai đoạn Khởi tạo và Cài đặt (Setup & Initialization)**

- [x] **Khởi tạo dự án ReactJS**: Sử dụng Vite hoặc Create React App để tạo cấu trúc dự án.
- [x] **Cài đặt thư viện Routing**: Cài đặt React Router DOM.
- [x] **Cài đặt thư viện Styling**: Cài đặt và cấu hình Tailwind CSS hoặc Material UI/Bootstrap.
- [x] **Thiết lập Cấu trúc Thư mục Frontend**: Tạo các thư mục `src/components`, `src/pages`, `src/services`, `src/context` (hoặc `src/store`).
- [x] **Cài đặt thư viện Quản lý API**: Cài đặt Axios và thiết lập cấu hình cơ bản (Base URL cho Backend và AI Service).
- [x] **Thiết lập cấu hình Axios instance**: Tạo tệp `src/services/api.ts` (hoặc `axios.ts`) để cấu hình Axios instance với `baseURL` cho Backend và AI Service.
- [x] **Thiết lập Quản lý State**: Sử dụng React Context API cho giai đoạn khởi tạo và cài đặt. (Sẽ tích hợp Redux Toolkit cho quản lý trạng thái phức tạp hơn sau này)

## **III. Giai đoạn Phát triển (Implementation)**

### A. Module Xác thực

- [x] **Tạo Component `LoginPage.js` và `RegisterPage.js`**: Dựa trên các tệp HTML tĩnh đã thiết kế.
- [x] **Triển khai Service Xác thực (`authService.js`)**:
  - [x] Hàm `login(username, password)`: Gọi API Backend (`POST /api/auth/login`), lưu Token JWT vào Local Storage và Redux State.
  - [x] Hàm `register(userData)`: Gọi API Backend (`POST /api/auth/register`).
- [x] **Triển khai Auth Guard/Private Routes**: Sử dụng React Router DOM để bảo vệ các route cần đăng nhập.

### B. Module Tư vấn Dinh dưỡng

- [x] **Tạo Component `NutritionalConsultation.tsx`**: Giao diện để nhập thông tin nhu cầu dinh dưỡng, hiển thị khuyến nghị và tạo kế hoạch dinh dưỡng cơ bản.
- [x] **Triển khai Service AI (`aiService.js`)**:
  - [x] Hàm `getAdvice(prompt, history)`: Gọi API AI Service (`POST http://localhost:5000/api/nlp/consult`).
  - [x] Xử lý phản hồi từ AI.
  - [x] Hàm `editMealPlan(currentPlan, editRequest)`: Gọi API AI Service để chỉnh sửa thực đơn.

### C. Module Lập Kế hoạch Dinh dưỡng

- [x] **Tạo Component `NutritionPlanList.tsx`**: Hiển thị danh sách các kế hoạch dinh dưỡng.
- [x] **Tạo Component `NutritionPlanForm.tsx`**: Cho phép người dùng tạo/chỉnh sửa kế hoạch.
- [x] **Tạo Component `NutritionPlanDetail.tsx`**: Hiển thị chi tiết một kế hoạch dinh dưỡng.
- [x] **Triển khai Service Kế hoạch (localStorage)**: Tạm thời sử dụng localStorage để lưu trữ và quản lý kế hoạch dinh dưỡng.

### D. Module Theo dõi và Đánh giá

- [x] **Tạo Component `DailyLogInputPage.tsx`**: Cho phép người dùng nhập/tải ảnh bữa ăn và hoạt động.
- [x] **Tạo Component `ProgressReportPage.tsx`**: Hiển thị biểu đồ và báo cáo.
- [x] **Thử nghiệm gọi API Vision Service (`aiService.js`)**:
  - [x] Hàm `identifyFood(imageFile)`: Gọi API AI Service (`POST http://localhost:5000/api/vision/recognize`).
  - [x] Sử dụng FormData để gửi tệp hình ảnh và xử lý phản hồi.

### E. Module Chia sẻ

- [x] **Tạo Component `SharePlanActivityPage.tsx`**: Giao diện để người dùng chia sẻ kế hoạch/hoạt động dinh dưỡng.
- [x] **Tạo Component `ViewSharedContentPage.tsx`**: Giao diện để người dùng xem, thích, bình luận và lưu nội dung chia sẻ.
- [x] **Triển khai Service Chia sẻ (`sharingService.js`)**: Gọi API Backend để quản lý các nội dung chia sẻ.

### F. Module Quản lý Hồ sơ Cá nhân

- [x] **Tạo Component `ProfileManagementPage.tsx`**: Cho phép người dùng cập nhật thông tin cá nhân và mục tiêu.
- [x] **Triển khai Service Người dùng (`userService.js`)**: Gọi API Backend để cập nhật/lấy thông tin người dùng. (Hiện tại sử dụng dữ liệu giả định và xử lý trong component).

### G. Module Quản trị viên (Admin)

- [x] **Đọc và phân tích các tài liệu liên quan đến module quản trị viên**
- [x] **Tạo cấu trúc thư mục cho module quản trị viên trong `ImplementationFrontend/src/admin`**
- [x] **Thiết lập Router cho module quản trị viên**
- [x] **Phát triển `AdminDashboardPage.tsx`**
- [x] **Phát triển `UserManagementPage.tsx`**
- [x] **Phát triển `SharedContentManagementPage.tsx`**
- [x] **Tạo các components chung (hiện tại đã tích hợp vào các trang)**
- [x] **Tạo services tương tác API (sử dụng mock data)**
- [x] **Định nghĩa các kiểu dữ liệu TypeScript (`User` và `SharedContent`)**

## **IV. Giai đoạn Bảo mật (Security)**

- [ ] **Cấu hình phân quyền (Authorization) frontend**: Đảm bảo các route/component chỉ hiển thị/truy cập được bởi người dùng có vai trò phù hợp (USER, ADMIN).

## **V. Giai đoạn Kiểm thử (Testing)**

- [ ] **Unit Tests**: Viết Unit Test cho các Component và Service.
- [ ] **Integration Tests**: Viết Integration Test cho các luồng nghiệp vụ chính và tích hợp API.

## **VI. Giai đoạn Hoàn thiện (Finalization)**

- [ ] **Tối ưu hóa hiệu suất**: Tối ưu hóa các React Component và việc gọi API.
- [ ] **Cập nhật FRONTEND_README.md**: Bổ sung hướng dẫn cài đặt, chạy dự án và mô tả API sau khi triển khai.
- [ ] **Dọn dẹp mã nguồn (Code Cleanup)**: Refactor và tối ưu hóa code.
