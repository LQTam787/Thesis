
import React, { useState, useEffect } from 'react';

// Định nghĩa kiểu dữ liệu cho hồ sơ người dùng
interface UserProfile {
  realName: string;
  email: string;
  username: string;
  height: string;
  weight: string;
  activityLevel: string;
  underlyingMedicalConditions: string;
  allergies: string;
}

const ProfileManagementPage: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile>({
    realName: 'Nguyễn Văn A',
    email: 'nguyenvana@example.com',
    username: 'nguyenvana',
    height: '175',
    weight: '70',
    activityLevel: 'lightlyActive',
    underlyingMedicalConditions: '',
    allergies: '',
  });

  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');

  // Simulate fetching profile data
  useEffect(() => {
    // In a real application, you would fetch data from an API here
    // For now, we use dummy data
    const fetchedProfile: UserProfile = {
      realName: 'Nguyễn Văn A',
      email: 'nguyenvana@example.com',
      username: 'nguyenvana',
      height: '175',
      weight: '70',
      activityLevel: 'lightlyActive',
      underlyingMedicalConditions: 'Không có',
      allergies: 'Không có',
    };
    setProfile(fetchedProfile);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setProfile((prevProfile) => ({
      ...prevProfile,
      [name]: value,
    }));
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(e.target.value);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert('Mật khẩu xác nhận không khớp!');
      return;
    }
    // Logic to save changes to the backend
    console.log('Profile saved:', profile);
    console.log('New password:', password);
    alert('Đã lưu thay đổi!');
  };

  const handleCancel = () => {
    alert('Đã hủy các thay đổi.');
    // Logic to revert changes or navigate away
  };

  return (
    <div className="container mx-auto p-6 bg-white rounded-lg shadow-md max-w-2xl mt-10">
      <h1 className="text-3xl font-bold text-center text-gray-800 mb-8">Quản lý Hồ sơ Cá nhân</h1>
      <form id="profileForm" onSubmit={handleSubmit}>
        <h2 className="text-xl font-semibold text-gray-700 mb-4">Thông tin cá nhân</h2>
        <div className="mb-4">
          <label htmlFor="realName" className="block text-gray-700 text-sm font-bold mb-2">Tên thật:</label>
          <input
            type="text"
            id="realName"
            name="realName"
            value={profile.realName}
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="email" className="block text-gray-700 text-sm font-bold mb-2">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={profile.email}
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            required
          />
        </div>

        <h2 className="text-xl font-semibold text-gray-700 mb-4 mt-6">Thông tin tài khoản</h2>
        <div className="mb-4">
          <label htmlFor="username" className="block text-gray-700 text-sm font-bold mb-2">Tên tài khoản:</label>
          <input
            type="text"
            id="username"
            name="username"
            value={profile.username}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-gray-100"
            disabled
          />
        </div>
        <div className="mb-4">
          <label htmlFor="password" className="block text-gray-700 text-sm font-bold mb-2">Mật khẩu mới (để trống nếu không đổi):</label>
          <input
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={handlePasswordChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-4">
          <label htmlFor="confirmPassword" className="block text-gray-700 text-sm font-bold mb-2">Xác nhận mật khẩu mới:</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={confirmPassword}
            onChange={handleConfirmPasswordChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
        </div>

        <h2 className="text-xl font-semibold text-gray-700 mb-4 mt-6">Đặc điểm cá nhân</h2>
        <div className="mb-4">
          <label htmlFor="height" className="block text-gray-700 text-sm font-bold mb-2">Chiều cao (cm):</label>
          <input
            type="text"
            id="height"
            name="height"
            value={profile.height}
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-4">
          <label htmlFor="weight" className="block text-gray-700 text-sm font-bold mb-2">Cân nặng (kg):</label>
          <input
            type="text"
            id="weight"
            name="weight"
            value={profile.weight}
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
        </div>
        <div className="mb-4">
          <label htmlFor="activityLevel" className="block text-gray-700 text-sm font-bold mb-2">Mức độ vận động:</label>
          <select
            id="activityLevel"
            name="activityLevel"
            value={profile.activityLevel}
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          >
            <option value="sedentary">Ít vận động</option>
            <option value="lightlyActive">Vận động nhẹ</option>
            <option value="moderatelyActive">Vận động vừa</option>
            <option value="veryActive">Vận động nhiều</option>
            <option value="extraActive">Vận động rất nhiều</option>
          </select>
        </div>
        <div className="mb-4">
          <label htmlFor="underlyingMedicalConditions" className="block text-gray-700 text-sm font-bold mb-2">Bệnh lý nền:</label>
          <textarea
            id="underlyingMedicalConditions"
            name="underlyingMedicalConditions"
            value={profile.underlyingMedicalConditions}
            onChange={handleChange}
            placeholder="Ví dụ: Tiểu đường, huyết áp cao..."
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline h-24 resize-y"
          ></textarea>
        </div>
        <div className="mb-4">
          <label htmlFor="allergies" className="block text-gray-700 text-sm font-bold mb-2">Dị ứng:</label>
          <textarea
            id="allergies"
            name="allergies"
            value={profile.allergies}
            onChange={handleChange}
            placeholder="Ví dụ: Lạc, hải sản..."
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline h-24 resize-y"
          ></textarea>
        </div>

        <div className="flex justify-end gap-4 mt-8">
          <button
            type="submit"
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline transition duration-200 ease-in-out transform hover:-translate-y-1"
          >
            Lưu thay đổi
          </button>
          <button
            type="button"
            onClick={handleCancel}
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline transition duration-200 ease-in-out transform hover:-translate-y-1"
          >
            Hủy
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProfileManagementPage;
