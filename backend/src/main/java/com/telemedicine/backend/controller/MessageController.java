package com.telemedicine.backend.controller;

import com.telemedicine.backend.ResponseBody;
import com.telemedicine.backend.dto.MessageRequest;
import com.telemedicine.backend.entity.Consultation;
import com.telemedicine.backend.entity.Message;
import com.telemedicine.backend.service.ConsultationService;
import com.telemedicine.backend.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/consultations/{consultationId}/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<ResponseBody> addMessage(
            @PathVariable Long consultationId,
            @Valid @RequestBody MessageRequest request,
            HttpServletRequest httpServletRequest
    ){
        Consultation consultation = consultationService.getConsultationById(consultationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consultation not found with id: " + consultationId
                ));

        Message message = messageService.createMessage(
                consultation,
                request.getAuthor(),
                request.getAuthorRole(),
                request.getContent()
        );

        ResponseBody responseBody = ResponseBody.created(
                message,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<ResponseBody> getMessages(
            @PathVariable Long consultationId,
            @RequestParam(required = false) String role,
            HttpServletRequest httpRequest) {

        if (!consultationService.consultationExist(consultationId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Consultation not found with id: " + consultationId
            );
        }

        List<Message> messages = (role != null && !role.isEmpty())
                ? messageService.getMessagesByConsulationIdAndRole(consultationId, role)
                : messageService.getMessagesByConsultationId(consultationId);

        ResponseBody response = ResponseBody.success(
                messages,
                httpRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }
}