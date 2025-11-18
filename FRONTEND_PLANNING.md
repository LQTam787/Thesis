# Kế hoạch Phát triển Frontend - Hệ thống Tư vấn Dinh dưỡng AI

## A. Định hướng Bậc cao

Mục tiêu của frontend là tạo ra một giao diện người dùng (UI) **trực quan**, **thân thiện** và **phản hồi nhanh**, đóng vai trò là cầu nối hiệu quả giữa người dùng và các dịch vụ backend/AI phức tạp. Thiết kế phải nhấn mạnh trải nghiệm **chatbox thông minh** và khả năng **trực quan hóa dữ liệu** tiến độ.

* **Tính năng cốt lõi**: Đảm bảo các chức năng chính như Đăng nhập/Đăng ký, Tư vấn Dinh dưỡng (Chatbot), Nhận dạng Món ăn và Theo dõi Tiến độ hoạt động mượt mà.
* **Công nghệ**: Sử dụng ReactJS để xây dựng giao diện dựa trên Component, giúp dễ bảo trì và mở rộng.
* **Tích hợp**: Phải đảm bảo tích hợp thông suốt với Backend Spring Boot (qua REST API, sử dụng JWT cho Spring Security) và Dịch vụ AI/ML (qua API riêng cho các tác vụ Vision, NLP).

## B. Tầm nhìn Thiết kế (Design Vision)

### 1. Nguyên tắc Thiết kế
* **Mobile-First/Responsive**: Ưu tiên thiết kế cho màn hình di động trước do tính chất ứng dụng theo dõi hàng ngày, sau đó mở rộng cho giao diện web.
* **Thân thiện và Tương tác**: Sử dụng các Component (thẻ, biểu đồ, hình ảnh) rõ ràng để giảm gánh nặng nhận thức. Giao diện chatbox phải là điểm nhấn.
* **Trực quan hóa Dữ liệu**: Sử dụng các thư viện biểu đồ mạnh mẽ để biến dữ liệu thô (calo, macro, tiến độ) thành thông tin dễ hiểu.

### 2. Các Modules Giao diện Chính (Dựa trên BusinessModel.md)
| Module | Mô tả trọng tâm | Công nghệ Tích hợp |
| :--- | :--- | :--- |
| **Xác thực** | Đăng ký, Đăng nhập (sử dụng JWT) | Backend Spring Boot (Spring Security) |
| **Hồ sơ Cá nhân** | Form nhập liệu sinh trắc học, mục tiêu, sở thích ăn uống. | Backend Spring Boot (CRUD User Data) |
| **Tư vấn Dinh dưỡng** | Giao diện Chatbot với AI. Phải có khả năng gửi văn bản và nhận phản hồi cấu trúc. | AI/ML Processing Layer (NLP Service) |
| **Theo dõi & Đánh giá** | Ghi nhật ký (Manual, **Image Upload**), hiển thị Biểu đồ Tiến độ, Báo cáo Dinh dưỡng. | Backend Spring Boot (Log CRUD), AI/ML Processing Layer (Vision Service) |
| **Lập Kế hoạch** | Hiển thị và quản lý các Kế hoạch Dinh dưỡng/Công thức, tính toán Calo/Macro trực tiếp trên UI. | Backend Spring Boot (Plan & Recipe CRUD) |
| **Cộng đồng/Chia sẻ** | Hiển thị Feed (danh sách bài chia sẻ), Tương tác (Thích, Bình luận). | Backend Spring Boot (Social/Share CRUD) |
| **Quản trị viên (Admin)** | Dashboard đơn giản để quản lý Người dùng/Dữ liệu (Food/Recipe). | Backend Spring Boot (Admin APIs) |

## C. Công nghệ Sử dụng cho Frontend

| Hạng mục | Công nghệ | Mục đích |
| :--- | :--- | :--- |
| **Framework** | ReactJS (hoặc Next.js/Gatsby nếu cần SSR) | Xây dựng giao diện dựa trên Component |
| **Quản lý State** | Redux Toolkit (hoặc Context API + Hooks) | Quản lý trạng thái ứng dụng phức tạp (User, Plan, Logs) |
| **Giao diện/Styling** | Tailwind CSS / Material UI / Bootstrap | Xây dựng UI nhanh chóng, đảm bảo Responsive |
| **Biểu đồ Dữ liệu** | Chart.js / Recharts | Trực quan hóa tiến độ (cân nặng, calo, macro) |
| **Quản lý API** | Axios (hoặc Fetch API) | Gọi API đến Backend Spring Boot và AI Service |
| **Build Tool** | Vite / Create React App | Khởi tạo và tối ưu hóa dự án |