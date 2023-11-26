package com.example.hospital.Domain;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Table(name = "patients")
@NoArgsConstructor
public class Patient {
    @Id
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters without accent mark")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9,. ]+$", message = "Address contains only letters, numbers, commas" +
            " and periods")
    private String address;
    public Patient(String dni) {

        this.dni = dni;
    }
    public Patient(String dni, String name, String address) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        this.name = name;
        this.address = address;
    }
}
