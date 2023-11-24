package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.ErrorDTO;
import com.example.hospital.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> alreadyExistExceptionHandler(AlreadyExistsException ex){
        ErrorDTO error = ErrorDTO.builder().code("1").message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = InvalidException.class)
    public ResponseEntity<ErrorDTO> invalidExceptionHandler(InvalidException ex){
        ErrorDTO error = ErrorDTO.builder().code("2").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }
    @ExceptionHandler(value = IsEmptyException.class)
    public ResponseEntity<ErrorDTO> isEmptyExceptionHandler(IsEmptyException ex){
        ErrorDTO error = ErrorDTO.builder().code("3").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = DoctorDoesNotExists.class)
    public ResponseEntity<ErrorDTO> doctorDoesNotExistsHandler(DoctorDoesNotExists ex){
        ErrorDTO error = ErrorDTO.builder().code("4").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = StaffDoesNotExists.class)
    public ResponseEntity<ErrorDTO> staffDoesNotExistsHandler(StaffDoesNotExists ex){
        ErrorDTO error = ErrorDTO.builder().code("5").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = TimeOutOfRangeException.class)
    public ResponseEntity<ErrorDTO> timeOutOfRangeExceptionHandler(TimeOutOfRangeException ex){
        ErrorDTO error = ErrorDTO.builder().code("6").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }
    @ExceptionHandler(value = PatientDoesNotExists.class)
    public ResponseEntity<ErrorDTO> patientDoesNotExistsHandler(PatientDoesNotExists ex){
        ErrorDTO error = ErrorDTO.builder().code("7").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = DateOutOfRange.class)
    public ResponseEntity<ErrorDTO> dateOutOfRangeHandler(DateOutOfRange ex){
        ErrorDTO error = ErrorDTO.builder().code("8").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }
    @ExceptionHandler(value = NurseDoesNotExists.class)
    public ResponseEntity<ErrorDTO> nurseDoesNotExistsHandler(NurseDoesNotExists ex){
        ErrorDTO error = ErrorDTO.builder().code("9").message((ex.getMessage())).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) //validation exception of @Valid
    public Map<String, String> handleValidateException (MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<String,String>();

        ex.getBindingResult().getAllErrors().forEach((error) ->
        {String fieldName = ((FieldError) error).getField();

            String message = error.getDefaultMessage();

            errors.put(fieldName, message);
        });
        return errors;
    }
}
