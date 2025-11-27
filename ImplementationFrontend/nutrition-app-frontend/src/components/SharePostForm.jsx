// src/components/SharePostForm.jsx

import React, { useState } from 'react';
import shareService from '../services/shareService';
import { Share2, Link, FileText, X } from 'lucide-react';

function SharePostForm({ onPostCreated, onClose }) {
    const [content, setContent] = useState('');
    const [planId, setPlanId] = useState(''); // ID của kế hoạch muốn chia sẻ
    const [logId, setLogId] = useState('');   // ID của mục nhật ký muốn chia sẻ
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isLoading || !content.trim()) return;
        setIsLoading(true);
        setError(null);

        const postData = {
            content: content.trim(),
            // Chỉ gửi các ID nếu chúng có giá trị
            planId: planId || undefined,
            logId: logId || undefined,
            // Thêm imageUrl nếu có tính năng tải ảnh trực tiếp lên Cloudinary/S3
        };

        try {
            const newPost = await shareService.createPost(postData);
            // Gọi hàm callback để cập nhật feed
            onPostCreated(newPost);
            // Đóng form
            onClose();
        } catch (err) {
            setError('Lỗi khi đăng bài. Vui lòng thử lại.');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="p-6 bg-white rounded-xl shadow-xl border border-gray-200">
            <div className="flex justify-between items-center mb-4 border-b pb-2">
                <h2 className="text-xl font-bold text-green-700 flex items-center">
                    <Share2 className="w-6 h-6 mr-2" />
                    Chia sẻ Hoạt động/Kế hoạch
                </h2>
                <button
                    type="button"
                    onClick={onClose}
                    className="w-8 h-8 flex items-center justify-center text-gray-500 hover:text-gray-900 absolute top-4 right-4"
                    aria-label="Nút đóng" // <--- DÒNG ĐƯỢC THÊM VÀO
                >
                    <X className="w-5 h-5" />
                </button>
            </div>

            {/* KHẮC PHỤC LỖI: Thêm logic hiển thị lỗi với role="alert" */}
            {error && (
                <div
                    role="alert" // <--- DÒNG QUAN TRỌNG NHẤT CHO TEST PASS VÀ A11Y
                    className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
                >
                    <span className="block sm:inline">{error}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
                {error && <div className="p-2 text-sm text-red-700 bg-red-100 rounded-lg">{error}</div>}

                {/* Nội dung Bài đăng */}
                <div>
          <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              rows="4"
              required
              placeholder="Bạn muốn chia sẻ điều gì hôm nay? (Ví dụ: Đã hoàn thành mục tiêu 10 ngày giảm cân!)"
              className="w-full p-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
          ></textarea>
                </div>

                {/* Tùy chọn Liên kết Kế hoạch/Nhật ký */}
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label htmlFor="plan-id" className="flex items-center text-sm font-medium text-gray-700 mb-1">{/* Đảm bảo icon đã đúng */}
                            <Share2 className="w-4 h-4 mr-1 text-green-500" /> ID Kế hoạch (Tùy chọn)
                        </label>
                        <input
                            id="plan-id" // CẬP NHẬT: Thêm id="plan-id"
                            type="text"
                            value={planId}
                            onChange={(e) => setPlanId(e.target.value)}
                            placeholder="VD: P123"
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm"
                        />
                    </div>
                    <div>
                        <label htmlFor="log-id" className="flex items-center text-sm font-medium text-gray-700 mb-1">
                            <FileText className="w-4 h-4 mr-1 text-red-500" /> ID Nhật ký (Tùy chọn)
                        </label>
                        <input
                            id="log-id" // CẬP NHẬT: Thêm id="log-id"
                            type="text"
                            value={logId}
                            onChange={(e) => setLogId(e.target.value)}
                            placeholder="VD: L456"
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm"
                        />
                    </div>
                </div>

                {/* Nút Đăng bài */}
                <button
                    type="submit"
                    disabled={isLoading || !content.trim()}
                    className={`w-full py-2 px-4 text-white font-semibold rounded-lg shadow-md transition duration-150 ${
                        (isLoading || !content.trim()) ? 'bg-gray-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700'
                    }`}
                >
                    {isLoading ? 'Đang đăng...' : 'Đăng Bài'}
                </button>
            </form>
        </div>
    );
}

export default SharePostForm;