package com.telemedicine.backend.service;

import com.telemedicine.backend.entity.Consultation;
import com.telemedicine.backend.entity.Message;
import com.telemedicine.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;

    public Message createMessage(Consultation consultation, String author, String authorRole, String content){

        if (authorRole == null || authorRole.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Author role is required"
            );
        }

        if (!authorRole.equalsIgnoreCase("PATIENT") && !authorRole.equalsIgnoreCase("DOCTOR")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Author role must be either PATIENT or DOCTOR"
            );
        }

        Message message =  Message.builder()
                            .consultation(consultation)
                            .author(author)
                            .authorRole(authorRole.toUpperCase())
                            .content(content)
                            .timestamp(LocalDateTime.now())
                            .build();

        return  messageRepository.save(message);
    }

    public List<Message> getMessagesByConsultationId(Long consultationId){
        return messageRepository.findByConsultation_IdOrderByTimestampAsc(consultationId);
    }

    public List<Message> getMessagesByConsulationIdAndRole(Long consultationId, String authorRole){
        return messageRepository.findByConsultation_IdAndAuthorRoleOrderByTimestampAsc(consultationId, authorRole.toUpperCase());
    }
}
