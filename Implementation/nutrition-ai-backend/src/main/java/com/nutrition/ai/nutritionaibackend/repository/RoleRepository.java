package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.Role;
import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Role entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository<Role, Integer>.
 * 2. Khóa chính: Khóa chính của thực thể Role có kiểu là Integer.
 * 3. Phương thức truy vấn tùy chỉnh: findByName là 'Phương thức tìm kiếm theo Tên'.
 * 4. Tự động tạo truy vấn: Spring Data JPA tạo truy vấn để tìm một Role
 * - WHERE name = [tham số name].
 * 5. Optional Return: Sử dụng Optional<Role> để xử lý trường hợp không tìm thấy vai trò.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find.
     * @return an Optional containing the role if found, or empty otherwise.
     */
    Optional<Role> findByName(ERole name);
}