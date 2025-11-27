import '@testing-library/jest-dom'; // Thêm các matcher của jest-dom
import { server } from './mocks/server'; // Import server MSW đã được setup

// Khởi động server MSW trước khi chạy tất cả các test
beforeAll(() => server.listen());

// Reset các mock handlers sau mỗi test để đảm bảo tính độc lập
afterEach(() => server.resetHandlers());

// Đóng server MSW sau khi tất cả test đã chạy xong
afterAll(() => server.close());