package edu.brown.cs.student.main.datasources;

public class Datasource<T> {

  private T data;
  private boolean loaded;

  public Datasource() {
    this.loaded = false;
  }

  public void setData(T data) {
    loaded = true;
    this.data = data;
  }

  public T getData() {
    return this.data;
  }

  public boolean isLoaded() {
    return this.loaded;
  }
}
