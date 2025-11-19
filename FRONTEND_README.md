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

| Hạng mục           | Công nghệ                             | Mục đích                                    |
| :---------------- | :------------------------------------ | :------------------------------------------ |
| **Framework**     | ReactJS                               | Xây dựng giao diện dựa trên Component       |
| **Quản lý State** | Redux Toolkit                         | Quản lý trạng thái ứng dụng phức tạp       |
| **Giao diện/Styling** | Tailwind CSS / Material UI / Bootstrap | Xây dựng UI nhanh chóng, đảm bảo Responsive |
| **Biểu đồ Dữ liệu** | Chart.js / Recharts                   | Trực quan hóa tiến độ                       |
| **Quản lý API**   | Axios                                 | Gọi API đến Backend Spring Boot và AI Service |
| **Build Tool**    | Vite / Create React App               | Khởi tạo và tối ưu hóa dự án                |
| **Thiết kế**      | PlantUML                              | Thiết kế biểu đồ UML                        |

## E. Cấu trúc thư mục

```
.
├── FrontendAnalysis/         # Tài liệu phân tích hệ thống (biểu đồ lớp, tuần tự...)
├── FrontendDesign/           # Tài liệu thiết kế chi tiết (biểu đồ CSDL, triển khai...)
├── FrontendRequirements/     # Tài liệu yêu cầu nghiệp vụ và use case
├── FRONTEND_PLANNING.md      # Kế hoạch và định hướng tổng thể của dự án Frontend
└── FRONTEND_TASK.md          # Danh sách các công việc cần thực hiện cho Frontend
```

## F. Tài liệu Phân tích & Thiết kế Hệ thống (System Analysis & Design Documents)

### 1. Tài liệu Yêu cầu nghiệp vụ (Requirements Documents)

#### a. Tài liệu mô hình nghiệp vụ (Business Model Documents)

- 'Tài liệu mô hình nghiệp vụ (Word)': Yêu cầu nghiệp vụ theo định dạng Word. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/NaturalLanguage/BusinessModel.docx`](D:/Workspaces/vscode/Thesis/FrontendRequirements/NaturalLanguage/BusinessModel.docx)
- 'Tài liệu mô hình nghiệp vụ (Markdown)': Yêu cầu nghiệp vụ theo định dạng Markdown. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/NaturalLanguage/BusinessModel.md`](D:/Workspaces/vscode/Thesis/FrontendRequirements/NaturalLanguage/BusinessModel.md)

#### b. Biểu đồ Use Case (Use Case Diagrams)

- 'Biểu đồ Use Case Tổng quan': Mô tả tổng quan các chức năng của hệ thống. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_tong_quan.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_tong_quan.png)
- 'Biểu đồ Use Case Chi tiết - Xác thực': Biểu đồ chi tiết cho chức năng đăng ký, đăng nhập, đăng xuất. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_xac_thuc.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_xac_thuc.png)
- 'Biểu đồ Use Case Chi tiết - Tư vấn chế độ dinh dưỡng': Biểu đồ chi tiết cho chức năng tư vấn dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_tu_van_che_do_dinh_duong.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_tu_van_che_do_dinh_duong.png)
- 'Biểu đồ Use Case Chi tiết - Lập kế hoạch dinh dưỡng': Biểu đồ chi tiết cho chức năng lập kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_lap_ke_hoach_dinh_duong.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_lap_ke_hoach_dinh_duong.png)
- 'Biểu đồ Use Case Chi tiết - Theo dõi và đánh giá kế hoạch dinh dưỡng': Biểu đồ chi tiết cho chức năng theo dõi và đánh giá. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_theo_doi_danh_gia.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_theo_doi_danh_gia.png)
- 'Biểu đồ Use Case Chi tiết - Quản lý hồ sơ cá nhân': Biểu đồ chi tiết cho chức năng quản lý hồ sơ cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_quan_ly_ho_so_ca_nhan.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_quan_ly_ho_so_ca_nhan.png)
- 'Biểu đồ Use Case Chi tiết - Chia sẻ kế hoạch và hoạt động dinh dưỡng': Biểu đồ chi tiết cho chức năng chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_chia_se_ke_hoach_hoat_dong.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_chia_se_ke_hoach_hoat_dong.png)
- 'Biểu đồ Use Case Chi tiết - Quản lý thông tin (Admin)': Biểu đồ chi tiết cho chức năng quản trị viên. [`D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_quan_ly_thong_tin_admin.png`](D:/Workspaces/vscode/Thesis/FrontendRequirements/UmlPictures/usecase_quan_ly_thong_tin_admin.png)

### 2. Tài liệu Phân tích (Analysis Documents)

#### a. Tài liệu kịch bản (Scenario Documents)

- 'Tài liệu kịch bản người dùng (Word)': Các kịch bản người dùng theo định dạng Word. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Scenarios/Scenarios.docx`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Scenarios/Scenarios.docx)
- 'Tài liệu kịch bản người dùng (Markdown)': Các kịch bản người dùng theo định dạng Markdown. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Scenarios/Scenarios.md`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Scenarios/Scenarios.md)

#### b. Biểu đồ lớp thực thể pha phân tích (Entity Class Diagrams - Analysis Phase)

- 'Biểu đồ lớp thực thể pha phân tích': Trích xuất từ các kịch bản người dùng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Entity/UmlPictures/entity_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Entity/UmlPictures/entity_class_diagram.png)

#### c. Biểu đồ lớp phân tích (Analysis Class Diagrams)

- 'Biểu đồ lớp phân tích - Xác thực': Biểu đồ lớp phân tích cho module Xác thực. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/xac_thuc_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/xac_thuc_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Tư vấn chế độ dinh dưỡng': Biểu đồ lớp phân tích cho module Tư vấn Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/tu_van_che_do_dinh_duong_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/tu_van_che_do_dinh_duong_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Lập kế hoạch dinh dưỡng': Biểu đồ lớp phân tích cho module Lập kế hoạch Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/lap_ke_hoach_dinh_duong_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/lap_ke_hoach_dinh_duong_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Theo dõi và đánh giá kế hoạch dinh dưỡng': Biểu đồ lớp phân tích cho module Theo dõi và Đánh giá. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/theo_doi_danh_gia_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/theo_doi_danh_gia_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Quản lý hồ sơ cá nhân': Biểu đồ lớp phân tích cho module Quản lý Hồ sơ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Chia sẻ kế hoạch và hoạt động dinh dưỡng': Biểu đồ lớp phân tích cho module Chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_class_diagram.png)
- 'Biểu đồ lớp phân tích - Quản lý thông tin đối tượng (Admin)': Biểu đồ lớp phân tích cho module Quản trị viên. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/quan_ly_thong_tin_admin_analysis_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Class/UmlPictures/quan_ly_thong_tin_admin_analysis_class_diagram.png)

#### d. Biểu đồ trạng thái (State Diagrams)

- 'Biểu đồ trạng thái - Xác thực': Biểu đồ trạng thái cho module Xác thực. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/xac_thuc_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/xac_thuc_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Tư vấn chế độ dinh dưỡng': Biểu đồ trạng thái cho module Tư vấn Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/tu_van_che_do_dinh_duong_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/tu_van_che_do_dinh_duong_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Lập kế hoạch dinh dưỡng': Biểu đồ trạng thái cho module Lập kế hoạch Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/lap_ke_hoach_dinh_duong_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/lap_ke_hoach_dinh_duong_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Theo dõi và đánh giá kế hoạch dinh dưỡng': Biểu đồ trạng thái cho module Theo dõi và Đánh giá. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/theo_doi_danh_gia_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/theo_doi_danh_gia_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Quản lý hồ sơ cá nhân': Biểu đồ trạng thái cho module Quản lý Hồ sơ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/quan_ly_ho_so_ca_nhan_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Chia sẻ kế hoạch và hoạt động dinh dưỡng': Biểu đồ trạng thái cho module Chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/chia_se_ke_hoach_hoat_dong_analysis_state_diagram.png)
- 'Biểu đồ trạng thái - Quản lý thông tin đối tượng (Admin)': Biểu đồ trạng thái cho module Quản trị viên. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/quan_ly_thong_tin_admin_analysis_state_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/State/UmlPictures/quan_ly_thong_tin_admin_analysis_state_diagram.png)

#### e. Biểu đồ tuần tự (Sequence Diagrams - Analysis Phase)

- 'Biểu đồ tuần tự - Đăng ký': Biểu đồ tuần tự cho chức năng đăng ký. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_ky_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_ky_sequence_diagram.png)
- 'Biểu đồ tuần tự - Đăng nhập': Biểu đồ tuần tự cho chức năng đăng nhập. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_nhap_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_nhap_sequence_diagram.png)
- 'Biểu đồ tuần tự - Đăng xuất': Biểu đồ tuần tự cho chức năng đăng xuất. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_xuat_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/dang_xuat_sequence_diagram.png)
- 'Biểu đồ tuần tự - Gửi thông tin tư vấn': Biểu đồ tuần tự cho chức năng gửi thông tin tư vấn dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/gui_thong_tin_tu_van_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/gui_thong_tin_tu_van_sequence_diagram.png)
- 'Biểu đồ tuần tự - Yêu cầu chỉnh sửa thực đơn': Biểu đồ tuần tự cho chức năng yêu cầu chỉnh sửa thực đơn. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/yeu_cau_chinh_sua_thuc_don_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/yeu_cau_chinh_sua_thuc_don_sequence_diagram.png)
- 'Biểu đồ tuần tự - Chọn kiểu kế hoạch': Biểu đồ tuần tự cho chức năng chọn kiểu kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/chon_kieu_ke_hoach_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/chon_kieu_ke_hoach_sequence_diagram.png)
- 'Biểu đồ tuần tự - Lưu kế hoạch': Biểu đồ tuần tự cho chức năng lưu kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/luu_ke_hoach_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/luu_ke_hoach_sequence_diagram.png)
- 'Biểu đồ tuần tự - Nhập dữ liệu hàng ngày': Biểu đồ tuần tự cho chức năng nhập dữ liệu hàng ngày. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/nhap_du_lieu_hang_ngay_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/nhap_du_lieu_hang_ngay_sequence_diagram.png)
- 'Biểu đồ tuần tự - Lấy báo cáo tiến độ': Biểu đồ tuần tự cho chức năng lấy báo cáo tiến độ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/lay_bao_cao_tien_do_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/lay_bao_cao_tien_do_sequence_diagram.png)
- 'Biểu đồ tuần tự - Xem hồ sơ': Biểu đồ tuần tự cho chức năng xem hồ sơ cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xem_ho_so_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xem_ho_so_sequence_diagram.png)
- 'Biểu đồ tuần tự - Cập nhật sinh trắc học': Biểu đồ tuần tự cho chức năng cập nhật thông tin sinh trắc học. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_sinh_trac_hoc_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_sinh_trac_hoc_sequence_diagram.png)
- 'Biểu đồ tuần tự - Cập nhật mục tiêu sức khỏe': Biểu đồ tuần tự cho chức năng cập nhật mục tiêu sức khỏe. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_muc_tieu_suc_khoe_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_muc_tieu_suc_khoe_sequence_diagram.png)
- 'Biểu đồ tuần tự - Cập nhật sở thích ăn uống': Biểu đồ tuần tự cho chức năng cập nhật sở thích ăn uống. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_so_thich_an_uong_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/cap_nhat_so_thich_an_uong_sequence_diagram.png)
- 'Biểu đồ tuần tự - Thay đổi chế độ chia sẻ': Biểu đồ tuần tự cho chức năng thay đổi chế độ chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/thay_doi_che_do_chia_se_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/thay_doi_che_do_chia_se_sequence_diagram.png)
- 'Biểu đồ tuần tự - Xem nội dung chia sẻ': Biểu đồ tuần tự cho chức năng xem nội dung chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xem_noi_dung_chia_se_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xem_noi_dung_chia_se_sequence_diagram.png)
- 'Biểu đồ tuần tự - Thích nội dung chia sẻ': Biểu đồ tuần tự cho chức năng thích nội dung chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/thich_noi_dung_chia_se_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/thich_noi_dung_chia_se_sequence_diagram.png)
- 'Biểu đồ tuần tự - Thêm bình luận': Biểu đồ tuần tự cho chức năng thêm bình luận. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/them_binh_luan_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/them_binh_luan_sequence_diagram.png)
- 'Biểu đồ tuần tự - Lưu nội dung chia sẻ': Biểu đồ tuần tự cho chức năng lưu nội dung chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/luu_noi_dung_chia_se_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/luu_noi_dung_chia_se_sequence_diagram.png)
- 'Biểu đồ tuần tự - Đề xuất người dùng/nhóm': Biểu đồ tuần tự cho chức năng đề xuất người dùng/nhóm. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/de_xuat_nguoi_dung_nhom_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/de_xuat_nguoi_dung_nhom_sequence_diagram.png)
- 'Biểu đồ tuần tự - Quản lý người dùng (Thêm)': Biểu đồ tuần tự cho chức năng quản lý người dùng (thêm). [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/quan_ly_nguoi_dung_them_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/quan_ly_nguoi_dung_them_sequence_diagram.png)
- 'Biểu đồ tuần tự - Quản lý món ăn (Thêm)': Biểu đồ tuần tự cho chức năng quản lý món ăn (thêm). [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/quan_ly_mon_an_them_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/quan_ly_mon_an_them_sequence_diagram.png)
- 'Biểu đồ tuần tự - Kích hoạt huấn luyện lại AI': Biểu đồ tuần tự cho chức năng kích hoạt huấn luyện lại AI. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/kich_hoat_huan_luyen_lai_ai_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/kich_hoat_huan_luyen_lai_ai_sequence_diagram.png)
- 'Biểu đồ tuần tự - Sửa đối tượng': Biểu đồ tuần tự cho chức năng sửa đối tượng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/sua_doi_tuong_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/sua_doi_tuong_sequence_diagram.png)
- 'Biểu đồ tuần tự - Xóa đối tượng': Biểu đồ tuần tự cho chức năng xóa đối tượng. [`D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xoa_doi_tuong_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendAnalysis/Sequence/UmlPictures/xoa_doi_tuong_sequence_diagram.png)

### 3. Tài liệu Thiết kế (Design Documents)

#### a. Biểu đồ lớp thực thể pha thiết kế (Entity Class Diagrams - Design Phase)

- 'Biểu đồ lớp thực thể pha thiết kế': Trích xuất từ biểu đồ lớp thực thể pha phân tích. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Entity/UmlPictures/entity_class_diagram_design.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Entity/UmlPictures/entity_class_diagram_design.png)

#### b. Biểu đồ quan hệ thực thể CSDL (Entity - Relationship Database Diagrams)

- 'Biểu đồ lớp thực thể cho cơ sở dữ liệu': Biểu đồ thiết kế schema cơ sở dữ liệu. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Database/UmlPictures/database_schema_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Database/UmlPictures/database_schema_diagram.png)

#### c. Biểu đồ lớp thiết kế (Design Class Diagrams)

- 'Biểu đồ lớp thiết kế - Quản trị viên': Biểu đồ lớp thiết kế cho module quản lý thông tin (Admin). [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/admin_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/admin_design_class_diagram.png)
- 'Biểu đồ lớp thiết kế - Xác thực': Biểu đồ lớp thiết kế cho module Xác thực. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/xac_thuc_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/xac_thuc_design_class_diagram.png)
- 'Biểu đồ lớp thiết kế - Chế độ dinh dưỡng': Biểu đồ lớp thiết kế cho module chế độ dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/che_do_dinh_duong_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/che_do_dinh_duong_design_class_diagram.png)
- 'Biểu đồ lớp thực thể thiết kế - Tư vấn chế độ dinh dưỡng': Biểu đồ lớp thực thể thiết kế cho module tư vấn chế độ dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_entity_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_entity_class_diagram.png)
- 'Biểu đồ lớp giao diện thiết kế - Tư vấn chế độ dinh dưỡng': Biểu đồ lớp giao diện thiết kế cho module tư vấn chế độ dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_design_class_diagram.png)
- 'Biểu đồ lớp DAO thiết kế - Tư vấn chế độ dinh dưỡng': Biểu đồ lớp DAO thiết kế cho module tư vấn chế độ dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_dao_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/nutritional_consultation_dao_class_diagram.png)
- 'Biểu đồ lớp thiết kế - Theo dõi và đánh giá': Biểu đồ lớp thiết kế cho module Theo dõi và Đánh giá Kế hoạch Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/theo_doi_danh_gia_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/theo_doi_danh_gia_design_class_diagram.png)
- 'Biểu đồ lớp thiết kế - Quản lý hồ sơ cá nhân': Biểu đồ lớp thiết kế cho module Quản lý Hồ sơ Cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/profile_management_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/profile_management_design_class_diagram.png)
- 'Biểu đồ lớp thiết kế - Chia sẻ kế hoạch và hoạt động': Biểu đồ lớp thiết kế cho module Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/share_design_class_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/UmlPictures/share_design_class_diagram.png)

#### d. Biểu đồ Triển khai (Deployment Diagrams)

- 'Biểu đồ triển khai hệ thống': Biểu đồ hiển thị kiến trúc triển khai các thành phần của hệ thống. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Deployment/UmlPictures/deployment_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Deployment/UmlPictures/deployment_diagram.png)

#### e. Biểu đồ hoạt động (Activity Diagrams)

- 'Biểu đồ hoạt động - Đăng nhập': Biểu đồ hoạt động cho chức năng Đăng nhập. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/login_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/login_activity_diagram.png)
- 'Biểu đồ hoạt động - Đăng ký': Biểu đồ hoạt động cho chức năng Đăng ký. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/register_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/register_activity_diagram.png)
- 'Biểu đồ hoạt động - Quản lý người dùng': Biểu đồ hoạt động cho chức năng Quản lý người dùng (Admin). [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/user_management_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/user_management_activity_diagram.png)
- 'Biểu đồ hoạt động - Quản lý nội dung chia sẻ': Biểu đồ hoạt động cho chức năng Quản lý nội dung chia sẻ (Admin). [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/shared_content_management_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/shared_content_management_activity_diagram.png)
- 'Biểu đồ hoạt động - Tư vấn dinh dưỡng': Biểu đồ hoạt động cho chức năng Tư vấn dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_consultation_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_consultation_activity_diagram.png)
- 'Biểu đồ hoạt động - Tìm kiếm món ăn': Biểu đồ hoạt động cho chức năng Tìm kiếm món ăn và thực phẩm. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/food_search_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/food_search_activity_diagram.png)
- 'Biểu đồ hoạt động - Nhập nhu cầu dinh dưỡng': Biểu đồ hoạt động cho chức năng Nhập thông tin nhu cầu dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_needs_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/nutritional_needs_activity_diagram.png)
- 'Biểu đồ hoạt động - Tạo kế hoạch dinh dưỡng': Biểu đồ hoạt động cho chức năng Tạo kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/create_nutrition_plan_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/create_nutrition_plan_activity_diagram.png)
- 'Biểu đồ hoạt động - Xem/Cập nhật kế hoạch dinh dưỡng': Biểu đồ hoạt động cho chức năng Xem và Cập nhật kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/view_update_nutrition_plan_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/view_update_nutrition_plan_activity_diagram.png)
- 'Biểu đồ hoạt động - Xóa kế hoạch dinh dưỡng': Biểu đồ hoạt động cho chức năng Xóa kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/delete_nutrition_plan_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/delete_nutrition_plan_activity_diagram.png)
- 'Biểu đồ hoạt động - Nhập dữ liệu hàng ngày': Biểu đồ hoạt động cho chức năng Nhập liệu dữ liệu hàng ngày. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/daily_log_input_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/daily_log_input_activity_diagram.png)
- 'Biểu đồ hoạt động - Báo cáo tiến độ': Biểu đồ hoạt động cho chức năng Báo cáo tiến độ và đánh giá. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/progress_report_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/progress_report_activity_diagram.png)
- 'Biểu đồ hoạt động - Quản lý hồ sơ cá nhân': Biểu đồ hoạt động cho chức năng Quản lý hồ sơ cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/profile_management_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/profile_management_activity_diagram.png)
- 'Biểu đồ hoạt động - Chia sẻ kế hoạch và hoạt động': Biểu đồ hoạt động cho chức năng Chia sẻ kế hoạch và hoạt động. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/share_plan_activity_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Activity/UmlPictures/share_plan_activity_diagram.png)

#### f. Tài liệu kịch bản người dùng (Scenario Documents - Version 3)

- 'Tài liệu kịch bản người dùng (phiên bản 3 - Word)': Các kịch bản người dùng (phiên bản 3) theo định dạng Word. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Scenarios/Scenarios_v3.docx`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Scenarios/Scenarios_v3.docx)
- 'Tài liệu kịch bản người dùng (phiên bản 3 - Markdown)': Các kịch bản người dùng (phiên bản 3) theo định dạng Markdown. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Scenarios/Scenarios_v3.md`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Scenarios/Scenarios_v3.md)

#### g. Biểu đồ tuần tự (Sequence Diagrams - Design Phase)

- 'Biểu đồ tuần tự - Đăng ký': Biểu đồ tuần tự cho chức năng đăng ký. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/register_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/register_sequence_diagram.png)
- 'Biểu đồ tuần tự - Đăng nhập': Biểu đồ tuần tự cho chức năng đăng nhập. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/login_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/login_sequence_diagram.png)
- 'Biểu đồ tuần tự - Quản lý người dùng': Biểu đồ tuần tự cho chức năng Quản lý người dùng (Admin). [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/user_management_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/user_management_sequence_diagram.png)
- 'Biểu đồ tuần tự - Quản lý nội dung chia sẻ': Biểu đồ tuần tự cho chức năng Quản lý nội dung chia sẻ (Admin). [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/shared_content_management_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/shared_content_management_sequence_diagram.png)
- 'Biểu đồ tuần tự - Tư vấn dinh dưỡng': Biểu đồ tuần tự cho chức năng Tư vấn dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_consultation_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_consultation_sequence_diagram.png)
- 'Biểu đồ tuần tự - Tìm kiếm món ăn': Biểu đồ tuần tự cho chức năng Tìm kiếm món ăn và thực phẩm. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/food_search_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/food_search_sequence_diagram.png)
- 'Biểu đồ tuần tự - Nhập nhu cầu dinh dưỡng': Biểu đồ tuần tự cho chức năng Nhập thông tin nhu cầu dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_needs_input_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/nutritional_needs_input_sequence_diagram.png)
- 'Biểu đồ tuần tự - Tạo kế hoạch dinh dưỡng': Biểu đồ tuần tự cho chức năng Tạo kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/create_nutrition_plan_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/create_nutrition_plan_sequence_diagram.png)
- 'Biểu đồ tuần tự - Xem/Cập nhật kế hoạch dinh dưỡng': Biểu đồ tuần tự cho chức năng Xem và Cập nhật kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/view_update_nutrition_plan_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/view_update_nutrition_plan_sequence_diagram.png)
- 'Biểu đồ tuần tự - Xóa kế hoạch dinh dưỡng': Biểu đồ tuần tự cho chức năng Xóa kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/delete_nutrition_plan_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/delete_nutrition_plan_sequence_diagram.png)
- 'Biểu đồ tuần tự - Nhập dữ liệu hàng ngày': Biểu đồ tuần tự cho chức năng Nhập liệu dữ liệu hàng ngày. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/daily_log_input_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/daily_log_input_sequence_diagram.png)
- 'Biểu đồ tuần tự - Báo cáo tiến độ': Biểu đồ tuần tự cho chức năng Báo cáo tiến độ và đánh giá. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/progress_report_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/progress_report_sequence_diagram.png)
- 'Biểu đồ tuần tự - Quản lý hồ sơ cá nhân': Biểu đồ tuần tự cho chức năng Quản lý hồ sơ cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/profile_management_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/profile_management_sequence_diagram.png)
- 'Biểu đồ tuần tự - Chia sẻ kế hoạch và hoạt động': Biểu đồ tuần tự cho chức năng Chia sẻ kế hoạch và hoạt động. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/share_plan_activity_sequence_diagram.png`](D:/Workspaces/vscode/Thesis/FrontendDesign/Dynamic/Sequence/UmlPictures/share_plan_activity_sequence_diagram.png)

#### h. Giao diện Tĩnh (Static Interfaces)

- 'Giao diện trang Đăng nhập': Giao diện trang Đăng nhập. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/LoginPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/LoginPage.html)
- 'Giao diện trang Đăng ký': Giao diện trang Đăng ký tài khoản mới. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/RegisterPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/RegisterPage.html)
- 'Giao diện bảng điều khiển quản trị viên': Trang bảng điều khiển quản trị viên. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/AdminDashboardPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/AdminDashboardPage.html)
- 'Giao diện quản lý người dùng': Trang quản lý người dùng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/UserManagementPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/UserManagementPage.html)
- 'Giao diện quản lý nội dung chia sẻ': Trang quản lý nội dung chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/SharedContentManagementPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/SharedContentManagementPage.html)
- 'Giao diện tư vấn chế độ dinh dưỡng': Trang tư vấn chế độ dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionalConsultationPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionalConsultationPage.html)
- 'Giao diện tìm kiếm món ăn và thực phẩm': Trang tìm kiếm thực phẩm. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/FoodSearchPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/FoodSearchPage.html)
- 'Giao diện nhập thông tin nhu cầu dinh dưỡng': Trang nhập liệu nhu cầu dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionalNeedsInputPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionalNeedsInputPage.html)
- 'Giao diện hiển thị kế hoạch dinh dưỡng': Trang hiển thị kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionPlanPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionPlanPage.html)
- 'Giao diện chi tiết kế hoạch dinh dưỡng': Trang chi tiết kế hoạch dinh dưỡng. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionPlanDetailPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/NutritionPlanDetailPage.html)
- 'Giao diện nhập liệu nhật ký hàng ngày': Trang nhập liệu nhật ký hàng ngày. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/DailyLogInputPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/DailyLogInputPage.html)
- 'Giao diện báo cáo tiến độ': Trang báo cáo tiến độ. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ProgressReportPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ProgressReportPage.html)
- 'Giao diện quản lý hồ sơ cá nhân': Trang quản lý hồ sơ cá nhân. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ProfileManagementPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ProfileManagementPage.html)
- 'Giao diện chia sẻ kế hoạch và hoạt động': Trang chia sẻ kế hoạch và hoạt động. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/SharePlanActivityPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/SharePlanActivityPage.html)
- 'Giao diện xem nội dung chia sẻ': Trang xem nội dung chia sẻ. [`D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ViewSharedContentPage.html`](D:/Workspaces/vscode/Thesis/FrontendDesign/Static/Html/ViewSharedContentPage.html)

## G. Hướng dẫn cài đặt và chạy

(Chưa cập nhật)

## H. Tài liệu API

(Chưa cập nhật)

## K. Kiểm thử (Testing)

(Chưa cập nhật)
