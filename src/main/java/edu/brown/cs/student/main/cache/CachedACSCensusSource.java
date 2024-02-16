package edu.brown.cs.student.main.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.CensusDatasource;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A class that wraps a FileServer instance and caches responses for efficiency. Notice that the
 * interface hasn't changed at all. This is an example of the proxy pattern; callers will interact
 * with the CachedFileServer, rather than the "real" data source.
 *
 * <p>This version uses a Guava cache class to manage the cache.
 */
public class CachedACSCensusSource implements CensusDatasource {
  private final ACSCensusSource wrappedDatasource;
  private final LoadingCache<String, CensusData> cache;

  /**
   * Proxy class: wrap an instance of Searcher (of any kind) and cache its results
   *
   * @param toWrap the Searcher to wrap
   */
  public CachedACSCensusSource(ACSCensusSource toWrap, CacheData cacheData) {
    this.wrappedDatasource = toWrap;

    // Look at the docs -- there are lots of builder parameters you can use
    //   including ones that affect garbage-collection (not needed for Server).
    this.cache =
        CacheBuilder.newBuilder()
            // How many entries maximum in the cache?
            .maximumSize(cacheData.maxSize())
            // How long should entries remain in the cache?
            .expireAfterWrite(cacheData.time(), TimeUnit.MINUTES)
            // Keep statistical info around for profiling purposes
            .recordStats()
            .build(
                // Strategy pattern: how should the cache behave when
                // it's asked for something it doesn't have?
                new CacheLoader<>() {
                  @Override
                  public CensusData load(String compositeKey) throws DatasourceException {

                    System.out.println("called load for: " + compositeKey);
                    String[] names = compositeKey.split(",");
                    // If this isn't yet present in the cache, load it:
                    try {
                      return wrappedDatasource.getCensusData(names[0], names[1]);
                    } catch(ArrayIndexOutOfBoundsException e) {
                      throw new DatasourceException("err_bad_input: no county was given");
                    }
                  }
                });
  }

  @Override
  public CensusData getCensusData(String stateName, String countyName) throws DatasourceException {
    String compositeKey =
        stateName.replaceAll("\\s", "").toLowerCase()
            + ","
            + countyName.replaceAll("\\s", "").toLowerCase();
    CensusData result = this.cache.getUnchecked(compositeKey);
    return result;


  }

  public LoadingCache<String, CensusData> getCache() {
    return this.cache;
  }
}
