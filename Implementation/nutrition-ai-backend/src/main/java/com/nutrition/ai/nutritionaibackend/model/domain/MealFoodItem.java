package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp `MealFoodItem` là một entity liên kết, đại diện cho mối quan hệ Many-to-Many giữa `Meal` và `FoodItem`.
 * Nó ánh xạ tới bảng "meal_food_items" trong cơ sở dữ liệu và chứa thông tin chi tiết về số lượng của một `FoodItem` trong một `Meal` cụ thể.
 * Lớp này cho phép định lượng chính xác các thành phần trong mỗi bữa ăn.
 *
 * Logic hoạt động:
 * - Định lượng thành phần bữa ăn:
 *   - Mỗi `MealFoodItem` có một ID duy nhất (`id`), số lượng (`quantity`) và đơn vị (`unit`) của `FoodItem` được sử dụng trong `Meal`.
 *   - Mục đích là để ghi lại chính xác bao nhiêu của một loại thực phẩm cụ thể được tiêu thụ trong một bữa ăn.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "meal_food_items")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `meal_food_items`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với bữa ăn và mặt hàng thực phẩm:
 *   - `@ManyToOne` với `Meal`: Một `MealFoodItem` thuộc về một `Meal` duy nhất. Khóa ngoại `meal_id` được tạo.
 *   - `@ManyToOne` với `FoodItem`: Một `MealFoodItem` liên kết với một `FoodItem` cụ thể. Khóa ngoại `food_item_id` được tạo.
 *   - `fetch = FetchType.LAZY`: Tối ưu hóa hiệu suất bằng cách chỉ tải các đối tượng `Meal` và `FoodItem` khi chúng thực sự được truy cập.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `MealFoodItem` được tạo khi một `FoodItem` được thêm vào một `Meal` với số lượng và đơn vị cụ thể.
 *   - Các đối tượng `Meal` và `FoodItem` hiện có được liên kết để thiết lập mối quan hệ.
 * - Truy vấn:
 *   - Khi truy vấn một `MealFoodItem`, các thuộc tính `id`, `quantity`, `unit` được tải.
 *   - Các đối tượng `Meal` và `FoodItem` liên quan sẽ được tải `LAZY` khi được yêu cầu, cho phép truy cập thông tin chi tiết về bữa ăn và thực phẩm.
 *   - Dữ liệu này là cơ sở để tính toán tổng giá trị dinh dưỡng (calo, protein, carbs, fats) cho mỗi bữa ăn và cho toàn bộ `DailyMenu`.
 */
@Entity
@Table(name = "meal_food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealFoodItem {
    /**
     * `id` là khóa chính duy nhất cho mỗi thành phần thực phẩm trong bữa ăn. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `MealFoodItem` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `quantity` lưu trữ số lượng của `FoodItem` được sử dụng trong bữa ăn này.
     * Ví dụ: 100 (gam), 1 (quả). Đây là giá trị quan trọng để tính toán dinh dưỡng chính xác.
     */
    private Double quantity;

    /**
     * `unit` là đơn vị của số lượng (ví dụ: "g", "ml", "phần").
     * Trường này cung cấp ngữ cảnh cho giá trị `quantity` và đảm bảo tính toán dinh dưỡng đúng đắn.
     */
    private String unit;

    /**
     * `meal` là đối tượng `Meal` mà thành phần thực phẩm này thuộc về.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `MealFoodItem` có thể thuộc về một `Meal` duy nhất.
     * `@JoinColumn(name = "meal_id")` tạo một khóa ngoại `meal_id` trong bảng `meal_food_items`,
     * liên kết đến bảng `meals`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `Meal` chỉ được tải khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    /**
     * `foodItem` là đối tượng `FoodItem` được sử dụng làm thành phần trong bữa ăn này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `MealFoodItem` có thể liên kết với cùng một `FoodItem`.
     * `@JoinColumn(name = "food_item_id")` tạo một khóa ngoại `food_item_id` trong bảng `meal_food_items`,
     * liên kết đến bảng `food_items`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `FoodItem` chỉ được tải khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
}