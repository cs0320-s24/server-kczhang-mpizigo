package edu.brown.cs.student.main;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class TestACSCensusSource {

  /**
   * Tests for good inputs, capitalization, bad parameters (both state & county), county & state switched
   */
    @Test
    public void testDatasourceBadParams()
        throws DatasourceException, URISyntaxException, IOException, InterruptedException {
      // Set up the request, make the request
      ACSCensusSource censusSource = new ACSCensusSource();
      CensusData data = censusSource.getCensusData("california", "buttecounty");
      assertEquals("california", data.state());
      assertEquals("buttecounty", data.county());
      assertEquals("88.5", data.percentage());
    }
}
