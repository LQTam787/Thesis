import React from 'react';
import { NutritionReportData } from '../types';

interface NutritionReportProps {
    data: NutritionReportData;
}

const NutritionReport: React.FC<NutritionReportProps> = ({ data }) => {
    return (
        <div className="bg-white p-4 rounded-lg shadow-sm">
            <h3 className="text-lg font-semibold mb-3">Báo cáo dinh dưỡng</h3>
            <div className="space-y-2 text-gray-700">
                <p><strong>Cân nặng hiện tại:</strong> {data.currentWeight} kg</p>
                <p><strong>Chỉ số cơ thể (BMI):</strong> {data.bmi}</p>
                <p><strong>Phần trăm mục tiêu đạt được:</strong> {data.targetAchievementPercentage}%</p>
                <p><strong>Macro dinh dưỡng đã tiêu thụ:</strong></p>
                <ul className="list-disc pl-5">
                    <li>Protein: {data.macroNutrientsConsumed.protein} g</li>
                    <li>Carb: {data.macroNutrientsConsumed.carb} g</li>
                    <li>Fat: {data.macroNutrientsConsumed.fat} g</li>
                </ul>
                <p><strong>Micro dinh dưỡng đã tiêu thụ:</strong></p>
                <ul className="list-disc pl-5">
                    {Object.entries(data.microNutrientsConsumed).map(([key, value]) => (
                        <li key={key}>{key}: {value}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default NutritionReport;
