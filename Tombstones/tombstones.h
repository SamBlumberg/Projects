#include <iostream>
#include <stdlib.h>

#if !defined(__TOMBSTONES_H__)
#define __TOMBSTONES_H__

bool ml = false;
template <class T> class Pointer;
template <class T> class Tombstone;
template <class T> void free(Pointer<T>& obj);


//tombstone class
template <class T>
class Tombstone {
    
public:
    T* ptr;
    int refCount;
    bool valid;
    bool initialized;

    //default constructor
    Tombstone<T>(){
        valid = true;
        initialized = false;

    }
    //bootstrapping constructor
    Tombstone<T>(T* v){                           
        valid = true;
        initialized = true;
        ptr =  v;
    }
    //dereferencing
    T& operator*() const{  
        if(!valid){
            std::cout<< "Dangling pointer error" << "\n";
            exit(-1);
        }
        return *ptr;
    }    
    //field dereferencing               
    T* operator&() const{    
        return ptr;
    }

};

//pointer class
template <class T>
class Pointer {
    
public:
    Tombstone<T>* tomb;

    // default constructor
    Pointer<T>(){                               
        Tombstone<T>* temp = new Tombstone<T>;
        temp->refCount = 1;
        tomb = temp;
    }
    // copy constructor
    Pointer<T>(Pointer<T>& p){                       
        if(!(p.tomb)->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }

        tomb = p.tomb;
        tomb->refCount++;

    }
    // bootstrapping constructor
    Pointer<T>(T* v){                             
        Tombstone<T>* temp = new Tombstone<T>(v);
        temp->refCount = 1;
        tomb =  temp;
        // argument should always be a call to new
    }
    // destructor
    ~Pointer<T>(){                              
        if(tomb->valid && (tomb->refCount == 1 || tomb->refCount == 0)){
            if(!ml){
                std::cout <<"Encountered a memory leak"<< "\n";
                ml = true;
            }
            exit(-1);
        }
        else if(tomb->valid){
            tomb->refCount--;
        }
    }
    
    // dereferencing
    T& operator*() const{  
        if(!tomb->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return **tomb;
    }
    // field dereferencing                   
    T* operator->() const{     
        if(!tomb->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return &(*tomb);
    }
    void operator=(T* v){       
        *tomb = v;
    }
    // assignment
    void operator=(const Pointer<T>& p){       
        if(!(p.tomb)->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        tomb->refCount--;
        tomb = p.tomb;
    }
    
    //free function
    friend void free(Pointer<T>& obj){
        if((obj.tomb)->valid == false){
            std::cout<< "Cannot delete twice"<< "\n";
            exit(-1);
        }
        if((obj.tomb)->initialized == false){
            std::cout<< "Can't release uninitialized pointer"<< "\n";
            exit(-1);
        }

        (obj.tomb)->valid = false;

        delete (obj.tomb->ptr);
    }
    
    // equality comparisons:
    bool operator==(const Pointer<T>& p) const{
        if(!tomb->valid || !(p.tomb)->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return (tomb == p.tomb);
    }

    bool operator!=(const Pointer<T>& p) const{
        if(!tomb->valid || !(p.tomb)->valid){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return (tomb != p.tomb);
    }

    bool operator==(const int n) const{
        if(!tomb->valid ){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return (n == 0) && (tomb->ptr == NULL);
        // true iff Pointer is null and int is zero
    }
    bool operator!=(const int n) const{
        if(!tomb->valid ){
            std::cout<< "Dangling pointer error"<< "\n";
            exit(-1);
        }
        return ! ((n == 0) && (tomb->ptr == NULL));
        // false iff Pointer is null and int is zero
    }
};

#endif // __TOMBSTONES_H__
