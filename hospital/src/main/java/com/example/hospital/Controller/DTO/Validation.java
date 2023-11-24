package com.example.hospital.Controller.DTO;

import com.example.hospital.Exception.InvalidException;

import java.time.LocalTime;
public class Validation {
    public void validateDni(String dni) throws InvalidException {
        if((dni.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!dni.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("DNI must contain 8 numbers " +
                "and 1 letter");
    }
    public void validateCNumber(String collegeNumber) throws InvalidException {
        if((collegeNumber.length() != 9)) throw new InvalidException("College number has 9 chars");
    }
    public void validateTime(LocalTime time) throws InvalidException {
        if ((time.getMinute() != 00) && (time.getMinute() != 15) &&
                (time.getMinute() != 30) && (time.getMinute() != 45))
            throw new InvalidException("Time appointment has to be at minute 00, 15, 30 or 45");
    }
    public void validateTime(LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        if(startingTime.isAfter(endingTime)) throw new InvalidException("Starting time must be" +
                "before ending time");
        if(startingTime.equals(endingTime)) throw new InvalidException("Starting time and ending time" +
                "can not be the same");

        String startString = "08:00:00";
        String endString = "20:00:00";

        LocalTime start = LocalTime.parse(startString);
        LocalTime end = LocalTime.parse(endString);

        if(startingTime.isBefore(start)) throw new InvalidException("Minimum starting time is 8h");
        if(endingTime.isAfter(end)) throw new InvalidException("Maximum ending time is 20h");
    }
    public void validateYearsExperience(int yearsOfExperience) throws InvalidException {
        String stringYears = String.valueOf(yearsOfExperience);
        if(!stringYears.matches("^[0-9]+$")) throw new InvalidException("Years of experience must be" +
                "a number");
    }
}
