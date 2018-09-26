COMPILE:
	javac schoolsearch.java

RUN:
	java schoolsearch
OR:
	java schoolsearch 1
	This version runs in test mode - it removes the output of the command list, for cleaner output.

Our program accepts any of the following in terms of commands:
	Capitalized first word of command (Student)
	First word of command (student)
	Capital first letter of command (S)
	First letter of command (s)
A colon is required after all commands except for Info and Quit
Separate arguments by spaces, any arguments in [] are optional