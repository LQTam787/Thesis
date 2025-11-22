import React, { useState } from 'react';
import { Activity } from '../types';

interface ActivityEntryFormProps {
    onAddActivity: (activity: Activity, duration: number, intensity: string) => void;
    availableActivities: Activity[];
}

const ActivityEntryForm: React.FC<ActivityEntryFormProps> = ({ onAddActivity, availableActivities }) => {
    const [selectedActivity, setSelectedActivity] = useState<Activity | undefined>(undefined);
    const [duration, setDuration] = useState<number>(30);
    const [intensity, setIntensity] = useState<string>('moderate');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (selectedActivity && duration > 0) {
            onAddActivity(selectedActivity, duration, intensity);
            setSelectedActivity(undefined);
            setDuration(30);
            setIntensity('moderate');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-4 border rounded-lg shadow-sm bg-white">
            <h3 className="text-lg font-semibold mb-3">Thêm hoạt động</h3>
            <div className="mb-3">
                <label htmlFor="activity-select" className="block text-sm font-medium text-gray-700">Hoạt động</label>
                <select
                    id="activity-select"
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                    value={selectedActivity?.id || ''}
                    onChange={(e) => setSelectedActivity(availableActivities.find(activity => activity.id === e.target.value))}
                    required
                >
                    <option value="" disabled>Chọn hoạt động</option>
                    {availableActivities.map(activity => (
                        <option key={activity.id} value={activity.id}>{activity.name}</option>
                    ))}
                </select>
            </div>
            <div className="mb-3">
                <label htmlFor="duration" className="block text-sm font-medium text-gray-700">Thời lượng (phút)</label>
                <input
                    type="number"
                    id="duration"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    value={duration}
                    onChange={(e) => setDuration(parseInt(e.target.value))}
                    min="1"
                    required
                />
            </div>
            <div className="mb-3">
                <label htmlFor="intensity" className="block text-sm font-medium text-gray-700">Cường độ</label>
                <select
                    id="intensity"
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                    value={intensity}
                    onChange={(e) => setIntensity(e.target.value)}
                    required
                >
                    <option value="low">Thấp</option>
                    <option value="moderate">Trung bình</option>
                    <option value="high">Cao</option>
                </select>
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

export default ActivityEntryForm;
