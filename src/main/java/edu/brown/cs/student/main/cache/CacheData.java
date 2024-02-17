package edu.brown.cs.student.main.cache;

/**
 * The record CacheData determines some parameters of the Guava cache built in
 * CachedACSCensusSource, such as the max number of entries in the cache, and the amount of tim that
 * the data will stay in memory.
 *
 * @param maxSize specified max number of entries in the cache
 * @param time specified max time the data will be kept for
 */
public record CacheData(int maxSize, int time) {

  /**
   * Returns the maxSize of the cache
   *
   * @return the specified max number of entries
   */
  @Override
  public int maxSize() {
    return maxSize;
  }

  /**
   * Returns the amount of time until expiry for the cached data.
   *
   * @return the specified time
   */
  @Override
  public int time() {
    return time;
  }
}
