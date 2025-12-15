// src/App.jsx (Cập nhật)
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';
import { useDispatch } from 'react-redux';
import { setCredentials, setLoading } from './store/authSlice';
import NutritionAdvicePage from './pages/NutritionAdvicePage';
import NutritionPlanPage from './pages/NutritionPlanPage';
import NutritionPlanDetailPage from './pages/NutritionPlanDetailPage';
import RecipeDetail from './components/RecipeDetail';
import DailyLogInputPage from './pages/DailyLogInputPage';
import ProgressReportPage from './pages/ProgressReportPage';
import CommunityFeedPage from './pages/CommunityFeedPage';
import ProfileManagementPage from './pages/ProfileManagementPage';
import AdminRoute from './components/AdminRoute';
import AdminDashboardPage from "./pages/admin/AdminDashboardPage";
import UserManagementPage from './pages/admin/UserManagementPage';
import FoodDataManagementPage from './pages/admin/FoodDataManagementPage';
import AITrainingTriggerPage from './pages/admin/AITrainingTriggerPage';
import MainLayout from './layouts/MainLayout';

function App() {
    const dispatch = useDispatch();

    React.useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            dispatch(setCredentials({ token, user: { username: 'user_temp', role: 'USER' } }));
        }
        dispatch(setLoading(false));
    }, [dispatch]);

    return (
        <BrowserRouter>
            <Routes>
                {/* Public Routes */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Private Routes with MainLayout */}
                <Route element={<PrivateRoute />}>
                    <Route element={<MainLayout />}>
                        <Route path="/dashboard" element={<DashboardPage />} />
                        <Route path="/advice" element={<NutritionAdvicePage />} />
                        <Route path="/plans" element={<NutritionPlanPage />} />
                        <Route path="/plans/:planId" element={<NutritionPlanDetailPage />} />
                        <Route path="/recipes/:recipeId" element={<RecipeDetail />} />
                        <Route path="/log" element={<DailyLogInputPage />} />
                        <Route path="/report" element={<ProgressReportPage />} />
                        <Route path="/community" element={<CommunityFeedPage />} />
                        <Route path="/profile" element={<ProfileManagementPage />} />
                        <Route path="/" element={<Navigate to="/dashboard" replace />} />
                    </Route>
                </Route>

                {/* Admin Routes */}
                <Route element={<AdminRoute />}>
                    <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
                    <Route path="/admin/users" element={<UserManagementPage />} />
                    <Route path="/admin/foods" element={<FoodDataManagementPage />} />
                    <Route path="/admin/ai-retrain" element={<AITrainingTriggerPage />} />
                </Route>

                {/* Catch-all route */}
                <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;