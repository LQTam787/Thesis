import unittest
from unittest.mock import patch, MagicMock
import base64
import sys
import os

# Add the src directory to the Python path to allow importing vision_service
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'src')))

from vision_service import analyze_image_for_food_recognition

class TestVisionService(unittest.TestCase):

    @patch('vision_service.MobileNetV2')
    @patch('vision_service.preprocess_input')
    @patch('vision_service.decode_predictions')
    @patch('vision_service.Image')
    @patch('vision_service.io.BytesIO')
    def test_analyze_image_for_food_recognition_success(self, mock_bytesio, mock_image, mock_decode_predictions, mock_preprocess_input, mock_mobilenet):
        # Mock image processing and model prediction
        mock_image_instance = MagicMock()
        mock_image.open.return_value = mock_image_instance
        mock_image_instance.resize.return_value = mock_image_instance
        mock_preprocess_input.return_value = MagicMock()
        mock_mobilenet_instance = MagicMock()
        mock_mobilenet.return_value = mock_mobilenet_instance
        mock_mobilenet_instance.predict.return_value = MagicMock()
        mock_decode_predictions.return_value = [[('n07749582', 'lemon', 0.90), ('n07749582', 'orange', 0.05)]]

        dummy_image_data = base64.b64encode(b"dummy_image_bytes").decode('utf-8')
        result = analyze_image_for_food_recognition(dummy_image_data)

        self.assertIsNotNone(result)
        self.assertIn("detected_foods", result)
        self.assertEqual(len(result["detected_foods"]), 2)
        self.assertEqual(result["detected_foods"][0]["name"], "lemon")
        self.assertAlmostEqual(result["detected_foods"][0]["confidence"], 0.90)
        self.assertEqual(result["nutritional_estimate"], "Estimates would require a specialized food recognition model with nutritional data.")

    def test_analyze_image_for_food_recognition_invalid_base64(self):
        invalid_image_data = "not-a-valid-base64-string"
        result = analyze_image_for_food_recognition(invalid_image_data)

        self.assertIsNotNone(result)
        self.assertIn("error", result)
        self.assertIn("message", result)
        self.assertEqual(result["message"], "Failed to process image.")

    @patch('vision_service.Image.open', side_effect=IOError("Corrupt image"))
    def test_analyze_image_for_food_recognition_image_processing_error(self, mock_image_open):
        dummy_image_data = base64.b64encode(b"dummy_image_bytes").decode('utf-8')
        result = analyze_image_for_food_recognition(dummy_image_data)

        self.assertIsNotNone(result)
        self.assertIn("error", result)
        self.assertIn("message", result)
        self.assertEqual(result["message"], "Failed to process image.")

if __name__ == '__main__':
    unittest.main()
