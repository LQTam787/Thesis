
import os
import pandas as pd
import pytest
import subprocess
import tempfile
from src.data_preprocessor import (
    preprocess_nlp_data,
    preprocess_vision_data,
    preprocess_recommendation_data,
    run_all_preprocessing,
)

# Đường dẫn đến thư mục dữ liệu giả lập
MOCK_DATA_DIR = "ImplementationAi/data"
MOCK_NLP_INPUT = os.path.join(MOCK_DATA_DIR, "test_mock_nlp_data.csv")
MOCK_NLP_OUTPUT = os.path.join(MOCK_DATA_DIR, "test_processed_nlp_data.csv")
MOCK_RECOMMENDATION_INPUT = os.path.join(MOCK_DATA_DIR, "test_mock_recommendation_data.csv")
MOCK_RECOMMENDATION_OUTPUT = os.path.join(
    MOCK_DATA_DIR, "test_processed_recommendation_data.csv"
)
MOCK_VISION_INPUT_DIR = os.path.join(MOCK_DATA_DIR, "test_mock_vision_data")
MOCK_VISION_OUTPUT_DIR = os.path.join(MOCK_DATA_DIR, "test_processed_vision_data")


@pytest.fixture(autouse=True)
def setup_and_teardown():
    # Setup: Đảm bảo thư mục dữ liệu giả lập tồn tại
    os.makedirs(MOCK_DATA_DIR, exist_ok=True)
    os.makedirs(MOCK_VISION_INPUT_DIR, exist_ok=True)
    os.makedirs(MOCK_VISION_OUTPUT_DIR, exist_ok=True)

    yield

    # Teardown: Xóa tất cả các file và thư mục giả lập sau khi test
    for f in [
        MOCK_NLP_INPUT,
        MOCK_NLP_OUTPUT,
        MOCK_RECOMMENDATION_INPUT,
        MOCK_RECOMMENDATION_OUTPUT,
    ]:
        if os.path.exists(f):
            os.remove(f)

    for d in [MOCK_VISION_INPUT_DIR, MOCK_VISION_OUTPUT_DIR]:
        if os.path.exists(d):
            for f in os.listdir(d):
                os.remove(os.path.join(d, f))
            # Chỉ xóa thư mục nếu nó rỗng, để tránh lỗi khi các test chạy đồng thời
            if not os.listdir(d):
                os.rmdir(d)


def test_preprocess_nlp_data():
    # Test case 1: Dữ liệu NLP cơ bản
    df_nlp = pd.DataFrame({"text": ["Hello, World!", "Python is great.", "  leading/trailing spaces ",
                                  "Mixed Case and Punctuation!@#$"]})
    df_nlp.to_csv(MOCK_NLP_INPUT, index=False)

    preprocess_nlp_data(input_file=MOCK_NLP_INPUT, output_file=MOCK_NLP_OUTPUT)

    assert os.path.exists(MOCK_NLP_OUTPUT)
    processed_df = pd.read_csv(MOCK_NLP_OUTPUT)
    assert "text_cleaned" in processed_df.columns
    assert list(processed_df["text_cleaned"]) == [
        "hello world",
        "python is great",
        "  leadingtrailing spaces ",
        "mixed case and punctuation"
    ]

    # Test case 2: File input rỗng
    df_empty = pd.DataFrame({"text": []})
    df_empty.to_csv(MOCK_NLP_INPUT, index=False)
    preprocess_nlp_data(input_file=MOCK_NLP_INPUT, output_file=MOCK_NLP_OUTPUT)
    processed_df_empty = pd.read_csv(MOCK_NLP_OUTPUT)
    assert processed_df_empty.empty


def test_preprocess_vision_data():
    # Tạo các file ảnh giả
    with open(os.path.join(MOCK_VISION_INPUT_DIR, "image1.jpg"), "w") as f:
        f.write("dummy jpg content")
    with open(os.path.join(MOCK_VISION_INPUT_DIR, "image2.png"), "w") as f:
        f.write("dummy png content")
    with open(os.path.join(MOCK_VISION_INPUT_DIR, "document.txt"), "w") as f:
        f.write("dummy txt content")  # Should be ignored

    preprocess_vision_data(input_dir=MOCK_VISION_INPUT_DIR, output_dir=MOCK_VISION_OUTPUT_DIR)

    assert os.path.exists(MOCK_VISION_OUTPUT_DIR)
    processed_files = os.listdir(MOCK_VISION_OUTPUT_DIR)
    assert "processed_image1.jpg" in processed_files
    assert "processed_image2.png" in processed_files
    assert "document.txt" not in processed_files  # Ensure non-image files are ignored
    assert len(processed_files) == 2

    # Test case: Thư mục input rỗng
    # Xóa các file đã tạo để đảm bảo thư mục rỗng
    for f in os.listdir(MOCK_VISION_INPUT_DIR):
        os.remove(os.path.join(MOCK_VISION_INPUT_DIR, f))
    # Dọn dẹp thư mục output trước khi test trường hợp rỗng
    for f in os.listdir(MOCK_VISION_OUTPUT_DIR):
        os.remove(os.path.join(MOCK_VISION_OUTPUT_DIR, f))

    preprocess_vision_data(input_dir=MOCK_VISION_INPUT_DIR, output_dir=MOCK_VISION_OUTPUT_DIR)
    processed_files_empty = os.listdir(MOCK_VISION_OUTPUT_DIR)
    assert len(processed_files_empty) == 0


def test_preprocess_recommendation_data():
    # Test case 1: Dữ liệu khuyến nghị cơ bản
    df_rec = pd.DataFrame(
        {
            "user_id": [1, 2, 3],
            "dietary_preferences": ["vegan", "vegetarian", "pescatarian"],
            "health_goals": ["lose_weight", "build_muscle", "maintain_weight"],
            "food_item": ["apple", "banana", "carrot"],
        }
    )
    df_rec.to_csv(MOCK_RECOMMENDATION_INPUT, index=False)

    preprocess_recommendation_data(
        input_file=MOCK_RECOMMENDATION_INPUT,
        output_file=MOCK_RECOMMENDATION_OUTPUT,
    )

    assert os.path.exists(MOCK_RECOMMENDATION_OUTPUT)
    processed_df = pd.read_csv(MOCK_RECOMMENDATION_OUTPUT)

    expected_columns = [
        "user_id",
        "food_item",
        "diet_pescatarian",
        "diet_vegan",
        "diet_vegetarian",
        "goal_build_muscle",
        "goal_lose_weight",
        "goal_maintain_weight",
    ]
    assert all(col in processed_df.columns for col in expected_columns)
    assert processed_df["diet_vegan"].iloc[0] == 1
    assert processed_df["goal_lose_weight"].iloc[0] == 1
    assert processed_df["diet_vegetarian"].iloc[1] == 1
    assert processed_df["goal_build_muscle"].iloc[1] == 1
    assert processed_df["diet_pescatarian"].iloc[2] == 1
    assert processed_df["goal_maintain_weight"].iloc[2] == 1

    # Test case 2: File input rỗng
    df_empty = pd.DataFrame(
        {"user_id": [], "dietary_preferences": [], "health_goals": [], "food_item": []}
    )
    df_empty.to_csv(MOCK_RECOMMENDATION_INPUT, index=False)
    preprocess_recommendation_data(
        input_file=MOCK_RECOMMENDATION_INPUT,
        output_file=MOCK_RECOMMENDATION_OUTPUT,
    )
    processed_df_empty = pd.read_csv(MOCK_RECOMMENDATION_OUTPUT)
    assert processed_df_empty.empty

    # Test case 3: Dữ liệu với các giá trị NA
    df_na = pd.DataFrame({
        "user_id": [4],
        "dietary_preferences": [None],
        "health_goals": ["build_muscle"],
        "food_item": ["pizza"]
    })
    df_na.to_csv(MOCK_RECOMMENDATION_INPUT, index=False)
    preprocess_recommendation_data(
        input_file=MOCK_RECOMMENDATION_INPUT,
        output_file=MOCK_RECOMMENDATION_OUTPUT,
    )
    processed_df_na = pd.read_csv(MOCK_RECOMMENDATION_OUTPUT)
    assert "diet_nan" not in processed_df_na.columns
    assert processed_df_na["goal_build_muscle"].iloc[0] == 1


def test_main_execution(mocker):
    # Sử dụng tempfile để tạo các tệp tạm thời cho input và output
    with tempfile.TemporaryDirectory() as temp_dir:
        # Mock NLP data
        nlp_input_file = os.path.join(temp_dir, "mock_nlp_data.csv")
        nlp_output_file = os.path.join(temp_dir, "processed_nlp_data.csv")
        pd.DataFrame({"text": ["Hello, World!"]}).to_csv(nlp_input_file, index=False)

        # Mock Vision data directory
        vision_input_dir = os.path.join(temp_dir, "mock_vision_data")
        vision_output_dir = os.path.join(temp_dir, "processed_vision_data")
        os.makedirs(vision_input_dir, exist_ok=True)
        with open(os.path.join(vision_input_dir, "image.jpg"), "w") as f:
            f.write("dummy image")

        # Mock Recommendation data
        rec_input_file = os.path.join(temp_dir, "mock_recommendation_data.csv")
        rec_output_file = os.path.join(temp_dir, "processed_recommendation_data.csv")
        pd.DataFrame({
            "user_id": [1],
            "dietary_preferences": ["vegan"],
            "health_goals": ["lose_weight"]
        }).to_csv(rec_input_file, index=False)

        # Mock các biến môi trường mà run_all_preprocessing sẽ sử dụng
        mocker.patch.dict(os.environ, {
            "NLP_INPUT_FILE": nlp_input_file,
            "NLP_OUTPUT_FILE": nlp_output_file,
            "VISION_INPUT_DIR": vision_input_dir,
            "VISION_OUTPUT_DIR": vision_output_dir,
            "REC_INPUT_FILE": rec_input_file,
            "REC_OUTPUT_FILE": rec_output_file,
        })

        # Gọi trực tiếp hàm run_all_preprocessing
        run_all_preprocessing()

        # Kiểm tra xem các file output có tồn tại không
        assert os.path.exists(nlp_output_file)
        assert os.path.exists(os.path.join(vision_output_dir, "processed_image.jpg"))
        assert os.path.exists(rec_output_file)
