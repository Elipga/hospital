package com.example.hospital.Domain;

import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "patients")
@NoArgsConstructor
public class Patient {
    @Id
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    private String name;
    private String adress;

    //private cita cita;

    public Patient(String id) {
        this.id = id;
    }

    public Patient(String id, String name, String adress) throws InvalidException {
        validateId(id);
        this.id = id;
        this.name = name;
        this.adress = adress;
    }

    private static void validateId(String id) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
    }
}
