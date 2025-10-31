package co.edu.unbosque.microservice_system_management.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileToExcelService {

    private final ObjectMapper mapper = new ObjectMapper();

    public byte[] convertJsonlToXlsx(String service, LocalDate date, InputStream input) throws Exception {
        try (SXSSFWorkbook wb = new SXSSFWorkbook();
             BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            var sheet = wb.createSheet("logs-" + date);
            int r = 0;
            String[] cols = {"timestamp","level","service","logger","thread","message","stack_trace"};
            var header = sheet.createRow(r++);
            for (int i=0;i<cols.length;i++) header.createCell(i).setCellValue(cols[i]);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Map<String,Object> json = mapper.readValue(line, Map.class);

                var row = sheet.createRow(r++);
                row.createCell(0).setCellValue(first(json.get("@timestamp"), json.get("timestamp")));
                row.createCell(1).setCellValue(str(json.get("level")));
                row.createCell(2).setCellValue(service != null ? service : str(json.get("service")));
                row.createCell(3).setCellValue(str(json.get("logger")));
                row.createCell(4).setCellValue(str(json.get("thread")));
                row.createCell(5).setCellValue(str(json.get("message")));
                row.createCell(6).setCellValue(str(json.get("stack_trace")));
            }
            wb.write(bos);
            return bos.toByteArray();
        }
    }

    private String first(Object... values){
        for (Object v : values) if (v != null) return String.valueOf(v);
        return "";
    }
    private String str(Object v){ return v==null? "" : String.valueOf(v); }
}
