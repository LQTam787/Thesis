## Biểu đồ Use Case Chi tiết - Chức năng Xác thực

```plantuml
@startuml AuthenticationUseCase
' !theme vibrant
title <size:20>Biểu đồ Use Case Chi tiết cho Chức năng Xác thực</size>

left to right direction

actor "Guest" as Guest
actor "User" as User

rectangle "Hệ thống Quản lý Dinh dưỡng AI" {
    ' Use case trừu tượng
    usecase "Cung cấp thông tin xác thực" as UC_Provide_Credentials <<Abstract>>
    usecase "Xác thực thông tin người dùng" as UC_Validate_Credentials <<Abstract>>

    ' Use case chính
    usecase "Đăng ký tài khoản" as UC_Register
    usecase "Đăng nhập" as UC_Login
    usecase "Đăng xuất" as UC_Logout

    ' Use case con được include
    usecase "Nhập thông tin đăng ký" as UC_Input_Register
    usecase "Nhập thông tin đăng nhập" as UC_Input_Login
    usecase "Kiểm tra tính hợp lệ" as UC_Validate_Input
    usecase "Kiểm tra thông tin đăng nhập" as UC_Check_Login
}

' Mối quan hệ giữa Actor và Use Case
Guest -- UC_Register
User -- UC_Login
User -- UC_Logout

' Phân rã và gộp nhóm
UC_Input_Register --|> UC_Provide_Credentials
UC_Input_Login --|> UC_Provide_Credentials

UC_Validate_Input --|> UC_Validate_Credentials
UC_Check_Login --|> UC_Validate_Credentials

' Mối quan hệ <<include>>
UC_Register ..> UC_Input_Register : <<include>>
UC_Register ..> UC_Validate_Input : <<include>>

UC_Login ..> UC_Input_Login : <<include>>
UC_Login ..> UC_Check_Login : <<include>>

@enduml
```

### **Giải thích biểu đồ:**

- **Actors:**
  - `Guest`: Người dùng chưa có tài khoản, chỉ có thể thực hiện chức năng `Đăng ký tài khoản`.
  - `User`: Người dùng đã đăng nhập, có thể thực hiện `Đăng nhập` và `Đăng xuất`.

- **Use Cases chính:**
  - `Đăng ký tài khoản`: Cho phép `Guest` tạo tài khoản mới.
  - `Đăng nhập`: Cho phép `User` truy cập vào hệ thống.
  - `Đăng xuất`: Cho phép `User` kết thúc phiên làm việc.

- **Use Cases trừu tượng (Abstract):**
  - `Cung cấp thông tin xác thực`: Tổng quát hóa hành động nhập liệu của người dùng (tên, email, mật khẩu).
  - `Xác thực thông tin người dùng`: Tổng quát hóa hành động kiểm tra và xác thực dữ liệu từ phía hệ thống.

- **Mối quan hệ `<<include>>`:**
  - `Đăng ký` bao gồm các bước bắt buộc là `Nhập thông tin đăng ký` và `Kiểm tra tính hợp lệ`.
  - `Đăng nhập` bao gồm các bước bắt buộc là `Nhập thông tin đăng nhập` và `Kiểm tra thông tin đăng nhập`.

- **Mối quan hệ `Generalization` (Mũi tên tam giác trắng):**
  - `Nhập thông tin đăng ký` và `Nhập thông tin đăng nhập` là các trường hợp cụ thể của `Cung cấp thông tin xác thực`.
  - `Kiểm tra tính hợp lệ` và `Kiểm tra thông tin đăng nhập` là các trường hợp cụ thể của `Xác thực thông tin người dùng`.
