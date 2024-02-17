# Project Details

Project Name: Server  
Team Members: Kenneth Zhang (kczhang) and Morgane Pizigo (mpizigo)  
Repo Link: [click here](https://github.com/cs0320-s24/server-kczhang-mpizigo)  

# Design Choices

### Relationships
The main class, Server, starts the local server and sets the loadcsv viewcsv searchcsv and broadband
endpoints. Each endpoint has its own Handler class implementing route, which therefore lets us 
handle requests and responses more easily.  
Each one (except for BroadbandHandler) also receives 
a Datasource dependency injection: the LoadHandler, SearchHandler and ViewHandler the same one, such that 
they all operate on the same loaded file simultaneously.  


BroadbandHandler instead gets a 
CachedACSDatasource passed in, to cache results from the API calls it sends to the Census API. This
datasource sends back the results of an API request based on state name and county name in the form
of a CensusData record. BroadbandHandler then transmits that to our local server as a Json.

### Explanations
**Regarding CensusData:** the data from the API is converted into a CensusData record because it ensures proper data
formatting (we'll always be able to access the percentage, for example).

**Regarding CacheData:** by using a record passed into CachedACSCensusSource, we're able to keep
developer intervention straightforward and lightweight as we are only passing in parameters,
while ensuring that all parameters are correctly inputted (the record enforces the presence and type of 
each parameter).


# Errors/Bugs

The CachedACSCensusSource does not seem to set the maximum size to its cache properly, whether
hardcoded or passed into it by the CacheData record. This has been tested in the testCacheEviction()
test in the TestCaching class, where it does not evict old cached data properly despite hitting
the max cache size.

# Tests
Under API testing:  
**Testing ACSCensusSource :**
- testing for valid inputs
- testing for inputs with odd capitalization
- testing for missing inputs (both state and/or county names)
- testing for bad inputs (both state and/or county names)

**Testing BroadbandHandler :**
- testing for valid request
- testing for invalid request
- testing for inputs with odd capitalization
- testing for missing inputs

**Testing CachedACSCensusSource :**
- testing for successful caching (not present the first time, present the second)
- testing that caching twice leads to only one entry in the cache
- testing for data not cached not being present in caching
- testing for caching eviction behavior

Under loadHandler/searchHandler/viewHandler testing:
**Testing LoadHandler :**
- testing for valid & invalid filepaths
- testing for headers & no headers

**Testing ViewHandler :**
- testing for valid & invalid filepaths to view (and formatting to Json)

**Testing SearchHandler :**
- testing for valid & invalid search inputs
- testing for search by column index or header

**Integration testing :**
- testing for loading the csv before view or search, and vice versa

From last sprint:  
**Testing CSVParser :**
- testing for normal parsing, with headers or no headers
- testing for bad CSV file name inputs, such as no file found and unreadable files
- testing that it takes in a generic reader, both FileReader and StringReader
- testing that it can handle a strategy pattern CreateFromRow (for Star or Earnings objects, or
  simply Strings)

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

# How to

To use each functionality in user story one and two:
- loadcsv "localhost:3232/loadcsv?filepath=<filepath goes here>&header=<y/n>”
- viewcsv "localhost:3232/viewcsv"
- searchcsv "localhost:3232/searchcsv?val=<search target>&col=<(optional) column name or int index>"
- broadband "localhost:3232/broadband?state=<state name>&county=<county name>"

To change the caching parameters in user story three:
1. Create a new CacheData object
2. Pass it into the instance of CachedACSCensusSource passed into BroadbandHandler
