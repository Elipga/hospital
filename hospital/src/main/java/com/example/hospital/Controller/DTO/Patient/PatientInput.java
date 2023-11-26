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
public class PatientInput {
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters without accent mark")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9,. ]+$", message = "Address contains only letters, numbers, commas" +
            " and periods")    private String address;
    public PatientInput(String dni, String name, String address) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        this.name = name;
        this.address = address;
    }
    public static Patient getPatient(PatientInput patientInput) throws InvalidException {
        return new Patient(patientInput.getDni(), patientInput.getName(), patientInput.getAddress());
    }
}
