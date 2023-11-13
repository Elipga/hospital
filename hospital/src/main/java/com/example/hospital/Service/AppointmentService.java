package com.example.hospital.Service;


import com.example.hospital.Controller.DTO.AppointmentInput;
import com.example.hospital.Controller.DTO.AppointmentOutput;
import com.example.hospital.Controller.DTO.DoctorOutputnumberOfAppointments;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Domain.Doctor;
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
        if(doctorRepository.existsById(collegeNumber))
            return true;
        else return false;
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
        LocalDate[] dates = temporalWindowArray();
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

    public LocalDate[] temporalWindowArray() {
        LocalDate date = LocalDate.now();
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate;
        int i = 0;
        LocalDate[] dates = new LocalDate[2];

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

    public TreeSet<DoctorOutputnumberOfAppointments> getMostbussydoctors(){
        List<Doctor> doctors = doctorRepository.findAll();

        TreeSet<DoctorOutputnumberOfAppointments> doctorsOutput = new TreeSet<>(Comparator.comparingLong(DoctorOutputnumberOfAppointments::getNumberOfAppointments).reversed());

        LocalDate[] dates = temporalWindowArray();
        LocalDate firstDay = dates[0];
        long appointmentCounter = 0;

        for(Doctor doctor: doctors){ //al doctors
            appointmentCounter = 0; //set counter to 0
            List<Appointment> appointments = appointmentRepository.findByCollegeNumber(doctor.getCollegeNumber());
            //appointments of doctor (by college number)
            for(Appointment appointment: appointments){
                if(appointment.getDateOfAppointment().isAfter(firstDay.minusDays(1))){ //if appointment is on the week of temporal window
                    appointmentCounter = appointmentCounter + 1;} //add +1 to counter
                //doctor.setNumberOfAppointments(appointmentCounter); //set number of appointments with counter
            }
        }

        for(Doctor doctor: doctors){ //convert doctor to doctorOutput
            doctorsOutput.add(DoctorOutputnumberOfAppointments.getDoctor(doctor, appointmentCounter));
        }
        return doctorsOutput;
    }

}
