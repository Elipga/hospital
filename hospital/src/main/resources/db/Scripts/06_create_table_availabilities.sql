CREATE TABLE availabilities(
    college_number char(9),
    day date,
    hour time,
    CONSTRAINT PK_availabilities PRIMARY KEY (college_number, day, hour)
    );