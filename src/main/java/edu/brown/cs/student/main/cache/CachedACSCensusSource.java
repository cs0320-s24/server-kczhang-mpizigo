package edu.brown.cs.student.main.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.CensusDatasource;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.util.concurrent.TimeUnit;

/**
 * A proxy class that wraps a ACSCensusDatasource instance and caches responses for efficiency. It
 * uses a Guava cache class to manage the cache.
 */
public class CachedACSCensusSource implements CensusDatasource {
  private final ACSCensusSource wrappedDatasource;
  private final LoadingCache<String, CensusData> cache;

  /**
   * Constructs the proxy class to wrap an instance of ACSCensusData and cache its results
   *
   * @param toWrap the datasource to wrap
   * @param cacheData the parameters the cache must follow; can be chosen by the developer
   */
  public CachedACSCensusSource(ACSCensusSource toWrap, CacheData cacheData) {
    this.wrappedDatasource = toWrap;

    // Builds the
    this.cache =
        CacheBuilder.newBuilder()
            // How many entries maximum in the cache?
            .maximumSize(cacheData.maxSize())
            // How long should entries remain in the cache?
            .expireAfterWrite(cacheData.time(), TimeUnit.MINUTES)
            // Keep statistical info around for profiling purposes
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @Override
                  public CensusData load(String compositeKey) throws DatasourceException {

                    System.out.println("called load for: " + compositeKey);
                    String[] names = compositeKey.split(",");
                    // If this isn't yet present in the cache, load it:
                    try {
                      return wrappedDatasource.getCensusData(names[0], names[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                      throw new DatasourceException("err_bad_input: no county was given");
                    }
                  }
                });
  }

  /**
   * Queries the census API for the broadband internet percentage of a specific state and county.
   * First checks if the data is already present in the cache. If not, calls the cache's load method
   * and retrieved the data from an API query.
   *
   * @param stateName the specified state
   * @param countyName the specified county
   * @return the CensusData holding the data from the API call
   * @throws DatasourceException if the data could not be retrieved
   */
  @Override
  public CensusData getCensusData(String stateName, String countyName) throws DatasourceException {
    String compositeKey =
        stateName.replaceAll("\\s", "").toLowerCase()
            + ","
            + countyName.replaceAll("\\s", "").toLowerCase();
    CensusData result = this.cache.getUnchecked(compositeKey);
    return result;
  }

  /**
   * Getter method for the cache object, used solely for testing. If published, this code would like
   * not incude this (as it would be too dangerous to expose the cache instance directly).
   *
   * @return the cache instance
   */
  public LoadingCache<String, CensusData> getCache() {
    return this.cache;
  }
}
