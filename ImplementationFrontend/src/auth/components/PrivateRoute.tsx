import React, { ReactNode } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../AuthContext'; // Import useAuth

interface PrivateRouteProps {
  requiredRoles?: string[];
  children?: ReactNode; // Add children prop for nested routes
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ requiredRoles }) => {
  const { isAuthenticated, userRole } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRoles && userRole && !requiredRoles.includes(userRole)) {
    // Optionally, redirect to an unauthorized page or home page
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default PrivateRoute;
