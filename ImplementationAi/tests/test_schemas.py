import pytest
from pydantic import ValidationError
from src.schemas import NlpRequest, VisionRequest, RecommendationRequest

def test_nlp_request_valid():
    """
    Kiểm tra NlpRequest với dữ liệu hợp lệ.
    """
    request_data = {"text": "This is a test sentence."}
    request = NlpRequest(**request_data)
    assert request.text == "This is a test sentence."

def test_nlp_request_invalid_missing_text():
    """
    Kiểm tra NlpRequest với trường 'text' bị thiếu.
    """
    with pytest.raises(ValidationError):
        NlpRequest()

def test_nlp_request_invalid_empty_text():
    """
    Kiểm tra NlpRequest với trường 'text' là chuỗi rỗng.
    Pydantic sẽ không tự động thất bại với chuỗi rỗng trừ khi có validation bổ sung.
    Tuy nhiên, việc kiểm tra sự hiện diện của trường là quan trọng.
    """
    request_data = {"text": ""}
    request = NlpRequest(**request_data)
    assert request.text == ""

def test_vision_request_valid():
    """
    Kiểm tra VisionRequest với dữ liệu hợp lệ.
    """
    request_data = {"image_data": "base64encodedstring"}
    request = VisionRequest(**request_data)
    assert request.image_data == "base64encodedstring"

def test_vision_request_invalid_missing_image_data():
    """
    Kiểm tra VisionRequest với trường 'image_data' bị thiếu.
    """
    with pytest.raises(ValidationError):
        VisionRequest()

def test_vision_request_invalid_empty_image_data():
    """
    Kiểm tra VisionRequest với trường 'image_data' là chuỗi rỗng.
    """
    request_data = {"image_data": ""}
    request = VisionRequest(**request_data)
    assert request.image_data == ""

def test_recommendation_request_valid():
    """
    Kiểm tra RecommendationRequest với dữ liệu hợp lệ.
    """
    request_data = {
        "user_profile": {"age": 30, "weight": 70},
        "dietary_preferences": {"vegetarian": True},
        "nutrition_goal_natural_language": "I want to lose weight."
    }
    request = RecommendationRequest(**request_data)
    assert request.user_profile == {"age": 30, "weight": 70}
    assert request.dietary_preferences == {"vegetarian": True}
    assert request.nutrition_goal_natural_language == "I want to lose weight."

def test_recommendation_request_invalid_missing_user_profile():
    """
    Kiểm tra RecommendationRequest với trường 'user_profile' bị thiếu.
    """
    request_data = {
        "dietary_preferences": {"vegetarian": True},
        "nutrition_goal_natural_language": "I want to lose weight."
    }
    with pytest.raises(ValidationError):
        RecommendationRequest(**request_data)

def test_recommendation_request_invalid_missing_dietary_preferences():
    """
    Kiểm tra RecommendationRequest với trường 'dietary_preferences' bị thiếu.
    """
    request_data = {
        "user_profile": {"age": 30, "weight": 70},
        "nutrition_goal_natural_language": "I want to lose weight."
    }
    with pytest.raises(ValidationError):
        RecommendationRequest(**request_data)

def test_recommendation_request_invalid_missing_nutrition_goal_natural_language():
    """
    Kiểm tra RecommendationRequest với trường 'nutrition_goal_natural_language' bị thiếu.
    """
    request_data = {
        "user_profile": {"age": 30, "weight": 70},
        "dietary_preferences": {"vegetarian": True}
    }
    with pytest.raises(ValidationError):
        RecommendationRequest(**request_data)

def test_recommendation_request_invalid_empty_user_profile():
    """
    Kiểm tra RecommendationRequest với trường 'user_profile' là dict rỗng.
    """
    request_data = {
        "user_profile": {},
        "dietary_preferences": {"vegetarian": True},
        "nutrition_goal_natural_language": "I want to lose weight."
    }
    request = RecommendationRequest(**request_data)
    assert request.user_profile == {}

def test_recommendation_request_invalid_empty_dietary_preferences():
    """
    Kiểm tra RecommendationRequest với trường 'dietary_preferences' là dict rỗng.
    """
    request_data = {
        "user_profile": {"age": 30, "weight": 70},
        "dietary_preferences": {},
        "nutrition_goal_natural_language": "I want to lose weight."
    }
    request = RecommendationRequest(**request_data)
    assert request.dietary_preferences == {}

def test_recommendation_request_invalid_empty_nutrition_goal_natural_language():
    """
    Kiểm tra RecommendationRequest với trường 'nutrition_goal_natural_language' là chuỗi rỗng.
    """
    request_data = {
        "user_profile": {"age": 30, "weight": 70},
        "dietary_preferences": {"vegetarian": True},
        "nutrition_goal_natural_language": ""
    }
    request = RecommendationRequest(**request_data)
    assert request.nutrition_goal_natural_language == ""
