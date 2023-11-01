package com.example.hospital.Domain;

import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
//@Table(name = "timetables")
@Getter
@Setter
@NoArgsConstructor
public class Timetable {
    @Id
    @NotNull(message = "Id is null")
    @NotEmpty(message = "Id is null")
    private String timetableId;
    @NotNull(message = "StaffID is null")
    @NotEmpty(message = "StaffID is null")
    private String staffId;

    @NotNull(message = "Week number is null")
    @NotEmpty(message = "Week number is null")
    private LocalDate dayOfWeek;

    //We supposed they have appointments 6h per day
    @NotNull(message = "Starting time is null")
    @NotEmpty(message = "Starting time is null")
    //@Min(value = 8, message = "Minimum starting time is 8h")
    //@Max(value = 14, message = "Maximum starting time is 14h")
    private LocalTime startingTime;

    @NotNull(message = "Ending time is null")
    @NotEmpty(message = "Ending time is null")
    //@Min(value = 14, message = "Minimum ending time is 14h")
    //@Max(value = 20, message = "Maximum ending time is 20h")
    private LocalTime endingTime;

    public Timetable(String staffId,LocalDate dayOfWeek, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        validateId(staffId);
        this.staffId = staffId;
        this.dayOfWeek = getDayOfWeek();
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    private static void validateId(String id) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
    }
}
