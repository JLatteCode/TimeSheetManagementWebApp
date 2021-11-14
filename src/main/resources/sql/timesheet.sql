CREATE DATABASE timesheet;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON timesheet.* TO 'admin'@'localhost';
GRANT ALL ON timesheet.* TO 'admin'@'%';

USE timesheet;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users(username TINYTEXT NOT NULL, password TINYTEXT NOT NULL, PRIMARY KEY(username));

INSERT INTO Users VALUES("admin","admin");

DROP TABLE IF EXISTS TimesheetData;
CREATE TABLE TimesheetData(projectNum INT NOT NULL, WP TINYTEXT NOT NULL, totalWeekHours INT, satHours INT, sunHours INT, monHours INT, tuesHours INT, wedHours INT, thurHours INT, friHours INT);
