CREATE DATABASE workplace;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON workplace.* TO 'admin'@'localhost';
GRANT ALL ON workplace.* TO 'admin'@'%';

USE workplace;

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Timesheet;

CREATE TABLE Users(empName TINYTEXT NOT NULL, empNo int NOT NULL, empID TINYTEXT, empPW TINYTEXT, PRIMARY KEY(empNo));

INSERT INTO Users VALUES("Jake","1", "admin", "admin");

CREATE TABLE Timesheet(empNo int NOT NULL, projectNo INT NOT NULL, WP VARCHAR(64) NOT NULL, totalWeekHours INT, satHours INT, sunHours INT, monHours INT, tuesHours INT, wedHours INT, thurHours INT, friHours INT, notes TINYTEXT, weekNumber INT, foreign key(empNo) REFERENCES Users(empNo), PRIMARY KEY(empNo, projectNo, WP, weekNumber));
