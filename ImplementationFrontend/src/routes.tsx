import React from 'react';
import { RouteObject } from 'react-router-dom';
import NutritionPlanList from './components/NutritionPlanList';
import NutritionPlanForm from './components/NutritionPlanForm';
import NutritionPlanDetail from './components/NutritionPlanDetail';
import HomePage from './pages/HomePage';
import LoginPage from './auth/LoginPage';
import RegisterPage from './auth/RegisterPage';
import PrivateRoute from './auth/PrivateRoute';
import DailyLogInputPage from './tracking-evaluation/pages/DailyLogInputPage';
import ProgressReportPage from './tracking-evaluation/pages/ProgressReportPage';
import SharePlanActivityPage from './tracking-evaluation/pages/SharePlanActivityPage';
import ViewSharedContentPage from './tracking-evaluation/pages/ViewSharedContentPage';
import SharedContentManagementPage from './admin/pages/SharedContentManagementPage';
import ProfileManagementPage from './profile-management/pages/ProfileManagementPage';
import AdminDashboardPage from './admin/pages/AdminDashboardPage';
import UserManagementPage from './admin/pages/UserManagementPage';

const routes: RouteObject[] = [
  {
    path: '/',
    element: <HomePage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/register',
    element: <RegisterPage />,
  },
  {
    element: <PrivateRoute />,
    children: [
      {
        path: '/nutrition-plans',
        element: <NutritionPlanList />,
      },
      {
        path: '/nutrition-plans/new',
        element: <NutritionPlanForm />,
      },
      {
        path: '/nutrition-plans/:id',
        element: <NutritionPlanDetail />,
      },
      {
        path: '/nutrition-plans/:id/edit',
        element: <NutritionPlanForm />,
      },
      {
        path: '/daily-log-input',
        element: <DailyLogInputPage />,
      },
      {
        path: '/progress-report',
        element: <ProgressReportPage />,
      },
      {
        path: '/share-plan-activity',
        element: <SharePlanActivityPage />,
      },
      {
        path: '/view-shared-content',
        element: <ViewSharedContentPage />,
      },
      {
        path: '/profile',
        element: <ProfileManagementPage />,
      },
      {
        path: '/admin/dashboard',
        element: <AdminDashboardPage />,
      },
      {
        path: '/admin/users',
        element: <UserManagementPage />,
  },
  {
        path: '/admin/shared-content',
    element: <SharedContentManagementPage />,
      },
    ],
  },
];

export default routes;
