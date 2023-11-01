package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.PatientInput;
import com.example.hospital.Controller.DTO.PatientOutput;
import com.example.hospital.Exception.AlreadyExistsException;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Service.PatientService;
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

    @GetMapping("/patients")
    public ResponseEntity<List<PatientOutput>> getAllPatients(){
        try {
            List<PatientOutput> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (IsEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/patients")
    public ResponseEntity<String> addPatient(@Valid @RequestBody PatientInput patientInput){
        try {
            patientService.addPatient(patientInput);
            return ResponseEntity.ok().build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
}
