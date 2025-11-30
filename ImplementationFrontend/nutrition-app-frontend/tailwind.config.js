// tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
    // Cấu hình `content` để bao gồm tất cả các file JSX/TSX/JS trong thư mục src
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}