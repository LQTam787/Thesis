import unittest
from unittest.mock import patch, MagicMock
import pandas as pd
import sys
import os

# Add the src directory to the Python path to allow importing recommendation_service
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'src')))

from recommendation_service import generate_nutrition_recommendations

class TestRecommendationService(unittest.TestCase):

    def setUp(self):
        # Mock food_database for consistent testing
        self.mock_food_data = {
            "food_item": ["oatmeal", "grilled chicken salad", "salmon with steamed vegetables", "beef steak", "vegan curry", "fruit smoothie"],
            "calories": [150, 350, 450, 500, 300, 200],
            "protein_g": [5, 30, 40, 50, 10, 8],
            "carbs_g": [25, 15, 20, 0, 40, 30],
            "fat_g": [3, 20, 25, 30, 15, 5],
            "tags": ["breakfast", "healthy", "lunch", "dinner", "high-protein", "low-carb", "vegan"]
        }
        self.mock_food_database = pd.DataFrame(self.mock_food_data)

    @patch('recommendation_service.food_database')
    def test_generate_nutrition_recommendations_basic(self, mock_db):
        mock_db.copy.return_value = self.mock_food_database.copy()

        user_profile = {"user_id": "123", "age": 30, "weight": 70, "height": 175}
        recommendations = generate_nutrition_recommendations(user_profile)

        self.assertIsNotNone(recommendations)
        self.assertIn("meal_plan", recommendations)
        self.assertGreater(len(recommendations["meal_plan"]), 0)
        self.assertEqual(recommendations["caloric_intake_target"], 2000)

    @patch('recommendation_service.food_database')
    def test_generate_nutrition_recommendations_vegan_preference(self, mock_db):
        mock_db.copy.return_value = self.mock_food_database.copy()

        user_profile = {"user_id": "123"}
        dietary_preferences = {"vegan": True}
        recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences=dietary_preferences)

        self.assertIsNotNone(recommendations)
        meal_plan_food_items = [meal["food_item"] for meal in recommendations["meal_plan"]]
        for item in meal_plan_food_items:
            self.assertTrue("vegan" in self.mock_food_database[self.mock_food_database['food_item'] == item]['tags'].iloc[0])

    @patch('recommendation_service.food_database')
    def test_generate_nutrition_recommendations_weight_loss_goal(self, mock_db):
        mock_db.copy.return_value = self.mock_food_database.copy()

        user_profile = {"user_id": "123"}
        health_goals = {"weight_loss": True}
        recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

        self.assertIsNotNone(recommendations)
        meal_plan_food_items = [meal["food_item"] for meal in recommendations["meal_plan"]]

        # Verify that recommended foods are generally lower calorie
        # This is a simplification; a real model would be more sophisticated
        for item in meal_plan_food_items:
            calories = self.mock_food_database[self.mock_food_database['food_item'] == item]['calories'].iloc[0]
            self.assertLessEqual(calories, 450) # Based on the mock data, top 3 lowest are <= 450

    @patch('recommendation_service.food_database')
    def test_generate_nutrition_recommendations_muscle_gain_goal(self, mock_db):
        mock_db.copy.return_value = self.mock_food_database.copy()

        user_profile = {"user_id": "123"}
        health_goals = {"muscle_gain": True}
        recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

        self.assertIsNotNone(recommendations)
        meal_plan_food_items = [meal["food_item"] for meal in recommendations["meal_plan"]]

        # Verify that recommended foods are generally higher protein
        for item in meal_plan_food_items:
            protein = self.mock_food_database[self.mock_food_database['food_item'] == item]['protein_g'].iloc[0]
            self.assertGreaterEqual(protein, 30) # Based on the mock data, top 3 highest are >= 30

    @patch('recommendation_service.food_database')
    def test_generate_nutrition_recommendations_no_matching_foods(self, mock_db):
        mock_db.copy.return_value = self.mock_food_database.copy()
        # Create a scenario where no food matches the criteria
        mock_db.copy.return_value = mock_db.copy.return_value[mock_db.copy.return_value['tags'].apply(lambda x: 'nonexistent_tag' in x)]

        user_profile = {"user_id": "123"}
        dietary_preferences = {"vegan": True}
        health_goals = {"weight_loss": True}
        recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences=dietary_preferences, health_goals=health_goals)

        self.assertIsNotNone(recommendations)
        self.assertEqual(len(recommendations["meal_plan"]), 0)

if __name__ == '__main__':
    unittest.main()
