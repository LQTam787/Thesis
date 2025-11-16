package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.Suggestion;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.SuggestionRepository;
import com.nutrition.ai.nutritionaibackend.service.ai.AiService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link SuggestionService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các hoạt động quản lý {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion},
 * bao gồm lưu gợi ý, truy xuất gợi ý theo người dùng, và đặc biệt là tích hợp với {@link AiService}
 * để lấy các đề xuất dinh dưỡng thông minh từ mô hình AI.
 * Nó sử dụng {@link ModelMapper} để chuyển đổi giữa các đối tượng DTO và Entity một cách linh hoạt.
 * Nguyên tắc Tiêm phụ thuộc (Dependency Injection) được sử dụng thông qua constructor.
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

    /**
     * {@code suggestionRepository} là giao diện repository để truy cập dữ liệu của {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion}.
     * Được tiêm thông qua constructor để thực hiện các thao tác CRUD với gợi ý dinh dưỡng.
     */
    private final SuggestionRepository suggestionRepository;
    /**
     * {@code aiService} là một dịch vụ chuyên biệt để giao tiếp với các API AI bên ngoài.
     * Được tiêm thông qua constructor để lấy các đề xuất dinh dưỡng từ AI.
     */
    private final AiService aiService;
    /**
     * {@code modelMapper} được sử dụng để chuyển đổi giữa các đối tượng Entity (Domain Model)
     * và DTO (Data Transfer Object) một cách tự động và linh hoạt.
     * Được tiêm thông qua constructor.
     */
    private final ModelMapper modelMapper;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết.
     * {@code SuggestionRepository}, {@code AiService} và {@code ModelMapper} được tự động cung cấp bởi Spring
     * khi tạo bean {@link SuggestionServiceImpl}, đảm bảo các dependency này luôn sẵn sàng.
     *
     * @param suggestionRepository Repository để quản lý các gợi ý dinh dưỡng.
     * @param aiService Dịch vụ để giao tiếp với AI Service.
     * @param modelMapper Đối tượng ModelMapper để chuyển đổi DTO/Entity.
     */
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, AiService aiService, ModelMapper modelMapper) {
        this.suggestionRepository = suggestionRepository;
        this.aiService = aiService;
        this.modelMapper = modelMapper;
    }

    /**
     * Lưu một đối tượng {@link SuggestionDto} vào cơ sở dữ liệu.
     * Phương thức này xử lý việc chuyển đổi từ DTO sang Entity, lưu vào DB, và chuyển đổi lại sang DTO.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Chuyển đổi DTO sang Entity:</b> Ánh xạ {@link SuggestionDto} đầu vào thành một đối tượng
     *         {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} Entity sử dụng {@code modelMapper}.</li>
     *     <li><b>Lưu Entity:</b> Gọi {@code suggestionRepository.save()} để lưu đối tượng {@code Suggestion} Entity
     *         vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi Entity đã lưu sang DTO:</b> Ánh xạ đối tượng {@code Suggestion} Entity đã được lưu
     *         trở lại thành một {@link SuggestionDto} để trả về cho client.</li>
     * </ol>
     *
     * @param suggestionDto DTO chứa thông tin gợi ý dinh dưỡng cần lưu.
     * @return {@link SuggestionDto} của gợi ý đã được lưu bền vững.
     */
    @Override
    public SuggestionDto save(SuggestionDto suggestionDto) {
        // Luồng hoạt động: Lưu Suggestion DTO. Chuyển đổi DTO -> Entity -> Lưu -> Entity -> DTO.
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        return modelMapper.map(suggestionRepository.save(suggestion), SuggestionDto.class);
    }

    /**
     * Tìm kiếm tất cả các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} được liên kết với một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Lấy tất cả Suggestion:</b> Phương thức này hiện tại lấy TẤT CẢ các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion}
     *         từ cơ sở dữ liệu thông qua {@code suggestionRepository.findAll()}.</li>
     *     <li><b>Lọc trong bộ nhớ:</b> Sử dụng Java Stream API để lọc danh sách trong bộ nhớ, chỉ giữ lại những gợi ý
     *         mà có thuộc tính {@code user} khớp với đối tượng {@link User} được cung cấp.</li>
     *     <li><b>Chuyển đổi Entity sang DTO:</b> Ánh xạ từng đối tượng {@code Suggestion} đã lọc thành {@link SuggestionDto}
     *         sử dụng {@code modelMapper}.</li>
     *     <li><b>Thu thập kết quả:</b> Thu thập các DTO đã chuyển đổi vào một {@link List} và trả về.</li>
     * </ol>
     * <p><b>Lưu ý về hiệu suất:</b></p>
     * Việc lấy tất cả dữ liệu từ DB và lọc trong bộ nhớ có thể kém hiệu quả đối với tập dữ liệu lớn.
     * Một cách tiếp cận tốt hơn là định nghĩa phương thức tìm kiếm {@code findByUser(User user)}
     * trực tiếp trong {@code SuggestionRepository} để cơ sở dữ liệu xử lý việc lọc hiệu quả hơn.
     *
     * @param user Đối tượng User để tìm các gợi ý dinh dưỡng liên quan.
     * @return Danh sách các {@link SuggestionDto} chứa các gợi ý dinh dưỡng của người dùng.
     */
    @Override
    public List<SuggestionDto> findAllByUser(User user) {
        // Luồng hoạt động: Tìm tất cả Gợi ý Dinh dưỡng của một User.
        // Nguyên lý hoạt động (Lọc trong bộ nhớ - cần tối ưu hóa bằng cách dùng `findByUser` ở Repository):
        return suggestionRepository.findAll().stream()
            .filter(suggestion -> suggestion.getUser().equals(user))
            .map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class))
            .collect(Collectors.toList());
    }

    /**
     * Lấy các đề xuất dinh dưỡng được tạo bởi Dịch vụ AI bên ngoài.
     * Phương thức này uỷ quyền việc giao tiếp với AI cho {@link AiService}.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận dữ liệu đầu vào của người dùng, bao gồm hồ sơ ({@code userProfile}),
     *         sở thích ăn uống ({@code dietaryPreferences}), và mục tiêu sức khỏe ({@code healthGoals}).</li>
     *     <li>Uỷ quyền hoàn toàn cuộc gọi đến phương thức {@link AiService#getNutritionRecommendations(Map, Map, Map)}
     *         của {@code AiService} để xử lý việc giao tiếp với AI.</li>
     *     <li>Nhận và trả về kết quả từ {@code AiService}, thường là một {@link Map} chứa các đề xuất dinh dưỡng.</li>
     * </ol>
     * <p><b>Nguyên lý hoạt động (Tách biệt trách nhiệm):</b></p>
     * Việc uỷ quyền này giúp giữ cho {@code SuggestionService} tập trung vào logic nghiệp vụ quản lý gợi ý,
     * trong khi {@code AiService} chịu trách nhiệm duy nhất về giao tiếp với các mô hình AI, tuân thủ nguyên tắc trách nhiệm đơn lẻ (Single Responsibility Principle).
     *
     * @param userProfile Dữ liệu hồ sơ của người dùng (ví dụ: tuổi, cân nặng, chiều cao, giới tính).
     * @param dietaryPreferences Các ràng buộc hoặc sở thích ăn kiêng của người dùng (ví dụ: ăn chay, dị ứng gluten).
     * @param healthGoals Các mục tiêu sức khỏe của người dùng (ví dụ: giảm cân, tăng cơ, duy trì cân nặng).
     * @return Một {@link Map} chứa các đề xuất dinh dưỡng được tạo bởi Service AI.
     */
    @Override
    public Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals) {
        // Luồng hoạt động: Lấy đề xuất từ AI. Uỷ quyền hoàn toàn cho AiService.
        return aiService.getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals);
    }
}