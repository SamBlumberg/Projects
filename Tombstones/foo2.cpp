/////////////////////////////////////////////////////////////////////////////
// foo2.cpp, test file for CS254, assignment 5
/////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <stdlib.h>
#include "tombstones.h"

using namespace std;

void error(const char *text)
{
    cout << "ERROR: " << text << endl;
    exit(-1);
}

int main(int argc, char **argv)
{
    Pointer<int> foo;
    
    foo = new int(12);

    Pointer<int> bar(foo);

    if (bar == 0)
	error("Bar should not be null!");

    if (*bar != 12)
	error("Bar got the wrong value!");
    if (foo != bar)
	error("Foo and bar are NOT distinct pointers!");
    if (*foo != *bar)
	error("Foo and bar should have the same value here!");

    *foo = 15;
    //std::cout << *(foo.ptr);

    
    if (*bar != 15)
	error("Bar should still match foo!");

    //std::cout << (*foo == *bar);

    free(foo);

    //std::cout << (foo.valid == bar.valid);
    //std::cout << (foo.valid);
    free(bar);

    error("Attempt to double-delete pointer not flagged!");
    return 0;

}
