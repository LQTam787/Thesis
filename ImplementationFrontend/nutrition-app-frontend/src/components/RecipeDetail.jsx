// src/components/RecipeDetail.jsx
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import planService from '../services/planService';
import { FlaskConical, CookingPot, Utensils, Heart } from 'lucide-react';

function RecipeDetail() {
    const { recipeId } = useParams(); // Lấy recipeId từ URL (ví dụ: /recipes/456)
    const [recipe, setRecipe] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const data = await planService.getRecipeDetail(recipeId);
                setRecipe(data);
            } catch (err) {
                setError(`Không tìm thấy công thức với ID: ${recipeId} hoặc lỗi tải dữ liệu.`);
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchRecipe();
    }, [recipeId]);

    if (loading) {
        return <div className="p-8 text-center text-lg">Đang tải công thức...</div>;
    }

    if (error) {
        return <div className="p-8 text-center text-red-600">{error}</div>;
    }

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-8">
            <h1 className="text-4xl font-extrabold text-green-700 mb-2">{recipe.recipeName}</h1>
            <p className="text-lg text-gray-600 mb-6">{recipe.description || 'Mô tả ngắn gọn về món ăn.'}</p>

            {/* Thông tin Dinh dưỡng */}
            <section className="bg-white p-6 rounded-xl shadow-lg border border-gray-200 mb-8">
                <h2 className="text-2xl font-bold text-gray-800 mb-4 flex items-center">
                    <Heart className="w-6 h-6 mr-2 text-red-500" />
                    Thông tin Dinh dưỡng (Mỗi khẩu phần)
                </h2>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-center">
                    <NutrientBox label="Calo (kcal)" value={recipe.calories || 'N/A'} color="bg-yellow-100 text-yellow-800" />
                    <NutrientBox label="Protein (g)" value={recipe.protein || 'N/A'} color="bg-blue-100 text-blue-800" />
                    <NutrientBox label="Carb (g)" value={recipe.carb || 'N/A'} color="bg-red-100 text-red-800" />
                    <NutrientBox label="Chất béo (g)" value={recipe.fat || 'N/A'} color="bg-green-100 text-green-800" />
                </div>
            </section>

            {/* Nguyên liệu */}
            <section className="mb-8">
                <h2 className="text-2xl font-bold text-gray-800 mb-4 flex items-center">
                    <FlaskConical className="w-6 h-6 mr-2 text-blue-600" />
                    Nguyên liệu
                </h2>
                <ul className="list-disc list-inside space-y-2 bg-white p-6 rounded-xl shadow-md border border-gray-200">
                    {recipe.ingredients && recipe.ingredients.map((item, index) => (
                        <li key={index} className="text-gray-700 font-medium">
                            {item.quantity} {item.unit} {item.name}
                        </li>
                    ))}
                </ul>
            </section>

            {/* Các bước Nấu ăn */}
            <section>
                <h2 className="text-2xl font-bold text-gray-800 mb-4 flex items-center">
                    <CookingPot className="w-6 h-6 mr-2 text-yellow-600" />
                    Các bước Nấu ăn
                </h2>
                <ol className="space-y-4">
                    {recipe.steps && recipe.steps.map((step, index) => (
                        <li key={index} className="flex items-start">
              <span className="flex items-center justify-center w-8 h-8 mr-4 text-white bg-green-600 rounded-full font-bold flex-shrink-0">
                {index + 1}
              </span>
                            <p className="text-lg text-gray-700 bg-white p-4 rounded-xl shadow-sm flex-grow border border-gray-100">
                                {step}
                            </p>
                        </li>
                    ))}
                </ol>
            </section>
        </div>
    );
}

// Component phụ cho việc hiển thị thông tin dinh dưỡng
const NutrientBox = ({ label, value, color }) => (
    <div className={`p-3 rounded-lg ${color}`}>
        <p className="text-xs font-semibold uppercase">{label}</p>
        <p className="text-xl font-bold mt-1">{value}</p>
    </div>
);

export default RecipeDetail;