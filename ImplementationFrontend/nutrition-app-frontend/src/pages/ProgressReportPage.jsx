// src/pages/ProgressReportPage.jsx
import React, { useState, useEffect } from 'react';
import logService from '../services/logService';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Activity, Clock } from 'lucide-react';

function ProgressReportPage() {
    const [reportData, setReportData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [range, setRange] = useState('7days'); // Tùy chọn xem: 7 ngày, 30 ngày

    useEffect(() => {
        const fetchReport = async () => {
            setLoading(true);
            setError(null);
            try {
                // Tính toán ngày bắt đầu/kết thúc dựa trên `range`
                const endDate = new Date().toISOString().slice(0, 10);
                let startDate = new Date();
                if (range === '7days') {
                    startDate.setDate(startDate.getDate() - 7);
                } else if (range === '30days') {
                    startDate.setDate(startDate.getDate() - 30);
                }
                const startDateString = startDate.toISOString().slice(0, 10);

                const data = await logService.getProgressData(startDateString, endDate);
                setReportData(data);

            } catch (err) {
                setError('Không thể tải dữ liệu báo cáo. Vui lòng thử lại.');
            } finally {
                setLoading(false);
            }
        };
        fetchReport();
    }, [range]);


    return (
        <>
            <header className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-800 flex items-center">
                    <Activity className="w-7 h-7 mr-3 text-green-600" />
                    Báo cáo & Theo dõi Tiến độ
                </h1>
                <div className="flex items-center space-x-2">
                    <Clock className="w-5 h-5 text-gray-500" />
                    <select
                        value={range}
                        onChange={(e) => setRange(e.target.value)}
                        className="px-3 py-2 border border-gray-300 rounded-lg text-sm"
                        disabled={loading}
                    >
                        <option value="7days">7 Ngày qua</option>
                        <option value="30days">30 Ngày qua</option>
                    </select>
                </div>
            </header>

            {loading && <div className="text-center p-8">Đang tải dữ liệu báo cáo...</div>}
            {error && <div className="text-center p-8 text-red-600">{error}</div>}

            {!loading && !error && (
                <div className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">
                    <h2 className="text-xl font-semibold mb-4 text-gray-700">Biểu đồ Lượng Calo Tiêu thụ vs. Mục tiêu</h2>

                    <ResponsiveContainer width="100%" height={400}>
                        <LineChart data={reportData} margin={{ top: 5, right: 20, left: 0, bottom: 5 }}>
                            <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                            <XAxis dataKey="date" />
                            <YAxis label={{ value: 'Calo (kcal)', angle: -90, position: 'insideLeft' }} />
                            <Tooltip />
                            <Legend />
                            {/* Lượng calo đã ghi */}
                            <Line
                                type="monotone"
                                dataKey="loggedCalories"
                                stroke="#10B981"
                                name="Calo Đã Tiêu Thụ"
                                activeDot={{ r: 8 }}
                                strokeWidth={2}
                            />
                            {/* Mục tiêu calo */}
                            <Line
                                type="monotone"
                                dataKey="targetCalories"
                                stroke="#F59E0B"
                                name="Mục Tiêu Hàng Ngày"
                                strokeDasharray="5 5"
                                strokeWidth={2}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            )}

            {/* Các báo cáo hoặc biểu đồ khác (ví dụ: Macro-nutrients chart) sẽ được thêm vào đây */}
        </>
    );
}

export default ProgressReportPage;