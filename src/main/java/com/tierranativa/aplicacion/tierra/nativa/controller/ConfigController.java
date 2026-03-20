package com.tierranativa.aplicacion.tierra.nativa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/configs")
public class ConfigController {

    @Value("${tierranativa.contact.whatsapp}")
    private String whatsappNumber;

    @Value("${tierranativa.contact.default-message}")
    private String defaultMessage;

    @GetMapping(value = "/contact", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> getContactInfo() {
        String cleanNumber = whatsappNumber.replaceAll("[^0-9]", "");
        return ResponseEntity.ok(Map.of(
                "whatsapp", cleanNumber,
                "message", defaultMessage
        ));
    }
}
