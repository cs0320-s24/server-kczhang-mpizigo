package CSV;

import static spark.Spark.after;

import java.util.ArrayList;
import java.util.List;
import spark.Spark;
public class CSVServer {
    public static void main(String[] args) {
        int port = 3232;
        Spark.port(port);

        after(
                (request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Access-Control-Allow-Methods", "*");
                });

        CSVDataSource<CSVSearch> dataSource = new CSVDataSource<>();
        // Setting up the handler for the endpoints
        Spark.get("loadcsv", new LoadCSVHandler(dataSource));
        Spark.get("viewcsv", new ViewCSVHandler(dataSource));
        Spark.get("searchcsv", new SearchCSVHandler(dataSource));
        Spark.init();
        Spark.awaitInitialization();

        System.out.println("Server started at http://localhost:" + port);
    }
}