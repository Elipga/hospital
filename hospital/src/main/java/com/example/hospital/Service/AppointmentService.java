package com.example.hospital.Service;


import com.example.hospital.Controller.DTO.AppointmentInput;
import com.example.hospital.Controller.DTO.AppointmentOutput;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.AppointmentRepository;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import com.example.hospital.Repository.PatientRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    PatientRepository patientRepository;


    public List<AppointmentOutput> getAllAppointments() throws IsEmptyException {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentOutput> appointmentsOutput = new ArrayList<>();
        if (appointments.isEmpty()) throw new IsEmptyException("List of appointments is empty");


        for (Appointment appointment : appointments) {
            appointmentsOutput.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentsOutput;
    }

    public Optional<? extends HealthStaff> doctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return doctorRepository.findById(collegeNumber);
        else return nurseRepository.findById(collegeNumber);
    }

    public boolean isDoctorOrNurse(String collegeNumber){
        boolean isDoctor = false;
        if(doctorRepository.existsById(collegeNumber))
            return isDoctor = true;
        else return isDoctor = false;
    }


    public void addAppointment(AppointmentInput appointmentInput) throws StaffDoesNotExists, TimeOutOfRangeException, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, InvalidException {
        if ((!doctorRepository.existsById(appointmentInput.getCollegeNumber()) &&
                (!nurseRepository.existsById(appointmentInput.getCollegeNumber()))))
            throw new StaffDoesNotExists("Health staff does not exist");
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(appointmentInput.getCollegeNumber());
        if (appointmentInput.getTimeOfAppointment().isBefore(healthStaff.get().getStartingTime()) ||
                appointmentInput.getTimeOfAppointment().isAfter(healthStaff.get().getEndingTime())) throw new
                TimeOutOfRangeException("Time out of timetable"); //time before or after timetable of staff
        if (!patientRepository.existsById(appointmentInput.getPatientId())) throw new PatientDoesNotExists
                ("Patient does not exists");
        if (appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getCollegeNumber(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Appointment already exists");
        if (appointmentIsPosible(appointmentInput.getDateOfAppointment()) == false) throw new DateOutOfRange("Date" +
                "out of temporal window");
        Appointment newAppointment = AppointmentInput.getAppointment(appointmentInput);
        appointmentRepository.save(newAppointment);
    }




    public List<AppointmentOutput> getAppointmentsOfPatient(String patienId, String dateOfAppointment) throws PatientDoesNotExists, IsEmptyException {
        if (!patientRepository.existsById(patienId)) throw new PatientDoesNotExists("Patient doesn´t" +
                "exist");
        LocalDate date = LocalDate.parse(dateOfAppointment);
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndDateOfAppointmentOrderByTimeOfAppointment(patienId, date);
        if(appointments.isEmpty()) throw new IsEmptyException("Patient doesn´t have appointments that day");
        List<AppointmentOutput> appointmentOutputs = new ArrayList<>();

        for(Appointment appointment: appointments){
            appointmentOutputs.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentOutputs;
    }

    public List<AppointmentOutput> getAppointmentsofDoctorAndWeek(String collegeNumber) throws StaffDoesNotExists, DoctorDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");
        if(isDoctorOrNurse(collegeNumber) == false) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        LocalDate[] dates = appointmentsOfWeek();
        LocalDate firstDay = dates[0];
        LocalDate lastDay = dates[1];
        List<Appointment> appointments = appointmentRepository.findByCollegeNumberAndDateOfAppointmentBetween(collegeNumber,
                firstDay,lastDay);
        List<AppointmentOutput> appointmentsOutput = new ArrayList<>();

        for(Appointment appointment: appointments){
            appointmentsOutput.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentsOutput;
    }

    public LocalDate[] appointmentsOfWeek(){
        LocalDate date = LocalDate.now();
        LocalDate firstDay = LocalDate.now();
        LocalDate lastDay = LocalDate.now();
        int i = 0;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        DayOfWeek[] daysOfWeekArray = DayOfWeek.values(); //array with days of week
        List<DayOfWeek> daysOfWeek = Arrays.asList(daysOfWeekArray); //array to list

        for(DayOfWeek day: daysOfWeek){
            if(day == DayOfWeek.SATURDAY){
                firstDay = date.plusDays(2);
                lastDay = firstDay.plusDays(4);
            }
            if(day == DayOfWeek.SUNDAY){
                firstDay = date.plusDays(1);
                lastDay = firstDay.plusDays(4);
            }
            if(day == dayOfWeek){
               firstDay = date; //Ej: if it´s Tuesday, starting date is date of now
               lastDay = date.plusDays(4 - i); //ending date is Tuesday + 3 days = Friday
               break;
            }
            i++;
        }
        LocalDate[] dates = {firstDay, lastDay};

        return dates;
    }

    public LocalDate[] temporalWindowArray() {
        LocalDate date = LocalDate.now();
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate;
        boolean appointmentIsPosible = true;
        int i = 0;
        LocalDate[] dates = new LocalDate[2];

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        DayOfWeek[] daysOfWeekArray = DayOfWeek.values(); //array with days of week
        List<DayOfWeek> daysOfWeek = Arrays.asList(daysOfWeekArray); //array to list

        for (DayOfWeek day : daysOfWeek) { //Iterate through the list of the days of the week
            if (date.getDayOfWeek() == day) { //know day of the week when asking for appointment
                startingDate = date.plusDays(7 - i); //set starting date for next Monday
                endingDate = startingDate.plusDays(4); //set ending day for next Friday
                dates[0] = startingDate;
                dates[1] = endingDate;
                break;
            }
            i++;
            System.out.println();
        }
        return dates;
    }

    public boolean appointmentIsPosible(LocalDate dateOfAppointment){
        LocalDate[] dates = temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];
        boolean appointmentIsPosible = true;

        if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate)) {
            appointmentIsPosible = false;
        }
        return appointmentIsPosible;
    }
    /*public boolean temporalWindow(LocalDate dateOfAppointment) {
        LocalDate date = LocalDate.now();
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate;
        boolean appointmentIsPosible = true;
        int i = 0;

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        DayOfWeek[] daysOfWeekArray = DayOfWeek.values(); //array with days of week
        List<DayOfWeek> daysOfWeek = Arrays.asList(daysOfWeekArray); //array to list

        for (DayOfWeek day : daysOfWeek) { //Iterate through the list of the days of the week
            if (date.getDayOfWeek() == day) { //know day of the week when asking for appointment
                startingDate = date.plusDays(7 - i); //set starting date for next Monday
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate)) {
                    appointmentIsPosible = false;
                    break;
                }
            }
            i++;
            System.out.println();
        }
        return appointmentIsPosible;
    }*/
}
