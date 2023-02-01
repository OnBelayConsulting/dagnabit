package com.onbelay.dagnabit.graphnode.filereader;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphNodeFileReader;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphRelationshipFileReader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GraphRelationshipFileReaderTest extends DagnabitSpringTestCase  {

    @Test
    public void readRelationships() throws IOException {

        List<GraphRelationshipSnapshot> relationships;
        try (InputStream stream = GraphRelationshipFileReaderTest.class.getResourceAsStream("/relationship_file_example.csv")) {
            GraphRelationshipFileReader reader = new GraphRelationshipFileReader(stream);
            relationships = reader.readFile();
        }
        assertEquals(5, relationships.size());

        GraphRelationshipSnapshot first = relationships.get(0);
        assertEquals("FatherSmith", first.getFromNodeName());
        assertEquals("JaneSmith", first.getToNodeName());
        assertEquals("ParentOf", first.getDetail().getType());


        GraphRelationshipSnapshot last = relationships.get(4);
        assertEquals("MotherSmith", last.getFromNodeName());
        assertEquals("FatherSmith", last.getToNodeName());
        assertEquals("spouse", last.getDetail().getType());


    }

}
