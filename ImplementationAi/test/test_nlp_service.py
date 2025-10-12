import unittest
from unittest.mock import patch
import sys
import os

# Add the src directory to the Python path to allow importing nlp_service
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'src')))

from nlp_service import process_text_for_nutrition_analysis

class TestNlpService(unittest.TestCase):

    @patch('nlp_service.nlp_pipeline')
    def test_process_text_for_nutrition_analysis_basic(self, mock_nlp_pipeline):
        # Mock the sentiment analysis pipeline
        mock_nlp_pipeline.return_value = [{'label': 'POSITIVE', 'score': 0.99}]

        text = "I ate an apple and chicken breast for lunch."
        result = process_text_for_nutrition_analysis(text)

        self.assertIsNotNone(result)
        self.assertEqual(result["original_text"], text)
        self.assertIn("apple", result["detected_food_items"])
        self.assertIn("chicken breast", result["detected_food_items"])
        self.assertEqual(result["sentiment"], "POSITIVE")
        self.assertEqual(result["sentiment_score"], 0.99)
        self.assertEqual(result["nutritional_summary"], "Further analysis needed with specialized models.")

    @patch('nlp_service.nlp_pipeline')
    def test_process_text_for_nutrition_analysis_no_food(self, mock_nlp_pipeline):
        mock_nlp_pipeline.return_value = [{'label': 'NEUTRAL', 'score': 0.75}]

        text = "This is a general statement."
        result = process_text_for_nutrition_analysis(text)

        self.assertIsNotNone(result)
        self.assertEqual(result["original_text"], text)
        self.assertEqual(len(result["detected_food_items"]), 0)
        self.assertEqual(result["sentiment"], "NEUTRAL")

    @patch('nlp_service.nlp_pipeline')
    def test_process_text_for_nutrition_analysis_empty_text(self, mock_nlp_pipeline):
        mock_nlp_pipeline.return_value = [{'label': 'NEUTRAL', 'score': 0.50}]

        text = ""
        result = process_text_for_nutrition_analysis(text)

        self.assertIsNotNone(result)
        self.assertEqual(result["original_text"], text)
        self.assertEqual(len(result["detected_food_items"]), 0)

if __name__ == '__main__':
    unittest.main()
