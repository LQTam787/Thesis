-- table_data.sql
-- Tệp này chèn dữ liệu vào tất cả các bảng.

-- Chèn dữ liệu vào bảng Users
INSERT INTO Users (Username, Password, FullName, Email, PhoneNumber, Weight, Height, Age, Gender) VALUES
('johndoe', 'hashed_password_123', 'John Doe', 'john.doe@example.com', '123-456-7890', 75.00, 1.75, 28, 'Male'),
('janedoe', 'hashed_password_456', 'Jane Doe', 'jane.doe@example.com', '987-654-3210', 60.00, 1.65, 25, 'Female'),
('alexsmith', 'hashed_password_789', 'Alex Smith', 'alex.smith@example.com', '111-222-3333', 35.00, 1.45, 12, 'Male'),
('emilyjones', 'hashed_password_101', 'Emily Jones', 'emily.jones@example.com', '444-555-6666', 58.00, 1.68, 17, 'Female'),
('robertbrown', 'hashed_password_202', 'Robert Brown', '777-888-9999', '777-888-9999', 85.00, 1.78, 50, 'Male'),
('susandavis', 'hashed_password_303', 'Susan Davis', 'susan.davis@example.com', '000-111-2222', 65.00, 1.60, 60, 'Female'),
('pepegavg', '725_blackout', 'Quang Tam Lai', 'lqtam3004@gmail.com', '036-208-3922', 77.00, 1.71, 22, 'Male');

-- Chèn dữ liệu vào bảng Dishes
INSERT INTO Dishes (DishName, Calories, MealType) VALUES
('Phở Bò', 528.00, 'Breakfast'),
('Bún Chả', 852.00, 'Lunch'),
('Cơm Tấm', 657.00, 'Lunch'),
('Bánh Mì', 657.00, 'Lunch'),
('Gỏi Cuốn', 70.00, 'Afternoon Snack'),
('Bò Kho', 600.00, 'Dinner'),
('Cháo Gà', 350.00, 'Dinner'),
('Cơm Trắng', 205.00, 'All'),
('Bún Bò Huế', 650.00, 'Lunch'),
('Bánh Xèo', 350.00, 'Lunch'),
('Chả Cá Lã Vọng', 550.00, 'Dinner'),
('Canh Chua', 250.00, 'Dinner'),
('Bánh Cuốn', 300.00, 'Breakfast'),
('Bún Đậu Mắm Tôm', 650.00, 'Lunch'),
('Chả Rươi', 400.00, 'Dinner'),
('Bún Riêu Cua', 414.00, 'Lunch'),
('Lẩu Mắm', 500.00, 'Dinner'),
('Cơm Hến', 236.00, 'Lunch'),
('Bánh Bèo', 358.00, 'Snack');

-- Chèn dữ liệu vào bảng Recipes
INSERT INTO Recipes (DishID, Ingredients, Instructions) VALUES
(1, 'Thịt bò, bánh phở, nước dùng hầm từ xương bò, hành tây, gừng, quế, hoa hồi', 'Hầm xương bò và gia vị, trần bánh phở và thịt bò thái mỏng, chan nước dùng nóng. Phục vụ với rau sống, chanh và ớt.'),
(2, 'Thịt lợn nướng, bún tươi, rau sống, dưa góp, nước mắm pha chua ngọt', 'Làm chả và thịt lợn ba chỉ nướng trên than hoa. Pha nước chấm chua ngọt. Ăn kèm bún, rau sống và dưa góp.'),
(3, 'Cơm tấm, sườn nướng, chả trứng, bì, mỡ hành, dưa leo, cà chua', 'Nấu cơm tấm. Ướp sườn với gia vị và nướng. Làm chả trứng và bì. Xếp các thành phần lên đĩa và chan mỡ hành.'),
(4, 'Bánh mì, thịt nguội, pate, chả lụa, dưa góp (cà rốt, củ cải), dưa leo, rau ngò, ớt, nước sốt', 'Xẻ dọc bánh mì, phết pate và mayonnaise. Nhồi các loại thịt, dưa góp, rau và nước sốt vào.'),
(5, 'Bánh tráng, tôm luộc, thịt ba chỉ, bún tươi, rau xà lách, húng quế', 'Luộc tôm và thịt, thái mỏng. Nhúng bánh tráng vào nước ấm, cuốn cùng các nguyên liệu. Chấm với tương đen hoặc nước mắm chua ngọt.'),
(6, 'Thịt bò, cà rốt, khoai tây, sả, gừng, hành tây, bột cà ri, dừa', 'Ướp thịt bò với gia vị và cà ri. Phi thơm hành, gừng và sả, cho thịt bò vào xào. Thêm nước dừa, cà rốt, khoai tây và hầm đến khi chín mềm.'),
(7, 'Gà, gạo nếp, hành lá, hành tây, gừng, rau răm, nước mắm', 'Luộc gà lấy nước dùng. Nấu cháo với nước dùng và gạo nếp. Xé thịt gà, trộn với hành tây, rau răm. Cho cháo ra bát, thêm thịt gà và hành phi.'),
(8, 'Gạo tẻ, nước', '1. Đong gạo và vo sạch. 2. Đong nước theo tỷ lệ 1:1.2 (1 phần gạo, 1.2 phần nước). 3. Cho vào nồi cơm điện và nấu. 4. Sau khi nấu xong, để cơm nguội bớt khoảng 10-15 phút trước khi xới ra ăn.'),
(9, 'Thịt bò, giò heo, bún, sả, mắm ruốc, huyết, chả cua', 'Nấu nước dùng từ xương bò và giò heo. Thêm sả và mắm ruốc để tạo hương vị đặc trưng. Trần bún và thịt, sau đó chan nước dùng nóng. Ăn kèm rau sống.'),
(10, 'Bột gạo, bột nghệ, tôm, thịt ba chỉ, giá đỗ, dừa, hành tây', 'Pha bột với nước cốt dừa và bột nghệ. Xào tôm, thịt và hành tây. Tráng bánh trong chảo nóng, thêm nhân và giá đỗ. Gấp đôi bánh lại và ăn kèm rau sống.'),
(11, 'Cá lăng, thì là, hành lá, bún, mắm tôm, đậu phộng rang', 'Ướp cá với riềng, nghệ và mắm tôm. Nướng hoặc chiên cá. Khi ăn, cho cá và rau thì là vào chảo trên bếp lẩu nhỏ, ăn kèm bún và mắm tôm pha chanh.'),
(12, 'Cá, cà chua, thơm (dứa), giá đỗ, bạc hà, me, ngò gai', 'Nấu nước dùng với me. Cho cá và các loại rau, củ quả vào nấu. Nêm nếm với nước mắm, đường và ăn nóng.'),
(13, 'Bột gạo, thịt nạc xay, mộc nhĩ, hành tây, chả lụa, hành phi', 'Tráng bột gạo đã pha lên khuôn hấp. Rắc nhân thịt và mộc nhĩ đã xào lên. Cuốn bánh lại và ăn kèm với chả lụa, hành phi và nước mắm pha.'),
(14, 'Bún lá, đậu hũ chiên, chả cốm, nem rán, dồi sụn, mắm tôm, rau sống', 'Pha mắm tôm với chanh, đường và ớt. Sắp xếp bún, đậu hũ, chả cốm, nem và dồi lên mẹt. Ăn kèm các loại rau sống như tía tô, kinh giới và dưa leo.'),
(15, 'Rươi, trứng gà, thịt ba chỉ, vỏ quýt, lá lốt, thì là, hành lá, gia vị', 'Rửa sạch rươi và đánh cho tan. Trộn rươi với trứng, thịt ba chỉ băm, vỏ quýt, lá lốt, hành thì là thái nhỏ. Nêm nếm gia vị và chiên vàng đều hai mặt.'),
(16, 'Cua đồng, bún, cà chua, đậu hũ, chả lụa, huyết, hành lá, rau sống', 'Phi hành tỏi, cho cà chua vào xào. Thêm nước dùng cua và nêm nếm gia vị. Cho riêu cua, đậu hũ, huyết và chả lụa vào. Ăn kèm bún và các loại rau sống.'),
(17, 'Mắm cá linh, thịt ba chỉ, tôm, mực, cá lóc, cà tím, ớt, rau đắng, bông súng, rau muống', 'Hầm mắm với nước dừa để lấy nước cốt, lọc bỏ xương. Thêm sả, tỏi, ớt, và các loại rau củ vào nồi lẩu. Khi nước sôi, nhúng các loại thịt, cá và rau vào ăn.'),
(18, 'Cơm trắng, hến, da heo chiên giòn, đậu phộng rang, rau sống, mắm ruốc', 'Hến luộc lấy nước. Hến xào với hành, tỏi. Cơm hâm nóng. Bày cơm, hến xào, da heo, đậu phộng, rau sống vào tô. Thêm nước hến và mắm ruốc. Ăn kèm với ớt chưng.'),
(19, 'Bột gạo, tôm tươi hoặc tôm chấy, thịt mỡ, nước mắm, đường, ớt, chanh', 'Pha bột gạo với nước và chút dầu ăn, muối. Hấp bánh trong chén nhỏ. Tôm tươi luộc hoặc tôm chấy. Nước mắm pha chua ngọt. Bánh chín, cho tôm và thịt mỡ lên trên, chan nước mắm.');

-- Chèn dữ liệu vào bảng RecommendedCalories
INSERT INTO RecommendedCalories (AgeGroup, Gender, ActivityLevel, BaseCalories) VALUES
('10-13', 'Male', 'Active', 2200),
('10-13', 'Female', 'Active', 1800),
('16-18', 'Male', 'Active', 2800),
('16-18', 'Female', 'Active', 2400),
('20-30', 'Male', 'Moderate', 2500),
('20-30', 'Female', 'Moderate', 2000),
('40-60', 'Male', 'Moderate', 2200),
('40-60', 'Female', 'Moderate', 1800),
('55+', 'Male', 'Low', 2000),
('55+', 'Female', 'Low', 1600);

-- Chèn dữ liệu vào bảng CalorieAdjustment
INSERT INTO CalorieAdjustment (BMIStatus, Goal, AdjustmentFactor) VALUES
('Underweight', 'Weight Gain', 1.20),
('Normal', 'Weight Maintenance', 1.00),
('Overweight', 'Weight Loss', 0.85),
('Obese', 'Weight Loss', 0.70);

-- Chèn dữ liệu vào bảng LifeStageModifiers
INSERT INTO LifeStageModifiers (StageName, ModifierType, CalorieFactor) VALUES
('Early Growth (10-13)', 'Growth', 1.15),
('Puberty (16-18)', 'Growth', 1.25),
('Post-Illness Recovery', 'Recovery', 1.10),
('Healthy Aging (55+)', 'Longevity', 0.90),
('Stable', 'Base', 1.00);

-- Chèn dữ liệu vào bảng GenderCalorieModifiers
INSERT INTO GenderCalorieModifiers (Gender, CalorieFactor, Description) VALUES
('Male', 1.15, 'Adjusts calories upward for male users due to higher muscle mass and BMR.'),
('Female', 0.85, 'Adjusts calories downward for female users due to lower muscle mass and BMR.');

-- Chèn dữ liệu vào bảng UserGoals
INSERT INTO UserGoals (UserID, CaloriesAdjID, GenderModID, LifeStageModID) VALUES
(1, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Overweight' AND Goal = 'Weight Loss'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Male'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Stable')),
(2, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Normal' AND Goal = 'Weight Maintenance'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Female'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Stable')),
(3, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Underweight' AND Goal = 'Weight Gain'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Male'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Post-Illness Recovery')),
(4, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Normal' AND Goal = 'Weight Maintenance'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Female'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Puberty (16-18)')),
(5, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Overweight' AND Goal = 'Weight Loss'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Male'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Healthy Aging (55+)')),
(6, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Overweight' AND Goal = 'Weight Loss'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Female'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Healthy Aging (55+)')),
(7, (SELECT AdjustmentID FROM CalorieAdjustment WHERE BMIStatus = 'Overweight' AND Goal = 'Weight Loss'), (SELECT ModifierID FROM GenderCalorieModifiers WHERE Gender = 'Male'), (SELECT ModifierID FROM LifeStageModifiers WHERE StageName = 'Stable'));

-- Chèn dữ liệu vào bảng CalculationHistory
INSERT INTO CalculationHistory (UserID, BMI, RecommendedCalories, CalculationDate) VALUES
(1, 24.49, 2125.00, '2025-09-21 11:36:23'),
(2, 22.04, 2000.00, '2025-09-21 11:36:23'),
(3, 16.66, 2783.00, '2025-09-21 11:36:23'),
(4, 20.52, 3000.00, '2025-09-21 11:36:23'),
(5, 26.85, 1683.00, '2025-09-21 11:36:23'),
(6, 25.39, 1224.00, '2025-09-21 11:36:23'),
(7, 26.33, 2300.00, '2025-09-21 11:36:23');

-- Chèn dữ liệu vào bảng MealDistribution
INSERT INTO MealDistribution (MealType, Percentage) VALUES
('Breakfast', 0.25),
('Lunch', 0.35),
('Afternoon Snack', 0.15),
('Dinner', 0.25);

-- Dữ liệu đề xuất bữa ăn cho tất cả người dùng vào ngày 21-09-2025
INSERT INTO MealRecommendations (UserID, CalculationID, RecommendedDate, TotalRecommendedCalories) VALUES
(1, 1, '2025-09-21', 2125.00),
(2, 2, '2025-09-21', 2000.00),
(3, 3, '2025-09-21', 2783.00),
(4, 4, '2025-09-21', 3000.00),
(5, 5, '2025-09-21', 1683.00),
(6, 6, '2025-09-21', 1224.00),
(7, 7, '2025-09-21', 2300.00);

-- Dữ liệu chi tiết đề xuất bữa ăn cho tất cả người dùng (Giả sử các RecommendationID là 1-7)
INSERT INTO RecommendationDetails (RecommendationID, MealType, DishID) VALUES
(1, 'Breakfast', 1),
(1, 'Lunch', 3),
(1, 'Afternoon Snack', 5),
(1, 'Dinner', 6),
(2, 'Breakfast', 13),
(2, 'Lunch', 18),
(2, 'Afternoon Snack', 19),
(2, 'Dinner', 7),
(3, 'Breakfast', 1),
(3, 'Lunch', 2),
(3, 'Afternoon Snack', 19),
(3, 'Dinner', 6),
(4, 'Breakfast', 1),
(4, 'Lunch', 9),
(4, 'Afternoon Snack', 5),
(4, 'Dinner', 11),
(5, 'Breakfast', 13),
(5, 'Lunch', 18),
(5, 'Afternoon Snack', 5),
(5, 'Dinner', 7),
(6, 'Breakfast', 13),
(6, 'Lunch', 18),
(6, 'Afternoon Snack', 5),
(6, 'Dinner', 7),
(7, 'Breakfast', 1),
(7, 'Lunch', 14),
(7, 'Afternoon Snack', 5),
(7, 'Dinner', 11);

-- Ghi lại thời điểm các đề xuất được tạo
INSERT INTO UserRecommendationLog (RecommendationID, UserID, LogDate) VALUES
(1, 1, '2025-09-21 14:10:05'),
(2, 2, '2025-09-21 14:10:05'),
(3, 3, '2025-09-21 14:10:05'),
(4, 4, '2025-09-21 14:10:05'),
(5, 5, '2025-09-21 14:10:05'),
(6, 6, '2025-09-21 14:10:05'),
(7, 7, '2025-09-21 14:10:05');