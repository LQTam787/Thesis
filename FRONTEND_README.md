# Frontend (ReactJS) Project

## 1. Tổng quan

Dự án này là một ứng dụng ReactJS phục vụ nhu cầu lập kế hoạch, thực hiện và theo dõi chế độ dinh dưỡng một cách hiệu quả, thông minh và được cá nhân hóa.

## 2. Phân tích Thiết kế (UML)

Các biểu đồ Use Case mô tả chức năng của hệ thống từ góc nhìn của người dùng đã được tạo và lưu trữ trong thư mục `FrontendRequirements/Uml/`.
Biểu đồ lớp thực thể pha phân tích đã được tạo và lưu trữ tại `FrontendAnalysis/Entity/Uml/entity_class_diagram.puml`.
Biểu đồ lớp thực thể pha thiết kế đã được tạo và lưu trữ tại `FrontendDesign/Entity/Uml/entity_class_diagram_design.puml`.
Biểu đồ cơ sở dữ liệu đã được tạo và lưu trữ tại `FrontendDesign/Database/Uml/database_schema_diagram.puml`.
Biểu đồ lớp phân tích đã được tạo và lưu trữ tại `FrontendAnalysis/Class/Uml/`.
Biểu đồ trạng thái phân tích đã được tạo và lưu trữ tại `FrontendAnalysis/State/Uml/`.
Biểu đồ tuần tự phân tích đã được tạo và lưu trữ tại `FrontendAnalysis/Sequence/Uml/`.
- **Biểu đồ Use Case Tổng quan:** `usecase_tong_quan.puml`
- **Biểu đồ Use Case Chi tiết - Tư vấn chế độ dinh dưỡng:** `usecase_tu_van_che_do_dinh_duong.puml`
- **Biểu đồ Use Case Chi tiết - Lập kế hoạch dinh dưỡng:** `usecase_lap_ke_hoach_dinh_duong.puml`
- **Biểu đồ Use Case Chi tiết - Theo dõi và đánh giá kế hoạch dinh dưỡng:** `usecase_theo_doi_danh_gia.puml`
- **Biểu đồ Use Case Chi tiết - Quản lý thông tin (Admin):** `usecase_quan_ly_thong_tin_admin.puml`
- **Biểu đồ Use Case Chi tiết - Xác thực (Đăng ký, Đăng nhập, Đăng xuất):** `usecase_xac_thuc.puml`
- **Biểu đồ Use Case Chi tiết - Quản lý hồ sơ cá nhân:** `usecase_quan_ly_ho_so_ca_nhan.puml`
- **Biểu đồ Use Case Chi tiết - Chia sẻ kế hoạch và hoạt động dinh dưỡng:** `usecase_chia_se_ke_hoach_hoat_dong.puml`
- **Biểu đồ lớp phân tích - Xác thực:** `xac_thuc_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Xác thực:** `xac_thuc_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Đăng ký:** `dang_ky_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Đăng nhập:** `dang_nhap_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Đăng xuất:** `dang_xuat_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Tư vấn chế độ dinh dưỡng:** `tu_van_che_do_dinh_duong_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Tư vấn chế độ dinh dưỡng:** `tu_van_che_do_dinh_duong_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Gửi thông tin tư vấn:** `gui_thong_tin_tu_van_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Yêu cầu chỉnh sửa thực đơn:** `yeu_cau_chinh_sua_thuc_don_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Lập kế hoạch dinh dưỡng:** `lap_ke_hoach_dinh_duong_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Lập kế hoạch dinh dưỡng:** `lap_ke_hoach_dinh_duong_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Chọn kiểu kế hoạch:** `chon_kieu_ke_hoach_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Lưu kế hoạch:** `luu_ke_hoach_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Theo dõi và đánh giá kế hoạch dinh dưỡng:** `theo_doi_danh_gia_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Theo dõi và đánh giá kế hoạch dinh dưỡng:** `theo_doi_danh_gia_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Nhập dữ liệu hàng ngày:** `nhap_du_lieu_hang_ngay_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Lấy báo cáo tiến độ:** `lay_bao_cao_tien_do_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Quản lý hồ sơ cá nhân:** `quan_ly_ho_so_ca_nhan_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Quản lý hồ sơ cá nhân:** `quan_ly_ho_so_ca_nhan_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Xem hồ sơ:** `xem_ho_so_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Cập nhật sinh trắc học:** `cap_nhat_sinh_trac_hoc_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Cập nhật mục tiêu sức khỏe:** `cap_nhat_muc_tieu_suc_khoe_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Cập nhật sở thích ăn uống:** `cap_nhat_so_thich_an_uong_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Chia sẻ kế hoạch và hoạt động dinh dưỡng:** `chia_se_ke_hoach_hoat_dong_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Chia sẻ kế hoạch và hoạt động dinh dưỡng:** `chia_se_ke_hoach_hoat_dong_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Thay đổi chế độ chia sẻ:** `thay_doi_che_do_chia_se_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Xem nội dung chia sẻ:** `xem_noi_dung_chia_se_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Thích nội dung chia sẻ:** `thich_noi_dung_chia_se_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Thêm bình luận:** `them_binh_luan_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Lưu nội dung chia sẻ:** `luu_noi_dung_chia_se_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Đề xuất người dùng/nhóm:** `de_xuat_nguoi_dung_nhom_sequence_diagram.puml`
- **Biểu đồ lớp phân tích - Quản lý thông tin đối tượng (Admin):** `quan_ly_thong_tin_admin_analysis_class_diagram.puml`
- **Biểu đồ trạng thái phân tích - Quản lý thông tin đối tượng (Admin):** `quan_ly_thong_tin_admin_analysis_state_diagram.puml`
- **Biểu đồ tuần tự phân tích - Quản lý người dùng:** `quan_ly_nguoi_dung_them_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Quản lý món ăn:** `quan_ly_mon_an_them_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Kích hoạt huấn luyện lại AI:** `kich_hoat_huan_luyen_lai_ai_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Sửa đối tượng:** `sua_doi_tuong_sequence_diagram.puml`
- **Biểu đồ tuần tự phân tích - Xóa đối tượng:** `xoa_doi_tuong_sequence_diagram.puml`
- **Biểu đồ lớp thiết kế - Quản lý thông tin (Admin):** `FrontendDesign/Static/Uml/admin_design_class_diagram.puml`
- **Giao diện tĩnh - Bảng điều khiển quản trị viên:** `FrontendDesign/Static/Html/AdminDashboardPage.html`
- **Giao diện tĩnh - Quản lý người dùng:** `FrontendDesign/Static/Html/UserManagementPage.html`
- **Giao diện tĩnh - Quản lý nội dung chia sẻ:** `FrontendDesign/Static/Html/SharedContentManagementPage.html`
- **Biểu đồ lớp thiết kế - Xác thực:** `FrontendDesign/Static/Uml/xac_thuc_design_class_diagram.puml`
- **Giao diện tĩnh - Đăng nhập:** `FrontendDesign/Static/Html/LoginPage.html`
- **Giao diện tĩnh - Đăng ký:** `FrontendDesign/Static/Html/RegisterPage.html`
- **Các biểu đồ và giao diện tĩnh cho module Tư vấn Chế độ Dinh dưỡng**
- **Biểu đồ lớp thiết kế - Tư vấn Chế độ Dinh dưỡng:** `FrontendDesign/Static/Uml/che_do_dinh_duong_design_class_diagram.puml`
- **Giao diện tĩnh - Quản lý Chế độ Dinh dưỡng:** `FrontendDesign/Static/Html/NutritionPlanPage.html`
- **Giao diện tĩnh - Tìm kiếm Món ăn & Thực phẩm:** `FrontendDesign/Static/Html/FoodSearchPage.html`
- **Giao diện tĩnh - Nhập Thông tin Nhu cầu Dinh dưỡng:** `FrontendDesign/Static/Html/NutritionalNeedsInputPage.html`
- **Các biểu đồ và giao diện tĩnh cho module Theo dõi và Đánh giá Kế hoạch Dinh dưỡng**
- **Biểu đồ lớp thiết kế - Theo dõi và Đánh giá Kế hoạch Dinh dưỡng:** `FrontendDesign/Static/Uml/theo_doi_danh_gia_design_class_diagram.puml`
- **Giao diện tĩnh - Nhập Liệu Dữ Liệu Hàng Ngày:** `FrontendDesign/Static/Html/DailyLogInputPage.html`
- **Giao diện tĩnh - Báo Cáo Tiến Độ và Đánh Giá:** `FrontendDesign/Static/Html/ProgressReportPage.html`
- **Giao diện tĩnh - Quản lý Hồ sơ cá nhân:** `FrontendDesign/Static/Html/ProfileManagementPage.html`
- **Các biểu đồ và giao diện tĩnh cho module Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng**
- **Biểu đồ lớp thiết kế - Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng:** `FrontendDesign/Static/Uml/share_design_class_diagram.puml`
- **Giao diện tĩnh - Chia sẻ Kế hoạch & Hoạt động:** `FrontendDesign/Static/Html/SharePlanActivityPage.html`
- **Giao diện tĩnh - Xem Nội dung Chia sẻ:** `FrontendDesign/Static/Html/ViewSharedContentPage.html`

## Thiết kế Giao diện Người dùng (Frontend Design)

### Giao diện HTML tĩnh

- `AdminDashboardPage.html`: Trang bảng điều khiển quản trị viên.
- `UserManagementPage.html`: Trang quản lý người dùng.
- `SharedContentManagementPage.html`: Trang quản lý nội dung chia sẻ.
- `LoginPage.html`: Trang đăng nhập.
- `RegisterPage.html`: Trang đăng ký tài khoản mới.
- `FoodSearchPage.html`: Trang tìm kiếm thực phẩm.
- `NutritionalNeedsInputPage.html`: Trang nhập liệu nhu cầu dinh dưỡng.
- `NutritionPlanPage.html`: Trang hiển thị kế hoạch dinh dưỡng.
- `NutritionalConsultationPage.html`: Trang tư vấn chế độ dinh dưỡng.
- `NutritionPlanDetailPage.html`: Trang chi tiết kế hoạch dinh dưỡng.
- `DailyLogInputPage.html`: Trang nhập liệu nhật ký hàng ngày.
- `ProgressReportPage.html`: Trang báo cáo tiến độ.
- `ProfileManagementPage.html`: Trang quản lý hồ sơ cá nhân.
- `SharePlanActivityPage.html`: Trang chia sẻ kế hoạch và hoạt động.
- `ViewSharedContentPage.html`: Trang xem nội dung chia sẻ.

### Biểu đồ Lớp (UML)

- `admin_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module quản lý thông tin (Admin).
- `xac_thuc_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module xác thực.
- `che_do_dinh_duong_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module chế độ dinh dưỡng.
- `nutritional_consultation_entity_class_diagram.puml`: Biểu đồ lớp thực thể thiết kế cho module tư vấn chế độ dinh dưỡng.
- `nutritional_consultation_design_class_diagram.puml`: Biểu đồ lớp giao diện thiết kế cho module tư vấn chế độ dinh dưỡng.
- `nutritional_consultation_dao_class_diagram.puml`: Biểu đồ lớp DAO thiết kế cho module tư vấn chế độ dinh dưỡng.
- `theo_doi_danh_gia_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module theo dõi và đánh giá.
- `profile_management_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module quản lý hồ sơ cá nhân.
- `share_design_class_diagram.puml`: Biểu đồ lớp thiết kế cho module chia sẻ kế hoạch và hoạt động.

### Cấu trúc thư mục

```
FrontendDesign/
├── Static/
│   ├── Html/
│   │   ├── AdminDashboardPage.html
│   │   ├── UserManagementPage.html
│   │   ├── SharedContentManagementPage.html
│   │   ├── LoginPage.html
│   │   ├── RegisterPage.html
│   │   ├── FoodSearchPage.html
│   │   ├── NutritionalNeedsInputPage.html
│   │   ├── NutritionPlanPage.html
│   │   ├── NutritionalConsultationPage.html
│   │   ├── NutritionPlanDetailPage.html
│   │   ├── DailyLogInputPage.html
│   │   ├── ProgressReportPage.html
│   │   ├── ProfileManagementPage.html
│   │   ├── SharePlanActivityPage.html
│   │   └── ViewSharedContentPage.html
│   └── Uml/
│       ├── admin_design_class_diagram.puml
│       ├── xac_thuc_design_class_diagram.puml
│       ├── che_do_dinh_duong_design_class_diagram.puml
│       ├── theo_doi_danh_gia_design_class_diagram.puml
│       ├── profile_management_design_class_diagram.puml
│       ├── share_design_class_diagram.puml
│       ├── nutritional_consultation_entity_class_diagram.puml
│       ├── nutritional_consultation_design_class_diagram.puml
│       └── nutritional_consultation_dao_class_diagram.puml
```

## 3. Cài đặt và Chạy dự án

(Chưa cập nhật)

## 4. Cấu trúc Thư mục

(Chưa cập nhật)

## 5. Các Công nghệ chính

- ReactJS
- React Router DOM
- Axios
- Redux Toolkit
- PlantUML (để thiết kế)
