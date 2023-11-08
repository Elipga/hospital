package com.example.hospital.Repository;

import com.example.hospital.Domain.Appointment;
import jdk.internal.net.http.common.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    boolean existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment
            (String collegeNumber, LocalDate dateOfAppointment,LocalTime timeOfAppointment);
    List<Appointment> findByPatientIdAndDateOfAppointmentOrderByTimeOfAppointment (String patienId,
                                                                                   LocalDate dateOfAppointment);
    List<Appointment> findByCollegeNumberAndDateOfAppointmentBetween(String collegeNumber,
                                                                     LocalDate firstDay, LocalDate lastDay);

    //@Query("SELECT a FROM Appointment a WHERE a.collegeNumber = :collegeNumber AND a.dateOfAppointment BETWEEN :firstDay AND :lastDay")
    //List<Appointment> findByCollegeNumberAndDateOfAppointmentBetween(@Param("collegeNumber") String collegeNumber, @Param("firstDay") LocalDate firstDay, @Param("lastDay") LocalDate lastDay);

}
