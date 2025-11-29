import React from 'react';
import { Link, Outlet } from 'react-router-dom';

function AdminDashboardPage() {
  return (
    <div className="flex min-h-screen bg-gray-100">
      {/* Sidebar */}
      <aside className="w-64 bg-green-800 text-white shadow-lg flex flex-col">
        <div className="p-6 border-b border-green-700">
          <h2 className="text-3xl font-extrabold text-white">Admin Panel</h2>
        </div>
        <nav className="flex-grow p-4">
          <ul>
            <li className="mb-2">
              <Link
                to="/admin/users"
                className="flex items-center p-3 rounded-lg text-lg font-medium hover:bg-green-700 transition duration-200"
              >
                <svg className="w-6 h-6 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.653-.146-1.286-.423-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.653.146-1.286.423-1.857m0 0a5.002 5.002 0 019.154 0m-4.577-4.577a4 4 0 11-5.656 0 4 4 0 015.656 0z"></path></svg>
                User Management
              </Link>
            </li>
            <li className="mb-2">
              <Link
                to="/admin/foods"
                className="flex items-center p-3 rounded-lg text-lg font-medium hover:bg-green-700 transition duration-200"
              >
                <svg className="w-6 h-6 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V7m-4 0V5a2 2 0 00-2-2H9a2 2 0 00-2 2v2m5 0h.01M12 12h.01M12 16h.01M16 12h.01M16 16h.01"></path></svg>
                Food Data Management
              </Link>
            </li>
            <li className="mb-2">
              <Link
                to="/admin/ai-retrain"
                className="flex items-center p-3 rounded-lg text-lg font-medium hover:bg-green-700 transition duration-200"
              >
                <svg className="w-6 h-6 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 0 012 2v10m-6 0a2 2 0 002 2h2a2 0 002-2m0 0V5a2 2 0 012-2h2a2 0 012 2v14a2 2 0 01-2 2h-2a2 0 01-2-2z"></path></svg>
                AI Training Trigger
              </Link>
            </li>
            {/* Add more admin links here */}
          </ul>
        </nav>
        {/* Optional: Add a footer section similar to DashboardPage if needed */}
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8">
        <header className="flex justify-between items-center mb-10 bg-white p-6 rounded-xl shadow-md">
          <h1 className="text-4xl font-extrabold text-green-700">
            Welcome to Admin Dashboard!
          </h1>
          {/* You can add more admin-specific header info here */}
        </header>

        <section className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">System Overview</h2>
          <p className="text-gray-600 mb-4">
            This is the Admin Dashboard, where you can access various administrative functions through the sidebar on the left.
          </p>
          <div className="mt-4 p-3 bg-green-50 rounded-lg border border-green-200 text-sm text-green-800">
            Status: Admin panel is active.
          </div>
        </section>

        {/* Outlet to render child components based on the route */}
        <div className="mt-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}

export default AdminDashboardPage;
