package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.AvailabilityOutput;
import com.example.hospital.Controller.DTO.AvailabilityOutputHour;
import com.example.hospital.Domain.Availability;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.StaffDoesNotExists;
import com.example.hospital.Repository.AppointmentRepository;
import com.example.hospital.Repository.AvailabilityRepository;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

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


    public TreeMap<DayOfWeek, List<AvailabilityOutputHour>> getAvailabilityOfStaff(String collegeNumber) throws StaffDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");
        Optional<? extends HealthStaff> healthStaff = appointmentService.doctorOrNurse(collegeNumber); //finding staff object
        List<AvailabilityOutput> availabilitiesOutput = new ArrayList<>();
        availabilityRepository.removeByCollegeNumber(collegeNumber); //delete the availability
        addAvailabilityOfStaff(collegeNumber); //adding the availability of the week
        List<Availability> availabilitiesOfStaff = availabilityRepository.findByCollegeNumber(collegeNumber); //creating a lis with availability

        return creatingAvailabilityColection(availabilitiesOfStaff);

    }

    public TreeMap<DayOfWeek, List<AvailabilityOutputHour>> creatingAvailabilityColection(List<Availability> availabilitiesOfStaff) {
        TreeMap<DayOfWeek, List<AvailabilityOutputHour>> availabilityPerDay = new TreeMap<>();

        for (Availability availability : availabilitiesOfStaff) {
            DayOfWeek dayOfWeek = availability.getDay().getDayOfWeek();

            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                // Verificar si ya hay una lista para ese día
                if (!availabilityPerDay.containsKey(dayOfWeek)) {
                    // Si no existe, crear una nueva lista
                    availabilityPerDay.put(dayOfWeek, new ArrayList<>());
                }
                // Obtener la lista existente para ese día y agregar la disponibilidad
                List<AvailabilityOutputHour> availabilities = availabilityPerDay.get(dayOfWeek);
                availabilities.add(AvailabilityOutputHour.getAvailability(availability));
            }
        }
        return availabilityPerDay;
    }

    public void addAvailabilityOfStaff(String collegeNumber) {
        Optional<? extends HealthStaff> healthStaff = appointmentService.doctorOrNurse(collegeNumber);
        LocalTime startingTime = healthStaff.get().getStartingTime();
        LocalTime endingTime = healthStaff.get().getEndingTime();
        LocalDate[] dates = appointmentService.temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];

        for (LocalDate date = startingDate; date.isBefore(endingDate.plusDays(1));
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
        /*Hours inside timetable of health staff:
        Days: Starting with Monday, while date is before Friday, adding 1 day in each iteration
        Hours: Starting with startingTime of health staff, while starting time is before ending time
        of health staff, adding 15 mins per appointment*/
    }

    public boolean isDoctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return true;
        else return false;
    }
}
