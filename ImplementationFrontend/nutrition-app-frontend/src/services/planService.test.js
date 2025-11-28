// src/services/planService.test.js

import { describe, it, expect, beforeAll, afterEach, afterAll } from 'vitest';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import planService from './planService';

// 1. Cài đặt Mock Server với http và HttpResponse
const server = setupServer(
    // Mock cho getAllPlans
    http.get('http://localhost:8080/api/plans', () => {
        return HttpResponse.json([{ id: 1, name: 'Kế hoạch giảm cân' }], { status: 200 });
    }),

    // Mock cho getPlanDetails (thành công)
    http.get('http://localhost:8080/api/plans/1', () => {
        return HttpResponse.json({ id: 1, name: 'Chi tiết kế hoạch' }, { status: 200 });
    }),
    // Mock cho getPlanDetails (thất bại)
    http.get('http://localhost:8080/api/plans/99', () => {
        return HttpResponse.json({ message: 'Plan not found' }, { status: 404 });
    }),

    // Mock cho createPlan (thành công)
    http.post('http://localhost:8080/api/plans', async ({ request }) => {
        const newPlan = await request.json();
        return HttpResponse.json({ id: 2, ...newPlan }, { status: 201 });
    }),

    // Mock cho getRecipeDetail (thành công)
    http.get('http://localhost:8080/api/recipes/101', () => {
        return HttpResponse.json({ id: 101, name: 'Công thức phở bò' }, { status: 200 });
    }),
    // Mock cho getRecipeDetail (thất bại)
    http.get('http://localhost:8080/api/recipes/999', () => {
        return HttpResponse.json({ message: 'Recipe not found' }, { status: 404 });
    })
);

// Bật mock server trước khi chạy tests
beforeAll(() => server.listen());

// Reset handlers sau mỗi test
afterEach(() => server.resetHandlers());

// Tắt mock server sau khi chạy xong
afterAll(() => server.close());


describe('planService', () => {
    // --- Tests for getAllPlans ---
    it('getAllPlans: nên trả về danh sách kế hoạch khi thành công', async () => {
        const plans = await planService.getAllPlans();
        expect(plans).toEqual([{ id: 1, name: 'Kế hoạch giảm cân' }]);
    });

    it('getAllPlans: nên ném ra lỗi khi API trả về lỗi server', async () => {
        server.use(
            http.get('http://localhost:8080/api/plans', () => {
                return HttpResponse.json({ message: 'Internal Server Error' }, { status: 500 });
            })
        );
        await expect(planService.getAllPlans()).rejects.toThrow();
    });

    it('getAllPlans: nên ném ra lỗi khi có lỗi mạng', async () => {
        server.use(
            http.get('http://localhost:8080/api/plans', () => {
                return HttpResponse.error(); // Giả lập lỗi mạng
            })
        );
        await expect(planService.getAllPlans()).rejects.toThrow();
    });

    // --- Tests for getPlanDetails ---
    it('getPlanDetails: nên trả về chi tiết kế hoạch khi thành công', async () => {
        const planDetails = await planService.getPlanDetails(1);
        expect(planDetails).toEqual({ id: 1, name: 'Chi tiết kế hoạch' });
    });

    it('getPlanDetails: nên ném ra lỗi khi planId không tồn tại', async () => {
        await expect(planService.getPlanDetails(99)).rejects.toThrow();
    });

    it('getPlanDetails: nên ném ra lỗi khi có lỗi mạng', async () => {
        server.use(
            http.get('http://localhost:8080/api/plans/1', () => {
                return HttpResponse.error();
            })
        );
        await expect(planService.getPlanDetails(1)).rejects.toThrow();
    });

    // --- Tests for createPlan ---
    it('createPlan: nên trả về kế hoạch đã tạo khi thành công', async () => {
        const newPlanData = { name: 'Kế hoạch mới' };
        const createdPlan = await planService.createPlan(newPlanData);
        expect(createdPlan).toEqual({ id: 2, name: 'Kế hoạch mới' });
    });

    it('createPlan: nên ném ra lỗi khi API trả về lỗi client', async () => {
        server.use(
            http.post('http://localhost:8080/api/plans', () => {
                return HttpResponse.json({ message: 'Invalid data' }, { status: 400 });
            })
        );
        await expect(planService.createPlan({})).rejects.toThrow();
    });

    it('createPlan: nên ném ra lỗi khi có lỗi mạng', async () => {
        server.use(
            http.post('http://localhost:8080/api/plans', () => {
                return HttpResponse.error();
            })
        );
        await expect(planService.createPlan({})).rejects.toThrow();
    });

    // --- Tests for getRecipeDetail ---
    it('getRecipeDetail: nên trả về chi tiết công thức khi thành công', async () => {
        const recipeDetails = await planService.getRecipeDetail(101);
        expect(recipeDetails).toEqual({ id: 101, name: 'Công thức phở bò' });
    });

    it('getRecipeDetail: nên ném ra lỗi khi recipeId không tồn tại', async () => {
        await expect(planService.getRecipeDetail(999)).rejects.toThrow();
    });

    it('getRecipeDetail: nên ném ra lỗi khi có lỗi mạng', async () => {
        server.use(
            http.get('http://localhost:8080/api/recipes/101', () => {
                return HttpResponse.error();
            })
        );
        await expect(planService.getRecipeDetail(101)).rejects.toThrow();
    });
});