# Integration Test: Luồng Xác thực (Authentication Flow)

## 1. Mục tiêu

Kiểm thử này xác minh các chức năng cốt lõi của luồng xác thực, bao gồm đăng ký người dùng, đăng nhập và xử lý các trường hợp lỗi liên quan. Các kịch bản được thiết kế dựa trên mã nguồn hiện tại.

**Lưu ý:** Hệ thống hiện tại chưa tích hợp JWT và các endpoint chưa được bảo vệ bằng Spring Security. Các kịch bản liên quan đến truy cập tài nguyên bằng token sẽ được cập nhật sau khi các tính năng đó được triển khai.

---

## 2. Các Kịch bản Kiểm thử (Test Cases)

### Kịch bản 1: Đăng ký người dùng mới thành công

- **ID:** `AUTH_TC01`
- **Mô tả:** Người dùng cung cấp thông tin hợp lệ và đăng ký tài khoản thành công.
- **Các bước thực hiện:**
  1. Chuẩn bị một đối tượng `UserDto` với `username`, `email`, và `password` hợp lệ và chưa tồn tại trong hệ thống.
  2. Gửi yêu cầu `POST` đến endpoint `/api/auth/register` với body là đối tượng `UserDto` đã chuẩn bị.
- **Kết quả mong đợi:**
  - Hệ thống trả về HTTP status `201 Created`.
  - Response body chứa thông tin của người dùng vừa được tạo (không bao gồm mật khẩu).
  - Một bản ghi người dùng mới được tạo trong bảng `users` của cơ sở dữ liệu.

### Kịch bản 2: Đăng ký người dùng với username đã tồn tại

- **ID:** `AUTH_TC02`
- **Mô tả:** Người dùng cố gắng đăng ký bằng một `username` đã được sử dụng.
- **Điều kiện tiên quyết:** Tồn tại một người dùng với `username` là `existinguser` trong CSDL.
- **Các bước thực hiện:**
  1. Chuẩn bị một đối tượng `UserDto` với `username` là `existinguser`.
  2. Gửi yêu cầu `POST` đến endpoint `/api/auth/register`.
- **Kết quả mong đợi:**
  - Hệ thống trả về HTTP status `400 Bad Request`.
  - Response body chứa thông báo lỗi cho biết username đã tồn tại.

### Kịch bản 3: Đăng nhập thành công

- **ID:** `AUTH_TC03`
- **Mô tả:** Người dùng đã đăng ký đăng nhập với thông tin chính xác.
- **Điều kiện tiên quyết:** Tồn tại một người dùng với `username` là `testuser` và `password` là `password123`.
- **Các bước thực hiện:**
  1. Chuẩn bị một đối tượng `LoginDto` với `username: "testuser"` và `password: "password123"`.
  2. Gửi yêu cầu `POST` đến endpoint `/api/auth/login`.
- **Kết quả mong đợi:**
  - Hệ thống trả về HTTP status `200 OK`.
  - Response body chứa chuỗi `"User authenticated successfully. JWT token would be here."`.
  - **(Tương lai):** Response body sẽ chứa một JWT token hợp lệ.

### Kịch bản 4: Đăng nhập thất bại với sai mật khẩu

- **ID:** `AUTH_TC04`
- **Mô tả:** Người dùng đăng nhập với mật khẩu không chính xác.
- **Điều kiện tiên quyết:** Tồn tại một người dùng với `username` là `testuser`.
- **Các bước thực hiện:**
  1. Chuẩn bị một đối tượng `LoginDto` với `username: "testuser"` và `password: "wrongpassword"`.
  2. Gửi yêu cầu `POST` đến endpoint `/api/auth/login`.
- **Kết quả mong đợi:**
  - Hệ thống trả về HTTP status `401 Unauthorized`.
  - Response body chứa thông báo lỗi `"Invalid username or password."`.

### Kịch bản 5: Truy cập thông tin người dùng (chưa được bảo vệ)

- **ID:** `AUTH_TC05`
- **Mô tả:** Sau khi đăng ký, kiểm tra xem có thể truy cập thông tin công khai của người dùng đó hay không.
- **Điều kiện tiên quyết:** Một người dùng với `username` là `newuser` vừa được tạo thành công.
- **Các bước thực hiện:**
  1. Gửi yêu cầu `GET` đến endpoint `/api/users/newuser`.
- **Kết quả mong đợi:**
  - Hệ thống trả về HTTP status `200 OK`.
  - Response body là một đối tượng `ProfileDto` chứa thông tin của `newuser`.
  - **(Tương lai):** Kịch bản này sẽ được cập nhật để kiểm tra việc truy cập tài nguyên được bảo vệ bằng JWT token.

---