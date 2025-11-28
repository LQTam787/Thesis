// src/store/__tests__/authSlice.test.js

import authReducer, {
    setCredentials,
    logout,
    setLoading
} from '../authSlice';
import { describe, test, expect, vi, beforeEach, afterEach } from 'vitest';

// Dữ liệu mẫu để sử dụng trong bài kiểm thử
const mockUser = { id: 1, username: 'tester', role: 'USER' };
const mockToken = 'jwt-token-for-tester-12345';
const mockPayload = { user: mockUser, token: mockToken };

// Trạng thái đã đăng nhập mẫu để sử dụng trong các bài test logout
const loggedInState = {
    user: mockUser,
    token: mockToken,
    isAuthenticated: true,
    isLoading: false, // Giả định đã hoàn tất kiểm tra lúc khởi động
};

// --- Mocks ---
// 1. Giả lập localStorage
// Sử dụng vi.spyOn để theo dõi (spy) các cuộc gọi đến localStorage
let localStorageSetItemSpy;
let localStorageRemoveItemSpy;

describe('authSlice Reducers', () => {

    beforeEach(() => {
        // Thiết lập spy trước mỗi bài test
        localStorageSetItemSpy = vi.spyOn(localStorage, 'setItem').mockImplementation(() => {});
        localStorageRemoveItemSpy = vi.spyOn(localStorage, 'removeItem').mockImplementation(() => {});
    });

    afterEach(() => {
        // Xóa các spy sau mỗi bài test
        vi.restoreAllMocks();
    });

    // --- TEST 1: Kiểm tra Trạng thái Khởi tạo (Initial State) ---
    test('nên trả về trạng thái khởi tạo mặc định khi chạy reducer lần đầu', () => {
        const newState = authReducer(undefined, { type: '@@INIT' });

        expect(newState).toEqual({
            user: null,
            token: null,
            isAuthenticated: false,
            isLoading: true, // Trạng thái này là mặc định khi khởi động
        });
    });

    // --- TEST 2: Kiểm tra setCredentials (Đăng nhập) ---
    test('nên lưu thông tin đăng nhập và cập nhật isAuthenticated', () => {
        // 1. Gọi reducer với action setCredentials
        const newState = authReducer(undefined, setCredentials(mockPayload));

        // 2. Kiểm tra trạng thái Redux mới
        expect(newState.user).toEqual(mockUser);
        expect(newState.token).toBe(mockToken);
        expect(newState.isAuthenticated).toBe(true);
        expect(newState.isLoading).toBe(true); // isLoading không bị ảnh hưởng

        // 3. Kiểm tra localStorage
        expect(localStorageSetItemSpy).toHaveBeenCalledTimes(1);
        expect(localStorageSetItemSpy).toHaveBeenCalledWith('token', mockToken);
        expect(localStorageRemoveItemSpy).not.toHaveBeenCalled();
    });

    // --- TEST 3: Kiểm tra logout (Đăng xuất) ---
    test('nên xóa thông tin đăng nhập và cập nhật isAuthenticated/localStorage', () => {
        // 1. Gọi reducer với action logout, sử dụng trạng thái đã đăng nhập
        const newState = authReducer(loggedInState, logout());

        // 2. Kiểm tra trạng thái Redux mới
        expect(newState.user).toBeNull();
        expect(newState.token).toBeNull();
        expect(newState.isAuthenticated).toBe(false);
        expect(newState.isLoading).toBe(false); // isLoading không bị ảnh hưởng

        // 3. Kiểm tra localStorage
        expect(localStorageRemoveItemSpy).toHaveBeenCalledTimes(1);
        expect(localStorageRemoveItemSpy).toHaveBeenCalledWith('token');
        expect(localStorageSetItemSpy).not.toHaveBeenCalled();
    });

    // --- TEST 4: Kiểm tra setLoading (Cập nhật trạng thái tải) ---
    test('nên cập nhật trạng thái isLoading thành false', () => {
        // Bắt đầu từ trạng thái mặc định (isLoading: true)
        const stateFalse = authReducer(undefined, setLoading(false));
        expect(stateFalse.isLoading).toBe(false);

        // Bắt đầu từ trạng thái false và cập nhật thành true
        const stateTrue = authReducer(stateFalse, setLoading(true));
        expect(stateTrue.isLoading).toBe(true);

        // Đảm bảo các trường khác không bị ảnh hưởng
        expect(stateTrue.user).toBeNull();
    });
});