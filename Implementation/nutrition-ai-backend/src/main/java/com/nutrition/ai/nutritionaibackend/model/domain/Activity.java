package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Lớp `Activity` đại diện cho một hoạt động thể chất chung mà người dùng có thể thực hiện (ví dụ: "Chạy bộ", "Yoga").
 * Nó là một Entity JPA, ánh xạ tới bảng "activities" trong cơ sở dữ liệu.
 * Lớp này được sử dụng để định nghĩa các loại hoạt động có sẵn trong hệ thống và các thuộc tính cơ bản của chúng.
 *
 * Logic hoạt động:
 * - Định nghĩa các hoạt động tiêu chuẩn:
 *   - Mỗi hoạt động có một ID duy nhất (`id`), tên (`name`) và lượng calo đốt cháy trung bình mỗi giờ (`caloriesBurnedPerHour`).
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "activities")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `activities`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code bằng cách tự động tạo getters, setters, constructors, v.v.
 * - Mối quan hệ với nhật ký hoạt động:
 *   - Một hoạt động có thể được ghi lại nhiều lần bởi nhiều người dùng. Mối quan hệ `@OneToMany` với `ActivityLog` thể hiện điều này.
 *   - `mappedBy = "activity"` chỉ ra rằng trường `activity` trong lớp `ActivityLog` là bên sở hữu mối quan hệ, nghĩa là `ActivityLog` chứa khóa ngoại tham chiếu đến `Activity`.
 *   - Khi một `Activity` được truy vấn, danh sách `activityLogs` sẽ chứa tất cả các nhật ký hoạt động liên quan đến hoạt động này.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - Các thực thể `Activity` được tạo hoặc cập nhật thông qua Repository layer, sau đó được lưu vào bảng `activities`.
 *   - ID được tạo tự động bởi cơ sở dữ liệu (`GenerationType.IDENTITY`).
 * - Truy vấn:
 *   - Khi truy vấn một `Activity` từ cơ sở dữ liệu, các thuộc tính `id`, `name`, `caloriesBurnedPerHour` và danh sách các `activityLogs` liên quan sẽ được tải.
 *   - Danh sách `activityLogs` cho phép truy cập trực tiếp các nhật ký cụ thể đã sử dụng hoạt động này.
 */
@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    /**
     * `id` là khóa chính duy nhất cho mỗi hoạt động. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Activity` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `name` là tên mô tả của hoạt động (ví dụ: "Chạy bộ", "Bơi lội").
     * Đây là một trường bắt buộc để định danh hoạt động.
     */
    private String name;

    /**
     * `caloriesBurnedPerHour` lưu trữ lượng calo trung bình mà một người có thể đốt cháy
     * mỗi giờ khi thực hiện hoạt động này. Giá trị này được sử dụng trong tính toán dinh dưỡng
     * và kế hoạch hoạt động của người dùng.
     */
    private Double caloriesBurnedPerHour;

    /**
     * `activityLogs` là một danh sách các `ActivityLog` liên kết với hoạt động này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `Activity` có thể có nhiều `ActivityLog`.
     * `mappedBy = "activity"` thiết lập mối quan hệ hai chiều, với trường `activity` trong `ActivityLog`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `Activity`.
     * Khi một `Activity` được tải, danh sách này sẽ tự động được điền với các `ActivityLog` tương ứng (nếu cấu hình fetch type cho phép).
     */
    @OneToMany(mappedBy = "activity")
    private List<ActivityLog> activityLogs;
}