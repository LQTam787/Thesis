import base64
import io
from PIL import Image
import numpy as np
from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2, preprocess_input, decode_predictions

# Nguyên lý: Tải mô hình phân loại hình ảnh MobileNetV2 đã được huấn luyện trên tập dữ liệu ImageNet.
# Luồng hoạt động: Mô hình này sẽ nhận diện đối tượng trong hình ảnh.
model = MobileNetV2(weights='imagenet')

def analyze_image_for_food_recognition(image_data: str):
    print("Analyzing image for food recognition.")
    
    try:
        # Luồng hoạt động: Giải mã Base64 thành dữ liệu byte, sau đó mở bằng PIL.
        image_bytes = base64.b64decode(image_data)
        image = Image.open(io.BytesIO(image_bytes))
        # Nguyên lý: Thay đổi kích thước hình ảnh về chuẩn đầu vào (224x224) của MobileNetV2.
        image = image.resize((224, 224)) # MobileNetV2 input size
        
        # Luồng hoạt động: Chuyển đổi sang mảng Numpy, thêm chiều batch, và tiền xử lý theo yêu cầu của mô hình.
        image_array = np.array(image)
        image_array = np.expand_dims(image_array, axis=0)
        image_array = preprocess_input(image_array)
        
        # Luồng hoạt động: Thực hiện dự đoán và giải mã 3 dự đoán hàng đầu.
        predictions = model.predict(image_array)
        decoded_predictions = decode_predictions(predictions, top=3)[0]
        
        detected_foods = []
        # Luồng hoạt động: Định dạng kết quả dự đoán thành danh sách các món ăn và độ tin cậy.
        for i, (imagenet_id, label, score) in enumerate(decoded_predictions):
            detected_foods.append({"name": label, "confidence": float(score)})

        recognition_result = {
            "detected_foods": detected_foods,
            "nutritional_estimate": "Estimates would require a specialized food recognition model with nutritional data."
        }
    except Exception as e:
        # Luồng hoạt động: Xử lý lỗi nếu việc giải mã hoặc xử lý hình ảnh thất bại.
        recognition_result = {"error": str(e), "message": "Failed to process image."}
        
    return recognition_result