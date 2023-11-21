CREATE TABLE appointments(
    college_number char(9),
    patient_dni char(9),
    date_of_appointment date,
    time_of_appointment time,
    CONSTRAINT PK_appointments PRIMARY KEY (college_number, patient_dni, date_of_appointment, time_of_appointment)
    );


