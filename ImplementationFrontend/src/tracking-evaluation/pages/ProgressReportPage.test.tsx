import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ProgressReportPage from './ProgressReportPage';

describe('ProgressReportPage', () => {
  // Test case 1: Renders correctly with initial data
  test('renders correctly with initial data', () => {
    render(<ProgressReportPage />);

    expect(screen.getByText('Báo Cáo Tiến Độ và Đánh Giá')).toBeInTheDocument();
    expect(screen.getByText('Tiến Độ Cân Nặng')).toBeInTheDocument();
    expect(screen.getByText('Chỉ Số Cơ Thể')).toBeInTheDocument();
    expect(screen.getByText('Báo Cáo Dinh Dưỡng')).toBeInTheDocument();
    expect(screen.getByText('Gợi Ý Điều Chỉnh')).toBeInTheDocument();

    // Check weight progress section
    expect(screen.getByText('Cân nặng hiện tại:').closest('div')).toHaveTextContent('Cân nặng hiện tại: 70 kg');
    expect(screen.getByText('Thay đổi trong 7 ngày qua:').closest('div')).toHaveTextContent('Thay đổi trong 7 ngày qua: -0.5 kg');

    // Check body metrics section
    expect(screen.getByText('BMI:').closest('div')).toHaveTextContent('BMI: 22.9 (Bình thường)');
    expect(screen.getByText('Tỷ lệ mỡ cơ thể:').closest('div')).toHaveTextContent('Tỷ lệ mỡ cơ thể: 18%');

    // Check nutrition report section
    expect(screen.getByText('Tổng Calo tiêu thụ hôm nay:').closest('div')).toHaveTextContent('Tổng Calo tiêu thụ hôm nay: 1800 Calo (Mục tiêu: 2000 Calo)');
    expect(screen.getByText('Protein:').closest('div')).toHaveTextContent('Protein: 120g (Mục tiêu: 150g)');
    expect(screen.getByText('Carb:').closest('div')).toHaveTextContent('Carb: 180g (Mục tiêu: 200g)');
    expect(screen.getByText('Fat:').closest('div')).toHaveTextContent('Fat: 60g (Mục tiêu: 70g)');

    // Check advice section
    expect(screen.getByText(
      /Bạn đang có xu hướng giảm nhẹ lượng protein. Hãy cân nhắc bổ sung thêm các nguồn protein từ thịt nạc, trứng hoặc các sản phẩm từ sữa. Hoạt động thể chất của bạn đang rất tốt, hãy duy trì cường độ này để đạt được mục tiêu./i
    )).toBeInTheDocument();

    expect(screen.getByRole('button', { name: /Tải lại Báo cáo/i })).toBeInTheDocument();
  });

  // Test case 2: generateNewReport function is called on button click
  test('calls generateNewReport on button click', () => {
    const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
    const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

    render(<ProgressReportPage />);
    const reloadButton = screen.getByRole('button', { name: /Tải lại Báo cáo/i });
    fireEvent.click(reloadButton);

    expect(alertMock).toHaveBeenCalledWith('Đang tạo báo cáo mới...');
    expect(consoleSpy).toHaveBeenCalledWith('Generating new report...');

    alertMock.mockRestore();
    consoleSpy.mockRestore();
  });
});
