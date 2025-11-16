package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `FoodLog` ghi lại chi tiết về việc người dùng đã tiêu thụ một mặt hàng thực phẩm cụ thể.
 * Nó là một Entity JPA, ánh xạ tới bảng "food_logs" trong cơ sở dữ liệu.
 * Mỗi `FoodLog` theo dõi thời gian tiêu thụ, số lượng và đơn vị, đồng thời liên kết với người dùng và `FoodItem` tương ứng.
 *
 * Logic hoạt động:
 * - Ghi lại nhật ký ăn uống:
 *   - Mỗi nhật ký được định danh bằng một ID duy nhất (`id`) và ghi lại thời gian (`logDate`), số lượng (`quantity`) và đơn vị (`unit`) của `FoodItem` đã tiêu thụ.
 *   - Mục đích là cung cấp một cách chi tiết để người dùng theo dõi lượng thức ăn đã ăn và tính toán dinh dưỡng thực tế.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "food_logs")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `food_logs`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng và mặt hàng thực phẩm:
 *   - `@ManyToOne` với `User`: Một `FoodLog` thuộc về một `User` duy nhất. Khóa ngoại `user_id` được tạo.
 *   - `@ManyToOne` với `FoodItem`: Một `FoodLog` ghi lại việc tiêu thụ một `FoodItem` cụ thể. Khóa ngoại `food_item_id` được tạo.
 *   - `fetch = FetchType.LAZY`: Tối ưu hóa hiệu suất bằng cách chỉ tải các đối tượng `User` và `FoodItem` khi chúng thực sự được truy cập.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `FoodLog` được tạo khi người dùng ghi lại việc tiêu thụ một món ăn. Các chi tiết như `logDate`, `quantity`, `unit` được thiết lập.
 *   - `user` và `foodItem` được liên kết thông qua các đối tượng `User` và `FoodItem` hiện có, tạo khóa ngoại trong cơ sở dữ liệu.
 * - Truy vấn:
 *   - Khi truy vấn một `FoodLog`, các thuộc tính cơ bản của nhật ký sẽ được tải.
 *   - Các đối tượng `User` và `FoodItem` liên quan sẽ được tải theo kiểu `LAZY` khi được yêu cầu, cho phép truy cập thông tin chi tiết về người dùng và thực phẩm đã tiêu thụ.
 *   - Dữ liệu này là cơ sở để tạo báo cáo dinh dưỡng cá nhân và phân tích thói quen ăn uống.
 */
@Entity
@Table(name = "food_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodLog {
    /**
     * `id` là khóa chính duy nhất cho mỗi nhật ký ăn uống. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `FoodLog` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `logDate` ghi lại thời điểm chính xác (ngày và giờ) khi người dùng tiêu thụ mặt hàng thực phẩm này.
     * Nó là một trường quan trọng để theo dõi thói quen ăn uống theo thời gian.
     */
    private LocalDateTime logDate;

    /**
     * `quantity` lưu trữ số lượng mặt hàng thực phẩm đã được tiêu thụ. Ví dụ: 200 (gam), 1 (quả), 1.5 (cốc).
     * Giá trị này, cùng với `unit`, được sử dụng để tính toán tổng giá trị dinh dưỡng của lần tiêu thụ.
     */
    private Double quantity;

    /**
     * `unit` là đơn vị của số lượng đã tiêu thụ (ví dụ: "g" cho gram, "ml" cho mililit, "phần" cho khẩu phần).
     * Trường này cung cấp ngữ cảnh cho giá trị `quantity` và rất quan trọng để tính toán dinh dưỡng chính xác.
     */
    private String unit;

    /**
     * `user` là đối tượng `User` liên kết với nhật ký ăn uống này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `FoodLog` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `food_logs`, liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * `foodItem` là đối tượng `FoodItem` đã được tiêu thụ trong nhật ký này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `FoodLog` có thể ghi lại cùng một loại `FoodItem`.
     * `@JoinColumn(name = "food_item_id")` tạo một khóa ngoại `food_item_id` trong bảng `food_logs`, liên kết đến bảng `food_items`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `FoodItem` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
}