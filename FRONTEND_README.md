# Frontend (ReactJS) Project

## 1. Tổng quan

Dự án này là một ứng dụng ReactJS phục vụ nhu cầu lập kế hoạch, thực hiện và theo dõi chế độ dinh dưỡng một cách hiệu quả, thông minh và được cá nhân hóa.

## 2. Phân tích Thiết kế (UML)

Các biểu đồ Use Case mô tả chức năng của hệ thống từ góc nhìn của người dùng đã được tạo và lưu trữ trong thư mục `FrontendRequirements/Uml/`.
Biểu đồ lớp thực thể pha phân tích đã được tạo và lưu trữ tại `FrontendAnalysis/Entity/Uml/entity_class_diagram.puml`.
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
