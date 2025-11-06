package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Profile entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<Profile, Long>.
 * 2. Tự động triển khai: Spring Data JPA sẽ tự động tạo một triển khai 
 * cung cấp các phương thức CRUD (ví dụ: save, findById, findAll) cho thực thể Profile.
 * 3. Mục đích: Nó hoạt động như một lớp trừu tượng để tương tác với bảng 'Profile' trong cơ sở dữ liệu.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}