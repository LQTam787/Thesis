from pydantic import BaseModel, Field
from typing import Dict, Any

# Nguyên lý: Định nghĩa schema cho yêu cầu API Phân tích Ngôn ngữ Tự nhiên (NLP).
# Luồng hoạt động: app.py sử dụng schema này để tự động xác thực dữ liệu POST.
class NlpRequest(BaseModel):
    text: str = Field(..., description="The text to analyze.", example='I ate a chicken salad for lunch.')

# Nguyên lý: Định nghĩa schema cho yêu cầu API Phân tích Thị giác Máy tính (Vision).
# Luồng hoạt động: Yêu cầu API chứa dữ liệu hình ảnh được mã hóa Base64.
class VisionRequest(BaseModel):
    image_data: str = Field(..., description="Base64 encoded image data.")

# Nguyên lý: Định nghĩa schema cho yêu cầu API Khuyến nghị Dinh dưỡng.
# Luồng hoạt động: Yêu cầu API tổng hợp thông tin hồ sơ, sở thích ăn kiêng và mục tiêu sức khỏe của người dùng.
class RecommendationRequest(BaseModel):
    user_profile: Dict[str, Any] = Field(..., description="The user's profile information.")
    dietary_preferences: Dict[str, Any] = Field(..., description="The user's dietary preferences.")
    health_goals: Dict[str, Any] = Field(..., description="The user's health goals.")