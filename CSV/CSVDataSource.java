package CSV;

public class CSVDataSource<T> {

    private T data;
    private boolean loaded;

    public CSVDataSource() {
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
