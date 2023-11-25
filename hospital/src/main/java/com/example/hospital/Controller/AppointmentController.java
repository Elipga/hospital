package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.Appointment.AppointmentInput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutputHourAndAndPatient;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@RestController
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;
    @Operation(summary = "Add appointment")
    @PostMapping("/appointments")
    public ResponseEntity<String> addAppointment(@Valid @RequestBody AppointmentInput appointmentInput) throws PatientDoesNotExists,
            AlreadyExistsException, InvalidException, DateOutOfRange, StaffDoesNotExists, TimeOutOfRangeException {
            appointmentService.addAppointment(appointmentInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Get appointments of patient")
    @GetMapping("/patients/{patientDni}/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAppointmentsOfPatient
            (@PathVariable String patientDni, @RequestParam (name = "dateOfAppointment") String dateOfAppointment) throws PatientDoesNotExists, IsEmptyException, InvalidException {
            List<AppointmentOutput> appointmentsOutput = appointmentService.getAppointmentsOfPatient(patientDni,dateOfAppointment);
            return ResponseEntity.ok(appointmentsOutput);
    }
    @Operation(summary = "Get appointments of doctor of next temporal window")
    @GetMapping("doctors/{collegeNumber}/appointments")
    public ResponseEntity<TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>>> getAppointmentsOfDoctorAndWeek(
            @PathVariable String collegeNumber) throws InvalidException, StaffDoesNotExists, DoctorDoesNotExists, IsEmptyException {
            TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> appointmentsOutput =
                    appointmentService.getAppointmentsOfDoctorAndWeek(collegeNumber);
            return ResponseEntity.ok(appointmentsOutput);
    }
}
