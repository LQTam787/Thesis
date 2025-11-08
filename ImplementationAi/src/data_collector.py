"""This module contains functions to generate mock data for the AI services.

This script is used to create sample datasets for the NLP, Vision, and Recommendation 
services. It generates CSV files with mock text data and user profiles, and creates 
dummy image files to simulate a vision dataset. This is useful for testing and 
development without requiring a real data collection pipeline.
"""
import os
import pandas as pd

def collect_mock_nlp_data(
    output_file: str = "ImplementationAi/data/mock_nlp_data.csv",
) -> None:
    """Collects mock data for the NLP service and saves it to a CSV file.

    Args:
        output_file (str): The path to the output CSV file.
    """
    print("Collecting mock NLP data...")
    # Nguyên lý: Tạo DataFrame với các cặp (text, labels) giả lập.
    # Luồng hoạt động: Dữ liệu này sẽ được data_preprocessor.py sử dụng để tiền xử lý.
    # Hoạt động thu thập AI: Tạo dữ liệu thô (mock) cho mô hình NLP.
    data = {
        "text": [
            "I had a delicious apple for breakfast.",
            "Chicken breast is great for muscle gain.",
            "This meal was too high in fat and low in protein.",
            "I need a low-carb meal plan.",
        ],
        "labels": [
            "positive, fruit",
            "positive, protein",
            "negative, unhealthy",
            "neutral, dietary_preference",
        ],
    }
    output_dir = os.path.dirname(output_file)
    if output_dir:
        os.makedirs(output_dir, exist_ok=True)

    df = pd.DataFrame(data)
    df.to_csv(output_file, index=False)
    print(f"Mock NLP data collected and saved to {output_file}")

def collect_mock_vision_data(
    output_dir: str = "ImplementationAi/data/mock_vision_data",
) -> None:
    """Collects mock data for the Vision service by creating dummy image files.

    Args:
        output_dir (str): The path to the output directory.
    """
    print("Collecting mock Vision data (simulated image files)...")
    os.makedirs(output_dir, exist_ok=True)
    # Nguyên lý: Tạo các tệp giả lập (.jpg, .png) để mô phỏng hình ảnh thô.
    # Luồng hoạt động: Tệp này chỉ chứa 'dummy data' nhưng mô phỏng sự tồn tại của dữ liệu hình ảnh.
    # Hoạt động thu thập AI: Tạo dữ liệu thô (mock) cho mô hình Thị giác Máy tính (dạng tệp).
    with open(os.path.join(output_dir, "apple.jpg"), "w", encoding="utf-8") as f:
        f.write("dummy image data for apple")
    with open(os.path.join(output_dir, "pizza.png"), "w", encoding="utf-8") as f:
        f.write("dummy image data for pizza")
    print(f"Mock Vision data collected and saved to {output_dir}")

def collect_mock_recommendation_data(
    output_file: str = "ImplementationAi/data/mock_recommendation_data.csv",
) -> None:
    """Collects mock data for the recommendation service.

    Args:
        output_file (str): The path to the output CSV file.
    """
    print("Collecting mock Recommendation data...")
    # Nguyên lý: Tạo DataFrame với dữ liệu hồ sơ người dùng, mục tiêu và thói quen ăn uống giả lập.
    # Luồng hoạt động: Dữ liệu này sẽ được dùng để huấn luyện/kiểm tra mô hình khuyến nghị.
    # Hoạt động thu thập AI: Tạo dữ liệu thô (mock) cho mô hình Khuyến nghị (hồ sơ người dùng/mục tiêu).
    data = {
        "user_id": ["user1", "user2", "user3"],
        "age": [30, 24, 45],
        "weight": [70, 60, 80],
        "dietary_preferences": ["vegan", "low-carb", "none"],
        "health_goals": ["weight_loss", "muscle_gain", "maintain"],
        "food_choices": [
            "oatmeal, fruit smoothie",
            "chicken breast, salad",
            "salmon, vegetables, beef steak",
        ],
    }
    output_dir = os.path.dirname(output_file)
    if output_dir:
        os.makedirs(output_dir, exist_ok=True)

    df = pd.DataFrame(data)
    df.to_csv(output_file, index=False)
    print(f"Mock Recommendation data collected and saved to {output_file}")

if __name__ == "__main__":
    # Luồng hoạt động: Khi chạy trực tiếp, hàm sẽ tạo ra tất cả các tệp dữ liệu mock.
    collect_mock_nlp_data()
    collect_mock_vision_data()
    collect_mock_recommendation_data()