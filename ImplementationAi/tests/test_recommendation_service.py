import pytest
import pandas as pd
from unittest.mock import patch

from src.recommendation_service import generate_nutrition_recommendations

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
def test_recommendation_for_weight_loss(mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho mục tiêu Giảm Cân.

    Luồng hoạt động:
    1. `mock_db.copy.return_value = mock_food_database`: Thiết lập để hàm đề xuất
       sử dụng dữ liệu giả lập.
    2. Gọi hàm với mục tiêu `weight_loss: True`.
    - Nguyên lý: Xác minh rằng tất cả các mục trong `meal_plan` được chọn
      từ các thực phẩm có lượng **calo thấp** (< 200).
    """
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_1'}
    health_goals = {'weight_loss': True}

    recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

    # Check if the meal plan prioritizes lower-calorie foods
    recommended_calories = [mock_food_database[mock_food_database['food_item'] == meal['food_item']]['calories'].iloc[0] for meal in recommendations['meal_plan']]
    assert all(c < 200 for c in recommended_calories) # Assuming low-calorie is < 200

@patch('src.recommendation_service.food_database')
def test_recommendation_for_muscle_gain(mock_db, mock_food_database):
    """
    Kiểm tra đề xuất cho mục tiêu Tăng Cơ.
    - Nguyên lý: Xác minh rằng tất cả các mục trong `meal_plan` được chọn
      từ các thực phẩm có lượng **protein cao** (> 20g).
    """
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_2'}
    health_goals = {'muscle_gain': True}

    recommendations = generate_nutrition_recommendations(user_profile, health_goals=health_goals)

    # Check if the meal plan prioritizes high-protein foods
    recommended_proteins = [mock_food_database[mock_food_database['food_item'] == meal['food_item']]['protein_g'].iloc[0] for meal in recommendations['meal_plan']]
    assert all(p > 20 for p in recommended_proteins) # Assuming high-protein is > 20g

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
def test_no_specific_goals(mock_db, mock_food_database):
    """
    Kiểm tra luồng mặc định khi không có mục tiêu hoặc sở thích cụ thể.
    - Nguyên lý: Xác minh rằng hàm vẫn tạo ra cấu trúc kết quả hợp lệ
      (`meal_plan` và `tips`) ngay cả khi không có tiêu chí lọc cụ thể.
    """
    mock_db.copy.return_value = mock_food_database
    user_profile = {'user_id': 'test_user_4'}

    recommendations = generate_nutrition_recommendations(user_profile)

    assert 'meal_plan' in recommendations
    assert 'tips' in recommendations