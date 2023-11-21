package com.example.hospital.Repository;

import com.example.hospital.Domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    boolean existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment
            (String collegeNumber, LocalDate dateOfAppointment,LocalTime timeOfAppointment);
    List<Appointment> findByPatientDniAndDateOfAppointmentOrderByTimeOfAppointment (String patientId,
                                                                                   LocalDate dateOfAppointment);
    List<Appointment> findByCollegeNumberAndDateOfAppointmentBetween(String collegeNumber,
                                                                     LocalDate firstDay, LocalDate lastDay);
    List<Appointment> findByCollegeNumber(String collegeNumber);

    boolean existsByPatientDniAndDateOfAppointmentAndTimeOfAppointment(String patientId, LocalDate dateOfAppointment,
                                                                      LocalTime timeOfAppointment );

}
