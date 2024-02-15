package CSV;

/**
 * A CensusDatasource  can be used to get the current weather
 * at a certain location, which is provided to the source at
 * time of request.
 */

public interface CensusDatasource {

    /**
     * Retrieve the current weather (in the form of a WeatherData record) at a
     * given geolocation.
     *
     * @param
     * @return the census data obtained
     * @throws DatasourceException if there is an issue retrieving data for this state and/or county
     * @throws IllegalArgumentException if the state and/or county given is invalid
     */
    CensusData getCensusData(String stateName, String countyName) throws DatasourceException, IllegalArgumentException;

}
