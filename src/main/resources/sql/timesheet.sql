CREATE DATABASE timesheet;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON timesheet.* TO 'admin'@'localhost';
GRANT ALL ON timesheet.* TO 'admin'@'%';

USE timesheet;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users(empNum INT NOT NULL, username TINYTEXT NOT NULL, password TINYTEXT NOT NULL, PRIMARY KEY(empNum));

INSERT INTO Users VALUES(1,"admin","admin");

DROP TABLE IF EXISTS TimesheetData;
CREATE TABLE TimesheetData(projectNum INT NOT NULL, WP varchar(64) NOT NULL, totalWeekHours INT, satHours INT, sunHours INT, monHours INT, tuesHours INT, wedHours INT, thurHours INT, friHours INT, notes TINYTEXT, empNum INT NOT NULL, CONSTRAINT fk_empNum FOREIGN KEY (empNum) REFERENCES Users(empNum), PRIMARY KEY (projectNum, WP));
