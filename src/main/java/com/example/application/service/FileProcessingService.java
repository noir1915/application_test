package com.example.application.service;

import com.example.application.model.DocumentEntity;
import com.example.application.repository.DocumentRepository;
import com.example.application.util.XmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final DocumentRepository documentRepository;

    @Transactional
    public void processFile(byte[] fileBytes, String filename) throws Exception {
        log.info("Обработка файла {}", filename);
        String xmlContent = new String(fileBytes, StandardCharsets.UTF_8);

        Source xsltSource = XmlUtils.loadResourceAsSource("idoc2order.xsl");
        Source xmlSource = new StreamSource(new ByteArrayInputStream(fileBytes));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
        transformer.transform(xmlSource, new StreamResult(outputStream));

        String transformedXml = outputStream.toString(StandardCharsets.UTF_8.name());

        DocumentEntity doc = new DocumentEntity();
        doc.setOriginalFileName(filename);
        doc.setReceivedAt(LocalDateTime.now());
        doc.setOriginalXml(xmlContent);
        doc.setTransformedXml(transformedXml);

        documentRepository.save(doc);
    }
}
