import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import NutritionalConsultation from './NutritionalConsultation';
import * as React from 'react'; // Import React for mocking useState

// Mock localStorage
const localStorageMock = (function () {
  let store: { [key: string]: string } = {};
  return {
    getItem: jest.fn((key: string) => store[key] || null),
    setItem: jest.fn((key: string, value: string) => {
      store[key] = value.toString();
    }),
    removeItem: jest.fn((key: string) => {
      delete store[key];
    }),
    clear: jest.fn(() => {
      store = {};
    }),
  };
})();

Object.defineProperty(window, 'localStorage', { value: localStorageMock });

// Mock window.confirm
const mockConfirm = jest.fn();
Object.defineProperty(window, 'confirm', { value: mockConfirm });

// Mock window.location for navigation tests
const mockAssign = jest.fn();
Object.defineProperty(window, 'location', {
  value: {
    assign: mockAssign,
  },
  writable: true,
});

describe('NutritionalConsultation', () => {
  beforeEach(() => {
    localStorageMock.clear();
    localStorageMock.setItem.mockClear();
    localStorageMock.getItem.mockClear();
    mockConfirm.mockClear();
    mockAssign.mockClear();
    jest.useFakeTimers(); // Enable fake timers
  });

  afterEach(() => {
    act(() => {
      jest.runOnlyPendingTimers();
    });
    jest.useRealTimers(); // Restore real timers
  });

  test('renders the component and displays initial state', () => {
    render(<NutritionalConsultation />);
    expect(screen.getByText('Tư vấn Chế độ Dinh dưỡng')).toBeInTheDocument();
    expect(screen.getByLabelText('Chiều cao (cm):')).toHaveValue(null);
    expect(screen.getByLabelText('Cân nặng (kg):')).toHaveValue(null);
    expect(screen.getByLabelText('Tuổi:')).toHaveValue(null);
    expect(screen.getByLabelText('Giới tính:')).toHaveValue('');
    expect(screen.getByLabelText('Mức độ hoạt động:')).toHaveValue('');
    expect(screen.getByLabelText(/Mục tiêu dinh dưỡng/i)).toHaveValue('');
    expect(screen.queryByText('Nhu cầu Dinh dưỡng Khuyến nghị')).not.toBeInTheDocument();
    expect(screen.getByText('Chưa có kế hoạch dinh dưỡng nào được tạo.')).toBeInTheDocument();
  });

  test('handles input changes correctly', () => {
    render(<NutritionalConsultation />);

    fireEvent.change(screen.getByLabelText('Chiều cao (cm):'), { target: { value: '170' } });
    fireEvent.change(screen.getByLabelText('Cân nặng (kg):'), { target: { value: '65' } });
    fireEvent.change(screen.getByLabelText('Tuổi:'), { target: { value: '30' } });
    fireEvent.change(screen.getByLabelText('Giới tính:'), { target: { value: 'nam' } });
    fireEvent.change(screen.getByLabelText('Mức độ hoạt động:'), { target: { value: 'vua' } });
    fireEvent.change(screen.getByLabelText(/Mục tiêu dinh dưỡng/i), { target: { value: 'Giảm cân' } });

    expect(screen.getByLabelText('Chiều cao (cm):')).toHaveValue(170);
    expect(screen.getByLabelText('Cân nặng (kg):')).toHaveValue(65);
    expect(screen.getByLabelText('Tuổi:')).toHaveValue(30);
    expect(screen.getByLabelText('Giới tính:')).toHaveValue('nam');
    expect(screen.getByLabelText('Mức độ hoạt động:')).toHaveValue('vua');
    expect(screen.getByLabelText(/Mục tiêu dinh dưỡng/i)).toHaveValue('Giảm cân');
  });

  test('displays error alert if required fields are empty on submit (lines 84-85)', async () => {
    render(<NutritionalConsultation />);
    fireEvent.click(screen.getByRole('button', { name: 'Lưu Nhu cầu & Nhận Khuyến nghị' }));

    await waitFor(() => {
      expect(screen.getByText('Vui lòng điền đầy đủ các thông tin bắt buộc.')).toBeInTheDocument();
    });
    expect(screen.getByText('Vui lòng điền đầy đủ các thông tin bắt buộc.')).toHaveClass('bg-red-500');
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('calculates nutritional needs and displays recommendations on successful submission for male', async () => {
    render(<NutritionalConsultation />);
    fireEvent.change(screen.getByLabelText('Chiều cao (cm):'), { target: { value: '180' } });
    fireEvent.change(screen.getByLabelText('Cân nặng (kg):'), { target: { value: '80' } });
    fireEvent.change(screen.getByLabelText('Tuổi:'), { target: { value: '30' } });
    fireEvent.change(screen.getByLabelText('Giới tính:'), { target: { value: 'nam' } });
    fireEvent.change(screen.getByLabelText('Mức độ hoạt động:'), { target: { value: 'it' } });
    fireEvent.change(screen.getByLabelText(/Mục tiêu dinh dưỡng/i), { target: { value: 'Tăng cơ' } });

    fireEvent.click(screen.getByRole('button', { name: 'Lưu Nhu cầu & Nhận Khuyến nghị' }));

    await waitFor(() => {
      expect(localStorageMock.setItem).toHaveBeenCalledWith(
        'nutritionalNeeds_USER_ID_PLACEHOLDER',
        expect.stringContaining('"caloKhuyenNghi":2136')
      );
      expect(screen.getByText('Lưu nhu cầu dinh dưỡng và nhận khuyến nghị thành công!')).toBeInTheDocument();
      expect(screen.getByText('Lưu nhu cầu dinh dưỡng và nhận khuyến nghị thành công!')).toHaveClass('bg-green-500');

      const caloTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Calo khuyến nghị: 2136 kcal/ngày') ?? false;
      }, { selector: 'strong' });
      expect(caloTextElement).toBeInTheDocument();

      const proteinTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Protein: 133.5 g/ngày') ?? false;
      }, { selector: 'strong' });
      expect(proteinTextElement).toBeInTheDocument();

      const fatTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Chất béo: 71.2 g/ngày') ?? false;
      }, { selector: 'strong' });
      expect(fatTextElement).toBeInTheDocument();

      const carbTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Carbohydrate: 240.3 g/ngày') ?? false;
      }, { selector: 'strong' });
      expect(carbTextElement).toBeInTheDocument();
    });
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('calculates nutritional needs and displays recommendations on successful submission for female', async () => {
    render(<NutritionalConsultation />);
    fireEvent.change(screen.getByLabelText('Chiều cao (cm):'), { target: { value: '165' } });
    fireEvent.change(screen.getByLabelText('Cân nặng (kg):'), { target: { value: '60' } });
    fireEvent.change(screen.getByLabelText('Tuổi:'), { target: { value: '25' } });
    fireEvent.change(screen.getByLabelText('Giới tính:'), { target: { value: 'nu' } });
    fireEvent.change(screen.getByLabelText('Mức độ hoạt động:'), { target: { value: 'it' } });
    fireEvent.change(screen.getByLabelText(/Mục tiêu dinh dưỡng/i), { target: { value: 'Duy trì' } });

    fireEvent.click(screen.getByRole('button', { name: 'Lưu Nhu cầu & Nhận Khuyến nghị' }));

    await waitFor(() => {
      expect(localStorageMock.setItem).toHaveBeenCalledWith(
        'nutritionalNeeds_USER_ID_PLACEHOLDER',
        expect.stringContaining('"caloKhuyenNghi":1614')
      );
      expect(screen.getByText('Lưu nhu cầu dinh dưỡng và nhận khuyến nghị thành công!')).toBeInTheDocument();

      const caloTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Calo khuyến nghị: 1614 kcal/ngày') ?? false;
      }, { selector: 'strong' });
      expect(caloTextElement).toBeInTheDocument();
    });
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('loads saved nutritional needs and plans from localStorage on mount', async () => {
    const mockNutritionalNeeds = {
      maNguoiDung: 'USER_ID_PLACEHOLDER',
      chieuCao: 175,
      canNang: 70,
      tuoi: 28,
      gioiTinh: 'nam',
      mucDoHoatDong: 'vua',
      mucTieuDinhDuong: 'Giảm mỡ',
      caloKhuyenNghi: 2500,
      proteinKhuyenNghi: 150,
      chatBeoKhuyenNghi: 80,
      carbohydrateKhuyenNghi: 280,
    };
    const mockNutritionPlans = [
      {
        maCheDo: 'CDD123',
        tenCheDo: 'Kế hoạch giảm mỡ',
        moTa: 'Kế hoạch dinh dưỡng dựa trên nhu cầu khuyến nghị: 2500 Calo, 150g Protein, 80g Fat, 280g Carb.',
        ngayBatDau: '2025-11-23',
        ngayKetThuc: '',
        mucTieu: 'Giảm mỡ',
      },
    ];

    localStorageMock.setItem('nutritionalNeeds_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionalNeeds));
    localStorageMock.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionPlans));

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(localStorageMock.getItem).toHaveBeenCalledWith('nutritionalNeeds_USER_ID_PLACEHOLDER');
      expect(localStorageMock.getItem).toHaveBeenCalledWith('nutritionPlans_USER_ID_PLACEHOLDER');
      expect(screen.getByLabelText('Chiều cao (cm):')).toHaveValue(175);

      const caloTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Calo khuyến nghị: 2500 kcal/ngày') ?? false;
      }, { selector: 'strong' });
      expect(caloTextElement).toBeInTheDocument();

      expect(screen.getByText('Kế hoạch giảm mỡ')).toBeInTheDocument();
      expect(screen.getByText('CDD123')).toBeInTheDocument();
    });
  });

  test('creates a new nutrition plan from recommendation', async () => {
    const mockNutritionalNeeds = {
      maNguoiDung: 'USER_ID_PLACEHOLDER',
      chieuCao: 175,
      canNang: 70,
      tuoi: 28,
      gioiTinh: 'nam',
      mucDoHoatDong: 'vua',
      mucTieuDinhDuong: 'Tăng cơ',
      caloKhuyenNghi: 2500,
      proteinKhuyenNghi: 150,
      chatBeoKhuyenNghi: 80,
      carbohydrateKhuyenNghi: 280,
    };
    localStorageMock.setItem('nutritionalNeeds_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionalNeeds));

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByText('Nhu cầu Dinh dưỡng Khuyến nghị')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Tạo Kế hoạch từ Khuyến nghị' }));

    await waitFor(() => {
      expect(localStorageMock.setItem).toHaveBeenCalledWith(
        'nutritionPlans_USER_ID_PLACEHOLDER',
        expect.stringContaining('"tenCheDo":"Kế hoạch cho Tăng cơ"')
      );
      expect(screen.getByText('Đã tạo kế hoạch dinh dưỡng mới từ khuyến nghị!')).toBeInTheDocument();
      expect(screen.getByText('Kế hoạch cho Tăng cơ')).toBeInTheDocument();
    });
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('shows error if trying to create plan without recommendations (lines 116-117)', async () => {
    // Initialize localStorage with valid nutritional needs so the button renders
    const initialNutritionalNeeds = {
      maNguoiDung: 'USER_ID_PLACEHOLDER',
      chieuCao: 170,
      canNang: 65,
      tuoi: 30,
      gioiTinh: 'nam',
      mucDoHoatDong: 'vua',
      mucTieuDinhDuong: 'Giảm cân',
      caloKhuyenNghi: 2000, // Valid caloKhuyenNghi so the button renders initially
    };
    localStorageMock.setItem('nutritionalNeeds_USER_ID_PLACEHOLDER', JSON.stringify(initialNutritionalNeeds));

    const { rerender } = render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByRole('button', { name: 'Tạo Kế hoạch từ Khuyến nghị' })).toBeInTheDocument();
    });

    // Now, simulate caloKhuyenNghi being unavailable right before the click
    // This mimics a scenario where the state might have been cleared or not properly loaded.
    localStorageMock.setItem(
      'nutritionalNeeds_USER_ID_PLACEHOLDER',
      JSON.stringify({
        ...initialNutritionalNeeds,
        caloKhuyenNghi: undefined, // Make caloKhuyenNghi undefined
      })
    );
    
    // Re-render the component to pick up the localStorage change. This will not make the button disappear
    // immediately, but the internal state will be updated for handleCreatePlanFromRecommendation.
    rerender(<NutritionalConsultation />); 

    fireEvent.click(screen.getByRole('button', { name: 'Tạo Kế hoạch từ Khuyến nghị' }));

    await waitFor(() => {
      expect(screen.getByText('Vui lòng tính toán nhu cầu dinh dưỡng trước khi tạo kế hoạch.')).toBeInTheDocument();
    });
    expect(screen.getByText('Vui lòng tính toán nhu cầu dinh dưỡng trước khi tạo kế hoạch.')).toHaveClass('bg-red-500');
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });


  test('deletes a nutrition plan when confirmed', async () => {
    const mockNutritionPlans = [
      {
        maCheDo: 'CDD123',
        tenCheDo: 'Kế hoạch giảm mỡ',
        moTa: 'Mô tả',
        ngayBatDau: '2025-11-23',
        ngayKetThuc: '',
        mucTieu: 'Giảm mỡ',
      },
    ];
    localStorageMock.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionPlans));
    mockConfirm.mockReturnValue(true); // Simulate user confirming deletion

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByText('CDD123')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Xóa', hidden: true })); // Use hidden: true for buttons within tables

    await waitFor(() => {
      expect(mockConfirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn xóa kế hoạch dinh dưỡng này không?');
      expect(localStorageMock.setItem).toHaveBeenCalledWith('nutritionPlans_USER_ID_PLACEHOLDER', '[]');
      expect(screen.queryByText('CDD123')).not.toBeInTheDocument();
      expect(screen.getByText('Xóa kế hoạch dinh dưỡng thành công!')).toBeInTheDocument();
    });
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('does not delete a nutrition plan when cancelled', async () => {
    const mockNutritionPlans = [
      {
        maCheDo: 'CDD123',
        tenCheDo: 'Kế hoạch giảm mỡ',
        moTa: 'Mô tả',
        ngayBatDau: '2025-11-23',
        ngayKetThuc: '',
        mucTieu: 'Giảm mỡ',
      },
    ];
    localStorageMock.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionPlans));
    mockConfirm.mockReturnValue(false); // Simulate user cancelling deletion

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByText('CDD123')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Xóa', hidden: true }));

    await waitFor(() => {
      expect(mockConfirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn xóa kế hoạch dinh dưỡng này không?');
      expect(localStorageMock.setItem).not.toHaveBeenCalledWith('nutritionPlans_USER_ID_PLACEHOLDER', '[]');
      expect(screen.getByText('CDD123')).toBeInTheDocument(); // Plan should still be there
    });
    expect(screen.queryByText('Xóa kế hoạch dinh dưỡng thành công!')).not.toBeInTheDocument();
  });

  test('displays info alert for unimplemented edit plan function', async () => {
    const mockNutritionPlans = [
      {
        maCheDo: 'CDD123',
        tenCheDo: 'Kế hoạch giảm mỡ',
        moTa: 'Mô tả',
        ngayBatDau: '2025-11-23',
        ngayKetThuc: '',
        mucTieu: 'Giảm mỡ',
      },
    ];
    localStorageMock.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionPlans));

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByText('CDD123')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Sửa', hidden: true }));

    await waitFor(() => {
      expect(screen.getByText('Chức năng sửa chi tiết kế hoạch chưa được triển khai.')).toBeInTheDocument();
    });
    expect(screen.getByText('Chức năng sửa chi tiết kế hoạch chưa được triển khai.')).toHaveClass('bg-blue-500');
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('clicking "Xem chi tiết" navigates to the correct URL', async () => {
    const mockNutritionPlans = [
      {
        maCheDo: 'CDD123',
        tenCheDo: 'Kế hoạch giảm mỡ',
        moTa: 'Mô tả',
        ngayBatDau: '2025-11-23',
        ngayKetThuc: '',
        mucTieu: 'Giảm mỡ',
      },
    ];
    localStorageMock.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(mockNutritionPlans));

    render(<NutritionalConsultation />);

    await waitFor(() => {
      expect(screen.getByText('CDD123')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Xem chi tiết', hidden: true }));

    expect(mockAssign).toHaveBeenCalledWith('/nutrition-plan-detail/CDD123');
  });

  test('BMR calculation for unknown gender returns 0 (line 66)', async () => {
    render(<NutritionalConsultation />);
    fireEvent.change(screen.getByLabelText('Chiều cao (cm):'), { target: { value: '170' } });
    fireEvent.change(screen.getByLabelText('Cân nặng (kg):'), { target: { value: '60' } });
    fireEvent.change(screen.getByLabelText('Tuổi:'), { target: { value: '25' } });
    // Do not set gender to make it unknown
    fireEvent.change(screen.getByLabelText('Mức độ hoạt động:'), { target: { value: 'it' } });
    fireEvent.change(screen.getByLabelText(/Mục tiêu dinh dưỡng/i), { target: { value: 'Duy trì' } });

    fireEvent.click(screen.getByRole('button', { name: 'Lưu Nhu cầu & Nhận Khuyến nghị' }));

    await waitFor(() => {
      expect(localStorageMock.setItem).toHaveBeenCalledWith(
        'nutritionalNeeds_USER_ID_PLACEHOLDER',
        expect.stringContaining('"caloKhuyenNghi":0')
      );
      const caloTextElement = screen.getByText((content, element) => {
        const parent = element?.closest('p');
        return parent?.textContent?.includes('Calo khuyến nghị: 0 kcal/ngày') ?? false;
      }, { selector: 'strong' });
      expect(caloTextElement).toBeInTheDocument();
    });
    act(() => {
        jest.runAllTimers(); // Advance timers to clear the alert
    });
  });

  test('TDEE calculation for different activity levels', async () => {
    const testActivityLevel = async (activityLevel: string, expectedMultiplier: number, expectedCalo: number) => {
      const { unmount } = render(<NutritionalConsultation />);

      fireEvent.change(screen.getByLabelText('Chiều cao (cm):'), { target: { value: '170' } });
      fireEvent.change(screen.getByLabelText('Cân nặng (kg):'), { target: { value: '65' } });
      fireEvent.change(screen.getByLabelText('Tuổi:'), { target: { value: '30' } });
      fireEvent.change(screen.getByLabelText('Giới tính:'), { target: { value: 'nam' } });
      fireEvent.change(screen.getByLabelText('Mức độ hoạt động:'), { target: { value: activityLevel } });
      fireEvent.change(screen.getByLabelText(/Mục tiêu dinh dưỡng/i), { target: { value: 'Test' } });

      fireEvent.click(screen.getByRole('button', { name: 'Lưu Nhu cầu & Nhận Khuyến nghị' }));

      await waitFor(() => {
        const caloTextElement = screen.getByText((content, element) => {
          const parent = element?.closest('p');
          return parent?.textContent?.includes(`Calo khuyến nghị: ${expectedCalo} kcal/ngày`) ?? false;
        }, { selector: 'strong' });
        expect(caloTextElement).toBeInTheDocument();
      });
      act(() => {
        jest.runAllTimers();
      });
      unmount();
    };

    // BMR for male: (10 * 65) + (6.25 * 170) - (5 * 30) + 5 = 650 + 1062.5 - 150 + 5 = 1567.5
    // Sedentary (it): 1567.5 * 1.2 = 1881
    // Lightly active (nhe): 1567.5 * 1.375 = 2155.3125
    // Moderately active (vua): 1567.5 * 1.55 = 2429.625
    // Very active (nang): 1567.5 * 1.725 = 2703.9375
    // Extra active (rat_nang): 1567.5 * 1.9 = 2978.25

    await testActivityLevel('it', 1.2, 1881);
    await testActivityLevel('nhe', 1.375, 2155);
    await testActivityLevel('vua', 1.55, 2430);
    await testActivityLevel('nang', 1.725, 2704);
    await testActivityLevel('rat_nang', 1.9, 2978);
  });
});
