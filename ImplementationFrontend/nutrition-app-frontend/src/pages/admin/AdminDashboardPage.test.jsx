import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import AdminDashboardPage from './AdminDashboardPage';
import { describe, it, expect } from 'vitest';

describe('AdminDashboardPage', () => {
    const setup = () => {
        render(
            <Router>
                <AdminDashboardPage />
            </Router>
        );
    };

    it('should render the page title correctly', () => {
        setup();
        const pageTitle = screen.getByText('Admin Panel');
        expect(pageTitle).toBeInTheDocument();
    });

    it('should render the welcome message correctly', () => {
        setup();
        const welcomeMessage = screen.getByText('Welcome to Admin Dashboard!');
        expect(welcomeMessage).toBeInTheDocument();
    });

    it('should render the sidebar with correct links', () => {
        setup();
        const userManagementLink = screen.getByRole('link', { name: /User Management/i });
        const foodManagementLink = screen.getByRole('link', { name: /Food Data Management/i });
        const aiTrainingLink = screen.getByRole('link', { name: /AI Training Trigger/i });

        expect(userManagementLink).toBeInTheDocument();
        expect(foodManagementLink).toBeInTheDocument();
        expect(aiTrainingLink).toBeInTheDocument();
    });

    it('should render the system overview title correctly', () => {
        setup();
        const systemOverviewTitle = screen.getByText('System Overview');
        expect(systemOverviewTitle).toBeInTheDocument();
    });

    it('should render the system status correctly', () => {
        setup();
        const systemStatus = screen.getByText('Status: Admin panel is active.');
        expect(systemStatus).toBeInTheDocument();
    });

    // it('should render the outlet for nested routes', () => {
    //     setup();
    //     // Assuming there is a rendered outlet, we can check the presence of its container div
    //     const outletContainer = screen.getByRole('region', { hidden: true });
    //     expect(outletContainer).toBeInTheDocument();
    // });

    it('should apply correct styles to elements', () => {
        setup();
        const adminPanelTitle = screen.getByText('Admin Panel');
        expect(adminPanelTitle).toHaveClass('text-3xl font-extrabold text-white');

        const systemStatus = screen.getByText('Status: Admin panel is active.');
        expect(systemStatus).toHaveClass('p-3 bg-green-50 rounded-lg border border-green-200 text-sm text-green-800');
    });
});
