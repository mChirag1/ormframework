create table department
(
code int primary key AUTO_INCREMENT,
name char(50) not null unique
);
create table employee
(
emp_id char(15) primary key,
name char(50) not null,
department_code int not null,
date_of_birth date not null,
gender char(1) not null,
gross_salary decimal(10,2) not null,
is_indian boolean not null, 
foreign key(department_code) references department(code)
);
create table project
(
code int primary key AUTO_INCREMENT,
title char(50) unique not null
);
create table technology
(
code int primary key AUTO_INCREMENT,
name char(50) unique not null
);
create table employee_project_mapping
(
emp_id char(50),
project_code int,
technology_code int,
start_date date not null,
end_date date,
primary key(emp_id,project_code,technology_code),
foreign key (emp_id) references employee(emp_id),
foreign key(project_code) references project(code),
foreign key(technology_code) references technology(code)
);
