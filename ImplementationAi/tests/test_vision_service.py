import pytest
import numpy as np
from unittest.mock import patch, MagicMock

from src.vision_service import analyze_image_for_food_recognition

@pytest.fixture
def mock_tf_model():
    """Fixture to mock the TensorFlow MobileNetV2 model and its dependencies."""
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
    """Tests successful food recognition from a valid base64 image string."""
    result = analyze_image_for_food_recognition(sample_image_base64)

    assert 'error' not in result
    assert 'detected_foods' in result
    assert len(result['detected_foods']) == 3
    assert result['detected_foods'][0]['name'] == 'banana'
    assert result['detected_foods'][0]['confidence'] > 0.8

def test_analyze_image_with_invalid_base64():
    """Tests the function's error handling with an invalid base64 string."""
    invalid_data = "this is not a valid base64 string"
    result = analyze_image_for_food_recognition(invalid_data)

    assert 'error' in result
    assert 'message' in result
    assert result['message'] == "Failed to process image."

def test_analyze_image_with_non_image_data():
    """Tests the function's error handling with valid base64 but non-image data."""
    non_image_data = "aGVsbG8gd29ybGQ=" # "hello world" in base64
    result = analyze_image_for_food_recognition(non_image_data)

    assert 'error' in result
    assert 'message' in result
    assert result['message'] == "Failed to process image."