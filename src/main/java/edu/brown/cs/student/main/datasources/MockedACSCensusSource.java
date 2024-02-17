package edu.brown.cs.student.main.datasources;

/**
 * The MockedACSCensusSource class is responsible for giving a mock CensusData response. This lets
 * use test BroadbandHandler (which takes in a CensusDatasource) without making constant calls to
 * the Census API.
 */
public class MockedACSCensusSource implements CensusDatasource {

  private final CensusData constantData;

  /**
   * Constructs the MockedACSCensusSource with the specified CensusData
   *
   * @param constantData the specified data
   */
  public MockedACSCensusSource(CensusData constantData) {
    this.constantData = constantData;
  }

  /**
   * r55t
   *
   * @param stateName the specified state
   * @param countyName the specified county
   * @return the CensusData instance with mock data
   * @throws DatasourceException if there is an issue retrieving data for this state and/or county
   */
  @Override
  public CensusData getCensusData(String stateName, String countyName) throws DatasourceException {
    return this.constantData;
  }
}
