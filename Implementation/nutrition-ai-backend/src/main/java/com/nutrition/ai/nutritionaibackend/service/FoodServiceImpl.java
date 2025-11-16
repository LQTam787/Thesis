package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.repository.FoodItemRepository;
import com.nutrition.ai.nutritionaibackend.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link FoodService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các thao tác với {@link FoodItem},
 * tương tác trực tiếp với {@link FoodItemRepository} để truy cập dữ liệu
 * và {@link RecipeRepository} để quản lý các mối quan hệ liên quan đến công thức.
 * Nó tuân thủ nguyên tắc Tiêm phụ thuộc (Dependency Injection) thông qua constructor.
 */
@Service
public class FoodServiceImpl implements FoodService {

    /**
     * {@code foodItemRepository} là giao diện repository để truy cập dữ liệu của {@link FoodItem}.
     * Được tiêm thông qua constructor.
     */
    private final FoodItemRepository foodItemRepository;
    /**
     * {@code recipeRepository} là giao diện repository để truy cập dữ liệu của {@link com.nutrition.ai.nutritionaibackend.model.domain.Recipe}.
     * Được tiêm thông qua constructor.
     */
    private final RecipeRepository recipeRepository;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết.
     * {@code FoodItemRepository} và {@code RecipeRepository} được tự động cung cấp bởi Spring
     * khi tạo bean {@link FoodServiceImpl}, đảm bảo các dependency này luôn sẵn sàng.
     *
     * @param foodItemRepository Repository để quản lý các món ăn.
     * @param recipeRepository Repository để quản lý các công thức nấu ăn.
     */
    public FoodServiceImpl(FoodItemRepository foodItemRepository, RecipeRepository recipeRepository) {
        this.foodItemRepository = foodItemRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Lưu một đối tượng {@link FoodItem} vào cơ sở dữ liệu.
     * Phương thức này xử lý mối quan hệ song hướng giữa {@link FoodItem} và {@link com.nutrition.ai.nutritionaibackend.model.domain.Recipe}.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Kiểm tra xem {@code foodItem} có liên kết với một {@link com.nutrition.ai.nutritionaibackend.model.domain.Recipe} hay không.</li>
     *     <li>Nếu có, thiết lập mối quan hệ song hướng bằng cách đặt {@code foodItem} vào {@code Recipe} đó.</li>
     *     <li>Thực hiện lưu {@code Recipe} trước (nếu cần thiết để đảm bảo {@code Recipe} có ID và được cập nhật/lưu).
     *         Điều này quan trọng để duy trì tính toàn vẹn tham chiếu.</li>
     *     <li>Cuối cùng, lưu {@code foodItem} vào cơ sở dữ liệu thông qua {@code foodItemRepository}.</li>
     * </ol>
     *
     * @param foodItem Đối tượng FoodItem cần được lưu.
     * @return Đối tượng FoodItem đã được lưu bền vững.
     */
    @Override
    public FoodItem save(FoodItem foodItem) {
        // Luồng hoạt động: Lưu FoodItem.
        // Logic Song hướng: Nếu FoodItem có Recipe được liên kết, cần thiết lập mối quan hệ song hướng
        // và lưu Recipe trước để đảm bảo tính toàn vẹn dữ liệu.
        if (foodItem.getRecipe() != null) {
            foodItem.getRecipe().setFoodItem(foodItem);
            // Lưu Recipe trước để đảm bảo Recipe có ID và được cập nhật/lưu trong DB
            recipeRepository.save(foodItem.getRecipe());
        }
        // Lưu FoodItem vào cơ sở dữ liệu
        return foodItemRepository.save(foodItem);
    }

    /**
     * Truy xuất tất cả các đối tượng {@link FoodItem} hiện có trong cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Phương thức này đơn giản uỷ quyền cuộc gọi đến {@code foodItemRepository.findAll()}.</li>
     *     <li>Trả về một danh sách (List) chứa tất cả các {@link FoodItem} từ cơ sở dữ liệu.</li>
     * </ol>
     *
     * @return Danh sách các đối tượng FoodItem.
     */
    @Override
    public List<FoodItem> findAll() {
        // Luồng hoạt động: Truy xuất tất cả FoodItem từ repository.
        return foodItemRepository.findAll();
    }

    /**
     * Tìm kiếm một đối tượng {@link FoodItem} cụ thể bằng ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID (Long) là tham số.</li>
     *     <li>Uỷ quyền cuộc gọi đến {@code foodItemRepository.findById(id)}.</li>
     *     <li>Trả về một {@link Optional<FoodItem>} chứa đối tượng nếu tìm thấy, hoặc một {@code Optional.empty()} nếu không.
     *         Sử dụng Optional giúp xử lý an toàn trường hợp không tìm thấy dữ liệu.</li>
     * </ol>
     *
     * @param id ID của đối tượng FoodItem cần tìm.
     * @return Optional chứa đối tượng FoodItem nếu tìm thấy, ngược lại là Optional trống.
     */
    @Override
    public Optional<FoodItem> findOne(Long id) {
        // Luồng hoạt động: Tìm FoodItem theo ID từ repository.
        return foodItemRepository.findById(id);
    }

    /**
     * Xóa một đối tượng {@link FoodItem} khỏi cơ sở dữ liệu dựa trên ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID (Long) là tham số.</li>
     *     <li>Uỷ quyền cuộc gọi đến {@code foodItemRepository.deleteById(id)}.</li>
     *     <li>Thực hiện thao tác xóa bản ghi khỏi cơ sở dữ liệu. Phương thức này không trả về giá trị.</li>
     * </ol>
     *
     * @param id ID của đối tượng FoodItem cần xóa.
     */
    @Override
    public void delete(Long id) {
        // Luồng hoạt động: Xóa FoodItem theo ID từ repository.
        foodItemRepository.deleteById(id);
    }

    /**
     * Tìm kiếm các đối tượng {@link FoodItem} có tên chứa một chuỗi con cụ thể (tìm kiếm gần đúng).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Lấy tất cả các {@link FoodItem} từ cơ sở dữ liệu thông qua {@code foodItemRepository.findAll()}.</li>
     *     <li>Sử dụng Java Stream API để lọc danh sách các món ăn này trong bộ nhớ.
     *         <ul>
     *             <li>Nếu chuỗi tìm kiếm {@code name} là {@code null} hoặc trống, trả về tất cả các món ăn.</li>
     *             <li>Ngược lại, so sánh tên của từng món ăn với chuỗi tìm kiếm (không phân biệt chữ hoa/chữ thường)
     *                 bằng cách chuyển đổi cả hai về chữ thường và sử dụng phương thức {@code contains()}.</li>
     *         </ul>
     *     </li>
     *     <li>Thu thập các món ăn khớp vào một danh sách mới và trả về.</li>
     * </ol>
     * <p><b>Lưu ý về hiệu suất:</b></p>
     * Việc lấy tất cả dữ liệu từ DB và lọc trong bộ nhớ có thể kém hiệu quả đối với tập dữ liệu lớn.
     * Một cách tiếp cận tốt hơn là định nghĩa phương thức tìm kiếm {@code findByNameContainingIgnoreCase(String name)}
     * trực tiếp trong {@code FoodItemRepository} để cơ sở dữ liệu xử lý việc lọc.
     *
     * @param name Tên hoặc một phần của tên món ăn cần tìm kiếm.
     * @return Danh sách các đối tượng FoodItem có tên chứa chuỗi đã cho.
     */
    @Override
    public List<FoodItem> findByNameContaining(String name) {
        // Luồng hoạt động: Tìm kiếm FoodItem có tên chứa chuỗi đã cho (không phân biệt chữ hoa/chữ thường).
        // Nguyên lý hoạt động (Tìm kiếm tại Service - kém hiệu quả hơn tìm kiếm tại Repository):
        // 1. Lấy tất cả các FoodItem từ DB (`findAll()`).
        // 2. Lọc (filter) danh sách trong bộ nhớ (Stream API) dựa trên tên.
        // 3. Chuyển đổi cả tên FoodItem và chuỗi tìm kiếm thành chữ thường để so sánh.
        return foodItemRepository.findAll().stream()
            .filter(food -> {
                if (name == null || name.isEmpty()) {
                    return true; // Trả về tất cả các món ăn nếu tên là null hoặc trống
                }
                return food.getName().toLowerCase().contains(name.toLowerCase());
            })
            .collect(Collectors.toList());
    }
}