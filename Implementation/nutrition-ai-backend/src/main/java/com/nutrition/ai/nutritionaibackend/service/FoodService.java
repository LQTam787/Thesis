package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface cho việc quản lý các đối tượng {@link FoodItem} trong hệ thống.
 * Giao diện này định nghĩa các hoạt động nghiệp vụ cơ bản cho phép tương tác với dữ liệu món ăn,
 * bao gồm các thao tác CRUD (Tạo, Đọc, Cập nhật, Xóa) và tìm kiếm món ăn theo tên.
 * Mục tiêu là cung cấp một tầng trừu tượng cho logic nghiệp vụ liên quan đến món ăn,
 * tách biệt khỏi chi tiết triển khai cụ thể của lớp repository.
 */
public interface FoodService {

    /**
     * Lưu một đối tượng {@link FoodItem} vào cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link FoodItem} để lưu hoặc cập nhật.</li>
     *     <li>Uỷ quyền cho lớp repository tương ứng để thực hiện thao tác lưu/cập nhật dữ liệu vào cơ sở dữ liệu.</li>
     *     <li>Trả về đối tượng {@link FoodItem} đã được lưu bền vững, có thể bao gồm ID được tạo tự động nếu là đối tượng mới.</li>
     * </ol>
     *
     * @param foodItem Đối tượng FoodItem cần được lưu.
     * @return Đối tượng FoodItem đã được lưu bền vững.
     */
    FoodItem save(FoodItem foodItem);

    /**
     * Truy xuất tất cả các đối tượng {@link FoodItem} hiện có trong cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận yêu cầu truy xuất tất cả các món ăn.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code findAll()} nhằm lấy tất cả các bản ghi {@link FoodItem}.</li>
     *     <li>Trả về một danh sách (List) các đối tượng {@link FoodItem}.</li>
     * </ol>
     *
     * @return Danh sách các đối tượng FoodItem.
     */
    List<FoodItem> findAll();

    /**
     * Tìm kiếm một đối tượng {@link FoodItem} cụ thể bằng ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code findById(id)}.
     *         Phương thức này trả về một {@link Optional} để xử lý trường hợp không tìm thấy đối tượng.</li>
     *     <li>Trả về một {@link Optional<FoodItem>} chứa đối tượng nếu tìm thấy, hoặc một {@code Optional.empty()} nếu không.</li>
     * </ol>
     *
     * @param id ID của đối tượng FoodItem cần tìm.
     * @return Optional chứa đối tượng FoodItem nếu tìm thấy, ngược lại là Optional trống.
     */
    Optional<FoodItem> findOne(Long id);

    /**
     * Xóa một đối tượng {@link FoodItem} khỏi cơ sở dữ liệu dựa trên ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code deleteById(id)}.</li>
     *     <li>Thực hiện thao tác xóa. Phương thức này không trả về giá trị.</li>
     * </ol>
     *
     * @param id ID của đối tượng FoodItem cần xóa.
     */
    void delete(Long id);

    /**
     * Tìm kiếm các đối tượng {@link FoodItem} có tên chứa một chuỗi con cụ thể (tìm kiếm gần đúng).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một chuỗi {@code name} để tìm kiếm.</li>
     *     <li>Uỷ quyền cho lớp repository hoặc thực hiện logic tìm kiếm trong service (tùy thuộc vào thiết kế cụ thể)
     *         để truy vấn các món ăn có tên chứa chuỗi đã cho.</li>
     *     <li>Trả về một danh sách các {@link FoodItem} khớp với tiêu chí tìm kiếm.</li>
     * </ol>
     *
     * @param name Tên hoặc một phần của tên món ăn cần tìm kiếm.
     * @return Danh sách các đối tượng FoodItem có tên chứa chuỗi đã cho.
     */
    List<FoodItem> findByNameContaining(String name);
}