# JavaCoreMileStone1


📘 UML Diagrams for Portfolio Projects

Below are UML class diagrams for each CLI project in this repository. Each diagram shows the main classes, their attributes, methods, and relationships. Together, they demonstrate object‑oriented design principles such as encapsulation, composition, and persistence.
1. Calculator CLI App

Note: A simple arithmetic calculator demonstrating basic operations.
Code

+-------------------+
| CalculatorCliApp  |
+-------------------+
|                   |
+-------------------+
| add(a:int,b:int):int      |
| subtract(a:int,b:int):int |
| multiply(a:int,b:int):int |
| divide(a:int,b:int):int   |
+-------------------+

2. Array Utilities

Note: Utility class showcasing array algorithms such as palindrome check and reversal.
Code

+-------------------+
| ArrayUtilities    |
+-------------------+
|                   |
+-------------------+
| isPalindrome(arr:int[]):boolean |
| reverseArray(arr:int[]):int[]   |
| findMax(arr:int[]):int          |
| findMin(arr:int[]):int          |
+-------------------+

3. Student Grade Manager

Note: Manages student grades, calculates statistics, and pass/fail reports.
Code

+-------------------+
| StudentGradeManager |
+-------------------+
| students:ArrayList<Student> |
+-------------------+
| addStudent(...)   |
| listStudents()    |
| showStats()       |
| passFail(threshold:int) |
+-------------------+

+-------------------+
| Student           |
+-------------------+
| name:String       |
| school:String     |
| regNo:String      |
| yearOfStudy:int   |
| grade:int         |
+-------------------+
| getGrade():int    |
+-------------------+

4. Learner Record Manager (StudentManagerCli)

Note: A persistent record manager for learners, with CRUD operations, statistics, and report export.
Code

+-------------------+
| Learner           |
+-------------------+
| id:int            |
| name:String       |
| school:String     |
| regNo:String      |
| yearOfStudy:int   |
| grade:int         |
+-------------------+
| toString():String |
| fromString(line)  |
| toFileString():String |
+-------------------+

+---------------------------+
| LearnerRecordManager      |
+---------------------------+
| learners:ArrayList<Learner> |
| nextId:int                |
| filePath:String           |
+---------------------------+
| loadLearners()            |
| saveLearners()            |
| addLearner(...)           |
| listLearners()            |
| updateGrade(id:int,grade:int) |
| deleteLearner(id:int)     |
| showStats()               |
| passFail(threshold:int)   |
| exportReport(threshold:int) |
+---------------------------+

5. To‑Do List Manager

Note: A task manager with persistence, supporting add, list, mark done, and delete.
Code

+-------------------+
| Work (Task)       |
+-------------------+
| id:int            |
| description:String|
| isDone:boolean    |
+-------------------+
| markDone()        |
| toString():String |
| fromString(line)  |
+-------------------+

+-------------------+
| ToDoLists         |
+-------------------+
| tasks:ArrayList<Work> |
| nextId:int        |
| filePath:String   |
+-------------------+
| loadTasks()       |
| saveTasks()       |
| addTask(desc:String) |
| listTasks()       |
| markTaskDone(id:int) |
| deleteTask(id:int) |
+-------------------+

6. Banking System

Note: Simulates accounts with deposit, withdrawal, and transfer functionality.
Code

+-------------------+
| Account           |
+-------------------+
| id:int            |
| owner:String      |
| balance:double    |
+-------------------+
| deposit(amount:double) |
| withdraw(amount:double)|
| getBalance():double    |
+-------------------+

+-------------------+
| BankingSystem     |
+-------------------+
| accounts:ArrayList<Account> |
+-------------------+
| addAccount(...)   |
| listAccounts()    |
| transfer(from:int,to:int,amount:double) |
+-------------------+

7. Authentication System

Note: Manages user registration and login with password validation.
Code

+-------------------+
| User              |
+-------------------+
| username:String   |
| password:String   |
+-------------------+
| checkPassword(pw:String):boolean |
+-------------------+

+-------------------+
| AuthenticationSystem |
+-------------------+
| users:ArrayList<User> |
+-------------------+
| register(username:String,password:String) |
| login(username:String,password:String):boolean |
+-------------------+

8. Library System

Note: A library manager for books, supporting borrow and return operations.
Code

+-------------------+
| Book              |
+-------------------+
| id:int            |
| title:String      |
| author:String     |
| available:boolean |
+-------------------+
| borrow()          |
| returnBook()      |
+-------------------+

+-------------------+
| LibrarySystem     |
+-------------------+
| books:ArrayList<Book> |
+-------------------+
| addBook(...)      |
| listBooks()       |
| borrowBook(id:int)|
| returnBook(id:int)|
+-------------------+

