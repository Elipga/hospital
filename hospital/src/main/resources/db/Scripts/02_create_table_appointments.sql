create table appointments(
    staff_id char(9),
    patient_id char(9),
    date_of_appointment date,
    time_of_appointment time,
    CONSTRAINT PK_appointments PRIMARY KEY (staff_id, patient_id, date_of_appointment, time_of_appointment)
    );