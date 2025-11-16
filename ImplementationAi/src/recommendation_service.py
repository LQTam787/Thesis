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
    # Logic: Create a simple in-memory food database.
    # Flow: In a real scenario, this function would load data from a database or a larger file.
    # AI Information Source: This is the food data source used for filtering and recommendations.
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

    nutrition_goal_description = "Mục tiêu dinh dưỡng chung"
    if nutrition_goal_natural_language:
        nutrition_goal_description = extract_nutrition_goals_from_text(nutrition_goal_natural_language)

    # Flow: Initialize the default recommendation structure.
    recommendations: Dict[str, Any] = {
        "meal_plan": [],
        "nutrition_goal_description": nutrition_goal_description,  # Use natural language description
        "macronutrient_distribution_notes": "Phân bổ macro sẽ được ước tính dựa trên mục tiêu dinh dưỡng tổng thể.",
        "tips": "Stay hydrated and incorporate regular exercise."
    }

    filtered_foods = food_database.copy()

    # Logic: Filter foods based on dietary preferences (e.g., vegan, low-carb).
    # Flow: Reduces the food DataFrame to only include suitable items.
    # AI Information Flow: Uses user information (dietary_preferences) to filter food data (food_database).
    if dietary_preferences:
        if dietary_preferences.get("vegan"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'vegan' in x)]
        if dietary_preferences.get("low_carb"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'low-carb' in x)]

    # AI Information Flow: Uses goal information (health_goals) to shape recommendation results.
    # Modify this logic so it no longer relies on numerical values from extracted_goals
    # Instead, it will rely on natural language analysis or other rules
    # For the purpose of this exercise, we will remove the filtering logic based on specific calorie numbers.

    # if extracted_goals:
    #     if extracted_goals.get("target_weight_loss_kg"):
    #         # Prioritize lower calorie foods
    #         filtered_foods = filtered_foods.sort_values(by='calories').head(3)
    #     elif extracted_goals.get("goal_type") == "muscle_gain":
    #         # Prioritize higher protein foods
    #         filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'high-protein' in x)]

    # Flow: Create a simple meal plan by randomly selecting one food item for each meal.
    meal_types = ["breakfast", "lunch", "dinner"]
    for meal_type in meal_types:
        # Filter foods for the specific meal type
        available_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: meal_type in x.lower() if isinstance(x, str) else False)]
        if not available_foods.empty:
            selected_food = available_foods.sample(1).iloc[0]
            recommendations["meal_plan"].append({"meal_type": meal_type, "food_item": selected_food['food_item']})
    
    return recommendations