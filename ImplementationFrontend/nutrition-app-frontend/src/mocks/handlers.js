import { http, HttpResponse } from 'msw';

// Đảm bảo BASE_URL khớp với axios instance trong src/services/api.js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const handlers = [
    // 1. Mock API Đăng nhập
    http.post(`${API_BASE_URL}/auth/login`, async ({ request }) => {
        const data = await request.json();
        if ((data.username === 'admin' && data.password === '123') || (data.username === 'testuser' && data.password === 'password')) {
            return HttpResponse.json({
                token: 'mock-test-token',
                user: { id: 'user1', username: data.username, roles: ['USER'] }
            }, { status: 200 });
        }
        return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 });
    }),

    // 2. Mock API lấy danh sách Thực phẩm
    http.get(`${API_BASE_URL}/foods`, () => {
        return HttpResponse.json([
            { id: 'f1', foodName: 'Gạo lứt', calories: 111, protein: 2.6 },
            { id: 'f2', foodName: 'Ức gà luộc', calories: 165, protein: 31 },
        ], { status: 200 });
    }),

    // 3. Mock API Thêm Thực phẩm (POST)
    http.post(`${API_BASE_URL}/foods`, async ({ request }) => {
        const newFood = await request.json();
        return HttpResponse.json({ ...newFood, id: Date.now().toString() }, { status: 201 });
    }),

    // Thêm các handlers khác cho API Quản trị viên, Tư vấn AI, v.v.
];