Collaborators: Sam Blumberg, Kennedy Agusi

Our database implementation was done entirely generically, while we did not implement it
like the textbook as stated in part 1, we spoke to Professor Ferguson and he said that doing
the entire project generically was ok. 

All code is in the file main.c

To see that our program works as intended simply run with the follwing instructions

HOW TO RUN OUR PROGRAM:
-Go the directory that contains our project and type 'make'. This will build the project.
- After building the project, go to directory 'dist/Debug/Cygwin-Windows' in thesame directory that contains our project, there you will find the executable file which was built using 'make'.
- Our program can now be run using './database'
 
The output is very intuitive and clearly states everything it is testing.
Reading our main method can verify we are indeed calling the correct methods.

Our implementation works by having a large global array of generic structs called genericDB as our database.
We can insert any relation we want into the database by calling genInsertRelation with the appropriate 
parameters. Our functions genInsert, genDelete and genLookup do as expected on the appropriate relation 
in the database of whos id it is passed.

To demonstrate our file write/read, we write to or create if needed a text file called DATABASE.txt.
After writing to file which can be verified by opening the txt file, we call emptydatabase, which 
goes though the entire database setting everything to null. After that we read from the file to repopulate
the database. We then print the entire database again to show that it worked.

Our part 2 implementation does what is stated in the project description and is very clear upon being run. 

For part 3, all of our relational algebra are clearly tested in the output and work entirely generically.

For extra credit, we did our entire program generically as previously stated. There is only 1 type of struct
that is defined and it is the generic one. On top of creating the registrar example from the textbook, we also
show that it works generically on a relation called "Calender" which is used to illustrate insert, delete, and
lookup.