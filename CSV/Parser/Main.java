package CSV.Parser;

import java.io.File;
import java.util.Scanner;

  /** The Main class of our project. This is where execution begins. */
  public final class Main {
    /**
     * The initial method called when execution begins.
     *
     * @param args An array of command line arguments
     */
    Scanner scan = new Scanner(System.in);

    String dataPath = "C:\\Users\\Kenny Zhang\\cs0320\\csv-kennethczhang\\data\\";

    public static void main(String[] args) {
      new Main(args).run();
    }

    private Main(String[] args) {}

    private void run() {
      /** Runs the main program for the UI: gets file and allows user to search */
      String fileInput = getFileInput();
      boolean hasHeader = getYN("Does your CSV file have headers?");
      CSVSearch searcher = new CSVSearch(this.dataPath + fileInput, hasHeader);
      search(searcher);
    }

    private String getFileInput() {
      /**
       * Gets a valid file path from the user. User must include \\ between dirs and the file, but not
       * in the beginning
       */
      String fileInput = scanInput("Input the path for the CSV file you want to search:");
      File file = new File(this.dataPath + fileInput);
      while (!file.exists() || file.isDirectory() || fileInput.contains("..")) {
        fileInput = scanInput("Couldn't find the CSV file, try again:");
        file = new File(this.dataPath + fileInput);
      }
      return fileInput;
    }

    private boolean getYN(String printString) {
      /** Gets a yes or no answer from user */
      String input = scanInput(printString + " Enter 'y' for yes and 'n' for no:");
      while (!input.equals("y") && !input.equals("n")) {
        input = scanInput("Invalid input. Enter 'y' for yes and 'n' for no:");
      }
      return input.equals("y");
    }

    private void search(CSVSearch searcher) {
      /** Searches for specified value in specified column as many times as user wants */
      String searchVal = scanInput("Enter the string value you would like search for:");
      String colVal =
          scanInput(
              "Enter the column index or name you would like to search on (leave blank if you want to search the whole file):");

      // Determines if colVal is unspecified, if colVal is an index, or if colVal is a column name
      if (colVal.isEmpty()) {
        searcher.search(searchVal);
      } else if (colVal.matches("(0|[1-9]\\d*)")) {
        searcher.search(searchVal, Integer.parseInt(colVal));
      } else {
        searcher.search(searchVal, colVal);
      }

      // Searches
      if (getYN("Do you want to search the CSV file again?")) {
        search(searcher);
      }
    }

    private String scanInput(String printString) {
      /**
       * Prints the input string and waits for user input
       *
       * @param printString string to be printed to terminal
       */
      System.out.println(printString);
      return this.scan.nextLine();
    }
  }