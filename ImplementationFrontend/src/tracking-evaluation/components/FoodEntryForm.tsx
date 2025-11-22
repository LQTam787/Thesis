import React, { useState } from 'react';
import { Food } from '../types';

interface FoodEntryFormProps {
    onAddFood: (food: Food, quantity: number) => void;
    availableFoods: Food[];
}

const FoodEntryForm: React.FC<FoodEntryFormProps> = ({ onAddFood, availableFoods }) => {
    const [selectedFood, setSelectedFood] = useState<Food | undefined>(undefined);
    const [quantity, setQuantity] = useState<number>(1);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (selectedFood && quantity > 0) {
            onAddFood(selectedFood, quantity);
            setSelectedFood(undefined);
            setQuantity(1);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-4 border rounded-lg shadow-sm bg-white">
            <h3 className="text-lg font-semibold mb-3">Thêm món ăn</h3>
            <div className="mb-3">
                <label htmlFor="food-select" className="block text-sm font-medium text-gray-700">Món ăn</label>
                <select
                    id="food-select"
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                    value={selectedFood?.id || ''}
                    onChange={(e) => setSelectedFood(availableFoods.find(food => food.id === e.target.value))}
                    required
                >
                    <option value="" disabled>Chọn món ăn</option>
                    {availableFoods.map(food => (
                        <option key={food.id} value={food.id}>{food.name}</option>
                    ))}
                </select>
            </div>
            <div className="mb-3">
                <label htmlFor="quantity" className="block text-sm font-medium text-gray-700">Số lượng</label>
                <input
                    type="number"
                    id="quantity"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    value={quantity}
                    onChange={(e) => setQuantity(parseFloat(e.target.value))}
                    min="0.1"
                    step="0.1"
                    required
                />
            </div>
            <button
                type="submit"
                className="w-full inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
                Thêm
            </button>
        </form>
    );
};

export default FoodEntryForm;
