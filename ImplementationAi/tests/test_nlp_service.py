import pytest
from unittest.mock import patch

from src.nlp_service import process_text_for_nutrition_analysis

@pytest.fixture
def mock_nlp_pipeline():
    """
    Fixture để mô phỏng (mock) pipeline NLP, vì việc tải mô hình NLP lớn rất tốn thời gian.

    Luồng hoạt động của Mock:
    1. `patch` biến `nlp_pipeline` trong mô-đun `src.nlp_service`.
    2. Thiết lập `mock_pipeline.return_value` để nó trả về kết quả phân tích
       cảm xúc cố định (POSITIVE, 0.99).
    - Nguyên lý: Đảm bảo rằng hàm chính gọi mock pipeline và xử lý kết quả
      sentiment analysis đúng cách, mà không cần chạy mô hình thực tế.
    """
    with patch('src.nlp_service.nlp_pipeline') as mock_pipeline:
        # Configure the mock to return a predictable sentiment analysis result
        mock_pipeline.return_value = [{'label': 'POSITIVE', 'score': 0.99}]
        yield mock_pipeline

def test_process_text_with_food_items(mock_nlp_pipeline):
    """
    Kiểm tra xử lý văn bản chứa các mục thực phẩm đã biết.
    - Nguyên lý: Xác minh hàm phát hiện các từ khóa thực phẩm ("apple", "chicken breast")
      và đồng thời gán kết quả sentiment từ mock pipeline.
    """
    text = "I ate an apple and some chicken breast."
    result = process_text_for_nutrition_analysis(text)

    assert result['original_text'] == text
    assert 'apple' in result['detected_food_items']
    assert 'chicken breast' in result['detected_food_items']
    assert result['sentiment'] == 'POSITIVE'
    assert 'sentiment_score' in result

def test_process_text_without_food_items(mock_nlp_pipeline):
    """
    Kiểm tra xử lý văn bản không chứa mục thực phẩm.
    - Nguyên lý: Xác minh danh sách thực phẩm được phát hiện là rỗng, nhưng
      sentiment analysis vẫn hoạt động (vì nó được mock).
    """
    text = "This is a generic sentence."
    result = process_text_for_nutrition_analysis(text)

    assert result['original_text'] == text
    assert len(result['detected_food_items']) == 0
    assert result['sentiment'] == 'POSITIVE'

def test_process_text_case_insensitivity(mock_nlp_pipeline):
    """
    Kiểm tra tính năng phát hiện thực phẩm KHÔNG phân biệt chữ hoa/chữ thường.
    - Nguyên lý: Xác minh từ khóa "APPLE" trong văn bản được chuẩn hóa và phát hiện
      thành công là "apple".
    """
    text = "I love my APPLE pie."
    result = process_text_for_nutrition_analysis(text)

    assert 'apple' in result['detected_food_items']
    assert len(result['detected_food_items']) == 1