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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
        if(appointments.isEmpty()) throw new IsEmptyException("List of appointments is empty");


        for(Appointment appointment: appointments){
            appointmentsOutput.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentsOutput;
    }

    public Optional<? extends HealthStaff> doctorOrNurse(String staffId){
        if(doctorRepository.existsById(staffId))
            return doctorRepository.findById(staffId);
        else return nurseRepository.findById(staffId);
    }


    public void addAppointment(AppointmentInput appointmentInput) throws StaffDoesNotExists, TimeOutOfRangeException, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, InvalidException {
        System.out.println("1");
        System.out.println(appointmentInput.getStaffId());
        System.out.println(appointmentInput.getPatientId());
        System.out.println(appointmentInput.getDateOfAppointment());
        System.out.println(appointmentInput.getTimeOfAppointment());
        if ((!doctorRepository.existsById(appointmentInput.getStaffId()) &&
                (!nurseRepository.existsById(appointmentInput.getStaffId())))) throw new StaffDoesNotExists ("Health staff does not exist");
        System.out.println("2");
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(appointmentInput.getStaffId());
        System.out.println("3");
        if (appointmentInput.getTimeOfAppointment().isBefore(healthStaff.get().getStartingTime()) ||
                appointmentInput.getTimeOfAppointment().isAfter(healthStaff.get().getEndingTime())) throw new
                TimeOutOfRangeException("Time out of timetable"); //time before or after timetable of staff
        System.out.println("4");
        if (!patientRepository.existsById(appointmentInput.getPatientId())) throw new PatientDoesNotExists
                ("Patient does not exists");
        System.out.println("5");
        if (appointmentRepository.existsByStaffIdAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getStaffId(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Appointment already exists");
        System.out.println("6");
        if(temporalWindow(appointmentInput.getDateOfAppointment()) == false) throw new DateOutOfRange("Date" +
                "out of temporal window");
        System.out.println("7");
        Appointment newAppointment = AppointmentInput.getAppointment(appointmentInput);
        System.out.println("8");
        appointmentRepository.save(newAppointment);
        System.out.println("9");
    }

    public boolean temporalWindow(LocalDate dateOfAppointment) {
        LocalDate date = LocalDate.now();
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate = startingDate.plusDays(4);
        boolean appointmentIsPosible = true;

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        switch (dayOfWeek) {
            case MONDAY:
                startingDate = date.plusDays(7);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case TUESDAY:
                startingDate = date.plusDays(6);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case WEDNESDAY:
                startingDate = date.plusDays(5);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case THURSDAY:
                startingDate = date.plusDays(4);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case FRIDAY:
                startingDate = date.plusDays(3);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case SATURDAY:
                startingDate = date.plusDays(2);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
            case SUNDAY:
                startingDate = date.plusDays(1);
                endingDate = startingDate.plusDays(4);
                if (dateOfAppointment.isBefore(startingDate) || dateOfAppointment.isAfter(endingDate))
                    appointmentIsPosible = false;
                break;
        }
        return appointmentIsPosible;
    }
}
