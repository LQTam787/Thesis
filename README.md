# Hệ thống Tư vấn Dinh dưỡng AI

Dự án này là một nền tảng tiên phong về Dinh dưỡng Cá nhân hóa (Personalized Nutrition), sử dụng AI để cung cấp giải pháp lập kế hoạch, theo dõi và điều chỉnh dinh dưỡng thông minh. Hệ thống hoạt động như một chuyên gia dinh dưỡng cá nhân 24/7, có khả năng tương tác và điều chỉnh kế hoạch dựa trên cuộc trò chuyện với người dùng.

## 1. Kiến trúc tổng quan

Hệ thống được thiết kế theo kiến trúc đa lớp để đảm bảo tính linh hoạt, bảo mật và hiệu suất cao.

![Biểu đồ triển khai](./Design/Deployment/UmlPictures/DeploymentDiagram.png)

- **Lớp Giao diện (Presentation Layer)**: Ứng dụng di động/web với giao diện chat thân thiện để người dùng tương tác.
- **Lớp Logic Ứng dụng (Application Layer)**: Backend Spring Boot xử lý logic nghiệp vụ cốt lõi như quản lý người dùng, kế hoạch dinh dưỡng.
- **Lớp Xử lý AI/ML (AI/ML Processing Layer)**: Dịch vụ Python riêng biệt chứa các mô hình NLP (phân tích yêu cầu), Vision (nhận dạng món ăn) và Recommendation (gợi ý thực đơn).
- **Lớp Dữ liệu (Data Layer)**: Sử dụng MySQL để lưu trữ dữ liệu có cấu trúc như thông tin người dùng, công thức, và kế hoạch.

## 2. Tính năng chính

### Đối với Người dùng (User)
- **Tư vấn dinh dưỡng thông minh**: Trò chuyện với AI để nhận lời khuyên, điều chỉnh kế hoạch ăn uống.
- **Nhận dạng món ăn**: Chụp ảnh bữa ăn để hệ thống nhận dạng và ghi nhận (sử dụng Vision AI).
- **Lập kế hoạch cá nhân hóa**: Tạo và quản lý kế hoạch dinh dưỡng dựa trên mục tiêu (tăng cân, giảm cân, v.v.).
- **Theo dõi tiến độ**: Ghi lại nhật ký bữa ăn, theo dõi lượng calo và các chỉ số dinh dưỡng.
- **Cộng đồng**: Chia sẻ công thức, kế hoạch và tương tác với người dùng khác.

### Đối với Quản trị viên (Admin)
- **Quản lý Người dùng**: Xem, cập nhật và xóa tài khoản người dùng.
- **Quản lý Dữ liệu Dinh dưỡng**: Thêm, sửa, xóa công thức món ăn, thực phẩm.
- **Quản lý Hệ thống**: Giám sát và quản lý các kế hoạch, mục tiêu của người dùng.

## 3. Công nghệ sử dụng

| Hạng mục      | Công nghệ                                                              |
|---------------|------------------------------------------------------------------------|
| **Backend**   | Java 17, Spring Boot, Spring Security (JWT), Maven                     |
| **AI/ML**     | Python, Flask, TensorFlow, Scikit-learn, Pandas                        |
| **Cơ sở dữ liệu** | MySQL, Spring Data JPA (Hibernate)                                     |
| **Thiết kế**     | PlantUML                                                               |
| **Kiểm thử**   | JUnit 5, Mockito (Backend) & Pytest (AI)                               |
| **Tài liệu API** | Swagger (OpenAPI)                                                      |

## 4. Cấu trúc thư mục

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

## 5. Hướng dẫn cài đặt và chạy

### 5.1. Backend (Spring Boot)

1.  **Yêu cầu**:
    - Java JDK 17+
    - Maven 3.6+
    - MySQL

2.  **Cấu hình**:
    - Mở tệp `Implementation/nutrition-ai-backend/src/main/resources/application.properties`.
    - Cập nhật thông tin kết nối `spring.datasource.url`, `username`, `password`.

3.  **Chạy ứng dụng**:
    ```bash
    cd Implementation/nutrition-ai-backend
    mvn spring-boot:run
    ```
    Backend sẽ chạy tại `http://localhost:8080`.

### 5.2. AI/ML Service (Python)

1.  **Yêu cầu**:
    - Python 3.8+

2.  **Cài đặt**: 
    ```bash
    cd ImplementationAi
    python -m venv .venv  # Tạo môi trường ảo
    # Kích hoạt môi trường ảo
    # Windows: .\.venv\Scripts\activate
    # Linux/macOS: source .venv/bin/activate
    pip install -r requirements.txt
    ```

3.  **Chạy dịch vụ**:
    ```bash
    cd ImplementationAi
    python app.py 
    ```
    AI Service sẽ chạy tại `http://localhost:5000`.

### 5.3. Quản lý Dữ liệu AI/ML

Các script quản lý dữ liệu nằm trong thư mục `ImplementationAi/`.

- **Tạo dữ liệu giả lập**: `python data_collector.py`
- **Tiền xử lý dữ liệu**: `python data_preprocessor.py`
- **Kiểm tra tải dữ liệu**: `python dataset_manager.py`

## 6. Tài liệu API

Sau khi khởi chạy, tài liệu API có thể được truy cập tại:
- **Backend API**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **AI Service API**: [http://localhost:5000/apidocs/](http://localhost:5000/apidocs/)

## 7. Kiểm thử

### Backend (Java)
```bash
cd Implementation/nutrition-ai-backend
mvn test
```

### AI/ML (Python)
```bash
cd ImplementationAi
python -m pytest
```

## 8. Toàn bộ tài liệu thiết kế

Toàn bộ tài liệu phân tích và thiết kế chi tiết (biểu đồ UML, kịch bản,...) được lưu trữ trong các thư mục `Requirements`, `Analysis`, và `Design`. Vui lòng tham khảo trực tiếp các thư mục này để có thông tin đầy đủ nhất.
