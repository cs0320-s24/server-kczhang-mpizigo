package brown.cs.student.main.cache;

public record CacheData(int maxSize, int time) {

  @Override
  public int maxSize() {
    return maxSize;
  }

  @Override
  public int time() {
    return time;
  }
}
