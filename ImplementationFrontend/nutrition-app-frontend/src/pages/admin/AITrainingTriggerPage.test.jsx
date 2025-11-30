
// Import necessary modules
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { vi } from 'vitest';
import AITrainingTriggerPage from "./AITrainingTriggerPage.jsx";
import adminService from "../../services/adminService.js";

// Mocking adminService
vi.mock('../../services/adminService.js');

describe('AITrainingTriggerPage', () => {

    beforeEach(() => {
        // Reset all mocks before each test vi.resetAllMocks();
        vi.restoreAllMocks();
    });

    afterEach(() => {
        // Reset all mocks after each test vi.resetAllMocks();
        vi.restoreAllMocks();
    });

    test('should display initial state', () => {
        render(<AITrainingTriggerPage />);
        expect(screen.getByText('Chờ lệnh kích hoạt từ Quản trị viên.')).toBeInTheDocument();
        expect(screen.getByText('Kích hoạt Huấn luyện lại AI NGAY BÂY GIỜ')).toBeInTheDocument();
        expect(screen.getByRole('button')).not.toBeDisabled();
    });

    test('should confirm before triggering retraining', () => {
        render(<AITrainingTriggerPage />);
        window.confirm = vi.fn(() => false);
        fireEvent.click(screen.getByRole('button'));
        expect(window.confirm).toBeCalledWith('Bạn có chắc chắn muốn kích hoạt Huấn luyện lại mô hình AI không? Quá trình này có thể tốn thời gian và tài nguyên máy chủ.');
    });

    test('should set loading state when retraining is initiated', async () => {
        window.confirm = vi.fn(() => true);
        adminService.triggerAIRetraining.mockResolvedValue({ status: 'ĐÃ BẮT ĐẦU', jobId: '123' });

        render(<AITrainingTriggerPage />);
        fireEvent.click(screen.getByRole('button'));

        expect(screen.getByText('Đang gửi yêu cầu kích hoạt...')).toBeInTheDocument();
        expect(screen.getByRole('button')).toBeDisabled();
    });

    // test('should set success state when retraining is successful', async () => {
    //     window.confirm = vi.fn(() => true);
    //     const mockResponse = { status: 'ĐÃ BẮT ĐẦU', jobId: '123' };
    //     adminService.triggerAIRetraining.mockResolvedValue(mockResponse);
    //
    //     render(<AITrainingTriggerPage />);
    //     fireEvent.click(screen.getByRole('button'));
    //
    //     await screen.findByText('Yêu cầu huấn luyện lại AI đã được kích hoạt thành công!');
    //     expect(screen.getByText('Trạng thái: ĐÃ BẮT ĐẦU')).toBeInTheDocument();
    //     expect(screen.getByText(`Job ID: ${mockResponse.jobId}`)).toBeInTheDocument();
    // });

    test('should set error state when retraining fails', async () => {
        window.confirm = vi.fn(() => true);
        const errorMessage = 'Error occurred';
        adminService.triggerAIRetraining.mockRejectedValue(new Error(errorMessage));

        render(<AITrainingTriggerPage />);
        fireEvent.click(screen.getByRole('button'));
        await screen.findByText('Kích hoạt thất bại.');
        expect(screen.getByText('Không thể kích hoạt quá trình huấn luyện lại. Vui lòng kiểm tra log Backend.')).toBeInTheDocument();
        expect(screen.getByText(`Chi tiết: ${errorMessage}`)).toBeInTheDocument();
    });

    // test('should display correct icon based on trainingStatus', async () => {
    //     // Check initial icon render(<AITrainingTriggerPage />);
    //     expect(screen.getByTestId('zap-icon')).toBeInTheDocument();
    //
    //     // Mock successful response
    //     window.confirm = vi.fn(() => true);
    //     adminService.triggerAIRetraining.mockResolvedValue({ status: 'ĐÃ BẮT ĐẦU', jobId: '123' });
    //     fireEvent.click(screen.getByRole('button'));
    //
    //     // Check loading icon
    //     await screen.findByTestId('loader-icon');
    //     expect(screen.getByTestId('loader-icon')).toBeInTheDocument();
    //
    //     // Check success icon
    //     await screen.findByTestId('check-circle-icon');
    //     expect(screen.getByTestId('check-circle-icon')).toBeInTheDocument();
    // });
});
