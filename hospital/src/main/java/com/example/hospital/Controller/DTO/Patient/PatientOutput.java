package com.example.hospital.Controller.DTO.Patient;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Domain.Patient;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class PatientOutput {
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters and spaces")
    private String name;
    public PatientOutput(String dni, String name) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        this.name = name;
    }
    public static PatientOutput getPatient(Patient patient) throws InvalidException {
        return new PatientOutput(patient.getDni(), patient.getName());
    }
}
