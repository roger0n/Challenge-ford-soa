CREATE TABLE vehicles (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    marca VARCHAR(100),
    modelo VARCHAR(100),
    versao VARCHAR(100),

    motor VARCHAR(100),
    potencia VARCHAR(100),
    torque VARCHAR(100),
    cambio VARCHAR(100),
    tracao VARCHAR(100),
    combustivel VARCHAR(100),
    capacidade_carga VARCHAR(100)
);