# conftest.py
# Tệp này chứa các pytest fixtures dùng chung cho toàn bộ suite kiểm thử.
# Các fixtures này cung cấp dữ liệu hoặc cấu hình được sử dụng lại bởi nhiều kiểm thử,
# giúp duy trì tính nhất quán và giảm trùng lặp mã.

"""Fixtures for testing."""
import base64
import io
import os
import sys

import pytest
from PIL import Image

# Thêm thư mục gốc của dự án vào sys.path
# Điều này đảm bảo Python có thể tìm thấy các mô-đun trong 'src' và các thư mục cấp cao hơn.
project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
sys.path.insert(0, project_root)
# Thêm thư mục 'src' vào sys.path để các import trực tiếp như 'from src.module import ...' hoạt động
sys.path.insert(0, os.path.join(project_root, 'ImplementationAi'))


@pytest.fixture(scope="session")
def sample_image_base64():
    """
    Tạo một hình ảnh đen đơn giản 10x10, lưu vào bộ đệm bytes,
    và trả về dưới dạng chuỗi base64 đã mã hóa (utf-8).

    Nguyên lý:
    1. Sử dụng thư viện PIL (Pillow) để tạo một đối tượng hình ảnh.
    2. Lưu hình ảnh vào một đối tượng bộ đệm trong bộ nhớ (io.BytesIO) ở định dạng JPEG.
    3. Mã hóa chuỗi bytes này bằng base64.
    4. Cung cấp chuỗi base64 này làm dữ liệu đầu vào chuẩn cho các bài kiểm tra
       liên quan đến xử lý hình ảnh (Vision Service).
    5. 'scope="session"' đảm bảo hình ảnh chỉ được tạo một lần cho toàn bộ phiên chạy test.
    """
    # Create a simple 10x10 black image
    img = Image.new('RGB', (10, 10), color='black')

    # Save it to a bytes buffer
    buf = io.BytesIO()
    img.save(buf, format='JPEG')
    byte_im = buf.getvalue()

    # Encode to base64
    return base64.b64encode(byte_im).decode('utf-8')