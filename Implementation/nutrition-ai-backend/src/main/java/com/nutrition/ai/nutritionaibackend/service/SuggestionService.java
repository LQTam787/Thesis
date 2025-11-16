package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Service Interface cho việc quản lý các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} trong hệ thống.
 * Giao diện này định nghĩa các hoạt động nghiệp vụ cơ bản cho phép tương tác với dữ liệu gợi ý dinh dưỡng,
 * bao gồm lưu gợi ý, truy xuất gợi ý theo người dùng, và tích hợp với các mô hình AI để tạo ra các đề xuất dinh dưỡng mới.
 * Nó cung cấp một tầng trừu tượng cho logic nghiệp vụ liên quan đến gợi ý dinh dưỡng.
 */
public interface SuggestionService {

    /**
     * Lưu một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} vào cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link SuggestionDto} chứa thông tin gợi ý cần lưu.</li>
     *     <li>Chuyển đổi {@link SuggestionDto} này thành một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} Entity.</li>
     *     <li>Uỷ quyền cho lớp repository tương ứng để thực hiện thao tác lưu Entity vào cơ sở dữ liệu.</li>
     *     <li>Trả về một {@link SuggestionDto} của gợi ý đã được lưu bền vững.
     *         (Thao tác này thường được thực hiện bởi người dùng hoặc hệ thống sau khi nhận được gợi ý từ AI).</li>
     * </ol>
     *
     * @param suggestionDto DTO chứa thông tin gợi ý dinh dưỡng cần lưu.
     * @return {@link SuggestionDto} của gợi ý đã được lưu bền vững.
     */
    SuggestionDto save(SuggestionDto suggestionDto);

    /**
     * Truy xuất tất cả các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion} được liên kết với một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link User} làm tham số, đại diện cho người dùng cần tìm gợi ý.</li>
     *     <li>Truy vấn cơ sở dữ liệu thông qua lớp repository để tìm tất cả các gợi ý dinh dưỡng
     *         có liên quan đến người dùng đã cho.</li>
     *     <li>Chuyển đổi danh sách các đối tượng Entity này thành danh sách các {@link SuggestionDto} và trả về.</li>
     * </ol>
     *
     * @param user Đối tượng User để tìm các gợi ý dinh dưỡng liên quan.
     * @return Danh sách các {@link SuggestionDto} chứa các gợi ý dinh dưỡng của người dùng.
     */
    List<SuggestionDto> findAllByUser(User user);

    /**
     * Lấy các đề xuất dinh dưỡng được tạo bởi Dịch vụ AI bên ngoài.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận dữ liệu đầu vào của người dùng, bao gồm hồ sơ ({@code userProfile}),
     *         sở thích ăn uống ({@code dietaryPreferences}), và mục tiêu sức khỏe ({@code healthGoals}).</li>
     *     <li>Gửi các dữ liệu này đến một Service AI chuyên biệt (ví dụ: {@link com.nutrition.ai.nutritionaibackend.service.ai.AiService})
     *         để xử lý và tạo ra các đề xuất dinh dưỡng.</li>
     *     <li>Nhận lại kết quả từ Service AI, thường là một {@link Map} chứa các đề xuất.</li>
     * </ol>
     *
     * @param userProfile Dữ liệu hồ sơ của người dùng (ví dụ: tuổi, cân nặng, chiều cao, giới tính).
     * @param dietaryPreferences Các ràng buộc hoặc sở thích ăn kiêng của người dùng (ví dụ: ăn chay, dị ứng gluten).
     * @param healthGoals Các mục tiêu sức khỏe của người dùng (ví dụ: giảm cân, tăng cơ, duy trì cân nặng).
     * @return Một {@link Map} chứa các đề xuất dinh dưỡng được tạo bởi Service AI.
     */
    Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals);
}