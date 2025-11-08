"""This module provides functions to load the processed datasets.

This script is responsible for loading the preprocessed data from the CSV files and 
image directories into memory. It provides a centralized way to access the datasets 
for the NLP, Vision, and Recommendation services. The functions in this module are 
used to load the data into pandas DataFrames or lists of file paths, making it easy 
to use them in other parts of the application.
"""
import os
import pandas as pd

def load_nlp_dataset(
    file_path: str = "ImplementationAi/data/processed_nlp_data.csv",
) -> pd.DataFrame:
    """Loads the processed NLP dataset.

    Args:
        file_path (str): The path to the processed NLP data CSV file.

    Returns:
        pd.DataFrame: The loaded NLP data as a DataFrame.
    """
    print(f"Loading NLP dataset from {file_path}")
    # Nguyên lý: Kiểm tra sự tồn tại và tải dữ liệu NLP đã được làm sạch (từ data_preprocessor.py).
    # Luồng hoạt động: Trả về DataFrame hoặc DataFrame rỗng kèm cảnh báo.
    # Hoạt động tải dữ liệu AI: Tải tập dữ liệu NLP đã sẵn sàng để huấn luyện/kiểm tra.
    if os.path.exists(file_path):
        return pd.read_csv(file_path)
    print(
        f"Warning: NLP dataset not found at {file_path}. "
        f"Please run data_preprocessor.py first."
    )
    return pd.DataFrame()

def load_vision_dataset(dir_path: str = "ImplementationAi/data/processed_vision_data") -> list:
    """Loads the processed vision dataset file paths.

    Args:
        dir_path (str): The path to the directory with processed vision data.

    Returns:
        list: A list of file paths for the processed images.
    """
    print(f"Loading Vision dataset from {dir_path}")
    images = []
    # Nguyên lý: Lấy danh sách các tệp hình ảnh đã được tiền xử lý (có tiền tố 'processed_').
    # Luồng hoạt động: Trả về danh sách các đường dẫn tệp.
    # Hoạt động tải dữ liệu AI: Lấy danh sách đường dẫn tệp hình ảnh đã được xử lý.
    if os.path.exists(dir_path):
        for filename in os.listdir(dir_path):
            if filename.startswith("processed_"):
                images.append(os.path.join(dir_path, filename))
    else:
        print(
            f"Warning: Vision dataset not found at {dir_path}. "
            f"Please run data_preprocessor.py first."
        )
    return images

def load_recommendation_dataset(
    file_path: str = "ImplementationAi/data/processed_recommendation_data.csv",
) -> pd.DataFrame:
    """Loads the processed recommendation dataset.

    Args:
        file_path (str): The path to the processed recommendation data CSV file.

    Returns:
        pd.DataFrame: The loaded recommendation data as a DataFrame.
    """
    print(f"Loading Recommendation dataset from {file_path}")
    # Nguyên lý: Tải dữ liệu Khuyến nghị đã được mã hóa one-hot.
    # Luồng hoạt động: Trả về DataFrame chứa dữ liệu người dùng/mục tiêu đã được số hóa.
    # Hoạt động tải dữ liệu AI: Tải tập dữ liệu Khuyến nghị đã sẵn sàng (với mã hóa one-hot).
    if os.path.exists(file_path):
        return pd.read_csv(file_path)
    print(
        f"Warning: Recommendation dataset not found at {file_path}. "
        f"Please run data_preprocessor.py first."
    )
    return pd.DataFrame()

if __name__ == "__main__":
    # Luồng hoạt động: Ví dụ kiểm tra cách tải và hiển thị dữ liệu đã tải.
    # Example usage
    nlp_data = load_nlp_dataset()
    print(f"NLP data head:\n{nlp_data.head()}")

    vision_data = load_vision_dataset()
    print(f"Vision files: {vision_data[:2]}")

    recommendation_data = load_recommendation_dataset()
    print(f"Recommendation data head:\n{recommendation_data.head()}")