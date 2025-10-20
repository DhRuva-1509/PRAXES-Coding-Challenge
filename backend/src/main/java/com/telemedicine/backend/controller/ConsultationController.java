package com.telemedicine.backend.controller;

import com.telemedicine.backend.ResponseBody;
import com.telemedicine.backend.dto.ConsultationRequest;
import com.telemedicine.backend.entity.Consultation;
import com.telemedicine.backend.service.ConsultationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<ResponseBody> createConsultation(@Valid @RequestBody ConsultationRequest request, HttpServletRequest httpServletRequest){
        Consultation consultation = consultationService.createConsultation(
                request.getPatientId(),
                request.getDoctorId()
        );

        ResponseBody responseBody = ResponseBody.created(
                consultation,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody> getConsultation(@PathVariable Long id, HttpServletRequest httpServletRequest){
        Consultation consultation = consultationService.getConsultationById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found with id: " + id));

        ResponseBody responseBody = ResponseBody.success(
                consultation,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping
    public ResponseEntity<ResponseBody> getAllConsultations(HttpServletRequest httpServletRequest){
        List<Consultation> consultations = consultationService.getAllConsultations();

        ResponseBody responseBody = ResponseBody.success(
                consultations,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.ok(responseBody);
    }
}
