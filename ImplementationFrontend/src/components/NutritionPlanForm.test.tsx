import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter, MemoryRouter, Route, Routes } from 'react-router-dom';
import NutritionPlanForm from './NutritionPlanForm';

// Mock useNavigate and useParams
const mockedUsedNavigate = jest.fn();
const mockedUseParams = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate,
  useParams: () => mockedUseParams(),
}));

// Mock localStorage
const localStorageMock = (() => {
  let store: { [key: string]: string } = {};
  return {
    getItem: (key: string) => store[key] || null,
    setItem: (key: string, value: string) => {
      store[key] = value.toString();
    },
    clear: () => {
      store = {};
    },
    removeItem: (key: string) => {
      delete store[key];
    },
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

describe('NutritionPlanForm', () => {
  beforeEach(() => {
    localStorage.clear();
    mockedUsedNavigate.mockClear();
    mockedUseParams.mockClear();
    jest.clearAllTimers();
    jest.useRealTimers(); // Reset to real timers for showMessage setTimeout
  });

  const renderComponent = (id: string | undefined = undefined) => {
    mockedUseParams.mockReturnValue({ id });
    return render(
      <MemoryRouter initialEntries={id ? [`/nutrition-plans/edit/${id}`] : ['/nutrition-plans/create']}>
        <Routes>
          <Route path="/nutrition-plans/create" element={<NutritionPlanForm />} />
          <Route path="/nutrition-plans/edit/:id" element={<NutritionPlanForm />} />
          <Route path="/nutrition-plans" element={<div>Nutrition Plans List</div>} />
        </Routes>
      </MemoryRouter>
    );
  };

  it('should render "Tạo mới Chế độ Dinh dưỡng" title for new plan creation', () => {
    renderComponent();
    expect(screen.getByText('Tạo mới Chế độ Dinh dưỡng')).toBeInTheDocument();
  });

  it('should render "Chỉnh sửa Chế độ Dinh dưỡng" title for existing plan edit', () => {
    const existingPlan = {
      maCheDo: 'CDD123',
      tenCheDo: 'Kế hoạch giảm cân',
      moTa: 'Mô tả giảm cân',
      ngayBatDau: '2023-01-01',
      ngayKetThuc: '2023-01-31',
      mucTieu: 'Giảm 5kg',
    };
    localStorage.setItem('nutritionPlans', JSON.stringify([existingPlan]));
    renderComponent('CDD123');
    expect(screen.getByText('Chỉnh sửa Chế độ Dinh dưỡng')).toBeInTheDocument();
  });

  it('should update input values on change', () => {
    renderComponent();
    const tenCheDoInput = screen.getByLabelText('Tên Chế độ:') as HTMLInputElement;
    fireEvent.change(tenCheDoInput, { target: { id: 'tenCheDo', value: 'Kế hoạch test' } });
    expect(tenCheDoInput.value).toBe('Kế hoạch test');

    const moTaTextarea = screen.getByLabelText('Mô tả:') as HTMLTextAreaElement;
    fireEvent.change(moTaTextarea, { target: { id: 'moTa', value: 'Mô tả test' } });
    expect(moTaTextarea.value).toBe('Mô tả test');
  });

  it('should add a new nutrition plan on form submission', async () => {
    renderComponent();

    fireEvent.change(screen.getByLabelText('Tên Chế độ:'), { target: { id: 'tenCheDo', value: 'Kế hoạch mới' } });
    fireEvent.change(screen.getByLabelText('Mô tả:'), { target: { id: 'moTa', value: 'Mô tả kế hoạch mới' } });
    fireEvent.change(screen.getByLabelText('Ngày bắt đầu:'), { target: { id: 'ngayBatDau', value: '2024-01-01' } });
    fireEvent.change(screen.getByLabelText('Ngày kết thúc:'), { target: { id: 'ngayKetThuc', value: '2024-01-31' } });
    fireEvent.change(screen.getByLabelText('Mục tiêu:'), { target: { id: 'mucTieu', value: 'Tăng cân' } });

    fireEvent.click(screen.getByText('Tạo mới'));

    await waitFor(() => {
      const storedPlans = JSON.parse(localStorage.getItem('nutritionPlans') || '[]');
      expect(storedPlans.length).toBe(1);
      expect(storedPlans[0].tenCheDo).toBe('Kế hoạch mới');
      expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
      expect(screen.getByText('Thêm chế độ dinh dưỡng thành công!')).toBeInTheDocument();
    });
  });

  it('should update an existing nutrition plan on form submission', async () => {
    const existingPlan = {
      maCheDo: 'CDD123',
      tenCheDo: 'Kế hoạch cũ',
      moTa: 'Mô tả cũ',
      ngayBatDau: '2023-01-01',
      ngayKetThuc: '2023-01-31',
      mucTieu: 'Giảm 5kg',
    };
    localStorage.setItem('nutritionPlans', JSON.stringify([existingPlan]));

    renderComponent('CDD123');

    const tenCheDoInput = screen.getByLabelText('Tên Chế độ:') as HTMLInputElement;
    fireEvent.change(tenCheDoInput, { target: { id: 'tenCheDo', value: 'Kế hoạch đã chỉnh sửa' } });

    fireEvent.click(screen.getByText('Cập nhật'));

    await waitFor(() => {
      const storedPlans = JSON.parse(localStorage.getItem('nutritionPlans') || '[]');
      expect(storedPlans.length).toBe(1);
      expect(storedPlans[0].tenCheDo).toBe('Kế hoạch đã chỉnh sửa');
      expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
      expect(screen.getByText('Cập nhật chế độ dinh dưỡng thành công!')).toBeInTheDocument();
    });
  });

  it('should load existing plan data when in edit mode', () => {
    const existingPlan = {
      maCheDo: 'CDD456',
      tenCheDo: 'Kế hoạch tải',
      moTa: 'Mô tả tải',
      ngayBatDau: '2023-02-01',
      ngayKetThuc: '2023-02-28',
      mucTieu: 'Duy trì cân nặng',
    };
    localStorage.setItem('nutritionPlans', JSON.stringify([existingPlan]));
    renderComponent('CDD456');

    expect(screen.getByLabelText('Tên Chế độ:')).toHaveValue('Kế hoạch tải');
    expect(screen.getByLabelText('Mô tả:')).toHaveValue('Mô tả tải');
    expect(screen.getByLabelText('Ngày bắt đầu:')).toHaveValue('2023-02-01');
    expect(screen.getByLabelText('Ngày kết thúc:')).toHaveValue('2023-02-28');
    expect(screen.getByLabelText('Mục tiêu:')).toHaveValue('Duy trì cân nặng');
  });

  it('should navigate to /nutrition-plans if plan to edit is not found', async () => {
    localStorage.setItem('nutritionPlans', JSON.stringify([]));
    renderComponent('NON_EXISTENT_ID');

    await waitFor(() => {
      expect(screen.getByText('Không tìm thấy chế độ dinh dưỡng để chỉnh sửa.')).toBeInTheDocument();
      expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
    });
  });

  it('should navigate to /nutrition-plans if no stored plans exist for edit mode', async () => {
    localStorage.clear(); // Ensure localStorage is empty
    renderComponent('SOME_ID');

    await waitFor(() => {
      expect(screen.getByText('Không tìm thấy chế độ dinh dưỡng để chỉnh sửa.')).toBeInTheDocument();
      expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
    });
  });

  it('should navigate to /nutrition-plans when cancel button is clicked', () => {
    renderComponent();
    fireEvent.click(screen.getByText('Hủy'));
    expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
  });

  it('should display and hide success message', async () => {
    jest.useFakeTimers();
    renderComponent();

    fireEvent.change(screen.getByLabelText('Tên Chế độ:'), { target: { id: 'tenCheDo', value: 'Kế hoạch thông báo' } });
    fireEvent.change(screen.getByLabelText('Ngày bắt đầu:'), { target: { id: 'ngayBatDau', value: '2024-01-01' } });
    fireEvent.change(screen.getByLabelText('Mục tiêu:'), { target: { id: 'mucTieu', value: 'Thông báo test' } });

    fireEvent.click(screen.getByText('Tạo mới'));

    expect(screen.getByText('Thêm chế độ dinh dưỡng thành công!')).toBeInTheDocument();

    act(() => {
      jest.advanceTimersByTime(3000);
    });

    await waitFor(() => {
      expect(screen.queryByText('Thêm chế độ dinh dưỡng thành công!')).not.toBeInTheDocument();
    });
  });

  it('should display error message if plan to update is not found', async () => {
    localStorage.setItem('nutritionPlans', JSON.stringify([])); // Empty storage
    renderComponent('CDD_NON_EXISTENT_UPDATE'); // Try to update a non-existent ID

    fireEvent.change(screen.getByLabelText('Tên Chế độ:'), { target: { id: 'tenCheDo', value: 'Some Plan' } });
    fireEvent.change(screen.getByLabelText('Ngày bắt đầu:'), { target: { id: 'ngayBatDau', value: '2024-01-01' } });
    fireEvent.change(screen.getByLabelText('Mục tiêu:'), { target: { id: 'mucTieu', value: 'Some Goal' } });

    fireEvent.click(screen.getByText('Cập nhật')); // Assuming the button text will be "Cập nhật" if id is present

    await waitFor(() => {
      expect(screen.getByText('Không tìm thấy chế độ để cập nhật.')).toBeInTheDocument();
      expect(mockedUsedNavigate).toHaveBeenCalledWith('/nutrition-plans');
    });
  });
});
