package edu.brown.cs.student.main.api;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class TestACSCensusSource {

  /**
   * Tests for good inputs, capitalization, bad parameters (both state & county), county & state
   * switched
   */
  @Test
  public void testDatasourceSuccess()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException {
    // Set up the request, make the request
    ACSCensusSource censusSource = new ACSCensusSource();
    CensusData data = censusSource.getCensusData("california", "buttecounty");
    assertEquals("california", data.state());
    assertEquals("buttecounty", data.county());
    assertEquals("88.5", data.getPercentage());
  }

  @Test
  public void testDatasourceCapitalization()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException {
    // Set up the request, make the request
    ACSCensusSource censusSource = new ACSCensusSource();
    CensusData data = censusSource.getCensusData("CaliForNia", "bUt tecou Nty");
    assertEquals("CaliForNia", data.state());
    assertEquals("bUt tecou Nty", data.county());
    assertEquals("88.5", data.getPercentage());
  }

  @Test
  public void testDatasourceBadStateInput()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException {
    // Set up the request, make the request
    ACSCensusSource censusSource = new ACSCensusSource();
    Throwable exception =
        assertThrows(
            DatasourceException.class, () -> censusSource.getCensusData("banana", "buttecounty"));
    assertEquals("err_bad_request: the state banana cannot be found", exception.getMessage());
  }

  @Test
  public void testDatasourceBadCountyInput()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException {
    // Set up the request, make the request
    ACSCensusSource censusSource = new ACSCensusSource();
    Throwable exception =
        assertThrows(
            DatasourceException.class, () -> censusSource.getCensusData("california", "butte"));
    assertEquals("err_bad_request: the county butte cannot be found", exception.getMessage());
  }

  @Test
  public void testDatasourceBadParams()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException {
    // Set up the request, make the request
    ACSCensusSource censusSource = new ACSCensusSource();
    Throwable exception =
        assertThrows(
            DatasourceException.class,
            () -> censusSource.getCensusData("buttecounty", "california"));
    assertEquals("err_bad_request: the state buttecounty cannot be found", exception.getMessage());
  }
}
