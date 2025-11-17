# **I. Định hướng Bậc cao (High-Level Direction)**

- **Tầm nhìn**: Xây dựng nền tảng tiên phong về Dinh dưỡng Cá nhân hóa (Personalized Nutrition) bằng AI, cung cấp giải pháp lập kế hoạch, theo dõi và điều chỉnh dinh dưỡng thông minh, hiệu quả, thân thiện.
- **Mục tiêu Kỹ thuật**: Thiết kế một hệ thống backend **linh hoạt (scalable)**, **bảo mật (secure)**, và **hiệu suất cao** để xử lý đồng thời yêu cầu từ ứng dụng di động/web và tích hợp sâu với các mô hình AI/Machine Learning.
- **Đặc điểm**: Nền tảng hoạt động như một chuyên gia dinh dưỡng cá nhân 24/7, có khả năng lắng nghe và điều chỉnh kế hoạch dựa trên trò chuyện.

# **II. Tầm hướng Thiết kế Hệ thống (System Design Overview)**

- **Lớp Giao diện (Presentation Layer)**:
    - Ứng dụng di động/Web.
    - Chịu trách nhiệm hiển thị giao diện, đặc biệt là giao diện **Chatbox/NLP** thân thiện để người dùng nhập liệu, đặt câu hỏi và nhận tư vấn.
    - Cung cấp trải nghiệm **Tư vấn chế độ dinh dưỡng** và **Theo dõi/Đánh giá** dễ dàng.
- **Lớp Logic Ứng dụng (Application/Service Layer)**:
    - Viết bằng **Java Spring Boot**.
    - Chứa logic nghiệp vụ cốt lõi: Đăng ký/Đăng nhập, Quản lý Hồ sơ, Quản lý Kế hoạch/Nhật ký.
    - Giao tiếp với lớp Dữ liệu và lớp AI.
    - Điều phối các chức năng: Tính toán BMR/TDEE cơ bản, so sánh dữ liệu (Kế hoạch vs. Thực tế), quản lý các đối tượng nghiệp vụ (User, Plan, Goal...).
- **Lớp Xử lý AI/ML (AI/ML Processing Layer)**:
    - Mô hình **NLP** (Xử lý Ngôn ngữ Tự nhiên) và **Vision** (Thị giác Máy tính).
    - **NLP:** Phân tích câu lệnh người dùng ("Tôi muốn giảm 5kg...", "Thay món gà bằng cá...").
    - **Vision:** Nhận dạng thực phẩm từ ảnh chụp (theo chức năng 5).
    - **ML:** Tính toán nhu cầu Calo/Macro tối ưu và tìm kiếm/chỉnh sửa thực đơn thay thế.
- **Lớp Dữ liệu (Data Layer)**:
    - Cơ sở dữ liệu quan hệ (MySQL) và Cơ sở dữ liệu NoSQL (cho dữ liệu nhật ký/phản hồi AI phi cấu trúc).
    - Lưu trữ các Đối tượng cốt lõi: User, Goal, Plan, Recipe/Food Item, Log Entry, Activity.

# **III. Công nghệ và Framework (Technology Stack)**

- **Backend/Core Logic**: 
    - **Java** (Ngôn ngữ Chính): Ngôn ngữ mạnh mẽ, hiệu suất cao, phù hợp cho các hệ thống phức tạp.
    - **Spring Boot** (Framework): Giúp phát triển API RESTful nhanh chóng, dễ dàng triển khai (Microservices-ready nếu cần).
    - **Spring Security**: Bảo mật API (Oauth2/JWT) cho các chức năng Đăng nhập/Đăng ký và phân quyền **User/Admin**.
- **Cơ sở Dữ liệu**:
    - **MySQL**: Độ tin cậy cao, phù hợp cho dữ liệu quan hệ (User, Plan, Recipe) và hỗ trợ tốt cho các truy vấn phức tạp (SQL).
    - **Spring Data JPA** (Hibernate): Framework truy vấn ORM giúp ánh xạ các đối tượng Java (User, Goal, Plan,...) sang bảng Cơ sở Dữ liệu.
- **AI/ML**:
    - **Python** (Ngôn ngữ): Ngôn ngữ tiêu chuẩn cho các thư viện AI/ML (TensorFlow, PyTorch, scikit-learn).
    - **API REST/gRPC**: Phương thức giao tiếp giữa Backend **Spring Boot** và các **Mô hình AI** (chạy trên một dịch vụ riêng biệt).
- **Mô hình Thiết kế**: **PlantUML**: Công cụ dùng để thể hiện các biểu đồ hệ thống (Use Case, Class, Sequence) giúp dễ dàng hình dung kiến trúc.