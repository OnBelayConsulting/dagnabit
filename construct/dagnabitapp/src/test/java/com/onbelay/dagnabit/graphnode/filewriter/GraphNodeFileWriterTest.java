package com.onbelay.dagnabit.graphnode.filewriter;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.filewriter.GraphNodeFileWriter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GraphNodeFileWriterTest extends DagnabitSpringTestCase {

    @Test
    public void writeNodes() throws IOException {

        List<GraphNodeSnapshot> nodes = new ArrayList<>();

        GraphNodeSnapshot snapshot = new GraphNodeSnapshot();
        snapshot.getDetail().setName("Fred");
        snapshot.getDetail().setCategory("Family");
        snapshot.getDetail().setWeight(2);
        snapshot.getDetail().setData("my, data");
        nodes.add(snapshot);

        snapshot = new GraphNodeSnapshot();
        snapshot.getDetail().setName("Sammy");
        snapshot.getDetail().setCategory("Family");
        snapshot.getDetail().setWeight(2);
        snapshot.getDetail().setData("");
        nodes.add(snapshot);

        GraphNodeFileWriter writer = new GraphNodeFileWriter(nodes);
        byte[] contents = writer.getContents();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(contents);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String headerLine = reader.readLine();
        String contentLine = reader.readLine();
        assertEquals("Name,Category,Weight,Data", headerLine);

        assertEquals("Fred,Family,2,\"my, data\"", contentLine);

    }

}
