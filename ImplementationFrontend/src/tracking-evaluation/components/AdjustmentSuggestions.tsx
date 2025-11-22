import React from 'react';
import { AdjustmentSuggestion } from '../types';

interface AdjustmentSuggestionsProps {
    suggestions: AdjustmentSuggestion[];
}

const AdjustmentSuggestions: React.FC<AdjustmentSuggestionsProps> = ({ suggestions }) => {
    return (
        <div className="bg-white p-4 rounded-lg shadow-sm">
            <h3 className="text-lg font-semibold mb-3">Gợi ý điều chỉnh</h3>
            {suggestions.length === 0 ? (
                <p className="text-gray-600">Không có gợi ý điều chỉnh nào vào lúc này.</p>
            ) : (
                <ul className="list-disc pl-5 space-y-2 text-gray-700">
                    {suggestions.map((suggestion, index) => (
                        <li key={index}>{suggestion.content}</li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default AdjustmentSuggestions;
