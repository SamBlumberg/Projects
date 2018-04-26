Sam Blumberg 

Run  Instructions:

add #include "tombstones.h" to a cpp file.

g++ name.cpp
./a.out

	In this project we implementing tombstones. This allows us to have our pointers point to a tombstone. With this extra level, 
we are able to find memory leaks and dangling references among a few other errors at run time and print out appropriate messages.
Our implementation pretty much exactly followed the given outline for the Pointer class, and we implemented all the given functions
as expected. We also created a tombstone class to create tombstone objects. With this all completed, we added a reference counter
to the tombstone class, which allowed us to check for memory leaks if there are no references to a tombstone that still points to
something. Checking for a dangling reference is also easily done by using a boolean to symbolize that the tombstone is dead.

foo1-8 are test files to show the programs functionality. 
