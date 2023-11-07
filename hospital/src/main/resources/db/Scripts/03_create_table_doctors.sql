CREATE TABLE doctors(
    id char(9) primary key,
    college_number char(9) not null,
    name varchar(50),
    starting_time time not null,
    ending_time time not null,
    years_experience tinyint
    );