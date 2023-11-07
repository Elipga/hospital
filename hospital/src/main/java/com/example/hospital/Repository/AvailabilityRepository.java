package com.example.hospital.Repository;

import com.example.hospital.Domain.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    //List<Availability> findByCollegeNumberOrderByDayAndHour(String collegeNumber, LocalDate day, LocalTime hour);
    List<Availability> findByCollegeNumberAndDayBetweenOrderByDay (String collegeNumber, LocalDate firstDay, LocalDate lastDay);

    List<Availability> findByCollegeNumber(String collegeNumber);

    @Transactional //to make sure that everything has been deleted
    void removeByCollegeNumber(String collegeNumber);

    //Transactional
    //@Query("DELETE FROM Availability e WHERE e.collegeNumber = :collegeNumber")
    //void deleteByCollegeNumber(@Param("collegeNumber") String collegeNumber);
}
