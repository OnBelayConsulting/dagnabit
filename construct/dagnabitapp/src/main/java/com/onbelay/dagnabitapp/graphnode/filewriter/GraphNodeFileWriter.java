package com.onbelay.dagnabitapp.graphnode.filewriter;

import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphNodeFileHeader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class GraphNodeFileWriter extends GraphNodeFileHeader {
    private static final Logger logger = LogManager.getLogger();

    private List<GraphNodeSnapshot> nodes = new ArrayList<>();

    public GraphNodeFileWriter(List<GraphNodeSnapshot> nodes) {
        this.nodes = nodes;
    }

    public List<GraphNodeSnapshot> getNodes() {
        return nodes;
    }

    public byte[] getContents() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out)) {

            try (CSVPrinter printer = new CSVPrinter(
                    outputStreamWriter,
                    CSVFormat.EXCEL.builder().setHeader(header).build()) ) {

                for (GraphNodeSnapshot node : nodes) {
                    printer.printRecord(node.getDetail().getName(), node.getDetail().getCategory(), node.getDetail().getWeight(), node.getDetail().getData());
                }
            } catch (IOException e) {
                logger.error("Writing node csv file failed with: ", e);
                throw new OBRuntimeException(TransactionErrorCode.NODE_FILE_WRITE_FAILED.getCode());
            }
        } catch (IOException e) {
            logger.error("Writing node csv file failed with: ", e);
            throw new OBRuntimeException(TransactionErrorCode.NODE_FILE_WRITE_FAILED.getCode());
        }

        return out.toByteArray();
    }
}
