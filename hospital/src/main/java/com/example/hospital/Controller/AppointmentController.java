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
import java.time.LocalTime;
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

    @Operation(summary = "Get all appointments")
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAllAppointments() throws IsEmptyException, InvalidException {
            List<AppointmentOutput> appointmentsOutput = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointmentsOutput);
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
    /*@Operation(summary = "Get busiest doctors for next temporal window")
    @GetMapping("doctors/busy")
    public ResponseEntity<TreeMap<String, Integer>> getBusiestDoctors() throws InvalidException {
        TreeMap<String, Integer> doctors = null;
            doctors = appointmentService.getBusiestDoctors();
            return ResponseEntity.ok(doctors);
    }*/


    /*@Operation(summary = "Get availability of doctor for next temporal window")
    @GetMapping("/doctors/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfDoctor(@PathVariable String collegeNumber) throws DoctorDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        availabilityOutputs = appointmentService.getAvailabilityOfDoctor(collegeNumber);
        return ResponseEntity.ok(availabilityOutputs);
    }*/

    /*@Operation(summary = "Get availability of nurse for next temporal window")
    @GetMapping("/nurses/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfNurse(@PathVariable String collegeNumber) throws NurseDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        availabilityOutputs = appointmentService.getAvailabilityOfNurse(collegeNumber);
        return ResponseEntity.ok(availabilityOutputs);
    }*/

}
