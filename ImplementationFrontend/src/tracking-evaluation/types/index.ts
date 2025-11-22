export interface Food {
    id: string;
    name: string;
    description: string;
    calories: number;
    protein: number;
    carb: number;
    fat: number;
}

export interface Activity {
    id: string;
    name: string;
    caloriesBurned: number;
}

export interface DailyFoodEntry {
    food: Food;
    quantity: number;
}

export interface DailyActivityEntry {
    activity: Activity;
    duration: number; // in minutes
    intensity: string;
}

export interface DailyLog {
    id: string;
    date: string; // ISO date string
    foodEntries: DailyFoodEntry[];
    activityEntries: DailyActivityEntry[];
}

export interface ProgressChartData {
    labels: string[]; // Dates
    calories: number[];
    protein: number[];
    carb: number[];
    fat: number[];
}

export interface NutritionReportData {
    currentWeight: number;
    bmi: string;
    targetAchievementPercentage: number;
    macroNutrientsConsumed: {
        protein: number;
        carb: number;
        fat: number;
    };
    microNutrientsConsumed: {
        [key: string]: number; // e.g., "Vitamin C": 100
    };
}

export interface AdjustmentSuggestion {
    content: string;
}

export interface NutritionPlan {
    id: string;
    name: string;
    targetCalories: number;
    targetMacroNutrients: {
        protein: number;
        carb: number;
        fat: number;
    };
    warnings: string[];
}

export interface Report {
    id: string;
    dateCreated: string;
    reportContent: string;
    charts: ProgressChartData[];
    suggestions: AdjustmentSuggestion[];
}
