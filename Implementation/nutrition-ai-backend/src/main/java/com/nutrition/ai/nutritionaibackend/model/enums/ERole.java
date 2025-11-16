package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `ERole` định nghĩa các vai trò mà một người dùng có thể có trong hệ thống.
 * Nó là một thành phần cốt lõi cho mục đích ủy quyền (authorization) và kiểm soát truy cập,
 * cho phép hệ thống phân biệt quyền hạn của các loại người dùng khác nhau.
 *
 * Logic hoạt động:
 * - Định nghĩa các vai trò cố định:
 *   - Enum này thiết lập một tập hợp các vai trò người dùng cố định (`ROLE_USER`, `ROLE_ADMIN`).
 *   - Điều này giúp đảm bảo rằng chỉ các vai trò được định nghĩa trước mới có thể được gán cho người dùng, duy trì tính nhất quán và bảo mật.
 * - Vai trò trong hệ thống bảo mật:
 *   - Các giá trị của `ERole` được sử dụng rộng rãi trong cơ chế bảo mật (ví dụ: Spring Security) 
 *     để xác định quyền truy cập và các hành động mà người dùng có thể thực hiện trong hệ thống.
 *   - Khi một người dùng cố gắng truy cập một tài nguyên được bảo vệ, hệ thống sẽ kiểm tra các vai trò của họ để quyết định liệu họ có quyền truy cập hay không.
 *
 * Luồng dữ liệu:
 * - Gán vai trò cho `User`:
 *   - Khi một người dùng (`User`) được tạo hoặc cập nhật, một hoặc nhiều giá trị từ `ERole` sẽ được gán cho trường `roles` của `User`.
 *   - Trong cơ sở dữ liệu, các giá trị này thường được lưu trữ dưới dạng chuỗi trong bảng liên kết (ví dụ: `user_roles`) khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity `Role`.
 * - Sử dụng trong logic nghiệp vụ và bảo mật:
 *   - Các dịch vụ (Service layer) và bộ lọc bảo mật sử dụng các vai trò này để thực thi các quy tắc ủy quyền.
 *   - Ví dụ: Chỉ `ROLE_ADMIN` mới có thể truy cập các chức năng quản trị hệ thống, trong khi `ROLE_USER` chỉ có thể truy cập các chức năng cá nhân.
 */
public enum ERole {
    /**
     * Đại diện cho vai trò người dùng tiêu chuẩn với các quyền cơ bản.
     * Người dùng có vai trò này có thể thực hiện các hành động thông thường như thêm bữa ăn vào nhật ký, 
     * xem báo cáo cá nhân, cập nhật hồ sơ của họ, và tương tác với các tính năng cơ bản của ứng dụng.
     */
    ROLE_USER,

    /**
     * Đại diện cho vai trò quản trị viên với các quyền hạn cao hơn để quản lý hệ thống.
     * Quản trị viên có thể quản lý người dùng, cấu hình hệ thống, truy cập tất cả dữ liệu, 
     * và thực hiện các thao tác quản trị khác cần thiết để duy trì và vận hành ứng dụng.
     */
    ROLE_ADMIN
}