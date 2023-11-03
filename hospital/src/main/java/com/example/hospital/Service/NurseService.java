package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.NurseInput;
import com.example.hospital.Controller.DTO.NurseOutput;
import com.example.hospital.Domain.Nurse;
import com.example.hospital.Exception.AlreadyExistsException;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NurseService {

    @Autowired
    NurseRepository nurseRepository;

    public List<NurseOutput> getAllNurses() throws IsEmptyException {
        List<Nurse> nurses = nurseRepository.findAll();
        List<NurseOutput> nursesOutput = new ArrayList<>();
        if(nurses.isEmpty()) throw new IsEmptyException("List of nurses is empty");

        for(Nurse nurse: nurses){
            nursesOutput.add(NurseOutput.getNurse(nurse));
        }
        return nursesOutput;
    }

    public void addNurse(NurseInput nurseInput) throws AlreadyExistsException, InvalidException {
        Nurse newNurse = NurseInput.getNurse(nurseInput);
        if(nurseRepository.existsById(nurseInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Nurse already exists");
        else {nurseRepository.save(newNurse);
        }
    }

}
