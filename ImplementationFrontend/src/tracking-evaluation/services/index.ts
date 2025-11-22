import { DailyLog, DailyFoodEntry, DailyActivityEntry, Food, Activity, NutritionPlan, Report, ProgressChartData, NutritionReportData, AdjustmentSuggestion } from '../types';

// Mock data
const mockFoods: Food[] = [
    { id: 'f1', name: 'Gạo lứt', description: 'Gạo lứt hữu cơ', calories: 110, protein: 2.7, carb: 23, fat: 0.9 },
    { id: 'f2', name: 'Ức gà', description: 'Ức gà không da, không xương', calories: 165, protein: 31, carb: 0, fat: 3.6 },
    { id: 'f3', name: 'Bông cải xanh', description: 'Rau xanh giàu vitamin', calories: 55, protein: 3.7, carb: 11.2, fat: 0.6 },
    { id: 'f4', name: 'Trứng gà', description: 'Trứng gà ta', calories: 155, protein: 13, carb: 1.1, fat: 11 },
];

const mockActivities: Activity[] = [
    { id: 'a1', name: 'Chạy bộ', caloriesBurned: 10 }, // calories per minute
    { id: 'a2', name: 'Đạp xe', caloriesBurned: 8 },
    { id: 'a3', name: 'Bơi lội', caloriesBurned: 12 },
];

const mockDailyLogs: DailyLog[] = [];

const mockNutritionPlan: NutritionPlan = {
    id: 'np1', 
    name: 'Kế hoạch giảm cân', 
    targetCalories: 2000, 
    targetMacroNutrients: { protein: 150, carb: 200, fat: 60 }, 
    warnings: []
};

const mockReports: Report[] = [];

// Service implementations
export const dailyLogService = {
    saveDailyLog: async (dailyLog: Omit<DailyLog, 'id'>): Promise<DailyLog> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                const newDailyLog = { ...dailyLog, id: `dl${mockDailyLogs.length + 1}` };
                mockDailyLogs.push(newDailyLog);
                resolve(newDailyLog);
            }, 500);
        });
    },
    getDailyLogByDate: async (date: string): Promise<DailyLog | undefined> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(mockDailyLogs.find(log => log.date === date));
            }, 500);
        });
    },
};

export const activityService = {
    getAllActivities: async (): Promise<Activity[]> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(mockActivities);
            }, 500);
        });
    },
};

export const foodService = {
    searchFoods: async (query: string): Promise<Food[]> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(mockFoods.filter(food => food.name.toLowerCase().includes(query.toLowerCase())));
            }, 500);
        });
    },
    getFoodById: async (id: string): Promise<Food | undefined> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(mockFoods.find(food => food.id === id));
            }, 500);
        });
    },
};

export const nutritionPlanService = {
    getCurrentNutritionPlan: async (): Promise<NutritionPlan> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(mockNutritionPlan);
            }, 500);
        });
    },
};

export const reportService = {
    generateProgressReport: async (userId: string, dailyLogs: DailyLog[], nutritionPlan: NutritionPlan): Promise<Report> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                const progressChart: ProgressChartData = {
                    labels: dailyLogs.map(log => log.date),
                    calories: dailyLogs.map(log => log.foodEntries.reduce((sum, entry) => sum + (entry.food.calories * entry.quantity), 0)),
                    protein: dailyLogs.map(log => log.foodEntries.reduce((sum, entry) => sum + (entry.food.protein * entry.quantity), 0)),
                    carb: dailyLogs.map(log => log.foodEntries.reduce((sum, entry) => sum + (entry.food.carb * entry.quantity), 0)),
                    fat: dailyLogs.map(log => log.foodEntries.reduce((sum, entry) => sum + (entry.food.fat * entry.quantity), 0)),
                };

                const nutritionReport: NutritionReportData = {
                    currentWeight: 70, // Mock data
                    bmi: '22.5', // Mock data
                    targetAchievementPercentage: 75, // Mock data
                    macroNutrientsConsumed: { protein: 120, carb: 180, fat: 50 }, // Mock data
                    microNutrientsConsumed: { 'Vitamin C': 90, 'Iron': 15 }, // Mock data
                };

                const suggestions: AdjustmentSuggestion[] = [
                    { content: 'Tăng cường rau xanh trong bữa ăn.' },
                    { content: 'Uống đủ 2 lít nước mỗi ngày.' },
                ];

                const newReport: Report = {
                    id: `r${mockReports.length + 1}`,
                    dateCreated: new Date().toISOString().split('T')[0],
                    reportContent: 'Báo cáo tiến độ dinh dưỡng hàng ngày.',
                    charts: [progressChart],
                    suggestions: suggestions,
                };
                mockReports.push(newReport);
                resolve(newReport);
            }, 1000);
        });
    },
};
