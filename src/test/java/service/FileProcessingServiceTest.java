package service;

import com.example.application.repository.DocumentRepository;
import com.example.application.service.FileProcessingService;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = com.example.application.ApplicationTest.class)
public class FileProcessingServiceTest {

    private final DocumentRepository repository = Mockito.mock(DocumentRepository.class);
    private FileProcessingService service;

    @BeforeEach
    public void setUp() {
        service = new FileProcessingService(repository);
    }

    @Test
    public void testProcessFile() throws Exception {
        byte[] xmlBytes = "<test></test>".getBytes(StandardCharsets.UTF_8);
        service.processFile(xmlBytes, "test.xml");
        verify(repository).save(any());
    }
}