# Danh sách Công việc Ban đầu cho Frontend (ReactJS)

## Mục tiêu Giai đoạn 1: Thiết lập Cấu trúc và Tích hợp Xác thực

### A. Thiết lập Dự án và Cấu trúc Cơ bản
1.  Khởi tạo dự án ReactJS:
    * Sử dụng Vite hoặc Create React App để tạo cấu trúc dự án.
    * Cài đặt thư viện Routing (React Router DOM).
    * Cài đặt thư viện Styling (ví dụ: Tailwind CSS) và cấu hình tệp CSS/config.
2.  Thiết lập Cấu trúc Thư mục Frontend:
    * Tạo các thư mục: `src/components`, `src/pages`, `src/services` (chứa logic gọi API), `src/context` (hoặc `src/store` cho Redux).
3.  Cài đặt thư viện Quản lý API:
    * Cài đặt **Axios** và thiết lập cấu hình cơ bản, bao gồm định nghĩa Base URL cho Backend Spring Boot (`http://localhost:8080`) và AI Service (`http://localhost:5000`).
4.  Thiết lập Quản lý State:
    * Cấu hình **Redux Toolkit** để quản lý trạng thái Đăng nhập (User, Token JWT).

### B. Thiết kế Module Xác thực
1.  Thiết kế tĩnh giao diện Đăng nhập và Đăng ký. Đã hoàn thành, các tệp được lưu tại `./FrontendDesign/Static/Html/LoginPage.html` và `./FrontendDesign/Static/Html/RegisterPage.html`.
2.  Thiết kế biểu đồ lớp thiết kế cho module Xác thực, bao gồm các lớp UI, Service và DAO. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Static/Uml/xac_thuc_design_class_diagram.puml`.

### C. Triển khai Module Xác thực (Tích hợp Backend)
1.  Tạo Component **`LoginPage.js`** và **`RegisterPage.js`** (Dựa trên `LoginPage.html` và `RegisterPage.html` tĩnh).
2.  Triển khai **Service Xác thực** (`authService.js`):
    * Hàm **`login(username, password)`**: Gọi API Backend Spring Boot (ví dụ: `POST /api/auth/login`). Lưu trữ **Token JWT** nhận được vào Local Storage và Redux State.
    * Hàm **`register(userData)`**: Gọi API Backend Spring Boot (ví dụ: `POST /api/auth/register`).
3. Triển khai **Auth Guard/Private Routes**: Sử dụng React Router DOM để bảo vệ các route cần đăng nhập (ví dụ: `/profile`, `/advice`).

### D. Phát triển các Module Chức năng Chính (Tích hợp AI/Backend)
1.  **Module Tư vấn chế độ dinh dưỡng:**
    *   Tạo Component **`NutritionAdvice.js`** (Giao diện Chatbox).
    *   Triển khai **Service AI** (`aiService.js`):
        *   Hàm **`getAdvice(prompt, history)`**: Gọi API **AI Service** (ví dụ: `POST http://localhost:5000/api/nlp/consult`).
        *   Gửi **câu lệnh người dùng** (prompt) và **lịch sử trò chuyện** (history) lên AI Service.
        *   Xử lý phản hồi từ AI (có thể là văn bản hoặc một đối tượng thực đơn được đề xuất).
    *   Thiết kế giao diện tĩnh cho các trang: Tư vấn chế độ dinh dưỡng, Chi tiết kế hoạch dinh dưỡng, Tìm kiếm món ăn. Đã hoàn thành, các tệp được lưu tại `./FrontendDesign/Static/Html/NutritionalConsultationPage.html`, `./FrontendDesign/Static/Html/NutritionPlanDetailPage.html`, và `./FrontendDesign/Static/Html/FoodSearchPage.html`.
    *   Thiết kế biểu đồ lớp thiết kế cho module Tư vấn Chế độ Dinh dưỡng, bao gồm các lớp UI, Service, DAO và Entity. Đã hoàn thành, các biểu đồ được lưu tại `./FrontendDesign/Static/Uml/nutritional_consultation_design_class_diagram.puml`, `./FrontendDesign/Static/Uml/nutritional_consultation_dao_class_diagram.puml`, và `./FrontendDesign/Static/Uml/nutritional_consultation_entity_class_diagram.puml`.
    * Phân tích biểu đồ use case module Tư vấn chế độ dinh dưỡng và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/tu_van_che_do_dinh_duong_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Tư vấn chế độ dinh dưỡng và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/tu_van_che_do_dinh_duong_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Tư vấn chế độ dinh dưỡng và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
2.  **Module Lập kế hoạch dinh dưỡng:**
    *   Tạo Component **`MealPlanCreator.js`** để người dùng tạo/chỉnh sửa kế hoạch.
    *   Triển khai **Service Kế hoạch** (`planService.js`): Gọi API Backend để lưu/lấy kế hoạch.
    *   Phân tích biểu đồ use case module Lập kế hoạch dinh dưỡng và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/lap_ke_hoach_dinh_duong_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Lập kế hoạch dinh dưỡng và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/lap_ke_hoach_dinh_duong_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Lập kế hoạch dinh dưỡng và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
3.  **Module Theo dõi và đánh giá:**
    *   Tạo Component **`DailyLogInput.js`** để người dùng nhập/tải ảnh bữa ăn và hoạt động.
    *   Tạo Component **`ProgressReport.js`** để hiển thị biểu đồ và báo cáo.
    *   Thử nghiệm gọi API Vision Service (`aiService.js`):
        *   Hàm **`identifyFood(imageFile)`**: Gọi API **AI Service** (ví dụ: `POST http://localhost:5000/api/vision/recognize`).
        *   Sử dụng **FormData** để gửi tệp hình ảnh.
        *   Xử lý phản hồi để hiển thị **tên món ăn** và **ước tính dinh dưỡng** cho người dùng xác nhận.
    *   Phân tích biểu đồ use case module Theo dõi và đánh giá kế hoạch dinh dưỡng và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/theo_doi_danh_gia_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Theo dõi và đánh giá kế hoạch dinh dưỡng và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/theo_doi_danh_gia_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Theo dõi và đánh giá kế hoạch dinh dưỡng và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
    * Thiết kế giao diện tĩnh cho các trang: Nhập Liệu Dữ Liệu Hàng Ngày và Báo Cáo Tiến Độ và Đánh Giá. Đã hoàn thành, các tệp được lưu tại `./FrontendDesign/Static/Html/DailyLogInputPage.html` và `./FrontendDesign/Static/Html/ProgressReportPage.html`.
    * Thiết kế biểu đồ lớp thiết kế cho module Theo dõi và Đánh giá Kế hoạch Dinh dưỡng. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Static/Uml/theo_doi_danh_gia_design_class_diagram.puml`.
4.  **Module Chia sẻ:**
    *   Tạo Component **`SharingOptions.js`** để quản lý chế độ công khai/chia sẻ.
    *   Triển khai **Service Chia sẻ** (`sharingService.js`): Gọi API Backend để quản lý các nội dung chia sẻ.
    *   Phân tích biểu đồ use case module Chia sẻ kế hoạch và hoạt động dinh dưỡng và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/chia_se_ke_hoach_hoat_dong_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Chia sẻ kế hoạch và hoạt động dinh dưỡng và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/chia_se_ke_hoach_hoat_dong_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Chia sẻ kế hoạch và hoạt động dinh dưỡng và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
    * Thiết kế tĩnh giao diện Đăng nhập và Đăng ký. Đã hoàn thành, các tệp được lưu tại `./FrontendDesign/Static/Html/SharePlanActivityPage.html` và `./FrontendDesign/Static/Html/ViewSharedContentPage.html`.
    * Thiết kế biểu đồ lớp thiết kế cho module Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng, bao gồm các lớp UI, Service và DAO. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Static/Uml/share_design_class_diagram.puml`.
5.  **Module Quản lý hồ sơ cá nhân:**
    *   Tạo Component **`UserProfile.js`** để người dùng cập nhật thông tin cá nhân và mục tiêu.
    *   Triển khai **Service Người dùng** (`userService.js`): Gọi API Backend để cập nhật/lấy thông tin người dùng.
    *   Phân tích biểu đồ use case module Quản lý hồ sơ cá nhân và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/quan_ly_ho_so_ca_nhan_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Quản lý hồ sơ cá nhân và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/quan_ly_ho_so_ca_nhan_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Quản lý hồ sơ cá nhân và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
    * Thiết kế giao diện tĩnh cho trang Quản lý hồ sơ cá nhân. Đã hoàn thành, tệp được lưu tại `./FrontendDesign/Static/Html/ProfileManagementPage.html`.
    * Thiết kế biểu đồ lớp thiết kế cho module Quản lý hồ sơ cá nhân. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Static/Uml/profile_management_design_class_diagram.puml`.
6.  **Module Quản lý (dành cho Quản trị viên):**
    *   Tạo các Component quản lý riêng biệt cho từng loại đối tượng (ví dụ: `UserManagement.js`, `FoodManagement.js`).
    *   Triển khai **Service Quản trị** (`adminService.js`): Gọi API Backend để thực hiện các thao tác thêm/sửa/xóa và kích hoạt huấn luyện lại AI.
    *   Phân tích biểu đồ use case module Quản lý thông tin đối tượng (Admin) và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/quan_ly_thong_tin_admin_analysis_class_diagram.puml`.
    * Phân tích biểu đồ lớp module Quản lý thông tin đối tượng (Admin) và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/quan_ly_thong_tin_admin_analysis_state_diagram.puml`.
    * Phân tích biểu đồ lớp module Quản lý thông tin đối tượng (Admin) và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
    * Thiết kế giao diện tĩnh cho các trang quản lý: Bảng điều khiển quản trị viên, Quản lý người dùng, Quản lý nội dung chia sẻ. Đã hoàn thành, các tệp được lưu tại `./FrontendDesign/Static/Html/AdminDashboardPage.html`, `./FrontendDesign/Static/Html/UserManagementPage.html`, và `./FrontendDesign/Static/Html/SharedContentManagementPage.html`.
    * Thiết kế biểu đồ lớp thiết kế cho module Quản lý thông tin (Admin), bao gồm các lớp UI, Service và DAO. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Static/Uml/admin_design_class_diagram.puml`.
- Phân tích tài liệu mô tả hệ thống bằng ngôn ngữ tự nhiên trong tập tin @Scenarios.md và vẽ biểu đồ lớp thực thể pha phân tích theo các hướng dẫn. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Entity/Uml/entity_class_diagram.puml`.
- Phân tích biểu đồ lớp thực thể pha phân tích và vẽ biểu đồ lớp thực thể pha thiết kế theo các hướng dẫn. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Entity/Uml/entity_class_diagram_design.puml`.
- Phân tích biểu đồ use case module Xác thực và vẽ biểu đồ lớp pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Class/Uml/xac_thuc_analysis_class_diagram.puml`.
- Phân tích biểu đồ lớp module Xác thực và vẽ biểu đồ trạng thái pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/State/Uml/xac_thuc_analysis_state_diagram.puml`.
- Phân tích biểu đồ lớp module Xác thực và vẽ biểu đồ tuần tự pha phân tích. Đã hoàn thành, biểu đồ được lưu tại `./FrontendAnalysis/Sequence/Uml/`.
- Phân tích biểu đồ lớp thực thể pha thiết kế và vẽ biểu đồ cơ sở dữ liệu theo các hướng dẫn. Đã hoàn thành, biểu đồ được lưu tại `./FrontendDesign/Database/Uml/database_schema_diagram.puml`.