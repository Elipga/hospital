package com.example.hospital.Repository;

import com.example.hospital.Domain.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, String> {

}
