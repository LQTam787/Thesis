# Kịch bản Kiểm thử Tích hợp cho các Luồng Nghiệp vụ Chính

## 1. Mục tiêu

Tài liệu này mở rộng kế hoạch kiểm thử tích hợp ban đầu, bao quát toàn diện các luồng nghiệp vụ chính của hệ thống Tư vấn Dinh dưỡng AI. Mục tiêu là xác minh sự tương tác liền mạch và tính toàn vẹn dữ liệu giữa các thành phần: Controllers, Services, Repositories, Cơ sở dữ liệu (MySQL), và Dịch vụ AI (Python/Flask).

## 2. Các Luồng Nghiệp vụ Cần Kiểm thử

### 2.1. Luồng Xác thực và Quản lý Người dùng (Authentication & User Management)

- **Kịch bản 2.1.1: Đăng ký người dùng mới thành công**
  - **Mô tả:** Người dùng cung cấp thông tin hợp lệ để tạo tài khoản.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/auth/register` với body chứa `username`, `email`, `password` hợp lệ.
  - **Kết quả mong đợi:** HTTP status `201 CREATED`, response body chứa thông tin người dùng đã tạo (không có mật khẩu), và một bản ghi mới trong bảng `users`.

- **Kịch bản 2.1.2: Đăng nhập thành công**
  - **Mô tả:** Người dùng đã có tài khoản đăng nhập vào hệ thống.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/auth/login` với `username` và `password` chính xác.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa thông báo thành công và JWT token.

- **Kịch bản 2.1.3: Người dùng xem thông tin hồ sơ cá nhân**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `GET` đến `/api/users/{username}` với `username` của người dùng.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa thông tin chi tiết trong `ProfileDto`.

- **Kịch bản 2.1.4: Người dùng cập nhật hồ sơ cá nhân**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `PUT` đến `/api/users/{username}` với body chứa các thông tin cần cập nhật trong `ProfileDto`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa thông tin hồ sơ đã được cập nhật, và dữ liệu trong CSDL được thay đổi tương ứng.

### 2.2. Luồng Quản lý Kế hoạch Dinh dưỡng (Nutrition Plan Management)

- **Kịch bản 2.2.1: Người dùng tạo kế hoạch dinh dưỡng mới**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/users/{username}/nutrition-plans` với body chứa thông tin kế hoạch (`NutritionPlanDto`).
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa thông tin kế hoạch vừa tạo, và một bản ghi mới trong bảng `nutrition_plan` được liên kết với người dùng.

- **Kịch bản 2.2.2: Người dùng xem danh sách kế hoạch dinh dưỡng của mình**
  - **Điều kiện:** Người dùng đã đăng nhập và đã tạo ít nhất một kế hoạch.
  - **Các bước:** Gửi yêu cầu `GET` đến `/api/users/{username}/nutrition-plans`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body là một danh sách các `NutritionPlanDto` thuộc về người dùng.

### 2.3. Luồng Theo dõi Tiến trình (Progress Tracking)

- **Kịch bản 2.3.1: Người dùng ghi lại nhật ký thực phẩm**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/v1/tracking/food/{userId}` với body chứa thông tin `FoodLogDto`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa thông tin `FoodLogDto` đã được lưu, và một bản ghi mới trong bảng `food_log`.

- **Kịch bản 2.3.2: Người dùng ghi lại nhật ký hoạt động**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/v1/tracking/activity/{userId}` với body chứa thông tin `ActivityLogDto`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa `ActivityLogDto` đã lưu, và một bản ghi mới trong bảng `activity_log`.

- **Kịch bản 2.3.3: Người dùng xem báo cáo tiến trình**
  - **Điều kiện:** Người dùng đã đăng nhập và có dữ liệu nhật ký.
  - **Các bước:** Gửi yêu cầu `GET` đến `/api/v1/tracking/report/{userId}` với query param `type=weekly`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa `ProgressReportDto` tổng hợp dữ liệu theo tuần.

### 2.4. Luồng Chia sẻ Cộng đồng (Social Sharing)

- **Kịch bản 2.4.1: Người dùng chia sẻ một nội dung**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/share` với body chứa `contentId`, `contentType`, và `visibility`.
  - **Kết quả mong đợi:** HTTP status `201 CREATED`, response body chứa `SharedContentDto` đã tạo.

- **Kịch bản 2.4.2: Người dùng thích một nội dung được chia sẻ**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/share/{contentId}/like`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, và một bản ghi mới được tạo trong bảng `content_like`.

- **Kịch bản 2.4.3: Người dùng bình luận về một nội dung**
  - **Điều kiện:** Người dùng đã đăng nhập.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/share/{contentId}/comment` với body chứa nội dung bình luận (`text`).
  - **Kết quả mong đợi:** HTTP status `201 CREATED`, response body chứa `ContentCommentDto` đã tạo.

### 2.5. Luồng Chức năng của Quản trị viên (Admin Functions)

- **Kịch bản 2.5.1: Admin truy cập danh sách người dùng (Truy cập thành công)**
  - **Điều kiện:** Người dùng đăng nhập với vai trò `ADMIN`.
  - **Các bước:** Gửi yêu cầu `GET` đến `/api/admin/users` với JWT của admin.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa danh sách tất cả người dùng.

- **Kịch bản 2.5.2: Người dùng thường cố gắng truy cập tài nguyên của Admin (Truy cập thất bại)**
  - **Điều kiện:** Người dùng đăng nhập với vai trò `USER`.
  - **Các bước:** Gửi yêu cầu `GET` đến `/api/admin/users` với JWT của user.
  - **Kết quả mong đợi:** HTTP status `403 Forbidden`.

- **Kịch bản 2.5.3: Admin tạo một công thức nấu ăn mới**
  - **Điều kiện:** Người dùng đăng nhập với vai trò `ADMIN`.
  - **Các bước:** Gửi yêu cầu `POST` đến `/api/admin/recipes` với body chứa `RecipeRequestDto`.
  - **Kết quả mong đợi:** HTTP status `200 OK`, response body chứa `RecipeDto` đã tạo, và một bản ghi mới trong bảng `recipe`.
