package brown.cs.student.main.cache;

import brown.cs.student.main.datasources.ACSCensusSource;
import brown.cs.student.main.datasources.CensusData;
import brown.cs.student.main.datasources.CensusDatasource;
import brown.cs.student.main.datasources.DatasourceException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A class that wraps a FileServer instance and caches responses
 * for efficiency. Notice that the interface hasn't changed at all.
 * This is an example of the proxy pattern; callers will interact
 * with the CachedFileServer, rather than the "real" data source.
 *
 * This version uses a Guava cache class to manage the cache.
 */
public class CachedACSCensusSource implements CensusDatasource {
    private final ACSCensusSource wrappedDatasource;
    private final LoadingCache<String, CensusData> cache;

    /**
     * Proxy class: wrap an instance of Searcher (of any kind) and cache
     * its results.
     *
     * There are _many_ ways to implement this! We could use a plain
     * HashMap, but then we'd have to handle "eviction" ourselves.
     * Lots of libraries exist. We're using Guava here, to demo the
     * strategy pattern.
     *
     * @param toWrap the Searcher to wrap
     */
    public CachedACSCensusSource(ACSCensusSource toWrap, CacheData cacheData) {
      this.wrappedDatasource = toWrap;

      // Look at the docs -- there are lots of builder parameters you can use
      //   including ones that affect garbage-collection (not needed for Server).
      this.cache = CacheBuilder.newBuilder()
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

                  System.out.println("called load for: "+ compositeKey);
                  String[] names = compositeKey.split(",");
                  // If this isn't yet present in the cache, load it:
                  return wrappedDatasource.getCensusData(names[0], names[1]);
                }
              });
    }


  @Override
  public CensusData getCensusData(String stateName, String countyName)
      throws DatasourceException, IllegalArgumentException, URISyntaxException, IOException, InterruptedException {
      String compositeKey = stateName.replaceAll("\\s", "").toLowerCase()
          + ","
          + countyName.replaceAll("\\s", "").toLowerCase();
      CensusData result = this.cache.getUnchecked(compositeKey);
      System.out.println("result stuff " + result);
      System.out.println(this.cache.stats());
      return result;
  }
  }

