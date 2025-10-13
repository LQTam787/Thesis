import pandas as pd
from typing import Dict, Any

def load_food_data() -> pd.DataFrame:
    # In a real application, this would load data from a database or a more complex source.
    # For demonstration, we'll use a simple in-memory DataFrame.
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
    print(f"Generating recommendations for user: {user_profile.get('user_id')}")
    
    recommendations: Dict[str, Any] = {
        "meal_plan": [],
        "caloric_intake_target": 2000,
        "macronutrient_distribution": {"protein": "30%", "carbs": "40%", "fats": "30%"},
        "tips": "Stay hydrated and incorporate regular exercise."
    }

    filtered_foods = food_database.copy()

    # Apply dietary preferences
    if dietary_preferences:
        if dietary_preferences.get("vegan"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'vegan' in x)]
        if dietary_preferences.get("low_carb"): 
            filtered_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: 'low-carb' in x)]

    # Apply health goals
    if health_goals:
        if health_goals.get("weight_loss"):
            # Prioritize lower calorie foods
            filtered_foods = filtered_foods.sort_values(by='calories').head(3)
        elif health_goals.get("muscle_gain"):
            # Prioritize higher protein foods
            filtered_foods = filtered_foods.sort_values(by='protein_g', ascending=False).head(3)

    # Generate a simple meal plan
    meal_types = ["breakfast", "lunch", "dinner"]
    for meal_type in meal_types:
        available_foods = filtered_foods[filtered_foods['tags'].apply(lambda x: meal_type in x.lower() if isinstance(x, str) else False)]
        if not available_foods.empty:
            selected_food = available_foods.sample(1).iloc[0]
            recommendations["meal_plan"].append({"meal_type": meal_type, "food_item": selected_food['food_item']})
    
    return recommendations
