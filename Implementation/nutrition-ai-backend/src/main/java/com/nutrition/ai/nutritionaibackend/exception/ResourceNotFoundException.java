package com.nutrition.ai.nutritionaibackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Tùy chỉnh ngoại lệ được ném ra khi một tài nguyên cụ thể không được tìm thấy
 * trong hệ thống. Ngoại lệ này được sử dụng để chỉ ra các tình huống như
 * không tìm thấy một bản ghi trong cơ sở dữ liệu dựa trên một ID đã cho.
 *
 * <p>Nguyên lý hoạt động:
 * <ul>
 *   <li>Khi ngoại lệ này được ném ra, Spring Boot sẽ tự động chặn nó.</li>
 *   <li>Annotation {@code @ResponseStatus} đảm bảo rằng Spring sẽ trả về mã trạng thái HTTP 404
 *       (Not Found) cho client gọi API.</li>
 *   <li>Kế thừa từ {@link java.lang.RuntimeException} giúp đây là một 'unchecked exception',
 *       nghĩa là các phương thức ném ra nó không cần phải khai báo trong mệnh đề {@code throws}.</li>
 * </ul>
 *
 * @author [Tên tác giả nếu có, hoặc để trống nếu không biết]
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
  /**
   * ID phiên bản tuần tự chuẩn cho các lớp ngoại lệ {@code Serializable}.
   * Được sử dụng để kiểm soát phiên bản hóa trong quá trình tuần tự hóa
   * và giải tuần tự hóa của các đối tượng ngoại lệ.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Tên của tài nguyên không được tìm thấy (ví dụ: "User", "Product", "Order").
   * Trường này cung cấp ngữ cảnh về loại thực thể bị thiếu.
   */
  private String resourceName;

  /**
   * Tên của trường (thuộc tính) được sử dụng để tìm kiếm tài nguyên
   * (ví dụ: "id", "email", "username").
   * Trường này giúp xác định tiêu chí tìm kiếm đã được sử dụng.
   */
  private String fieldName;

  /**
   * Giá trị của trường tìm kiếm không khớp với bất kỳ tài nguyên nào (ví dụ: '123',
   * 'test@example.com', 'nonExistentUser').
   * Trường này cung cấp giá trị cụ thể đã được tìm kiếm.
   */
  private Object fieldValue;

  /**
   * Constructor để tạo một {@code ResourceNotFoundException} với thông tin chi tiết.
   *
   * <p>Luồng hoạt động:
   * <ol>
   *   <li>Nhận thông tin về tên tài nguyên, tên trường và giá trị trường.</li>
   *   <li>Gọi constructor của lớp cha ({@link java.lang.RuntimeException}) với một thông báo lỗi
   *       được định dạng. Thông báo này thường có dạng "[Resource Name] not found with [Field Name] : '[Field Value]'".</li>
   *   <li>Gán các giá trị tham số cho các trường tương ứng của ngoại lệ.</li>
   * </ol>
   *
   * @param resourceName Tên của tài nguyên không tìm thấy (ví dụ: "User").
   * @param fieldName Tên của trường dùng để tìm kiếm (ví dụ: "id").
   * @param fieldValue Giá trị của trường tìm kiếm không khớp (ví dụ: 100).
   */
  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  /**
   * Trả về tên của tài nguyên không được tìm thấy.
   *
   * @return Tên tài nguyên (ví dụ: "User").
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * Trả về tên của trường tìm kiếm đã được sử dụng.
   *
   * @return Tên trường (ví dụ: "id").
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Trả về giá trị của trường tìm kiếm không khớp.
   *
   * @return Giá trị tìm kiếm (ví dụ: 100).
   */
  public Object getFieldValue() {
    return fieldValue;
  }
}