// src/pages/CommunityFeedPage.jsx
import React, { useState, useEffect } from 'react';
import shareService from '../services/shareService';
import SharePostForm from '../components/SharePostForm';
import { Heart, MessageCircle, Share2, PlusCircle, User, Zap } from 'lucide-react';
import { useSelector } from 'react-redux';

// Component hiển thị Bài đăng
const PostCard = ({ post, onLikeToggle }) => {
    // Lấy userId hiện tại để kiểm tra đã like hay chưa
    const currentUserId = useSelector((state) => state.auth.user?.id);
    const isLiked = post.likes.includes(currentUserId);

    // Xử lý click Like
    const handleLike = async () => {
        try {
            await shareService.toggleLike(post.id);
            // Gọi hàm callback để cập nhật UI
            onLikeToggle(post.id, currentUserId);
        } catch (error) {
            console.error('Lỗi Like/Unlike:', error);
        }
    };

    return (
        <div className="bg-white p-5 rounded-xl shadow-lg border border-gray-100 mb-6">

            {/* Header Bài đăng */}
            <div className="flex items-center mb-4 border-b pb-3">
                <User className="w-8 h-8 p-1 bg-green-100 text-green-600 rounded-full mr-3" />
                <div>
                    <p className="font-bold text-gray-800">{post.author.username}</p>
                    <p className="text-xs text-gray-500">{new Date(post.createdAt).toLocaleString()}</p>
                </div>
            </div>

            {/* Nội dung chính */}
            <p className="text-gray-700 mb-4 whitespace-pre-wrap">{post.content}</p>

            {/* Hiển thị Nội dung đính kèm (Kế hoạch/Nhật ký) */}
            {(post.planId || post.logId) && (
                <div className="p-3 mb-4 bg-gray-50 border border-gray-200 rounded-lg flex items-center text-sm text-gray-600">
                    <Zap className="w-4 h-4 mr-2 text-yellow-500" />
                    Đính kèm:
                    {post.planId && <span className="ml-2 px-2 py-0.5 bg-blue-100 text-blue-800 rounded">Kế hoạch {post.planId}</span>}
                    {post.logId && <span className="ml-2 px-2 py-0.5 bg-red-100 text-red-800 rounded">Nhật ký {post.logId}</span>}
                </div>
            )}

            {/* Thao tác (Likes/Comments) */}
            <div className="flex items-center space-x-6 border-t pt-3">
                <button
                    onClick={handleLike}
                    className="flex items-center text-sm font-semibold transition duration-150"
                >
                    <Heart className={`w-5 h-5 mr-1 ${isLiked ? 'text-red-500 fill-red-500' : 'text-gray-400 hover:text-red-500'}`} />
                    <span className={isLiked ? 'text-red-500' : 'text-gray-600'}>{post.likes.length} Thích</span>
                </button>

                {/* Tạm thời chỉ hiển thị số lượng */}
                <button className="flex items-center text-sm font-semibold text-gray-600 hover:text-blue-500 transition duration-150">
                    <MessageCircle className="w-5 h-5 mr-1" />
                    {post.comments.length} Bình luận
                </button>
            </div>
        </div>
    );
};


function CommunityFeedPage() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [error, setError] = useState(null);
    const currentUserId = useSelector((state) => state.auth.user?.id);

    // --- 1. Tải Feed ---
    const fetchFeed = async () => {
        try {
            setLoading(true);
            const data = await shareService.getCommunityFeed();
            setPosts(data);
        } catch (err) {
            setError('Không thể tải bảng tin cộng đồng.');
            // Giả định dữ liệu nếu lỗi
            setPosts([
                { id: 'p1', author: { username: 'AI_User_1' }, content: "Bài đăng giả định: Hôm nay tôi đã đạt được mục tiêu calo hàng ngày!", planId: 'PLN456', logId: null, likes: ['guest'], comments: [], createdAt: new Date() },
                { id: 'p2', author: { username: 'NutritionGuru' }, content: "Lời khuyên: Uống đủ nước là chìa khóa để giảm cân.", planId: null, logId: null, likes: [], comments: [{id: 'c1', content: 'Cảm ơn!', author: 'user_a'}], createdAt: new Date(Date.now() - 3600000) }
            ]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchFeed();
    }, []);

    // --- 2. Xử lý cập nhật Like (Tối ưu hóa UI) ---
    const handleLikeToggle = (postId, userId) => {
        setPosts(prevPosts =>
            prevPosts.map(post => {
                if (post.id === postId) {
                    // Tạo bản sao của mảng likes
                    const newLikes = [...post.likes];
                    const userIndex = newLikes.indexOf(userId);

                    if (userIndex > -1) {
                        // Đã thích -> Bỏ thích
                        newLikes.splice(userIndex, 1);
                    } else {
                        // Chưa thích -> Thích
                        newLikes.push(userId);
                    }
                    return { ...post, likes: newLikes };
                }
                return post;
            })
        );
    };

    // --- 3. Xử lý bài đăng mới ---
    const handlePostCreated = (newPost) => {
        // Thêm bài đăng mới lên đầu feed
        setPosts(prevPosts => [newPost, ...prevPosts]);
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <header className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-800 flex items-center">
                    <Share2 className="w-7 h-7 mr-3 text-green-600" />
                    Bảng tin Cộng đồng
                </h1>
                <button
                    onClick={() => setIsFormOpen(true)}
                    className="flex items-center px-4 py-2 text-sm font-semibold text-white bg-green-600 rounded-lg shadow-md hover:bg-green-700 transition duration-150"
                >
                    <PlusCircle className="w-5 h-5 mr-2" />
                    Đăng Bài
                </button>
            </header>

            {/* Modal/Form Đăng bài */}
            {isFormOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
                    <div className="relative w-full max-w-lg">
                        <SharePostForm onPostCreated={handlePostCreated} onClose={() => setIsFormOpen(false)} />
                    </div>
                </div>
            )}

            {/* Hiển thị Feed */}
            {loading ? (
                <div className="p-8 text-center text-lg">Đang tải bảng tin...</div>
            ) : error ? (
                <div className="p-8 text-center text-red-600">{error} (Đang hiển thị dữ liệu giả định)</div>
            ) : (
                <div className="max-w-3xl mx-auto">
                    {posts.length > 0 ? (
                        posts.map((post) => (
                            <PostCard key={post.id} post={post} onLikeToggle={handleLikeToggle} />
                        ))
                    ) : (
                        <div className="text-center p-10 border-2 border-dashed border-gray-300 rounded-xl bg-white">
                            <p className="text-lg text-gray-600">Chưa có bài đăng nào. Hãy là người đầu tiên chia sẻ!</p>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default CommunityFeedPage;