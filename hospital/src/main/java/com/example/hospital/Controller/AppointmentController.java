package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.Appointment.AppointmentInput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutputHourAndAndPatient;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Add appointment", responses = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "208", description = "Already reported"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "412", description = "Precondition failed"),
            @ApiResponse(responseCode = "416", description = "Invalid time or date")}
    )
    @PostMapping("/appointments")
    public ResponseEntity<String> addAppointment(@Valid @RequestBody AppointmentInput appointmentInput){
        try {
            appointmentService.addAppointment(appointmentInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TimeOutOfRangeException e) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(e.getMessage());
        } catch (PatientDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getMessage());
        } catch (DateOutOfRange e) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(e.getMessage());
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all appointments", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAllAppointments(){
        try {
            List<AppointmentOutput> appointmentsOutput = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointmentsOutput);
        } catch (IsEmptyException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @Operation(summary = "Get appointments of patient", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @GetMapping("/patients/{patientDni}/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAppointmentsOfPatient
            (@PathVariable String patientDni, @RequestParam (name = "dateOfAppointment") String dateOfAppointment){
        try {
            List<AppointmentOutput> appointmentsOutput = appointmentService.getAppointmentsOfPatient(patientDni,dateOfAppointment);
            return ResponseEntity.ok(appointmentsOutput);
        } catch (PatientDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IsEmptyException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();

        }
    }
    @Operation(summary = "Get appointments of doctor of next temporal window", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @GetMapping("doctors/{collegeNumber}/appointments")
    public ResponseEntity<TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>>> getAppointmentsOfDoctorAndWeek(
            @PathVariable String collegeNumber){
        try {
            TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> appointmentsOutput = appointmentService.getAppointmentsOfDoctorAndWeek(collegeNumber);
            return ResponseEntity.ok(appointmentsOutput);
        } catch (StaffDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DoctorDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @Operation(summary = "Get busiest doctors for next temporal window", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @GetMapping("doctors/busy")
    public ResponseEntity<TreeMap<String, Integer>> getBusiestDoctors(){
        TreeMap<String, Integer> doctors = null;
        try {
            doctors = appointmentService.getBusiestDoctors();
            return ResponseEntity.ok(doctors);
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

    }
    @Operation(summary = "Get availability of doctor for next temporal window", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not found")}
    )
    @GetMapping("/doctors/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfDoctor(@PathVariable String collegeNumber){
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        try {
            if(!appointmentService.isDoctorOrNurse(collegeNumber)) throw new DoctorDoesNotExists("Doctor" +
                    "doesn´t exist");
            availabilityOutputs = appointmentService.getAvailabilityOfStaff(collegeNumber);
            return ResponseEntity.ok(availabilityOutputs);
        } catch (DoctorDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (StaffDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get availability of nurse for next temporal window", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not found")}
    )
    @GetMapping("/nurses/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfNurse(@PathVariable String collegeNumber){
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        try {
            if(appointmentService.isDoctorOrNurse(collegeNumber)) throw new NurseDoesNotExists("Nurse" +
                    "doesn´t exist");
            availabilityOutputs = appointmentService.getAvailabilityOfStaff(collegeNumber);
            return ResponseEntity.ok(availabilityOutputs);
        } catch (StaffDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NurseDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
