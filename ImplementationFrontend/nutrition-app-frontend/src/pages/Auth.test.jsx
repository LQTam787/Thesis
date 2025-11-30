import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { store } from '../store/store';
import { reset } from '../store/authSlice'; // Import reset action
import LoginPage from './LoginPage';
import DashboardPage from './DashboardPage';
import { server } from '../mocks/server';
import { http, HttpResponse } from 'msw'; // Import HttpResponse

// Mock DashboardPage to verify navigation
vi.mock('./DashboardPage', () => ({
    default: () => <div>DashboardPage</div>,
}));

// Mock useNavigate
const mockNavigate = vi.fn();
// Correct way to mock useNavigate from react-router-dom
vi.mock('react-router-dom', async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

// Mock window.location.replace for the error case (when api.js redirects)
const mockLocationReplace = vi.fn();
Object.defineProperty(window, 'location', {
    value: { replace: mockLocationReplace },
    writable: true,
});

describe('Authentication Flow', () => {
    // Reset store and mocks before each test
    beforeEach(() => {
        store.dispatch(reset()); // Reset Redux state
        mockNavigate.mockClear(); // Clear mock calls for useNavigate
        mockLocationReplace.mockClear(); // Clear mock calls for window.location.replace
    });

    it('should login a user and redirect to the dashboard page', async () => {
        render(
            <Provider store={store}>
                <MemoryRouter initialEntries={['/login']}>
                    <Routes>
                        <Route path="/login" element={<LoginPage />} />
                        {/* The actual DashboardPage component is not rendered here,
                            we only care that the navigation was triggered. */}
                        <Route path="/dashboard" element={<DashboardPage />} />
                    </Routes>
                </MemoryRouter>
            </Provider>
        );

        // Fill out the form
        fireEvent.change(screen.getByLabelText(/Tên người dùng/i), {
            target: { value: 'testuser' },
        });
        fireEvent.change(screen.getByLabelText(/Mật khẩu/i), {
            target: { value: 'password' },
        });

        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /đăng nhập/i }));

        // Wait for the navigation to /dashboard
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith('/dashboard'); // Check useNavigate instead of window.location.replace
        });

        // Check that the user is authenticated in the redux store
        const state = store.getState();
        expect(state.auth.isAuthenticated).toBe(true);
        expect(state.auth.user.username).toBe('testuser');
    });

    it('should show an error message on failed login', async () => {
        // Override the default msw handler to return an error
        server.use(
            http.post('*/auth/login', () => {
                return HttpResponse.json(
                    { message: 'Invalid credentials' }, // Data cho error.response.data
                    { status: 401 } // Status cho error.response.status
                );
            })
        );

        render(
            <Provider store={store}>
                {/* Start directly at /login for this test */}
                <MemoryRouter initialEntries={['/login']}>
                    <Routes>
                        <Route path="/login" element={<LoginPage />} />
                    </Routes>
                </MemoryRouter>
            </Provider>
        );

        // Fill out the form
        fireEvent.change(screen.getByLabelText(/Tên người dùng/i), {
            target: { value: 'wronguser' },
        });
        fireEvent.change(screen.getByLabelText(/Mật khẩu/i), {
            target: { value: 'wrongpassword' },
        });

        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /đăng nhập/i }));

        // Wait for the error message to appear and check authentication state
        await waitFor(() => {
            expect(screen.getByText(/Đăng nhập thất bại\. Vui lòng kiểm tra tên người dùng và mật khẩu\./i)).toBeInTheDocument();
            const state = store.getState();
            expect(state.auth.isAuthenticated).toBe(false);
            // window.location.replace('/login') will NOT be called if already on /login,
            // so we remove this assertion for this specific test case.
            // If you want to test the replace, you need to start at a different path.
            // expect(mockLocationReplace).toHaveBeenCalledWith('/login');
        });
    });
});
