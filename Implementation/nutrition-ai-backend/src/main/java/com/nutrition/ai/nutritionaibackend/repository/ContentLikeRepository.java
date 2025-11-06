package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.ContentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link ContentLike} entities.
 * Provides methods for data access operations related to content likes.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository.
 * 2. Phương thức truy vấn tùy chỉnh: Tương tự như ContentCommentRepository, 
 * countBySharedContentId là một 'Phương thức tìm kiếm theo Tên' đếm số lượng.
 * 3. Tự động tạo truy vấn: Spring Data JPA tạo một truy vấn COUNT(*) trên 
 * thực thể ContentLike.
 * 4. Chi tiết truy vấn: Nó đếm số lượng bản ghi ContentLike:
 * - WHERE sharedContentId = [tham số sharedContentId].
 */
@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {
    long countBySharedContentId(Long sharedContentId);
}