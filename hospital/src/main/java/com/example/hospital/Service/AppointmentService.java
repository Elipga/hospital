package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.Appointment.AppointmentInput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutput;
import com.example.hospital.Controller.DTO.Appointment.AppointmentOutputHourAndAndPatient;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    HealthStaffService healthStaffService;
    public void addAppointment(AppointmentInput appointmentInput) throws InvalidException, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, StaffDoesNotExists, TimeOutOfRangeException {
        validateAppointment(appointmentInput);
        Appointment newAppointment = AppointmentInput.getAppointment(appointmentInput);
        appointmentRepository.save(newAppointment);
    }
    public void validateAppointment(AppointmentInput appointmentInput) throws StaffDoesNotExists, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, TimeOutOfRangeException, InvalidException {
        if ((!doctorRepository.existsById(appointmentInput.getCollegeNumber()) &&
                (!nurseRepository.existsById(appointmentInput.getCollegeNumber()))))
            throw new StaffDoesNotExists("Health staff does not exist");
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(appointmentInput.getCollegeNumber());
        if (!patientRepository.existsById(appointmentInput.getPatientDni())) throw new PatientDoesNotExists
                ("Patient does not exists");
        if (appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getCollegeNumber(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Health staff has already an appointment");
        if(appointmentRepository.existsByPatientDniAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getPatientDni(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Patient has already an appointment");
        if(appointmentRepository.existsByCollegeNumberAndPatientDniAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getCollegeNumber(),
                appointmentInput.getPatientDni(),appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Appointment already exists");
        if(healthStaff.get().getDni().equals(appointmentInput.getPatientDni())) throw new InvalidException("" +
               "Patient and health staff has to be different people");
        if (!appointmentIsPossibleDate(appointmentInput.getDateOfAppointment())) throw new DateOutOfRange("Date " +
                "out of temporal window");
        if(!appointmentIsPossibleTime(appointmentInput)) throw new TimeOutOfRangeException("Time out of timetable"); //time before or after timetable of staff
    }
    public List<AppointmentOutput> getAppointmentsOfPatient(String patientDni, String dateOfAppointment) throws PatientDoesNotExists, IsEmptyException, InvalidException {
        if (!patientRepository.existsById(patientDni)) throw new PatientDoesNotExists("Patient doesn´t" +
                "exist");
        if(!dateOfAppointment.matches("\\d{4}-\\d{2}-\\d{2}")) throw new InvalidException("Format of " +
                "date of appointment is YYYY-MM-DD");
        LocalDate date = LocalDate.parse(dateOfAppointment);
        List<Appointment> appointments = appointmentRepository.findByPatientDniAndDateOfAppointmentOrderByTimeOfAppointment(patientDni, date);
        if(appointments.isEmpty()) throw new IsEmptyException("Patient doesn´t have appointments that day");
        List<AppointmentOutput> appointmentOutputs = new ArrayList<>();

        for(Appointment appointment: appointments){
            appointmentOutputs.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentOutputs;
    }
    public TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> getAppointmentsOfDoctorAndWeek(String collegeNumber) throws StaffDoesNotExists, DoctorDoesNotExists, InvalidException, IsEmptyException {
        if(!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor doen´t exist");
        LocalDate[] dates = healthStaffService.temporalWindowArray();
        LocalDate firstDay = dates[0];
        LocalDate lastDay = dates[1];
        TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> appointmentsOfWeek = new TreeMap<>();
        List<Appointment> appointments = appointmentRepository.findByCollegeNumberAndDateOfAppointmentBetween(collegeNumber,
                firstDay,lastDay);
        if(appointments.isEmpty()) throw new IsEmptyException("Doctor doesn´t have appointments for the week");

        while (firstDay.isBefore(lastDay.plusDays(1))){ //Iterating though days of week (temporal window)
            ArrayList<AppointmentOutputHourAndAndPatient> appointmentOutputs = new ArrayList<>(); // create ArrayList for outputs
            for(Appointment appointment: appointments){ //iterating though appointments on the week
                if(appointment.getDateOfAppointment().isEqual(firstDay)){ //if date of appointment is same as day of the week
                    AppointmentOutputHourAndAndPatient appointmentOutput = AppointmentOutputHourAndAndPatient.getAppointment(appointment);
                    appointmentOutputs.add(appointmentOutput); //save appointment in the arraylist
                }
            }
            appointmentsOfWeek.put(firstDay, appointmentOutputs); //save in the TreeMap
            firstDay = firstDay.plusDays(1); //adding one day to continue
        }
        return appointmentsOfWeek; //return TreeMap
    }

    public boolean appointmentIsPossibleDate(LocalDate dateOfAppointment){
        LocalDate[] dates = healthStaffService.temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];

        return !dateOfAppointment.isBefore(startingDate) && !dateOfAppointment.isAfter(endingDate);
    }
    public boolean appointmentIsPossibleTime(AppointmentInput appointmentInput){
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(appointmentInput.getCollegeNumber());
        if (appointmentInput.getTimeOfAppointment().isBefore(healthStaff.get().getStartingTime()) || //time before or after timetable of staff
                appointmentInput.getTimeOfAppointment().isAfter(healthStaff.get().getEndingTime())) {
            return false;
        }
        //if the appointment is same hour of ending time
        return appointmentInput.getTimeOfAppointment() != healthStaff.get().getEndingTime();
    }
    public Optional<? extends HealthStaff> doctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return doctorRepository.findById(collegeNumber);
        else return nurseRepository.findById(collegeNumber);
    }
}
