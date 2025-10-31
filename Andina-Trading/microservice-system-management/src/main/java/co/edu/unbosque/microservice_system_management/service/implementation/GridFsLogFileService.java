package co.edu.unbosque.microservice_system_management.service.implementation;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GridFsLogFileService {

    private final GridFsTemplate grid;

    public ObjectId store(LocalDate date, String service, byte[] bytes) {
        var meta = new Document(Map.of(
                "type", "daily-log-excel",
                "date", date.toString(),
                "service", service
        ));
        String filename = service + "-" + date + ".xlsx";
        return grid.store(new ByteArrayInputStream(bytes), filename,
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").toString(),
                meta);
    }

    public ObjectId storeSnapshot(LocalDateTime ts, String service, byte[] bytes) {
        String date = ts.toLocalDate().toString();
        String time = ts.toLocalTime().withSecond(0).withNano(0).toString(); // HH:mm
        String filename = service + "-" + date + "_" + time.replace(":", "-") + ".xlsx";

        var meta = new Document(Map.of(
                "type", "snapshot-2min-excel",
                "date", date,
                "time", time,
                "service", service
        ));
        return grid.store(new ByteArrayInputStream(bytes), filename,
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").toString(),
                meta);
    }

    // GridFsLogFileService.java
    public List<Map<String,Object>> list(String start, String end, String service, String type){
        List<Criteria> cs = new ArrayList<>();
        if (type != null && !type.isBlank()) {
            cs.add(Criteria.where("metadata.type").is(type)); // solo si lo piden
        }
        if (start!=null && !start.isBlank()) cs.add(Criteria.where("metadata.date").gte(start));
        if (end!=null && !end.isBlank())     cs.add(Criteria.where("metadata.date").lte(end));
        if (service!=null && !service.isBlank()) cs.add(Criteria.where("metadata.service").is(service));

        Query q = cs.isEmpty() ? new Query() : new Query(new Criteria().andOperator(cs.toArray(new Criteria[0])));
        List<Map<String,Object>> out = new ArrayList<>();
        grid.find(q).forEach(f -> {
            var md = f.getMetadata()==null? new Document(): f.getMetadata();
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", f.getObjectId().toHexString());
            m.put("filename", f.getFilename());
            m.put("date", md.getString("date"));
            if (md.containsKey("time")) m.put("time", md.getString("time"));
            m.put("service", md.getString("service"));
            m.put("type", md.getString("type"));
            m.put("length", f.getLength());
            out.add(m);
        });
        out.sort(Comparator
                .comparing((Map<String,Object> m) -> (String)m.get("date"))
                .thenComparing(m -> String.valueOf(m.getOrDefault("time", "")))
        );
        return out;
    }


    public GridFsResource getResourceById(String id){
        var file = grid.findOne(Query.query(Criteria.where("_id").is(new ObjectId(id))));
        return file == null ? null : grid.getResource(file);
    }
}