"""This module provides functions for generating personalized nutrition recommendations.

It uses a simple in-memory food database and a rule-based approach to generate a 
meal plan based on user preferences and health goals. The `generate_nutrition_recommendations` 
function is the core of this module, which filters food items and creates a 
customized meal plan.
"""
import pandas as pd
from typing import Dict, Any
from .nlp_service import extract_nutrition_goals_from_text

def load_food_data() -> pd.DataFrame:
    # Nguyên lý: Tạo một cơ sở dữ liệu thực phẩm đơn giản trong bộ nhớ (in-memory).
    # Luồng hoạt động: Trong thực tế, hàm này sẽ tải dữ liệu từ CSDL hoặc tệp lớn hơn.
    # Nguồn thông tin AI: Đây là nguồn dữ liệu thực phẩm được dùng để lọc và khuyến nghị.
    data = {
        "food_item": ["oatmeal", "grilled chicken salad", "salmon with steamed vegetables", "beef steak", "vegan curry", "fruit smoothie"],
        "calories": [150, 350, 450, 500, 300, 200],
        "protein_g": [5, 30, 40, 50, 10, 8],
        "carbs_g": [25, 15, 20, 0, 40, 30],
        "fat_g": [3, 20, 25, 30, 15, 5],
        "tags": [["breakfast", "healthy"], ["lunch", "healthy"], ["dinner", "high-protein"], ["dinner", "high-protein", "low-carb"], ["lunch", "vegan"], ["breakfast", "snack"]]
    }
    return pd.DataFrame(data)

food_database = load_food_data()

def generate_nutrition_recommendations(user_profile: Dict[str, Any], dietary_preferences: Dict[str, Any] = None, nutrition_goal_natural_language: str = None) -> Dict[str, Any]:
    """Generates personalized nutrition recommendations based on user data.

    This function takes a user's profile, dietary preferences, and a natural language description of health goals to
    create a customized meal plan. It filters a food database based on these
    parameters and provides a simple set of recommendations.

    Args:
        user_profile (Dict[str, Any]): A dictionary containing user information, such as user_id.
        dietary_preferences (Dict[str, Any], optional): A dictionary of dietary
                                                     preferences (e.g., 'vegan', 'low-carb').
        nutrition_goal_natural_language (str, optional): A natural language description of the user's nutrition goals.

    Returns:
        A dictionary containing the generated meal plan and other nutritional advice.
    """
    print(f"Generating recommendations for user: {user_profile.get('user_id')}")

    extracted_goals = {}
    if nutrition_goal_natural_language:
        extracted_goals = extract_nutrition_goals_from_text(nutrition_goal_natural_language)

    # Luồng hoạt động: Khởi tạo cấu trúc khuyến nghị mặc định.
    recommendations: Dict[str, Any] = {
        "meal_plan": [],
        "caloric_intake_target": extracted_goals.get("daily_calories", 2000),
        "macronutrient_distribution": {
            "protein": f"{extracted_goals.get("daily_protein_g", 150) / (extracted_goals.get("daily_calories", 2000) / 4) * 100:.0f}%" if extracted_goals.get("daily_calories") else "30%",
            "carbs": f"{extracted_goals.get("daily_carbs_g", 200) / (extracted_goals.get("daily_calories", 2000) / 4) * 100:.0f}%" if extracted_goals.get("daily_calories") else "40%",
            "fats": f"{extracted_goals.get("daily_fats_g", 60) / (extracted_goals.get("daily_calories", 2000) / 9) * 100:.0f}%" if extracted_goals.get("daily_calories") else "30%"
        },
        "tips": "Stay hydrated and incorporate regular exercise."
    }

    filtered_foods = food_database.copy()

    # Nguyên lý: Lọc thực phẩm dựa trên sở thích ăn kiêng (ví dụ: ăn chay, ít carb).
    # Luồng hoạt động: Giảm thiểu DataFrame thực phẩm chỉ còn các món phù hợp.
    # Luồng thông tin AI: Sử dụng thông tin người dùng (dietary_preferences) để lọc dữ liệu thực phẩm (food_database).
    if dietary_preferences:
        if dietary_preferences.get("vegan"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'vegan' in x)]
        if dietary_preferences.get("low_carb"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'low-carb' in x)]

    # Nguyên lý: Lọc hoặc sắp xếp thực phẩm dựa trên mục tiêu sức khỏe (ví dụ: giảm cân, tăng cơ).
    # Luồng hoạt động: Ưu tiên các món ăn ít calo (giảm cân) hoặc giàu protein (tăng cơ).
    # Luồng thông tin AI: Sử dụng thông tin mục tiêu (health_goals) để định hình kết quả khuyến nghị.
    if extracted_goals:
        if extracted_goals.get("target_weight_loss_kg"):
            # Prioritize lower calorie foods
            filtered_foods = filtered_foods.sort_values(by='calories').head(3)
        elif extracted_goals.get("goal_type") == "muscle_gain":
            # Prioritize higher protein foods
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'high-protein' in x)]

    # Luồng hoạt động: Tạo một kế hoạch bữa ăn đơn giản bằng cách chọn ngẫu nhiên một món ăn cho mỗi bữa.
    meal_types = ["breakfast", "lunch", "dinner"]
    for meal_type in meal_types:
        # Lọc thực phẩm cho loại bữa ăn cụ thể
        available_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: meal_type in x.lower() if isinstance(x, str) else False)]
        if not available_foods.empty:
            selected_food = available_foods.sample(1).iloc[0]
            recommendations["meal_plan"].append({"meal_type": meal_type, "food_item": selected_food['food_item']})
    
    return recommendations