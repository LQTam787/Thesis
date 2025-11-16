package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Lớp `Goal` đại diện cho mục tiêu dinh dưỡng và thể chất mà một người dùng đặt ra (ví dụ: giảm cân, tăng cơ).
 * Nó là một Entity JPA, ánh xạ tới bảng "goals" trong cơ sở dữ liệu.
 * Mỗi mục tiêu có các thuộc tính như cân nặng mục tiêu, ngày dự kiến đạt được, loại mục tiêu và trạng thái.
 *
 * Logic hoạt động:
 * - Định nghĩa mục tiêu cá nhân:
 *   - Mỗi `Goal` có một ID duy nhất (`id`), `targetWeight` (nếu có), `targetDate` dự kiến hoàn thành, `goalType` (enum) và `status`.
 *   - `nutritionGoal` cung cấp mô tả mục tiêu bằng ngôn ngữ tự nhiên, hữu ích cho các chức năng AI/NLP.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "goals")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `goals`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng:
 *   - `@ManyToOne` với `User`: Một `Goal` thuộc về một `User` duy nhất. Khóa ngoại `user_id` được tạo trong bảng `goals`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải đối tượng `User` khi cần.
 * - Xử lý loại mục tiêu:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `EGoalType` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu, giúp dễ đọc và bảo trì hơn.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Goal` được tạo hoặc cập nhật khi người dùng thiết lập hoặc thay đổi mục tiêu của họ.
 *   - Các thuộc tính như `targetWeight`, `targetDate`, `goalType`, `status` được thiết lập dựa trên đầu vào của người dùng.
 *   - Liên kết với đối tượng `User` hiện có để thiết lập mối quan hệ sở hữu.
 * - Truy vấn:
 *   - Khi truy vấn một `Goal`, các thuộc tính của mục tiêu sẽ được tải.
 *   - Đối tượng `User` liên quan sẽ được tải `LAZY` khi được yêu cầu, cho phép hệ thống truy cập thông tin người dùng liên quan đến mục tiêu.
 *   - Dữ liệu mục tiêu được sử dụng để cá nhân hóa các kế hoạch dinh dưỡng (`NutritionPlan`), gợi ý hoạt động và báo cáo tiến độ cho người dùng.
 */
@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    /**
     * `id` là khóa chính duy nhất cho mỗi mục tiêu. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Goal` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `targetWeight` là cân nặng mà người dùng muốn đạt được (nếu mục tiêu liên quan đến cân nặng).
     * Giá trị này là một mục tiêu định lượng quan trọng cho các kế hoạch giảm/tăng cân.
     */
    private Double targetWeight;

    /**
     * `targetDate` là ngày dự kiến mà người dùng hy vọng đạt được mục tiêu của mình.
     * Trường này giúp theo dõi tiến độ và đặt ra các mốc thời gian cho kế hoạch.
     */
    private LocalDate targetDate;

    /**
     * `goalType` là loại mục tiêu mà người dùng đặt ra (ví dụ: `INCREASE_WEIGHT`, `DECREASE_WEIGHT`, `MAINTAIN_WEIGHT`).
     * @Enumerated(EnumType.STRING) chỉ thị cho JPA lưu giá trị enum dưới dạng chuỗi (tên) trong cơ sở dữ liệu thay vì số thứ tự,
     * giúp tăng khả năng đọc và bảo trì dữ liệu.
     */
    @Enumerated(EnumType.STRING)
    private EGoalType goalType;

    /**
     * `status` là trạng thái hiện tại của mục tiêu (ví dụ: "Active", "Completed", "On Hold", "Cancelled").
     * Trường này giúp quản lý và lọc các mục tiêu của người dùng.
     */
    private String status;

    /**
     * `user` là đối tượng `User` liên kết với mục tiêu này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `Goal` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `goals`, liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * `nutritionGoal` là một mô tả mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên được cung cấp bởi người dùng hoặc hệ thống.
     * Trường này có thể được sử dụng bởi các mô hình AI/NLP để hiểu rõ hơn ý định của người dùng và tạo ra các gợi ý cá nhân hóa.
     */
    private String nutritionGoal;
}