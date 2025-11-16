package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EMealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * Lớp `Meal` đại diện cho một bữa ăn cụ thể (ví dụ: Bữa sáng, Bữa trưa, Ăn nhẹ) trong một `DailyMenu`.
 * Nó là một Entity JPA, ánh xạ tới bảng "meals" trong cơ sở dữ liệu.
 * Mỗi bữa ăn được định nghĩa bởi loại bữa ăn, thời gian dự kiến và danh sách các `FoodItem` cấu thành nó.
 *
 * Logic hoạt động:
 * - Định nghĩa các bữa ăn:
 *   - Mỗi `Meal` có một ID duy nhất (`id`), loại bữa ăn (`mealType` - enum), thời gian dự kiến (`time`).
 *   - Mục đích là để tổ chức các mặt hàng thực phẩm thành các bữa ăn cụ thể trong ngày.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "meals")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `meals`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với thực đơn hàng ngày:
 *   - `@ManyToOne` với `DailyMenu`: Một `Meal` thuộc về một `DailyMenu` duy nhất. Khóa ngoại `daily_menu_id` được tạo trong bảng `meals`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải đối tượng `DailyMenu` khi cần.
 * - Mối quan hệ với các mặt hàng thực phẩm trong bữa ăn:
 *   - `@OneToMany` với `MealFoodItem`: Một `Meal` có thể chứa nhiều `FoodItem` thông qua các thực thể liên kết `MealFoodItem`, mỗi thực thể mô tả một `FoodItem` cụ thể và số lượng của nó trong bữa ăn.
 *   - `cascade = CascadeType.ALL`: Đảm bảo rằng mọi thao tác (thêm, cập nhật, xóa) trên `Meal` sẽ được áp dụng cho các `MealFoodItem` liên quan.
 *   - `orphanRemoval = true`: Tự động xóa một `MealFoodItem` khỏi cơ sở dữ liệu nếu nó bị tách khỏi danh sách `mealFoodItems` của một `Meal`.
 * - Xử lý loại bữa ăn:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `EMealType` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Meal` được tạo như một phần của `DailyMenu`. `mealType` và `time` được thiết lập.
 *   - Các `MealFoodItem` được thêm vào danh sách `mealFoodItems` của `Meal`, mô tả các thành phần thực phẩm và số lượng của chúng.
 *   - Khi `Meal` được lưu, các `MealFoodItem` liên quan cũng được lưu hoặc cập nhật do `CascadeType.ALL`.
 * - Truy vấn:
 *   - Khi truy vấn một `Meal`, các thuộc tính cơ bản của bữa ăn được tải.
 *   - Đối tượng `DailyMenu` liên quan được tải `LAZY`.
 *   - Danh sách `mealFoodItems` sẽ được tải, cho phép truy cập tất cả các thành phần thực phẩm của bữa ăn. Thông tin dinh dưỡng tổng hợp cho bữa ăn có thể được tính toán từ các `MealFoodItem` này.
 */
@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    /**
     * `id` là khóa chính duy nhất cho mỗi bữa ăn. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Meal` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `mealType` là loại bữa ăn (ví dụ: `BREAKFAST`, `LUNCH`, `DINNER`, `SNACK`).
     * @Enumerated(EnumType.STRING) chỉ thị cho JPA lưu giá trị enum dưới dạng chuỗi (tên) trong cơ sở dữ liệu,
     * giúp tăng khả năng đọc và bảo trì dữ liệu.
     */
    @Enumerated(EnumType.STRING)
    private EMealType mealType;

    /**
     * `time` là thời gian dự kiến hoặc thực tế của bữa ăn trong ngày.
     * Trường này giúp người dùng hoặc hệ thống tổ chức các bữa ăn theo lịch trình.
     */
    private LocalTime time;

    /**
     * `dailyMenu` là đối tượng `DailyMenu` mà bữa ăn này thuộc về.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `Meal` có thể thuộc về một `DailyMenu` duy nhất.
     * `@JoinColumn(name = "daily_menu_id")` tạo một khóa ngoại `daily_menu_id` trong bảng `meals`,
     * liên kết đến bảng `daily_menus`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `DailyMenu` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_menu_id")
    private DailyMenu dailyMenu;

    /**
     * `mealFoodItems` là danh sách các `MealFoodItem` liên kết với bữa ăn này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `Meal` có thể chứa nhiều `FoodItem` với số lượng cụ thể thông qua `MealFoodItem`.
     * `mappedBy = "meal"` thiết lập mối quan hệ hai chiều, với trường `meal` trong `MealFoodItem`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `Meal`.
     * `cascade = CascadeType.ALL` đảm bảo rằng các thao tác như persist, merge, remove trên `Meal`
     * sẽ được áp dụng cho các `MealFoodItem` liên quan. Điều này giúp quản lý vòng đời của các thành phần thực phẩm cùng với bữa ăn.
     * `orphanRemoval = true` tự động xóa một `MealFoodItem` khỏi cơ sở dữ liệu nếu nó bị loại bỏ khỏi danh sách `mealFoodItems` của `Meal` này.
     */
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealFoodItem> mealFoodItems;
}