package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Thể hiện một báo cáo tổng hợp (ví dụ: Tình trạng dinh dưỡng hàng tháng) được tạo cho người dùng.
 * Ánh xạ tới bảng "reports".
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Loại báo cáo (ví dụ: NUTRITION_SUMMARY, PROGRESS_REPORT).
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi trong CSDL.
     */
    @Enumerated(EnumType.STRING)
    private EReportType reportType;

    /**
     * Ngày và giờ báo cáo được tạo.
     */
    private LocalDateTime generatedDate;

    /**
     * Nội dung chi tiết của báo cáo (thường là một chuỗi JSON hoặc TEXT).
     * @Lob: Lưu trữ dữ liệu lớn.
     */
    @Lob
    private String content;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một Report thuộc về một User.
     * @JoinColumn(name = "user_id"): Khóa ngoại liên kết tới User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}