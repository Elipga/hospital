package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.Patient.PatientInput;
import com.example.hospital.Controller.DTO.Patient.PatientOutput;
import com.example.hospital.Domain.Patient;
import com.example.hospital.Exception.AlreadyExistsException;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public List<PatientOutput> getAllPatients() throws IsEmptyException {
        List<Patient> patients = patientRepository.findAll();
        List<PatientOutput> patientsOutput = new ArrayList<>();
        if(patients.isEmpty()) throw new IsEmptyException("List of patients is empty");

        for(Patient patient: patients){
            patientsOutput.add(PatientOutput.getPatient(patient));
        }
        return patientsOutput;
    }

    public void addPatient(PatientInput patientInput) throws AlreadyExistsException, InvalidException {
        Patient newPatient = PatientInput.getPatient(patientInput);
        if(patientRepository.existsById(patientInput.getDni())) throw new AlreadyExistsException
                ("Patient already exists");
        else patientRepository.save(newPatient);
    }

}
