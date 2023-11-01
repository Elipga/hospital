package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PatientOutput {
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    private String name;

    public PatientOutput(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PatientOutput getPatient(Patient patient) {
        return new PatientOutput(patient.getId(), patient.getName());
    }
}
