// src/mocks/setupGlobalMocks.js

// Đảm bảo localStorage được định nghĩa sớm nhất có thể và không phụ thuộc vào bất kỳ import nào khác.
// Sử dụng hàm arrow function đơn giản thay cho function expression để đảm bảo tính đồng bộ.
global.localStorage = {
    store: {},
    getItem: key => global.localStorage.store[key] || null,
    setItem: (key, value) => { global.localStorage.store[key] = String(value); },
    removeItem: key => { delete global.localStorage.store[key]; },
    clear: () => { global.localStorage.store = {}; }
};

// Console warning cho biết đã chạy
console.log('Global localStorage mock applied.');