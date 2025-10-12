-- create_tables.sql
-- Tệp này tạo các bảng cho cơ sở dữ liệu nutrition_app.

-- Bảng Users
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    FullName VARCHAR(100),
    Email VARCHAR(100) NOT NULL UNIQUE,
    PhoneNumber VARCHAR(20),
    Weight DECIMAL(5, 2),
    Height DECIMAL(5, 2),
    Age INT,
    Gender VARCHAR(10)
);

-- Bảng Dishes
CREATE TABLE Dishes (
    DishID INT AUTO_INCREMENT PRIMARY KEY,
    DishName VARCHAR(255),
    Calories DECIMAL(6, 2),
    MealType VARCHAR(50)
);

-- Bảng Recipes
CREATE TABLE Recipes (
    RecipeID INT AUTO_INCREMENT PRIMARY KEY,
    DishID INT,
    Ingredients TEXT,
    Instructions TEXT,
    FOREIGN KEY (DishID) REFERENCES Dishes(DishID)
);

-- Bảng RecommendedCalories
CREATE TABLE RecommendedCalories (
    CalorieID INT AUTO_INCREMENT PRIMARY KEY,
    AgeGroup VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    ActivityLevel VARCHAR(20) NOT NULL,
    BaseCalories INT NOT NULL
);

-- Bảng CalorieAdjustment
CREATE TABLE CalorieAdjustment (
    AdjustmentID INT AUTO_INCREMENT PRIMARY KEY,
    BMIStatus VARCHAR(50) NOT NULL,
    Goal VARCHAR(255) NOT NULL,
    AdjustmentFactor DECIMAL(3, 2) NOT NULL
);

-- Bảng GenderCalorieModifiers
CREATE TABLE GenderCalorieModifiers (
    ModifierID INT AUTO_INCREMENT PRIMARY KEY,
    Gender VARCHAR(10) NOT NULL UNIQUE,
    CalorieFactor DECIMAL(3, 2) NOT NULL,
    Description VARCHAR(255)
);

-- Bảng LifeStageModifiers
CREATE TABLE LifeStageModifiers (
    ModifierID INT AUTO_INCREMENT PRIMARY KEY,
    StageName VARCHAR(100) NOT NULL UNIQUE,
    ModifierType VARCHAR(50),
    CalorieFactor DECIMAL(3, 2) NOT NULL
);

-- Bảng UserGoals
CREATE TABLE UserGoals (
    UserGoalID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    CaloriesAdjID INT NOT NULL,
    GenderModID INT NOT NULL,
    LifeStageModID INT NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (CaloriesAdjID) REFERENCES CalorieAdjustment(AdjustmentID),
    FOREIGN KEY (GenderModID) REFERENCES GenderCalorieModifiers(ModifierID),
    FOREIGN KEY (LifeStageModID) REFERENCES LifeStageModifiers(ModifierID)
);

-- Bảng CalculationHistory
CREATE TABLE CalculationHistory (
    CalculationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    BMI DECIMAL(4, 2) NOT NULL,
    RecommendedCalories DECIMAL(6, 2) NOT NULL,
    CalculationDate DATETIME NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng MealDistribution
CREATE TABLE MealDistribution (
    MealID INT AUTO_INCREMENT PRIMARY KEY,
    MealType VARCHAR(50) NOT NULL UNIQUE,
    Percentage DECIMAL(4, 2) NOT NULL
);