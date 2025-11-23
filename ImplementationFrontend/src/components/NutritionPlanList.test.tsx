import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import NutritionPlanList from './NutritionPlanList';
import { CheDoDinhDuong } from '../types';

// Mock localStorage
const localStorageMock = (
  function() {
    let store: { [key: string]: string } = {};
    return {
      getItem: function(key: string) {
        return store[key] || null;
      },
      setItem: function(key: string, value: string) {
        store[key] = value.toString();
      },
      removeItem: function(key: string) {
        delete store[key];
      },
      clear: function() {
        store = {};
      }
    };
  }
)();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

// Mock confirm function
window.confirm = jest.fn(() => true);

document.execCommand = jest.fn();

describe('NutritionPlanList', () => {
  beforeEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  const mockNutritionPlans: CheDoDinhDuong[] = [
    {
      maCheDo: 'CDD001',
      tenCheDo: 'Chế độ ăn kiêng Keto',
      moTa: 'Giảm cân nhanh chóng với chế độ ăn ít carb.',
      ngayBatDau: '2023-01-01',
      ngayKetThuc: '2023-03-31',
      mucTieu: 'Giảm 10kg',
      buaAn: [],
    },
    {
      maCheDo: 'CDD002',
      tenCheDo: 'Chế độ tăng cơ bắp',
      moTa: 'Xây dựng cơ bắp với protein cao và tập luyện đều đặn.',
      ngayBatDau: '2023-02-01',
      ngayKetThuc: '2023-05-31',
      mucTieu: 'Tăng 5kg cơ bắp',
      buaAn: [],
    },
  ];

  test('hiển thị tiêu đề và nút tạo mới', () => {
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );
    expect(screen.getByText('Quản lý Chế độ Dinh dưỡng')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Tạo mới Chế độ Dinh dưỡng/i })).toBeInTheDocument();
  });

  test('hiển thị danh sách kế hoạch dinh dưỡng từ localStorage', () => {
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    expect(screen.getByText('Chế độ ăn kiêng Keto')).toBeInTheDocument();
    expect(screen.getByText('Giảm cân nhanh chóng với chế độ ăn ít carb.')).toBeInTheDocument();
    expect(screen.getByText('Chế độ tăng cơ bắp')).toBeInTheDocument();
    expect(screen.getByText('Xây dựng cơ bắp với protein cao và tập luyện đều đặn.')).toBeInTheDocument();
  });

  test('không hiển thị kế hoạch dinh dưỡng nếu localStorage trống', () => {
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );
    expect(screen.queryByText('Chế độ ăn kiêng Keto')).not.toBeInTheDocument();
  });

  test('xử lý xóa một kế hoạch dinh dưỡng', async () => {
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    expect(screen.getByText('Chế độ ăn kiêng Keto')).toBeInTheDocument();

    fireEvent.click(screen.getAllByText('Xóa')[0]);

    expect(window.confirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn xóa chế độ dinh dưỡng này không?');

    await waitFor(() => {
      expect(screen.queryByText('Chế độ ăn kiêng Keto')).not.toBeInTheDocument();
      expect(screen.getByText('Xóa chế độ dinh dưỡng thành công!')).toBeInTheDocument();
    });
    expect(JSON.parse(localStorage.getItem('nutritionPlans') || '[]')).toEqual([mockNutritionPlans[1]]);
  });

  test('hiển thị thông báo lỗi khi không thể xóa (người dùng hủy)', async () => {
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));
    (window.confirm as jest.Mock).mockImplementationOnce(() => false); // User cancels deletion

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    fireEvent.click(screen.getAllByText('Xóa')[0]);

    await waitFor(() => {
      expect(screen.getByText('Chế độ ăn kiêng Keto')).toBeInTheDocument(); // Item should still be there
      expect(screen.queryByText('Xóa chế độ dinh dưỡng thành công!')).not.toBeInTheDocument();
    });
  });

  test('hiển thị thông báo thành công và biến mất sau 3 giây', async () => {
    jest.useFakeTimers();
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    fireEvent.click(screen.getAllByText('Xóa')[0]);

    await waitFor(() => {
      expect(screen.getByText('Xóa chế độ dinh dưỡng thành công!')).toBeInTheDocument();
    });

    jest.runAllTimers();

    await waitFor(() => {
      expect(screen.queryByText('Xóa chế độ dinh dưỡng thành công!')).not.toBeInTheDocument();
    });
    jest.useRealTimers();
  });

  test('điều hướng đến trang chỉnh sửa khi nhấp vào nút Sửa', () => {
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    const editButton = screen.getAllByRole('link', { name: /Sửa/i })[0];
    expect(editButton).toHaveAttribute('href', '/nutrition-plans/CDD001/edit');
  });

  test('điều hướng đến trang xem chi tiết khi nhấp vào nút Xem', () => {
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));
    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    const viewButton = screen.getAllByRole('link', { name: /Xem/i })[0];
    expect(viewButton).toHaveAttribute('href', '/nutrition-plans/CDD001');
  });

  test('hiển thị thông báo lỗi với kiểu "error" và biến mất sau 3 giây', async () => {
    jest.useFakeTimers();
    localStorage.setItem('nutritionPlans', JSON.stringify(mockNutritionPlans));

    // Directly simulate an error message being set
    const mockSetState = jest.fn();
    jest.spyOn(React, 'useState')
      .mockImplementationOnce(initialState => [
        initialState, // nutritionPlans state
        jest.fn(),
      ])
      .mockImplementationOnce(initialState => [
        { text: 'Lỗi đã xảy ra', type: 'error' }, // Initial message state
        mockSetState, // message state setter
      ]);

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );

    expect(screen.getByText('Lỗi đã xảy ra')).toBeInTheDocument();
    expect(screen.getByText('Lỗi đã xảy ra')).toHaveClass('bg-red-500');

    jest.runAllTimers();

    await waitFor(() => {
      expect(mockSetState).toHaveBeenCalledWith(null);
    });
    jest.useRealTimers();
  });

  test('hiển thị mô tả trống nếu không có', () => {
    const planWithoutDescription = [
      {
        maCheDo: 'CDD003',
        tenCheDo: 'Chế độ ăn đơn giản',
        moTa: '',
        ngayBatDau: '2023-01-01',
        ngayKetThuc: '',
        mucTieu: '',
        buaAn: [],
      },
    ];
    localStorage.setItem('nutritionPlans', JSON.stringify(planWithoutDescription));

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );
    expect(screen.getByText('Chế độ ăn đơn giản')).toBeInTheDocument();
    const moTaCell = screen.getByText('Chế độ ăn đơn giản').closest('tr')?.children[2];
    expect(moTaCell).toHaveTextContent('');
  });

  test('hiển thị mục tiêu trống nếu không có', () => {
    const planWithoutTarget = [
      {
        maCheDo: 'CDD004',
        tenCheDo: 'Chế độ ăn nhẹ nhàng',
        moTa: 'Ăn uống lành mạnh',
        ngayBatDau: '2023-01-01',
        ngayKetThuc: '2023-01-31',
        mucTieu: '',
        buaAn: [],
      },
    ];
    localStorage.setItem('nutritionPlans', JSON.stringify(planWithoutTarget));

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );
    expect(screen.getByText('Chế độ ăn nhẹ nhàng')).toBeInTheDocument();
    const mucTieuCell = screen.getByText('Chế độ ăn nhẹ nhàng').closest('tr')?.children[5];
    expect(mucTieuCell).toHaveTextContent('');
  });

  test('hiển thị ngày kết thúc trống nếu không có', () => {
    const planWithoutEndDate = [
      {
        maCheDo: 'CDD005',
        tenCheDo: 'Chế độ ăn không giới hạn',
        moTa: 'Ăn theo ý muốn',
        ngayBatDau: '2023-01-01',
        ngayKetThuc: '',
        mucTieu: 'Duy trì cân nặng',
        buaAn: [],
      },
    ];
    localStorage.setItem('nutritionPlans', JSON.stringify(planWithoutEndDate));

    render(
      <Router>
        <NutritionPlanList />
      </Router>
    );
    expect(screen.getByText('Chế độ ăn không giới hạn')).toBeInTheDocument();
    const ngayKetThucCell = screen.getByText('Chế độ ăn không giới hạn').closest('tr')?.children[4];
    expect(ngayKetThucCell).toHaveTextContent('');
  });

});
