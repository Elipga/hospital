package com.example.hospital.Repository;

import com.example.hospital.Domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    boolean existsByStaffIdAndDateOfAppointmentAndTimeOfAppointment (String StaffId, LocalDate dateOfAppointment,
                                                                  LocalTime timeOfAppointment);


}
