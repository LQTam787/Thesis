
# test_data_preprocessor.py
# Tệp này chứa các kiểm thử cho module 'data_preprocessor.py', đảm bảo rằng các hàm
# tiền xử lý dữ liệu cho NLP, Vision và Recommendation hoạt động chính xác.
# Nó kiểm tra các trường hợp cơ bản, trường hợp rỗng và xử lý các giá trị đặc biệt.

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
# Các biến này định nghĩa đường dẫn đến các tệp và thư mục dữ liệu đầu vào và đầu ra
# giả lập được sử dụng trong quá trình kiểm thử.
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
    # Fixture này thiết lập môi trường kiểm thử bằng cách tạo các thư mục cần thiết
    # trước khi mỗi kiểm thử chạy. Nó đảm bảo rằng các đường dẫn file đầu vào và đầu ra
    # đã sẵn sàng cho các hàm tiền xử lý.
    os.makedirs(MOCK_DATA_DIR, exist_ok=True)
    os.makedirs(MOCK_VISION_INPUT_DIR, exist_ok=True)
    os.makedirs(MOCK_VISION_OUTPUT_DIR, exist_ok=True)

    yield

    # Teardown: Xóa tất cả các file và thư mục giả lập sau khi test
    # Sau khi các kiểm thử hoàn tất, phần teardown này sẽ dọn dẹp môi trường
    # bằng cách xóa tất cả các tệp và thư mục dữ liệu giả lập đã được tạo ra.
    # Điều này giúp đảm bảo tính độc lập và sạch sẽ giữa các lần chạy kiểm thử.
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
    # Kiểm thử này xác minh hàm preprocess_nlp_data với dữ liệu văn bản mẫu.
    # Nó kiểm tra việc làm sạch văn bản, bao gồm loại bỏ dấu câu, chuyển đổi chữ thường,
    # và xử lý khoảng trắng.
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
    # Kiểm thử này đảm bảo rằng hàm preprocess_nlp_data xử lý đúng cách các tệp đầu vào rỗng,
    # tạo ra một DataFrame rỗng.
    df_empty = pd.DataFrame({"text": []})
    df_empty.to_csv(MOCK_NLP_INPUT, index=False)
    preprocess_nlp_data(input_file=MOCK_NLP_INPUT, output_file=MOCK_NLP_OUTPUT)
    processed_df_empty = pd.read_csv(MOCK_NLP_OUTPUT)
    assert processed_df_empty.empty


def test_preprocess_vision_data():
    # Tạo các file ảnh giả
    # Kiểm thử này xác minh hàm preprocess_vision_data, tập trung vào việc xử lý các tệp hình ảnh.
    # Nó tạo ra các tệp hình ảnh giả trong thư mục đầu vào và đảm bảo rằng chỉ các tệp hình ảnh
    # được xử lý và sao chép vào thư mục đầu ra, trong khi các tệp không phải hình ảnh bị bỏ qua.
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
    # Kiểm thử này xử lý trường hợp thư mục đầu vào rỗng, đảm bảo rằng thư mục đầu ra cũng rỗng.
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
    # Kiểm thử này xác minh hàm preprocess_recommendation_data. Nó tạo dữ liệu giả lập
    # cho sở thích ăn kiêng và mục tiêu sức khỏe, sau đó áp dụng mã hóa one-hot
    # để chuyển đổi các cột phân loại thành định dạng số.
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
    # Kiểm thử này đảm bảo rằng hàm preprocess_recommendation_data xử lý đúng cách các tệp đầu vào rỗng,
    # tạo ra một DataFrame rỗng.
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
    # Kiểm thử này xác minh khả năng của hàm xử lý các giá trị thiếu (None) trong các cột phân loại,
    # đảm bảo rằng chúng không tạo ra các cột mới không mong muốn trong đầu ra đã xử lý.
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
    # Kiểm thử này mô phỏng việc thực thi hàm `run_all_preprocessing` (thường được gọi từ main).
    # Nó tạo các tệp đầu vào giả lập trong một thư mục tạm thời và sử dụng `mocker.patch.dict`
    # để ghi đè các biến môi trường mà hàm này sử dụng. Cuối cùng, nó xác minh rằng
    # tất cả các tệp đầu ra đã xử lý được tạo ra thành công.
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
