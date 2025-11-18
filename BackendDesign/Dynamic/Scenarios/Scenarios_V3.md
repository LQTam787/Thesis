# Kịch bản Thiết kế chi tiết - Module Xác thực (Phiên bản 3)

## 1. Use Case: Đăng ký tài khoản

- **Tóm tắt**: Người dùng cung cấp thông tin cá nhân để tạo tài khoản mới trong hệ thống.
- **Actor**: Người dùng khách (Guest User)

### Luồng sự kiện chính (Thành công)

1.  Người dùng gửi yêu cầu `GET /register` đến hệ thống.
2.  `AuthController` xử lý yêu cầu, gọi phương thức `showRegistrationForm()`.
3.  Hệ thống trả về giao diện `register.html`.
4.  Người dùng điền thông tin vào form và gửi yêu cầu `POST /register` với `RegistrationDto` (chứa `fullName`, `email`, `username`, `password`).
5.  `AuthController` nhận yêu cầu và gọi phương thức `registerUser(registrationDto)`.
6.  Bên trong `registerUser`, `AuthService` được gọi: `authService.register(registrationDto)`.
7.  `AuthService` thực hiện:
    a.  Gọi `userRepository.findByEmail(email)` để kiểm tra email đã tồn tại chưa.
    b.  Nếu chưa tồn tại, mã hóa mật khẩu bằng `passwordEncoder.encode(password)`.
    c.  Tạo một đối tượng `User` mới từ `RegistrationDto`.
    d.  Gọi `userRepository.save(user)` để lưu người dùng vào cơ sở dữ liệu.
8.  `AuthService` trả về kết quả thành công cho `AuthController`.
9.  `AuthController` chuyển hướng người dùng đến trang đăng nhập (`redirect:/login?success`).

### Luồng rẽ nhánh (Ngoại lệ)

- **4a. Dữ liệu không hợp lệ**: 
  - Tại bước 5, `RegistrationDto` không vượt qua được các ràng buộc `@Valid`.
  - Hệ thống trả về giao diện `register.html` kèm theo các thông báo lỗi tương ứng.
- **7a. Email đã tồn tại**:
  - `userRepository.findByEmail(email)` trả về một đối tượng `User`.
  - `AuthService` ném ra một ngoại lệ `UserAlreadyExistsException`.
  - `GlobalExceptionHandler` bắt ngoại lệ và trả về trang `register.html` với thông báo lỗi "Email đã được sử dụng".

## 2. Use Case: Đăng nhập

- **Tóm tắt**: Người dùng đã có tài khoản đăng nhập vào hệ thống.
- **Actor**: Người dùng (User)

### Luồng sự kiện chính (Thành công)

1.  Người dùng gửi yêu cầu `GET /login` đến hệ thống.
2.  `AuthController` xử lý yêu cầu, gọi phương thức `showLoginForm()`.
3.  Hệ thống trả về giao diện `login.html`.
4.  Người dùng điền email, mật khẩu và gửi yêu cầu `POST /login` với `LoginDto`.
5.  `AuthController` nhận yêu cầu và gọi `loginUser(loginDto)`.
6.  Bên trong `loginUser`, `AuthService` được gọi: `authService.login(loginDto)`.
7.  `AuthService` thực hiện:
    a.  Gọi `authenticationManager.authenticate()` với `UsernamePasswordAuthenticationToken`.
    b.  `AuthenticationManager` sử dụng `UserDetailsService` (do ta cài đặt) để tải thông tin người dùng từ `userRepository.findByEmail(email)`.
    c.  So sánh mật khẩu đã mã hóa trong DB với mật khẩu người dùng cung cấp.
    d.  Nếu xác thực thành công, tạo một `SecurityContext` và lưu thông tin người dùng.
8.  `AuthService` trả về kết quả thành công.
9.  `AuthController` chuyển hướng người dùng đến trang chủ của ứng dụng (`redirect:/dashboard`).

### Luồng rẽ nhánh (Ngoại lệ)

- **7c. Sai thông tin đăng nhập**:
  - `AuthenticationManager` không thể xác thực người dùng (sai email hoặc mật khẩu).
  - `AuthService` bắt ngoại lệ `BadCredentialsException`.
  - `AuthController` trả về trang `login.html` với thông báo lỗi "Email hoặc mật khẩu không chính xác".

# Kịch bản Phiên bản 3: Tư vấn Chế độ dinh dưỡng

- **Use case**: Tư vấn chế độ dinh dưỡng dựa trên AI
- **Actor**: Người dùng (User)
- **Tiền điều kiện**: Người dùng đã đăng nhập vào hệ thống.
- **Hậu điều kiện**: Người dùng nhận được một gợi ý dinh dưỡng (Suggestion) từ hệ thống AI.

## Kịch bản chính:

1.  Người dùng nhập yêu cầu tư vấn vào `NutritionAdviceUI` thông qua phương thức `getUserInput()`.
    - **Dữ liệu**: `query = "Tôi muốn giảm 5kg trong 2 tháng"`
2.  `NutritionAdviceUI` gọi đến `NutritionAdviceController`.
    - **Action**: `askAI(userId, query)`
3.  `NutritionAdviceController` ủy quyền xử lý cho `NutritionAdviceService`.
    - **Action**: `generateSuggestion(userId, query)`
4.  `NutritionAdviceService` thực hiện các bước sau:
    a.  Truy vấn thông tin `User` và `Goal` từ `UserRepository` và `GoalRepository` để xây dựng ngữ cảnh (prompt).
    b.  Gọi đến `AIServiceClient` để nhận phân tích và gợi ý từ mô hình AI.
        - **Action**: `getCompletion(prompt)`
    c.  `AIServiceClient` gửi yêu cầu đến AI Service và nhận về kết quả.
    d.  `NutritionAdviceService` nhận kết quả, tạo một đối tượng `Suggestion` mới và lưu vào cơ sở dữ liệu.
5.  `NutritionAdviceController` nhận đối tượng `Suggestion` đã được tạo, chuyển đổi thành `SuggestionDto` và trả về cho `NutritionAdviceUI`.
6.  `NutritionAdviceUI` hiển thị gợi ý cho người dùng.
    - **Action**: `displaySuggestions(suggestions)`

## Ngoại lệ:

- **4.b**: Nếu `AIServiceClient` không kết nối được với AI Service, hệ thống trả về lỗi và `NutritionAdviceUI` hiển thị thông báo lỗi qua `displayError()`.
- **5**: Nếu dữ liệu trả về từ `NutritionAdviceService` không hợp lệ, `NutritionAdviceController` sẽ xử lý và trả về lỗi HTTP tương ứng.

# Kịch bản Chi tiết: Tạo và Quản lý Kế hoạch Dinh dưỡng (Phiên bản 3)

- **Use Case**: Hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng
- **Actor**: Người dùng (đã đăng nhập)
- **Mục tiêu**: Người dùng tạo thành công một kế hoạch dinh dưỡng mới, hệ thống tính toán và cung cấp các thông tin hỗ trợ.
- **Tiền điều kiện**: Người dùng đã đăng nhập vào hệ thống.

---

### Luồng sự kiện chính:

| Bước | Hành động của Người dùng | Phản hồi của Hệ thống (Giao diện WebApp) | Lớp Controller & Service (Backend) | Tương tác với AI & DB |
|:----:|:---|:---|:---|:---|
| 1 | Click vào chức năng "Lập kế hoạch dinh dưỡng". | Hiển thị trang quản lý kế hoạch, bao gồm tùy chọn "Tạo kế hoạch mới" và danh sách các kế hoạch mẫu. | `NutritionPlanController.showPlanOptions()` | - |
| 2 | Click nút "Tạo kế hoạch mới". | Hiển thị form để nhập chi tiết kế hoạch. | `NutritionPlanController.showCreatePlanForm()` | - |
| 3 | Điền thông tin vào form (Tên: "Giảm cân cấp tốc", Mục tiêu: Giảm 2kg/tuần, Bữa sáng: "Ức gà, bông cải xanh",...) và nhấn "Lưu". | Gửi yêu cầu HTTP POST đến endpoint `/plans/create` với dữ liệu kế hoạch (PlanData). | 1. `NutritionPlanController.createPlan(planData)` nhận yêu cầu. <br> 2. Gọi `NutritionPlanService.createAndAnalyzePlan(planData)`. | - |
| 4 | - | - | 3. `NutritionPlanService` gọi `AIEngine.calculateNutrition(planData.meals)` để tính toán tổng lượng calo và macros. | 1. **AI Engine**: Nhận dữ liệu bữa ăn, tính toán và trả về kết quả `{calories: 1800, protein: 150, ...}`. |
| 5 | - | - | 4. `NutritionPlanService` so sánh kết quả dinh dưỡng với mục tiêu của người dùng (`planData.target`). | - |
| 6 | - | - | 5. Giả sử kết quả hợp lệ, `NutritionPlanService` lưu kế hoạch vào CSDL. | 2. **Database**: `NutritionPlanRepository.save(newPlan)` được gọi để lưu thông tin kế hoạch. |
| 7 | - | - | 6. `NutritionPlanService` gọi `ShoppingListService.generateFromPlan(newPlan)` để tạo danh sách mua sắm. | 3. **Database**: `ShoppingListRepository.save(newList)` được gọi để lưu danh sách. |
| 8 | - | - | 7. `NutritionPlanService` trả về đối tượng kế hoạch đã hoàn chỉnh (bao gồm cả checklist) cho Controller. | - |
| 9 | - | Nhận được phản hồi thành công và hiển thị trang chi tiết kế hoạch mới tạo, bao gồm biểu đồ dinh dưỡng, danh sách mua sắm và các lời khuyên. | 8. `NutritionPlanController` trả về `ResponseEntity` chứa dữ liệu kế hoạch và mã trạng thái 201 (Created). | - |

### Luồng sự kiện thay thế (Ngoại lệ):

- **Tại bước 5**: Nếu `NutritionPlanService` phát hiện có sự chênh lệch lớn giữa dinh dưỡng tính toán và mục tiêu:
    - **5a**: Dịch vụ không lưu kế hoạch mà trả về một đối tượng lỗi (ErrorResponse) chứa thông báo: "Lượng calo của kế hoạch (1800) cao hơn nhiều so với mục tiêu (1500). Vui lòng điều chỉnh."
    - **5b**: `NutritionPlanController` nhận lỗi và trả về `ResponseEntity` với mã trạng thái 400 (Bad Request) và thông báo lỗi.
    - **5c**: Giao diện WebApp nhận lỗi và hiển thị cảnh báo cho người dùng ngay trên form tạo kế hoạch.

# Kịch bản Chi tiết (Phiên bản 3) - Theo dõi và Đánh giá

- **Use Case**: Theo dõi và Đánh giá Kế hoạch Dinh dưỡng
- **Actor**: Người dùng (đã đăng nhập)
- **Tiền điều kiện**: Người dùng đã có một kế hoạch dinh dưỡng đang hoạt động.

### Luồng sự kiện chính:

1.  **Người dùng -> Hệ thống**: Người dùng chọn chức năng "Theo dõi và Đánh giá" từ giao diện chính.
2.  **Hệ thống -> Giao diện**: `TrackingAndReviewUI` được hiển thị, cho phép người dùng chọn ghi lại nhật ký (thức ăn/hoạt động) hoặc xem báo cáo.

#### Nhánh A: Ghi lại Nhật ký

3.  **Người dùng -> Giao diện**: Người dùng chọn "Ghi lại bữa ăn". `TrackingAndReviewUI` gọi phương thức `showLogCreationForm()`.
4.  **Giao diện -> Người dùng**: Hiển thị form cho phép nhập thông tin bữa ăn (tên món, số lượng, hình ảnh...).
5.  **Người dùng -> Giao diện**: Người dùng nhập thông tin và nhấn "Lưu". `TrackingAndReviewUI` gọi phương thức `submitFoodLog()` để thu thập dữ liệu.
6.  **Giao diện -> Controller**: `TrackingAndReviewUI` gửi yêu cầu đến `TrackingController` thông qua phương thức `handleLogFood(userId, logData)`.
7.  **Controller -> Service**: `TrackingController` gọi phương thức `logFood(userId, foodLog)` của `TrackingService`.
8.  **Service -> DAO**: `TrackingService` xử lý logic, tạo đối tượng `FoodLog` và gọi phương thức `save(foodLog)` của `FoodLogDAO`.
9.  **DAO -> CSDL**: `FoodLogDAO` lưu đối tượng `FoodLog` vào cơ sở dữ liệu.
10. **Hệ thống -> Người dùng**: Giao diện hiển thị thông báo ghi nhật ký thành công.

#### Nhánh B: Xem Báo cáo và Gợi ý

3.  **Người dùng -> Giao diện**: Người dùng chọn "Xem Báo cáo Tuần". `TrackingAndReviewUI` gọi phương thức `requestReport('weekly')`.
4.  **Giao diện -> Controller**: `TrackingAndReviewUI` gửi yêu cầu đến `TrackingController` qua phương thức `handleGenerateReport(userId, 'weekly')`.
5.  **Controller -> Service**: `TrackingController` gọi phương thức `generateProgressReport(userId, 'weekly')` của `TrackingService`.
6.  **Service -> DAO**: `TrackingService` truy vấn dữ liệu cần thiết từ `FoodLogDAO` và `ActivityLogDAO` để tổng hợp báo cáo.
7.  **Service -> Controller**: `TrackingService` trả về đối tượng `Report` cho `TrackingController`.
8.  **Controller -> Service**: `TrackingController` tiếp tục gọi `getAISuggestions(userId)` để lấy gợi ý.
9.  **Service -> AI Engine & DAO**: `TrackingService` tương tác với hệ thống AI và `SuggestionDAO` để tạo và lấy các gợi ý phù hợp.
10. **Service -> Controller**: `TrackingService` trả về danh sách `Suggestion`.
11. **Controller -> Giao diện**: `TrackingController` gửi cả `Report` và danh sách `Suggestion` về cho `TrackingAndReviewUI`.
12. **Giao diện -> Người dùng**: `TrackingAndReviewUI` gọi `displayReport(report)` và `displaySuggestions(suggestions)` để hiển thị thông tin cho người dùng.

### Ngoại lệ:

-   **Bước 6, 8 (Nhánh A)**: Dữ liệu nhật ký không hợp lệ -> Hệ thống báo lỗi.
-   **Bước 5, 8 (Nhánh B)**: Không có dữ liệu để tạo báo cáo -> Hệ thống thông báo cho người dùng.
-   **Bất kỳ bước nào**: Lỗi kết nối CSDL hoặc lỗi máy chủ -> Hệ thống hiển thị thông báo lỗi chung.

# Kịch bản phiên bản 3: Quản lý hồ sơ người dùng

- **Use Case:** Cập nhật thông tin hồ sơ cá nhân
- **Actor:** Người dùng (đã đăng nhập)
- **Tiền điều kiện:** Người dùng đã đăng nhập thành công vào hệ thống.
- **Hậu điều kiện:** Thông tin hồ sơ của người dùng được cập nhật trong cơ sở dữ liệu.

## Kịch bản chính (Happy Path)

1.  **Người dùng** chọn chức năng "Hồ sơ của tôi" từ thanh điều hướng.
2.  **Hệ thống** gọi đến `UserProfileController` để xử lý yêu cầu.
3.  `UserProfileController` gọi phương thức `getUserProfile()` từ `UserProfileService`.
4.  `UserProfileService` truy vấn `UserRepository` để lấy thông tin `User` hiện tại.
5.  `UserRepository` trả về đối tượng `User`.
6.  `UserProfileService` chuyển đổi đối tượng `User` thành `UserProfileDTO`.
7.  `UserProfileController` nhận `UserProfileDTO` và trả về view `user-profile` với dữ liệu hồ sơ.
8.  **Hệ thống** hiển thị trang "Hồ sơ của tôi" với các thông tin hiện có.
9.  **Người dùng** nhấn nút "Chỉnh sửa".
10. **Hệ thống** hiển thị một form cho phép chỉnh sửa thông tin (chiều cao, cân nặng, mục tiêu dinh dưỡng, v.v.).
11. **Người dùng** thay đổi thông tin và nhấn "Lưu thay đổi".
12. **Trình duyệt** gửi một yêu cầu POST đến endpoint `/profile/update` với dữ liệu đã cập nhật.
13. **Hệ thống** (`UserProfileController`) nhận yêu cầu và dữ liệu `UpdateProfileDTO`.
14. `UserProfileController` gọi phương thức `updateUserProfile()` của `UserProfileService`, truyền vào `UpdateProfileDTO`.
15. `UserProfileService` xác thực dữ liệu đầu vào.
16. `UserProfileService` tìm đối tượng `User` trong CSDL qua `UserRepository`.
17. `UserProfileService` cập nhật các thuộc tính của đối tượng `User` với dữ liệu mới.
18. `UserProfileService` gọi phương thức `save()` của `UserRepository` để lưu thay đổi.
19. `UserRepository` thực thi câu lệnh UPDATE trong CSDL.
20. **Hệ thống** hiển thị thông báo "Cập nhật hồ sơ thành công" và chuyển hướng người dùng về trang hồ sơ.

## Kịch bản thay thế (Alternative Path)

- **Tại bước 15:** Dữ liệu không hợp lệ (ví dụ: chiều cao không phải là số).
    1. `UserProfileService` ném ra một ngoại lệ `InvalidProfileDataException`.
    2. `UserProfileController` bắt ngoại lệ và trả về view `user-profile` với thông báo lỗi cụ thể.
    3. **Hệ thống** hiển thị lại form chỉnh sửa với thông báo lỗi bên cạnh trường dữ liệu không hợp lệ.

# Kịch bản Chi tiết (Phiên bản 3) - Chức năng Chia sẻ

- **Use Case**: Chia sẻ Kế hoạch và Hoạt động Dinh dưỡng
- **Actor**: Người dùng (đã đăng nhập)
- **Tiền điều kiện**: Người dùng đã đăng nhập và đang ở trang chi tiết một kế hoạch dinh dưỡng hoặc một hoạt động.
- **Hậu điều kiện**: Một bài viết mới được tạo và hiển thị trên dòng thời gian của người dùng và những người liên quan.

## Luồng sự kiện chính:

1.  Người dùng nhấn vào nút "Chia sẻ" trên giao diện `ShareScreen`.
2.  Giao diện `ShareScreen` gọi phương thức `showShareDialog()` để hiển thị hộp thoại chia sẻ.
3.  Người dùng nhập nội dung mô tả cho bài viết và chọn chế độ riêng tư (ví dụ: "Công khai"). Sau đó, người dùng nhấn nút "Đăng".
4.  Giao diện `ShareScreen` khởi tạo một đối tượng `PostDTO` chứa thông tin (nội dung, ID đối tượng được chia sẻ, chế độ riêng tư).
5.  Giao diện `ShareScreen` gọi phương thức `sharePost(postDTO)` của lớp `PostController`.
6.  `PostController` nhận yêu cầu và gọi phương thức `createPost(postDTO)` của lớp `PostService`.
7.  `PostService` thực hiện logic nghiệp vụ:
    a.  Gọi `userRepository.findById()` để lấy thông tin người dùng hiện tại.
    b.  Gọi `planRepository.findById()` (hoặc `activityRepository.findById()`) để lấy đối tượng được chia sẻ.
    c.  Tạo một đối tượng thực thể `Post` mới, thiết lập nội dung, người tạo, đối tượng được chia sẻ và chế độ riêng tư.
8.  `PostService` gọi phương thức `save(post)` của `PostRepository` để lưu đối tượng `Post` vào cơ sở dữ liệu.
9.  `PostRepository` thực thi câu lệnh SQL `INSERT` và trả về đối tượng `Post` đã được lưu.
10. `PostService` nhận lại đối tượng `Post` và có thể kích hoạt một sự kiện (ví dụ: `PostCreatedEvent`).
11. Một `PostEventListener` (lớp lắng nghe sự kiện) sẽ bắt sự kiện này và gọi đến `AIService`.
12. `AIService` nhận thông tin bài viết, gọi phương thức `findSimilarUsers(post)` để phân tích và tìm kiếm những người dùng có cùng mục tiêu/sở thích để gợi ý kết nối.
13. `PostService` trả về kết quả thành công cho `PostController`.
14. `PostController` trả về `ResponseEntity` với mã trạng thái `201 Created` cho giao diện `ShareScreen`.
15. Giao diện `ShareScreen` nhận phản hồi thành công, đóng hộp thoại và cập nhật lại danh sách bài viết để hiển thị bài viết mới.

## Ngoại lệ:

- **Bước 7a, 7b**: Nếu không tìm thấy người dùng hoặc đối tượng được chia sẻ, `PostService` sẽ ném ra một ngoại lệ `ResourceNotFoundException`.
- **Bước 8**: Nếu việc lưu vào cơ sở dữ liệu thất bại, `PostRepository` sẽ ném ra một ngoại lệ (ví dụ: `DataAccessException`), `PostService` sẽ bắt lại và trả về lỗi cho `PostController`.
- **Bước 14**: `PostController` sẽ bắt các ngoại lệ và trả về `ResponseEntity` với mã lỗi phù hợp (ví dụ: `404 Not Found`, `500 Internal Server Error`).

# Kịch bản Phiên bản 3: Quản lý Đối tượng (Module Quản trị viên)

## Use Case 1: Thêm mới một đối tượng (Ví dụ: Thực phẩm)

- **Actor**: Quản trị viên
- **Tiền điều kiện**: Quản trị viên đã đăng nhập thành công vào hệ thống quản trị.
- **Hậu điều kiện**: Một thực phẩm mới được thêm vào cơ sở dữ liệu và hệ thống AI/NLP nhận được dữ liệu để huấn luyện lại.
- **Kịch bản chính**:

1.  Quản trị viên chọn chức năng "Quản lý Thực phẩm" từ giao diện quản trị.
2.  Hệ thống hiển thị danh sách các thực phẩm hiện có và tùy chọn "Thêm mới".
3.  Quản trị viên chọn "Thêm mới".
4.  Hệ thống hiển thị một biểu mẫu (form) để nhập thông tin chi tiết về thực phẩm mới (ví dụ: Tên, Calo, Protein, Carb, Fat, Đơn vị).
5.  Quản trị viên điền đầy đủ thông tin và nhấn nút "Lưu".
6.  Giao diện người dùng (UI) gửi một yêu cầu HTTP POST đến `FoodAdminController` với dữ liệu của thực phẩm mới.
7.  `FoodAdminController` nhận yêu cầu và gọi phương thức `createFood()` của `FoodAdminService`.
8.  `FoodAdminService` thực hiện logic nghiệp vụ, xác thực dữ liệu đầu vào.
9.  `FoodAdminService` gọi phương thức `save()` của `FoodRepository` để lưu đối tượng `Food` mới vào cơ sở dữ liệu.
10. `FoodRepository` thực thi câu lệnh SQL INSERT và trả về đối tượng `Food` đã được lưu.
11. `FoodAdminService` nhận lại đối tượng đã lưu, sau đó gọi `AINlpTrainingService` để gửi dữ liệu mới cho việc huấn luyện lại mô hình.
12. Hệ thống trả về một thông báo thành công cho giao diện người dùng.

- **Ngoại lệ**:
    - **Bước 8**: Dữ liệu không hợp lệ (ví dụ: thiếu tên, giá trị dinh dưỡng âm). `FoodAdminService` sẽ ném ra một ngoại lệ `ValidationException`.
    - **Bước 9**: Lỗi kết nối cơ sở dữ liệu. `FoodRepository` sẽ ném ra một ngoại lệ `DataAccessException`.
    - **Bước 11**: Lỗi khi gửi dữ liệu đến hệ thống AI/NLP.

---

## Use Case 2: Cập nhật một đối tượng (Ví dụ: Thực phẩm)

- **Actor**: Quản trị viên
- **Tiền điều kiện**: Quản trị viên đã đăng nhập và đang ở trong giao diện quản lý thực phẩm. Đối tượng thực phẩm cần cập nhật đã tồn tại.
- **Hậu điều kiện**: Thông tin của thực phẩm được cập nhật trong cơ sở dữ liệu. Hệ thống AI/NLP nhận được dữ liệu để huấn luyện lại.
- **Kịch bản chính**:

1.  Từ danh sách thực phẩm, quản trị viên chọn một thực phẩm và nhấn nút "Sửa".
2.  Hệ thống hiển thị biểu mẫu với thông tin hiện tại của thực phẩm đó.
3.  Quản trị viên thay đổi thông tin cần thiết (ví dụ: cập nhật lại lượng calo) và nhấn "Lưu".
4.  Giao diện người dùng gửi yêu cầu HTTP PUT đến `FoodAdminController` với ID của thực phẩm và dữ liệu đã cập nhật.
5.  `FoodAdminController` gọi phương thức `updateFood()` của `FoodAdminService`.
6.  `FoodAdminService` tìm thực phẩm trong cơ sở dữ liệu thông qua `FoodRepository`.
7.  `FoodAdminService` cập nhật các thuộc tính của đối tượng `Food` và gọi `save()` của `FoodRepository`.
8.  `FoodRepository` thực thi câu lệnh SQL UPDATE.
9.  `FoodAdminService` gọi `AINlpTrainingService` để gửi dữ liệu đã cập nhật.
10. Hệ thống trả về thông báo cập nhật thành công.

- **Ngoại lệ**:
    - **Bước 6**: Không tìm thấy thực phẩm với ID cung cấp. Hệ thống trả về lỗi 404 Not Found.
    - **Bước 7**: Dữ liệu cập nhật không hợp lệ.

---

## Use Case 3: Xóa một đối tượng (Ví dụ: Thực phẩm)

- **Actor**: Quản trị viên
- **Tiền điều kiện**: Quản trị viên đã đăng nhập và đang ở trong giao diện quản lý thực phẩm.
- **Hậu điều kiện**: Thực phẩm bị xóa khỏi cơ sở dữ liệu.
- **Kịch bản chính**:

1.  Từ danh sách, quản trị viên chọn một thực phẩm và nhấn nút "Xóa".
2.  Hệ thống hiển thị hộp thoại xác nhận.
3.  Quản trị viên xác nhận thao tác xóa.
4.  Giao diện người dùng gửi yêu cầu HTTP DELETE đến `FoodAdminController` với ID của thực phẩm.
5.  `FoodAdminController` gọi phương thức `deleteFood()` của `FoodAdminService`.
6.  `FoodAdminService` gọi `deleteById()` của `FoodRepository`.
7.  `FoodRepository` thực thi câu lệnh SQL DELETE.
8.  `FoodAdminService` gọi `AINlpTrainingService` để thông báo về việc xóa dữ liệu.
9.  Hệ thống trả về thông báo xóa thành công.

- **Ngoại lệ**:
    - **Bước 6**: Không tìm thấy thực phẩm để xóa. Hệ thống trả về lỗi 404 Not Found.