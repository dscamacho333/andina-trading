package co.edu.unbosque.microservice_system_management.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelfPullService {

    private final FileToExcelService converter;
    private final GridFsLogFileService grid;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${log.shared.root}")
    private String sharedRoot;

    @Value("${log.timezone:America/Bogota}")
    private String tz;

    /** Cierre diario: lee <root>/<service>/<date>.log, convierte y guarda en GridFS */
    public String pull(LocalDate date) throws Exception {
        Path file = Paths.get(sharedRoot, serviceName, date + ".log");
        if (!Files.exists(file)) {
            log.warn("No existe log diario para {} en {}", serviceName, file);
            return null;
        }
        try (var is = Files.newInputStream(file)) {
            byte[] xlsx = converter.convertJsonlToXlsx(serviceName, date, is);
            var id = grid.store(date, serviceName, xlsx);
            return id.toHexString();
        }
    }

    /** Snapshot 2 min: lee current.log, convierte y guarda en GridFS */
    public String pullSnapshot2min() throws Exception {
        Path current = Paths.get(sharedRoot, serviceName, "current.log");
        if (!Files.exists(current)) {
            log.debug("Aún no existe current.log en {}", current);
            return null;
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of(tz));
        try (var is = Files.newInputStream(current)) {
            byte[] xlsx = converter.convertJsonlToXlsx(serviceName, now.toLocalDate(), is);
            var id = grid.storeSnapshot(now, serviceName, xlsx);
            return id.toHexString();
        }
    }
}