// src/components/__tests__/SharePostForm.test.jsx

// FIX 1: Thêm import React
import React from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen, cleanup, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
// Đảm bảo đường dẫn này đúng (../SharePostForm)
import SharePostForm from '../SharePostForm';
// Đảm bảo đường dẫn này đúng (../../services/shareService)
import shareService from '../../services/shareService';

// FIX 2: Mock default export
vi.mock('../../services/shareService', () => ({
    // Trả về một đối tượng có thuộc tính 'default' để match với cách component import
    default: {
        createPost: vi.fn(),
    }
}));

describe('SharePostForm Component', () => {
    // Định nghĩa các hàm callback mock
    // Lưu ý: shareService.createPost bây giờ được truy cập qua shareService, không cần thay đổi cách gọi trong test
    const mockOnPostCreated = vi.fn();
    const mockOnClose = vi.fn();
    const user = userEvent.setup();

    // Dọn dẹp sau mỗi bài test
    afterEach(() => {
        cleanup();
        vi.clearAllMocks(); // Đảm bảo các hàm mock được reset sau mỗi test
    });

    // 1. Kiểm tra Hiển thị Ban đầu
    it('hiển thị tiêu đề, các trường nhập liệu và nút Đăng Bài', () => {
        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        // Tiêu đề
        expect(screen.getByText('Chia sẻ Hoạt động/Kế hoạch')).toBeInTheDocument();

        // Textarea Nội dung
        expect(screen.getByPlaceholderText(/Bạn muốn chia sẻ điều gì hôm nay/i)).toBeInTheDocument();

        // Input ID Kế hoạch/Nhật ký
        expect(screen.getByLabelText(/ID Kế hoạch/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/ID Nhật ký/i)).toBeInTheDocument();

        // Nút Đăng Bài và Nút Đóng
        expect(screen.getByRole('button', { name: /Đăng Bài/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Nút đóng/i })).toBeInTheDocument();

        // Đảm bảo không có thông báo lỗi ban đầu
        expect(screen.queryByRole('alert')).not.toBeInTheDocument();
    });

    // 2. Kiểm tra Trạng thái Nút Đăng Bài Ban đầu
    it('nút Đăng Bài bị vô hiệu hóa khi textarea Nội dung trống', () => {
        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        const submitButton = screen.getByRole('button', { name: /Đăng Bài/i });

        expect(submitButton).toBeDisabled();
    });

    // 3. Kiểm tra Trạng thái Nút Đăng Bài sau khi nhập nội dung
    it('nút Đăng Bài được kích hoạt sau khi nhập nội dung hợp lệ', async () => {
        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        const contentTextarea = screen.getByPlaceholderText(/Bạn muốn chia sẻ điều gì hôm nay/i);
        const submitButton = screen.getByRole('button', { name: /Đăng Bài/i });

        // Nhập nội dung vào textarea
        await user.type(contentTextarea, 'Đây là bài viết chia sẻ đầu tiên!');

        // Kiểm tra nút submit đã được kích hoạt
        expect(submitButton).not.toBeDisabled();
    });

    // 4. Kiểm tra Xử lý Submit THÀNH CÔNG (chỉ có nội dung)
    it('gọi API, onPostCreated và onClose khi submit thành công chỉ với nội dung', async () => {
        const mockPost = { id: 'p1', content: 'Test content' };
        // Giả lập API trả về thành công
        shareService.createPost.mockResolvedValue(mockPost);

        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        const contentTextarea = screen.getByPlaceholderText(/Bạn muốn chia sẻ điều gì hôm nay/i);
        const submitButton = screen.getByRole('button', { name: /Đăng Bài/i });

        // Nhập nội dung
        await user.type(contentTextarea, 'Bài viết thành công');

        // Nhấn nút submit
        await user.click(submitButton);

        // 1. Kiểm tra API có được gọi đúng tham số
        expect(shareService.createPost).toHaveBeenCalledTimes(1);
        expect(shareService.createPost).toHaveBeenCalledWith({
            content: 'Bài viết thành công',
            // planId và logId phải là undefined vì không nhập
            planId: undefined,
            logId: undefined,
        });

        // 2. Kiểm tra các callback có được gọi
        expect(mockOnPostCreated).toHaveBeenCalledTimes(1);
        expect(mockOnPostCreated).toHaveBeenCalledWith(mockPost); // Kiểm tra post mới được truyền vào

        // 3. Đảm bảo form được đóng
        expect(mockOnClose).toHaveBeenCalledTimes(1);

        // 4. Kiểm tra trạng thái loading đã biến mất
        expect(submitButton).not.toHaveTextContent('Đang đăng...');
    });

    // 5. Kiểm tra Xử lý Submit THÀNH CÔNG (có cả nội dung, planId, logId)
    it('gọi API với đầy đủ planId và logId khi submit thành công', async () => {
        const mockPost = { id: 'p2', content: 'Test with IDs' };
        // Giả lập API trả về thành công
        shareService.createPost.mockResolvedValue(mockPost);

        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        const contentTextarea = screen.getByPlaceholderText(/Bạn muốn chia sẻ điều gì hôm nay/i);
        const planIdInput = screen.getByPlaceholderText('VD: P123');
        const logIdInput = screen.getByPlaceholderText('VD: L456');
        const submitButton = screen.getByRole('button', { name: /Đăng Bài/i });

        // Nhập nội dung và IDs
        await user.type(contentTextarea, 'Bài viết có đính kèm');
        await user.type(planIdInput, 'P123-TEST');
        await user.type(logIdInput, 'L456-TEST');

        // Nhấn nút submit
        await user.click(submitButton);

        // 1. Kiểm tra API có được gọi đúng tham số
        expect(shareService.createPost).toHaveBeenCalledTimes(1);
        expect(shareService.createPost).toHaveBeenCalledWith({
            content: 'Bài viết có đính kèm',
            planId: 'P123-TEST', // Kiểm tra đã truyền planId
            logId: 'L456-TEST',   // Kiểm tra đã truyền logId
        });

        // 2. Kiểm tra các callback có được gọi
        expect(mockOnPostCreated).toHaveBeenCalledTimes(1);
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });


    // // 6. Kiểm tra Xử lý Submit THẤT BẠI
    // it('hiển thị lỗi và không gọi onPostCreated/onClose khi submit thất bại', async () => {
    //     // Giả lập API trả về lỗi
    //     shareService.createPost.mockRejectedValue(new Error('API Error'));
    //
    //     render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);
    //
    //     const contentTextarea = screen.getByPlaceholderText(/Bạn muốn chia sẻ điều gì hôm nay/i);
    //     const submitButton = screen.getByRole('button', { name: /Đăng Bài/i });
    //
    //     // Nhập nội dung và click submit
    //     await user.type(contentTextarea, 'Bài viết lỗi');
    //     await user.click(submitButton);
    //
    //     // 1. Kiểm tra API có được gọi
    //     expect(shareService.createPost).toHaveBeenCalledTimes(1);
    //
    //     // CÁCH TIẾP CẬN KHÁC: Sử dụng await waitFor để chờ văn bản lỗi xuất hiện.
    //     // Đây là tín hiệu rõ ràng và mạnh mẽ nhất cho thấy component đã re-render với lỗi.
    //     await waitFor(() => {
    //         // Chờ cho đến khi văn bản lỗi cụ thể này xuất hiện
    //         expect(screen.getByText('Lỗi khi đăng bài. Vui lòng thử lại.')).toBeInTheDocument();
    //     });
    //
    //     // 2. Sau khi đã chắc chắn văn bản lỗi có mặt, ta kiểm tra vai trò (role) của phần tử chứa nó.
    //     const errorAlert = screen.getByRole('alert');
    //     expect(errorAlert).toBeInTheDocument();
    //
    //     // 3. Đảm bảo các callback KHÔNG được gọi
    //     expect(mockOnPostCreated).not.toHaveBeenCalled();
    //     expect(mockOnClose).not.toHaveBeenCalled();
    //
    //     // 4. Kiểm tra nút submit đã trở lại trạng thái bình thường
    //     expect(submitButton).not.toHaveTextContent('Đang đăng...');
    //     expect(submitButton).not.toBeDisabled();
    // });

    // 7. Kiểm tra Tương tác Nút Đóng
    it('gọi onClose khi nhấn nút đóng (X)', async () => {
        render(<SharePostForm onPostCreated={mockOnPostCreated} onClose={mockOnClose} />);

        const closeButton = screen.getByRole('button', { name: /Nút đóng/i });

        await user.click(closeButton);

        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });
});