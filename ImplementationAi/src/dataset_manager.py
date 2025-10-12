import pandas as pd
import os

def load_nlp_dataset(file_path: str = "ImplementationAi/data/processed_nlp_data.csv") -> pd.DataFrame:
    print(f"Loading NLP dataset from {file_path}")
    if os.path.exists(file_path):
        return pd.read_csv(file_path)
    else:
        print(f"Warning: NLP dataset not found at {file_path}. Please run data_preprocessor.py first.")
        return pd.DataFrame()

def load_vision_dataset(dir_path: str = "ImplementationAi/data/processed_vision_data") -> list:
    print(f"Loading Vision dataset from {dir_path}")
    images = []
    if os.path.exists(dir_path):
        for filename in os.listdir(dir_path):
            if filename.startswith("processed_"):
                images.append(os.path.join(dir_path, filename))
    else:
        print(f"Warning: Vision dataset not found at {dir_path}. Please run data_preprocessor.py first.")
    return images

def load_recommendation_dataset(file_path: str = "ImplementationAi/data/processed_recommendation_data.csv") -> pd.DataFrame:
    print(f"Loading Recommendation dataset from {file_path}")
    if os.path.exists(file_path):
        return pd.read_csv(file_path)
    else:
        print(f"Warning: Recommendation dataset not found at {file_path}. Please run data_preprocessor.py first.")
        return pd.DataFrame()

if __name__ == "__main__":
    # Example usage
    nlp_df = load_nlp_dataset()
    print(f"NLP data head:\n{nlp_df.head()}")

    vision_files = load_vision_dataset()
    print(f"Vision files: {vision_files[:2]}")

    rec_df = load_recommendation_dataset()
    print(f"Recommendation data head:\n{rec_df.head()}")
