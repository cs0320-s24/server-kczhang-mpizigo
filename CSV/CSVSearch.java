package CSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSearch {

    private CSVParser<List<String>> parser;
    private List<List<String>> parsedFile;
    private List<List<String>> lastSearchResult;
    private Boolean hasHeader;
    private List<String> header;
    private BufferedReader reader;
    private boolean parsed = false;

    public CSVSearch(String fileName, Boolean hasHeader) {
        /**
         * Constructor for CSVSearch, finds the corresponding file and parses it for searching
         *
         * @param filename is the path to file from the data directory
         * @param hasHeader indicates whether the file has a header or not
         */

        // Determines if the file exists
        this.hasHeader = hasHeader;
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory() || fileName.contains("..")) {
            System.err.println("File not found");
            return;
        }

        // Reads the file based on given filename
        try {
            this.reader = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            System.err.println("File cannot be read");
            return;
        }

        // Parses the file after being read
        this.parser = new CSVParser(this.reader, new RowParser());
        this.parsed = true;
        this.parsedFile = this.parser.getParsedFile();
        if (this.hasHeader) {
            this.header = this.parser.getParsedFile().get(0);
            this.parsedFile.remove(0);
        }
    }

    public boolean search(String val) {
        /**
         * Searches for given string on all columns
         *
         * @param val is the value we are looking for
         * @return whether or not the search was successful
         */
        this.lastSearchResult = new ArrayList<>();
        boolean found = false;
        for (List<String> row : this.parsedFile) {
            for (String item : row) {
                if (item.equals(val)) {
                    System.out.println(row);
                    this.lastSearchResult.add(row);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("No matches found for " + val + " in this CSV file.");
        }
        return true;
    }

    public boolean search(String val, int index) {
        /**
         * Searches for given string in a given column based on index, errors with bad index
         *
         * @param val is the value we are looking for
         * @param index is the column index
         * @return whether or not the search was successful
         */
        this.lastSearchResult = new ArrayList<>();
        if (index < 0 || index >= parsedFile.get(0).size()) {
            System.err.println(
                    "Invalid index, must be between 0 and " + (parsedFile.get(0).size() - 1) + ":");
            return false;
        } else {
            searchByIndex(val, index);
            return true;
        }
    }

    public boolean search(String val, String col) {
        /**
         * Searches for given string in a given column based on index, error with invalid column name
         *
         * @param val is the value we are looking for
         * @param col is the column name
         * @return whether or not the search was successful
         */
        this.lastSearchResult = new ArrayList<>();
        if (!this.hasHeader) {
            System.err.println("No headers exist: cannot search by header name");
            return false;
        } else {
            int index = this.header.indexOf(col);
            if (index == -1) {
                System.err.println("Header does not exist");
                return false;
            } else {
                searchByIndex(val, index);
                return true;
            }
        }
    }

    public void searchByIndex(String val, int index) {
        /**
         * Searches for given string in a given valid index for column
         *
         * @param val is the value we are looking for
         * @param index is the column index
         */
        boolean found = false;
        for (List<String> strings : this.parsedFile) {
            if (strings.size() > index && strings.get(index).equals(val)) {
                System.out.println(strings);
                this.lastSearchResult.add(strings);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matches found for " + val + " in this CSV file.");
        }
    }

    public List<List<String>> getLastSearch() {
        return this.lastSearchResult;
    }

    public boolean parsed() {
        return this.parsed;
    }
}
