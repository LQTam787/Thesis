# Hệ thống Tư vấn Dinh dưỡng AI

## A. Mô tả hệ thống

Dự án này là một nền tảng ứng dụng web giúp người dùng quản lý và theo dõi chế độ ăn uống. Hệ thống sử dụng trí tuệ nhân tạo AI để cung cấp giải pháp lập kế hoạch, theo dõi và điều chỉnh dinh dưỡng thông minh và hoạt động như một chuyên gia dinh dưỡng cá nhân 24/7, có khả năng tương tác và điều chỉnh kế hoạch dựa trên cuộc trò chuyện với người dùng.

## B. Kiến trúc tổng quan

Hệ thống được thiết kế theo kiến trúc đa lớp để đảm bảo tính linh hoạt, bảo mật và hiệu suất cao.

- **Lớp Giao diện (Presentation Layer)**: Ứng dụng di động/web với giao diện chat thân thiện để người dùng tương tác.
- **Lớp Logic Ứng dụng (Application Layer)**: Backend Spring Boot xử lý logic nghiệp vụ cốt lõi như quản lý người dùng, kế hoạch dinh dưỡng.
- **Lớp Xử lý AI/ML (AI/ML Processing Layer)**: Dịch vụ Python riêng biệt chứa các mô hình NLP (phân tích yêu cầu), Vision (nhận dạng món ăn) và Recommendation (gợi ý thực đơn).
- **Lớp Dữ liệu (Data Layer)**: Sử dụng MySQL để lưu trữ dữ liệu có cấu trúc như thông tin người dùng, công thức, và kế hoạch.

## C. Tính năng chính

### 1. Đối với Người dùng (User)
- **Tư vấn dinh dưỡng thông minh**: Trò chuyện với AI để nhận lời khuyên, điều chỉnh kế hoạch ăn uống.
- **Nhận dạng món ăn**: Chụp ảnh bữa ăn để hệ thống nhận dạng và ghi nhận (sử dụng Vision AI).
- **Lập kế hoạch cá nhân hóa**: Tạo và quản lý kế hoạch dinh dưỡng dựa trên mục tiêu (tăng cân, giảm cân, v.v.).
- **Theo dõi tiến độ**: Ghi lại nhật ký bữa ăn, theo dõi lượng calo và các chỉ số dinh dưỡng.
- **Cộng đồng**: Chia sẻ công thức, kế hoạch và tương tác với người dùng khác.

### 2. Đối với Quản trị viên (Admin)
- **Quản lý Người dùng**: Xem, cập nhật và xóa tài khoản người dùng.
- **Quản lý Dữ liệu Dinh dưỡng**: Thêm, sửa, xóa công thức món ăn, thực phẩm.
- **Quản lý Hệ thống**: Giám sát và quản lý các kế hoạch, mục tiêu của người dùng.

## D. Công nghệ sử dụng

| Hạng mục      | Công nghệ                                                              |
|---------------|------------------------------------------------------------------------|
| **Backend**   | Java 17, Spring Boot, Spring Security (JWT), Maven                     |
| **AI/ML**     | Python, Flask, TensorFlow, Scikit-learn, Pandas                        |
| **Cơ sở dữ liệu** | MySQL, Spring Data JPA (Hibernate)                                 |
| **Thiết kế**  | PlantUML                                                               |
| **Kiểm thử**  | JUnit 5, Mockito (Backend) & Pytest (AI)                               |
| **Tài liệu API** | Swagger (OpenAPI)                                                   |

## E. Cấu trúc thư mục

```
.
├── Analysis/         # Tài liệu phân tích hệ thống (biểu đồ lớp, tuần tự...)
├── Design/           # Tài liệu thiết kế chi tiết (biểu đồ CSDL, triển khai...)
├── Implementation/   # Mã nguồn Backend (Java Spring Boot)
├── ImplementationAi/ # Mã nguồn AI/ML (Python Flask)
├── Requirements/     # Tài liệu yêu cầu nghiệp vụ và use case
├── Testing/          # Kế hoạch và kịch bản kiểm thử
├── PLANNING.md       # Kế hoạch và định hướng tổng thể của dự án
└── TASK.md           # Danh sách các công việc cần thực hiện
```

## G. Tài liệu Phân tích & Thiết kế Hệ thống (System Analysis & Design Documents)

### 1. Tài liệu Yêu cầu nghiệp vụ (Requirements Documents)

#### a. Tài liệu mô hình nghiệp vụ (Business Model Documents)

- [`BusinessModel.docx`: Yêu cầu nghiệp vụ theo định dạng Word.](./Requirements/NaturalLanguage/BusinessModel.docx)
- [`BusinessModel.md`: Yêu cầu nghiệp vụ theo định dạng Markdown.](./Requirements/NaturalLanguage/BusinessModel.md)

#### b. Biểu đồ Use Case (Use Case Diagrams)

- [`BaseUseCase.png`: Biểu đồ use case tổng quan.](./Requirements/UmlPictures/BaseUseCase.png)
- [`AuthUseCase.png`: Biểu đồ chi tiết cho chức năng xác thực.](./Requirements/UmlPictures/AuthUseCase.png)
- [`NutritionAdviceUseCase.png`: Biểu đồ chi tiết cho chức năng tư vấn dinh dưỡng.](./Requirements/UmlPictures/NutritionAdviceUseCase.png)
- [`NutritionPlanUseCase.png`: Biểu đồ chi tiết cho chức năng quản lý kế hoạch dinh dưỡng.](./Requirements/UmlPictures/NutritionPlanUseCase.png)
- [`ProgressReviewUseCase.png`: Biểu đồ chi tiết cho chức năng theo dõi và đánh giá.](./Requirements/UmlPictures/ProgressReviewUseCase.png)
- [`ProfileUseCase.png`: Biểu đồ chi tiết cho chức năng quản lý hồ sơ.](./Requirements/UmlPictures/ProfileUseCase.png)
- [`ShareUseCase.png`: Biểu đồ chi tiết cho chức năng chia sẻ.](./Requirements/UmlPictures/ShareUseCase.png)
- [`AdminUseCase.png`: Biểu đồ chi tiết cho chức năng của quản trị viên.](./Requirements/UmlPictures/AdminUseCase.png)

### 2. Tài liệu Phân tích (Analysis Documents)

#### a. Tài liệu kịch bản (Scenario Documents)

- [`Scenarios.docx`: Các kịch bản người dùng theo định dạng Word.](./Analysis/Scenarios/Scenarios.docx)
- [`Scenarios.md`: Các kịch bản người dùng theo định dạng Markdown.](./Analysis/Scenarios/Scenarios.md)

#### b. Biểu đồ lớp thực thể (Entity Class Diagrams)

- [`EntityClassDiagram.png`: Biểu đồ lớp thực thể pha phân tích, trích xuất từ các kịch bản người dùng.](./Analysis/Entity/UmlPictures/EntityClassDiagram.png)

#### c. Biểu đồ lớp phân tích (Analysis Class Diagrams)

- [`AuthModule.png`: Biểu đồ lớp phân tích cho module Xác thực.](./Analysis/Class/UmlPictures/AuthModule.png)
- [`NutritionAdviceModule.png`: Biểu đồ lớp phân tích cho module Tư vấn Dinh dưỡng.](./Analysis/Class/UmlPictures/NutritionAdviceModule.png)
- [`NutritionPlanModule.png`: Biểu đồ lớp phân tích cho module Quản lý Kế hoạch Dinh dưỡng.](./Analysis/Class/UmlPictures/NutritionPlanModule.png)
- [`TrackingAndReviewModule.png`: Biểu đồ lớp phân tích cho module Theo dõi và Đánh giá.](./Analysis/Class/UmlPictures/TrackingAndReviewModule.png)
- [`ProfileManagementModule.png`: Biểu đồ lớp phân tích cho module Quản lý Hồ sơ.](./Analysis/Class/UmlPictures/ProfileManagementModule.png)
- [`ShareModule.png`: Biểu đồ lớp phân tích cho module Chia sẻ.](./Analysis/Class/UmlPictures/ShareModule.png)
- [`AdminFunctionsModule.png`: Biểu đồ lớp phân tích cho module Quản trị viên.](./Analysis/Class/UmlPictures/AdminFunctionsModule.png)

#### d. Biểu đồ trạng thái (State Diagrams)

- [`AuthenticationStateDiagram.png`: Biểu đồ trạng thái cho module Xác thực.](./Analysis/State/UmlPictures/AuthenticationStateDiagram.png)
- [`NutritionAdviceStateDiagram.png`: Biểu đồ trạng thái cho module Tư vấn Dinh dưỡng.](./Analysis/State/UmlPictures/NutritionAdviceStateDiagram.png)
- [`NutritionPlanStateDiagram.png`: Biểu đồ trạng thái cho module Quản lý Kế hoạch Dinh dưỡng.](./Analysis/State/UmlPictures/NutritionPlanStateDiagram.png)
- [`TrackingAndEvaluationState.png`: Biểu đồ trạng thái cho module Theo dõi và Đánh giá.](./Analysis/State/UmlPictures/TrackingAndEvaluationState.png)
- [`ProfileManagementStateDiagram.png`: Biểu đồ trạng thái cho module Quản lý Hồ sơ.](./Analysis/State/UmlPictures/ProfileManagementStateDiagram.png)
- [`ShareStateDiagram.png`: Biểu đồ trạng thái cho module Chia sẻ.](./Analysis/State/UmlPictures/ShareStateDiagram.png)
- [`AdminState.png`: Biểu đồ trạng thái cho luồng chính của Quản trị viên.](./Analysis/State/UmlPictures/AdminState.png)
- [`ObjectManagementState.png`: Biểu đồ trạng thái cho module Đối tượng.](./Analysis/State/UmlPictures/ObjectManagementState.png)

#### e. Biểu đồ tuần tự (Sequence Diagrams)

- [`RegistrationSequenceDiagram.png`: Biểu đồ tuần tự cho chức năng Đăng ký.](./Analysis/Sequence/UmlPictures/RegistrationSequenceDiagram.png)
- [`LoginSequenceDiagram.png`: Biểu đồ tuần tự cho chức năng Đăng nhập.](./Analysis/Sequence/UmlPictures/LoginSequenceDiagram.png)
- [`NutritionAdviceSequenceDiagram.png`: Biểu đồ tuần tự cho chức năng Tư vấn Dinh dưỡng.](./Analysis/Sequence/UmlPictures/NutritionAdviceSequenceDiagram.png)
- [`TrackingAndEvaluationSequence.png`: Biểu đồ tuần tự cho chức năng Theo dõi và Đánh giá.](./Analysis/Sequence/UmlPictures/TrackingAndEvaluationSequence.png)
- [`ProfileManagementSequenceDiagram.png`: Biểu đồ tuần tự cho chức năng Cập nhật Hồ sơ.](./Analysis/Sequence/UmlPictures/ProfileManagementSequenceDiagram.png)
- [`ShareSequence.png`: Biểu đồ tuần tự cho chức năng Chia sẻ.](./Analysis/Sequence/UmlPictures/ShareSequence.png)
- [`AdminAddObjectSequence.png`: Biểu đồ tuần tự cho chức năng Thêm đối tượng của Quản trị viên.](./Analysis/Sequence/UmlPictures/AdminAddObjectSequence.png)
- [`AdminEditDeleteObjectSequence.png`: Biểu đồ tuần tự cho chức năng Sửa/Xóa đối tượng của Quản trị viên.](./Analysis/Sequence/UmlPictures/AdminEditDeleteObjectSequence.png)

### 3. Tài liệu Thiết kế (Design Documents)

#### a. Biểu đồ lớp thực thể (Entity Class Diagrams)

- [`DesignEntityDiagram.png`: Biểu đồ lớp thực thể pha thiết kế, trích xuất từ biểu đồ lớp thực thể pha phân tích.](./Design/Entity/UmlPictures/DesignEntityDiagram.png)

#### b. Biểu đồ quan hệ thực thể CSDL (Entity - Relationship Database Diagrams)

- [`DatabaseEntityClassDiagram.png`: Biểu đồ lớp thực thể cho cơ sở dữ liệu.](./Design/Database/UmlPictures/DatabaseEntityClassDiagram.png)

#### c. Biểu đồ lớp thiết kế (Design Class Diagrams)

- [`AuthenticationModule_Design.png`: Biểu đồ lớp thiết kế cho module Xác thực.](./Design/Static/UmlPictures/AuthenticationModule_Design.png)
- [`DesignClassDiagram_NutritionAdvice.png`: Biểu đồ lớp thiết kế cho module Tư vấn Dinh dưỡng.](./Design/Static/UmlPictures/DesignClassDiagram_NutritionAdvice.png)
- [`DesignClassDiagram_NutritionPlan.png`: Biểu đồ lớp thiết kế cho module Quản lý Kế hoạch Dinh dưỡng.](./Design/Static/UmlPictures/DesignClassDiagram_NutritionPlan.png)
- [`DesignClassDiagram_TrackingAndReview.png`: Biểu đồ lớp thiết kế cho module Theo dõi và Đánh giá.](./Design/Static/UmlPictures/DesignClassDiagram_TrackingAndReview.png)
- [`ProfileManagementDesignClassDiagram.png`: Biểu đồ lớp thiết kế cho module Quản lý Hồ sơ.](./Design/Static/UmlPictures/ProfileManagementDesignClassDiagram.png)
- [`SharingModuleStaticClassDiagram.png`: Biểu đồ lớp thiết kế cho module Chia sẻ.](./Design/Static/UmlPictures/SharingModuleStaticClassDiagram.png)
- [`AdminModuleStaticClassDiagram.png`: Biểu đồ lớp thiết kế cho module Quản trị viên.](./Design/Static/UmlPictures/AdminModuleStaticClassDiagram.png)

#### d. Biểu đồ Triển khai (Deployment Diagrams)

- [`DeploymentDiagram.png`: Biểu đồ triển khai hệ thống.](./Design/Deployment/UmlPictures/DeploymentDiagram.png)

#### e. Biểu đồ hoạt động (Activity Diagrams)

- [`AuthenticationActivity.png`: Biểu đồ hoạt động cho module Xác thực.](./Design/Dynamic/Activity/UmlPictures/AuthenticationActivity.png)
- [`NutritionAdviceActivityDiagram.png`: Biểu đồ hoạt động cho module Tư vấn Dinh dưỡng.](./Design/Dynamic/Activity/UmlPictures/NutritionAdviceActivityDiagram.png)
- [`NutritionPlanManagementActivity.png`: Biểu đồ hoạt động cho module Quản lý Kế hoạch Dinh dưỡng.](./Design/Dynamic/Activity/UmlPictures/NutritionPlanManagementActivity.png)
- [`TrackingAndReviewActivityDiagram.png`: Biểu đồ hoạt động cho module Theo dõi và Đánh giá.](./Design/Dynamic/Activity/UmlPictures/TrackingAndReviewActivityDiagram.png)
- [`UserProfileManagementActivity.png`: Biểu đồ hoạt động cho module Quản lý Hồ sơ.](./Design/Dynamic/Activity/UmlPictures/UserProfileManagementActivity.png)
- [`ShareActivity.png`: Biểu đồ hoạt động cho module Chia sẻ.](./Design/Dynamic/Activity/UmlPictures/ShareActivity.png)
- [`AdminActivityDiagram.png`: Biểu đồ hoạt động cho module Quản trị viên.](./Design/Dynamic/Activity/UmlPictures/AdminActivityDiagram.png)

#### g. Tài liệu kịch bản người dùng (Scenario Documents)

- [`Scenarios_V3.docx`: Các kịch bản người dùng (phiên bản 3) theo định dạng Word](./Design/Dynamic/Scenarios/Scenarios_V3.docx)
- [`Scenarios_V3.md`: Các kịch bản người dùng (phiên bản 3) theo định dạng Markdown](./Design/Dynamic/Scenarios/Scenarios_V3.md)

#### h. Biểu đồ tuần tự (Sequence Diagrams)

- [`AuthenticationSequence.png`: Biểu đồ tuần tự cho module Xác thực.](./Design/Dynamic/Sequence/UmlPictures/AuthenticationSequence.png)
- [`NutritionAdviceSequenceDiagram.png`: Biểu đồ tuần tự cho module Tư vấn Dinh dưỡng.](./Design/Dynamic/Sequence/UmlPictures/NutritionAdviceSequenceDiagram.png)
- [`NutritionPlanManagementSequenceDiagram.png`: Biểu đồ tuần tự cho module Quản lý Kế hoạch Dinh dưỡng.](./Design/Dynamic/Sequence/UmlPictures/NutritionPlanManagementSequenceDiagram.png)
- [`TrackingAndReviewSequenceDiagram.png`: Biểu đồ tuần tự cho module Theo dõi và Đánh giá.](./Design/Dynamic/Sequence/UmlPictures/TrackingAndReviewSequenceDiagram.png)
- [`UserProfileManagementSequence.png`: Biểu đồ tuần tự cho module Quản lý Hồ sơ.](./Design/Dynamic/Sequence/UmlPictures/UserProfileManagementSequence.png)
- [`ShareSequence.png`: Biểu đồ tuần tự cho module Chia sẻ.](./Design/Dynamic/Sequence/UmlPictures/ShareSequence.png)
- [`AdminSequenceDiagram.png`: Biểu đồ tuần tự cho module Quản trị viên.](./Design/Dynamic/Sequence/UmlPictures/AdminSequenceDiagram.png)

#### i. Giao diện Tĩnh (Static Interfaces)

- [`LoginPage.html`: Giao diện trang Đăng nhập.](./Design/Static/Html/LoginPage.html)
- [`RegisterPage.html`: Giao diện trang Đăng ký.](./Design/Static/Html/RegisterPage.html)
- [`NutritionAdvice.html`: Giao diện tư vấn dinh dưỡng.](./Design/Static/Html/NutritionAdvice.html)
- [`NutritionPlan.html`: Giao diện quản lý kế hoạch dinh dưỡng.](./Design/Static/Html/NutritionPlan.html)
- [`TrackingAndReview.html`: Giao diện theo dõi và đánh giá.](./Design/Static/Html/TrackingAndReview.html)
- [`ProfileManagementPage.html`: Giao diện quản lý hồ sơ.](./Design/Static/Html/ProfileManagementPage.html)
- [`SharingModule.html`: Giao diện module Chia sẻ.](./Design/Static/Html/SharingModule.html)
- [`AdminDashboard.html`: Giao diện module Quản trị viên.](./Design/Static/Html/AdminDashboard.html)

## H. Hướng dẫn cài đặt và chạy

### 0. Tải về dự án (Clone Repository)

Bạn có thể tải về mã nguồn dự án bằng một trong các cách sau:

**HTTPS:**
```shell
git clone https://github.com/LQTam787/Thesis.git
```

**SSH:**
```shell
git clone git@github.com:LQTam787/Thesis.git
```

**GitHub CLI:**
```shell
gh repo clone LQTam787/Thesis
```

### 1. Backend (Spring Boot)

a.  **Yêu cầu**:
    - Java JDK 17 hoặc mới hơn.
    - Maven 3.6 hoặc mới hơn.
    - Một instance MySQL đang chạy.

b.  **Cấu hình**:
    - Mở tệp `Implementation/nutrition-ai-backend/src/main/resources/application.properties`.
    - Cập nhật các thuộc tính `spring.datasource.url`, `spring.datasource.username`, và `spring.datasource.password` để trỏ đến cơ sở dữ liệu MySQL của bạn.

c.  **Chạy ứng dụng**:
    - Điều hướng đến thư mục `Implementation/nutrition-ai-backend`.
    - Chạy lệnh sau: `mvn spring-boot:run`.
    - Backend sẽ khởi động và chạy tại `http://localhost:8080`.

### 2. AI/ML (Python, TensorFlow)

a.  **Yêu cầu**:
    - Python 3.8 hoặc mới hơn.

b.  **Cấu hình và Cài đặt**:
    - Điều hướng đến thư mục `ImplementationAi`.
    - Tạo môi trường ảo: `python -m venv venv`.
    - Kích hoạt môi trường ảo (trên Windows): `.\venv\Scripts\activate`.
    - Kích hoạt môi trường ảo (trên Linux/macOS): `source venv/bin/activate`.
    - Cài đặt các thư viện cần thiết: `pip install -r requirements.txt`.

c.  **Chạy ứng dụng AI**:
    - Điều hướng đến thư mục `ImplementationAi/src`.
    - Chạy lệnh sau: `python app.py`.
    - AI Service sẽ khởi động và chạy tại `http://localhost:5000`.

### 3. Quản lý Dữ liệu AI/ML

a.  **Thu thập dữ liệu giả lập**: Để tạo các tệp dữ liệu giả lập (mock data) cho NLP, Vision và Recommendation, hãy điều hướng đến thư mục `ImplementationAi/src` và chạy:
    `python data_collector.py`

b.  **Tiền xử lý dữ liệu**: Sau khi thu thập dữ liệu, để tiền xử lý chúng, hãy điều hướng đến thư mục `ImplementationAi/src` và chạy:
    `python data_preprocessor.py`

c.  **Tải dữ liệu**: Các mô hình AI trong `nlp_service.py`, `vision_service.py` và `recommendation_service.py` sẽ sử dụng `dataset_manager.py` để tải dữ liệu đã được tiền xử lý. Bạn có thể kiểm tra chức năng tải dữ liệu bằng cách chạy:
    `python dataset_manager.py`

## K. Tài liệu API

Sau khi khởi chạy, tài liệu API có thể được truy cập tại:
- **Backend API**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **AI Service API**: [http://localhost:5000/apidocs/](http://localhost:5000/apidocs/)

## L. Kiểm thử (Testing)

### 1. Backend (Spring Boot)

**Chạy Unit Tests**:
    - Điều hướng đến thư mục `Implementation/nutrition-ai-backend`.
    - Chạy lệnh sau: `mvn test`.
    - Maven sẽ tự động tìm và chạy tất cả các Unit Tests trong thư mục `src/test/java`.

### 2. AI/ML (Python)

**Chạy Unit Tests**:
    - Điều hướng đến thư mục `ImplementationAi`.
    - Chạy lệnh sau: `python -m pytest`.
    - Pytest sẽ tự động tìm và chạy tất cả các tệp kiểm thử bắt đầu bằng `test_`.