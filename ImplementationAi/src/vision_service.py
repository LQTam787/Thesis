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

# Nguyên lý: Tải mô hình phân loại hình ảnh MobileNetV2 đã được huấn luyện trên tập dữ liệu ImageNet.
# Luồng hoạt động: Mô hình này sẽ nhận diện đối tượng trong hình ảnh.
# Luồng dữ liệu AI: Tải mô hình đã được huấn luyện với kiến thức từ tập dữ liệu ImageNet.
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
        # Luồng hoạt động: Giải mã Base64 thành dữ liệu byte, sau đó mở bằng PIL.
        # Luồng dữ liệu AI: Chuyển đổi dữ liệu hình ảnh (input) thô thành định dạng có thể xử lý.
        image_bytes = base64.b64decode(image_data)
        image = Image.open(io.BytesIO(image_bytes))
        # Nguyên lý: Thay đổi kích thước hình ảnh về chuẩn đầu vào (224x224) của MobileNetV2.
        image = image.resize((224, 224)) # MobileNetV2 input size
        
        # Luồng hoạt động: Chuyển đổi sang mảng Numpy, thêm chiều batch, và tiền xử lý theo yêu cầu của mô hình.
        # Luồng dữ liệu AI: Chuẩn hóa và định hình dữ liệu hình ảnh đầu vào (pre-processing).
        image_array = np.array(image)
        image_array = np.expand_dims(image_array, axis=0)
        image_array = preprocess_input(image_array)
        
        # Luồng hoạt động: Thực hiện dự đoán và giải mã 3 dự đoán hàng đầu.
        predictions = model.predict(image_array)
        decoded_predictions = decode_predictions(predictions, top=3)[0]
        
        detected_foods = []
        # Luồng hoạt động: Định dạng kết quả dự đoán thành danh sách các món ăn và độ tin cậy.
        detected_foods = [{"name": label, "confidence": float(score)} for _, label, score in decoded_predictions]

        recognition_result = {
            "detected_foods": detected_foods,
            "nutritional_estimate": "Estimates would require a specialized food recognition model with nutritional data."
        }
    except Exception as e:
        # Luồng hoạt động: Xử lý lỗi nếu việc giải mã hoặc xử lý hình ảnh thất bại.
        recognition_result = {"error": str(e), "message": "Failed to process image."}
        
    return recognition_result