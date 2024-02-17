package edu.brown.cs.student.main.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class TestCSVParser {
  private String dataPath = "C:/Users/Morgane/Desktop/cs0320/server-kczhang-mpizigo/data/";

  @Test
  public void diffReaders() {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(dataPath + "census/income_by_race.csv"));
    } catch (IOException e) {
      System.err.println("File cannot be read");
      return;
    }
    CSVParser parser = new CSVParser(reader, new RowParser());

    // Parses file correctly with buffer reader
    assertEquals(parser.getParsedFile().size(), 324);

    StringReader readerTwo = new StringReader("Tim, Nelson, CSCI 0320, Instructor");
    CSVParser parserTwo = new CSVParser(readerTwo, new RowParser());

    // Parses file correctly with string reader
    assertEquals(parserTwo.getParsedFile().size(), 1);
  }

  @Test
  public void diffCreators() {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(dataPath + "census/income_by_race.csv"));
    } catch (IOException e) {
      System.err.println("File cannot be read");
      return;
    }
    CSVParser parser = new CSVParser(reader, new RowParser());

    // Parses file correctly with row parser
    assertEquals(parser.getParsedFile().size(), 324);

    try {
      reader = new BufferedReader(new FileReader(dataPath + "census/income_by_race.csv"));
    } catch (IOException e) {
      System.err.println("File cannot be read");
      return;
    }
    parser = new CSVParser(reader, new RowParserNew());

    // Parses file correctly with row parser new
    assertEquals(parser.getParsedFile().size(), 324);
  }
}
