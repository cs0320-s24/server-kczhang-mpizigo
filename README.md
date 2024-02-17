> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details

Project Name: Server
Team Members: Kenneth Zhang (kczhang) and Morgane Pizigo (mpizigo)
Repo Link: https://github.com/cs0320-s24/server-kczhang-mpizigo

# Design Choices

We decided to implement CSVParser and CSVSearcher from the previous CSV project to
load, view, and search classes. Each of these classes receives dependency injection with a
Datasource object, ensuring that they all operate on the same loaded file simultaneously.


# Errors/Bugs

There are no known bugs.

# Tests

Our test suite encompasses all handlers, the search and parser, and so on, and is incredibly extensive / thorough.

# How to

To use each functionality in user story one and two:
loadcsv "localhost:3232/loadcsv?filepath=<filepath goes here>&header=<y/n>”
viewcsv "localhost:3232/viewcsv"
searchcsv "localhost:3232/searchcsv?val=<search target>&col=<(optional) column name or int index>"
broadband "localhost:3232/broadband?state=<state name>&county=<county name>"
