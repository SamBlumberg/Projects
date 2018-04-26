
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define TRUE 1;
#define FALSE 0;
typedef int BOOLEAN;

typedef struct schema *SCHEMA;
struct schema{
    char *data[1000];
};

typedef struct genericDB *GDB;
struct genericDB{
    int length;
    int index;
    char *id;
    char *relation[100];
    SCHEMA rows[1000];
};

GDB database[1009];
int dbINDEX = 0;

void genPrintRelation(GDB relation);
void emptyDatabase();
void genWriteToFile();
void genReadFromFile();
void genPutInDatabase();
char *getGrade(char *name, char *course);
char *getLocation(char *name, char *time, char *day);
void genInsertRelation(char *relation[], int length, char *id ,int index);
int schemaLength(char *schema[]);
void genInsert(char *data[], char *id);
void genDelete(char *data[], char *id);
GDB genLookup(char *data[], char *id);
void genPrintDatabase();
GDB intersection(char *id1, char *id2);
GDB difference(char *id1, char *id2);
GDB getUnion(char *id1, char *id2);
GDB selection(GDB gdb, char *column, char *find);
GDB projection(GDB gdb, char *columns[], int length);
GDB join(GDB gdb1, GDB gdb2, char *column);
int getIndex(char *id);
int getColumn(GDB gdb, char *id);

int main(int argc, char** argv) {
    printf("Insert a new relation into the database, and print its schema\n\n");
    char *rel[] = {"Month", "Day", "Year"};
    genInsertRelation(rel, 3, "Calender", dbINDEX);
    genPrintDatabase();
    
    printf("Insert some tuples into the new relation\n\n");
    char *in1[] = {"August", "12", "2017"};
    genInsert(in1, "Calender");
    char *in2[] = {"May", "23", "2008"};
    genInsert(in2, "Calender");
    char *in3[] = {"June", "9", "2008"};
    genInsert(in3, "Calender");
    char *in4[] = {"May", "17", "1997"};
    genInsert(in4, "Calender");
    char *in5[] = {"April", "3", "2012"};
    genInsert(in5, "Calender");
    genPrintDatabase();
    
    printf("Lookup all items satisfying {May, *, *}\n\n");
    char *lu1[] = {"May", "*", "*"};
    GDB luh = genLookup(lu1,"Calender");
    genPrintRelation(luh);        
    
    printf("Delete all items satisfying {*, *, 2008}\n\n");
    char *de1[] = {"*", "*", "2008"};
    genDelete(de1, "Calender");
    genPrintDatabase(); 
    
    printf("input all of the relations and tuples from the registrar example \ninto the database and write the database to DATABASE.txt\n");
    
    //fill database
    genPutInDatabase();
    
    //write database to file
    genWriteToFile();
    
    genPrintDatabase();
    printf("Empty the database and refill it by reading from the file\n\n");
    //empty database
    emptyDatabase();
    
    //fill database from file
    genReadFromFile();
    genPrintDatabase();
    
    printf("Part 2\n\n");
    char *lut = getGrade("C. Brown" , "CS101");
    printf("Using our 2.1 query to find what grade C. Brown got in CS101: %s\n", lut);
    lut = getGrade("P. Patty" , "EE200");
    printf("Using our 2.1 query to find what grade C. Brown got in EE200: %s\n", lut);
    
    lut = getLocation("C. Brown" , "10AM", "Tu");
    printf("Using our 2.2 query to find where C. Brown is at 10am on Tuesday: %s\n", lut);
    lut = getLocation("L. Van Pelt" , "9AM", "M");
    printf("Using our 2.2 query to find where L. Van Pelt is at 9am on Monday: %s\n\n", lut);
    
    printf("Part 3\n\n");
    printf("σCourse = “CS101”(CSG)  (example 8.12)\n");
    GDB test1 = selection(database[getIndex("CSG")], "Course", "CS101");
    genPrintRelation(test1);
    
    printf("πStudentId(σCourse=“CS101”(CSG)) (example 8.13)\n");
    char *p3[] = {"StudentID"};
    GDB test2 = projection(selection(database[getIndex("CSG")], "Course", "CS101"), p3, 1);
    genPrintRelation(test2);
    
    printf("CR ⊲⊳ CDH (example 8.14)\n");
    GDB test3 = join(database[getIndex("CR")], database[getIndex("CDH")], "Course");
    genPrintRelation(test3);
    
    printf("πDay,Hour (σRoom=“Turing Aud.”(CR ⊲⊳ CDH)) (example 8.15)\n");
    char *a3[] = {"Day", "Hour"};
    GDB test4 = projection(selection(join(database[getIndex("CR")], database[getIndex("CDH")], "Course") , "Room", "Turing Aud."), a3, 2);;
    genPrintRelation(test4);
    
    printf("Here are two relations CR and CR2 which will be used to demonstrate our relational algebras\n\n");
    printf("Relation CR\n");
    genPrintRelation(database[getIndex("CR")]);
    
    printf("Relation CR2\n");
    genPrintRelation(database[getIndex("CR2")]);
    
    printf("Intersection of CR and CR2\n");
    GDB inter = intersection("CR", "CR2");
    genPrintRelation(inter);
    
    printf("Union of CR and CR2\n");
    GDB unio = getUnion("CR", "CR2");
    genPrintRelation(unio);
    
    printf("Difference of CR and CR2\n");
    GDB differ = difference("CR", "CR2");
    genPrintRelation(differ);
    
    return (EXIT_SUCCESS);
}

//empty the database
void emptyDatabase(){  
    for(int i = 0; i<dbINDEX; i++){
        database[i] = NULL;
    }
    dbINDEX = 0;
}
//get index in the database of a given relation id
int getIndex(char *id){
    for(int i = 0;i<dbINDEX; i++){
        if(strcmp(database[i]->id, id) == 0){
            return i;
        }
    }
    return -1;
}
//get attribute location from within a relation of a schema id
int getColumn(GDB gdb, char *id){
    for(int i = 0;i<gdb->length; i++){
        if(strcmp(gdb->relation[i], id) == 0){
            return i;
        }
    }
    return -1;
}
//print the entire database
void genPrintDatabase(){
    for(int k = 0; k<dbINDEX; k++){
        for(int i = 0; i<database[k]->length;i ++){
            printf("%s\t", database[k]->relation[i]);
        }
        printf("\n");
        for(int i = 0; i<database[k]->index; i++){
            for(int j = 0; j<database[k]->length; j++){
                printf("%s\t", database[k]->rows[i]->data[j]);
            }
            printf("\n");
        }
        printf("\n");
    }
}
//print a single relation
void genPrintRelation(GDB relation){
    //printf("%d", (relation == NULL));
    for(int i = 0; i<relation->length;i ++){
        printf("%s\t", relation->relation[i]);
    }
    printf("\n");
    for(int i = 0; i<relation->index; i++){
        for(int j = 0; j<relation->length; j++){
            printf("%s\t", relation->rows[i]->data[j]);
        }
        printf("\n");
         
    } 
    printf("\n");
}
// part 2.1 query
char *getGrade(char *name, char *course){
    char *temp[4];
    temp[0] = "*";
    temp[1] = name;
    temp[2] = "*";
    temp[3] = "*";
    GDB getID = genLookup(temp , "SNAP");
    int col = getColumn(getID, "StudentID");
    
    temp[0] = course;
    temp[1] = getID->rows[0]->data[col];
    temp[2] = "*";
    
    GDB getGrade = genLookup(temp , "CSG");
    col = getColumn(getGrade, "Grade");
    
    return getGrade->rows[0]->data[col];
}
//part 2.2 query
char *getLocation(char *name, char *time, char *day){
      
    BOOLEAN exists = FALSE;
    char *temp[4];
    temp[0] = "*";
    temp[1] = name;
    temp[2] = "*";
    temp[3] = "*";
    GDB getName = genLookup(temp , "SNAP");
    
    temp[0] = "*";
    temp[1] = getName->rows[0]->data[getColumn(getName, "StudentID")];
    temp[2] = "*";
    
    GDB getID = genLookup(temp , "CSG");
    
    char* ret;
    
    for(int i = 0; i< getID->index; i++){
        
        temp[0] = getID->rows[i]->data[getColumn(getID, "Course")];
        temp[1] = "*";
        temp[2] = "*";
        GDB getCourse = genLookup(temp , "CDH");
        for(int j =0; j<getCourse->index;j++){
            if((strcmp(time , getCourse->rows[j]->data[getColumn(getCourse, "Hour")]) == 0) && ((strcmp(day , getCourse->rows[j]->data[getColumn(getCourse, "Day")])) == 0)){
                //printf("test\n");
                ret = getCourse->rows[j]->data[getColumn(getCourse, "Course")];
                exists = TRUE;
                break;
            }
        }
    }
    temp[0] = ret;
    temp[1] = "*";
    
    GDB getRoom = genLookup(temp , "CR");
        
    return getRoom->rows[0]->data[getColumn(getRoom, "Room")];
}
//output database to file
void genWriteToFile(){
    FILE *fp;
    fp = fopen("DATABASE.txt" , "w+");
    for(int k = 0; k<dbINDEX; k++){
        //fputs(database[k]->rows[i]->data[j], fp);
        fputs("#\n", fp);
        fputs(database[k]->id, fp);
        fputs("\n", fp);
        fprintf(fp, "%d", database[k]->length);
        fputs("\n", fp);
        for(int i = 0; i<database[k]->length; i++){
            fputs(database[k]->relation[i], fp);
            fputs("_", fp);
        }
        fputs("\n", fp);
        for(int i = 0; i<database[k]->index; i++){
            fputs(database[k]->id, fp);
            fputs("_", fp);
            for(int j = 0; j<database[k]->length; j++){
                fputs(database[k]->rows[i]->data[j], fp);
                fputs("_", fp);
            }
            fputs("\n", fp);
        }
    }
    fclose(fp);
}
//fill database from file
void genReadFromFile(){
    FILE *fp;
    char str[200];
    fp = fopen("DATABASE.txt" , "r");
    while(fgets(str, 200, fp)!= NULL){
        char *str1 = strdup(str);
        //printf("%d \n", (str1 == );
        if(*str1 == '#'){
            //printf("Test  ");
            
            fgets(str, 200, fp);
            str1 = strdup(str);
        }
        char *id = strsep(&str1, "\n");
        fgets(str, 200, fp);
        str1 = strdup(str);    
        int length = atoi(strsep(&str1, "\n"));
        
        fgets(str, 200, fp);
        str1 = strdup(str);
        char* schema[length];
        for(int i = 0; i<length; i++){
            schema[i] = strsep(&str1, "_");
        }
        genInsertRelation(schema, length, id, dbINDEX);
        while(fgets(str, 200, fp)!= NULL){
            str1 = strdup(str);
            if(*str1 == '#'){
                break;
            }
            char* row[length];
            id = strsep(&str1, "_");
            for(int i = 0; i<length+1; i++){
                row[i] = strsep(&str1, "_");
            }
            genInsert(row, id);
        }
    }
    fclose(fp);
}
//insert tuple into a relation
void genInsert(char *data[], char *id){
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id) == 0){
            int j = database[i]->index;
            database[i]->rows[j] = (SCHEMA)malloc(sizeof(struct schema));
            database[i]->index++;
            
            for(int k = 0; k<database[i]->length; k++){
                database[i]->rows[j]->data[k] = data[k];
            }
        }
    }       
}
//delete tuple from a relation
void genDelete(char *data[], char *id){
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id) == 0){
            for(int j =0; j<database[i]->index; j++){
                BOOLEAN toDelete = TRUE;
                for(int k = 0; k<database[i]->length; k++){
                    if(!(strcmp(data[k], database[i]->rows[j]->data[k]) == 0 || (*(data[k]) == '*'))){
                        toDelete = FALSE;
                    }
                }
                if(toDelete){
                    for(int k = j; k<database[i]->index-1; k++){
                        database[i]->rows[k] = database[i]->rows[k+1];
                    }
                    j--;
                    database[i]->index--;
                }
            }
        }
    }
}
// join relational algebra
GDB join(GDB gdb1, GDB gdb2, char *column){
    
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    int col1;
    int col2;
    int relCount = 0;
    
    for(int i = 0; i<gdb1->length; i++){
        if(strcmp(gdb1->relation[i], column) == 0){
            col1 = i;
        }
    }
    
    for(int i = 0; i<gdb2->length; i++){
        if(strcmp(gdb2->relation[i], column) == 0){
            col2 = i;
        }
    }
    
    int newLength = gdb2->length + gdb1->length;
    for(int i = 0; i<gdb1->length; i++){    
        for(int j = 0; j<gdb2->length; j++){
            if(strcmp(gdb1->relation[i], gdb2->relation[j]) == 0){
                newLength--;
            }
        }
    }
    ret->length = newLength;
    for(int i = 0; i<gdb1->index; i++){
        
        for(int j = 0; j<gdb2->index; j++){
            if(strcmp(gdb1->rows[i]->data[col1], gdb2->rows[j]->data[col2]) == 0){
                //printf("test\n");
                ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
                for(int k = 0; k<gdb1->length; k++){
                    ret->rows[ret->index]->data[k] = gdb1->rows[i]->data[k];
                    if(relCount < gdb1->length){
                        ret->relation[relCount] = gdb1->relation[k];
                        relCount++;
                    }
                }
                for(int k = 0; k<gdb2->length; k++){
                    BOOLEAN exist = FALSE;
                    for(int l = 0; l<gdb1->length; l++){
                        if(strcmp(gdb2->relation[k], gdb1->relation[l]) == 0){
                            exist = TRUE;
                        }
                    }
                    if(!exist){
                        ret->rows[ret->index]->data[k + newLength - gdb1->length - 1] = gdb2->rows[j]->data[k];
                        if(relCount < ret->length){
                            ret->relation[relCount] = gdb2->relation[k];
                            //printf("%s %d \n",gdb2->relation[k], relCount);
                            relCount++;
                        }
                    }
                }//*/
                ret->index++;
            }
        }
    }
    //printf("%d\n", relCount);
    return ret;
}
GDB projection(GDB gdb, char *columns[], int length){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    ret->length = length;
    for(int i = 0; i<length; i++){
        ret->relation[i] = columns[i];
    }
    int col[length];
    for(int j = 0; j<length; j++){
        for(int i = 0; i<gdb->length; i++){
            if(strcmp(gdb->relation[i], columns[j]) == 0){
                col[j] = i;
            }
        }
    }
    //printf("%d\n", col[1]);
    for(int i = 0; i<gdb->index; i++){
        ret->rows[i] = (SCHEMA)malloc(sizeof(struct schema));
        for(int j = 0; j<length; j++){
            ret->rows[i]->data[j] = gdb->rows[i]->data[col[j]];
        }
        ret->index++;
    }
    return ret;
}
// selection relational algebra
GDB selection(GDB gdb, char *column, char *find){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    int col;
    ret->length = gdb->length;
    for(int i = 0; i<gdb->length; i++){
        if(strcmp(gdb->relation[i], column) == 0){
            col = i;
        }
    }
    for(int j =0; j<gdb->index; j++){
        BOOLEAN toReturn = TRUE;
        if((strcmp(find, gdb->rows[j]->data[col]) == 0)){
            ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
            for(int k = 0; k<gdb->length; k++){
                ret->rows[ret->index]->data[k] = gdb->rows[j]->data[k];
            }
            ret->index++;
        }
    }
    
    for(int i = 0;i<ret->length; i++){
        ret->relation[i] = gdb->relation[i];
    }
    return ret;
}
//lookup and return a tuple from a relation
GDB genLookup(char *data[], char *id){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id) == 0){
            ret->length = database[i]->length;
            for(int k = 0; k<ret->length;k++){
                ret->relation[k] = database[i]->relation[k];
            }
            for(int j =0; j<database[i]->index; j++){
                BOOLEAN toReturn = TRUE;
                for(int k = 0; k<database[i]->length; k++){
                    if(!(strcmp(data[k], database[i]->rows[j]->data[k]) == 0 || (*(data[k]) == '*'))){
                        toReturn = FALSE;
                    }
                }
                if(toReturn){
                    ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
                    for(int k = 0; k<database[i]->length; k++){
                        ret->rows[ret->index]->data[k] = database[i]->rows[j]->data[k];
                    }
                    ret->index++;
                }
            }
        }
    }
    return ret;
}
// intersection relational algebra
GDB intersection(char *id1, char *id2){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    int index1;
    int index2;
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id1) == 0){
            index1 = i;
        }
        else if(strcmp(database[i]->id, id2) == 0){
            index2 = i;
        }
    }
    for(int i = 0; i<database[index1]->length;i++){
        
        ret->relation[i] = database[index1]->relation[i];
    }
    ret->length = database[index1]->length;
    for(int j =0; j<database[index1]->index; j++){
        for(int k = 0; k<database[index2]->index; k++){
            BOOLEAN toReturn = TRUE;
            for(int i = 0; i<database[index2]->length; i++){
                if(strcmp(database[index1]->rows[j]->data[i], database[index2]->rows[k]->data[i]) != 0){
                    //printf("%s\t%s\n",database[index1]->rows[j]->data[i],  database[index2]->rows[k]->data[i]);
                    toReturn = FALSE;
                }
            }
            if(toReturn){
                ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
                for(int i = 0; i<database[index1]->length; i++){
                    ret->rows[ret->index]->data[i] = database[index1]->rows[j]->data[i];
                }
                ret->index++;
            }
        }   
    }   
    return ret;
}
// difference relational algebra
GDB difference(char *id1, char *id2){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    int index1;
    int index2;
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id1) == 0){
            index1 = i;
        }
        else if(strcmp(database[i]->id, id2) == 0){
            index2 = i;
        }
    }
    for(int i = 0; i<database[index1]->length;i++){
        ret->relation[i] = database[index1]->relation[i];
    }
    ret->length = database[index1]->length;
    for(int j =0; j<database[index1]->index; j++){
        BOOLEAN toReturn = TRUE;
        for(int k = 0; k<database[index2]->index; k++){
            BOOLEAN test = TRUE;
            for(int i = 0; i<database[index2]->length; i++){
                if(strcmp(database[index1]->rows[j]->data[i], database[index2]->rows[k]->data[i]) != 0){
                    //printf("%s\t%s\n",database[index1]->rows[j]->data[i],  database[index2]->rows[k]->data[i]);
                    test = FALSE;
                }
            }
            if(test){
                toReturn = FALSE;
            }
        }   
        if(toReturn){
                ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
                for(int i = 0; i<database[index1]->length; i++){
                    ret->rows[ret->index]->data[i] = database[index1]->rows[j]->data[i];
                }
                ret->index++;
        }
    }   
    return ret;
}
// union relational algebra
GDB getUnion(char *id1, char *id2){
    GDB ret = (GDB)malloc(sizeof(struct genericDB));
    ret->index = 0;
    int index1;
    int index2;
    for(int i = 0; i<dbINDEX; i++){
        if(strcmp(database[i]->id, id1) == 0){
            index1 = i;
        }
        else if(strcmp(database[i]->id, id2) == 0){
            index2 = i;
        }
    }
    for(int i = 0; i<database[index1]->length;i++){
        ret->relation[i] = database[index1]->relation[i];
    }
    ret->length = database[index1]->length;
    for(int i=0; i<database[index1]->index; i++){
        ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
        for(int j = 0; j<database[index1]->length; j++){
            ret->rows[ret->index]->data[j] = database[index1]->rows[i]->data[j];
        }
        ret->index++;
    }
    GDB test = difference(id2, id1);
    for(int i=0; i<test->index; i++){
        ret->rows[ret->index] = (SCHEMA)malloc(sizeof(struct schema));
        for(int j = 0; j<database[index1]->length; j++){
            ret->rows[ret->index]->data[j] = test->rows[i]->data[j];
        }
        ret->index++;
    }
    return ret;
}
// insert a relation into the database
void genInsertRelation(char *relation[], int length, char *id ,int index){
    dbINDEX ++;
    database[index] = (GDB)malloc(sizeof(struct genericDB));
    
    for(int i = 0;i<length; i++){
        database[index]->relation[i] = relation[i];
    }
    database[index]->id = id;
    database[index]->length = length;
    database[index]->index = 0;
}
//fill the database with the registrar data from the textbook
void genPutInDatabase(){
    //char *in[4];
    
    // CSG
    char *csg[] = {"Course", "StudentID", "Grade"};
    genInsertRelation(csg, 3, "CSG" ,dbINDEX);
    
    char *in1[] = {"CS101", "12345", "A"};
    genInsert(in1, "CSG");
    
    char *in2[] = {"CS101", "67890", "B"};
    genInsert(in2, "CSG");
    
    char *in3[3] = {"EE200", "12345", "C"};
    genInsert(in3, "CSG");
    
    char *in[4];
    in[0] = "EE200";
    in[1] = "22222";
    in[2] = "B+";
    genInsert(in, "CSG");
    
    in[0] = "CS101";
    in[1] = "33333";
    in[2] = "A-";
    genInsert(in, "CSG");
    
    in[0] = "PH100";
    in[1] = "67890";
    in[2] = "C+";
    genInsert(in, "CSG");
    
    // SNAP
    char *snap[] = {"StudentID", "Name", "Address", "Phone"};
    genInsertRelation(snap, 4, "SNAP" ,dbINDEX);
    
    in[0] = "12345";
    in[1] = "C. Brown";
    in[2] = "12 Apple St.";
    in[3] = "555-1234";
    genInsert(in, "SNAP");
    
    in[0] = "67890";
    in[1] = "L. Van Pelt";
    in[2] = "34 Pear Ave.";
    in[3] = "555-5678";
    genInsert(in, "SNAP");
    
    in[0] = "22222";
    in[1] = "P. Patty";
    in[2] = "56 Grape Blvd.";
    in[3] = "555-9999";
    genInsert(in, "SNAP");
    
    // CP
    char *cp[] = {"Course", "Prerequisite"};
    genInsertRelation(cp, 2, "CP" ,dbINDEX);
    
    in[0] = "CS101";
    in[1] = "CS100";
    genInsert(in, "CP");
    
    in[0] = "EE200";
    in[1] = "EE005";
    genInsert(in, "CP");
    
    in[0] = "EE200";
    in[1] = "CS100";
    genInsert(in, "CP");
    
    in[0] = "CS120";
    in[1] = "CS101";
    genInsert(in, "CP");
    
    in[0] = "CS121";
    in[1] = "CS120";
    genInsert(in, "CP");
    
    in[0] = "CS205";
    in[1] = "CS101";
    genInsert(in, "CP");
    
    in[0] = "CS206";
    in[1] = "CS121";
    genInsert(in, "CP");
    
    in[0] = "CS206";
    in[1] = "CS205";
    genInsert(in, "CP");
    
    // CDH
    char *cdh[] = {"Course", "Day", "Hour"};
    genInsertRelation(cdh, 3, "CDH" ,dbINDEX);
    
    in[0] = "CS101";
    in[1] = "M";
    in[2] = "9AM";
    genInsert(in, "CDH");
    
    in[0] = "CS101";
    in[1] = "W";
    in[2] = "9AM";
    genInsert(in, "CDH");
    
    in[0] = "CS101";
    in[1] = "F";
    in[2] = "9AM";
    genInsert(in, "CDH"); 
    
    in[0] = "EE200";
    in[1] = "Tu";
    in[2] = "10AM";
    genInsert(in, "CDH");
    
    in[0] = "EE200";
    in[1] = "W";
    in[2] = "1PM";
    genInsert(in, "CDH");
    
    in[0] = "EE200";
    in[1] = "Th";
    in[2] = "10AM";
    genInsert(in, "CDH");
    
    // CR
    char *cr[] = {"Course", "Room"};
    genInsertRelation(cr, 2, "CR" ,dbINDEX);
    
    in[0] = "CS101";
    in[1] = "Turing Aud.";
    genInsert(in, "CR");
    
    in[0] = "EE200";
    in[1] = "25 Ohm Hall";
    genInsert(in, "CR");
    
    in[0] = "PH100";
    in[1] = "Newton Lab.";
    genInsert(in, "CR");
    
    // CR2
    genInsertRelation(cr, 2, "CR2" ,dbINDEX);
    
    in[0] = "CS173";
    in[1] = "Turing Aud.";
    genInsert(in, "CR2");
    
    in[0] = "EE200";
    in[1] = "25 Ohm Hall";
    genInsert(in, "CR2");
    
    in[0] = "WR273";
    in[1] = "Meliora 203";
    genInsert(in, "CR2");
}