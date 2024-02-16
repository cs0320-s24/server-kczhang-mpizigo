package edu.brown.cs.student.main.Parser;

import edu.brown.cs.student.main.CreatorFromRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CSVParser<T> {
    private BufferedReader reader;
    private CreatorFromRow<T> creator;
    private List<T> parsedFile;
    private Pattern regexSplitCSVRow =
            Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

    public CSVParser(Reader reader, CreatorFromRow<T> creator) {
        /**
         * Constructor for CSVParser, parses a given file
         *
         * @param reader stores the file to be read from
         * @param creator determines the row format
         */
        this.reader = new BufferedReader(reader);
        this.creator = creator;
        this.parsedFile = new ArrayList<T>();
        this.parseFile();
    }

    public void parseFile() {
        /** Parses each row CSV file into List<String> based on commas */
        try {
            String row;
            while ((row = reader.readLine()) != null) {
                try {
                    List<String> parsedRow = Arrays.asList(regexSplitCSVRow.split(row));
                    this.parsedFile.add(this.creator.create(parsedRow));
                } catch (FactoryFailureException e) {
                    System.err.println("Cannot parse row in file");
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot read row in file");
        }
    }

    public List<T> getParsedFile() {
        return this.parsedFile;
    }
}
