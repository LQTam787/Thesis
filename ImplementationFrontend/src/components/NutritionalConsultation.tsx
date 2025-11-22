import React, { useState, useEffect } from 'react';

interface NutritionalNeeds {
  maNhuCau?: string;
  maNguoiDung: string;
  chieuCao: number;
  canNang: number;
  tuoi: number;
  gioiTinh: 'nam' | 'nu' | '';
  mucDoHoatDong: 'it' | 'nhe' | 'vua' | 'nang' | 'rat_nang' | '';
  mucTieuDinhDuong: string;
  caloKhuyenNghi?: number;
  proteinKhuyenNghi?: number;
  chatBeoKhuyenNghi?: number;
  carbohydrateKhuyenNghi?: number;
}

interface NutritionPlan {
  maCheDo: string;
  tenCheDo: string;
  moTa: string;
  ngayBatDau: string;
  ngayKetThuc: string;
  mucTieu: string;
}

const NutritionalConsultation: React.FC = () => {
  const [nutritionalNeeds, setNutritionalNeeds] = useState<NutritionalNeeds>({
    maNguoiDung: 'USER_ID_PLACEHOLDER', // Placeholder, replace with actual user ID
    chieuCao: 0,
    canNang: 0,
    tuoi: 0,
    gioiTinh: '',
    mucDoHoatDong: '',
    mucTieuDinhDuong: '',
  });

  const [nutritionPlans, setNutritionPlans] = useState<NutritionPlan[]>([]);
  const [showAlert, setShowAlert] = useState<{ message: string; type: 'success' | 'error' | 'info' } | null>(null);

  useEffect(() => {
    // Load data from localStorage
    const savedNutritionalNeeds = localStorage.getItem('nutritionalNeeds_USER_ID_PLACEHOLDER');
    if (savedNutritionalNeeds) {
      setNutritionalNeeds(JSON.parse(savedNutritionalNeeds));
    }
    const savedNutritionPlans = localStorage.getItem('nutritionPlans_USER_ID_PLACEHOLDER');
    if (savedNutritionPlans) {
      setNutritionPlans(JSON.parse(savedNutritionPlans));
    }
  }, []);

  const displayAlert = (message: string, type: 'success' | 'error' | 'info') => {
    setShowAlert({ message, type });
    setTimeout(() => {
      setShowAlert(null);
    }, 3000);
  };

  const calculateBMR = (weight: number, height: number, age: number, gender: 'nam' | 'nu' | '') => {
    if (gender === 'nam') {
      return (10 * weight) + (6.25 * height) - (5 * age) + 5;
    } else if (gender === 'nu') {
      return (10 * weight) + (6.25 * height) - (5 * age) - 161;
    }
    return 0;
  };

  const calculateTDEE = (bmr: number, activityLevel: 'it' | 'nhe' | 'vua' | 'nang' | 'rat_nang' | '') => {
    let multiplier = 1.2; // Sedentary
    if (activityLevel === 'nhe') multiplier = 1.375;
    else if (activityLevel === 'vua') multiplier = 1.55;
    else if (activityLevel === 'nang') multiplier = 1.725;
    else if (activityLevel === 'rat_nang') multiplier = 1.9;
    return bmr * multiplier;
  };

  const handleSubmitNutritionalNeeds = (e: React.FormEvent) => {
    e.preventDefault();

    const { chieuCao, canNang, tuoi, gioiTinh, mucDoHoatDong, mucTieuDinhDuong } = nutritionalNeeds;

    if (!chieuCao || !canNang || !tuoi || !gioiTinh || !mucDoHoatDong || !mucTieuDinhDuong) {
      displayAlert('Vui lòng điền đầy đủ các thông tin bắt buộc.', 'error');
      return;
    }

    const bmr = calculateBMR(canNang, chieuCao, tuoi, gioiTinh);
    const tdee = calculateTDEE(bmr, mucDoHoatDong);

    // Simple macronutrient distribution (example: 25% protein, 30% fat, 45% carb)
    const proteinCalo = tdee * 0.25;
    const fatCalo = tdee * 0.30;
    const carbCalo = tdee * 0.45;

    const proteinGrams = proteinCalo / 4;
    const fatGrams = fatCalo / 9;
    const carbGrams = carbCalo / 4;

    const updatedNutritionalNeeds = {
      ...nutritionalNeeds,
      maNhuCau: nutritionalNeeds.maNhuCau || `NCN${Date.now()}`,
      caloKhuyenNghi: tdee,
      proteinKhuyenNghi: proteinGrams,
      chatBeoKhuyenNghi: fatGrams,
      carbohydrateKhuyenNghi: carbGrams,
    };

    setNutritionalNeeds(updatedNutritionalNeeds);
    localStorage.setItem('nutritionalNeeds_USER_ID_PLACEHOLDER', JSON.stringify(updatedNutritionalNeeds));
    displayAlert('Lưu nhu cầu dinh dưỡng và nhận khuyến nghị thành công!', 'success');
  };

  const handleCreatePlanFromRecommendation = () => {
    if (!nutritionalNeeds.caloKhuyenNghi) {
      displayAlert('Vui lòng tính toán nhu cầu dinh dưỡng trước khi tạo kế hoạch.', 'error');
      return;
    }

    const newPlan: NutritionPlan = {
      maCheDo: `CDD${Date.now()}`,
      tenCheDo: `Kế hoạch cho ${nutritionalNeeds.mucTieuDinhDuong}`,
      moTa: `Kế hoạch dinh dưỡng dựa trên nhu cầu khuyến nghị: ${nutritionalNeeds.caloKhuyenNghi.toFixed(0)} Calo, ${nutritionalNeeds.proteinKhuyenNghi?.toFixed(1)}g Protein, ${nutritionalNeeds.chatBeoKhuyenNghi?.toFixed(1)}g Fat, ${nutritionalNeeds.carbohydrateKhuyenNghi?.toFixed(1)}g Carb.`,
      ngayBatDau: new Date().toISOString().slice(0, 10), // Current date
      ngayKetThuc: '', // Can be set later
      mucTieu: nutritionalNeeds.mucTieuDinhDuong,
    };

    const updatedPlans = [...nutritionPlans, newPlan];
    setNutritionPlans(updatedPlans);
    localStorage.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(updatedPlans));
    displayAlert('Đã tạo kế hoạch dinh dưỡng mới từ khuyến nghị!', 'success');
  };

  const handleDeletePlan = (id: string) => {
    if (confirm('Bạn có chắc chắn muốn xóa kế hoạch dinh dưỡng này không?')) {
      const updatedPlans = nutritionPlans.filter(p => p.maCheDo !== id);
      setNutritionPlans(updatedPlans);
      localStorage.setItem('nutritionPlans_USER_ID_PLACEHOLDER', JSON.stringify(updatedPlans));
      displayAlert('Xóa kế hoạch dinh dưỡng thành công!', 'success');
    }
  };

  // Basic form input change handler
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { id, value } = e.target;
    setNutritionalNeeds(prev => ({
      ...prev,
      [id]: (id === 'chieuCao' || id === 'canNang' || id === 'tuoi') ? parseFloat(value) || 0 : value,
    }));
  };

  return (
    <div className="container mx-auto p-4 max-w-4xl">
      <h1 className="text-3xl font-bold text-center text-gray-800 mb-8">Tư vấn Chế độ Dinh dưỡng</h1>

      {showAlert && (
        <div className={`p-4 mb-4 rounded-md text-white ${showAlert.type === 'success' ? 'bg-green-500' : showAlert.type === 'error' ? 'bg-red-500' : 'bg-blue-500'}`}>
          {showAlert.message}
        </div>
      )}

      <div className="bg-white shadow-lg rounded-lg p-6 mb-8">
        <h2 className="text-2xl font-semibold text-gray-700 mb-6">Nhập Thông tin Nhu cầu Dinh dưỡng</h2>
        <form onSubmit={handleSubmitNutritionalNeeds}>
          <input type="hidden" value={nutritionalNeeds.maNhuCau || ''} />
          <input type="hidden" value={nutritionalNeeds.maNguoiDung} />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="form-group">
              <label htmlFor="chieuCao" className="block text-gray-700 text-sm font-bold mb-2">Chiều cao (cm):</label>
              <input
                type="number"
                id="chieuCao"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                min="50"
                max="250"
                required
                value={nutritionalNeeds.chieuCao || ''}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="canNang" className="block text-gray-700 text-sm font-bold mb-2">Cân nặng (kg):</label>
              <input
                type="number"
                id="canNang"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                min="10"
                max="300"
                step="0.1"
                required
                value={nutritionalNeeds.canNang || ''}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="tuoi" className="block text-gray-700 text-sm font-bold mb-2">Tuổi:</label>
              <input
                type="number"
                id="tuoi"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                min="1"
                max="120"
                required
                value={nutritionalNeeds.tuoi || ''}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="gioiTinh" className="block text-gray-700 text-sm font-bold mb-2">Giới tính:</label>
              <select
                id="gioiTinh"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                required
                value={nutritionalNeeds.gioiTinh}
                onChange={handleChange}
              >
                <option value="">Chọn giới tính</option>
                <option value="nam">Nam</option>
                <option value="nu">Nữ</option>
              </select>
            </div>
            <div className="form-group col-span-1 md:col-span-2">
              <label htmlFor="mucDoHoatDong" className="block text-gray-700 text-sm font-bold mb-2">Mức độ hoạt động:</label>
              <select
                id="mucDoHoatDong"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                required
                value={nutritionalNeeds.mucDoHoatDong}
                onChange={handleChange}
              >
                <option value="">Chọn mức độ hoạt động</option>
                <option value="it">Ít vận động (sedentary)</option>
                <option value="nhe">Vận động nhẹ (lightly active)</option>
                <option value="vua">Vận động vừa (moderately active)</option>
                <option value="nang">Vận động nhiều (very active)</option>
                <option value="rat_nang">Vận động rất nhiều (extra active)</option>
              </select>
            </div>
            <div className="form-group col-span-1 md:col-span-2">
              <label htmlFor="mucTieuDinhDuong" className="block text-gray-700 text-sm font-bold mb-2">Mục tiêu dinh dưỡng (ví dụ: giảm 5kg trong 2 tháng, tăng cơ bắp):</label>
              <textarea
                id="mucTieuDinhDuong"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline h-24 resize-y"
                required
                value={nutritionalNeeds.mucTieuDinhDuong}
                onChange={handleChange}
              ></textarea>
            </div>
          </div>
          
          <div className="flex justify-end mt-6">
            <button
              type="submit"
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            >
              Lưu Nhu cầu & Nhận Khuyến nghị
            </button>
          </div>
        </form>

        {nutritionalNeeds.caloKhuyenNghi && (
          <div className="mt-8 pt-6 border-t border-gray-200">
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Nhu cầu Dinh dưỡng Khuyến nghị</h2>
            <p className="mb-2"><strong>Calo khuyến nghị:</strong> <span className="font-medium text-blue-600">{nutritionalNeeds.caloKhuyenNghi.toFixed(0)}</span> kcal/ngày</p>
            <p className="mb-2"><strong>Protein:</strong> <span className="font-medium text-blue-600">{nutritionalNeeds.proteinKhuyenNghi?.toFixed(1)}</span> g/ngày</p>
            <p className="mb-2"><strong>Chất béo:</strong> <span className="font-medium text-blue-600">{nutritionalNeeds.chatBeoKhuyenNghi?.toFixed(1)}</span> g/ngày</p>
            <p className="mb-2"><strong>Carbohydrate:</strong> <span className="font-medium text-blue-600">{nutritionalNeeds.carbohydrateKhuyenNghi?.toFixed(1)}</span> g/ngày</p>
            <div className="flex justify-end mt-6">
              <button
                type="button"
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                onClick={handleCreatePlanFromRecommendation}
              >
                Tạo Kế hoạch từ Khuyến nghị
              </button>
            </div>
          </div>
        )}
      </div>

      <div className="bg-white shadow-lg rounded-lg p-6">
        <h2 className="text-2xl font-semibold text-gray-700 mb-6">Danh sách Kế hoạch Dinh dưỡng</h2>
        {nutritionPlans.length === 0 ? (
          <p className="text-gray-600">Chưa có kế hoạch dinh dưỡng nào được tạo.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full bg-white">
              <thead>
                <tr>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Mã</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Tên Chế độ</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Mô tả</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Bắt đầu</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Kết thúc</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Mục tiêu</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {nutritionPlans.map((plan) => (
                  <tr key={plan.maCheDo} className="hover:bg-gray-50">
                    <td className="py-3 px-4 border-b border-gray-200">{plan.maCheDo}</td>
                    <td className="py-3 px-4 border-b border-gray-200">{plan.tenCheDo}</td>
                    <td className="py-3 px-4 border-b border-gray-200">{plan.moTa}</td>
                    <td className="py-3 px-4 border-b border-gray-200">{plan.ngayBatDau}</td>
                    <td className="py-3 px-4 border-b border-gray-200">{plan.ngayKetThuc || 'N/A'}</td>
                    <td className="py-3 px-4 border-b border-gray-200">{plan.mucTieu}</td>
                    <td className="py-3 px-4 border-b border-gray-200 flex space-x-2">
                      <button
                        className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-1 px-3 rounded text-sm focus:outline-none focus:shadow-outline"
                        onClick={() => displayAlert('Chức năng sửa chi tiết kế hoạch chưa được triển khai.', 'info')}
                      >
                        Sửa
                      </button>
                      <button
                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded text-sm focus:outline-none focus:shadow-outline"
                        onClick={() => handleDeletePlan(plan.maCheDo)}
                      >
                        Xóa
                      </button>
                      <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-sm focus:outline-none focus:shadow-outline"
                        onClick={() => window.location.href = `/nutrition-plan-detail/${plan.maCheDo}`}
                      >
                        Xem chi tiết
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default NutritionalConsultation;
