# Hệ thống Tư vấn Dinh dưỡng AI (Frontend)

## A. Mô tả hệ thống

Dự án này là một ứng dụng ReactJS phục vụ nhu cầu lập kế hoạch, thực hiện và theo dõi chế độ dinh dưỡng một cách hiệu quả, thông minh và được cá nhân hóa. Frontend đóng vai trò là giao diện trực quan, thân thiện với người dùng để tương tác với các dịch vụ backend và AI phức tạp.

## B. Kiến trúc tổng quan

Hệ thống frontend được thiết kế theo kiến trúc dựa trên Component của ReactJS, đảm bảo tính linh hoạt, dễ bảo trì và mở rộng.

- **Lớp Giao diện (Presentation Layer)**: Các React Components tương tác trực tiếp với người dùng, hiển thị dữ liệu và thu thập đầu vào.
- **Lớp Logic Ứng dụng (Application Layer - Frontend)**: Quản lý trạng thái ứng dụng (sử dụng Redux Toolkit), xử lý routing (React Router DOM) và gọi các API đến Backend Spring Boot và AI/ML Processing Layer.
- **Lớp Tích hợp API (API Integration Layer)**: Sử dụng Axios để quản lý các cuộc gọi API đến Backend Spring Boot (qua REST API, sử dụng JWT cho Spring Security) và Dịch vụ AI/ML (qua API riêng cho các tác vụ Vision, NLP).

## C. Tính năng chính

### 1. Đối với Người dùng (User)
- **Tư vấn dinh dưỡng thông minh**: Giao diện chatbox thân thiện để trò chuyện với AI, nhận lời khuyên và điều chỉnh kế hoạch ăn uống.
- **Nhận dạng món ăn**: Tải lên hình ảnh bữa ăn để hệ thống nhận dạng và ghi nhận thông tin dinh dưỡng.
- **Lập kế hoạch cá nhân hóa**: Tạo, chỉnh sửa và quản lý các kế hoạch dinh dưỡng dựa trên mục tiêu cá nhân.
- **Theo dõi tiến độ**: Ghi lại nhật ký bữa ăn hàng ngày, theo dõi lượng calo, macro và các chỉ số dinh dưỡng khác thông qua biểu đồ trực quan.
- **Cộng đồng**: Chia sẻ công thức, kế hoạch và tương tác với người dùng khác thông qua tính năng thích và bình luận.
- **Quản lý hồ sơ cá nhân**: Cập nhật thông tin sinh trắc học, mục tiêu sức khỏe và sở thích ăn uống.

### 2. Đối với Quản trị viên (Admin)
- **Quản lý Người dùng**: Xem, cập nhật và xóa tài khoản người dùng.
- **Quản lý Dữ liệu Dinh dưỡng**: Thêm, sửa, xóa thông tin món ăn, thực phẩm.
- **Kích hoạt huấn luyện lại AI**: Giao diện để kích hoạt các quy trình huấn luyện lại mô hình AI.

## D. Công nghệ sử dụng

| Hạng mục          | Công nghệ                     | Mục đích                                          |
|:------------------|:------------------------------|:--------------------------------------------------|
| **Framework**     | React                         | Xây dựng giao diện người dùng dựa trên component. |
| **Build Tool**    | Vite                          | Khởi tạo và tối ưu hóa dự án.                     |
| **Routing**       | React Router                  | Xử lý điều hướng phía máy khách.                  |
| **Quản lý State** | Redux Toolkit                 | Quản lý trạng thái ứng dụng toàn cục.             |
| **Styling**       | Tailwind CSS                  | Xây dựng giao diện tùy chỉnh nhanh chóng.         |
| **Gọi API**       | Axios                         | Thực hiện các yêu cầu HTTP đến backend.           |
| **Biểu đồ**       | Recharts                      | Hiển thị dữ liệu trực quan.                       |
| **Kiểm thử**      | Vitest, React Testing Library | Viết và chạy unit test và integration test.       |

## E. Cấu trúc dự án

Dự án tuân theo một cấu trúc dựa trên tính năng để tổ chức mã nguồn một cách hợp lý.

```
.
├── FrontendAnalysis/         # Tài liệu phân tích hệ thống (biểu đồ lớp, tuần tự...)
├── FrontendDesign/           # Tài liệu thiết kế chi tiết (biểu đồ CSDL, triển khai...)
├── FrontendRequirements/     # Tài liệu yêu cầu nghiệp vụ và use case
├── ImplementationFrontend/nutrition-app-frontend/
│   ├── public/              # Chứa các tài sản tĩnh (static assets)
│   ├── src/                 # Thư mục mã nguồn chính
│   │   ├── assets/          # Hình ảnh, fonts và các tài sản khác
│   │   ├── components/      # Các React component có thể tái sử dụng
│   │   ├── context/         # React context providers
│   │   ├── hooks/           # Các custom React hook
│   │   ├── mocks/           # Mock server và handlers cho kiểm thử (MSW)
│   │   ├── pages/           # Các component trang chính, được liên kết với các route
│   │   ├── services/        # Các module để tương tác với API backend
│   │   ├── store/           # Redux Toolkit store, slices và logic
│   │   └── main.jsx         # Điểm vào của ứng dụng
│   ├── .env.development     # Cấu hình biến môi trường cho môi trường phát triển
│   ├── package.json         # Liệt kê các dependency và script của dự án
│   └── vite.config.js       # Cấu hình cho Vite
├── FRONTEND_PLANNING.md      # Kế hoạch và định hướng tổng thể của dự án Frontend
└── FRONTEND_TASK.md          # Danh sách các công việc cần thực hiện cho Frontend
```

## F. Tài liệu Phân tích & Thiết kế Hệ thống (System Analysis & Design Documents)

### 1. Tài liệu Yêu cầu nghiệp vụ (Requirements Documents)

#### a. Tài liệu mô hình nghiệp vụ (Business Model Documents)

- [`BusinessModel.docx`: Yêu cầu nghiệp vụ theo định dạng Word.](./FrontendRequirements/NaturalLanguage/BusinessModel.docx)
- [`BusinessModel.md`: Yêu cầu nghiệp vụ theo định dạng Markdown.](./FrontendRequirements/NaturalLanguage/BusinessModel.md)

#### b. Biểu đồ Use Case (Use Case Diagrams)

- [`usecase_tong_quan.png`: Mô tả tổng quan các chức năng của hệ thống.](./FrontendRequirements/UmlPictures/usecase_tong_quan.png)
- [`usecase_xac_thuc.png`: Biểu đồ chi tiết cho chức năng đăng ký, đăng nhập, đăng xuất.](./FrontendRequirements/UmlPictures/usecase_xac_thuc.png)
- [`usecase_tu_van_che_do_dinh_duong.png`: Biểu đồ chi tiết cho chức năng tư vấn dinh dưỡng.](./FrontendRequirements/UmlPictures/usecase_tu_van_che_do_dinh_duong.png)
- [`usecase_lap_ke_hoach_dinh_duong.png`: Biểu đồ chi tiết cho chức năng lập kế hoạch dinh dưỡng.](./FrontendRequirements/UmlPictures/usecase_lap_ke_hoach_dinh_duong.png)
- [`usecase_theo_doi_danh_gia.png`: Biểu đồ chi tiết cho chức năng theo dõi và đánh giá.](./FrontendRequirements/UmlPictures/usecase_theo_doi_danh_gia.png)
- [`usecase_quan_ly_ho_so_ca_nhan.png`: Biểu đồ chi tiết cho chức năng quản lý hồ sơ cá nhân.](./FrontendRequirements/UmlPictures/usecase_quan_ly_ho_so_ca_nhan.png)
- [`usecase_chia_se_ke_hoach_hoat_dong.png`: Biểu đồ chi tiết cho chức năng chia sẻ.](./FrontendRequirements/UmlPictures/usecase_chia_se_ke_hoach_hoat_dong.png)
- [`usecase_quan_ly_thong_tin_admin.png`: Biểu đồ chi tiết cho chức năng quản trị viên.](./FrontendRequirements/UmlPictures/usecase_quan_ly_thong_tin_admin.png)

### 2. Tài liệu Phân tích (Analysis Documents)

#### a. Tài liệu kịch bản (Scenario Documents)

- [`Scenarios.docx`: Các kịch bản người dùng theo định dạng Word.](./FrontendAnalysis/Scenarios/Scenarios.docx)
- [`Scenarios.md`: Các kịch bản người dùng theo định dạng Markdown.](./FrontendAnalysis/Scenarios/Scenarios.md)

#### b. Biểu đồ lớp thực thể pha phân tích (Entity Class Diagrams - Analysis Phase)

- [`entity_class_diagram.png`: Trích xuất từ các kịch bản người dùng.](./FrontendAnalysis/Entity/UmlPictures/entity_class_diagram.png)

#### c. Biểu đồ lớp phân tích (Analysis Class Diagrams)

- [`xac_thuc_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Xác thực.](./FrontendAnalysis/Class/UmlPictures/xac_thuc_analysis_class_diagram.png)
- [`tu_van_che_do_dinh_duong_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Tư vấn Dinh dưỡng.](./FrontendAnalysis/Class/UmlPictures/tu_van_che_do_dinh_duong_analysis_class_diagram.png)
- [`lap_ke_hoach_dinh_duong_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Lập kế hoạch Dinh dưỡng.](./FrontendAnalysis/Class/UmlPictures/lap_ke_hoach_dinh_duong_analysis_class_diagram.png)
- [`theo_doi_danh_gia_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Theo dõi và Đánh giá.](./FrontendAnalysis/Class/UmlPictures/theo_doi_danh_gia_analysis_class_diagram.png)
- [`quan_ly_ho_so_ca_nhan_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Quản lý Hồ sơ.](./FrontendAnalysis/Class/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_class_diagram.png)
- [`chia_se_ke_hoach_hoat_dong_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Chia sẻ.](./FrontendAnalysis/Class/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_class_diagram.png)
- [`quan_ly_thong_tin_admin_analysis_class_diagram.png`: Biểu đồ lớp phân tích cho module Quản trị viên. ](./FrontendAnalysis/Class/UmlPictures/quan_ly_thong_tin_admin_analysis_class_diagram.png)

#### d. Biểu đồ trạng thái (State Diagrams)

- [`xac_thuc_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Xác thực.](./FrontendAnalysis/State/UmlPictures/xac_thuc_analysis_state_diagram.png)
- [`tu_van_che_do_dinh_duong_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Tư vấn Dinh dưỡng.](./FrontendAnalysis/State/UmlPictures/tu_van_che_do_dinh_duong_analysis_state_diagram.png)
- [`lap_ke_hoach_dinh_duong_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Lập kế hoạch Dinh dưỡng.](./FrontendAnalysis/State/UmlPictures/lap_ke_hoach_dinh_duong_analysis_state_diagram.png)
- [`theo_doi_danh_gia_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Theo dõi và Đánh giá.](./FrontendAnalysis/State/UmlPictures/theo_doi_danh_gia_analysis_state_diagram.png)
- [`quan_ly_ho_so_ca_nhan_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Quản lý Hồ sơ.](./FrontendAnalysis/State/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_state_diagram.png)
- [`chia_se_ke_hoach_hoat_dong_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Chia sẻ.](./FrontendAnalysis/State/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_state_diagram.png)
- [`quan_ly_thong_tin_admin_analysis_state_diagram.png`: Biểu đồ trạng thái cho module Quản trị viên.](./FrontendAnalysis/State/UmlPictures/quan_ly_thong_tin_admin_analysis_state_diagram.png)

#### e. Biểu đồ tuần tự (Sequence Diagrams - Analysis Phase)

- [`dang_ky_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đăng ký.](./FrontendAnalysis/Sequence/UmlPictures/dang_ky_sequence_diagram.png)
- [`dang_nhap_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đăng nhập.](./FrontendAnalysis/Sequence/UmlPictures/dang_nhap_sequence_diagram.png)
- [`dang_xuat_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đăng xuất.](./FrontendAnalysis/Sequence/UmlPictures/dang_xuat_sequence_diagram.png)
- [`gui_thong_tin_tu_van_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng gửi thông tin tư vấn dinh dưỡng.](./FrontendAnalysis/Sequence/UmlPictures/gui_thong_tin_tu_van_sequence_diagram.png)
- [`yeu_cau_chinh_sua_thuc_don_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng yêu cầu chỉnh sửa thực đơn.](./FrontendAnalysis/Sequence/UmlPictures/yeu_cau_chinh_sua_thuc_don_sequence_diagram.png)
- [`chon_kieu_ke_hoach_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng chọn kiểu kế hoạch dinh dưỡng.](./FrontendAnalysis/Sequence/UmlPictures/chon_kieu_ke_hoach_sequence_diagram.png)
- [`luu_ke_hoach_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng lưu kế hoạch dinh dưỡng.](./FrontendAnalysis/Sequence/UmlPictures/luu_ke_hoach_sequence_diagram.png)
- [`nhap_du_lieu_hang_ngay_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng nhập dữ liệu hàng ngày.](./FrontendAnalysis/Sequence/UmlPictures/nhap_du_lieu_hang_ngay_sequence_diagram.png)
- [`lay_bao_cao_tien_do_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng lấy báo cáo tiến độ.](./FrontendAnalysis/Sequence/UmlPictures/lay_bao_cao_tien_do_sequence_diagram.png)
- [`xem_ho_so_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng xem hồ sơ cá nhân.](./FrontendAnalysis/Sequence/UmlPictures/xem_ho_so_sequence_diagram.png)
- [`cap_nhat_sinh_trac_hoc_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng cập nhật thông tin sinh trắc học.](./FrontendAnalysis/Sequence/UmlPictures/cap_nhat_sinh_trac_hoc_sequence_diagram.png)
- [`cap_nhat_muc_tieu_suc_khoe_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng cập nhật mục tiêu sức khỏe.](./FrontendAnalysis/Sequence/UmlPictures/cap_nhat_muc_tieu_suc_khoe_sequence_diagram.png)
- [`cap_nhat_so_thich_an_uong_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng cập nhật sở thích ăn uống.](./FrontendAnalysis/Sequence/UmlPictures/cap_nhat_so_thich_an_uong_sequence_diagram.png)
- [`thay_doi_che_do_chia_se_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng thay đổi chế độ chia sẻ.](./FrontendAnalysis/Sequence/UmlPictures/thay_doi_che_do_chia_se_sequence_diagram.png)
- [`xem_noi_dung_chia_se_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng xem nội dung chia sẻ.](./FrontendAnalysis/Sequence/UmlPictures/xem_noi_dung_chia_se_sequence_diagram.png)
- [`thich_noi_dung_chia_se_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng thích nội dung chia sẻ.](./FrontendAnalysis/Sequence/UmlPictures/thich_noi_dung_chia_se_sequence_diagram.png)
- [`them_binh_luan_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng thêm bình luận.](./FrontendAnalysis/Sequence/UmlPictures/them_binh_luan_sequence_diagram.png)
- [`luu_noi_dung_chia_se_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng lưu nội dung chia sẻ.](./FrontendAnalysis/Sequence/UmlPictures/luu_noi_dung_chia_se_sequence_diagram.png)
- [`de_xuat_nguoi_dung_nhom_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đề xuất người dùng/nhóm.](./FrontendAnalysis/Sequence/UmlPictures/de_xuat_nguoi_dung_nhom_sequence_diagram.png)
- [`quan_ly_nguoi_dung_them_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng quản lý người dùng (thêm).](./FrontendAnalysis/Sequence/UmlPictures/quan_ly_nguoi_dung_them_sequence_diagram.png)
- [`quan_ly_mon_an_them_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng quản lý món ăn (thêm).](./FrontendAnalysis/Sequence/UmlPictures/quan_ly_mon_an_them_sequence_diagram.png)
- [`kich_hoat_huan_luyen_lai_ai_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng kích hoạt huấn luyện lại AI.](./FrontendAnalysis/Sequence/UmlPictures/kich_hoat_huan_luyen_lai_ai_sequence_diagram.png)
- [`sua_doi_tuong_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng sửa đối tượng.](./FrontendAnalysis/Sequence/UmlPictures/sua_doi_tuong_sequence_diagram.png)
- [`xoa_doi_tuong_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng xóa đối tượng.](./FrontendAnalysis/Sequence/UmlPictures/xoa_doi_tuong_sequence_diagram.png)

### 3. Tài liệu Thiết kế (Design Documents)

#### a. Biểu đồ lớp thực thể pha thiết kế (Entity Class Diagrams - Design Phase)

- [`entity_class_diagram_design.png`: Trích xuất từ biểu đồ lớp thực thể pha phân tích.](./FrontendDesign/Entity/UmlPictures/entity_class_diagram_design.png)

#### b. Biểu đồ quan hệ thực thể CSDL (Entity - Relationship Database Diagrams)

- [`database_schema_diagram.png`: Biểu đồ thiết kế schema cơ sở dữ liệu.](./FrontendDesign/Database/UmlPictures/database_schema_diagram.png)

#### c. Biểu đồ lớp thiết kế (Design Class Diagrams)

- [`admin_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module quản lý thông tin (Admin).](./FrontendDesign/Static/UmlPictures/admin_design_class_diagram.png)
- [`xac_thuc_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module Xác thực.](./FrontendDesign/Static/UmlPictures/xac_thuc_design_class_diagram.png)
- [`che_do_dinh_duong_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module chế độ dinh dưỡng.](./FrontendDesign/Static/UmlPictures/che_do_dinh_duong_design_class_diagram.png)
- [`nutritional_consultation_entity_class_diagram.png`: Biểu đồ lớp thực thể thiết kế cho module tư vấn chế độ dinh dưỡng.](./FrontendDesign/Static/UmlPictures/nutritional_consultation_entity_class_diagram.png)
- [`nutritional_consultation_design_class_diagram.png`: Biểu đồ lớp giao diện thiết kế cho module tư vấn chế độ dinh dưỡng.](./FrontendDesign/Static/UmlPictures/nutritional_consultation_design_class_diagram.png)
- [`nutritional_consultation_dao_class_diagram.png`: Biểu đồ lớp DAO thiết kế cho module tư vấn chế độ dinh dưỡng.](./FrontendDesign/Static/UmlPictures/nutritional_consultation_dao_class_diagram.png)
- [`theo_doi_danh_gia_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module Theo dõi và Đánh giá Kế hoạch Dinh dưỡng.](./FrontendDesign/Static/UmlPictures/theo_doi_danh_gia_design_class_diagram.png)
- [`profile_management_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module Quản lý Hồ sơ Cá nhân.](./FrontendDesign/Static/UmlPictures/profile_management_design_class_diagram.png)
- [`share_design_class_diagram.png`: Biểu đồ lớp thiết kế cho module Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng.](./FrontendDesign/Static/UmlPictures/share_design_class_diagram.png)

#### d. Biểu đồ Triển khai (Deployment Diagrams)

- [`deployment_diagram.png`: Biểu đồ hiển thị kiến trúc triển khai các thành phần của hệ thống.](./FrontendDesign/Deployment/UmlPictures/deployment_diagram.png)

#### e. Biểu đồ hoạt động (Activity Diagrams)

- [`login_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Đăng nhập.](./FrontendDesign/Dynamic/Activity/UmlPictures/login_activity_diagram.png)
- [`register_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Đăng ký.](./FrontendDesign/Dynamic/Activity/UmlPictures/register_activity_diagram.png)
- [`user_management_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Quản lý người dùng (Admin).](./FrontendDesign/Dynamic/Activity/UmlPictures/user_management_activity_diagram.png)
- [`shared_content_management_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Quản lý nội dung chia sẻ (Admin).](./FrontendDesign/Dynamic/Activity/UmlPictures/shared_content_management_activity_diagram.png)
- [`nutritional_consultation_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Tư vấn dinh dưỡng.](./FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_consultation_activity_diagram.png)
- [`food_search_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Tìm kiếm món ăn và thực phẩm.](./FrontendDesign/Dynamic/Activity/UmlPictures/food_search_activity_diagram.png)
- [`nutritional_needs_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Nhập thông tin nhu cầu dinh dưỡng.](./FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_needs_activity_diagram.png)
- [`create_nutrition_plan_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Tạo kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Activity/UmlPictures/create_nutrition_plan_activity_diagram.png)
- [`view_update_nutrition_plan_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Xem và Cập nhật kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Activity/UmlPictures/view_update_nutrition_plan_activity_diagram.png)
- [`delete_nutrition_plan_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Xóa kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Activity/UmlPictures/delete_nutrition_plan_activity_diagram.png)
- [`daily_log_input_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Nhập liệu dữ liệu hàng ngày.](./FrontendDesign/Dynamic/Activity/UmlPictures/daily_log_input_activity_diagram.png)
- [`progress_report_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Báo cáo tiến độ và đánh giá.](./FrontendDesign/Dynamic/Activity/UmlPictures/progress_report_activity_diagram.png)
- [`profile_management_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Quản lý hồ sơ cá nhân.](./FrontendDesign/Dynamic/Activity/UmlPictures/profile_management_activity_diagram.png)
- [`share_plan_activity_diagram.png`: Biểu đồ hoạt động cho chức năng Chia sẻ kế hoạch và hoạt động.](./FrontendDesign/Dynamic/Activity/UmlPictures/share_plan_activity_diagram.png)

#### f. Tài liệu kịch bản người dùng (Scenario Documents - Version 3)

- [`Scenarios_v3.docx`: Các kịch bản người dùng (phiên bản 3) theo định dạng Word.](./FrontendDesign/Dynamic/Scenarios/Scenarios_v3.docx)
- [`Scenarios_v3.md`: Các kịch bản người dùng (phiên bản 3) theo định dạng Markdown.](./FrontendDesign/Dynamic/Scenarios/Scenarios_v3.md)

#### g. Biểu đồ tuần tự (Sequence Diagrams - Design Phase)

- [`register_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đăng ký.](./FrontendDesign/Dynamic/Sequence/UmlPictures/register_sequence_diagram.png)
- [`login_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng đăng nhập.](./FrontendDesign/Dynamic/Sequence/UmlPictures/login_sequence_diagram.png)
- [`user_management_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Quản lý người dùng (Admin).](./FrontendDesign/Dynamic/Sequence/UmlPictures/user_management_sequence_diagram.png)
- [`shared_content_management_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Quản lý nội dung chia sẻ (Admin).](./FrontendDesign/Dynamic/Sequence/UmlPictures/shared_content_management_sequence_diagram.png)
- [`nutritional_consultation_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Tư vấn dinh dưỡng.](./FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_consultation_sequence_diagram.png)
- [`food_search_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Tìm kiếm món ăn và thực phẩm.](./FrontendDesign/Dynamic/Sequence/UmlPictures/food_search_sequence_diagram.png)
- [`nutritional_needs_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Nhập thông tin nhu cầu dinh dưỡng.](./FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_needs_sequence_diagram.png)
- [`create_nutrition_plan_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Tạo kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Sequence/UmlPictures/create_nutrition_plan_sequence_diagram.png)
- [`view_update_nutrition_plan_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Xem và Cập nhật kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Sequence/UmlPictures/view_update_nutrition_plan_sequence_diagram.png)
- [`delete_nutrition_plan_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Xóa kế hoạch dinh dưỡng.](./FrontendDesign/Dynamic/Sequence/UmlPictures/delete_nutrition_plan_sequence_diagram.png)
- [`daily_log_input_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Nhập liệu dữ liệu hàng ngày.](./FrontendDesign/Dynamic/Sequence/UmlPictures/daily_log_input_sequence_diagram.png)
- [`progress_report_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Báo cáo tiến độ và đánh giá.](./FrontendDesign/Dynamic/Sequence/UmlPictures/progress_report_sequence_diagram.png)
- [`profile_management_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Quản lý hồ sơ cá nhân.](./FrontendDesign/Dynamic/Sequence/UmlPictures/profile_management_sequence_diagram.png)
- [`share_plan_sequence_diagram.png`: Biểu đồ tuần tự cho chức năng Chia sẻ kế hoạch và hoạt động.](./FrontendDesign/Dynamic/Sequence/UmlPictures/share_plan_sequence_diagram.png)

#### h. Giao diện Tĩnh (Static Interfaces)

- [`LoginPage.html`: Giao diện trang Đăng nhập.](./FrontendDesign/Static/Html/LoginPage.html)
- [`RegisterPage.html`: Giao diện trang Đăng ký tài khoản mới.](./FrontendDesign/Static/Html/RegisterPage.html)
- [`AdminDashboardPage.html`: Trang bảng điều khiển quản trị viên.](./FrontendDesign/Static/Html/AdminDashboardPage.html)
- [`UserManagementPage.html`: Trang quản lý người dùng.](./FrontendDesign/Static/Html/UserManagementPage.html)
- [`SharedContentManagementPage.html`: Trang quản lý nội dung chia sẻ.](./FrontendDesign/Static/Html/SharedContentManagementPage.html)
- [`NutritionalConsultationPage.html`: Trang tư vấn chế độ dinh dưỡng.](./FrontendDesign/Static/Html/NutritionalConsultationPage.html)
- [`FoodSearchPage.html`: Trang tìm kiếm thực phẩm.](./FrontendDesign/Static/Html/FoodSearchPage.html)
- [`NutritionalNeedsInputPage.html`: Trang nhập liệu nhu cầu dinh dưỡng.](./FrontendDesign/Static/Html/NutritionalNeedsInputPage.html)
- [`NutritionPlanPage.html`: Trang hiển thị kế hoạch dinh dưỡng.](./FrontendDesign/Static/Html/NutritionPlanPage.html)
- [`NutritionPlanDetailPage.html`: Trang chi tiết kế hoạch dinh dưỡng.](./FrontendDesign/Static/Html/NutritionPlanDetailPage.html)
- [`DailyLogInputPage.html`: Trang nhập liệu nhật ký hàng ngày.](./FrontendDesign/Static/Html/DailyLogInputPage.html)
- [`ProgressReportPage.html`: Trang báo cáo tiến độ.](./FrontendDesign/Static/Html/ProgressReportPage.html)
- [`ProfileManagementPage.html`: Trang quản lý hồ sơ cá nhân.](./FrontendDesign/Static/Html/ProfileManagementPage.html)
- [`SharePlanActivityPage.html`: Trang chia sẻ kế hoạch và hoạt động.](./FrontendDesign/Static/Html/SharePlanActivityPage.html)
- [`ViewSharedContentPage.html`: Trang xem nội dung chia sẻ.](./FrontendDesign/Static/Html/ViewSharedContentPage.html)

## G. Hướng dẫn Cài đặt và Chạy

### 1. Yêu cầu
- Node.js (phiên bản 18.x trở lên)
- npm

### 2. Cài đặt
1.  Di chuyển đến thư mục gốc của dự án frontend:
    ```bash
    cd ImplementationFrontend/nutrition-app-frontend
    ```
2.  Cài đặt các dependency:
    ```bash
    npm install
    ```

### 3. Cấu hình Biến Môi trường
1.  Tạo một tệp `.env.development` trong thư mục gốc của frontend (`ImplementationFrontend/nutrition-app-frontend`).
2.  Thêm các URL của API backend và AI service vào tệp:
    ```env
    # .env.development

    # --- Cấu hình cho API Backend Chính ---
    # Thay đổi cổng (port) nếu Backend của bạn chạy ở một cổng khác.
    VITE_BACKEND_API_URL=http://localhost:8080/api

    # --- Cấu hình cho AI Service (Tư vấn Dinh dưỡng và Thị giác Máy tính) ---
    # Thay đổi cổng (port) nếu AI Service của bạn chạy ở một cổng khác.
    VITE_AI_SERVICE_API_URL=http://localhost:5000/api
    ```
    *Lưu ý: Vite yêu cầu tiền tố `VITE_` cho các biến môi trường được sử dụng ở phía client.*

### 4. Chạy Ứng dụng
Để khởi động máy chủ phát triển, chạy lệnh sau:
```bash
npm run dev
```
Ứng dụng sẽ có sẵn tại `http://localhost:5173` (hoặc một cổng khác nếu 5173 đang được sử dụng).

### 5. Các Scripts khác
- **Build cho Production**: `npm run build`
- **Lint mã nguồn**: `npm run lint`
- **Xem bản build production**: `npm run preview`
- **Chạy kiểm thử**: `npm test`
- **Chạy kiểm thử với coverage**: `npm run test:coverage`
- **Chạy kiểm thử ở chế độ watch**: `npm run test:watch`

## H. Tài liệu API

Frontend giao tiếp với hai dịch vụ backend chính:

### 1. Main Backend API (`VITE_BACKEND_API_URL`)
-   **URL Mặc định**: `http://localhost:8080`
-   **Chức năng**: Chịu trách nhiệm xử lý logic nghiệp vụ cốt lõi của ứng dụng, bao gồm:
    -   Quản lý xác thực và tài khoản người dùng (Đăng ký, Đăng nhập, JWT).
    -   Lưu trữ và truy xuất dữ liệu (thông tin người dùng, bữa ăn, thực phẩm, kế hoạch dinh dưỡng).
    -   Quản lý các tính năng cộng đồng như chia sẻ, thích và bình luận.

### 2. AI Service API (`VITE_AI_SERVICE_API_URL`)
-   **URL Mặc định**: `http://localhost:5000`
-   **Chức năng**: Cung cấp các tính năng dựa trên trí tuệ nhân tạo:
    -   **Tư vấn Dinh dưỡng**: Xử lý các cuộc hội thoại với AI để đưa ra lời khuyên dinh dưỡng.
    -   **Thị giác Máy tính (Computer Vision)**: Phân tích hình ảnh món ăn do người dùng tải lên để nhận dạng và trích xuất thông tin dinh dưỡng.

## I. Kiểm thử (Testing)

Kiểm thử là một phần quan trọng của dự án để đảm bảo chất lượng và độ ổn định của mã nguồn. Dự án sử dụng bộ công cụ hiện đại để thực hiện kiểm thử đơn vị (unit testing) và kiểm thử tích hợp (integration testing) cho các component React.

### 1. Công nghệ sử dụng

-   **Test Runner**: [Vitest](https://vitest.dev/) - Một framework kiểm thử thế hệ mới, nhanh và tương thích hoàn toàn với Vite.
-   **Thư viện Kiểm thử Component**: [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/) - Cung cấp các công cụ để kiểm thử component React theo cách mà người dùng cuối tương tác với chúng.
-   **API Mocking**: [Mock Service Worker (MSW)](https://mswjs.io/) - Cho phép chặn các yêu cầu mạng ở cấp độ mạng, giúp mô phỏng API backend một cách đáng tin cậy trong quá trình kiểm thử mà không cần máy chủ thực.
-   **Môi trường Test**: [JSDOM](https://github.com/jsdom/jsdom) - Mô phỏng môi trường DOM của trình duyệt để chạy các bài kiểm thử trong Node.js.

### 2. Triết lý Kiểm thử

Chúng tôi tuân theo triết lý rằng các bài kiểm thử nên càng giống với cách người dùng sử dụng ứng dụng càng tốt. Thay vì kiểm thử chi tiết triển khai (implementation details) của component, chúng tôi tập trung vào:

-   **Hành vi người dùng**: Kiểm tra xem component có hiển thị đúng thông tin và phản hồi chính xác với các tương tác của người dùng (như nhấp chuột, nhập liệu) hay không.
-   **Tích hợp**: Đảm bảo các component hoạt động tốt với nhau và với các dịch vụ bên ngoài (được giả lập bởi MSW).

### 3. Vị trí các tệp Kiểm thử

Các tệp kiểm thử thường được đặt trong thư mục `__tests__` bên trong thư mục của component hoặc tính năng tương ứng, hoặc có đuôi là `.test.jsx` hoặc `.spec.jsx`.

### 4. Cách chạy Kiểm thử

Các lệnh để chạy kiểm thử đã được định nghĩa trong `package.json` và được liệt kê trong phần **Hướng dẫn Cài đặt và Chạy**.