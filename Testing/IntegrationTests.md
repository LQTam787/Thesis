# Kế hoạch Kiểm thử Tích hợp (Integration Test Plan)

## 1. Mục tiêu

Kiểm thử này nhằm mục đích xác minh sự tương tác và luồng dữ liệu chính xác giữa các thành phần khác nhau của hệ thống, bao gồm:

- **API Endpoints (Controller)** và **Lớp Dịch vụ (Service)**.
- **Lớp Dịch vụ (Service)** và **Lớp Truy cập Dữ liệu (Repository)**.
- **Backend (Spring Boot)** và **Cơ sở dữ liệu (MySQL)**.
- **Backend (Spring Boot)** và **Dịch vụ AI (Python/Flask)**.

## 2. Các Luồng Nghiệp vụ Chính Cần Kiểm thử

### 2.1. Luồng Xác thực và Phân quyền (Authentication & Authorization Flow)

- **Kịch bản 2.1.1: Đăng ký người dùng mới thành công**
  - **Mô tả:** Một người dùng mới cung cấp thông tin hợp lệ và đăng ký tài khoản thành công.
  - **Các bước:**
    1. Gửi yêu cầu POST đến `/api/auth/signup` với thông tin (username, email, password) hợp lệ.
    2. Hệ thống kiểm tra thông tin.
    3. Hệ thống lưu người dùng mới vào cơ sở dữ liệu với vai trò `USER`.
  - **Kết quả mong đợi:**
    - API trả về HTTP status `200 OK`.
    - Response body chứa thông báo đăng ký thành công.
    - Một bản ghi người dùng mới xuất hiện trong bảng `users` của CSDL.

- **Kịch bản 2.1.2: Đăng nhập thành công và nhận JWT**
  - **Mô tả:** Người dùng đã đăng ký đăng nhập với thông tin chính xác.
  - **Các bước:**
    1. Gửi yêu cầu POST đến `/api/auth/signin` với username và password chính xác.
    2. Hệ thống xác thực thông tin.
  - **Kết quả mong đợi:**
    - API trả về HTTP status `200 OK`.
    - Response body chứa JWT (JSON Web Token).

- **Kịch bản 2.1.3: Truy cập tài nguyên được bảo vệ với JWT hợp lệ**
  - **Mô tả:** Người dùng đã đăng nhập sử dụng JWT để truy cập một endpoint yêu cầu xác thực.
  - **Các bước:**
    1. Gửi yêu cầu GET đến `/api/user/me` với header `Authorization: Bearer <JWT>`.
  - **Kết quả mong đợi:**
    - API trả về HTTP status `200 OK`.
    - Response body chứa thông tin của người dùng đã đăng nhập.

- **Kịch bản 2.1.4: Truy cập tài nguyên được bảo vệ với JWT không hợp lệ/hết hạn**
  - **Mô tả:** Người dùng cố gắng truy cập tài nguyên với JWT không hợp lệ.
  - **Các bước:**
    1. Gửi yêu cầu GET đến `/api/user/me` với JWT không hợp lệ.
  - **Kết quả mong đợi:**
    - API trả về HTTP status `401 Unauthorized`.

### 2.2. Luồng Quản lý Kế hoạch Dinh dưỡng (Nutrition Plan Management Flow)

- **Kịch bản 2.2.1: Người dùng tạo kế hoạch dinh dưỡng mới**
  - **Mô tả:** Người dùng đã đăng nhập tạo một kế hoạch dinh dưỡng mới cho mình.
  - **Các bước:**
    1. Đăng nhập để nhận JWT.
    2. Gửi yêu cầu POST đến `/api/plans` với JWT hợp lệ và thông tin chi tiết về kế hoạch (tên, mục tiêu, ngày bắt đầu, ngày kết thúc).
  - **Kết quả mong đợi:**
    - API trả về HTTP status `201 Created`.
    - Response body chứa thông tin kế hoạch vừa tạo.
    - Một bản ghi kế hoạch mới được lưu trong CSDL và liên kết với đúng người dùng.

### 2.3. Luồng Tương tác với Dịch vụ AI (AI Service Interaction Flow)

- **Kịch bản 2.3.1: Backend gọi đến AI Service để nhận gợi ý thực đơn**
  - **Mô tả:** Khi người dùng yêu cầu gợi ý, backend sẽ gọi đến AI service để xử lý và trả về kết quả.
  - **Các bước:**
    1. Đăng nhập để nhận JWT.
    2. Gửi yêu cầu POST đến một endpoint của backend (ví dụ: `/api/suggestion/recipe`) với các yêu cầu (ví dụ: "cần một món ăn sáng ít calo").
    3. Backend service gọi đến endpoint `/recommend` của AI service.
    4. AI service xử lý yêu cầu và trả về danh sách gợi ý.
    5. Backend trả kết quả về cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về HTTP status `200 OK`.
    - Response body chứa danh sách các công thức/món ăn phù hợp từ AI service.

## 3. Môi trường và Công cụ Kiểm thử

- **Framework:** Spring Boot Test, MockMvc, JUnit 5, AssertJ.
- **Cơ sở dữ liệu:** H2 (In-memory database) cho môi trường kiểm thử để đảm bảo tính cô lập và tốc độ.
- **Mocking:** Mockito để giả lập các dependency ngoại vi (nếu cần), đặc biệt là các cuộc gọi đến AI service trong một số kịch bản.
- **HTTP Client:** RestAssured hoặc `TestRestTemplate` để thực hiện các cuộc gọi API trong môi trường kiểm thử.

## 4. Dữ liệu Kiểm thử (Test Data)

- Sẽ sử dụng các script SQL hoặc các lớp `@Configuration` để khởi tạo dữ liệu mẫu trước khi chạy các bài kiểm thử. Ví dụ: tạo sẵn một vài người dùng với các vai trò khác nhau, một số công thức nấu ăn, v.v.
