package CSV.LoadSearchView;

/**
 * The DataSource class represents a data source that can hold a specific type of data.
 * It provides methods to set and retrieve data, as well as check if data has been loaded.
 *
 * @param <T> The type of data that the DataSource can hold.
 */
public class DataSource<T> {

    private T data;            // The data stored in the DataSource
    private boolean loaded;    // A flag indicating whether data has been loaded

    /**
     * Constructs a new DataSource object with no data loaded initially.
     */
    public DataSource() {
        this.loaded = false;
    }

    /**
     * Sets the data for the DataSource and marks it as loaded.
     *
     * @param data The data to be set in the DataSource.
     */
    public void setData(T data) {
        loaded = true;
        this.data = data;
    }

    /**
     * Retrieves the data stored in the DataSource.
     *
     * @return The data stored in the DataSource.
     */
    public T getData() {
        return this.data;
    }

    /**
     * Checks if data has been loaded into the DataSource.
     *
     * @return true if data has been loaded, false otherwise.
     */
    public boolean isLoaded() {
        return this.loaded;
    }
}