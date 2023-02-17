package com.onbelay.dagnabitapp.graphnode.filereader;

import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GraphRelationshipFileReader extends GraphRelationshipFileHeader{
    private static final Logger logger = LogManager.getLogger();
    private InputStream streamIn;

    public GraphRelationshipFileReader(InputStream streamIn) {
        this.streamIn = streamIn;
    }

    public List<GraphRelationshipSnapshot> readFile() {

        ArrayList<GraphRelationshipSnapshot> snapshots = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(streamIn)) {

            CSVParser parser = new CSVParser(
                    reader,
                    CSVFormat.EXCEL.builder()
                            .setHeader(header)
                            .setSkipHeaderRecord(true)
                            .build());

            Iterable<CSVRecord> records = parser;

            for (CSVRecord record : records) {
                GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
                snapshot.setFromNodeName(record.get("FromNode"));
                snapshot.setToNodeName(record.get("ToNode"));
                snapshot.getDetail().setType(record.get("Relationship"));

                String weightStr = record.get("Weight");
                if (weightStr != null && weightStr.isBlank() == false) {
                    int weight = Integer.parseInt(weightStr);
                    snapshot.getDetail().setWeight(weight);
                }
                if (record.isSet("Data"))
                    snapshot.getDetail().setData(record.get("Data"));
                snapshots.add(snapshot);
            }

            parser.close();

        } catch (IOException e) {
            logger.error("CSV file parsing read failed. ", e);
            throw new OBRuntimeException(TransactionErrorCode.RELATIONSHIP_FILE_WRITE_FAILED.getCode());
        }
        return snapshots;
    }
}
