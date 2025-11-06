package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The ERole enum defines the roles that a user can have within the system.
 * This is used for authorization and access control purposes.
 *
 * Nguyên lý hoạt động:
 * Enum này thiết lập các vai trò người dùng cố định, được sử dụng trong cơ chế
 * bảo mật (Spring Security hoặc tương đương) để xác định quyền truy cập 
 * và các hành động mà người dùng có thể thực hiện trong hệ thống.
 */
public enum ERole {
    /**
     * Standard user role with basic permissions.
     * Vai trò: Cung cấp quyền truy cập cơ bản, chẳng hạn như thêm bữa ăn, xem báo cáo cá nhân.
     */
    ROLE_USER,

    /**
     * Administrator role with elevated permissions to manage the system.
     * Vai trò: Cung cấp quyền quản lý hệ thống, chẳng hạn như quản lý người dùng, 
     * cấu hình hệ thống, hoặc truy cập tất cả dữ liệu.
     */
    ROLE_ADMIN
}