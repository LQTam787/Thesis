import pandas as pd
import numpy as np
from PIL import Image
import os

def preprocess_nlp_data(input_file: str = "ImplementationAi/data/mock_nlp_data.csv", output_file: str = "ImplementationAi/data/processed_nlp_data.csv") -> None:
    print("Preprocessing NLP data...")
    df = pd.read_csv(input_file)
    df['text_cleaned'] = df['text'].str.lower().str.replace(r'[^\w\s]', '', regex=True) # Simple cleaning
    df.to_csv(output_file, index=False)
    print(f"Processed NLP data saved to {output_file}")

def preprocess_vision_data(input_dir: str = "ImplementationAi/data/mock_vision_data", output_dir: str = "ImplementationAi/data/processed_vision_data") -> None:
    print("Preprocessing Vision data...")
    os.makedirs(output_dir, exist_ok=True)
    for filename in os.listdir(input_dir):
        if filename.endswith((".jpg", ".png")):
            filepath = os.path.join(input_dir, filename)
            # In a real scenario, resize, normalize, etc.
            # For mock, just copy or create a dummy processed file
            with open(os.path.join(output_dir, f"processed_{filename}"), "w") as f: f.write(f"processed data for {filename}")
    print(f"Processed Vision data saved to {output_dir}")

def preprocess_recommendation_data(input_file: str = "ImplementationAi/data/mock_recommendation_data.csv", output_file: str = "ImplementationAi/data/processed_recommendation_data.csv") -> None:
    print("Preprocessing Recommendation data...")
    df = pd.read_csv(input_file)
    # Example: one-hot encode categorical features for a machine learning model
    df = pd.get_dummies(df, columns=['dietary_preferences', 'health_goals'], prefix=['diet', 'goal'])
    df.to_csv(output_file, index=False)
    print(f"Processed Recommendation data saved to {output_file}")

if __name__ == "__main__":
    preprocess_nlp_data()
    preprocess_vision_data()
    preprocess_recommendation_data()
