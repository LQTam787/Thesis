// src/pages/DailyLogInputPage.jsx
import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import logService from '../services/logService';
import aiService from '../services/aiService'; // Tái sử dụng AI Service
import { PlusCircle, Utensils, Zap, Image, X, Soup } from 'lucide-react';

function DailyLogInputPage() {
    const [logData, setLogData] = useState({
        foodName: '',
        calories: '',
        protein: '',
        carb: '',
        fat: '',
        mealType: 'BREAKFAST', // Bữa sáng, Trưa, Tối, Snack
        date: new Date().toISOString().slice(0, 10), // Ngày hiện tại YYYY-MM-DD
    });
    const [imageFile, setImageFile] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState(null);

    const userId = useSelector((state) => state.auth.user?.id);
    const MEAL_TYPES = ['BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'];

    // --- Xử lý Nhập liệu Bằng tay ---
    const handleManualSubmit = async (e) => {
        e.preventDefault();
        if (isLoading) return;
        setIsLoading(true);
        setMessage(null);

        const dataToSend = {
            ...logData,
            calories: parseFloat(logData.calories) || 0,
            protein: parseFloat(logData.protein) || 0,
            carb: parseFloat(logData.carb) || 0,
            fat: parseFloat(logData.fat) || 0,
            userId,
        };

        try {
            await logService.createDailyLog(dataToSend);
            setMessage({ type: 'success', text: 'Đã ghi nhật ký bữa ăn thành công!' });
            // Reset form
            setLogData(prev => ({ ...prev, foodName: '', calories: '', protein: '', carb: '', fat: '' }));
        } catch (err) {
            setMessage({ type: 'error', text: 'Lỗi khi ghi nhật ký. Vui lòng kiểm tra dữ liệu.' });
        } finally {
            setIsLoading(false);
        }
    };

    // --- Xử lý Nhận dạng Hình ảnh ---
    const handleImageAnalyze = async (e) => {
        e.preventDefault();
        if (!imageFile || isLoading) return;
        setIsLoading(true);
        setMessage(null);

        try {
            // 1. Gửi ảnh đến AI Service
            const analysisResult = await aiService.analyzeFoodImage(imageFile, userId);

            // 2. Điền kết quả phân tích vào form
            const recognizedFood = analysisResult.recognizedFood || 'Món ăn được nhận dạng';
            const calories = analysisResult.calories ? analysisResult.calories.toString() : '';

            setLogData(prev => ({
                ...prev,
                foodName: recognizedFood,
                calories: calories,
                // Điền các macro khác nếu có (protein, carb, fat)
            }));

            setMessage({
                type: 'info',
                text: `Phân tích hình ảnh hoàn tất! Đã điền thông tin món ăn (${recognizedFood}). Vui lòng kiểm tra lại trước khi Lưu.`
            });

        } catch (err) {
            setMessage({ type: 'error', text: 'Phân tích hình ảnh thất bại. Vui lòng nhập bằng tay.' });
        } finally {
            setImageFile(null); // Xóa ảnh sau khi phân tích hoặc thất bại
            setIsLoading(false);
        }
    };


    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 flex items-center">
                <Utensils className="w-7 h-7 mr-3 text-green-600" />
                Ghi nhật ký Bữa ăn Hàng ngày
            </h1>

            {/* Thông báo */}
            {message && (
                <div className={`p-3 mb-4 rounded-lg text-sm ${message.type === 'success' ? 'bg-green-100 text-green-700' : message.type === 'error' ? 'bg-red-100 text-red-700' : 'bg-blue-100 text-blue-700'}`}>
                    {message.text}
                </div>
            )}

            <div className="bg-white p-6 rounded-xl shadow-lg">

                {/* Tùy chọn 1: Nhận dạng bằng Hình ảnh */}
                <div className="mb-6 pb-6 border-b border-gray-200">
                    <h2 className="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                        <Image className="w-5 h-5 mr-2" />
                        Nhận dạng Món ăn (Vision AI)
                    </h2>
                    <form onSubmit={handleImageAnalyze} className="flex items-end space-x-4">
                        <div className="flex-grow">
                            <label className="block text-sm font-medium text-gray-700">Tải lên Hình ảnh Bữa ăn</label>
                            <input
                                type="file"
                                accept="image/*"
                                onChange={(e) => setImageFile(e.target.files[0])}
                                className="mt-1 w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-green-50 file:text-green-700 hover:file:bg-green-100"
                                disabled={isLoading}
                            />
                            {imageFile && (
                                <p className="mt-2 text-xs text-gray-500 flex items-center">
                                    <X className="w-3 h-3 mr-1 cursor-pointer" onClick={() => setImageFile(null)} />
                                    {imageFile.name} đã sẵn sàng để phân tích.
                                </p>
                            )}
                        </div>
                        <button
                            type="submit"
                            disabled={isLoading || !imageFile}
                            className={`px-6 py-2 text-white font-semibold rounded-lg transition duration-150 ${
                                (isLoading || !imageFile) ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'
                            }`}
                        >
                            {isLoading ? 'Đang phân tích...' : 'Phân tích & Điền Form'}
                        </button>
                    </form>
                </div>

                {/* Tùy chọn 2: Nhập liệu Thủ công */}
                <form onSubmit={handleManualSubmit} className="space-y-4">
                    <h2 className="text-xl font-semibold text-gray-700 mb-4 flex items-center">
                        <Soup className="w-5 h-5 mr-2" />
                        Nhập liệu Thủ công
                    </h2>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {/* Tên món ăn */}
                        <div className="col-span-1">
                            <label className="block text-sm font-medium text-gray-700">Tên Món ăn/Thực phẩm</label>
                            <input
                                type="text"
                                required
                                value={logData.foodName}
                                onChange={(e) => setLogData({ ...logData, foodName: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                                placeholder="Ví dụ: Cơm gà, Trứng ốp la"
                            />
                        </div>

                        {/* Loại bữa ăn */}
                        <div className="col-span-1">
                            <label className="block text-sm font-medium text-gray-700">Loại Bữa ăn</label>
                            <select
                                required
                                value={logData.mealType}
                                onChange={(e) => setLogData({ ...logData, mealType: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                            >
                                {MEAL_TYPES.map(type => (
                                    <option key={type} value={type}>{type}</option>
                                ))}
                            </select>
                        </div>

                        {/* Ngày */}
                        <div className="col-span-1">
                            <label className="block text-sm font-medium text-gray-700">Ngày Ghi nhật ký</label>
                            <input
                                type="date"
                                required
                                value={logData.date}
                                onChange={(e) => setLogData({ ...logData, date: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                            />
                        </div>
                    </div>

                    {/* Calo và Macros */}
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 pt-2 border-t border-gray-100">
                        {/* Calo */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 flex items-center">
                                <Zap className="w-4 h-4 mr-1 text-yellow-500" />
                                Calo (kcal)
                            </label>
                            <input
                                type="number"
                                value={logData.calories}
                                onChange={(e) => setLogData({ ...logData, calories: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                                placeholder="0"
                            />
                        </div>
                        {/* Protein */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Protein (g)</label>
                            <input
                                type="number"
                                value={logData.protein}
                                onChange={(e) => setLogData({ ...logData, protein: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                                placeholder="0"
                            />
                        </div>
                        {/* Carb */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Carb (g)</label>
                            <input
                                type="number"
                                value={logData.carb}
                                onChange={(e) => setLogData({ ...logData, carb: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                                placeholder="0"
                            />
                        </div>
                        {/* Fat */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Chất béo (g)</label>
                            <input
                                type="number"
                                value={logData.fat}
                                onChange={(e) => setLogData({ ...logData, fat: e.target.value })}
                                className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                                placeholder="0"
                            />
                        </div>
                    </div>

                    {/* Nút Ghi nhật ký */}
                    <button
                        type="submit"
                        disabled={isLoading || !logData.foodName.trim()}
                        className={`w-full py-3 mt-4 text-white font-semibold rounded-lg shadow-md transition duration-150 ${
                            (isLoading || !logData.foodName.trim()) ? 'bg-gray-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700'
                        }`}
                    >
                        <div className="flex items-center justify-center">
                            <PlusCircle className="w-5 h-5 mr-2" />
                            {isLoading ? 'Đang lưu...' : 'Lưu Mục nhật ký'}
                        </div>
                    </button>
                </form>
            </div>
        </div>
    );
}

export default DailyLogInputPage;