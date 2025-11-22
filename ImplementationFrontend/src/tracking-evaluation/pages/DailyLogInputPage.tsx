import React, { useState } from 'react';
import { identifyFood } from '../../services/aiService';

const DailyLogInputPage: React.FC = () => {
  const [mealTime, setMealTime] = useState('breakfast');
  const [foodSearch, setFoodSearch] = useState('');
  const [foodItems, setFoodItems] = useState<{ name: string; calories: number }[]>([]);
  const [activityName, setActivityName] = useState('');
  const [activityDuration, setActivityDuration] = useState(30);
  const [activityIntensity, setActivityIntensity] = useState('medium');
  const [activityItems, setActivityItems] = useState<{ name: string; duration: number; intensity: string; calories: number }[]>([]);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [recognitionResult, setRecognitionResult] = useState<string | null>(null);

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setImageFile(event.target.files[0]);
    }
  };

  const handleIdentifyFood = async () => {
    if (imageFile) {
      try {
        setRecognitionResult('Đang nhận dạng...');
        const result = await identifyFood(imageFile);
        setRecognitionResult(JSON.stringify(result, null, 2));
        // Optionally, add recognized food items to the food list
        if (result && result.foodName && result.calories) {
          setFoodItems(prev => [...prev, { name: result.foodName, calories: result.calories }]);
        }
      } catch (error) {
        setRecognitionResult('Lỗi nhận dạng thực phẩm.');
        console.error('Failed to identify food:', error);
      }
    } else {
      alert('Vui lòng chọn một hình ảnh để nhận dạng.');
    }
  };

  const addFoodItem = () => {
    if (foodSearch.trim()) {
      // For now, calorie is a placeholder. In a real app, this would come from a backend API.
      const newFoodItem = { name: foodSearch.trim(), calories: Math.floor(Math.random() * 300) + 100 };
      setFoodItems([...foodItems, newFoodItem]);
      setFoodSearch('');
    }
  };

  const removeFoodItem = (index: number) => {
    setFoodItems(foodItems.filter((_, i) => i !== index));
  };

  const addActivityItem = () => {
    if (activityName.trim() && activityDuration > 0) {
      // For now, calorie is a placeholder. In a real app, this would come from a backend API.
      const newActivityItem = { name: activityName.trim(), duration: activityDuration, intensity: activityIntensity, calories: Math.floor(Math.random() * 500) + 150 };
      setActivityItems([...activityItems, newActivityItem]);
      setActivityName('');
      setActivityDuration(30);
      setActivityIntensity('medium');
    }
  };

  const removeActivityItem = (index: number) => {
    setActivityItems(activityItems.filter((_, i) => i !== index));
  };

  const saveDailyLog = () => {
    alert('Dữ liệu hàng ngày đã được lưu!');
    // Logic to send data to the backend would go here
    console.log({ mealTime, foodItems, activityItems });
  };

  return (
    <div className="container mx-auto p-6 bg-white rounded-lg shadow-md max-w-3xl my-10">
      <h1 className="text-3xl font-bold text-center text-teal-700 mb-8">Nhập Liệu Dữ Liệu Hàng Ngày</h1>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Nhận Dạng Món Ăn Bằng Hình Ảnh</h2>
        <div className="mb-4">
          <label htmlFor="food-image" className="block text-gray-700 font-bold mb-2">Tải lên hình ảnh bữa ăn:</label>
          <input
            type="file"
            id="food-image"
            accept="image/*"
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500 bg-white"
            onChange={handleImageChange}
          />
        </div>
        <div className="mb-4 text-center">
          <button
            onClick={handleIdentifyFood}
            className="bg-blue-600 text-white py-3 px-6 rounded-md hover:bg-blue-700 transition duration-300 disabled:opacity-50"
            disabled={!imageFile}
          >
            Nhận dạng Thực phẩm
          </button>
        </div>
        {recognitionResult && (
          <div className="mt-4 p-4 bg-yellow-100 border border-yellow-300 text-yellow-800 rounded-md">
            <strong>Kết quả nhận dạng:</strong>
            <pre className="whitespace-pre-wrap">`{recognitionResult}`</pre>
          </div>
        )}
      </section>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Bữa Ăn Đã Tiêu Thụ</h2>
        <div className="mb-4">
          <label htmlFor="meal-time" className="block text-gray-700 font-bold mb-2">Thời gian bữa ăn:</label>
          <select
            id="meal-time"
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500"
            value={mealTime}
            onChange={(e) => setMealTime(e.target.value)}
          >
            <option value="breakfast">Bữa sáng</option>
            <option value="lunch">Bữa trưa</option>
            <option value="dinner">Bữa tối</option>
            <option value="snack">Bữa phụ</option>
          </select>
        </div>
        <div className="mb-4 flex items-center space-x-2">
          <input
            type="text"
            id="food-search"
            className="flex-grow p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500"
            placeholder="Nhập tên món ăn hoặc thực phẩm"
            value={foodSearch}
            onChange={(e) => setFoodSearch(e.target.value)}
          />
          <button
            onClick={addFoodItem}
            className="bg-green-600 text-white py-3 px-6 rounded-md hover:bg-green-700 transition duration-300"
          >
            Thêm món ăn
          </button>
        </div>
        <div id="food-list" className="space-y-3">
          {foodItems.map((item, index) => (
            <div key={index} className="flex items-center justify-between bg-blue-100 p-3 rounded-md shadow-sm">
              <span className="text-gray-800">{item.name} ({item.calories} Calo)</span>
              <button
                onClick={() => removeFoodItem(index)}
                className="bg-red-500 text-white py-1 px-3 rounded-md hover:bg-red-600 transition duration-300"
              >
                Xóa
              </button>
            </div>
          ))}
        </div>
      </section>

      <section className="mb-8 border border-gray-200 p-6 rounded-lg bg-gray-50">
        <h2 className="text-2xl font-semibold text-teal-600 mb-6 text-center">Hoạt Động Thể Chất Đã Thực Hiện</h2>
        <div className="mb-4">
          <label htmlFor="activity-name" className="block text-gray-700 font-bold mb-2">Tên hoạt động:</label>
          <input
            type="text"
            id="activity-name"
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500"
            placeholder="Ví dụ: Chạy bộ, Yoga, Nâng tạ"
            value={activityName}
            onChange={(e) => setActivityName(e.target.value)}
          />
        </div>
        <div className="mb-4">
          <label htmlFor="activity-duration" className="block text-gray-700 font-bold mb-2">Thời lượng (phút):</label>
          <input
            type="number"
            id="activity-duration"
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500"
            min="1"
            value={activityDuration}
            onChange={(e) => setActivityDuration(Number(e.target.value))}
          />
        </div>
        <div className="mb-4">
          <label htmlFor="activity-intensity" className="block text-gray-700 font-bold mb-2">Cường độ:</label>
          <select
            id="activity-intensity"
            className="w-full p-3 border border-gray-300 rounded-md focus:ring-teal-500 focus:border-teal-500"
            value={activityIntensity}
            onChange={(e) => setActivityIntensity(e.target.value)}
          >
            <option value="low">Thấp</option>
            <option value="medium">Trung bình</option>
            <option value="high">Cao</option>
          </select>
        </div>
        <div className="mb-6 text-center">
          <button
            onClick={addActivityItem}
            className="bg-green-600 text-white py-3 px-6 rounded-md hover:bg-green-700 transition duration-300"
          >
            Thêm hoạt động
          </button>
        </div>
        <div id="activity-list" className="space-y-3">
          {activityItems.map((item, index) => (
            <div key={index} className="flex items-center justify-between bg-blue-100 p-3 rounded-md shadow-sm">
              <span className="text-gray-800">{item.name} ({item.duration} phút, Cường độ {item.intensity}) - {item.calories} Calo</span>
              <button
                onClick={() => removeActivityItem(index)}
                className="bg-red-500 text-white py-1 px-3 rounded-md hover:bg-red-600 transition duration-300"
              >
                Xóa
              </button>
            </div>
          ))}
        </div>
      </section>

      <div className="text-center">
        <button
          onClick={saveDailyLog}
          className="bg-teal-700 text-white py-4 px-10 rounded-lg text-xl font-semibold hover:bg-teal-800 transition duration-300"
        >
          Lưu Dữ Liệu Hàng Ngày
        </button>
      </div>
    </div>
  );
};

export default DailyLogInputPage;
