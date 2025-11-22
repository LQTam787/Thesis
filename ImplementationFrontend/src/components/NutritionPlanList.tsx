import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { CheDoDinhDuong } from '../types';

const NutritionPlanList: React.FC = () => {
  const [nutritionPlans, setNutritionPlans] = useState<CheDoDinhDuong[]>([]);
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'error' } | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadNutritionPlans();
  }, []);

  const loadNutritionPlans = () => {
    const storedPlans = localStorage.getItem('nutritionPlans');
    if (storedPlans) {
      setNutritionPlans(JSON.parse(storedPlans));
    }
  };

  const showMessage = (text: string, type: 'success' | 'error') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(null), 3000);
  };

  const handleDelete = (maCheDo: string) => {
    if (confirm('Bạn có chắc chắn muốn xóa chế độ dinh dưỡng này không?')) {
      const updatedPlans = nutritionPlans.filter(plan => plan.maCheDo !== maCheDo);
      localStorage.setItem('nutritionPlans', JSON.stringify(updatedPlans));
      setNutritionPlans(updatedPlans);
      showMessage('Xóa chế độ dinh dưỡng thành công!', 'success');
    }
  };

  return (
    <div className="container mx-auto p-6 bg-white shadow-lg rounded-lg my-8">
      <h1 className="text-4xl font-bold text-center text-gray-800 mb-8">Quản lý Chế độ Dinh dưỡng</h1>

      {message && (
        <div className={`p-4 mb-4 text-white rounded-md ${message.type === 'success' ? 'bg-green-500' : 'bg-red-500'}`}>
          {message.text}
        </div>
      )}

      <div className="flex justify-end mb-6">
        <Link to="/nutrition-plans/new" className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-md transition duration-300">
          Tạo mới Chế độ Dinh dưỡng
        </Link>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full bg-white border border-gray-200">
          <thead>
            <tr>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Mã</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Tên Chế độ</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Mô tả</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Bắt đầu</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Kết thúc</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Mục tiêu</th>
              <th className="py-3 px-4 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {nutritionPlans.map((plan) => (
              <tr key={plan.maCheDo} className="border-b border-gray-200 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-700">{plan.maCheDo}</td>
                <td className="py-3 px-4 text-sm text-gray-700">{plan.tenCheDo}</td>
                <td className="py-3 px-4 text-sm text-gray-700">{plan.moTa || ''}</td>
                <td className="py-3 px-4 text-sm text-gray-700">{plan.ngayBatDau}</td>
                <td className="py-3 px-4 text-sm text-gray-700">{plan.ngayKetThuc || ''}</td>
                <td className="py-3 px-4 text-sm text-gray-700">{plan.mucTieu || ''}</td>
                <td className="py-3 px-4 text-sm text-gray-700 flex space-x-2">
                  <Link to={`/nutrition-plans/${plan.maCheDo}`} className="bg-green-500 hover:bg-green-600 text-white py-1 px-3 rounded-md text-xs transition duration-300">Xem</Link>
                  <Link to={`/nutrition-plans/${plan.maCheDo}/edit`} className="bg-yellow-500 hover:bg-yellow-600 text-white py-1 px-3 rounded-md text-xs transition duration-300">Sửa</Link>
                  <button
                    onClick={() => handleDelete(plan.maCheDo)}
                    className="bg-red-500 hover:bg-red-600 text-white py-1 px-3 rounded-md text-xs transition duration-300"
                  >
                    Xóa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default NutritionPlanList;
