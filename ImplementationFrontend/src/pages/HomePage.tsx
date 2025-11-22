import React from 'react';
import { useNavigate } from 'react-router-dom';
import NutritionalConsultation from '../components/NutritionalConsultation';
import authService from '../auth/services/authService';

const HomePage: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-100 py-8">
      <button
        onClick={handleLogout}
        className="absolute top-4 right-4 px-4 py-2 text-white bg-red-600 rounded-lg hover:bg-red-900"
      >
        Logout
      </button>
      <NutritionalConsultation />
    </div>
  );
};

export default HomePage;
