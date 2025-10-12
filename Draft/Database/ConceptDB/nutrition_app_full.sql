-- nutrition_app_full.sql
-- Tệp này chứa toàn bộ schema và dữ liệu cho cơ sở dữ liệu nutrition_app.

-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS nutrition_app;
USE nutrition_app;

-- Tạo tất cả các bảng
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

CREATE TABLE Dishes (
    DishID INT AUTO_INCREMENT PRIMARY KEY,
    DishName VARCHAR(255),
    Calories DECIMAL(6, 2),
    MealType VARCHAR(50)
);

CREATE TABLE Recipes (
    RecipeID INT AUTO_INCREMENT PRIMARY KEY,
    DishID INT,
    Ingredients TEXT,
    Instructions TEXT,
    FOREIGN KEY (DishID) REFERENCES Dishes(DishID)
);

CREATE TABLE RecommendedCalories (
    CalorieID INT AUTO_INCREMENT PRIMARY KEY,
    AgeGroup VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    ActivityLevel VARCHAR(20) NOT NULL,
    BaseCalories INT NOT NULL
);

CREATE TABLE CalorieAdjustment (
    AdjustmentID INT AUTO_INCREMENT PRIMARY KEY,
    BMIStatus VARCHAR(50) NOT NULL,
    Goal VARCHAR(255) NOT NULL,
    AdjustmentFactor DECIMAL(3, 2) NOT NULL
);

CREATE TABLE LifeStageModifiers (
    ModifierID INT AUTO_INCREMENT PRIMARY KEY,
    StageName VARCHAR(100) NOT NULL UNIQUE,
    ModifierType VARCHAR(50),
    CalorieFactor DECIMAL(3, 2) NOT NULL
);

CREATE TABLE GenderCalorieModifiers (
    ModifierID INT AUTO_INCREMENT PRIMARY KEY,
    Gender VARCHAR(10) NOT NULL UNIQUE,
    CalorieFactor DECIMAL(3, 2) NOT NULL,
    Description VARCHAR(255)
);

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

CREATE TABLE CalculationHistory (
    CalculationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    BMI DECIMAL(4, 2) NOT NULL,
    RecommendedCalories DECIMAL(6, 2) NOT NULL,
    CalculationDate DATETIME NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE MealDistribution (
    MealID INT AUTO_INCREMENT PRIMARY KEY,
    MealType VARCHAR(50) NOT NULL UNIQUE,
    Percentage DECIMAL(4, 2) NOT NULL
);

CREATE TABLE MealRecommendations (
    RecommendationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    CalculationID INT NOT NULL,
    RecommendedDate DATE NOT NULL,
    TotalRecommendedCalories DECIMAL(6, 2) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (CalculationID) REFERENCES CalculationHistory(CalculationID)
);

CREATE TABLE RecommendationDetails (
    DetailID INT AUTO_INCREMENT PRIMARY KEY,
    RecommendationID INT NOT NULL,
    MealType VARCHAR(50) NOT NULL,
    DishID INT NOT NULL,
    FOREIGN KEY (RecommendationID) REFERENCES MealRecommendations(RecommendationID),
    FOREIGN KEY (DishID) REFERENCES Dishes(DishID)
);

CREATE TABLE UserRecommendationLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    RecommendationID INT NOT NULL,
    UserID INT NOT NULL,
    LogDate DATETIME NOT NULL,
    FOREIGN KEY (RecommendationID) REFERENCES MealRecommendations(RecommendationID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Chèn dữ liệu vào các bảng

-- Tắt kiểm tra khóa ngoại tạm thời để chèn dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- Chèn dữ liệu vào bảng Users
INSERT INTO Users (UserID, Username, Password, FullName, Email, PhoneNumber, Weight, Height, Age, Gender) VALUES
(1, 'johndoe', 'hashed_password_123', 'John Doe', 'john.doe@example.com', '123-456-7890', 75.00, 1.75, 28, 'Male'),
(2, 'janedoe', 'hashed_password_456', 'Jane Doe', 'jane.doe@example.com', '987-654-3210', 60.00, 1.65, 25, 'Female'),
(3, 'alexsmith', 'hashed_password_789', 'Alex Smith', 'alex.smith@example.com', '111-222-3333', 35.00, 1.45, 12, 'Male'),
(4, 'emilyjones', 'hashed_password_101', 'Emily Jones', 'emily.jones@example.com', '444-555-6666', 58.00, 1.68, 17, 'Female'),
(5, 'robertbrown', 'hashed_password_202', 'Robert Brown', '777-888-9999', '777-888-9999', 85.00, 1.78, 50, 'Male'),
(6, 'susandavis', 'hashed_password_303', 'Susan Davis', 'susan.davis@example.com', '000-111-2222', 65.00, 1.60, 60, 'Female'),
(7, 'pepegavg', '725_blackout', 'Quang Tam Lai', 'lqtam3004@gmail.com', '036-208-3922', 77.00, 1.71, 22, 'Male');

-- Chèn dữ liệu vào bảng Dishes
INSERT INTO Dishes (DishID, DishName, Calories, MealType) VALUES
(1, 'Phở Bò', 528.00, 'Breakfast'),
(2, 'Bún Chả', 852.00, 'Lunch'),
(3, 'Cơm Tấm', 657.00, 'Lunch'),
(4, 'Bánh Mì', 657.00, 'Lunch'),
(5, 'Gỏi Cuốn', 70.00, 'Afternoon Snack'),
(6, 'Bò Kho', 600.00, 'Dinner'),
(7, 'Cháo Gà', 350.00, 'Dinner'),
(8, 'Cơm Trắng', 205.00, 'All'),
(9, 'Bún Bò Huế', 650.00, 'Lunch'),
(10, 'Bánh Xèo', 350.00, 'Lunch'),
(11, 'Chả Cá Lã Vọng', 550.00, 'Dinner'),
(12, 'Canh Chua', 250.00, 'Dinner'),
(13, 'Bánh Cuốn', 300.00, 'Breakfast'),
(14, 'Bún Đậu Mắm Tôm', 650.00, 'Lunch'),
(15, 'Chả Rươi', 400.00, 'Dinner'),
(16, 'Bún Riêu Cua', 414.00, 'Lunch'),
(17, 'Lẩu Mắm', 500.00, 'Dinner'),
(18, 'Cơm Hến', 236.00, 'Lunch'),
(19, 'Bánh Bèo', 358.00, 'Snack');

-- Chèn dữ liệu vào bảng Recipes
INSERT INTO Recipes (RecipeID, DishID, Ingredients, Instructions) VALUES
(1, 1, 'Thịt bò, bánh phở, nước dùng hầm từ xương bò, hành tây, gừng, quế, hoa hồi', 'Hầm xương bò và gia vị, trần bánh phở và thịt bò thái mỏng, chan nước dùng nóng. Phục vụ với rau sống, chanh và ớt.'),
(2, 2, 'Thịt lợn nướng, bún tươi, rau sống, dưa góp, nước mắm pha chua ngọt', 'Làm chả và thịt lợn ba chỉ nướng trên than hoa. Pha nước chấm chua ngọt. Ăn kèm bún, rau sống và dưa góp.'),
(3, 3, 'Cơm tấm, sườn nướng, chả trứng, bì, mỡ hành, dưa leo, cà chua', 'Nấu cơm tấm. Ướp sườn với gia vị và nướng. Làm chả trứng và bì. Xếp các thành phần lên đĩa và chan mỡ hành.'),
(4, 4, 'Bánh mì, thịt nguội, pate, chả lụa, dưa góp (cà rốt, củ cải), dưa leo, rau ngò, ớt, nước sốt', 'Xẻ dọc bánh mì, phết pate và mayonnaise. Nhồi các loại thịt, dưa góp, rau và nước sốt vào.'),
(5, 5, 'Bánh tráng, tôm luộc, thịt ba chỉ, bún tươi, rau xà lách, húng quế', 'Luộc tôm và thịt, thái mỏng. Nhúng bánh tráng vào nước ấm, cuốn cùng các nguyên liệu. Chấm với tương đen hoặc nước mắm chua ngọt.'),
(6, 6, 'Thịt bò, cà rốt, khoai tây, sả, gừng, hành tây, bột cà ri, dừa', 'Ướp thịt bò với gia vị và cà ri. Phi thơm hành, gừng và sả, cho thịt bò vào xào. Thêm nước dừa, cà rốt, khoai tây và hầm đến khi chín mềm.'),
(7, 7, 'Gà, gạo nếp, hành lá, hành tây, gừng, rau răm, nước mắm', 'Luộc gà lấy nước dùng. Nấu cháo với nước dùng và gạo nếp. Xé thịt gà, trộn với hành tây, rau răm. Cho cháo ra bát, thêm thịt gà và hành phi.'),
(8, 8, 'Gạo tẻ, nước', '1. Đong gạo và vo sạch. 2. Đong nước theo tỷ lệ 1:1.2 (1 phần gạo, 1.2 phần nước). 3. Cho vào nồi cơm điện và nấu. 4. Sau khi nấu xong, để cơm nguội bớt khoảng 10-15 phút trước khi xới ra ăn.'),
(9, 9, 'Thịt bò, giò heo, bún, sả, mắm ruốc, huyết, chả cua', 'Nấu nước dùng từ xương bò và giò heo. Thêm sả và mắm ruốc để tạo hương vị đặc trưng. Trần bún và thịt, sau đó chan nước dùng nóng. Ăn kèm rau sống.'),
(10, 10, 'Bột gạo, bột nghệ, tôm, thịt ba chỉ, giá đỗ, dừa, hành tây', 'Pha bột với nước cốt dừa và bột nghệ. Xào tôm, thịt và hành tây. Tráng bánh trong chảo nóng, thêm nhân và giá đỗ. Gấp đôi bánh lại và ăn kèm rau sống.'),
(11, 11, 'Cá lăng, thì là, hành lá, bún, mắm tôm, đậu phộng rang', 'Ướp cá với riềng, nghệ và mắm tôm. Nướng hoặc chiên cá. Khi ăn, cho cá và rau thì là vào chảo trên bếp lẩu nhỏ, ăn kèm bún và mắm tôm pha chanh.'),
(12, 12, 'Cá, cà chua, thơm (dứa), giá đỗ, bạc hà, me, ngò gai', 'Nấu nước dùng với me. Cho cá và các loại rau, củ quả vào nấu. Nêm nếm với nước mắm, đường và ăn nóng.'),
(13, 13, 'Bột gạo, thịt nạc xay, mộc nhĩ, hành tây, chả lụa, hành phi', 'Tráng bột gạo đã pha lên khuôn hấp. Rắc nhân thịt và mộc nhĩ đã xào lên. Cuốn bánh lại và ăn kèm với chả lụa, hành phi và nước mắm pha.'),
(14, 14, 'Bún lá, đậu hũ chiên, chả cốm, nem rán, dồi sụn, mắm tôm, rau sống', 'Pha mắm tôm với chanh, đường và ớt. Sắp xếp bún, đậu hũ, chả cốm, nem và dồi lên mẹt. Ăn kèm các loại rau sống như tía tô, kinh giới và dưa leo.'),
(15, 15, 'Rươi, trứng gà, thịt ba chỉ, vỏ quýt, lá lốt, thì là, hành lá, gia vị', 'Rửa sạch rươi và đánh cho tan. Trộn rươi với trứng, thịt ba chỉ băm, vỏ quýt, lá lốt, hành thì là thái nhỏ. Nêm nếm gia vị và chiên vàng đều hai mặt.'),
(16, 16, 'Cua đồng, bún, cà chua, đậu hũ, chả lụa, huyết, hành lá, rau sống', 'Phi hành tỏi, cho cà chua vào xào. Thêm nước dùng cua và nêm nếm gia vị. Cho riêu cua, đậu hũ, huyết và chả lụa vào. Ăn kèm bún và các loại rau sống.'),
(17, 17, 'Mắm cá linh, thịt ba chỉ, tôm, mực, cá lóc, cà tím, ớt, rau đắng, bông súng, rau muống', 'Hầm mắm với nước dừa để lấy nước cốt, lọc bỏ xương. Thêm sả, tỏi, ớt, và các loại rau củ vào nồi lẩu. Khi nước sôi, nhúng các loại thịt, cá và rau vào ăn.'),
(18, 18, 'Cơm trắng, hến, da heo chiên giòn, đậu phộng rang, rau sống, mắm ruốc', 'Hến luộc lấy nước. Hến xào với hành, tỏi. Cơm hâm nóng. Bày cơm, hến xào, da heo, đậu phộng, rau sống vào tô. Thêm nước hến và mắm ruốc. Ăn kèm với ớt chưng.'),
(19, 19, 'Bột gạo, tôm tươi hoặc tôm chấy, thịt mỡ, nước mắm, đường, ớt, chanh', 'Pha bột gạo với nước và chút dầu ăn, muối. Hấp bánh trong chén nhỏ. Tôm tươi luộc hoặc tôm chấy. Nước mắm pha chua ngọt. Bánh chín, cho tôm và thịt mỡ lên trên, chan nước mắm.');

-- Chèn dữ liệu vào bảng RecommendedCalories
INSERT INTO RecommendedCalories (CalorieID, AgeGroup, Gender, ActivityLevel, BaseCalories) VALUES
(1, '10-13', 'Male', 'Active', 2200),
(2, '10-13', 'Female', 'Active', 1800),
(3, '16-18', 'Male', 'Active', 2800),
(4, '16-18', 'Female', 'Active', 2400),
(5, '20-30', 'Male', 'Moderate', 2500),
(6, '20-30', 'Female', 'Moderate', 2000),
(7, '40-60', 'Male', 'Moderate', 2200),
(8, '40-60', 'Female', 'Moderate', 1800),
(9, '55+', 'Male', 'Low', 2000),
(10, '55+', 'Female', 'Low', 1600);

-- Chèn dữ liệu vào bảng CalorieAdjustment
INSERT INTO CalorieAdjustment (AdjustmentID, BMIStatus, Goal, AdjustmentFactor) VALUES
(1, 'Underweight', 'Weight Gain', 1.20),
(2, 'Normal', 'Weight Maintenance', 1.00),
(3, 'Overweight', 'Weight Loss', 0.85),
(4, 'Obese', 'Weight Loss', 0.70);

-- Chèn dữ liệu vào bảng LifeStageModifiers
INSERT INTO LifeStageModifiers (ModifierID, StageName, ModifierType, CalorieFactor) VALUES
(1, 'Early Growth (10-13)', 'Growth', 1.15),
(2, 'Puberty (16-18)', 'Growth', 1.25),
(3, 'Post-Illness Recovery', 'Recovery', 1.10),
(4, 'Healthy Aging (55+)', 'Longevity', 0.90),
(5, 'Stable', 'Base', 1.00);

-- Chèn dữ liệu vào bảng GenderCalorieModifiers
INSERT INTO GenderCalorieModifiers (ModifierID, Gender, CalorieFactor, Description) VALUES
(1, 'Male', 1.15, 'Adjusts calories upward for male users due to higher muscle mass and BMR.'),
(2, 'Female', 0.85, 'Adjusts calories downward for female users due to lower muscle mass and BMR.');

-- Chèn dữ liệu vào bảng UserGoals
INSERT INTO UserGoals (UserGoalID, UserID, CaloriesAdjID, GenderModID, LifeStageModID) VALUES
(1, 1, 3, 1, 5),
(2, 2, 2, 2, 5),
(3, 3, 1, 1, 3),
(4, 4, 2, 2, 2),
(5, 5, 3, 1, 4),
(6, 6, 3, 2, 4),
(7, 7, 3, 1, 5);

-- Chèn dữ liệu vào bảng CalculationHistory
INSERT INTO CalculationHistory (CalculationID, UserID, BMI, RecommendedCalories, CalculationDate) VALUES
(1, 1, 24.49, 2125.00, '2025-09-21 11:36:23'),
(2, 2, 22.04, 2000.00, '2025-09-21 11:36:23'),
(3, 3, 16.66, 2783.00, '2025-09-21 11:36:23'),
(4, 4, 20.52, 3000.00, '2025-09-21 11:36:23'),
(5, 5, 26.85, 1683.00, '2025-09-21 11:36:23'),
(6, 6, 25.39, 1224.00, '2025-09-21 11:36:23'),
(7, 7, 26.33, 2300.00, '2025-09-21 11:36:23');

-- Chèn dữ liệu vào bảng MealDistribution
INSERT INTO MealDistribution (MealID, MealType, Percentage) VALUES
(1, 'Breakfast', 0.25),
(2, 'Lunch', 0.35),
(3, 'Afternoon Snack', 0.15),
(4, 'Dinner', 0.25);

-- Chèn dữ liệu vào bảng MealRecommendations
INSERT INTO MealRecommendations (RecommendationID, UserID, CalculationID, RecommendedDate, TotalRecommendedCalories) VALUES
(1, 1, 1, '2025-09-21', 2125.00),
(2, 2, 2, '2025-09-21', 2000.00),
(3, 3, 3, '2025-09-21', 2783.00),
(4, 4, 4, '2025-09-21', 3000.00),
(5, 5, 5, '2025-09-21', 1683.00),
(6, 6, 6, '2025-09-21', 1224.00),
(7, 7, 7, '2025-09-21', 2300.00);

-- Chèn dữ liệu vào bảng RecommendationDetails
INSERT INTO RecommendationDetails (DetailID, RecommendationID, MealType, DishID) VALUES
(1, 1, 'Breakfast', 1),
(2, 1, 'Lunch', 3),
(3, 1, 'Afternoon Snack', 5),
(4, 1, 'Dinner', 6),
(5, 2, 'Breakfast', 13),
(6, 2, 'Lunch', 18),
(7, 2, 'Afternoon Snack', 19),
(8, 2, 'Dinner', 7),
(9, 3, 'Breakfast', 1),
(10, 3, 'Lunch', 2),
(11, 3, 'Afternoon Snack', 19),
(12, 3, 'Dinner', 6),
(13, 4, 'Breakfast', 1),
(14, 4, 'Lunch', 9),
(15, 4, 'Afternoon Snack', 5),
(16, 4, 'Dinner', 11),
(17, 5, 'Breakfast', 13),
(18, 5, 'Lunch', 18),
(19, 5, 'Afternoon Snack', 5),
(20, 5, 'Dinner', 7),
(21, 6, 'Breakfast', 13),
(22, 6, 'Lunch', 18),
(23, 6, 'Afternoon Snack', 5),
(24, 6, 'Dinner', 7),
(25, 7, 'Breakfast', 1),
(26, 7, 'Lunch', 14),
(27, 7, 'Afternoon Snack', 5),
(28, 7, 'Dinner', 11);

-- Chèn dữ liệu vào bảng UserRecommendationLog
INSERT INTO UserRecommendationLog (LogID, RecommendationID, UserID, LogDate) VALUES
(1, 1, 1, '2025-09-21 14:10:05'),
(2, 2, 2, '2025-09-21 14:10:05'),
(3, 3, 3, '2025-09-21 14:10:05'),
(4, 4, 4, '2025-09-21 14:10:05'),
(5, 5, 5, '2025-09-21 14:10:05'),
(6, 6, 6, '2025-09-21 14:10:05'),
(7, 7, 7, '2025-09-21 14:10:05');

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;