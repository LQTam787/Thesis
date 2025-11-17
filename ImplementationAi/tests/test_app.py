# test_app.py
# Tệp này chứa các kiểm thử tích hợp cho ứng dụng Flask chính, tập trung vào việc xác minh
# các endpoint API và cách chúng xử lý các yêu cầu. Các kiểm thử sử dụng đối tượng giả lập (mocks)
# để cô lập logic của endpoint khỏi các dịch vụ phụ thuộc.

import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import pytest
from flask import json
from unittest.mock import patch

try:
    from src.app import app
except ImportError:
    # Attempt relative import fallback if running tests in different context
    from app import app

# Cấu hình client kiểm thử cho ứng dụng Flask
# Fixture này cung cấp một client kiểm thử để gửi các yêu cầu HTTP đến ứng dụng Flask.
# Nó cấu hình ứng dụng ở chế độ kiểm thử để đảm bảo các hành vi nhất định,
# như xử lý lỗi, được đơn giản hóa cho mục đích kiểm thử.
@pytest.fixture
def client():
    app.config['TESTING'] = True
    with app.test_client() as client:
        yield client

# Kiểm thử endpoint NLP
# Kiểm thử này xác minh endpoint '/api/ai/nlp/analyze'. Nó giả lập hàm
# 'process_text_for_nutrition_analysis' để kiểm soát đầu ra của dịch vụ NLP.
# Sau đó, nó gửi một yêu cầu POST với dữ liệu văn bản và kiểm tra rằng phản hồi
# có mã trạng thái 200 (OK) và nội dung phản hồi khớp với đầu ra giả lập.
# Cuối cùng, nó xác nhận rằng hàm giả lập đã được gọi một lần với đầu vào chính xác.
def test_nlp_analyze(client):
    with patch('src.app.process_text_for_nutrition_analysis') as mock_nlp_service:
        mock_nlp_service.return_value = {"analysis_result": "mocked nlp analysis"}
        response = client.post(
            '/api/ai/nlp/analyze',
            data=json.dumps({"text": "Một quả táo và một quả chuối"}),
            content_type='application/json'
        )
        assert response.status_code == 200
        assert json.loads(response.data) == mock_nlp_service.return_value
        mock_nlp_service.assert_called_once_with("Một quả táo và một quả chuối")

# Kiểm thử endpoint Vision
# Kiểm thử này xác minh endpoint '/api/ai/vision/analyze'. Tương tự như kiểm thử NLP,
# nó giả lập hàm 'analyze_image_for_food_recognition' để mô phỏng kết quả nhận dạng hình ảnh.
# Yêu cầu POST được gửi với dữ liệu hình ảnh được mã hóa base64 và kiểm tra
# mã trạng thái và nội dung phản hồi. Hàm giả lập được xác minh đã được gọi chính xác.
def test_vision_analyze(client):
    with patch('src.app.analyze_image_for_food_recognition') as mock_vision_service:
        mock_vision_service.return_value = {"recognition_result": "mocked vision recognition"}
        response = client.post(
            '/api/ai/vision/analyze',
            data=json.dumps({"image_data": "base64encodedimagedata"}),
            content_type='application/json'
        )
        assert response.status_code == 200
        assert json.loads(response.data) == mock_vision_service.return_value
        mock_vision_service.assert_called_once_with("base64encodedimagedata")

# Kiểm thử endpoint Recommendation
# Kiểm thử này xác minh endpoint '/api/ai/recommendation/generate'. Nó giả lập hàm
# 'generate_nutrition_recommendations' để kiểm soát các khuyến nghị dinh dưỡng.
# Yêu cầu POST bao gồm dữ liệu hồ sơ người dùng, sở thích ăn kiêng và mục tiêu dinh dưỡng
# bằng ngôn ngữ tự nhiên. Kiểm thử đảm bảo rằng phản hồi API hợp lệ và hàm giả lập
# được gọi với tất cả các tham số đầu vào cần thiết.
def test_recommendation_generate(client):
    with patch('src.app.generate_nutrition_recommendations') as mock_recommendation_service:
        mock_recommendation_service.return_value = {"recommendation_result": "mocked recommendations"}
        response = client.post(
            '/api/ai/recommendation/generate',
            data=json.dumps({
                "user_profile": {"age": 30, "weight": 70, "height": 170},
                "dietary_preferences": {"type": "vegetarian"},
                "nutrition_goal_natural_language": "Tôi muốn giảm cân"
            }),
            content_type='application/json'
        )
        assert response.status_code == 200
        assert json.loads(response.data) == mock_recommendation_service.return_value
        mock_recommendation_service.assert_called_once_with(
            {"age": 30, "weight": 70, "height": 170},
            {"type": "vegetarian"},
            "Tôi muốn giảm cân"
        )
