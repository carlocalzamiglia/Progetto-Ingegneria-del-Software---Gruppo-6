package itpolimiingsw;

import itpolimiingsw.Game.Markers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestMarkers {

    @Test
    public void testMarkersTrue(){
        Markers markers = new Markers();
        markers.dump();
        assertEquals(1, markers.getValue());
    }
}
