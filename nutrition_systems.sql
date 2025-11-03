-- Bảng lưu trữ vai trò người dùng (USER, ADMIN)
CREATE TABLE roles (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT uk_roles_name UNIQUE (name)
);

-- Bảng lưu trữ thông tin tài khoản người dùng
CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- Bảng trung gian cho quan hệ nhiều-nhiều giữa users và roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Bảng hồ sơ chi tiết của người dùng
CREATE TABLE profiles (
    id                  BIGINT PRIMARY KEY,
    full_name           VARCHAR(255),
    date_of_birth       DATE,
    gender              VARCHAR(50),
    height              DOUBLE,
    weight              DOUBLE,
    activity_level      VARCHAR(255),
    allergies           TEXT,
    medical_conditions  TEXT,
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);

-- Bảng mục tiêu của người dùng (ví dụ: giảm cân, tăng cân)
CREATE TABLE goals (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT,
    goal_type     VARCHAR(255),
    target_weight DOUBLE,
    target_date   DATE,
    status        VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Bảng danh mục các loại thực phẩm
CREATE TABLE food_items (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    calories     DOUBLE,
    protein      DOUBLE,
    carbs        DOUBLE,
    fats         DOUBLE,
    serving_size VARCHAR(255)
);

-- Bảng nhật ký ăn uống của người dùng
CREATE TABLE food_logs (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT,
    food_item_id BIGINT,
    log_date     DATETIME,
    quantity     DOUBLE,
    unit         VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_items (id) ON DELETE SET NULL
);

-- Bảng danh mục các hoạt động thể chất
CREATE TABLE activities (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    calories_per_hour   DOUBLE
);

-- Bảng nhật ký hoạt động của người dùng
CREATE TABLE activity_logs (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT,
    activity_id       BIGINT,
    log_date          DATETIME,
    duration_in_minutes INT,
    calories_burned   DOUBLE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities (id) ON DELETE SET NULL
);

-- Bảng định nghĩa các bữa ăn (sáng, trưa, tối)
CREATE TABLE meals (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

-- Bảng trung gian cho quan hệ nhiều-nhiều giữa bữa ăn và thực phẩm
CREATE TABLE meal_food_items (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    meal_id      BIGINT,
    food_item_id BIGINT,
    quantity     DOUBLE,
    unit         VARCHAR(50),
    FOREIGN KEY (meal_id) REFERENCES meals (id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_items (id) ON DELETE CASCADE
);

-- Bảng kế hoạch dinh dưỡng
CREATE TABLE nutrition_plans (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    name        VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Bảng thực đơn hàng ngày trong một kế hoạch dinh dưỡng
CREATE TABLE daily_menus (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    nutrition_plan_id  BIGINT,
    day_of_week        VARCHAR(20),
    meal_id            BIGINT,
    FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans (id) ON DELETE CASCADE,
    FOREIGN KEY (meal_id) REFERENCES meals (id) ON DELETE CASCADE
);