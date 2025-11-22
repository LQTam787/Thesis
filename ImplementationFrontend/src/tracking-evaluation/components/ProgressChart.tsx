import React from 'react';
import { ProgressChartData } from '../types';

interface ProgressChartProps {
    data: ProgressChartData;
}

const ProgressChart: React.FC<ProgressChartProps> = ({ data }) => {
    // This is a placeholder for a chart component.
    // In a real application, you would integrate a charting library like Chart.js (via react-chartjs-2)
    // or Recharts here to render interactive charts.
    // For demonstration, we'll just display the data in a basic format.

    return (
        <div className="bg-white p-4 rounded-lg shadow-sm">
            <h3 className="text-lg font-semibold mb-3">Biểu đồ tiến độ</h3>
            <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ngày</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Calo</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Protein (g)</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Carb (g)</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fat (g)</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {data.labels.map((label, index) => (
                            <tr key={index}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{label}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{data.calories[index]}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{data.protein[index]}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{data.carb[index]}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{data.fat[index]}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ProgressChart;
