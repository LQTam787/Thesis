package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.ContentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link ContentComment} entities.
 * Provides methods for data access operations related to content comments.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository.
 * 2. Phương thức truy vấn tùy chỉnh: Phương thức countBySharedContentId 
 * là một 'Phương thức tìm kiếm theo Tên' đặc biệt.
 * 3. Tự động tạo truy vấn: Spring Data JPA sẽ tạo một truy vấn 
 * COUNT(*) dựa trên thực thể ContentComment.
 * 4. Chi tiết truy vấn: Nó đếm số lượng bản ghi ContentComment:
 * - WHERE sharedContentId = [tham số sharedContentId].
 * - Kết quả trả về là kiểu 'long'.
 */
@Repository
public interface ContentCommentRepository extends JpaRepository<ContentComment, Long> {
    long countBySharedContentId(Long sharedContentId);
}