package com.example.application.controller;

import com.example.application.model.DocumentEntity;
import com.example.application.repository.DocumentRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepository documentRepository;

    @GetMapping("/")
    public List<DocumentEntity> listDocuments() {
        return documentRepository.findAll();
    }

    @GetMapping("/{id}/original")
    public ResponseEntity<byte[]> downloadOriginal(@PathVariable Long id) {
        return documentRepository.findById(id).map(doc -> {
            byte[] content = doc.getOriginalXml().getBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getOriginalFileName() + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .body(content);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/transformed")
    public ResponseEntity<byte[]> downloadTransformed(@PathVariable Long id) {
        return documentRepository.findById(id).map(doc -> {
            byte[] content = doc.getTransformedXml().getBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transformed_" + doc.getOriginalFileName() + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .body(content);
        }).orElse(ResponseEntity.notFound().build());
    }
}
