# Kịch bản Kiểm thử Tích hợp cho Luồng Tương tác AI (AI Interaction Flow)

## 1. Mục tiêu

Các kịch bản này nhằm xác minh sự tương tác và luồng dữ liệu chính xác giữa **Backend (Spring Boot)** và **Dịch vụ AI (Python/Flask)** cho các chức năng cốt lõi liên quan đến trí tuệ nhân tạo.

## 2. Các Luồng Kiểm thử Chi tiết

### 2.1. Luồng Phân tích Văn bản Dinh dưỡng (NLP Analysis Flow)

- **Kịch bản 2.1.1: Backend gửi yêu cầu phân tích văn bản hợp lệ đến AI Service**
  - **Mô tả:** Người dùng nhập một yêu cầu bằng ngôn ngữ tự nhiên (ví dụ: qua chatbot hoặc ô tìm kiếm), và backend chuyển tiếp yêu cầu này đến AI Service để xử lý.
  - **Các bước:**
    1.  Client (giả lập) gửi yêu cầu POST đến một endpoint của backend (ví dụ: `/api/ai/analyze-text`) với một chuỗi văn bản hợp lệ trong body (ví dụ: `{"text": "Gợi ý cho tôi một bữa tối giàu protein"}`).
    2.  Backend service gọi đến endpoint `POST /api/ai/nlp/analyze` của AI Service với nội dung văn bản tương ứng.
    3.  AI Service (giả lập/mock) xử lý thành công và trả về một cấu trúc JSON chứa các thực thể đã được nhận dạng (ví dụ: `{"intent": "suggest_meal", "entities": [{"type": "meal_time", "value": "bữa tối"}, {"type": "nutrient", "value": "protein"}]}`).
    4.  Backend nhận kết quả từ AI Service và trả về cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về HTTP status `200 OK`.
    - Response body chứa cấu trúc JSON phân tích từ AI Service.
    - Log của backend ghi nhận cuộc gọi thành công đến AI Service.

- **Kịch bản 2.1.2: AI Service trả về lỗi khi nhận văn bản không hợp lệ**
  - **Mô tả:** Backend gửi một yêu cầu không chứa văn bản đến AI Service.
  - **Các bước:**
    1.  Client (giả lập) gửi yêu cầu POST đến endpoint của backend với body rỗng hoặc thiếu trường `text`.
    2.  Backend gọi đến `POST /api/ai/nlp/analyze` của AI Service.
    3.  AI Service trả về lỗi (ví dụ: HTTP status `400 Bad Request` với thông báo lỗi).
    4.  Backend xử lý lỗi này một cách hợp lệ (gracefully) và thông báo lại cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về một mã lỗi phù hợp (ví dụ: `400 Bad Request` hoặc `502 Bad Gateway`).
    - Response body chứa thông báo lỗi rõ ràng cho client.

### 2.2. Luồng Phân tích Hình ảnh Món ăn (Vision Analysis Flow)

- **Kịch bản 2.2.1: Backend gửi yêu cầu phân tích hình ảnh hợp lệ đến AI Service**
  - **Mô tả:** Người dùng tải lên hình ảnh một món ăn, backend gửi dữ liệu hình ảnh này đến AI Service để nhận dạng.
  - **Các bước:**
    1.  Client (giả lập) gửi yêu cầu POST đến endpoint của backend (ví dụ: `/api/ai/analyze-image`) với dữ liệu hình ảnh (dạng base64) trong body (ví dụ: `{"image_data": "<base64_string>"}`).
    2.  Backend service gọi đến endpoint `POST /api/ai/vision/analyze` của AI Service.
    3.  AI Service (giả lập/mock) xử lý thành công và trả về kết quả nhận dạng (ví dụ: `{"food_items": [{"name": "Pho Bo", "confidence": 0.95}], "calories": 450}`).
    4.  Backend nhận kết quả và trả về cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về HTTP status `200 OK`.
    - Response body chứa thông tin món ăn và dinh dưỡng ước tính từ AI Service.

- **Kịch bản 2.2.2: AI Service không thể nhận dạng món ăn từ hình ảnh**
  - **Mô tả:** Người dùng tải lên một hình ảnh không chứa món ăn hoặc chất lượng quá thấp.
  - **Các bước:**
    1.  Client gửi yêu cầu như trên.
    2.  Backend gọi đến AI Service.
    3.  AI Service xử lý và trả về kết quả không nhận dạng được (ví dụ: `{"food_items": [], "error": "Could not identify food item"}`).
    4.  Backend nhận kết quả và thông báo cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về HTTP status `200 OK` (vì yêu cầu vẫn hợp lệ).
    - Response body chứa thông báo cho biết không thể nhận dạng được món ăn.

### 2.3. Luồng Gợi ý Dinh dưỡng Cá nhân hóa (Recommendation Flow)

- **Kịch bản 2.3.1: Backend yêu cầu và nhận gợi ý dinh dưỡng thành công**
  - **Mô tả:** Dựa trên thông tin của người dùng, backend yêu cầu AI Service tạo ra một kế hoạch hoặc gợi ý bữa ăn.
  - **Các bước:**
    1.  Client (giả lập, đã đăng nhập) gửi yêu cầu GET hoặc POST đến endpoint của backend (ví dụ: `/api/suggestions/generate`).
    2.  Backend lấy thông tin người dùng (hồ sơ, mục tiêu, sở thích) từ CSDL.
    3.  Backend service gọi đến endpoint `POST /api/ai/recommendation/generate` của AI Service với các thông tin trên.
    4.  AI Service (giả lập/mock) xử lý và trả về một danh sách các gợi ý (ví dụ: `{"recommendations": [{"meal": "breakfast", "recipe": "Yen mach trai cay"}, {"meal": "lunch", "recipe": "Uc ga ap chao"}]}`).
    5.  Backend nhận kết quả, có thể lưu vào CSDL và trả về cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về HTTP status `200 OK`.
    - Response body chứa danh sách các gợi ý dinh dưỡng phù hợp.

- **Kịch bản 2.3.2: AI Service không thể tạo gợi ý do thiếu thông tin**
  - **Mô tả:** Người dùng chưa cung cấp đủ thông tin trong hồ sơ (ví dụ: chưa có mục tiêu sức khỏe).
  - **Các bước:**
    1.  Client gửi yêu cầu như trên.
    2.  Backend lấy thông tin người dùng, nhưng một số trường quan trọng bị thiếu.
    3.  Backend vẫn gọi đến AI Service với dữ liệu không đầy đủ.
    4.  AI Service trả về lỗi `400 Bad Request` do thiếu tham số `user_profile` hoặc các trường con quan trọng.
    5.  Backend xử lý lỗi và trả về thông báo cho client.
  - **Kết quả mong đợi:**
    - API của backend trả về `400 Bad Request`.
    - Response body chứa thông báo yêu cầu người dùng cập nhật hồ sơ.
