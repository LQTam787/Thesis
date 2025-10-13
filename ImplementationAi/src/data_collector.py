"""This module contains functions to generate mock data for the AI services."""
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
    # Create dummy files to simulate images
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
    df = pd.DataFrame(data)
    df.to_csv(output_file, index=False)
    print(f"Mock Recommendation data collected and saved to {output_file}")

if __name__ == "__main__":
    collect_mock_nlp_data()
    collect_mock_vision_data()
    collect_mock_recommendation_data()
