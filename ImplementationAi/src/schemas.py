from pydantic import BaseModel, Field
from typing import Dict, Any

# Logic: Define schema for Natural Language Processing (NLP) API requests.
# Flow: app.py uses this schema to automatically validate POST data.
# API Data Flow: Describes the structure of input data (text) for the NLP service.
class NlpRequest(BaseModel):
    text: str = Field(..., description="The text to analyze.", json_schema_extra={'example': 'I ate a chicken salad for lunch.'})

# Logic: Define schema for Computer Vision (Vision) API requests.
# Flow: API requests contain Base64 encoded image data.
# API Data Flow: Describes the structure of input data (image_data) for the Vision service.
class VisionRequest(BaseModel):
    image_data: str = Field(..., description="Base64 encoded image data.")

# Logic: Define schema for Nutrition Recommendation API requests.
# Flow: API requests aggregate user profile information, dietary preferences, and health goals.
# API Data Flow: Describes the structure of input data (profile, preferences, goals) for the Recommendation service.
class RecommendationRequest(BaseModel):
    user_profile: Dict[str, Any] = Field(..., description="The user's profile information.")
    dietary_preferences: Dict[str, Any] = Field(..., description="The user's dietary preferences.")
    nutrition_goal_natural_language: str = Field(..., description="The user's health goals described in natural language.", json_schema_extra={'example': 'Tôi muốn giảm 5kg trong 2 tháng và tăng cơ bắp.'})