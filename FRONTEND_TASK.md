# **I. Giai đoạn Phân tích và Thiết kế (Analysis & Design)**

* [x] **Thiết kế Biểu đồ Use Case (Use Case Diagram)**
* [x] **Thiết kế Biểu đồ Lớp Thực thể (Entity Class Diagram \- Analysis & Design)**
* [x] **Thiết kế Biểu đồ Lớp CSDL (Database Entity Class Diagram)**
* [x] **Thiết kế Biểu đồ Lớp Phân tích (Analysis Class Diagram)**
* [x] **Thiết kế Biểu đồ Lớp Thiết kế (Design Class Diagram)**
* [x] **Thiết kế Giao diện Tĩnh (Static Interface Design)**
* [x] **Thiết kế Động (Dynamic Design \- Scenarios, Activity & Sequence Diagrams)**
* [x] **Thiết kế Biểu đồ Triển khai (Deployment Diagram)**

*Ghi chú: Toàn bộ các hạng mục trong giai đoạn I đã hoàn thành và tài liệu được lưu trữ trong thư mục FrontendRequirements, FrontendAnalysis và FrontendDesign.*

---

### **II. Giai đoạn Khởi tạo và Cài đặt (Setup & Initialization)**

* [x] **Khởi tạo dự án ReactJS**: Sử dụng Vite hoặc Create React App để tạo cấu trúc dự án.
* [x] **Cài đặt thư viện Routing**: Cài đặt React Router DOM.
* [x] **Cài đặt thư viện Styling**: Cài đặt và cấu hình Tailwind CSS hoặc Material UI/Bootstrap.
* [x] **Thiết lập Cấu trúc Thư mục Frontend**: Tạo các thư mục src/components, src/pages, src/services, src/context (hoặc src/store).
* [x] **Cài đặt thư viện Quản lý API**: Cài đặt Axios và thiết lập cấu hình cơ bản (Base URL cho Backend và AI Service).
* [x] **Thiết lập Quản lý State**: Cấu hình Redux Toolkit để quản lý trạng thái Đăng nhập (User, Token JWT).
* [x] **Cài đặt thư viện Biểu đồ**: Cài đặt thư viện như Recharts hoặc Chart.js để hiển thị báo cáo tiến độ.

---

### **III. Giai đoạn Phát triển (Implementation)**

#### **A. Module Xác thực**

* [x] **Tạo Component LoginPage.js và RegisterPage.js**: Dựa trên các tệp HTML tĩnh đã thiết kế.
* [x] **Triển khai Service Xác thực (authService.js)**:
    * [x] Hàm login(username, password): Gọi API Backend (POST /api/auth/login), lưu Token JWT vào Local Storage và Redux State.
    * [x] Hàm register(userData): Gọi API Backend (POST /api/auth/register).
* [x] **Triển khai Auth Guard/Private Routes**: Sử dụng React Router DOM để bảo vệ các route cần đăng nhập.
* [x] **Triển khai chức năng Đăng xuất (logout)**: Xóa Token JWT khỏi Local Storage và Redux State.

#### **B. Module Tư vấn Dinh dưỡng**

* [x] **Tạo Component NutritionAdvice.js**: Giao diện Chatbox.
* [x] **Triển khai Service AI (aiService.js)**:
    * [x] Hàm getAdvice(prompt, history): Gọi API AI Service (POST http://localhost:5000/api/nlp/consult).
    * [x] Xử lý phản hồi từ AI.
* [x] **Hiển thị lịch sử trò chuyện**: Cho phép người dùng xem lại các phiên tư vấn trước đó.
* [x] **Lưu lịch sử trò chuyện**: Triển khai API gọi Backend để lưu/lấy lịch sử trò chuyện của người dùng.

#### **C. Module Lập Kế hoạch Dinh dưỡng**

* [x] **Tạo Component MealPlanCreator.js**: Cho phép người dùng tạo/chỉnh sửa kế hoạch.
* [x] **Triển khai Service Kế hoạch (planService.js)**: Gọi API Backend để lưu/lấy kế hoạch.
* [x] **Tạo Component MealPlanDetail.js**: Hiển thị chi tiết một kế hoạch dinh dưỡng/công thức cụ thể.
* [x] **Triển khai chức năng CRUD (Create, Read, Update, Delete)** cho kế hoạch dinh dưỡng và công thức nấu ăn.
* [x] **Tính toán Calo/Macro trên UI**: Hiển thị tổng calo, protein, carb, fat cho một kế hoạch hoặc bữa ăn.

#### **D. Module Theo dõi và Đánh giá**

* [x] **Tạo Component DailyLogInput.js**: Cho phép người dùng nhập/tải ảnh bữa ăn và hoạt động.
* [x] **Tạo Component ProgressReport.js**: Hiển thị biểu đồ và báo cáo.
* [x] **Thử nghiệm gọi API Vision Service (aiService.js)**:
    * [x] Hàm identifyFood(imageFile): Gọi API AI Service (POST http://localhost:5000/api/vision/recognize).
    * [x] Sử dụng FormData để gửi tệp hình ảnh và xử lý phản hồi.
* [x] **Triển khai Service Ghi nhật ký (logService.js)**: Gọi API Backend để lưu/lấy nhật ký hàng ngày (Log) bao gồm bữa ăn và hoạt động.
* [x] **Hiển thị biểu đồ tiến độ**: Sử dụng thư viện biểu đồ đã cài đặt để hiển thị các chỉ số như cân nặng, calo tiêu thụ, và macro theo thời gian.

#### **E. Module Cộng đồng và Chia sẻ**

* [x] **Tạo Component SharingOptions.js**: Quản lý chế độ công khai/chia sẻ.
* [x] **Triển khai Service Chia sẻ (sharingService.js)**: Gọi API Backend để quản lý các nội dung chia sẻ.
* [x] **Tạo Component SharedContentFeed.js**: Hiển thị danh sách các bài chia sẻ từ cộng đồng (Feed).
* [x] **Tạo Component SharedContentDetail.js**: Cho phép người dùng xem chi tiết nội dung đã chia sẻ.
* [x] **Triển khai chức năng Tương tác**: Gọi API để thực hiện thao tác Thích (Like) và Bình luận (Comment) trên các nội dung chia sẻ.

#### **F. Module Quản lý Hồ sơ Cá nhân**

* [x] **Tạo Component UserProfile.js**: Cho phép người dùng cập nhật thông tin cá nhân và mục tiêu.
* [x] **Triển khai Service Người dùng (userService.js)**: Gọi API Backend để cập nhật/lấy thông tin người dùng.
* [x] **Triển khai chức năng **Cập nhật Đặc điểm cá nhân** (Chiều cao, cân nặng, mức độ hoạt động, bệnh lý, dị ứng).
* [x] **Triển khai chức năng **Cập nhật Mục tiêu Dinh dưỡng** (Giảm cân, tăng cơ, v.v.).

#### **G. Module Quản trị viên (Admin)**

* [x] **Tạo các Component quản lý riêng biệt**: Ví dụ: UserManagement.js, FoodManagement.js, SharedContentManagement.js, AdminDashboardPage.js.
* [x] **Triển khai Service Quản trị (adminService.js)**: Gọi API Backend để thực hiện các thao tác thêm/sửa/xóa và kích hoạt huấn luyện lại AI.
* [x] **Triển khai chức năng Quản lý Người dùng**: Hiển thị danh sách, xem chi tiết, và khóa/mở khóa tài khoản.
* [x] **Triển khai chức năng Quản lý Món ăn/Thực phẩm**: Hỗ trợ CRUD cho dữ liệu **Thực phẩm (Food)**.
* [x] **Thiết kế giao diện kích hoạt Huấn luyện lại AI**: Nút bấm/thao tác gọi API Backend/AI Service để kích hoạt quá trình huấn luyện lại mô hình AI.

---

### **IV. Giai đoạn Bảo mật (Security)**

* [x] **Cấu hình phân quyền (Authorization) frontend**: Đảm bảo các route/component chỉ hiển thị/truy cập được bởi người dùng có vai trò phù hợp (USER, ADMIN).
* [x] **Xử lý lỗi API tập trung**: Xử lý các lỗi 401 (Unauthorized) và 403 (Forbidden) bằng cách chuyển hướng đến trang đăng nhập hoặc hiển thị thông báo.

---

### **V. Giai đoạn Kiểm thử (Testing)**

#### **A. Cài đặt và Cấu hình Môi trường Kiểm thử**
* [x] **Cài đặt Dependencies**: Cài đặt `vitest`, `jsdom`, `@testing-library/react`, `user-event`, và `msw`.
* [x] **Cấu hình Vitest**: Tạo và cấu hình `vitest.config.js` để sử dụng `jsdom`, thiết lập `setupFiles`, và định nghĩa các tùy chọn báo cáo `coverage`.
* [x] **Thiết lập Global Setup**: Tạo file `src/setupTests.js` để import `@testing-library/jest-dom` và khởi động/dọn dẹp mock server `msw`.

#### **B. Thiết lập API Mocking (MSW)**
* [x] **Định nghĩa API Handlers**: Tạo `src/mocks/handlers.js` để giả lập các API endpoint (đăng nhập, lấy dữ liệu, v.v.).
* [x] **Khởi tạo Mock Server**: Tạo `src/mocks/server.js` để thiết lập `msw/node`.

#### **C. Viết Bài kiểm thử (Test Cases)**
* [x] **Unit Tests**:
    * [x] **Components**: Kiểm tra các component độc lập (`RecipeDetail`, `SharePostForm`) render đúng và xử lý sự kiện.
    * [x] **Redux Slices**: Kiểm tra các reducer của `authSlice` cập nhật state chính xác.
    * [x] **Services**: Kiểm tra các hàm service (`authService`, `planService`) gọi đúng API (sử dụng mock) và xử lý dữ liệu trả về.
* [ ] **Integration Tests**:
    * [x] **Luồng Xác thực**: Kiểm tra luồng đăng nhập/đăng xuất hoàn chỉnh, bao gồm cả cập nhật Redux state và điều hướng.
    * [x] **Luồng Phân quyền**: Kiểm tra `PrivateRoute` và `AdminRoute` bảo vệ route và điều hướng chính xác dựa trên vai trò người dùng.
    * [ ] **Luồng Nghiệp vụ chính**: Kiểm tra các trang chính (`DashboardPage`, `DailyLogInputPage`, `NutritionPlanPage`) hoạt động đúng khi tích hợp với API mock.
* [ ] **Kiểm thử khả năng đáp ứng (Responsiveness Testing)**: Đảm bảo giao diện hoạt động tốt trên thiết bị di động (Mobile-First) và máy tính để bàn.

#### **D. Tích hợp vào Quy trình (CI/CD)**
* [ ] **Cập nhật `package.json`**: Thêm các scripts `test`, `test:watch`, và `test:coverage`.
* [ ] **Chạy và xác thực báo cáo Coverage**: Đảm bảo các bài test chạy thành công và tạo ra báo cáo độ bao phủ của mã nguồn.

---

### **VI. Giai đoạn Hoàn thiện (Finalization)**

* [ ] **Tối ưu hóa hiệu suất**: Tối ưu hóa các React Component và việc gọi API.
* [ ] **Cập nhật FRONTEND_README.md**: Bổ sung hướng dẫn cài đặt, chạy dự án và mô tả API sau khi triển khai.
* [ ] **Dọn dẹp mã nguồn (Code Cleanup)**: Refactor và tối ưu hóa code.
* [ ] **Tạo Icon/Favicon cho ứng dụng**.