// src/pages/__tests__/CommunityFeedPage.test.jsx
import React, { useState } from 'react';
import { render, screen, waitFor, within } from '@testing-library/react'; // Import 'within'
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import CommunityFeedPage from '../CommunityFeedPage';
import authReducer from '../../store/authSlice'; // Giả định authSlice nằm ở đường dẫn này
import shareService from '../../services/shareService';

// Mock shareService để tránh các cuộc gọi mạng thực tế
vi.mock('../../services/shareService', () => ({
    __esModule: true,
    default: {
        getCommunityFeed: vi.fn(),
        toggleLike: vi.fn(),
        createPost: vi.fn(),
    },
}));

// Mock component SharePostForm để đơn giản hóa việc kiểm thử
vi.mock('../../components/SharePostForm', () => ({
    __esModule: true,
    default: ({ onPostCreated, onClose }) => {
        const [content, setContent] = useState('');
        return (
            <div data-testid="share-post-form">
                <h2>Tạo Bài Đăng Mới</h2>
                <textarea
                    placeholder="Bạn muốn chia sẻ điều gì?"
                    data-testid="post-content-textarea"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                />
                <button
                    onClick={() => {
                        onPostCreated({
                            id: `p_${Date.now()}`, // Tạo ID duy nhất
                            author: { id: 'user1', username: 'TestUser1' },
                            content: content, // Sử dụng nội dung động
                            planId: null,
                            logId: null,
                            likes: [],
                            comments: [],
                            createdAt: new Date().toISOString(),
                        });
                        onClose(); // Đóng form sau khi đăng bài
                    }}
                >
                    Đăng Bài
                </button>
                <button onClick={onClose}>Hủy</button>
            </div>
        );
    },
}));

const mockPosts = [
    {
        id: 'p1',
        author: { id: 'user1', username: 'TestUser1' },
        content: 'This is the first test post!',
        planId: null,
        logId: 'log123',
        likes: ['user1'], // Đã thích bởi người dùng hiện tại
        comments: [],
        createdAt: new Date().toISOString(),
    },
    {
        id: 'p2',
        author: { id: 'user2', username: 'TestUser2' },
        content: 'Another post from the community.',
        planId: 'plan456',
        logId: null,
        likes: [], // Chưa thích bởi người dùng hiện tại
        comments: [{ id: 'c1', content: 'Great post!', author: { id: 'user1', username: 'TestUser1' } }],
        createdAt: new Date(Date.now() - 3600000).toISOString(),
    },
];

// Một mock đơn giản cho authReducer
const mockAuthReducer = (state = { user: { id: 'user1', username: 'TestUser1', token: 'mock-token' }, isAuthenticated: true, loading: false, error: null }, action) => {
    switch (action.type) {
        default:
            return state;
    }
};

const mockStore = configureStore({
    reducer: {
        auth: mockAuthReducer,
    },
});

describe('CommunityFeedPage', () => {
    beforeEach(() => {
        // Đặt lại các mock trước mỗi bài kiểm thử
        shareService.getCommunityFeed.mockReset();
        shareService.toggleLike.mockReset();
        shareService.createPost.mockReset();

        // Mock mặc định cho việc tải feed thành công
        shareService.getCommunityFeed.mockResolvedValue(mockPosts);
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    const renderComponent = (store = mockStore) => {
        return render(
            <Provider store={store}>
                <CommunityFeedPage />
            </Provider>
        );
    };

    test('nên hiển thị trạng thái tải ban đầu và sau đó là các bài đăng', async () => {
        renderComponent();

        expect(screen.getByText('Đang tải bảng tin...')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.queryByText('Đang tải bảng tin...')).not.toBeInTheDocument();
        });

        expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        expect(screen.getByText('Another post from the community.')).toBeInTheDocument();
        expect(screen.getByText('TestUser1')).toBeInTheDocument();
        expect(screen.getByText('TestUser2')).toBeInTheDocument();
    });

    test('nên hiển thị trạng thái rỗng nếu không có bài đăng nào được trả về', async () => {
        shareService.getCommunityFeed.mockResolvedValue([]);
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('Chưa có bài đăng nào. Hãy là người đầu tiên chia sẻ!')).toBeInTheDocument();
        });
    });

    test('nên hiển thị thông báo lỗi nếu việc tải bài đăng thất bại và hiển thị dữ liệu dự phòng', async () => {
        shareService.getCommunityFeed.mockRejectedValue(new Error('Failed to fetch'));
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText(/Không thể tải bảng tin cộng đồng/i)).toBeInTheDocument();
            // Kiểm tra dữ liệu dự phòng
            expect(screen.getByText(/Bài đăng giả định: Hôm nay tôi đã đạt được mục tiêu calo hàng ngày!/i)).toBeInTheDocument();
            expect(screen.getByText(/Lời khuyên: Uống đủ nước là chìa khóa để giảm cân./i)).toBeInTheDocument();
        });
    });

    test('nên mở và đóng SharePostForm khi nút "Đăng Bài" được nhấp', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        const postButton = screen.getByRole('button', { name: /Đăng Bài/i });
        await user.click(postButton);

        expect(screen.getByTestId('share-post-form')).toBeInTheDocument();
        expect(screen.getByText('Tạo Bài Đăng Mới')).toBeInTheDocument();

        const closeButton = screen.getByRole('button', { name: /Hủy/i });
        await user.click(closeButton);

        await waitFor(() => {
            expect(screen.queryByTestId('share-post-form')).not.toBeInTheDocument();
        });
    });

    test('nên cho phép người dùng thích và bỏ thích một bài đăng', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        // Bài đăng 1 đã được 'user1' thích ban đầu trong mockPosts
        const post1LikeButton = screen.getByRole('button', { name: /1 Thích/i });
        expect(post1LikeButton).toHaveTextContent('1 Thích');
        expect(post1LikeButton.querySelector('svg')).toHaveClass('text-red-500'); // Trái tim phải màu đỏ

        // Bài đăng 2 chưa được 'user1' thích ban đầu
        // Tìm nút thích của bài đăng 2 bằng cách tìm nút "1 Bình luận" và sau đó tìm nút thích trong cùng một div cha
        const post2CommentsButton = screen.getByRole('button', { name: /1 Bình luận/i }); // Corrected to find post2's comments button
        const post2Card = post2CommentsButton.closest('.flex.items-center.space-x-6');
        const post2LikeButton = post2Card.querySelector('button'); // Nút thích là nút đầu tiên trong div này
        
        expect(post2LikeButton).toHaveTextContent('0 Thích');
        expect(post2LikeButton.querySelector('svg')).not.toHaveClass('text-red-500'); // Trái tim không nên màu đỏ

        // Kiểm thử bỏ thích bài đăng 1
        shareService.toggleLike.mockResolvedValueOnce({}); // Mock toggle thành công
        await user.click(post1LikeButton);

        await waitFor(() => {
            expect(post1LikeButton).toHaveTextContent('0 Thích');
            expect(post1LikeButton.querySelector('svg')).not.toHaveClass('text-red-500');
        });
        expect(shareService.toggleLike).toHaveBeenCalledWith('p1');

        // Kiểm thử thích bài đăng 2
        shareService.toggleLike.mockResolvedValueOnce({}); // Mock toggle thành công
        await user.click(post2LikeButton);

        await waitFor(() => {
            expect(post2LikeButton).toHaveTextContent('1 Thích');
            expect(post2LikeButton.querySelector('svg')).toHaveClass('text-red-500');
        });
        expect(shareService.toggleLike).toHaveBeenCalledWith('p2');
    });

    test('nên thêm một bài đăng mới vào feed sau khi tạo thành công từ SharePostForm', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        const postButton = screen.getByRole('button', { name: /Đăng Bài/i });
        await user.click(postButton);

        const newPostContent = 'This is a brand new post from the test!';
        const textarea = screen.getByTestId('post-content-textarea');
        // Use within to target the button inside the form
        const submitButton = within(screen.getByTestId('share-post-form')).getByRole('button', { name: /Đăng Bài/i, exact: true });

        await user.type(textarea, newPostContent);
        await user.click(submitButton);

        await waitFor(() => {
            expect(screen.queryByTestId('share-post-form')).not.toBeInTheDocument(); // Form nên đóng
        });

        // Xác minh bài đăng mới nằm ở đầu feed
        const posts = screen.getAllByText(new RegExp(newPostContent, 'i'));
        expect(posts[0]).toHaveTextContent(newPostContent);
    });

    test('nên hiển thị các ID kế hoạch/nhật ký đính kèm', async () => {
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        // Bài đăng 1 có logId
        expect(screen.getByText('Nhật ký log123')).toBeInTheDocument();
        // Bài đăng 2 có planId
        expect(screen.getByText('Kế hoạch plan456')).toBeInTheDocument();
    });

    test('nên hiển thị số lượng bình luận', async () => {
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        // Bài đăng 1 có 0 bình luận
        expect(screen.getByRole('button', { name: /0 Bình luận/i })).toBeInTheDocument();
        // Bài đăng 2 có 1 bình luận
        expect(screen.getByRole('button', { name: /1 Bình luận/i })).toBeInTheDocument();
    });

    test('nên xử lý lỗi toggleLike một cách duyên dáng', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('This is the first test post!')).toBeInTheDocument();
        });

        const post1LikeButton = screen.getByRole('button', { name: /1 Thích/i });
        shareService.toggleLike.mockRejectedValueOnce(new Error('Failed to like'));

        // Nhấp để bỏ thích
        await user.click(post1LikeButton);

        // UI lý tưởng là không nên thay đổi hoặc nên hoàn nguyên nếu cuộc gọi API thất bại.
        // Đối với bài kiểm thử này, chúng ta chỉ đảm bảo lỗi được ghi lại (được thực hiện bởi component)
        // và trạng thái UI vẫn như trước khi thử thất bại.
        await waitFor(() => {
            expect(post1LikeButton).toHaveTextContent('1 Thích'); // Nên vẫn được thích
            expect(post1LikeButton.querySelector('svg')).toHaveClass('text-red-500');
        });
        expect(shareService.toggleLike).toHaveBeenCalledWith('p1');
    });
});
