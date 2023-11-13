package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Nurse;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class NurseInput {
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    private String collegeNumber;

    private String name;

    @NotNull(message = "Starting time is null")
    //@Min(value = 8, message = "Minimum starting time is 8h")
    //@Max(value = 14, message = "Maximum starting time is 14h")
    private LocalTime startingTime;

    @NotNull(message = "Ending time is null")
    //@Min(value = 14, message = "Minimum ending time is 14h")
    //@Max(value = 20, message = "Maximum ending time is 20h")
    private LocalTime endingTime;

    public NurseInput(String id, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
        this.id = id;
        if((collegeNumber.length() != 9)) throw new InvalidException("College number has 9 chars");
        this.collegeNumber = collegeNumber;
        this.name = name;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public static Nurse getNurse(NurseInput nurseInput) throws InvalidException {
        return new Nurse(nurseInput.getId(), nurseInput.getCollegeNumber(), nurseInput.name, nurseInput.startingTime,
                nurseInput.getEndingTime());
    }
}
