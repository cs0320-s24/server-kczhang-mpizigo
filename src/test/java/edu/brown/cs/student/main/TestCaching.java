package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.cache.CacheData;
import edu.brown.cs.student.main.cache.CachedACSCensusSource;
import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

public class TestCaching {

  @Test
  public void testCachingSuccess()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException, ExecutionException {
    //Checks that cache is empty
    ACSCensusSource datasource = new ACSCensusSource();
    CachedACSCensusSource mockedDatasource =
        new CachedACSCensusSource(datasource, new CacheData(10, 10));
    assertEquals(0, mockedDatasource.getCache().size());

    //Check that data was cached
    mockedDatasource.getCensusData("california", "kings county");
    assertEquals(1, mockedDatasource.getCache().size());
    assertEquals(new CensusData("california", "kingscounty", "83.5"), mockedDatasource.getCache().get("california,kingscounty"));
  }

  @Test
  public void testCachingTwice()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException, ExecutionException {
    ACSCensusSource datasource = new ACSCensusSource();
    CachedACSCensusSource mockedDatasource =
        new CachedACSCensusSource(datasource, new CacheData(10, 10));

    mockedDatasource.getCensusData("california", "kings county");
    mockedDatasource.getCensusData("california", "kings county");
    assertEquals(1, mockedDatasource.getCache().size());
  }

  @Test
  public void testCachingNotThere()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException, ExecutionException {
    ACSCensusSource datasource = new ACSCensusSource();
    CachedACSCensusSource mockedDatasource =
        new CachedACSCensusSource(datasource, new CacheData(10, 10));

    mockedDatasource.getCensusData("california", "kings county");
    assertEquals(new CensusData("california", "kingscounty", "83.5"), mockedDatasource.getCache().get("california,kingscounty"));
    Throwable exception =
        assertThrows(
            ExecutionException.class, () -> mockedDatasource.getCache().get("california,banana"));
    assertEquals("err_bad_request: the county banana cannot be found", exception.getCause().getMessage());
    exception =
        assertThrows(
            ExecutionException.class, () -> mockedDatasource.getCache().get("banana"));
    assertEquals("err_bad_input: no county was given", exception.getCause().getMessage());
  }

  @Test
  public void testCachingEviction()
      throws DatasourceException, URISyntaxException, IOException, InterruptedException, ExecutionException {
    //Checks that cache is empty
    ACSCensusSource datasource = new ACSCensusSource();
    CacheData data = new CacheData(1, 10);
    CachedACSCensusSource mockedDatasource =
        new CachedACSCensusSource(datasource, data);
    assertEquals(0, mockedDatasource.getCache().size());

    //Check that data was cached
    mockedDatasource.getCensusData("california", "kings county");
    assertEquals(new CensusData("california", "kingscounty", "83.5"), mockedDatasource.getCache().get("california,kingscounty"));

    //Cache new data
    mockedDatasource.getCensusData("california", "butte county");
    assertEquals(new CensusData("california", "buttecounty", "88.5"), mockedDatasource.getCache().get("california,buttecounty"));

    //Check that data was evicted
//    Throwable exception =
//        assertThrows(
//            ExecutionException.class, () -> mockedDatasource.getCache().get("california,kingscounty"));
//    assertEquals("err_bad_request: the county banana cannot be found", exception.getCause().getMessage());
  }
}
