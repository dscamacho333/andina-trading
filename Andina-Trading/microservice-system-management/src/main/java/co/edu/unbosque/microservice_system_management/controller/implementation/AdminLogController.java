package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.service.implementation.GridFsLogFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/logs")
@RequiredArgsConstructor
public class AdminLogController {

    private final GridFsLogFileService grid;

    // type: daily-log-excel (default) | snapshot-2min-excel
    @GetMapping("/files")
    public List<Map<String,Object>> list(
            @RequestParam(required=false) String start,
            @RequestParam(required=false) String end,
            @RequestParam(required=false) String service,
            @RequestParam(required=false) String type
    ){
        return grid.list(start, end, service, type);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String id) throws IOException {
        var res = grid.getResourceById(id);
        if (res == null || !res.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(res.contentLength())
                .body(new InputStreamResource(res.getInputStream()));
    }
}
