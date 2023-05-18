CREATE TABLE USER(
    ID INT NOT NULL AUTO_INCREMENT,
    USERNAME VARCHAR(50),
    EMAIL VARCHAR(100),
    PASSWORD VARCHAR(100),
    PHONE VARCHAR(100),
    VERIFICATION_CODE VARCHAR(100),
    ENABLED BIT(1) DEFAULT 0,
    PRIMARY KEY (ID)
);