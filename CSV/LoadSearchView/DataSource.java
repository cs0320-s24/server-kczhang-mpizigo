package CSV.LoadSearchView;

public class DataSource<T> {

    private T data;
    private boolean loaded;

    public DataSource() {
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
