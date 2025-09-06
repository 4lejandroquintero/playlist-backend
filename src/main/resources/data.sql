
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Inserta los usuarios solo si no existen
INSERT INTO users (username, password, role)
VALUES
  ('admin', '$2a$10$SIrOeuhC9AbmtqmR2GvA.uD0WM.vwTxwLEDy8vSE.tjcBXGGtdeo2', 'ROLE_ADMIN'),
  ('alejo', '$2a$10$ApNtNfVg1cDjoj82uveJWuLUdLaTO.F/Pc0QX727Mj8f8bqH6y7pC', 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;