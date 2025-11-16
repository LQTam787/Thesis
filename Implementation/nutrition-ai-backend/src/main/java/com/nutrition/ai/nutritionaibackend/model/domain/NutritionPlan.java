package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Lớp `NutritionPlan` đại diện cho một kế hoạch dinh dưỡng tổng thể được thiết kế cho một người dùng.
 * Nó là một Entity JPA, ánh xạ tới bảng "nutrition_plans" trong cơ sở dữ liệu.
 * Một `NutritionPlan` có tên, ngày bắt đầu và kết thúc, mục tiêu dinh dưỡng và chứa nhiều `DailyMenu`.
 *
 * Logic hoạt động:
 * - Định nghĩa kế hoạch tổng thể:
 *   - Mỗi `NutritionPlan` có một ID duy nhất (`id`), `planName`, `startDate`, `endDate` và `nutritionGoal` (mô tả bằng ngôn ngữ tự nhiên).
 *   - Mục đích là cung cấp một khung sườn cho các kế hoạch dinh dưỡng cá nhân hóa, kéo dài trong một khoảng thời gian nhất định.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "nutrition_plans")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `nutrition_plans`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng:
 *   - `@ManyToOne` với `User`: Một `NutritionPlan` thuộc về một `User` duy nhất. Khóa ngoại `user_id` được tạo trong bảng `nutrition_plans`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải đối tượng `User` khi cần.
 * - Mối quan hệ với thực đơn hàng ngày:
 *   - `@OneToMany` với `DailyMenu`: Một `NutritionPlan` bao gồm nhiều `DailyMenu`, mỗi `DailyMenu` đại diện cho một ngày trong kế hoạch.
 *   - `mappedBy = "nutritionPlan"` chỉ ra rằng trường `nutritionPlan` trong `DailyMenu` là bên sở hữu mối quan hệ.
 *   - `cascade = CascadeType.ALL`: Đảm bảo rằng mọi thao tác trên `NutritionPlan` sẽ được áp dụng cho các `DailyMenu` liên quan, quản lý vòng đời của chúng.
 *   - `orphanRemoval = true`: Tự động xóa một `DailyMenu` khỏi cơ sở dữ liệu nếu nó bị tách khỏi danh sách `dailyMenus` của một `NutritionPlan`.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `NutritionPlan` được tạo hoặc cập nhật, với tên, ngày và mục tiêu. Sau đó, các `DailyMenu` được tạo và thêm vào kế hoạch.
 *   - Khi `NutritionPlan` được lưu, các `DailyMenu` liên quan cũng được lưu hoặc cập nhật do `CascadeType.ALL`.
 * - Truy vấn:
 *   - Khi truy vấn một `NutritionPlan`, các thuộc tính cơ bản của kế hoạch được tải.
 *   - Đối tượng `User` liên quan được tải `LAZY`.
 *   - Danh sách `dailyMenus` sẽ được tải, cho phép truy cập tất cả các thực đơn hàng ngày trong kế hoạch. Thông tin dinh dưỡng tổng thể của kế hoạch có thể được tính toán từ các `DailyMenu` này.
 */
@Entity
@Table(name = "nutrition_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlan {
    /**
     * `id` là khóa chính duy nhất cho mỗi kế hoạch dinh dưỡng. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `NutritionPlan` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `planName` là tên mô tả của kế hoạch dinh dưỡng (ví dụ: "Kế hoạch giảm cân 30 ngày").
     * Trường này giúp người dùng dễ dàng nhận biết và quản lý các kế hoạch của mình.
     */
    private String planName;

    /**
     * `startDate` là ngày bắt đầu hiệu lực của kế hoạch dinh dưỡng.
     * Cùng với `endDate`, nó định nghĩa khoảng thời gian của kế hoạch.
     */
    private LocalDate startDate;

    /**
     * `endDate` là ngày kết thúc dự kiến của kế hoạch dinh dưỡng.
     * Nó giúp theo dõi tiến độ và đánh giá hiệu quả của kế hoạch.
     */
    private LocalDate endDate;

    /**
     * `nutritionGoal` là một mô tả mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên cho kế hoạch này.
     * Trường này có thể được sử dụng bởi các mô hình AI/NLP để tạo ra các gợi ý và điều chỉnh kế hoạch.
     */
    private String nutritionGoal;

    /**
     * `user` là đối tượng `User` liên kết với kế hoạch dinh dưỡng này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `NutritionPlan` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `nutrition_plans`,
     * liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * `dailyMenus` là danh sách các `DailyMenu` cấu thành kế hoạch dinh dưỡng này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `NutritionPlan` có thể bao gồm nhiều `DailyMenu`.
     * `mappedBy = "nutritionPlan"` thiết lập mối quan hệ hai chiều, với trường `nutritionPlan` trong `DailyMenu`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `NutritionPlan`.
     * `cascade = CascadeType.ALL` đảm bảo rằng các thao tác như persist, merge, remove trên `NutritionPlan`
     * sẽ được áp dụng cho các `DailyMenu` liên quan. Điều này giúp quản lý vòng đời của các thực đơn hàng ngày cùng với kế hoạch.
     * `orphanRemoval = true` tự động xóa một `DailyMenu` khỏi cơ sở dữ liệu nếu nó bị loại bỏ khỏi danh sách `dailyMenus` của `NutritionPlan` này.
     */
    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyMenu> dailyMenus;
}