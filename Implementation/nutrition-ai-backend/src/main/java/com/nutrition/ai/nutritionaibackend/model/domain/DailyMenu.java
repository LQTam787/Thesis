package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Lớp `DailyMenu` đại diện cho thực đơn dinh dưỡng được lên kế hoạch cho một ngày cụ thể.
 * Nó là một Entity JPA, ánh xạ tới bảng "daily_menus" trong cơ sở dữ liệu.
 * Mỗi `DailyMenu` là một phần của một `NutritionPlan` lớn hơn và chứa một tập hợp các `Meal` (bữa ăn) cho ngày đó.
 *
 * Logic hoạt động:
 * - Tổ chức thực đơn theo ngày:
 *   - Mỗi `DailyMenu` được định danh bằng một ID duy nhất (`id`) và một ngày cụ thể (`date`).
 *   - Mục đích là cung cấp một cấu trúc để lưu trữ các bữa ăn được đề xuất hoặc tiêu thụ trong một ngày.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "daily_menus")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `daily_menus`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với kế hoạch dinh dưỡng:
 *   - `@ManyToOne` với `NutritionPlan`: Một `DailyMenu` thuộc về một `NutritionPlan` duy nhất.
 *   - Khóa ngoại `nutrition_plan_id` được tạo trong bảng `daily_menus` để liên kết với `NutritionPlan`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải `NutritionPlan` khi cần.
 * - Mối quan hệ với các bữa ăn:
 *   - `@OneToMany` với `Meal`: Một `DailyMenu` chứa nhiều `Meal` (ví dụ: bữa sáng, bữa trưa, bữa tối).
 *   - `cascade = CascadeType.ALL`: Đảm bảo rằng mọi thao tác (thêm, cập nhật, xóa) trên `DailyMenu` sẽ được áp dụng cho các `Meal` liên quan. Điều này có nghĩa là khi một `DailyMenu` bị xóa, tất cả các `Meal` của nó cũng sẽ bị xóa.
 *   - `orphanRemoval = true`: Nếu một `Meal` bị tách khỏi danh sách `meals` của một `DailyMenu` (ví dụ: bằng cách gán nó cho một `DailyMenu` khác hoặc loại bỏ khỏi danh sách), nó sẽ tự động bị xóa khỏi cơ sở dữ liệu.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - Một `DailyMenu` được tạo cho một ngày và một `NutritionPlan` cụ thể. Sau đó, các `Meal` được thêm vào danh sách `meals` của nó.
 *   - Khi `DailyMenu` được lưu, các `Meal` liên quan cũng được lưu hoặc cập nhật do `CascadeType.ALL`.
 * - Truy vấn:
 *   - Khi truy vấn một `DailyMenu`, các thuộc tính `id` và `date` được tải.
 *   - Đối tượng `NutritionPlan` liên quan được tải `LAZY`.
 *   - Danh sách `meals` sẽ được tải, cho phép truy cập các bữa ăn được lên kế hoạch cho ngày đó. Các thông tin dinh dưỡng tổng hợp cho ngày có thể được tính toán từ các bữa ăn này.
 */
@Entity
@Table(name = "daily_menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMenu {
    /**
     * `id` là khóa chính duy nhất cho mỗi thực đơn hàng ngày. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `DailyMenu` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `date` là ngày mà thực đơn này áp dụng. Trường này đảm bảo mỗi `DailyMenu` đại diện cho một ngày cụ thể,
     * cho phép theo dõi và quản lý thực đơn theo lịch.
     */
    private LocalDate date;

    /**
     * `nutritionPlan` là đối tượng `NutritionPlan` mà `DailyMenu` này thuộc về.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `DailyMenu` có thể thuộc về một `NutritionPlan` duy nhất.
     * `@JoinColumn(name = "nutrition_plan_id")` tạo một khóa ngoại `nutrition_plan_id` trong bảng `daily_menus`,
     * liên kết đến bảng `nutrition_plans`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `NutritionPlan` chỉ được tải khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan;

    /**
     * `meals` là danh sách các `Meal` (bữa ăn) cấu thành thực đơn cho ngày này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `DailyMenu` có thể chứa nhiều `Meal`.
     * `mappedBy = "dailyMenu"` thiết lập mối quan hệ hai chiều, với trường `dailyMenu` trong `Meal`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `DailyMenu`.
     * `cascade = CascadeType.ALL` đảm bảo rằng các thao tác như persist, merge, remove trên `DailyMenu`
     * sẽ được áp dụng cho các `Meal` liên quan. Điều này giúp quản lý vòng đời của các bữa ăn cùng với thực đơn hàng ngày.
     * `orphanRemoval = true` tự động xóa một `Meal` khỏi cơ sở dữ liệu nếu nó bị loại bỏ khỏi danh sách `meals` của `DailyMenu` này.
     */
    @OneToMany(mappedBy = "dailyMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;
}