package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `ActivityLog` ghi lại chi tiết về việc một người dùng đã thực hiện một hoạt động thể chất cụ thể.
 * Nó là một Entity JPA, ánh xạ tới bảng "activity_logs" trong cơ sở dữ liệu.
 * Lớp này theo dõi thời gian thực hiện, thời lượng, lượng calo đốt cháy, và liên kết với người dùng và loại hoạt động tương ứng.
 *
 * Logic hoạt động:
 * - Ghi lại chi tiết hoạt động:
 *   - Mỗi nhật ký hoạt động có một ID duy nhất (`id`), thời gian ghi (`logDate`), thời lượng (`durationInMinutes`) và tổng lượng calo đốt cháy ước tính (`caloriesBurned`).
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "activity_logs")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `activity_logs`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng và hoạt động:
 *   - `@ManyToOne` với `User`: Một `ActivityLog` thuộc về một `User`. Khóa ngoại `user_id` được tạo trong bảng `activity_logs`.
 *   - `@ManyToOne` với `Activity`: Một `ActivityLog` liên kết với một `Activity` cụ thể. Khóa ngoại `activity_id` được tạo trong bảng `activity_logs`.
 *   - `fetch = FetchType.LAZY`: Tối ưu hóa hiệu suất bằng cách chỉ tải các đối tượng `User` và `Activity` khi chúng thực sự được truy cập, tránh tải không cần thiết.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `ActivityLog` được tạo khi người dùng ghi lại một hoạt động. Các chi tiết như `logDate`, `durationInMinutes`, `caloriesBurned` được thiết lập.
 *   - `user` và `activity` được liên kết thông qua các đối tượng `User` và `Activity` hiện có, tạo khóa ngoại trong cơ sở dữ liệu.
 * - Truy vấn:
 *   - Khi truy vấn một `ActivityLog`, các thuộc tính cơ bản của nhật ký sẽ được tải.
 *   - Các đối tượng `User` và `Activity` liên quan sẽ được tải theo kiểu `LAZY` khi được yêu cầu, cho phép truy cập thông tin chi tiết của người dùng và hoạt động đã thực hiện.
 *   - Lượng calo đốt cháy (`caloriesBurned`) có thể được tính toán dựa trên `durationInMinutes` và `caloriesBurnedPerHour` của `Activity` liên quan.
 */
@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    /**
     * `id` là khóa chính duy nhất cho mỗi nhật ký hoạt động. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `ActivityLog` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `logDate` ghi lại thời điểm chính xác (ngày và giờ) khi hoạt động được thực hiện và nhật ký này được tạo.
     * Nó là một trường quan trọng để theo dõi lịch sử hoạt động của người dùng.
     */
    private LocalDateTime logDate;

    /**
     * `durationInMinutes` lưu trữ thời lượng của hoạt động đã thực hiện, tính bằng phút.
     * Giá trị này được sử dụng cùng với `caloriesBurnedPerHour` của `Activity` để tính toán tổng lượng calo đốt cháy.
     */
    private Integer durationInMinutes;

    /**
     * `caloriesBurned` là tổng lượng calo ước tính mà người dùng đã đốt cháy trong lần hoạt động này.
     * Giá trị này thường được tính toán từ `durationInMinutes` và `caloriesBurnedPerHour` của `Activity` liên quan.
     */
    private Double caloriesBurned;

    /**
     * `user` là đối tượng `User` liên kết với nhật ký hoạt động này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ActivityLog` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `activity_logs`, liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu, giúp tối ưu hiệu suất.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * `activity` là đối tượng `Activity` mà người dùng đã thực hiện.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ActivityLog` có thể tham chiếu đến cùng một loại `Activity`.
     * `@JoinColumn(name = "activity_id")` tạo một khóa ngoại `activity_id` trong bảng `activity_logs`, liên kết đến bảng `activities`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `Activity` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;
}