package com.example.hospital.Repository;

import com.example.hospital.Domain.Appointment;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<Appointment> findByCollegeNumberBetween(String collegeNumber, Pair<LocalDate, LocalDate> appointmentsOfWeek);


    /*@Query("SELECT a FROM Appointment a " +
            "WHERE a.patientId = :patientId " +
            "AND a.dateOfAppointment = :dateOfAppointment " +
            "ORDER BY a.timeOfAppointment ASC")
    List<Appointment> findByPatientIdAndDateOfAppointmentOrderByTimeOfAppointment
            (@Param("patientId") String patientId, @Param("dateOfAppointment") LocalDate dateOfAppointment);*/
}
