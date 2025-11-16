package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp `Recipe` đại diện cho một công thức nấu ăn chi tiết trong hệ thống dinh dưỡng.
 * Nó là một Entity JPA, ánh xạ tới bảng "recipes" trong cơ sở dữ liệu.
 * Mỗi `Recipe` có thể liên kết One-to-One với một `FoodItem` (ví dụ: mô tả cách làm món "Salad gà" là một `FoodItem`).
 * Nó lưu trữ hướng dẫn chế biến và thời gian chuẩn bị.
 *
 * Logic hoạt động:
 * - Định nghĩa công thức nấu ăn:
 *   - Mỗi `Recipe` có một ID duy nhất (`id`), `instructions` chi tiết và `preparationTime`.
 *   - `instructions` sử dụng `@Lob` để lưu trữ chuỗi văn bản dài.
 *   - Mục đích là để cung cấp hướng dẫn đầy đủ cho người dùng muốn tự chế biến món ăn.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "recipes")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `recipes`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ One-to-One với `FoodItem`:
 *   - `@OneToOne` với `FoodItem`: Một `Recipe` liên kết với một `FoodItem` duy nhất. Điều này cho phép một `FoodItem` có thêm thông tin công thức nấu ăn.
 *   - `cascade = CascadeType.ALL`: Đảm bảo rằng mọi thao tác trên `Recipe` sẽ được áp dụng cho `FoodItem` liên quan (ví dụ: khi xóa `Recipe`, `FoodItem` liên kết cũng có thể bị xóa nếu `orphanRemoval` được cấu hình trên phía `FoodItem`).
 *   - `@JoinColumn(name = "food_item_id", referencedColumnName = "id")`: Tạo khóa ngoại `food_item_id` trong bảng `recipes` trỏ tới cột `id` của bảng `food_items`.
 *   - `fetch = FetchType.LAZY`: Tối ưu hóa hiệu suất bằng cách chỉ tải đối tượng `FoodItem` khi cần.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Recipe` được tạo hoặc cập nhật, với hướng dẫn và thời gian chuẩn bị.
 *   - `FoodItem` được liên kết với `Recipe` (hoặc tạo mới nếu chưa tồn tại) để thiết lập mối quan hệ.
 * - Truy vấn:
 *   - Khi truy vấn một `Recipe`, các thuộc tính của công thức được tải.
 *   - Đối tượng `FoodItem` liên quan sẽ được tải `LAZY` khi được yêu cầu, cho phép truy cập thông tin dinh dưỡng cơ bản của món ăn.
 *   - `instructions` và `preparationTime` được hiển thị cho người dùng để họ có thể làm theo công thức.
 */
@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    /**
     * `id` là khóa chính duy nhất cho mỗi công thức. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Recipe` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `foodItem` là đối tượng `FoodItem` mà công thức này mô tả cách chế biến.
     * Mối quan hệ `@OneToOne` chỉ ra rằng một `Recipe` liên kết với một `FoodItem` duy nhất.
     * `cascade = CascadeType.ALL` đảm bảo rằng các thao tác trên `Recipe` cũng ảnh hưởng đến `FoodItem` liên quan.
     * `@JoinColumn(name = "food_item_id", referencedColumnName = "id")` tạo một khóa ngoại `food_item_id` trong bảng `recipes`,
     * tham chiếu đến cột `id` của bảng `food_items`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `FoodItem` chỉ được tải khi nó được truy cập lần đầu.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", referencedColumnName = "id")
    private FoodItem foodItem;

    /**
     * `instructions` là hướng dẫn chi tiết từng bước để chế biến món ăn.
     * @Lob chỉ định rằng trường này là một đối tượng lớn, phù hợp để lưu trữ dữ liệu văn bản dài.
     */
    @Lob
    private String instructions;

    /**
     * `preparationTime` là thời gian cần thiết để chuẩn bị món ăn, tính bằng phút.
     * Trường này giúp người dùng ước tính thời gian cần thiết để nấu ăn.
     */
    private Integer preparationTime; // in minutes
}