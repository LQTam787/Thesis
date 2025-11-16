package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface cho việc quản lý các đối tượng {@link NutritionPlan} trong hệ thống.
 * Giao diện này định nghĩa các hoạt động nghiệp vụ cơ bản cho phép tương tác với dữ liệu kế hoạch dinh dưỡng,
 * bao gồm các thao tác CRUD (Tạo, Đọc, Cập nhật, Xóa) và tìm kiếm kế hoạch dinh dưỡng theo người dùng cụ thể.
 * Mục tiêu là cung cấp một tầng trừu tượng cho logic nghiệp vụ liên quan đến kế hoạch dinh dưỡng,
 * tách biệt khỏi chi tiết triển khai cụ thể của lớp repository.
 */
public interface NutritionPlanService {

    /**
     * Lưu một đối tượng {@link NutritionPlan} mới hoặc cập nhật một kế hoạch hiện có vào cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link NutritionPlan} để lưu hoặc cập nhật.</li>
     *     <li>Uỷ quyền cho lớp repository tương ứng để thực hiện thao tác lưu/cập nhật dữ liệu vào cơ sở dữ liệu.</li>
     *     <li>Trả về đối tượng {@link NutritionPlan} đã được lưu bền vững, có thể bao gồm ID được tạo tự động nếu là đối tượng mới.</li>
     * </ol>
     *
     * @param nutritionPlan Đối tượng NutritionPlan cần được lưu.\n     * @return Đối tượng NutritionPlan đã được lưu bền vững.
     */
    NutritionPlan save(NutritionPlan nutritionPlan);

    /**
     * Truy xuất tất cả các đối tượng {@link NutritionPlan} hiện có trong hệ thống.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận yêu cầu truy xuất tất cả các kế hoạch dinh dưỡng.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code findAll()} nhằm lấy tất cả các bản ghi {@link NutritionPlan}.</li>
     *     <li>Trả về một danh sách (List) các đối tượng {@link NutritionPlan}.</li>
     * </ol>
     *
     * @return Danh sách các đối tượng NutritionPlan.
     */
    List<NutritionPlan> findAll();

    /**
     * Tìm kiếm một đối tượng {@link NutritionPlan} cụ thể bằng ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code findById(id)}.
     *         Phương thức này trả về một {@link Optional} để xử lý trường hợp không tìm thấy đối tượng.</li>
     *     <li>Trả về một {@link Optional<NutritionPlan>} chứa đối tượng nếu tìm thấy, hoặc một {@code Optional.empty()} nếu không.</li>
     * </ol>
     *
     * @param id ID của đối tượng NutritionPlan cần tìm.
     * @return Optional chứa đối tượng NutritionPlan nếu tìm thấy, ngược lại là Optional trống.
     */
    Optional<NutritionPlan> findOne(Long id);

    /**
     * Xóa một đối tượng {@link NutritionPlan} khỏi cơ sở dữ liệu dựa trên ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi phương thức {@code deleteById(id)}.</li>
     *     <li>Thực hiện thao tác xóa. Phương thức này không trả về giá trị.</li>
     * </ol>
     *
     * @param id ID của đối tượng NutritionPlan cần xóa.
     */
    void delete(Long id);

    /**
     * Tìm kiếm tất cả các đối tượng {@link NutritionPlan} được liên kết với một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link User} làm tham số, đại diện cho người dùng cần tìm kế hoạch.</li>
     *     <li>Uỷ quyền cho lớp repository để gọi một phương thức tùy chỉnh (ví dụ: {@code findByUser(User user)})
     *         được định nghĩa trong NutritionPlanRepository.</li>
     *     <li>Trả về một danh sách các đối tượng {@link NutritionPlan} thuộc về người dùng đã cho.</li>
     * </ol>
     *
     * @param user Đối tượng User để tìm các kế hoạch dinh dưỡng liên quan.
     * @return Danh sách các đối tượng NutritionPlan được liên kết với người dùng đã cho.
     */
    List<NutritionPlan> findAllByUser(User user);
}