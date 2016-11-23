# ScanUnsafe4Macro
This is a Netbean Project, checking unsafe commands for a Java Macro File.
To run, open Netbean and set Running Parameters in Running Settings of Netbean project.
Set the paramter like below.
  <RootPath>/Test/csv <RootPath>/Test/boundarySet.java
Program Output:
  It prints "macro is safe", iff java file has no unsafed commands.
  It prints "macro contains unsafe commands" with number of lines containg line number and full class name, iff java file has some unsafe commands.
