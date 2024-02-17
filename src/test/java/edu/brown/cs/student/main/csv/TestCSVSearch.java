package edu.brown.cs.student.main.csv;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.csv.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

//  CSV data with and without column headers;
//  CSV data in different Reader types (e.g., StringReader and FileReader);
//  CSV data with inconsistent column count;
//  CSV data that lies outside the protected directory;
//  Searching for values that are, and aren’t, present in the CSV;
//  Searching for values that are present, but are in the wrong column;
//  Searching for values by index, by column name, and without a column identifier; and
//  Using multiple CreatorFromRow classes to extract CSV data in different formats.

public class TestCSVSearch {

    private String dataPath = "C:\\Users\\Kenny Zhang\\cs0320\\csv-kennethczhang\\data\\";

    @Test
    public void simpleTest() {
        CSVSearch searcher = new CSVSearch(this.dataPath + "malformed\\malformed_signs.csv", true);
        searcher.search("Leo");
        assertEquals(searcher.getLastSearch().get(0).get(0), "Leo");
    }

    @Test
    public void diffColIdentifier() {
        CSVSearch searcher = new CSVSearch(this.dataPath + "census\\income_by_race.csv", true);

        // No col identifier, index identifier, header name identifier
        assertTrue(searcher.search("Asian"));
        assertEquals(searcher.getLastSearch().size(), 40);
        assertTrue(searcher.search("Other", 1));
        assertEquals(searcher.getLastSearch().size(), 26);
        assertTrue(searcher.search("Black", "Race"));
        assertEquals(searcher.getLastSearch().size(), 34);
    }

    @Test
    public void badColParams() {
        CSVSearch searcherOne = new CSVSearch(this.dataPath + "malformed\\malformed_signs.csv", true);
        CSVSearch searcherTwo = new CSVSearch(this.dataPath + "malformed\\malformed_signs.csv", false);

        // Out of bounds index and invalid header name input and w or wo header
        assertFalse(searcherOne.search("Leo", -1));
        assertFalse(searcherOne.search("Leo", 2));
        assertFalse(searcherOne.search("Cancer", "Header Name"));
        assertFalse(searcherTwo.search("Cancer", "Header Name"));
        assertFalse(searcherTwo.search("Libra", "Star Sign"));
    }

    @Test
    public void badVals() {
        CSVSearch searcher = new CSVSearch(this.dataPath + "census\\postsecondary_education.csv", true);

        // Values not present or present in the wrong column
        assertTrue(searcher.search("Chinese"));
        assertEquals(searcher.getLastSearch(), new ArrayList<>());
        assertTrue(searcher.search("Religious"));
        assertEquals(searcher.getLastSearch(), new ArrayList<>());
        assertTrue(searcher.search("Asian", 1));
        assertEquals(searcher.getLastSearch(), new ArrayList<>());
        assertTrue(searcher.search("Asian", 0));
        assertEquals(searcher.getLastSearch().size(), 2);
        assertTrue(searcher.search("Women", "University"));
        assertEquals(searcher.getLastSearch(), new ArrayList<>());
        assertTrue(searcher.search("Women", "Sex"));
        assertEquals(searcher.getLastSearch().size(), 8);
    }

    @Test
    public void outsideDir() {
        CSVSearch searcher =
                new CSVSearch(this.dataPath + "..\\data\\census\\postsecondary_education.csv", true);

        // Does not allow user to go out data directory
        assertFalse(searcher.parsed());
    }
}