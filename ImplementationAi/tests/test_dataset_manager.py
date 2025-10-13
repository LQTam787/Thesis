import pandas as pd
from unittest.mock import patch

from src.dataset_manager import (
    load_nlp_dataset,
    load_vision_dataset,
    load_recommendation_dataset
)

# Test for load_nlp_dataset
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.pd.read_csv')
def test_load_nlp_dataset_exists(mock_read_csv, mock_exists):
    """Tests loading the NLP dataset when the file exists."""
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
    """Tests loading the NLP dataset when the file does not exist."""
    mock_exists.return_value = False

    df = load_nlp_dataset('non_existent.csv')

    mock_exists.assert_called_once_with('non_existent.csv')
    assert df.empty
    captured = capsys.readouterr()
    assert "Warning: NLP dataset not found" in captured.out

# Test for load_vision_dataset
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.os.listdir')
def test_load_vision_dataset_exists(mock_listdir, mock_exists):
    """Tests loading the vision dataset when the directory exists."""
    mock_exists.return_value = True
    mock_listdir.return_value = ['processed_img1.jpg', 'other_file.txt']

    file_list = load_vision_dataset('dummy_dir')

    mock_exists.assert_called_once_with('dummy_dir')
    mock_listdir.assert_called_once_with('dummy_dir')
    assert len(file_list) == 1
    assert 'processed_img1.jpg' in file_list[0]

@patch('src.dataset_manager.os.path.exists')
def test_load_vision_dataset_not_found(mock_exists, capsys):
    """Tests loading the vision dataset when the directory does not exist."""
    mock_exists.return_value = False

    file_list = load_vision_dataset('non_existent_dir')

    mock_exists.assert_called_once_with('non_existent_dir')
    assert len(file_list) == 0
    captured = capsys.readouterr()
    assert "Warning: Vision dataset not found" in captured.out

# Test for load_recommendation_dataset
@patch('src.dataset_manager.os.path.exists')
@patch('src.dataset_manager.pd.read_csv')
def test_load_recommendation_dataset_exists(mock_read_csv, mock_exists):
    """Tests loading the recommendation dataset when the file exists."""
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
    """Tests loading the recommendation dataset when the file does not exist."""
    mock_exists.return_value = False

    df = load_recommendation_dataset('non_existent_rec.csv')

    mock_exists.assert_called_once_with('non_existent_rec.csv')
    assert df.empty
    captured = capsys.readouterr()
    assert "Warning: Recommendation dataset not found" in captured.out