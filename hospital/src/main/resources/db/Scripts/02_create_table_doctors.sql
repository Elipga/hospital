CREATE TABLE doctors(
    dni char(9) not null,
    college_number char(9) primary key,
    name varchar(50),
    starting_time time not null,
    ending_time time not null,
    years_experience int
    );