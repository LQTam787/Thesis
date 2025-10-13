from pydantic import BaseModel, Field
from typing import Dict, Any

class NlpRequest(BaseModel):
    text: str = Field(..., description="The text to analyze.", example='I ate a chicken salad for lunch.')

class VisionRequest(BaseModel):
    image_data: str = Field(..., description="Base64 encoded image data.")

class RecommendationRequest(BaseModel):
    user_profile: Dict[str, Any] = Field(..., description="The user's profile information.")
    dietary_preferences: Dict[str, Any] = Field(..., description="The user's dietary preferences.")
    health_goals: Dict[str, Any] = Field(..., description="The user's health goals.")
