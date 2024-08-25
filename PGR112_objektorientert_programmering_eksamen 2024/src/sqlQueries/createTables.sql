USE Funn;

CREATE TABLE Person (
    Id INT PRIMARY KEY,
    Navn VARCHAR(255),
    Tlf VARCHAR(12),
    E_post VARCHAR(255)
);

CREATE TABLE Museum (
    Id INT PRIMARY KEY,
    Navn VARCHAR(255),
    Sted VARCHAR(255)
);

CREATE TABLE Vaapen (
    Id INT PRIMARY KEY,
    Funnsted VARCHAR(255),
    Finner_id INT,
    Funntidspunkt DATETIME,
    Antatt_årstall INT,
    Museum_id INT,
    Type VARCHAR(100),
    Materiale VARCHAR(100),
    Vekt INT,
    FOREIGN KEY (Finner_id) REFERENCES Person(Id),
    FOREIGN KEY (Museum_id) REFERENCES Museum(Id)
);

CREATE TABLE Smykke (
    Id INT PRIMARY KEY,
    Funnsted VARCHAR(255),
    Finner_id INT,
    Funntidspunkt DATETIME,
    Antatt_årstall INT,
    Museum_id INT,
    Type VARCHAR(100),
    Verdiestimat INT,
    filnavn VARCHAR(255),
    FOREIGN KEY (Finner_id) REFERENCES Person(Id),
    FOREIGN KEY (Museum_id) REFERENCES Museum(Id)
);

CREATE TABLE Mynt (
    Id INT PRIMARY KEY,
    Funnsted VARCHAR(255),
    Finner_id INT,
    Funntidspunkt DATETIME,
    Antatt_årstall INT,
    Museum_id INT,
    Diameter INT,
    Metall VARCHAR(100),
    FOREIGN KEY (Finner_id) REFERENCES Person(Id),
    FOREIGN KEY (Museum_id) REFERENCES Museum(Id)
);