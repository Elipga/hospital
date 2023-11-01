package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.AppointmentInput;
import com.example.hospital.Controller.DTO.AppointmentOutput;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<String> addAppointment(@Valid @RequestBody AppointmentInput appointmentInput){
        try {
            appointmentService.addAppointment(appointmentInput);
            return ResponseEntity.ok().build();
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (TimeOutOfRangeException e) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        } catch (PatientDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        } catch (DateOutOfRange e) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentOutput>> getAllAppointments(){
        try {
            List<AppointmentOutput> appointmentsOutput = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointmentsOutput);
        } catch (IsEmptyException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
