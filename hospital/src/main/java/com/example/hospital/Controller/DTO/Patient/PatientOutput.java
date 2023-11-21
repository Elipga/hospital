package com.example.hospital.Controller.DTO.Patient;

import com.example.hospital.Domain.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatientOutput {
    private String dni;
    private String name;
    public PatientOutput(String dni, String name) {
        this.dni = dni;
        this.name = name;
    }
    public static PatientOutput getPatient(Patient patient) {
        return new PatientOutput(patient.getDni(), patient.getName());
    }
}
