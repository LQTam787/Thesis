// src/pages/NutritionPlanDetailPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import planService from '../services/planService';
import { ArrowLeft, Clock, Zap, Soup, Utensils } from 'lucide-react';

function NutritionPlanDetailPage() {
    const { planId } = useParams(); // Lấy planId từ URL (ví dụ: /plans/123)
    const [plan, setPlan] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPlan = async () => {
            try {
                const data = await planService.getPlanDetails(planId);
                setPlan(data);
            } catch (err) {
                setError(`Không tìm thấy kế hoạch với ID: ${planId} hoặc lỗi tải dữ liệu.`);
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchPlan();
    }, [planId]);

    if (loading) {
        return <div className="p-8 text-center text-lg">Đang tải chi tiết kế hoạch...</div>;
    }

    if (error) {
        return <div className="p-8 text-center text-red-600">{error}</div>;
    }

    // Dữ liệu giả định cho cấu trúc của plan
    // plan.dailyPlans: [ {day: 1, meals: [{name: 'Bữa sáng', recipe: {id: 1, name: 'Trứng ốp la'}}, ...]} ]

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-8">
            <Link to="/plans" className="flex items-center text-green-600 hover:text-green-800 mb-6">
                <ArrowLeft className="w-5 h-5 mr-2" />
                Quay lại Danh sách Kế hoạch
            </Link>

            <h1 className="text-4xl font-extrabold text-gray-900 mb-2">{plan.name}</h1>
            <p className="text-lg text-gray-600 mb-6">{plan.description || 'Chi tiết về kế hoạch dinh dưỡng cá nhân.'}</p>

            {/* Thông tin Tóm tắt */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-10">
                <div className="bg-white p-5 rounded-xl shadow-md border border-green-100">
                    <Zap className="w-6 h-6 text-yellow-500 mb-2" />
                    <p className="text-sm text-gray-500">Tổng Calo Mục tiêu (hàng ngày)</p>
                    <p className="text-2xl font-bold text-green-700">{plan.targetDailyCalories} kcal</p>
                </div>
                <div className="bg-white p-5 rounded-xl shadow-md border border-green-100">
                    <Clock className="w-6 h-6 text-blue-500 mb-2" />
                    <p className="text-sm text-gray-500">Thời gian Kế hoạch</p>
                    <p className="text-2xl font-bold text-green-700">{plan.durationDays} ngày</p>
                </div>
                <div className="bg-white p-5 rounded-xl shadow-md border border-green-100">
                    <Utensils className="w-6 h-6 text-red-500 mb-2" />
                    <p className="text-sm text-gray-500">Mục tiêu Chính</p>
                    <p className="text-2xl font-bold text-green-700">{plan.goal === 'WEIGHT_LOSS' ? 'Giảm Cân' : 'Tăng Cơ'}</p>
                </div>
            </div>

            {/* Lịch trình Bữa ăn hàng ngày */}
            <section>
                <h2 className="text-2xl font-bold text-gray-800 mb-4 border-b pb-2">Lịch trình Chi tiết Bữa ăn</h2>

                {/* Lặp qua các ngày trong kế hoạch */}
                {plan.dailyPlans && plan.dailyPlans.map((dailyPlan) => (
                    <div key={dailyPlan.day} className="mb-8 p-6 bg-white rounded-xl shadow-lg border border-gray-100">
                        <h3 className="text-xl font-bold text-green-600 mb-4">Ngày {dailyPlan.day}</h3>
                        <div className="space-y-4">
                            {/* Lặp qua các bữa ăn trong ngày */}
                            {dailyPlan.meals.map((meal, index) => (
                                <div key={index} className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                                    <div className="flex items-center">
                                        <Soup className="w-5 h-5 mr-3 text-green-500" />
                                        <div>
                                            <p className="font-semibold text-gray-800">{meal.name}</p>
                                            <p className="text-sm text-gray-500">{meal.recipe.name}</p>
                                        </div>
                                    </div>
                                    <Link
                                        to={`/recipes/${meal.recipe.id}`}
                                        className="text-sm font-medium text-blue-600 hover:text-blue-800"
                                    >
                                        Xem Công thức
                                    </Link>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </section>
        </div>
    );
}

export default NutritionPlanDetailPage;