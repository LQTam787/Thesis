import React, { useState, useEffect } from 'react';

const ProgressReportPage: React.FC = () => {
  const [currentWeight, setCurrentWeight] = useState(70);
  const [weightChange, setWeightChange] = useState(-0.5);
  const [bmi, setBmi] = useState(22.9);
  const [bodyFat, setBodyFat] = useState(18);
  const [caloriesConsumed, setCaloriesConsumed] = useState(1800);
  const [targetCalories, setTargetCalories] = useState(2000);
  const [proteinConsumed, setProteinConsumed] = useState(120);
  const [targetProtein, setTargetProtein] = useState(150);
  const [carbConsumed, setCarbConsumed] = useState(180);
  const [targetCarb, setTargetCarb] = useState(200);
  const [fatConsumed, setFatConsumed] = useState(60);
  const [targetFat, setTargetFat] = useState(70);
  const [advice, setAdvice] = useState(
    "Bạn đang có xu hướng giảm nhẹ lượng protein. Hãy cân nhắc bổ sung thêm các nguồn protein từ thịt nạc, trứng hoặc các sản phẩm từ sữa. Hoạt động thể chất của bạn đang rất tốt, hãy duy trì cường độ này để đạt được mục tiêu."
  );

  // Simulate fetching data for the report
  useEffect(() => {
    // In a real application, you would fetch this data from an API
    // For now, we use static data
  }, []);

  const generateNewReport = () => {
    alert('Đang tạo báo cáo mới...');
    // Logic to fetch new data and update the report would go here
    console.log('Generating new report...');
  };

  return (
    <div className="container mx-auto p-6 bg-white rounded-lg shadow-md max-w-3xl my-10">
      <h1 className="text-3xl font-bold text-center text-teal-700 mb-8">Báo Cáo Tiến Độ và Đánh Giá</h1>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Tiến Độ Cân Nặng</h2>
        <div className="w-full h-72 bg-gray-200 flex items-center justify-center text-gray-600 rounded-md mb-6">
          Biểu đồ tiến độ cân nặng (dữ liệu giả định)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm mb-3">
          <strong className="text-teal-700">Cân nặng hiện tại:</strong> {currentWeight} kg
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm">
          <strong className="text-teal-700">Thay đổi trong 7 ngày qua:</strong> {weightChange} kg
        </div>
      </section>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Chỉ Số Cơ Thể</h2>
        <div className="w-full h-72 bg-gray-200 flex items-center justify-center text-gray-600 rounded-md mb-6">
          Biểu đồ chỉ số cơ thể (dữ liệu giả định)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm mb-3">
          <strong className="text-teal-700">BMI:</strong> {bmi} (Bình thường)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm">
          <strong className="text-teal-700">Tỷ lệ mỡ cơ thể:</strong> {bodyFat}%
        </div>
      </section>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Báo Cáo Dinh Dưỡng</h2>
        <div className="w-full h-72 bg-gray-200 flex items-center justify-center text-gray-600 rounded-md mb-6">
          Biểu đồ Macro/Micro Nutrient (dữ liệu giả định)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm mb-3">
          <strong className="text-teal-700">Tổng Calo tiêu thụ hôm nay:</strong> {caloriesConsumed} Calo (Mục tiêu: {targetCalories} Calo)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm mb-3">
          <strong className="text-teal-700">Protein:</strong> {proteinConsumed}g (Mục tiêu: {targetProtein}g)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm mb-3">
          <strong className="text-teal-700">Carb:</strong> {carbConsumed}g (Mục tiêu: {targetCarb}g)
        </div>
        <div className="bg-blue-100 p-3 rounded-md shadow-sm">
          <strong className="text-teal-700">Fat:</strong> {fatConsumed}g (Mục tiêu: {targetFat}g)
        </div>
      </section>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Gợi Ý Điều Chỉnh</h2>
        <div className="bg-green-100 border border-green-200 text-green-700 p-4 rounded-md mt-6">
          <strong className="text-green-800">Dựa trên dữ liệu của bạn:</strong>
          <p>{advice}</p>
        </div>
      </section>

      <div className="text-center">
        <button
          onClick={generateNewReport}
          className="bg-teal-700 text-white py-4 px-10 rounded-lg text-xl font-semibold hover:bg-teal-800 transition duration-300"
        >
          Tải lại Báo cáo
        </button>
      </div>
    </div>
  );
};

export default ProgressReportPage;
