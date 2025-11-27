// src/pages/admin/FoodDataManagementPage.jsx
import React, { useState, useEffect } from 'react';
import adminService from '../../services/adminService';
import { Utensils, Edit, Trash2, PlusCircle, X, Search, Zap } from 'lucide-react';

const INITIAL_FOOD_STATE = {
    foodName: '',
    calories: 0,
    protein: 0,
    carb: 0,
    fat: 0,
    description: '',
};

function FoodDataManagementPage() {
    const [foods, setFoods] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentFood, setCurrentFood] = useState(INITIAL_FOOD_STATE);
    const [isEditing, setIsEditing] = useState(false);
    const [message, setMessage] = useState(null);

    // --- 1. Tải danh sách Thực phẩm ---
    const fetchFoods = async () => {
        try {
            setLoading(true);
            const data = await adminService.getAllFoods();
            setFoods(data);
            setError(null);
        } catch (err) {
            setError('Lỗi: Không thể tải danh sách Thực phẩm. Bạn có phải là Admin không?');
            // Dữ liệu giả định nếu lỗi (chỉ để demo UI)
            setFoods([
                { id: 'f1', foodName: 'Gạo trắng', calories: 130, protein: 2.7, carb: 28, fat: 0.3, description: '100g cơm trắng' },
                { id: 'f2', foodName: 'Thịt bò (thăn)', calories: 250, protein: 26, carb: 0, fat: 17, description: '100g thịt bò nạc' },
                { id: 'f3', foodName: 'Trứng gà', calories: 155, protein: 13, carb: 1.1, fat: 11, description: '100g trứng (khoảng 2 quả)' },
            ]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchFoods();
    }, []);

    // --- 2. Xử lý Mở/Đóng Modal ---
    const handleOpenCreateModal = () => {
        setCurrentFood(INITIAL_FOOD_STATE);
        setIsEditing(false);
        setIsModalOpen(true);
        setMessage(null);
    };

    const handleOpenEditModal = (food) => {
        setCurrentFood(food);
        setIsEditing(true);
        setIsModalOpen(true);
        setMessage(null);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setCurrentFood(INITIAL_FOOD_STATE);
    };

    // --- 3. Xử lý Gửi Form (Thêm/Sửa) ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        const dataToSend = {
            ...currentFood,
            // Đảm bảo các trường số là số
            calories: Number(currentFood.calories) || 0,
            protein: Number(currentFood.protein) || 0,
            carb: Number(currentFood.carb) || 0,
            fat: Number(currentFood.fat) || 0,
        };

        // Giả định rằng khi admin thay đổi dữ liệu, hệ thống sẽ tự động gửi thông báo
        // đến AI service để huấn luyện lại (như trong sequence diagram)

        try {
            let result;
            if (isEditing) {
                result = await adminService.updateFood(currentFood.id, dataToSend);
                setMessage({ type: 'success', text: `Đã cập nhật thực phẩm: ${result.foodName}` });
            } else {
                result = await adminService.createFood(dataToSend);
                setMessage({ type: 'success', text: `Đã thêm thực phẩm mới: ${result.foodName}` });
            }

            // Cập nhật lại danh sách
            fetchFoods();
            handleCloseModal();
        } catch (err) {
            setMessage({ type: 'error', text: 'Lỗi khi lưu dữ liệu. Vui lòng kiểm tra lại.' });
            console.error(err);
        }
    };

    // --- 4. Xử lý Xóa ---
    const handleDelete = async (foodId, foodName) => {
        if (!window.confirm(`Bạn có chắc chắn muốn XÓA thực phẩm "${foodName}" không? Hành động này không thể hoàn tác và sẽ ảnh hưởng đến AI! `)) {
            return;
        }
        try {
            await adminService.deleteFood(foodId);
            setMessage({ type: 'success', text: `Đã xóa thực phẩm: ${foodName}` });
            // Cập nhật lại danh sách
            fetchFoods();
        } catch (err) {
            setMessage({ type: 'error', text: 'Lỗi khi xóa dữ liệu.' });
        }
    };

    // --- 5. Lọc Thực phẩm ---
    const filteredFoods = foods.filter(food =>
        food.foodName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        food.description.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 flex items-center">
                <Utensils className="w-7 h-7 mr-3 text-red-600" />
                Quản lý Dữ liệu Thực phẩm (Admin)
            </h1>

            {error && <div className="p-4 mb-6 text-red-700 bg-red-100 border border-red-200 rounded-lg">{error}</div>}
            {message && (
                <div className={`p-3 mb-6 rounded-lg text-sm ${message.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {message.text}
                </div>
            )}

            <div className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">

                {/* Thanh tìm kiếm và Nút thêm mới */}
                <div className="mb-6 flex justify-between items-center">
                    <div className="flex items-center border border-gray-300 rounded-lg overflow-hidden w-full max-w-md">
                        <Search className="w-5 h-5 ml-3 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Tìm kiếm theo Tên hoặc Mô tả..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="w-full p-3 focus:ring-red-500 focus:border-red-500 border-none"
                        />
                    </div>
                    <button
                        onClick={handleOpenCreateModal}
                        className="flex items-center px-4 py-2 text-sm font-semibold text-white bg-red-600 rounded-lg shadow-md hover:bg-red-700 transition duration-150"
                    >
                        <PlusCircle className="w-5 h-5 mr-2" />
                        Thêm Thực phẩm Mới
                    </button>
                </div>

                {loading ? (
                    <div className="text-center p-8">Đang tải danh sách thực phẩm...</div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tên Thực phẩm</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Calo (kcal/100g)</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Protein (g)</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Carb (g)</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fat (g)</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Hành động</th>
                            </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                            {filteredFoods.map((food) => (
                                <tr key={food.id}>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        {food.foodName}
                                        <p className="text-xs text-gray-500 truncate max-w-xs">{food.description}</p>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-700 font-bold">{food.calories}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-blue-500">{food.protein}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-yellow-500">{food.carb}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-red-500">{food.fat}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button
                                            onClick={() => handleOpenEditModal(food)}
                                            className="text-indigo-600 hover:text-indigo-900"
                                        >
                                            <Edit className="w-4 h-4 inline mr-1" /> Sửa
                                        </button>
                                        <button
                                            onClick={() => handleDelete(food.id, food.foodName)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            <Trash2 className="w-4 h-4 inline mr-1" /> Xóa
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Modal Thêm/Sửa Thực phẩm */}
                {isModalOpen && (
                    <FoodFormModal
                        foodData={currentFood}
                        isEditing={isEditing}
                        onClose={handleCloseModal}
                        onSubmit={handleSubmit}
                        onChange={setCurrentFood}
                    />
                )}
            </div>
        </div>
    );
}

// Component Modal Form
const FoodFormModal = ({ foodData, isEditing, onClose, onSubmit, onChange }) => {
    const handleChange = (e) => {
        const { name, value } = e.target;
        onChange(prev => ({ ...prev, [name]: value }));
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
            <form onSubmit={onSubmit} className="bg-white p-6 rounded-xl w-full max-w-lg shadow-2xl">
                <div className="flex justify-between items-center border-b pb-3 mb-4">
                    <h3 className="text-2xl font-bold text-red-700">
                        {isEditing ? 'Sửa' : 'Thêm'} Dữ liệu Thực phẩm
                    </h3>
                    <button type="button" onClick={onClose} className="text-gray-400 hover:text-gray-600">
                        <X className="w-6 h-6" />
                    </button>
                </div>

                <div className="space-y-4">
                    <InputField label="Tên Thực phẩm" name="foodName" type="text" value={foodData.foodName} onChange={handleChange} required />
                    <InputField label="Mô tả/Đơn vị (VD: 100g, 1 bát)" name="description" type="text" value={foodData.description} onChange={handleChange} required />

                    <div className="grid grid-cols-2 gap-4">
                        <InputField label="Calo (kcal)" name="calories" type="number" value={foodData.calories} onChange={handleChange} required />
                        <InputField label="Protein (g)" name="protein" type="number" value={foodData.protein} onChange={handleChange} required />
                        <InputField label="Carb (g)" name="carb" type="number" value={foodData.carb} onChange={handleChange} required />
                        <InputField label="Fat (Chất béo) (g)" name="fat" type="number" value={foodData.fat} onChange={handleChange} required />
                    </div>
                </div>

                <button
                    type="submit"
                    className="w-full py-2 mt-6 rounded-lg text-white font-semibold bg-red-600 hover:bg-red-700 transition"
                >
                    {isEditing ? 'Lưu Thay Đổi' : 'Thêm Thực phẩm'}
                </button>
            </form>
        </div>
    );
};

// Component Input Field tiện ích
const InputField = ({ label, name, type, value, onChange, required = false }) => (
    <div>
        <label className="block text-sm font-medium text-gray-700">{label}</label>
        <input
            type={type}
            name={name}
            value={value}
            onChange={onChange}
            required={required}
            min={type === 'number' ? 0 : undefined}
            className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-red-500 focus:border-red-500"
        />
    </div>
);

export default FoodDataManagementPage;