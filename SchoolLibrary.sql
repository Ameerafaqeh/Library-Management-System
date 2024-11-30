-- DROP DATABASE SchoolLibrary; 
create database SchoolLibrary;
use SchoolLibrary;
create table Student(studentID int primary key, studentName varchar(32), class int);
insert into Student(studentID, studentName, class) values
(22222, "Dustin", 7),
(31222, "Lubber", 8),
(58222, "Rusty", 10),
(32222, "Andy", 8);
SHOW TABLES;
show databases;
