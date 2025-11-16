# tests/test_data_collector.py

import os
import shutil
import pandas as pd
from unittest.mock import patch, MagicMock

from src.data_collector import (
    collect_mock_nlp_data,
    collect_mock_vision_data,
    collect_mock_recommendation_data,
)

# Đường dẫn thư mục dữ liệu mock cho các bài kiểm tra
TEST_DATA_DIR = "ImplementationAi/data/test_mock_data"


def setup_function():
    """Thiết lập môi trường kiểm thử: đảm bảo thư mục TEST_DATA_DIR tồn tại."""
    os.makedirs(TEST_DATA_DIR, exist_ok=True)


def teardown_function():
    """Dọn dẹp môi trường kiểm thử: xóa thư mục TEST_DATA_DIR sau khi chạy các bài kiểm thử."""
    if os.path.exists(TEST_DATA_DIR):
        shutil.rmtree(TEST_DATA_DIR)


def test_collect_mock_nlp_data():
    """Kiểm tra chức năng collect_mock_nlp_data."""
    output_file = os.path.join(TEST_DATA_DIR, "mock_nlp_data.csv")
    collect_mock_nlp_data(output_file=output_file)

    assert os.path.exists(output_file)
    df = pd.read_csv(output_file)
    assert not df.empty
    assert "text" in df.columns
    assert "labels" in df.columns
    assert len(df) == 4  # Kiểm tra số lượng hàng


def test_collect_mock_vision_data():
    """Kiểm tra chức năng collect_mock_vision_data."""
    output_dir = os.path.join(TEST_DATA_DIR, "mock_vision_data")
    collect_mock_vision_data(output_dir=output_dir)

    assert os.path.exists(output_dir)
    assert os.path.isdir(output_dir)
    assert os.path.exists(os.path.join(output_dir, "apple.jpg"))
    assert os.path.exists(os.path.join(output_dir, "pizza.png"))


def test_collect_mock_recommendation_data():
    """Kiểm tra chức năng collect_mock_recommendation_data."""
    output_file = os.path.join(TEST_DATA_DIR, "mock_recommendation_data.csv")
    collect_mock_recommendation_data(output_file=output_file)

    assert os.path.exists(output_file)
    df = pd.read_csv(output_file)
    assert not df.empty
    assert "user_id" in df.columns
    assert "age" in df.columns
    assert len(df) == 3  # Kiểm tra số lượng hàng


def test_main_functionality():
    """Kiểm tra khi chạy data_collector.py trực tiếp (thông qua hàm main)."""
    with (
        patch("src.data_collector.collect_mock_nlp_data") as mock_nlp,
        patch("src.data_collector.collect_mock_vision_data") as mock_vision,
        patch("src.data_collector.collect_mock_recommendation_data") as mock_recommendation,
    ):
        from src.data_collector import main
        main()

        mock_nlp.assert_called_once_with()
        mock_vision.assert_called_once_with()
        mock_recommendation.assert_called_once_with()
