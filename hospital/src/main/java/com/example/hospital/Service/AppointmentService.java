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

    public List<AppointmentOutput> getAllAppointments() throws IsEmptyException, InvalidException {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentOutput> appointmentsOutput = new ArrayList<>();
        if (appointments.isEmpty()) throw new IsEmptyException("List of appointments is empty");

        for (Appointment appointment : appointments) {
            appointmentsOutput.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentsOutput;
    }

    public void addAppointment(AppointmentInput appointmentInput) throws InvalidException, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, StaffDoesNotExists, TimeOutOfRangeException {
        validateAppointment(appointmentInput);
        Appointment newAppointment = AppointmentInput.getAppointment(appointmentInput);
        appointmentRepository.save(newAppointment);
    }

    public void validateAppointment(AppointmentInput appointmentInput) throws StaffDoesNotExists, PatientDoesNotExists, AlreadyExistsException, DateOutOfRange, TimeOutOfRangeException {
        if ((!doctorRepository.existsById(appointmentInput.getCollegeNumber()) &&
                (!nurseRepository.existsById(appointmentInput.getCollegeNumber()))))
            throw new StaffDoesNotExists("Health staff does not exist");
        if (!patientRepository.existsById(appointmentInput.getPatientDni())) throw new PatientDoesNotExists
                ("Patient does not exists");
        if (appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getCollegeNumber(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Health staff has already an appointment");
        if(appointmentRepository.existsByPatientDniAndDateOfAppointmentAndTimeOfAppointment(appointmentInput.getPatientDni(),
                appointmentInput.getDateOfAppointment(), appointmentInput.getTimeOfAppointment())) throw new
                AlreadyExistsException("Patient has already an appointment");
        if (!appointmentIsPossibleDate(appointmentInput.getDateOfAppointment())) throw new DateOutOfRange("Date" +
                "out of temporal window");
        if(!appointmentIsPossibleTime(appointmentInput)) throw new TimeOutOfRangeException("Time out of timetable"); //time before or after timetable of staff
    }

    public List<AppointmentOutput> getAppointmentsOfPatient(String patientDni, String dateOfAppointment) throws PatientDoesNotExists, IsEmptyException, InvalidException {
        if (!patientRepository.existsById(patientDni)) throw new PatientDoesNotExists("Patient doesn´t" +
                "exist");
        LocalDate date = LocalDate.parse(dateOfAppointment);
        List<Appointment> appointments = appointmentRepository.findByPatientDniAndDateOfAppointmentOrderByTimeOfAppointment(patientDni, date);
        if(appointments.isEmpty()) throw new IsEmptyException("Patient doesn´t have appointments that day");
        List<AppointmentOutput> appointmentOutputs = new ArrayList<>();

        for(Appointment appointment: appointments){
            appointmentOutputs.add(AppointmentOutput.getAppointment(appointment));
        }
        return appointmentOutputs;
    }

    public TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> getAppointmentsOfDoctorAndWeek(String collegeNumber) throws StaffDoesNotExists, DoctorDoesNotExists, InvalidException {
        if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");
        if(!isDoctorOrNurse(collegeNumber)) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        LocalDate[] dates = healthStaffService.temporalWindowArray();
        LocalDate firstDay = dates[0];
        LocalDate lastDay = dates[1];
        TreeMap<LocalDate, List<AppointmentOutputHourAndAndPatient>> appointmentsOfWeek = new TreeMap<>();
        List<Appointment> appointments = appointmentRepository.findByCollegeNumberAndDateOfAppointmentBetween(collegeNumber,
                firstDay,lastDay);

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

    /*public TreeMap<String, Integer> getBusiestDoctors() throws InvalidException {
        List<Doctor> doctors = doctorRepository.findAll();
        TreeMap<String, Integer> doctorsOutput = new TreeMap<>();
        //TreeMap<String, Integer> doctorsOutput = new TreeMap(Comparator.comparingLong(DoctorOutputNumberOfAppointments::getNumberOfAppointments).reversed());

        LocalDate[] dates = healthStaffService.temporalWindowArray();
        LocalDate firstDay = dates[0];

        for(Doctor doctor: doctors){ //all doctors
            int appointmentCounter = 0; //set counter to 0
            List<Appointment> appointments = appointmentRepository.findByCollegeNumber(doctor.getCollegeNumber());
            //appointments of doctor (by college number)
            for(Appointment appointment: appointments){
                if(appointment.getDateOfAppointment().isAfter(firstDay.minusDays(1))){ //if appointment is on the week of temporal window
                    appointmentCounter = appointmentCounter + 1;} //add +1 to counter
            }
            doctorsOutput.put(doctor.getCollegeNumber(), appointmentCounter);
        }
        return doctorsOutput;
    }*/

    /*public LocalDate[] temporalWindowArray() {
        LocalDate date = LocalDate.now();
        LocalDate[] dates = new LocalDate[2];

        LocalDate startingDate = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate endingDate = startingDate.plusDays(4);
        dates[0] = startingDate;
        dates[1] = endingDate;

        return dates;
    }*/
    public Optional<? extends HealthStaff> doctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return doctorRepository.findById(collegeNumber);
        else return nurseRepository.findById(collegeNumber);
    }

    public boolean isDoctorOrNurse(String collegeNumber){
        return doctorRepository.existsById(collegeNumber);
    }

    /*public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfStaff2(String collegeNumber) throws StaffDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber) &&
                (!nurseRepository.existsById(collegeNumber))))
            throw new StaffDoesNotExists("Health staff doesn't exist");
        Optional<? extends HealthStaff> healthStaff = doctorOrNurse(collegeNumber);
        LocalTime startingTime = healthStaff.get().getStartingTime();
        LocalTime endingTime = healthStaff.get().getEndingTime();
        LocalDate[] dates = temporalWindowArray();
        LocalDate startingDate = dates[0];
        LocalDate endingDate = dates[1];
        TreeMap<LocalDate, List<LocalTime>> availabilities = new TreeMap<>();

        for (LocalDate date = startingDate; date.isBefore(endingDate.plusDays(1));
             date = date.plusDays(1)) { //iterating through days of temporal window
            List<LocalTime> availability = new ArrayList<>();
            for (LocalTime time = startingTime; time.isBefore(endingTime);
                 time = time.plusMinutes(15)) { //iterating through hours of timetable
                if (!appointmentRepository.existsByCollegeNumberAndDateOfAppointmentAndTimeOfAppointment(
                        collegeNumber, date, time)) { //if appointment doesn´t exist
                    availability.add(time);
                }
            }
            availabilities.put(date, availability);
        }
        Hours inside timetable of health staff:
        Days: Starting with Monday, while date is before Friday, adding 1 day in each iteration
        Hours: Starting with startingTime of health staff, while starting time is before ending time
        of health staff, adding 15 mins per appointment

        return availabilities;
    }*/

    /*public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfDoctor(String collegeNumber) throws DoctorDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilities;
        if(!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor" +
                "doesn´t exist");
        else{
            availabilities = getAvailabilityOfStaff(collegeNumber);
            return availabilities;
        }
    }*/
    /*public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfNurse(String collegeNumber) throws StaffDoesNotExists, NurseDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilities;
        if(!nurseRepository.existsById(collegeNumber)) throw new NurseDoesNotExists("Nurse" +
                "doesn´t exist");
        else{
            availabilities = getAvailabilityOfStaff(collegeNumber);
            return availabilities;
        }
    }*/

    /*public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfStaff(String collegeNumber) throws StaffDoesNotExists {

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
    }*/
}
