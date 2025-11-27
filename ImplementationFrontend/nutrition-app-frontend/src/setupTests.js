// src/setupTests.js (Nội dung mới)

import { beforeAll, afterEach, afterAll } from 'vitest';
import '@testing-library/jest-dom';

// 1. Setup Mock Service Worker (MSW)
// Bây giờ import này sẽ chạy sau khi setupGlobalMocks.js đã định nghĩa localStorage
import { server } from './mocks/server';

// 2. Thiết lập lifecycle của MSW
beforeAll(() => server.listen({ onUnhandledRequest: 'warn' }));
afterEach(() => server.resetHandlers());
afterAll(() => server.close());