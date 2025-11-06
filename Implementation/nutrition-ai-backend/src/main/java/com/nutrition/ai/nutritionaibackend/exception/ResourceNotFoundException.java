package com.nutrition.ai.nutritionaibackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ tùy chỉnh được sử dụng khi một tài nguyên (resource) không được tìm thấy
 * trong hệ thống (ví dụ: một bản ghi trong cơ sở dữ liệu).
 *
 * Nguyên lý hoạt động:
 * - Khi ngoại lệ này được ném ra, Spring Boot sẽ tự động chặn nó.
 * - Annotation @ResponseStatus đảm bảo rằng Spring sẽ trả về mã trạng thái HTTP 404 (Not Found)
 * cho client (người gọi API).
 * - Kế thừa từ RuntimeException giúp đây là một 'unchecked exception' (không cần khai báo throws).
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * ID phiên bản tuần tự (Serializable) chuẩn cho các lớp ngoại lệ.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Tên của tài nguyên không tìm thấy (ví dụ: "User", "Product").
     */
    private String resourceName;
    
    /**
     * Tên của trường dùng để tìm kiếm (ví dụ: "id", "email").
     */
    private String fieldName;
    
    /**
     * Giá trị của trường tìm kiếm không khớp (ví dụ: '123', 'test@example.com').
     */
    private Object fieldValue;

    /**
     * Constructor để tạo ResourceNotFoundException với thông tin chi tiết.
     *
     * Luồng hoạt động:
     * 1. Nhận thông tin về tài nguyên, trường và giá trị.
     * 2. Gọi constructor lớp cha (RuntimeException) với thông báo lỗi được định dạng.
     *
     * @param resourceName Tên tài nguyên (ví dụ: "User")
     * @param fieldName Tên trường tìm kiếm (ví dụ: "id")
     * @param fieldValue Giá trị tìm kiếm (ví dụ: 100)
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Định dạng thông báo lỗi: "User not found with id : '100'"
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Trả về tên của tài nguyên không tìm thấy.
     * @return Tên tài nguyên
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Trả về tên của trường tìm kiếm.
     * @return Tên trường
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Trả về giá trị của trường tìm kiếm.
     * @return Giá trị tìm kiếm
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}