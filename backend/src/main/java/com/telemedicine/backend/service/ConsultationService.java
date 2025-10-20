package com.telemedicine.backend.service;

import com.telemedicine.backend.entity.Consultation;
import com.telemedicine.backend.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public Consultation createConsultation(String parentId, String doctorId){
        Consultation consultation = new Consultation();
        consultation.setPatientId(parentId);
        consultation.setDoctorId(doctorId);
        consultation.setCreatedAt(LocalDateTime.now());
        return consultationRepository.save(consultation);
    }

    public Optional<Consultation> getConsultationById(Long id){
        return consultationRepository.findById(id);
    }

    public  List<Consultation> getAllConsultations(){
        return consultationRepository.findAll();
    }

    public boolean consultationExist(Long id){
        return consultationRepository.existsById(id);
    }
}
