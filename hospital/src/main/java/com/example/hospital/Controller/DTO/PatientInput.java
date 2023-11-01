package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Patient;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PatientInput {
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    private String name;
    private String adress;

    public PatientInput(String id, String name, String adress) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
        this.id = id;
        this.name = name;
        this.adress = adress;
    }

    public static Patient getPatient(PatientInput patientInput) throws InvalidException {
        return new Patient(patientInput.getId(), patientInput.getName(), patientInput.getAdress());
    }
}
