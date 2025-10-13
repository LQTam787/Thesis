import pytest
from unittest.mock import patch

from src.nlp_service import process_text_for_nutrition_analysis

@pytest.fixture
def mock_nlp_pipeline():
    """Fixture to mock the transformers pipeline."""
    with patch('src.nlp_service.nlp_pipeline') as mock_pipeline:
        # Configure the mock to return a predictable sentiment analysis result
        mock_pipeline.return_value = [{'label': 'POSITIVE', 'score': 0.99}]
        yield mock_pipeline

def test_process_text_with_food_items(mock_nlp_pipeline):
    """Tests NLP processing with text containing known food items."""
    text = "I ate an apple and some chicken breast."
    result = process_text_for_nutrition_analysis(text)

    assert result['original_text'] == text
    assert 'apple' in result['detected_food_items']
    assert 'chicken breast' in result['detected_food_items']
    assert result['sentiment'] == 'POSITIVE'
    assert 'sentiment_score' in result

def test_process_text_without_food_items(mock_nlp_pipeline):
    """Tests NLP processing with text that does not contain known food items."""
    text = "This is a generic sentence."
    result = process_text_for_nutrition_analysis(text)

    assert result['original_text'] == text
    assert len(result['detected_food_items']) == 0
    assert result['sentiment'] == 'POSITIVE'

def test_process_text_case_insensitivity(mock_nlp_pipeline):
    """Tests that food item detection is case-insensitive."""
    text = "I love my APPLE pie."
    result = process_text_for_nutrition_analysis(text)

    assert 'apple' in result['detected_food_items']
    assert len(result['detected_food_items']) == 1