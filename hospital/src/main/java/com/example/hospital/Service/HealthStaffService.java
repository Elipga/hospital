package com.example.hospital.Service;

import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.StaffDoesNotExists;
import com.example.hospital.Repository.AppointmentRepository;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@NoArgsConstructor
@Service
public class HealthStaffService { //common methods for doctor and nurse
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

    public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfStaff(String collegeNumber) throws StaffDoesNotExists {
        /*if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");*/
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(collegeNumber);
        LocalTime startingTime = healthStaff.get().getStartingTime();
        LocalTime endingTime = healthStaff.get().getEndingTime();
        LocalDate[] dates = temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];
        TreeMap<LocalDate, List<LocalTime>> availabilities = new TreeMap<>();
        LocalDate date = startingDate;

        while (date.isBefore(endingDate.plusDays(1))){//iterating through days of temporal window
            List<LocalTime> availability = new ArrayList<>();
            LocalTime time = startingTime; //reset time
            while (time.isBefore(endingTime)){ //iterating through hours of timetable
                if (!appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(
                        collegeNumber, date, time)) { //if appointment doesn´t exist
                    availability.add(time); //add to availability list
                }
                time = time.plusMinutes(15);
            }
            availabilities.put(date, availability);
            date = date.plusDays(1);
        }
        return availabilities;
    }

    public Optional<? extends HealthStaff> doctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return doctorRepository.findById(collegeNumber);
        else return nurseRepository.findById(collegeNumber);
    }

    public LocalDate[] temporalWindowArray() {
        LocalDate date = LocalDate.now();
        LocalDate[] dates = new LocalDate[2];

        LocalDate startingDate = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate endingDate = startingDate.plusDays(4);
        dates[0] = startingDate;
        dates[1] = endingDate;

        return dates;
    }
}
