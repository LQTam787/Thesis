"""This module provides functions for image analysis to recognize food items.

It uses a pre-trained MobileNetV2 model from TensorFlow/Keras for image classification. 
The primary function, `analyze_image_for_food_recognition`, takes a base64-encoded image, 
preprocesses it, and returns a list of detected food items with their confidence scores.
"""
import base64
import io
from PIL import Image
import numpy as np
from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2, preprocess_input, decode_predictions

# Logic: Load the MobileNetV2 image classification model, pre-trained on the ImageNet dataset.
# Flow: This model will recognize objects in the image.
# AI Data Flow: Loads a model pre-trained with knowledge from the ImageNet dataset.
model = MobileNetV2(weights='imagenet')

def analyze_image_for_food_recognition(image_data: str):
    """Analyzes a base64-encoded image to recognize food items.

    This function decodes the image, resizes it to the input size required by 
    MobileNetV2 (224x224), preprocesses it, and then uses the model to predict the 
    contents. It returns the top three predictions.

    Args:
        image_data (str): A base64-encoded string representing the image.

    Returns:
        A dictionary containing the detected food items and their confidence scores, 
        or an error message if the image processing fails.
    """
    print("Analyzing image for food recognition.")
    
    try:
        # Flow: Decode Base64 to byte data, then open with PIL.
        # AI Data Flow: Converts raw image data (input) into a processable format.
        image_bytes = base64.b64decode(image_data)
        image = Image.open(io.BytesIO(image_bytes))
        # Logic: Resize the image to the standard input size (224x224) for MobileNetV2.
        image = image.resize((224, 224)) # MobileNetV2 input size
        
        # Flow: Convert to Numpy array, add batch dimension, and preprocess as required by the model.
        # AI Data Flow: Normalizes and shapes input image data (pre-processing).
        image_array = np.array(image)
        image_array = np.expand_dims(image_array, axis=0)
        image_array = preprocess_input(image_array)
        
        # Flow: Perform prediction and decode the top 3 predictions.
        predictions = model.predict(image_array)
        decoded_predictions = decode_predictions(predictions, top=3)[0]
        
        detected_foods = []
        # Flow: Format prediction results into a list of food items and confidence scores.
        detected_foods = [{"name": label, "confidence": float(score)} for _, label, score in decoded_predictions]

        recognition_result = {
            "detected_foods": detected_foods,
            "nutritional_estimate": "Estimates would require a specialized food recognition model with nutritional data."
        }
    except Exception as e:
        # Flow: Handle errors if image decoding or processing fails.
        recognition_result = {"error": str(e), "message": "Failed to process image."}
        
    return recognition_result