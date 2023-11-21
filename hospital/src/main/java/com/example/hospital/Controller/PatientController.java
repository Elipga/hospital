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
    @Operation(summary = "Add patient", responses = {
            @ApiResponse(responseCode = "201", description = "Patient created"),
            @ApiResponse(responseCode = "208", description = "Already reported"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @PostMapping("/patients")
    public ResponseEntity<String> addPatient(@Valid @RequestBody PatientInput patientInput){
        try {
            patientService.addPatient(patientInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getMessage());
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }
    @Operation(summary = "Get patients", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Patient not found") }
    )
    @GetMapping("/patients")
    public ResponseEntity<List<PatientOutput>> getAllPatients(){
        try {
            List<PatientOutput> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (IsEmptyException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
