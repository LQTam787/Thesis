// src/pages/NutritionAdvicePage.jsx
import React, { useState, useRef, useEffect } from 'react';
import { useSelector } from 'react-redux';
import aiService from '../services/aiService';
import { Send, Camera, Image, X } from 'lucide-react'; // Sử dụng icon (cần cài đặt thư viện lucide-react)

// Ghi chú: Nếu chưa cài đặt, hãy chạy: npm install lucide-react

// Component đơn giản cho bong bóng chat (chat bubble)
const ChatBubble = ({ message, sender }) => (
    <div className={`flex ${sender === 'user' ? 'justify-end' : 'justify-start'} mb-4`}>
        <div
            className={`max-w-xs md:max-w-md lg:max-w-lg p-3 rounded-xl shadow-md ${
                sender === 'user'
                    ? 'bg-green-500 text-white rounded-br-none'
                    : 'bg-white text-gray-800 rounded-tl-none border border-gray-200'
            }`}
        >
            <p className="font-semibold capitalize mb-1">{sender === 'user' ? 'Bạn' : 'AI Dinh Dưỡng'}</p>
            <p className="text-sm">{message}</p>
        </div>
    </div>
);

function NutritionAdvicePage() {
    const [messages, setMessages] = useState([
        { sender: 'ai', message: 'Chào bạn! Tôi là AI tư vấn dinh dưỡng của bạn. Bạn muốn hỏi gì về kế hoạch ăn uống hoặc mục tiêu sức khỏe của mình?', type: 'text' }
    ]);
    const [inputMessage, setInputMessage] = useState('');
    const [imageFile, setImageFile] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    // Lấy userId để gửi kèm theo yêu cầu AI
    const userId = useSelector((state) => state.auth.user?.id || 'guest');

    const messagesEndRef = useRef(null);

    // Cuộn xuống cuối tin nhắn mới
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(scrollToBottom, [messages]);

    // Xử lý gửi tin nhắn văn bản
    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputMessage.trim() || isLoading) return;

        setIsLoading(true); // Di chuyển lên đầu
        const userMessage = inputMessage.trim();
        setMessages((prev) => [...prev, { sender: 'user', message: userMessage, type: 'text' }]);
        setInputMessage('');

        try {
            // Gọi AI Service (NLP)
            const aiResponse = await aiService.getNutritionAdvice(userMessage, userId);

            setMessages((prev) => [...prev, { sender: 'ai', message: aiResponse.text || 'Tôi đã nhận được tin nhắn của bạn.', type: 'text' }]);

        } catch (error) {
            setMessages((prev) => [...prev, { sender: 'ai', message: 'Xin lỗi, đã có lỗi xảy ra khi kết nối với AI.', type: 'error' }]);
        } finally {
            setIsLoading(false);
        }
    };

    // Xử lý gửi hình ảnh (Vision)
    const handleImageUpload = async () => {
        if (!imageFile || isLoading) return;

        setIsLoading(true); // Di chuyển lên đầu
        // Hiển thị tin nhắn của người dùng (chỉ là thông báo)
        setMessages((prev) => [...prev, { sender: 'user', message: `Đang phân tích hình ảnh: ${imageFile.name}`, type: 'image_upload' }]);
        setImageFile(null); // Xóa file đang chờ

        try {
            // Gọi AI Service (Vision)
            const aiResponse = await aiService.analyzeFoodImage(imageFile, userId);

            const analysisText = `Kết quả nhận dạng: ${aiResponse.recognizedFood || 'Không rõ món ăn'}. Ước tính Calories: ${aiResponse.calories || 'N/A'} kcal.`;

            setMessages((prev) => [...prev, { sender: 'ai', message: analysisText, type: 'image_result' }]);

        } catch (error) {
            setMessages((prev) => [...prev, { sender: 'ai', message: 'Xin lỗi, không thể phân tích hình ảnh này.', type: 'error' }]);
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <>
            <header className="p-4 bg-white shadow-md border-b">
                <h1 className="text-xl font-bold text-green-700">Trò chuyện với AI Dinh Dưỡng</h1>
            </header>

            {/* Khu vực Hiển thị Chat */}
            <div className="flex-grow p-4 overflow-y-auto space-y-4">
                {messages.map((msg, index) => (
                    <ChatBubble key={index} message={msg.message} sender={msg.sender} />
                ))}
                {isLoading && <ChatBubble sender="ai" message="AI đang xử lý..." />}
                <div ref={messagesEndRef} /> {/* Dùng để cuộn xuống */}
            </div>

            {/* Khu vực Nhập liệu */}
            <div className="p-4 bg-white border-t shadow-inner">

                {/* Xem trước Hình ảnh */}
                {imageFile && (
                    <div className="flex items-center justify-between p-3 mb-2 border border-gray-300 rounded-lg bg-gray-100">
                        <div className="flex items-center">
                            <Image className="w-5 h-5 mr-2 text-green-600" />
                            <span className="text-sm font-medium">{imageFile.name} - Sẵn sàng phân tích.</span>
                        </div>
                        <button data-testid="remove-image-button" onClick={() => setImageFile(null)} className="text-gray-500 hover:text-gray-800">
                            <X className="w-4 h-4" />
                        </button>
                    </div>
                )}

                <form onSubmit={handleSendMessage} className="flex space-x-3">

                    {/* Nút Tải ảnh */}
                    <label className="flex items-center justify-center w-12 h-12 text-gray-600 bg-gray-200 rounded-full cursor-pointer hover:bg-gray-300 transition duration-150">
                        <Camera className="w-6 h-6" />
                        <input
                            data-testid="file-input"
                            type="file"
                            accept="image/*"
                            className="hidden"
                            onChange={(e) => {
                                if (e.target.files && e.target.files[0]) {
                                    setImageFile(e.target.files[0]);
                                    setInputMessage(''); // Xóa tin nhắn nếu có ảnh
                                }
                            }}
                            disabled={isLoading}
                        />
                    </label>

                    {/* Input Chat */}
                    <input
                        type="text"
                        value={inputMessage}
                        onChange={(e) => {
                            setInputMessage(e.target.value);
                            setImageFile(null); // Xóa ảnh nếu bắt đầu gõ
                        }}
                        placeholder="Nhập câu hỏi hoặc mô tả bữa ăn của bạn..."
                        className="flex-grow px-4 py-3 border border-gray-300 rounded-full focus:ring-green-500 focus:border-green-500 disabled:bg-gray-100"
                        disabled={isLoading || !!imageFile}
                    />

                    {/* Nút Gửi (hoặc Phân tích Ảnh) */}
                    <button
                        type={imageFile ? 'button' : 'submit'} // Đổi type nếu có ảnh
                        onClick={imageFile ? handleImageUpload : handleSendMessage}
                        disabled={isLoading || (!inputMessage.trim() && !imageFile)}
                        className={`flex items-center justify-center w-12 h-12 rounded-full text-white transition duration-200 ${
                            (isLoading || (!inputMessage.trim() && !imageFile))
                                ? 'bg-gray-400 cursor-not-allowed'
                                : 'bg-green-600 hover:bg-green-700'
                        }`}
                    >
                        <Send className="w-6 h-6" />
                    </button>
                </form>
            </div>
        </>
    );
}

export default NutritionAdvicePage;