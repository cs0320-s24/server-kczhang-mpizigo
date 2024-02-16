package edu.brown.cs.student.main.datasources;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockedACSCensusSource implements CensusDatasource {

  private final CensusData constantData;

  public MockedACSCensusSource(CensusData constantData) {
    this.constantData = constantData;
  }

  @Override
  public CensusData getCensusData(String stateName, String countyName)
      throws DatasourceException, IllegalArgumentException, URISyntaxException, IOException,
          InterruptedException {
    return this.constantData;
  }
}
