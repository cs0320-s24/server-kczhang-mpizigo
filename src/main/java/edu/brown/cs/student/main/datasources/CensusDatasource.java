package edu.brown.cs.student.main.datasources;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A CensusDatasource interface that can be used to get the broadband home percentage of a specific
 * state and county, which is provided to the source at time of request.
 */
public interface CensusDatasource {

  /**
   * Retrieve the percentage of homes with broadband internet access (in the form of a CensusData
   * record) for a given state and county.
   *
   * @param stateName the specified state
   * @param countyName the specified county
   * @return the census data obtained
   * @throws DatasourceException if there is an issue retrieving data for this state and/or county
   */
  CensusData getCensusData(String stateName, String countyName)
      throws DatasourceException, IllegalArgumentException, URISyntaxException, IOException,
          InterruptedException;
}
