package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.Patient.PatientInput;
import com.example.hospital.Controller.DTO.Patient.PatientOutput;
import com.example.hospital.Exception.AlreadyExistsException;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class PatientController {
    @Autowired
    PatientService patientService;
    @Operation(summary = "Add patient")
    @PostMapping("/patients")
    public ResponseEntity<String> addPatient(@Valid @RequestBody PatientInput patientInput) throws AlreadyExistsException, InvalidException {
            patientService.addPatient(patientInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Get patients")
    @GetMapping("/patients")
    public ResponseEntity<List<PatientOutput>> getAllPatients() throws IsEmptyException, InvalidException {
            List<PatientOutput> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
    }
}
