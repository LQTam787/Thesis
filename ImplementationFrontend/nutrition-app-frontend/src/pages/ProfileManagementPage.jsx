// src/pages/ProfileManagementPage.jsx
import React, { useState, useEffect } from 'react';
import profileService from '../services/profileService';
import { User, Target, Heart, Scale, Clock, AlertTriangle } from 'lucide-react';
import { useSelector } from 'react-redux';

// Dữ liệu giả định cho lần tải đầu tiên (để tránh lỗi)
const INITIAL_PROFILE = {
    user: { fullName: 'Tên người dùng', email: 'user@example.com', username: 'username123' },
    characteristics: { height: 170, weight: 65, activityLevel: 'MODERATE', allergies: 'Không', underlyingDisease: 'Không' },
    goals: { goalType: 'MAINTAIN', durationDays: 30, targetDailyCalories: 2000 },
};

function ProfileManagementPage() {
    const [profile, setProfile] = useState(INITIAL_PROFILE);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState(null);

    // Lấy dữ liệu người dùng từ Redux store để hiển thị thông tin cơ bản
    const reduxUser = useSelector(state => state.auth.user);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await profileService.getProfile();
                setProfile(prev => ({
                    ...prev,
                    ...data, // Ghi đè bằng dữ liệu API
                    user: reduxUser || data.user // Ưu tiên dữ liệu đã xác thực
                }));
            } catch (err) {
                setMessage({ type: 'error', text: 'Không thể tải hồ sơ. Vui lòng kiểm tra kết nối.' });
                // Nếu lỗi, vẫn hiển thị thông tin user từ Redux
                setProfile(prev => ({ ...prev, user: reduxUser || prev.user }));
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, [reduxUser]);

    const handleUpdate = (type, updatedData, serviceCall) => async (e) => {
        e.preventDefault();
        setMessage(null);
        try {
            const result = await serviceCall(updatedData);

            // Cập nhật state cục bộ
            setProfile(prev => ({
                ...prev,
                [type]: { ...prev[type], ...result }
            }));

            setMessage({ type: 'success', text: `${type === 'goals' ? 'Mục tiêu' : 'Đặc điểm'} đã được cập nhật thành công!` });
        } catch (err) {
            setMessage({ type: 'error', text: `Lỗi cập nhật ${type}. Vui lòng thử lại.` });
        }
    };

    if (loading) {
        return <div className="p-8 text-center text-lg">Đang tải hồ sơ...</div>;
    }

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 flex items-center">
                <User className="w-7 h-7 mr-3 text-green-600" />
                Quản lý Hồ sơ Cá nhân
            </h1>

            {/* Thông báo */}
            {message && (
                <div className={`p-3 mb-6 rounded-lg text-sm ${message.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {message.text}
                </div>
            )}

            {/* 1. Thông tin Cơ bản (Chỉ đọc) */}
            <div className="bg-white p-6 rounded-xl shadow-lg mb-8">
                <h2 className="text-xl font-bold text-gray-700 mb-4 flex items-center">
                    <User className="w-5 h-5 mr-2" />
                    Thông tin Tài khoản
                </h2>
                <div className="grid grid-cols-2 gap-4 text-sm text-gray-600">
                    <p><strong>Tên đầy đủ:</strong> {profile.user.fullName}</p>
                    <p><strong>Username:</strong> {profile.user.username}</p>
                    <p className="col-span-2"><strong>Email:</strong> {profile.user.email}</p>
                </div>
                <p className="mt-3 text-xs text-blue-500">Liên hệ quản trị viên để thay đổi email hoặc username.</p>
            </div>

            {/* 2. Form Cập nhật Đặc điểm Cá nhân */}
            <CharacteristicsForm
                initialData={profile.characteristics}
                onUpdate={handleUpdate('characteristics', profile.characteristics, profileService.updateCharacteristics)}
            />

            {/* 3. Form Cập nhật Mục tiêu Dinh dưỡng */}
            <GoalsForm
                initialData={profile.goals}
                onUpdate={handleUpdate('goals', profile.goals, profileService.updateGoals)}
            />

        </div>
    );
}

// --- Component Form Đặc điểm Cá nhân ---
const ACTIVITY_LEVELS = [
    { value: 'SEDENTARY', label: 'Ít hoạt động (ngồi nhiều)' },
    { value: 'LIGHT', label: 'Hoạt động nhẹ (tập 1-3 lần/tuần)' },
    { value: 'MODERATE', label: 'Hoạt động vừa (tập 3-5 lần/tuần)' },
    { value: 'ACTIVE', label: 'Hoạt động nặng (tập hàng ngày)' },
];

const CharacteristicsForm = ({ initialData, onUpdate }) => {
    const [data, setData] = useState(initialData);

    // Đảm bảo cập nhật khi initialData thay đổi (từ props)
    useEffect(() => {
        setData(initialData);
    }, [initialData]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setData(prev => ({ ...prev, [name]: value }));
    };

    return (
        <form onSubmit={onUpdate(data)} className="bg-white p-6 rounded-xl shadow-lg mb-8 border border-gray-200">
            <h2 className="text-xl font-bold text-gray-700 mb-4 flex items-center">
                <Scale className="w-5 h-5 mr-2" />
                Đặc điểm Sinh học & Sức khỏe
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <InputField name="height" label="Chiều cao (cm)" type="number" value={data.height} onChange={handleChange} required />
                <InputField name="weight" label="Cân nặng (kg)" type="number" value={data.weight} onChange={handleChange} required />

                {/* Mức độ hoạt động */}
                <div>
                    <label className="block text-sm font-medium text-gray-700">Mức độ hoạt động</label>
                    <select
                        name="activityLevel"
                        value={data.activityLevel}
                        onChange={handleChange}
                        className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        required
                    >
                        {ACTIVITY_LEVELS.map(level => (
                            <option key={level.value} value={level.value}>{level.label}</option>
                        ))}
                    </select>
                </div>

                {/* Dị ứng */}
                <InputField name="allergies" label="Dị ứng (ghi rõ)" type="text" value={data.allergies} onChange={handleChange} placeholder="VD: Hải sản, đậu phộng" />

                {/* Bệnh nền */}
                <InputField name="underlyingDisease" label="Bệnh nền (ghi rõ)" type="text" value={data.underlyingDisease} onChange={handleChange} placeholder="VD: Tiểu đường, Cao huyết áp" />
            </div>

            <button
                type="submit"
                className="mt-6 px-4 py-2 text-white bg-green-600 rounded-lg font-semibold hover:bg-green-700 transition"
            >
                Cập nhật Đặc điểm
            </button>
        </form>
    );
};

// --- Component Form Mục tiêu Dinh dưỡng ---
const GOAL_TYPES = [
    { value: 'WEIGHT_LOSS', label: 'Giảm Cân' },
    { value: 'MUSCLE_GAIN', label: 'Tăng Cơ' },
    { value: 'MAINTAIN', label: 'Duy Trì Sức Khỏe' },
];

const GoalsForm = ({ initialData, onUpdate }) => {
    const [data, setData] = useState(initialData);

    useEffect(() => {
        setData(initialData);
    }, [initialData]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setData(prev => ({ ...prev, [name]: value }));
    };

    return (
        <form onSubmit={onUpdate(data)} className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">
            <h2 className="text-xl font-bold text-gray-700 mb-4 flex items-center">
                <Target className="w-5 h-5 mr-2" />
                Mục tiêu Dinh dưỡng
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {/* Loại mục tiêu */}
                <div>
                    <label className="block text-sm font-medium text-gray-700">Mục tiêu</label>
                    <select
                        name="goalType"
                        value={data.goalType}
                        onChange={handleChange}
                        className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        required
                    >
                        {GOAL_TYPES.map(goal => (
                            <option key={goal.value} value={goal.value}>{goal.label}</option>
                        ))}
                    </select>
                </div>

                {/* Calo mục tiêu */}
                <InputField name="targetDailyCalories" label="Calo Mục tiêu (kcal/ngày)" type="number" value={data.targetDailyCalories} onChange={handleChange} required />

                {/* Thời gian */}
                <InputField name="durationDays" label="Thời gian dự kiến (ngày)" type="number" value={data.durationDays} onChange={handleChange} required />
            </div>

            <button
                type="submit"
                className="mt-6 px-4 py-2 text-white bg-red-500 rounded-lg font-semibold hover:bg-red-600 transition"
            >
                Cập nhật Mục tiêu
            </button>
        </form>
    );
};

// Component Input Field tiện ích
const InputField = ({ label, name, type, value, onChange, placeholder = '', required = false }) => (
    <div>
        <label className="block text-sm font-medium text-gray-700">{label}</label>
        <input
            type={type}
            name={name}
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            required={required}
            className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
        />
    </div>
);

export default ProfileManagementPage;