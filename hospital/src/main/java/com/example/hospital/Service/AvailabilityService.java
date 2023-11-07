package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.AvailabilityOutput;
import com.example.hospital.Domain.Availability;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.StaffDoesNotExists;
import com.example.hospital.Repository.AppointmentRepository;
import com.example.hospital.Repository.AvailabilityRepository;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {
    @Autowired
    AvailabilityRepository availabilityRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentRepository appointmentRepository;


    public List<AvailabilityOutput> getAvailabilityOfStaff(String collegeNumber) throws StaffDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");
        LocalDate[] dates = appointmentService.appointmentsOfWeek();
        LocalDate firstDay = dates[0];
        LocalDate lastDay = dates[1];
        Optional<? extends HealthStaff> healthStaff = appointmentService.doctorOrNurse(collegeNumber);
        List<AvailabilityOutput> availabilitiesOutput = new ArrayList<>();
        availabilityRepository.removeByCollegeNumber(collegeNumber);
        addAvailabilityOfStaff(collegeNumber); //añade con los datos de la nueva semana
        List<Availability> availabilitiesOfStaff = availabilityRepository.findByCollegeNumber(collegeNumber);

        for(Availability availability: availabilitiesOfStaff){
            availabilitiesOutput.add(AvailabilityOutput.getAvailability(availability));
        }
        return availabilitiesOutput;
    }

    public void addAvailabilityOfStaff(String collegeNumber){
        Optional<? extends HealthStaff> healthStaff = appointmentService.doctorOrNurse(collegeNumber);
        LocalTime startingTime = healthStaff.get().getStartingTime();
        LocalTime endingTime = healthStaff.get().getEndingTime();
        List<LocalTime> availabilities = new ArrayList<>();
        LocalDate[] dates = appointmentService.temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];

        /*Hours inside timetable of health staff:
        Days: Starting with Monday, while date is before Friday, adding 1 day in each iteration
        Hours: Starting with startingTime of health staff, while starting time is before ending time
        of health staff, adding 15 mins per appointment*/

        for(LocalDate date = startingDate; date.isBefore(endingDate.plusDays(1));
            date = date.plusDays(1)) { //iterating through days of temporal window
            for (LocalTime time = startingTime; time.isBefore(endingTime);
                 time = time.plusMinutes(15)) { //iterating through hours of timetable
                if (!appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(
                        collegeNumber, date, time)) { //if appointment doesn´t exist
                    Availability availability = new Availability(collegeNumber, date, time);
                    availabilityRepository.save(availability); //add new availability
                }
            }
        }
    }

    public boolean isDoctorOrNurse(String collegeNumber){
        boolean isDoctor = false;
        if(doctorRepository.existsById(collegeNumber))
            return isDoctor = true;
        else return isDoctor = false;
    }
}
