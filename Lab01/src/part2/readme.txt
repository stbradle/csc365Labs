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

The "Grade" command has been refactored - it now requires a third argument, instead of just allowing for two. Either the S, T, H, or H third argument must be specified. S is the functionality of the old command with no arguments, T is the functionality of NR4, and H and L behave the same as before.

"P", or "GPA" lets you list all GPAs organized by the provided argument: "B" for bus, "G" for grade, and "T" for teacher.

"Enrollment" lets you see all of the students enrolled in each grade, organized by grade.