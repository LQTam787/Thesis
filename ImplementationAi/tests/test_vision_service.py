# test_vision_service.py
# Tệp này chứa các kiểm thử cho module 'vision_service.py', tập trung vào việc xác minh
# chức năng nhận dạng thực phẩm trong hình ảnh. Các kiểm thử sử dụng đối tượng giả lập
# cho pipeline TensorFlow MobileNetV2 để cô lập logic của dịch vụ thị giác máy tính
# khỏi các phụ thuộc mô hình nặng.

import pytest
import numpy as np
from unittest.mock import patch, MagicMock

from src.vision_service import analyze_image_for_food_recognition

@pytest.fixture
def mock_tf_model():
    """
    Fixture này được tạo ra để mô phỏng (mock) toàn bộ quá trình nhận dạng hình ảnh
    dựa trên TensorFlow MobileNetV2, vì quá trình tải và chạy mô hình thực tế rất chậm
    và phức tạp trong môi trường kiểm thử.

    Luồng hoạt động của Mock:
    1. `patch` các hàm/biến quan trọng: `model`, `decode_predictions`, và `preprocess_input`
       trong mô-đun `src.vision_service`.
    2. **Mock `model.predict()`**: Thiết lập để nó trả về một đối tượng MagicMock (thay vì
       thực hiện tính toán trên GPU/CPU).
    3. **Mock `decode_predictions`**: Thiết lập để nó trả về một kết quả **dự đoán cố định**
       (chuối 90%, chanh 5%, cam 5%). Điều này cho phép kiểm tra xem logic của dịch vụ
       Vision Service có xử lý kết quả dự đoán này đúng cách hay không.
    4. **Mock `preprocess_input`**: Thiết lập để nó trả về một mảng NumPy rỗng, mô phỏng
       quá trình chuẩn bị hình ảnh đầu vào.
    5. `yield` trả về các đối tượng mock, và tự động dọn dẹp (un-patch) khi test kết thúc.
    """
    # Mock the entire TensorFlow dependency chain for image recognition
    with (patch('src.vision_service.model') as mock_model,
          patch('src.vision_service.decode_predictions') as mock_decode,
          patch('src.vision_service.preprocess_input') as mock_preprocess):
        
        # Mock model.predict() to return a dummy numpy array
        mock_model.predict.return_value = MagicMock()
        
        # Mock decode_predictions to return a predictable result
        mock_decode.return_value = [[('n07753592', 'banana', 0.9), ('n07749582', 'lemon', 0.05), ('n07747607', 'orange', 0.05)]]
        
        # Mock preprocess_input to return a dummy numpy array
        mock_preprocess.return_value = np.zeros((1, 224, 224, 3))
        
        yield mock_model, mock_decode, mock_preprocess

def test_analyze_image_successfully(mock_tf_model, sample_image_base64):
    """
    Kiểm tra luồng thành công.
    - Dùng `sample_image_base64` (hình ảnh đen hợp lệ).
    - Dùng `mock_tf_model` để đảm bảo dự đoán trả về là 'banana' với độ tin cậy > 0.8.
    - Nguyên lý: Xác minh dịch vụ xử lý input, gọi mock model, xử lý mock output và
      trả về kết quả JSON/dict hợp lệ.
    """
    result = analyze_image_for_food_recognition(sample_image_base64)

    assert 'error' not in result
    assert 'detected_foods' in result
    assert len(result['detected_foods']) == 3
    assert result['detected_foods'][0]['name'] == 'banana'
    assert result['detected_foods'][0]['confidence'] > 0.8

def test_analyze_image_with_invalid_base64():
    """
    Kiểm tra xử lý lỗi khi đầu vào KHÔNG phải là chuỗi base64 hợp lệ (Malformed Base64).
    - Nguyên lý: Xác minh hàm bắt được lỗi giải mã base64 (Decoding Error) và trả về
      cấu trúc lỗi chuẩn.
    """
    invalid_data = "this is not a valid base64 string"
    result = analyze_image_for_food_recognition(invalid_data)

    assert 'error' in result
    assert 'message' in result
    assert result['message'] == "Failed to process image."

def test_analyze_image_with_non_image_data():
    """
    Kiểm tra xử lý lỗi khi chuỗi base64 HỢP LỆ nhưng nội dung bytes KHÔNG phải là hình ảnh.
    - Nguyên lý: Xác minh hàm bắt được lỗi từ thư viện xử lý hình ảnh (như PIL/Pillow)
      khi cố gắng mở dữ liệu không phải hình ảnh (tức là "hello world").
    """
    non_image_data = "aGVsbG8gd29ybGQ=" # "hello world" in base64
    result = analyze_image_for_food_recognition(non_image_data)

    assert 'error' in result
    assert 'message' in result
    assert result['message'] == "Failed to process image."