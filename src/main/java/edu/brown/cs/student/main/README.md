# CSV Project

## Project Details

Name: CSV
Team members and contributions: mpizigo
Time spent on this project: 16+ hours
Link to our repository: [click here](https://github.com/cs0320-s24/server-kczhang-mpizigo.git)


## Design Choices

### Relationships
There are three main classes in this program: Main, CSVSearcher, and CSVParser. Main handles
user input and exceptions/error messages; it passes this user input into an instance of CSVSearcher
and returns the List<String> of rows it gets back to the user.
CSVSearcher takes in a CSV file name, a value to search for, the presence of headers, and a column
identifier. Using this information, it searches through the parsed CSV mentioned for the value. To
do so, it instantiates a CSVParser to make the wanted CSV file iterable and its search method returns
a List<List<String>> to Main.
CSVParser takes in a Reader object, a generic CreatorFromRow, and the presence of headers. Using the
Reader object, it parses through the rows of the CSV, then feeds it into CreatorFromRow to create
an object the developer wishes for. Its parse method returns a generic List<T> to CSVSearcher,
or another developer's Searcher.

The CreatorFromRow interface mentioned above is implemented by StringFromRow. This concrete class lets
us convert the parsed rows in CSVParser into iterable objects in CSVSearcher.
The FactoryFailureException exception is an exception thrown when there is an issue in creating
an object out of a CSV row.

### Explanations
**Regarding data structures:**
In CSVParser, I made use of generics and the CreateFromRow strategy pattern so that CSVParser can
be used outside of my personal CSVSearcher, in differing contexts. I therefore used a generic List<T>,
as the order of the CSV terms and rows mattered.

I created StringFromRow such that CSVSearcher is usable. It returns each row as a List<String> such
that CSVSearcher can iterate through each element. Furthermore, most CSVs are parseable into a
List<List<String>>, making for a general CSVSearcher.

**Regarding error handling:**
CSVParser throws all its exceptions, instead of handling them. By letting these exceptions propagate,
the parser remain generic and lets the developer handle the exception as they wish. These exceptions
are generally handled in Main, as they only need a terminal error message and system exit. The only
exception caught and handled in CSVSearcher (NumberFormatException regarding the column identifier
being a bad input) is not propagated, as I have chosen to keep the program running and convert to
a general search instead of specific. I believe this is more user-friendly, as they do not have to
re-input everything again.

**Regarding user experience:**
Main takes in one extra argument than strictly necessary, namely the presence of headers or not.
While it makes the input fractionally more difficult for the user, it lets us search more efficiently,
as it removes checking for headers, as well as parsing through them unnecessarily. It also makes
searching by column name easier.

Regarding handling malformed data (defensive programming):
CSVParser currently handles teh malformed data in inputted CSVs by skipping the row with the malformed
data. This results in some loss of data (the quality of that data being questionable), but also
results in straightforward code as there is no need to parse through the row and attempt to assign
the correct column to each element.
However, the signaling of that malformed data remains inadequate when considering the delevoper
stakeholder. As of now, CSVParser does not throw an exception (IndexOutOfBounds, for example) and
instead prints out the malformed line's number to the terminal. This means ony the end-user
stakeholder is informed of this, and the developer has to either parse through that string or
not be aware of it at al (if they do not have access to said terminal). I chose to approach it in this
way as I could not continue the program after throwing an exception, and thus would have to cut the
search short. However, I believe it is more important to both the end-user and developer to receive
some information, rather than none (as the search would have been terminated, and no rows would have
been found). By printing the lines to the console, I am at least ensuring some degree of transparency.

## Errors/Bugs
As an end-user stakeholder, you can sometimes access some protected files you should not be able to.
This is only if the filepath to that csv contains "data/", but is not in the data/ folder containing
the csvs.
To do this, the fileName argument inputted must have "data/" in it.
This is due to my crude solution for a modicum of safety (checking that all filepaths contain "data/").

## Tests
**Testing CSVParser :**
- testing for normal parsing, with headers or no headers
- testing for bad CSV file name inputs, such as no file found and unreadable files
- testing that it takes in a generic reader, both FileReader and StringReader
- testing that it can handle a strategy pattern CreateFromRow (for Star or Earnings objects, or
simply Strings)
- testing that it properly skips malformed data

**Testing CSVSearcher :**
- testing that users are unable to access protected files
- testing for different value inputs: a value to look for with different cases is found,
a "sub-value" (such as sand in sandwich) is not found, a value to look for with spaces (such as
Black and African American) is found.
- testing for general searching through the entire CSV, with the value present or not
- testing for searching by column index, with value present or not
- testing for searching by column name, with value present or not
- testing for searching by column index or name defaulting to a general search when the column
identifier provided is wrong, on correct or malformed data

### How to run them
Run mvn package, or press the green play button to the left side in the test files you wish to run.