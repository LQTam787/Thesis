// src/App.jsx (Cập nhật)
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage'; // Tạo file này (tạm thời)
import PrivateRoute from './components/PrivateRoute';
import { useDispatch } from 'react-redux';
import { logout, setCredentials, setLoading } from './store/authSlice';
import authService from './services/authService';
import NutritionAdvicePage from './pages/NutritionAdvicePage';
import NutritionPlanPage from './pages/NutritionPlanPage';
import NutritionPlanDetailPage from './pages/NutritionPlanDetailPage';
import RecipeDetail from './components/RecipeDetail'; // Lưu ý: RecipeDetail là component, không phải page

// Component Tạm thời cho Dashboard
const TempDashboard = () => {
    const dispatch = useDispatch();

    // Hàm Đăng xuất
    const handleLogout = () => {
        authService.performLogout();
    };

    return (
        <div className="p-8">
            <h1 className="text-4xl font-bold text-green-600 mb-4">Trang Quản lý (Dashboard)</h1>
            <p className="mb-4">Bạn đã đăng nhập thành công!</p>
            <button
                onClick={handleLogout}
                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
            >
                Đăng Xuất
            </button>
        </div>
    );
};
// Tạm thời tạo file src/pages/DashboardPage.jsx với nội dung trên (hoặc giữ nguyên TempDashboard)


function App() {
    // Logic khởi tạo: Kiểm tra token khi ứng dụng khởi động
    const dispatch = useDispatch();

    React.useEffect(() => {
        const token = localStorage.getItem('token');

        if (token) {
            // Trong ứng dụng thực tế, bạn sẽ cần gọi API để xác thực token này
            // và lấy thông tin người dùng (vd: dispatch(checkTokenAndFetchUser(token)))
            // Tạm thời, chúng ta sẽ giả định token hợp lệ và đánh dấu đã đăng nhập
            dispatch(setCredentials({ token, user: { username: 'user_temp', role: 'USER' } }));
        }

        // Kết thúc trạng thái tải
        dispatch(setLoading(false));
    }, [dispatch]);

    return (
        <BrowserRouter>
            <Routes>
                {/* Public Routes */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />


                <Route path="/dashboard" element={<TempDashboard />} />
                {/* THÊM ROUTE NÀY */}
                <Route path="/advice" element={<NutritionAdvicePage />} />
                {/* Thêm các route cần bảo vệ khác tại đây */}
                <Route path="/plans" element={<NutritionPlanPage />} />
                <Route path="/plans/:planId" element={<NutritionPlanDetailPage />} />
                <Route path="/recipes/:recipeId" element={<RecipeDetail />} />
                <Route path="/" element={<Navigate to="/dashboard" replace />} />


                {/* Private Routes (Cần đăng nhập) */}

                {/*<Route element={<PrivateRoute />}>*/}
                {/*    <Route path="/dashboard" element={<TempDashboard />} />*/}
                {/*    /!* THÊM ROUTE NÀY *!/*/}
                {/*    <Route path="/advice" element={<NutritionAdvicePage />} />*/}
                {/*    /!* Thêm các route cần bảo vệ khác tại đây *!/*/}
                {/*    <Route path="/plans" element={<NutritionPlanPage />} />*/}
                {/*    <Route path="/plans/:planId" element={<NutritionPlanDetailPage />} />*/}
                {/*    <Route path="/recipes/:recipeId" element={<RecipeDetail />} />*/}
                {/*    <Route path="/" element={<Navigate to="/dashboard" replace />} />*/}
                {/*</Route>*/}

                {/* Catch-all route cho trang 404 hoặc chuyển hướng */}
                <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;