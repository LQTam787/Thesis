import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { CheDoDinhDuong } from '../types';

const NutritionPlanDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [plan, setPlan] = useState<CheDoDinhDuong | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const loadPlanDetail = () => {
      const storedPlans = localStorage.getItem('nutritionPlans');
      if (storedPlans) {
        const plans: CheDoDinhDuong[] = JSON.parse(storedPlans);
        const foundPlan = plans.find(p => p.maCheDo === id);
        if (foundPlan) {
          setPlan(foundPlan);
        } else {
          alert('Không tìm thấy kế hoạch dinh dưỡng.');
          navigate('/nutrition-plans');
        }
      } else {
        alert('Không có kế hoạch dinh dưỡng nào được lưu.');
        navigate('/nutrition-plans');
      }
      setLoading(false);
    };
    loadPlanDetail();
  }, [id, navigate]);

  if (loading) {
    return <div className="text-center text-lg mt-8">Đang tải...</div>;
  }

  if (!plan) {
    return <div className="text-center text-lg mt-8">Không tìm thấy thông tin kế hoạch.</div>;
  }

  return (
    <div className="container mx-auto p-6 bg-white shadow-lg rounded-lg my-8">
      <h1 className="text-4xl font-bold text-center text-gray-800 mb-8">Chi tiết Kế hoạch Dinh dưỡng</h1>

      <div className="detail-section bg-gray-50 p-6 rounded-md shadow-sm mb-8">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg">
          <p><strong>Mã Kế hoạch:</strong> {plan.maCheDo}</p>
          <p><strong>Tên Kế hoạch:</strong> {plan.tenCheDo}</p>
          <p><strong>Mô tả:</strong> {plan.moTa || 'Không có'}</p>
          <p><strong>Ngày bắt đầu:</strong> {plan.ngayBatDau}</p>
          <p><strong>Ngày kết thúc:</strong> {plan.ngayKetThuc || 'Chưa xác định'}</p>
          <p><strong>Mục tiêu:</strong> {plan.mucTieu}</p>
        </div>
      </div>

      <div className="meal-plan mt-8 border-t pt-8 border-gray-200">
        <h2 className="text-3xl font-semibold text-gray-700 mb-6">Thực đơn chi tiết</h2>
        <div id="mealList" className="space-y-6">
          {/* Simulate loading detailed meal data. In a real app, this would be fetched from a backend. */}
          {/* For now, we use a static placeholder similar to the example HTML. */}
          <div className="meal bg-white p-5 rounded-md shadow-md">
            <h3 className="text-xl font-semibold text-blue-600 mb-4">Bữa sáng (7:00 AM)</h3>
            <div className="space-y-2">
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Trứng ốp la (2 quả)</span>
                <span className="text-gray-600 text-sm">150 kcal, 12g P, 1g C, 11g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Bánh mì nguyên cám (1 lát)</span>
                <span className="text-gray-600 text-sm">80 kcal, 4g P, 15g C, 1g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Sữa tươi không đường (200ml)</span>
                <span className="text-gray-600 text-sm">100 kcal, 7g P, 10g C, 4g F</span>
              </div>
            </div>
          </div>

          <div className="meal bg-white p-5 rounded-md shadow-md">
            <h3 className="text-xl font-semibold text-blue-600 mb-4">Bữa trưa (12:30 PM)</h3>
            <div className="space-y-2">
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Ức gà luộc (150g)</span>
                <span className="text-gray-600 text-sm">240 kcal, 45g P, 0g C, 5g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Cơm gạo lứt (150g)</span>
                <span className="text-gray-600 text-sm">200 kcal, 4g P, 40g C, 1g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Rau cải xanh luộc (200g)</span>
                <span className="text-gray-600 text-sm">50 kcal, 3g P, 10g C, 0g F</span>
              </div>
            </div>
          </div>

          <div className="meal bg-white p-5 rounded-md shadow-md">
            <h3 className="text-xl font-semibold text-blue-600 mb-4">Bữa tối (7:00 PM)</h3>
            <div className="space-y-2">
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Cá hồi nướng (100g)</span>
                <span className="text-gray-600 text-sm">200 kcal, 20g P, 0g C, 13g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Khoai lang luộc (100g)</span>
                <span className="text-gray-600 text-sm">90 kcal, 2g P, 20g C, 0g F</span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-gray-100 last:border-b-0">
                <span className="text-gray-800">Salad rau trộn</span>
                <span className="text-gray-600 text-sm">80 kcal, 2g P, 8g C, 5g F</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="flex justify-end mt-8">
        <Link to="/nutrition-plans" className="px-6 py-3 bg-gray-600 text-white font-bold rounded-md hover:bg-gray-700 transition duration-300">
          Quay lại danh sách
        </Link>
      </div>
    </div>
  );
};

export default NutritionPlanDetail;
