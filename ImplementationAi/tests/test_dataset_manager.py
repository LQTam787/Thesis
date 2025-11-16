# test_dataset_manager.py
# Tệp này chứa các kiểm thử cho module 'dataset_manager.py', đảm bảo rằng các hàm
# tải tập dữ liệu cho NLP, Vision và Recommendation hoạt động chính xác. Nó tập trung
# vào việc xác minh rằng các tệp/thư mục được tải đúng cách và các trường hợp lỗi
# (tệp không tìm thấy) được xử lý một cách duyên dáng.

import pandas as pd
from unittest.mock import patch

from src.dataset_manager import (
    load_nlp_dataset,
    load_vision_dataset,
    load_recommendation_dataset
)

# Test for load_nlp_dataset
# Các decoractor @patch được sử dụng để giả lập các hàm của hệ thống và thư viện Pandas
# mà 'load_nlp_dataset' gọi. Điều này cho phép kiểm thử chức năng tải mà không cần
# thực sự đọc từ hoặc ghi vào hệ thống tệp.
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.pd.read_csv')
def test_load_nlp_dataset_exists(mock_read_csv, mock_exists):
    """
    Kiểm tra việc tải tập dữ liệu NLP thành công.

    Luồng hoạt động của Mock:
    1. `mock_exists.return_value = True`: Giả lập rằng tệp CSV **tồn tại**.
    2. `mock_read_csv.return_value = mock_df`: Giả lập Pandas đọc tệp và trả về
       một DataFrame giả.
    - Nguyên lý: Xác minh hàm gọi đúng `os.path.exists` và `pd.read_csv` với đường dẫn
      chính xác, và trả về DataFrame giả đã được định trước.
    """
    mock_exists.return_value = True
    mock_df = pd.DataFrame({'text': ['test sentence']})
    mock_read_csv.return_value = mock_df

    df = load_nlp_dataset('dummy_path.csv')

    mock_exists.assert_called_once_with('dummy_path.csv')
    mock_read_csv.assert_called_once_with('dummy_path.csv')
    assert not df.empty
    assert df.equals(mock_df)

@patch('src.dataset_manager.os.path.exists')
def test_load_nlp_dataset_not_found(mock_exists, capsys):
    """
    Kiểm tra xử lý khi tập dữ liệu NLP không tìm thấy.

    Luồng hoạt động của Mock:
    1. `mock_exists.return_value = False`: Giả lập rằng tệp CSV **không tồn tại**.
    2. Sử dụng `capsys` (một fixture của pytest) để bắt đầu ra của console.
    - Nguyên lý: Xác minh hàm gọi `os.path.exists`, trả về DataFrame rỗng,
      và in ra thông báo cảnh báo (Warning).
    """
    mock_exists.return_value = False

    df = load_nlp_dataset('non_existent.csv')

    mock_exists.assert_called_once_with('non_existent.csv')
    assert df.empty
    captured = capsys.readouterr()
    assert "Warning: NLP dataset not found" in captured.out

# Test for load_vision_dataset
# Tương tự như kiểm thử NLP, các hàm của hệ thống tệp được giả lập để kiểm soát
# hành vi của việc tải tập dữ liệu hình ảnh. Kiểm thử này đảm bảo rằng chỉ các tệp
# hình ảnh hợp lệ (có tiền tố 'processed_') được trả về.
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.os.listdir')
def test_load_vision_dataset_exists(mock_listdir, mock_exists):
    """
    Kiểm tra việc tải tập dữ liệu Hình ảnh thành công.

    Luồng hoạt động của Mock:
    1. `mock_exists.return_value = True`: Giả lập thư mục **tồn tại**.
    2. `mock_listdir.return_value = [...]`: Giả lập việc liệt kê các tệp trong thư mục.
    - Nguyên lý: Xác minh hàm gọi đúng `os.path.exists` và `os.listdir`, đồng thời
      chỉ lọc và trả về các tệp hình ảnh có liên quan (ví dụ: loại bỏ `other_file.txt`).
      Chú ý: Giả lập rằng hàm chỉ quan tâm đến các tệp đã được xử lý (processed_).
    """
    mock_exists.return_value = True
    mock_listdir.return_value = ['processed_img1.jpg', 'other_file.txt']

    file_list = load_vision_dataset('dummy_dir')

    mock_exists.assert_called_once_with('dummy_dir')
    mock_listdir.assert_called_once_with('dummy_dir')
    assert len(file_list) == 1
    assert 'processed_img1.jpg' in file_list[0]

@patch('src.dataset_manager.os.path.exists')
def test_load_vision_dataset_not_found(mock_exists, capsys):
    """
    Kiểm tra xử lý khi tập dữ liệu Hình ảnh (thư mục) không tìm thấy.
    - Nguyên lý: Xác minh hàm gọi `os.path.exists`, trả về danh sách rỗng,
      và in ra thông báo cảnh báo. (Tương tự test NLP not found).
    """
    mock_exists.return_value = False

    file_list = load_vision_dataset('non_existent_dir')

    mock_exists.assert_called_once_with('non_existent_dir')
    assert len(file_list) == 0
    captured = capsys.readouterr()
    assert "Warning: Vision dataset not found" in captured.out

# Test for load_recommendation_dataset (Tương tự NLP)
# Kiểm thử cho chức năng tải tập dữ liệu đề xuất, với các giả lập tương tự
# được sử dụng trong kiểm thử NLP để xác minh việc tải thành công và xử lý lỗi.
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.pd.read_csv')
def test_load_recommendation_dataset_exists(mock_read_csv, mock_exists):
    """Kiểm tra việc tải tập dữ liệu Đề xuất thành công."""
    mock_exists.return_value = True
    mock_df = pd.DataFrame({'user_id': [1]})
    mock_read_csv.return_value = mock_df

    df = load_recommendation_dataset('dummy_rec.csv')

    mock_exists.assert_called_once_with('dummy_rec.csv')
    mock_read_csv.assert_called_once_with('dummy_rec.csv')
    assert not df.empty
    assert df.equals(mock_df)

@patch('src.dataset_manager.os.path.exists')
def test_load_recommendation_dataset_not_found(mock_exists, capsys):
    """Kiểm tra xử lý khi tập dữ liệu Đề xuất không tìm thấy."""
    mock_exists.return_value = False

    df = load_recommendation_dataset('non_existent_rec.csv')

    mock_exists.assert_called_once_with('non_existent_rec.csv')
    assert df.empty
    captured = capsys.readouterr()
    assert "Warning: Recommendation dataset not found" in captured.out