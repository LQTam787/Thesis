"""This module contains functions to preprocess the mock data."""
import os
import pandas as pd

def preprocess_nlp_data(
    input_file: str = "ImplementationAi/data/mock_nlp_data.csv",
    output_file: str = "ImplementationAi/data/processed_nlp_data.csv",
) -> None:
    """Preprocesses the NLP data by cleaning the text.

    Args:
        input_file (str): The path to the input CSV file.
        output_file (str): The path to the output CSV file.
    """
    print("Preprocessing NLP data...")
    df = pd.read_csv(input_file)
    # Simple cleaning: lowercase and remove non-alphanumeric characters
    df["text_cleaned"] = df["text"].str.lower().str.replace(r"[^\w\s]", "", regex=True)
    df.to_csv(output_file, index=False)
    print(f"Processed NLP data saved to {output_file}")

def preprocess_vision_data(
    input_dir: str = "ImplementationAi/data/mock_vision_data",
    output_dir: str = "ImplementationAi/data/processed_vision_data",
) -> None:
    """Simulates preprocessing of vision data.

    Args:
        input_dir (str): The path to the input directory.
        output_dir (str): The path to the output directory.
    """
    print("Preprocessing Vision data...")
    os.makedirs(output_dir, exist_ok=True)
    for filename in os.listdir(input_dir):
        if filename.endswith((".jpg", ".png")):
            # In a real scenario, this would involve resizing, normalization, etc.
            # For this mock, we just create a dummy processed file.
            processed_filepath = os.path.join(output_dir, f"processed_{filename}")
            with open(processed_filepath, "w", encoding="utf-8") as f:
                f.write(f"processed data for {filename}")
    print(f"Processed Vision data saved to {output_dir}")

def preprocess_recommendation_data(
    input_file: str = "ImplementationAi/data/mock_recommendation_data.csv",
    output_file: str = "ImplementationAi/data/processed_recommendation_data.csv",
) -> None:
    """Preprocesses recommendation data using one-hot encoding.

    Args:
        input_file (str): The path to the input CSV file.
        output_file (str): The path to the output CSV file.
    """
    print("Preprocessing Recommendation data...")
    df = pd.read_csv(input_file)
    # One-hot encode categorical features for a machine learning model
    df = pd.get_dummies(
        df, columns=["dietary_preferences", "health_goals"], prefix=["diet", "goal"]
    )
    df.to_csv(output_file, index=False)
    print(f"Processed Recommendation data saved to {output_file}")

if __name__ == "__main__":
    preprocess_nlp_data()
    preprocess_vision_data()
    preprocess_recommendation_data()
