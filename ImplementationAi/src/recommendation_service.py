"""This module provides functions for generating personalized nutrition recommendations.

It uses a simple in-memory food database and a rule-based approach to generate a 
meal plan based on user preferences and health goals. The `generate_nutrition_recommendations` 
function is the core of this module, which filters food items and creates a 
customized meal plan.
"""
import pandas as pd
from typing import Dict, Any

def load_food_data() -> pd.DataFrame:
<<<<<<< HEAD
    """Loads a mock food database into a pandas DataFrame.

    In a real-world application, this data would be loaded from a database or a more 
    robust data source. This function creates an in-memory DataFrame with sample 
    food items and their nutritional information.

    Returns:
        pd.DataFrame: A DataFrame containing the food database.
    """
    # In a real application, this would load data from a database or a more complex source.
    # For demonstration, we'll use a simple in-memory DataFrame.
=======
    # Nguyên lý: Tạo một cơ sở dữ liệu thực phẩm đơn giản trong bộ nhớ (in-memory).
    # Luồng hoạt động: Trong thực tế, hàm này sẽ tải dữ liệu từ CSDL hoặc tệp lớn hơn.
>>>>>>> temp
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

def generate_nutrition_recommendations(user_profile: Dict[str, Any], dietary_preferences: Dict[str, Any] = None, health_goals: Dict[str, Any] = None) -> Dict[str, Any]:
    """Generates personalized nutrition recommendations based on user data.

    This function takes a user's profile, dietary preferences, and health goals to 
    create a customized meal plan. It filters a food database based on these 
    parameters and provides a simple set of recommendations.

    Args:
        user_profile (Dict[str, Any]): A dictionary containing user information, such as user_id.
        dietary_preferences (Dict[str, Any], optional): A dictionary of dietary 
                                                     preferences (e.g., 'vegan', 'low-carb').
        health_goals (Dict[str, Any], optional): A dictionary of health goals (e.g., 
                                              'weight_loss', 'muscle_gain').

    Returns:
        A dictionary containing the generated meal plan and other nutritional advice.
    """
    print(f"Generating recommendations for user: {user_profile.get('user_id')}")
    
    recommendations: Dict[str, Any] = {
        "meal_plan": [],
        "caloric_intake_target": 2000,
        "macronutrient_distribution": {"protein": "30%", "carbs": "40%", "fats": "30%"},
        "tips": "Stay hydrated and incorporate regular exercise."
    }

    filtered_foods = food_database.copy()

    # Nguyên lý: Lọc thực phẩm dựa trên sở thích ăn kiêng (ví dụ: ăn chay, ít carb).
    # Luồng hoạt động: Giảm thiểu DataFrame thực phẩm chỉ còn các món phù hợp.
    if dietary_preferences:
        if dietary_preferences.get("vegan"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'vegan' in x)]
        if dietary_preferences.get("low_carb"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'low-carb' in x)]

    # Nguyên lý: Lọc hoặc sắp xếp thực phẩm dựa trên mục tiêu sức khỏe (ví dụ: giảm cân, tăng cơ).
    # Luồng hoạt động: Ưu tiên các món ăn ít calo (giảm cân) hoặc giàu protein (tăng cơ).
    if health_goals:
        if health_goals.get("weight_loss"):
            # Prioritize lower calorie foods
            filtered_foods = filtered_foods.sort_values(by='calories').head(3)
        elif health_goals.get("muscle_gain"):
            # Prioritize higher protein foods
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'high-protein' in x)]

    # Luồng hoạt động: Tạo một kế hoạch bữa ăn đơn giản bằng cách chọn ngẫu nhiên một món ăn cho mỗi bữa.
    meal_types = ["breakfast", "lunch", "dinner"]
    for meal_type in meal_types:
<<<<<<< HEAD
        available_foods = filtered_foods[filtered_foods['tags'].apply(lambda tags: meal_type in tags if isinstance(tags, list) else False)]
=======
        # Lọc thực phẩm cho loại bữa ăn cụ thể
        available_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: meal_type in x.lower() if isinstance(x, str) else False)]
>>>>>>> temp
        if not available_foods.empty:
            selected_food = available_foods.sample(1).iloc[0]
            recommendations["meal_plan"].append({"meal_type": meal_type, "food_item": selected_food['food_item']})
    
    return recommendations