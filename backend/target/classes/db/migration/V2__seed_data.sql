-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, role)
VALUES (
    'admin',
    'admin@floodguard.com',
    '$2a$10$rDkPvvAFV6GgJkKq8G8Y1.8Q9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z',
    'System Administrator',
    'ADMIN'
); 