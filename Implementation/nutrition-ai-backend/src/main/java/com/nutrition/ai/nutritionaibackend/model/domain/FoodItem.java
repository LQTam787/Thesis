package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Lớp `FoodItem` đại diện cho một mặt hàng thực phẩm cơ bản (ví dụ: "Táo", "Ức gà") trong hệ thống dinh dưỡng.
 * Nó là một Entity JPA, ánh xạ tới bảng "food_items" trong cơ sở dữ liệu.
 * Lớp này chứa các thông tin dinh dưỡng cơ bản như calo, protein, carbs, chất béo và kích thước khẩu phần.
 *
 * Logic hoạt động:
 * - Định nghĩa các mặt hàng thực phẩm:
 *   - Mỗi `FoodItem` có một ID duy nhất (`id`), tên (`name`) và các giá trị dinh dưỡng trên một khẩu phần cụ thể (calo, protein, carbs, fats).
 *   - `servingSize` mô tả đơn vị khẩu phần cơ sở để tính toán các giá trị dinh dưỡng.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "food_items")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `food_items`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với các thành phần khác:
 *   - `@OneToOne` với `Recipe`: Một `FoodItem` có thể là thành phần cơ sở cho một `Recipe`. `mappedBy="foodItem"` chỉ ra rằng `Recipe` là bên sở hữu mối quan hệ.
 *   - `@OneToMany` với `MealFoodItem`: Một `FoodItem` có thể xuất hiện trong nhiều `Meal` thông qua bảng liên kết `MealFoodItem`, ghi lại số lượng/khối lượng của thực phẩm đó trong bữa ăn.
 *   - `@OneToMany` với `FoodLog`: Một `FoodItem` có thể được ghi lại trong nhiều `FoodLog` của người dùng, theo dõi lượng thực phẩm đã tiêu thụ.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `FoodItem` được tạo và quản lý thông qua Repository layer, sau đó được lưu vào bảng `food_items`.
 *   - Thông tin dinh dưỡng được nhập và cập nhật để đảm bảo tính chính xác cho các tính toán sau này.
 * - Truy vấn:
 *   - Khi truy vấn một `FoodItem`, các thuộc tính `id`, `name`, `calories`, `protein`, `carbs`, `fats`, `servingSize` được tải.
 *   - Các mối quan hệ với `Recipe`, `MealFoodItem`, và `FoodLog` cho phép truy cập ngược lại đến các thực thể liên quan, ví dụ: tìm tất cả các bữa ăn chứa một `FoodItem` cụ thể, hoặc tất cả các nhật ký tiêu thụ liên quan.
 *   - Các giá trị dinh dưỡng này được sử dụng rộng rãi trong các tính toán tổng calo, macro cho bữa ăn, thực đơn hàng ngày và báo cáo dinh dưỡng tổng thể của người dùng.
 */
@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    /**
     * `id` là khóa chính duy nhất cho mỗi mặt hàng thực phẩm. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `FoodItem` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `name` là tên mô tả của mặt hàng thực phẩm (ví dụ: "Táo", "Bánh mì nguyên hạt").
     * Đây là một trường bắt buộc để định danh và hiển thị cho người dùng.
     */
    private String name;

    /**
     * `calories` là lượng calo có trong một khẩu phần (`servingSize`) của mặt hàng thực phẩm này.
     * Giá trị này là cơ sở để tính toán tổng lượng calo trong bữa ăn và kế hoạch dinh dưỡng.
     */
    private Double calories;

    /**
     * `protein` là lượng protein (gram) có trong một khẩu phần (`servingSize`) của mặt hàng thực phẩm này.
     * Cùng với carbs và fats, nó được sử dụng để tính toán macro dinh dưỡng.
     */
    private Double protein;

    /**
     * `carbs` là lượng carbohydrate (gram) có trong một khẩu phần (`servingSize`) của mặt hàng thực phẩm này.
     */
    private Double carbs;

    /**
     * `fats` là lượng chất béo (gram) có trong một khẩu phần (`servingSize`) của mặt hàng thực phẩm này.
     */
    private Double fats;

    /**
     * `servingSize` là một chuỗi mô tả kích thước khẩu phần cơ sở mà các giá trị dinh dưỡng (calo, protein,...) được tính toán.
     * Ví dụ: "100g", "1 quả", "1 cốc". Nó cung cấp ngữ cảnh cho các giá trị dinh dưỡng.
     */
    private String servingSize;

    /**
     * `recipe` là đối tượng `Recipe` mà `FoodItem` này có thể là thành phần cơ sở, đặc biệt nếu `FoodItem` là một công thức đơn giản hoặc nguyên liệu chính.
     * Mối quan hệ `@OneToOne` chỉ ra rằng một `FoodItem` có thể liên kết với một `Recipe` và ngược lại.
     * `mappedBy = "foodItem"` chỉ ra rằng trường `foodItem` trong `Recipe` là bên sở hữu mối quan hệ, nghĩa là `Recipe` chứa khóa ngoại đến `FoodItem`.
     * `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải `Recipe` khi được truy cập.
     */
    @OneToOne(mappedBy = "foodItem", fetch = FetchType.LAZY)
    private Recipe recipe;

    /**
     * `mealFoodItems` là danh sách các `MealFoodItem` liên kết với mặt hàng thực phẩm này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `FoodItem` có thể xuất hiện trong nhiều `Meal` thông qua các `MealFoodItem`.
     * `mappedBy = "foodItem"` thiết lập mối quan hệ hai chiều, với trường `foodItem` trong `MealFoodItem`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `FoodItem`.
     * Danh sách này cho phép truy vấn tất cả các bữa ăn mà `FoodItem` này là một phần.
     */
    @OneToMany(mappedBy = "foodItem")
    private List<MealFoodItem> mealFoodItems;

    /**
     * `foodLogs` là danh sách các `FoodLog` liên kết với mặt hàng thực phẩm này.
     * Mối quan hệ `@OneToMany` chỉ ra rằng một `FoodItem` có thể được ghi lại trong nhiều `FoodLog` của người dùng.
     * `mappedBy = "foodItem"` thiết lập mối quan hệ hai chiều, với trường `foodItem` trong `FoodLog`
     * là bên sở hữu mối quan hệ, chứa khóa ngoại tới `FoodItem`.
     * Danh sách này cho phép theo dõi lịch sử tiêu thụ của một mặt hàng thực phẩm cụ thể bởi người dùng.
     */
    @OneToMany(mappedBy = "foodItem")
    private List<FoodLog> foodLogs;
}