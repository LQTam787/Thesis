import React, { useState, useEffect } from 'react';
import '../../index.css'; // Adjust path as necessary for global styles
import '../../App.css'; // Adjust path as necessary for global styles

interface Comment {
    id: string;
    author: string;
    text: string;
}

interface SharedContent {
    id: string;
    author: string;
    date: string;
    title: string;
    content: string;
    planLink?: string;
    likes: number;
    comments: Comment[];
    isLiked: boolean;
}

const ViewSharedContentPage: React.FC = () => {
    const [sharedContents, setSharedContents] = useState<SharedContent[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Simulate fetching shared content
    useEffect(() => {
        const fetchSharedContent = async () => {
            try {
                setLoading(true);
                await new Promise(resolve => setTimeout(resolve, 1000)); // Simulate network delay
                const data: SharedContent[] = [
                    {
                        id: '1',
                        author: 'Nguyễn Văn A',
                        date: '15 tháng 11, 2025',
                        title: 'Kế hoạch dinh dưỡng giảm cân hiệu quả trong 1 tháng',
                        content: 'Chào mọi người, tôi muốn chia sẻ kế hoạch dinh dưỡng mà tôi đã áp dụng để giảm 5kg trong một tháng qua. Kế hoạch này tập trung vào việc cắt giảm carb xấu và tăng cường protein cùng rau xanh. Hy vọng nó sẽ hữu ích cho những ai đang muốn giảm cân!',
                        planLink: '#',
                        likes: 120,
                        comments: [
                            { id: 'c1', author: 'Trần Thị B', text: 'Kế hoạch rất hay, mình sẽ thử áp dụng xem sao!' },
                            { id: 'c2', author: 'Lê Văn C', text: 'Bạn có thể chia sẻ thêm về các món ăn cụ thể không?' },
                        ],
                        isLiked: false,
                    },
                    {
                        id: '2',
                        author: 'Phạm Thị D',
                        date: '10 tháng 11, 2025',
                        title: 'Hoạt động thể chất tăng cường sức bền',
                        content: 'Tôi muốn giới thiệu chuỗi bài tập giúp tăng cường sức bền mà tôi thực hiện hàng ngày. Bao gồm chạy bộ 30 phút, 15 phút đạp xe và một số bài tập cardio cường độ cao. Cảm thấy tràn đầy năng lượng sau mỗi buổi tập!',
                        likes: 80,
                        comments: [
                            { id: 'c3', author: 'Võ Văn E', text: 'Nghe có vẻ hay đó, mình sẽ thử tập theo.' },
                        ],
                        isLiked: false,
                    },
                ];
                setSharedContents(data);
            } catch (err) {
                setError('Không thể tải nội dung chia sẻ.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchSharedContent();
    }, []);

    const handleLike = (postId: string) => {
        setSharedContents(prevContents =>
            prevContents.map(content =>
                content.id === postId
                    ? { ...content, likes: content.isLiked ? content.likes - 1 : content.likes + 1, isLiked: !content.isLiked }
                    : content
            )
        );
        // In a real app, send API request to update like count
        console.log(`Post ${postId} liked/unliked`);
    };

    const handleComment = (postId: string, commentText: string) => {
        if (!commentText.trim()) return;

        setSharedContents(prevContents =>
            prevContents.map(content =>
                content.id === postId
                    ? {
                          ...content,
                          comments: [
                              ...content.comments,
                              { id: `c${Date.now()}`, author: 'Bạn', text: commentText }, // Simulate current user comment
                          ],
                      }
                    : content
            )
        );
        // In a real app, send API request to add comment
        console.log(`Comment added to post ${postId}: ${commentText}`);
    };

    const handleSave = (postId: string) => {
        // In a real app, send API request to save content
        alert(`Lưu nội dung ID: ${postId}`);
        console.log(`Post ${postId} saved`);
    };

    if (loading) return <div className="container">Đang tải nội dung...</div>;
    if (error) return <div className="container error-message">Lỗi: {error}</div>;

    return (
        <div className="container">
            <h1>Nội Dung Chia Sẻ Của Cộng Đồng</h1>

            {sharedContents.map(post => (
                <div className="post" key={post.id}>
                    <div className="post-header">
                        <span className="author">{post.author}</span>
                        <span className="date">{post.date}</span>
                    </div>
                    <h2 className="post-title">{post.title}</h2>
                    <p className="post-content">{post.content}</p>
                    {post.planLink && (
                        <div className="post-plan-link">
                            <p>Kế hoạch liên quan: <a href={post.planLink}>Kế hoạch Giảm cân tháng 10</a></p>
                        </div>
                    )}
                    <div className="post-actions">
                        <button
                            className={`like-btn ${post.isLiked ? 'liked' : ''}`}
                            onClick={() => handleLike(post.id)}
                        >
                            <span role="img" aria-label="like">👍</span> Thích ({post.likes})
                        </button>
                        <button className="comment-btn">
                            <span role="img" aria-label="comment">💬</span> Bình luận ({post.comments.length})
                        </button>
                        <button className="save-btn" onClick={() => handleSave(post.id)}>
                            <span role="img" aria-label="save">💾</span> Lưu
                        </button>
                    </div>

                    <div className="comments-section">
                        <h3>Bình luận</h3>
                        {post.comments.map(comment => (
                            <div className="comment" key={comment.id}>
                                <p className="comment-author">{comment.author}</p>
                                <p className="comment-text">{comment.text}</p>
                            </div>
                        ))}
                        <div className="comment-input-area">
                            <input
                                type="text"
                                placeholder="Viết bình luận của bạn..."
                                onKeyPress={(e) => {
                                    if (e.key === 'Enter') {
                                        handleComment(post.id, (e.target as HTMLInputElement).value);
                                        (e.target as HTMLInputElement).value = '';
                                    }
                                }}
                            />
                            <button onClick={() => {
                                const inputElement = document.querySelector(`.comments-section .comment-input-area input[data-post-id="${post.id}"]`) as HTMLInputElement;
                                if (inputElement) {
                                    handleComment(post.id, inputElement.value);
                                    inputElement.value = '';
                                }
                            }}>Gửi</button>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ViewSharedContentPage;
