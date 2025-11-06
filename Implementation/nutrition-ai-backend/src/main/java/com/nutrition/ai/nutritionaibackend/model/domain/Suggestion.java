package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ESuggestionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Thể hiện một đề xuất hoặc khuyến nghị (ví dụ: về thực phẩm, hoạt động) được tạo bởi hệ thống.
 * Ánh xạ tới bảng "suggestions".
 */
@Entity
@Table(name = "suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nội dung của đề xuất.
     * @Lob: Lưu trữ dữ liệu lớn.
     */
    @Lob
    private String suggestionText;

    /**
     * Ngày và giờ đề xuất được tạo.
     */
    private LocalDateTime createdDate;

    /**
     * Thể loại đề xuất (ví dụ: NUTRITION, ACTIVITY).
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi.
     */
    @Enumerated(EnumType.STRING)
    private ESuggestionCategory category;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một Suggestion được tạo cho một User.
     * @JoinColumn(name = "user_id"): Khóa ngoại liên kết tới User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}