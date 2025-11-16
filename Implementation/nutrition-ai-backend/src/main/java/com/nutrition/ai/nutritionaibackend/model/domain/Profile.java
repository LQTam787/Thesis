package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Lớp `Profile` đại diện cho hồ sơ cá nhân và thông tin chi tiết của người dùng.
 * Nó là một Entity JPA, ánh xạ tới bảng "profiles" trong cơ sở dữ liệu.
 * Lớp này có mối quan hệ One-to-One với `User`, nghĩa là mỗi `User` có một `Profile` duy nhất.
 * Nó lưu trữ các thông tin nhân khẩu học, thể chất và y tế của người dùng.
 *
 * Logic hoạt động:
 * - Lưu trữ thông tin cá nhân:
 *   - `Profile` bao gồm các thông tin như `fullName`, `dateOfBirth`, `gender` (enum), `height`, `weight`, `activityLevel`.
 *   - Các trường `allergies` và `medicalConditions` sử dụng `@Lob` để lưu trữ chuỗi dài (TEXT/CLOB).
 *   - Mục đích là để thu thập dữ liệu cần thiết cho việc cá nhân hóa kế hoạch dinh dưỡng và hoạt động.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "profiles")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `profiles`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ One-to-One với người dùng (Shared Primary Key):
 *   - `@OneToOne` với `User`: Thiết lập mối quan hệ 1:1.
 *   - `@MapsId`: Chỉ định rằng khóa chính của `Profile` (`id`) được lấy từ khóa chính của thực thể `User` liên kết.
 *   - `@JoinColumn(name = "id")`: Khóa ngoại `id` trong bảng `profiles` cũng là khóa chính và tham chiếu đến khóa chính của bảng `users`.
 *   - `fetch = FetchType.LAZY`: Tối ưu hóa hiệu suất bằng cách chỉ tải đối tượng `User` khi nó được truy cập lần đầu.
 * - Xử lý giới tính:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `EGender` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Profile` thường được tạo hoặc cập nhật cùng với việc đăng ký/cập nhật thông tin `User`.
 *   - Khóa chính `id` được quản lý tự động thông qua `User` do `MapsId`.
 * - Truy vấn:
 *   - Khi truy vấn một `Profile`, các thuộc tính cá nhân của người dùng được tải.
 *   - Đối tượng `User` liên quan sẽ được tải `LAZY` khi được yêu cầu, cho phép truy cập thông tin đăng nhập và các mối quan hệ khác của người dùng.
 *   - Dữ liệu trong `Profile` là đầu vào quan trọng cho các thuật toán đề xuất AI để tạo ra các kế hoạch dinh dưỡng và hoạt động cá nhân hóa.
 */
@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    /**
     * `id` là khóa chính của hồ sơ người dùng. Nó không được tạo tự động mà được lấy từ khóa chính của thực thể `User` liên kết
     * thông qua `@MapsId`, thiết lập mối quan hệ Shared Primary Key One-to-One.
     * @Id đánh dấu trường này là khóa chính.
     */
    @Id
    private Long id;

    /**
     * `fullName` là tên đầy đủ của người dùng. Đây là thông tin cơ bản để cá nhân hóa giao diện và giao tiếp.
     */
    private String fullName;

    /**
     * `dateOfBirth` là ngày sinh của người dùng. Được sử dụng để tính tuổi và các yếu tố liên quan đến trao đổi chất cơ bản (BMR).
     */
    private LocalDate dateOfBirth;

    /**
     * `gender` là giới tính của người dùng.
     * @Enumerated(EnumType.STRING) đảm bảo rằng giá trị enum `EGender` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
     * Giới tính là một yếu tố quan trọng trong việc tính toán nhu cầu dinh dưỡng và calo hàng ngày.
     */
    @Enumerated(EnumType.STRING)
    private EGender gender;

    /**
     * `height` là chiều cao của người dùng, thường tính bằng centimet hoặc inch.
     * Cùng với cân nặng, nó được sử dụng để tính chỉ số BMI và nhu cầu calo.
     */
    private Double height;

    /**
     * `weight` là cân nặng hiện tại của người dùng, thường tính bằng kilogram hoặc pound.
     * Đây là một trong những thông số quan trọng nhất để theo dõi tiến độ và điều chỉnh kế hoạch dinh dưỡng.
     */
    private Double weight;

    /**
     * `activityLevel` mô tả mức độ hoạt động thể chất hàng ngày của người dùng (ví dụ: "Ít vận động", "Vận động nhẹ", "Vận động vừa").
     * Trường này ảnh hưởng lớn đến việc tính toán tổng năng lượng tiêu hao hàng ngày (TDEE) và nhu cầu calo.
     */
    private String activityLevel;

    /**
     * `allergies` lưu trữ thông tin về các dị ứng thực phẩm của người dùng dưới dạng một chuỗi dài.
     * @Lob chỉ định rằng trường này là một đối tượng lớn, phù hợp để lưu trữ dữ liệu văn bản dài.
     * Thông tin này rất quan trọng để tránh các thành phần gây dị ứng trong các gợi ý thực phẩm và kế hoạch dinh dưỡng.
     */
    @Lob
    private String allergies;

    /**
     * `medicalConditions` lưu trữ thông tin về các tình trạng y tế hoặc bệnh lý của người dùng dưới dạng một chuỗi dài.
     * @Lob chỉ định rằng trường này là một đối tượng lớn.
     * Thông tin này giúp điều chỉnh kế hoạch dinh dưỡng để phù hợp với các yêu cầu sức khỏe đặc biệt.
     */
    @Lob
    private String medicalConditions;

    /**
     * `user` là đối tượng `User` mà hồ sơ này thuộc về, thiết lập mối quan hệ One-to-One.
     * `@OneToOne(fetch = FetchType.LAZY)`: Mối quan hệ 1:1, tải đối tượng `User` khi được truy cập lần đầu.
     * `@MapsId`: Chỉ định rằng khóa chính của `Profile` được ánh xạ từ khóa chính của `User`.
     * `@JoinColumn(name = "id")`: Khóa ngoại trong bảng `profiles` có tên `id` và tham chiếu đến khóa chính của bảng `users`.
     * Điều này tạo ra một Shared Primary Key, nơi khóa chính của `Profile` cũng là khóa ngoại đến `User`.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}