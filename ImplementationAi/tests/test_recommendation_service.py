import pytest
import pandas as pd
from unittest.mock import patch

from src.recommendation_service import generate_nutrition_recommendations

@pytest.fixture
def mock_food_database():
    """Fixture to provide a mock food database for testing."""
    # This data should be consistent and cover various tags for testing
    data = {
        'food_item': ['apple', 'banana', 'chicken breast', 'tofu salad', 'beef steak'],
        'calories': [52, 89, 165, 150, 250],
        'protein_g': [0.3, 1.1, 31, 15, 26],
        'carbs_g': [14, 23, 0, 10, 0],
        'fat_g': [0.2, 0.3, 3.6, 8, 15],
        'tags': [['fruit', 'snack', 'breakfast'], ['fruit', 'snack', 'breakfast'], ['meat', 'high-protein', 'low-carb', 'lunch', 'dinner'], ['vegan', 'healthy', 'lunch'], ['meat', 'high-protein', 'low-carb', 'dinner']]
    }
    return pd.DataFrame(data)

@patch('src.recommendation_service.food_database')
def test_recommendation_for_weight_loss(mock_db, mock_food_database):
    """Tests recommendations for a user with a weight loss goal."""
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_1'}
    health_goals = {'weight_loss': True}

    recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

    # Check if the meal plan prioritizes lower-calorie foods
    recommended_calories = [mock_food_database[mock_food_database['food_item'] == meal['food_item']]['calories'].iloc[0] for meal in recommendations['meal_plan']]
    assert all(c < 200 for c in recommended_calories) # Assuming low-calorie is < 200

@patch('src.recommendation_service.food_database')
def test_recommendation_for_muscle_gain(mock_db, mock_food_database):
    """Tests recommendations for a user with a muscle gain goal."""
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_2'}
    health_goals = {'muscle_gain': True}

    recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

    # Check if the meal plan prioritizes high-protein foods
    recommended_proteins = [mock_food_database[mock_food_database['food_item'] == meal['food_item']]['protein_g'].iloc[0] for meal in recommendations['meal_plan']]
    assert all(p > 20 for p in recommended_proteins) # Assuming high-protein is > 20g

@patch('src.recommendation_service.food_database')
def test_recommendation_for_vegan(mock_db, mock_food_database):
    """Tests recommendations for a user with a vegan dietary preference."""
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_3'}
    dietary_preferences = {'vegan': True}

    recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences=dietary_preferences)

    # Check if all recommended foods are vegan
    for meal in recommendations['meal_plan']:
        tags = mock_food_database[mock_food_database['food_item'] == meal['food_item']]['tags'].iloc[0]
        assert 'vegan' in tags

@patch('src.recommendation_service.food_database')
def test_no_specific_goals(mock_db, mock_food_database):
    """Tests that some recommendations are generated even with no specific goals."""
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_4'}

    recommendations = generate_nutrition_recommendations(user_profile)

    assert 'meal_plan' in recommendations
    assert 'tips' in recommendations