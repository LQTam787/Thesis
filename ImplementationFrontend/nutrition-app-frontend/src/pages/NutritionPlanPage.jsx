// src/pages/NutritionPlanPage.jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import planService from '../services/planService';
import { PlusCircle, Search, Calendar, Heart } from 'lucide-react';

// Component hiển thị thẻ Kế hoạch
const PlanCard = ({ plan }) => {
    const getGoalText = (plan) => {
        if (plan.goal === 'WEIGHT_LOSS') return 'Giảm Cân';
        if (plan.goal === 'MUSCLE_GAIN') return 'Tăng Cơ';
        return 'Duy Trì Sức Khỏe';
    };

    return (
        <Link to={`/plans/${plan.id}`} className="block bg-white border border-gray-200 rounded-xl shadow-md hover:shadow-lg transition duration-200 overflow-hidden">
            <div className="p-4">
                <h3 className="text-xl font-bold text-green-700 mb-1 truncate">{plan.name}</h3>
                <p className={`text-sm font-semibold mb-3 ${plan.isActive ? 'text-blue-600' : 'text-gray-500'}`}>
                    {plan.isActive ? 'Kế hoạch Hiện tại' : 'Đã Hoàn thành'}
                </p>
                <div className="flex items-center text-sm text-gray-600 space-x-4">
                    <p className="flex items-center">
                        <Heart className="w-4 h-4 mr-1 text-red-500" />
                        Mục tiêu: {getGoalText(plan)}
                    </p>
                    <p className="flex items-center">
                        <Calendar className="w-4 h-4 mr-1 text-yellow-600" />
                        Thời hạn: {plan.durationDays} ngày
                    </p>
                </div>
            </div>
        </Link>
    );
};

function NutritionPlanPage() {
    const [plans, setPlans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPlans = async () => {
            try {
                const data = await planService.getAllPlans();
                setPlans(data);
            } catch (err) {
                setError('Không thể tải danh sách kế hoạch. Vui lòng thử lại.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchPlans();
    }, []);

    if (loading) {
        return <div className="p-8 text-center text-lg">Đang tải kế hoạch...</div>;
    }

    if (error) {
        return <div className="p-8 text-center text-red-600">{error}</div>;
    }

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-8">
            <header className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-800">Quản lý Kế hoạch Dinh dưỡng</h1>
                <Link
                    to="/plans/new" // Route này sẽ được tạo sau cho form tạo kế hoạch
                    className="flex items-center px-4 py-2 text-sm font-semibold text-white bg-green-600 rounded-lg shadow-md hover:bg-green-700 transition duration-150"
                >
                    <PlusCircle className="w-5 h-5 mr-2" />
                    Tạo Kế hoạch Mới
                </Link>
            </header>

            {plans.length === 0 ? (
                <div className="text-center p-10 border-2 border-dashed border-gray-300 rounded-xl bg-white">
                    <p className="text-lg text-gray-600">Bạn chưa có kế hoạch dinh dưỡng nào.</p>
                    <p className="text-sm text-gray-500 mt-2">Hãy bắt đầu bằng cách tạo một kế hoạch mới!</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {plans.map((plan) => (
                        <PlanCard key={plan.id} plan={plan} />
                    ))}
                </div>
            )}
        </div>
    );
}

export default NutritionPlanPage;