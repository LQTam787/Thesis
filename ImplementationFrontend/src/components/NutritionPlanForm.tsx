import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { CheDoDinhDuong } from '../types';

const NutritionPlanForm: React.FC = () => {
  const [plan, setPlan] = useState<CheDoDinhDuong>({
    maCheDo: '',
    tenCheDo: '',
    moTa: '',
    ngayBatDau: '',
    ngayKetThuc: '',
    mucTieu: '',
  });
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'error' } | null>(null);
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  useEffect(() => {
    if (id) {
      // Editing existing plan
      const storedPlans = localStorage.getItem('nutritionPlans');
      if (storedPlans) {
        const plans: CheDoDinhDuong[] = JSON.parse(storedPlans);
        const planToEdit = plans.find(p => p.maCheDo === id);
        if (planToEdit) {
          setPlan(planToEdit);
        } else {
          showMessage('Không tìm thấy chế độ dinh dưỡng để chỉnh sửa.', 'error');
          navigate('/nutrition-plans');
        }
      }
    }
  }, [id, navigate]);

  const showMessage = (text: string, type: 'success' | 'error') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(null), 3000);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { id, value } = e.target;
    setPlan(prevPlan => ({
      ...prevPlan,
      [id]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const storedPlans = localStorage.getItem('nutritionPlans');
    let plans: CheDoDinhDuong[] = storedPlans ? JSON.parse(storedPlans) : [];

    if (id) {
      // Update existing plan
      const index = plans.findIndex(p => p.maCheDo === id);
      if (index > -1) {
        plans[index] = plan;
        showMessage('Cập nhật chế độ dinh dưỡng thành công!', 'success');
      } else {
        showMessage('Không tìm thấy chế độ để cập nhật.', 'error');
      }
    } else {
      // Add new plan
      const newPlan = { ...plan, maCheDo: `CDD${Date.now()}` };
      plans.push(newPlan);
      showMessage('Thêm chế độ dinh dưỡng thành công!', 'success');
    }

    localStorage.setItem('nutritionPlans', JSON.stringify(plans));
    navigate('/nutrition-plans');
  };

  return (
    <div className="container mx-auto p-6 bg-white shadow-lg rounded-lg my-8">
      <h1 className="text-4xl font-bold text-center text-gray-800 mb-8">{id ? 'Chỉnh sửa Chế độ Dinh dưỡng' : 'Tạo mới Chế độ Dinh dưỡng'}</h1>

      {message && (
        <div className={`p-4 mb-4 text-white rounded-md ${message.type === 'success' ? 'bg-green-500' : 'bg-red-500'}`}>
          {message.text}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="form-group">
          <label htmlFor="tenCheDo" className="block text-lg font-medium text-gray-700">Tên Chế độ:</label>
          <input
            type="text"
            id="tenCheDo"
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            value={plan.tenCheDo}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="moTa" className="block text-lg font-medium text-gray-700">Mô tả:</label>
          <textarea
            id="moTa"
            rows={3}
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            value={plan.moTa}
            onChange={handleChange}
          ></textarea>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="form-group">
            <label htmlFor="ngayBatDau" className="block text-lg font-medium text-gray-700">Ngày bắt đầu:</label>
            <input
              type="date"
              id="ngayBatDau"
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              value={plan.ngayBatDau}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="ngayKetThuc" className="block text-lg font-medium text-gray-700">Ngày kết thúc:</label>
            <input
              type="date"
              id="ngayKetThuc"
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              value={plan.ngayKetThuc}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="mucTieu" className="block text-lg font-medium text-gray-700">Mục tiêu:</label>
          <input
            type="text"
            id="mucTieu"
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            value={plan.mucTieu}
            onChange={handleChange}
            placeholder="VD: Giảm cân, Tăng cơ"
            required
          />
        </div>

        <div className="flex justify-end space-x-4 mt-6">
          <button
            type="button"
            onClick={() => navigate('/nutrition-plans')}
            className="px-4 py-2 bg-gray-300 text-gray-800 font-bold rounded-md hover:bg-gray-400 transition duration-300"
          >
            Hủy
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white font-bold rounded-md hover:bg-blue-700 transition duration-300"
          >
            {id ? 'Cập nhật' : 'Tạo mới'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default NutritionPlanForm;
