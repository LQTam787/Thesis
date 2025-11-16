package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link NutritionPlanService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các hoạt động quản lý {@link NutritionPlan},
 * bao gồm các thao tác CRUD (Tạo, Đọc, Cập nhật, Xóa) và tìm kiếm kế hoạch dinh dưỡng theo người dùng.
 * Nó tương tác với {@link NutritionPlanRepository} để truy cập dữ liệu kế hoạch dinh dưỡng
 * và {@link UserRepository} để đảm bảo tính hợp lệ của người dùng liên quan đến kế hoạch.
 * Nguyên tắc Tiêm phụ thuộc (Dependency Injection) được sử dụng thông qua constructor.
 */
@Service
public class NutritionPlanServiceImpl implements NutritionPlanService {

    /**
     * {@code nutritionPlanRepository} là giao diện repository để truy cập dữ liệu của {@link NutritionPlan}.
     * Được tiêm thông qua constructor để thực hiện các thao tác CRUD với kế hoạch dinh dưỡng.
     */
    private final NutritionPlanRepository nutritionPlanRepository;
    /**
     * {@code userRepository} là giao diện repository để truy cập dữ liệu của {@link User}.
     * Được tiêm thông qua constructor để tìm kiếm và xác thực người dùng liên quan đến kế hoạch dinh dưỡng.
     */
    private final UserRepository userRepository;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết.
     * {@code NutritionPlanRepository} và {@code UserRepository} được tự động cung cấp bởi Spring
     * khi tạo bean {@link NutritionPlanServiceImpl}, đảm bảo các dependency này luôn sẵn sàng.
     *
     * @param nutritionPlanRepository Repository để quản lý các kế hoạch dinh dưỡng.
     * @param userRepository Repository để quản lý thông tin người dùng.
     */
    public NutritionPlanServiceImpl(NutritionPlanRepository nutritionPlanRepository, UserRepository userRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lưu một đối tượng {@link NutritionPlan} mới hoặc cập nhật một kế hoạch hiện có vào cơ sở dữ liệu.
     * Phương thức này đảm bảo rằng đối tượng {@link User} liên kết với kế hoạch là một entity được quản lý (managed entity).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Xác thực và Tải User:</b> Lấy ID của người dùng từ đối tượng {@code nutritionPlan} và truy vấn
     *         {@code userRepository} để tìm {@link User} Entity. Nếu không tìm thấy người dùng, ném {@link RuntimeException}.
     *         Điều này quan trọng để tránh {@code TransientObjectException} khi lưu {@code NutritionPlan}.</li>
     *     <li><b>Gán lại User Entity:</b> Gán lại đối tượng {@link User} (đã được tải và quản lý bởi Persistence Context)
     *         vào {@code nutritionPlan}.</li>
     *     <li><b>Lưu NutritionPlan:</b> Gọi {@code nutritionPlanRepository.save()} để lưu hoặc cập nhật đối tượng
     *         {@code NutritionPlan} vào cơ sở dữ liệu.</li>
     * </ol>
     *
     * @param nutritionPlan Đối tượng NutritionPlan cần được lưu hoặc cập nhật.
     * @return Đối tượng NutritionPlan đã được lưu bền vững.
     * @throws RuntimeException nếu không tìm thấy người dùng liên kết với ID đã cho.
     */
    @Override
    public NutritionPlan save(NutritionPlan nutritionPlan) {
        // Luồng hoạt động: Lưu/Cập nhật Kế hoạch Dinh dưỡng. Cần đảm bảo User liên kết là hợp lệ và được quản lý.
        // 1. Tìm User trong DB bằng ID từ NutritionPlan.
        User user = userRepository.findById(nutritionPlan.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + nutritionPlan.getUser().getId()));
        // 2. Gán lại entity User được quản lý (managed entity) vào NutritionPlan
        // để đảm bảo tính nhất quán trong Persistence Context và tránh lỗi TransientObjectException.
        nutritionPlan.setUser(user); 
        // 3. Lưu NutritionPlan vào cơ sở dữ liệu
        return nutritionPlanRepository.save(nutritionPlan);
    }

    /**
     * Truy xuất tất cả các đối tượng {@link NutritionPlan} hiện có trong hệ thống.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Phương thức này đơn giản uỷ quyền cuộc gọi đến {@code nutritionPlanRepository.findAll()}.</li>
     *     <li>Trả về một danh sách (List) chứa tất cả các {@link NutritionPlan} từ cơ sở dữ liệu.</li>
     * </ol>
     *
     * @return Danh sách các đối tượng NutritionPlan.
     */
    @Override
    public List<NutritionPlan> findAll() {
        // Luồng hoạt động: Lấy tất cả các Kế hoạch Dinh dưỡng từ repository.
        return nutritionPlanRepository.findAll();
    }

    /**
     * Tìm kiếm một đối tượng {@link NutritionPlan} cụ thể bằng ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho {@code nutritionPlanRepository} để gọi phương thức {@code findById(id)}.
     *         Phương thức này trả về một {@link Optional} để xử lý trường hợp không tìm thấy đối tượng.</li>
     *     <li>Trả về một {@link Optional<NutritionPlan>} chứa đối tượng nếu tìm thấy, hoặc một {@code Optional.empty()} nếu không.
     *         Sử dụng Optional giúp xử lý an toàn trường hợp không tìm thấy dữ liệu.</li>
     * </ol>
     *
     * @param id ID của đối tượng NutritionPlan cần tìm.
     * @return Optional chứa đối tượng NutritionPlan nếu tìm thấy, ngược lại là Optional trống.
     */
    @Override
    public Optional<NutritionPlan> findOne(Long id) {
        // Luồng hoạt động: Tìm Kế hoạch Dinh dưỡng theo ID từ repository.
        return nutritionPlanRepository.findById(id);
    }

    /**
     * Xóa một đối tượng {@link NutritionPlan} khỏi cơ sở dữ liệu dựa trên ID của nó.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một ID là tham số.</li>
     *     <li>Uỷ quyền cho {@code nutritionPlanRepository} để gọi phương thức {@code deleteById(id)}.</li>
     *     <li>Thực hiện thao tác xóa bản ghi khỏi cơ sở dữ liệu. Phương thức này không trả về giá trị.</li>
     * </ol>
     *
     * @param id ID của đối tượng NutritionPlan cần xóa.
     */
    @Override
    public void delete(Long id) {
        // Luồng hoạt động: Xóa Kế hoạch Dinh dưỡng theo ID từ repository.
        nutritionPlanRepository.deleteById(id);
    }

    /**
     * Tìm kiếm tất cả các đối tượng {@link NutritionPlan} được liên kết với một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link User} làm tham số, đại diện cho người dùng cần tìm kế hoạch.</li>
     *     <li>Uỷ quyền cho {@code nutritionPlanRepository} để gọi phương thức {@code findByUser(User user)}.
     *         Phương thức này được tạo tự động bởi Spring Data JPA dựa trên quy ước đặt tên và mối quan hệ entity.</li>
     *     <li>Trả về một danh sách các đối tượng {@link NutritionPlan} thuộc về người dùng đã cho.</li>
     * </ol>
     *
     * @param user Đối tượng User để tìm các kế hoạch dinh dưỡng liên quan.
     * @return Danh sách các đối tượng NutritionPlan được liên kết với người dùng đã cho.
     */
    @Override
    public List<NutritionPlan> findAllByUser(User user) {
        // Luồng hoạt động: Tìm tất cả Kế hoạch Dinh dưỡng thuộc về một User cụ thể từ repository.
        return nutritionPlanRepository.findByUser(user);
    }
}