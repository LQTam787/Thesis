import pytest
import pandas as pd
from unittest.mock import patch

from src.recommendation_service import generate_nutrition_recommendations
from src.nlp_service import extract_nutrition_goals_from_text

@pytest.fixture
def mock_food_database():
    """
    Fixture để tạo một DataFrame giả lập (mock) cơ sở dữ liệu thực phẩm.
    - Nguyên lý: Cung cấp một tập dữ liệu cố định, đã biết để các bài kiểm tra
      có thể xác minh rằng logic đề xuất (lọc, sắp xếp) hoạt động chính xác
      dựa trên các giá trị calo, protein, và tag được thiết lập sẵn.
    """
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
@patch('src.recommendation_service.extract_nutrition_goals_from_text')
def test_recommendation_for_weight_loss(mock_nlp_extract, mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho mục tiêu Giảm Cân với mô tả ngôn ngữ tự nhiên.
    """
    mock_db.copy.return_value = mock_food_database
    mock_nlp_extract.return_value = "Mục tiêu giảm cân, khoảng 1500 calo mỗi ngày, 100g protein, 150g carbs, 50g chất béo."

    user_profile = {'user_id': 'test_user_1'}
    nutrition_goal_natural_language = "Tôi muốn giảm cân."

    recommendations = generate_nutrition_recommendations(user_profile, nutrition_goal_natural_language=nutrition_goal_natural_language)

    assert "Mục tiêu giảm cân" in recommendations['nutrition_goal_description']
    assert "Phân bổ macro sẽ được ước tính dựa trên mục tiêu dinh dưỡng tổng thể." in recommendations['macronutrient_distribution_notes']
    # Không kiểm tra calo cụ thể vì logic đã được loại bỏ

@patch('src.recommendation_service.food_database')
@patch('src.recommendation_service.extract_nutrition_goals_from_text')
def test_recommendation_for_muscle_gain(mock_nlp_extract, mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho mục tiêu Tăng Cơ với mô tả ngôn ngữ tự nhiên.
    """
    mock_db.copy.return_value = mock_food_database
    mock_nlp_extract.return_value = "Mục tiêu tăng cơ, khoảng 2500 calo mỗi ngày, 200g protein, 250g carbs, 80g chất béo."

    user_profile = {'user_id': 'test_user_2'}
    nutrition_goal_natural_language = "Tôi muốn tăng cơ bắp."

    recommendations = generate_nutrition_recommendations(user_profile, nutrition_goal_natural_language=nutrition_goal_natural_language)

    assert "Mục tiêu tăng cơ" in recommendations['nutrition_goal_description']
    assert "Phân bổ macro sẽ được ước tính dựa trên mục tiêu dinh dưỡng tổng thể." in recommendations['macronutrient_distribution_notes']
    # Không kiểm tra protein cụ thể vì logic đã được loại bỏ

@patch('src.recommendation_service.food_database')
def test_recommendation_for_low_carb(mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho sở thích ăn kiêng Ít Carb (Low-Carb).
    - Nguyên lý: Xác minh rằng tất cả các mục được đề xuất đều có tag 'low-carb'
      trong cơ sở dữ liệu giả lập.
    """
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_5'}
    dietary_preferences = {'low_carb': True}

    recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences=dietary_preferences)

    # Check if all recommended foods are low-carb
    for meal in recommendations['meal_plan']:
        tags = mock_food_database[mock_food_database['food_item'] == meal['food_item']]['tags'].iloc[0]
        assert 'low-carb' in tags

@patch('src.recommendation_service.food_database')
def test_recommendation_for_vegan(mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho sở thích ăn kiêng Thuần chay (Vegan).
    - Nguyên lý: Xác minh rằng tất cả các mục được đề xuất đều có tag 'vegan'
      trong cơ sở dữ liệu giả lập.
    """
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_3'}
    dietary_preferences = {'vegan': True}

    recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences=dietary_preferences)

    # Check if all recommended foods are vegan
    for meal in recommendations['meal_plan']:
        tags = mock_food_database[mock_food_database['food_item'] == meal['food_item']]['tags'].iloc[0]
        assert 'vegan' in tags

@patch('src.recommendation_service.food_database')
@patch('src.recommendation_service.extract_nutrition_goals_from_text')
def test_no_specific_goals(mock_nlp_extract, mock_db, mock_food_database):
    """
    Kiểm tra luồng mặc định khi không có mục tiêu hoặc sở thích cụ thể.
    """
    mock_db.copy.return_value = mock_food_database
    mock_nlp_extract.return_value = "Mục tiêu dinh dưỡng chung, khoảng 2000 calo mỗi ngày, 150g protein, 200g carbs, 60g chất béo."

    user_profile = {'user_id': 'test_user_4'}

    recommendations = generate_nutrition_recommendations(user_profile)

    assert 'meal_plan' in recommendations
    assert 'tips' in recommendations
    assert "Mục tiêu dinh dưỡng chung" in recommendations['nutrition_goal_description']
    assert "Phân bổ macro sẽ được ước tính dựa trên mục tiêu dinh dưỡng tổng thể." in recommendations['macronutrient_distribution_notes']