# AddressBookCLI

## Adderss BookCLI V2.1
 This is a minor quality of life update to fix some bugs!
 Change Log:
 1. Imporoved reliablity when performing action on the database
 2. Auto check for update
 - The program will check for updates when launched and notify you if you need to update!

Verison 2.0 has been released!

This is the biggest update since I have release the first address book!

## Key Features
 - Save Connections
 - New CLI
 - More data fields
 - More ways to interact with data
 - Logging system for errors
 - Check for updated version
 - More flexibility for data searching
 - Date and time creation for each object
 - More reliable
 - Bug fixes
 
 ## How to Set Up your own server!
  If you want to use your own server then this program has that ability!
  To start you will need to setup a MySQL server and a database.
  Then create 3 tables one for address, occupation and person!
  I will release the scripts that I use to setup my server once I finish creating them!
  
 ## How to use my server!
  At this time there is no easy way to use my server unless you want to edit the addressBook.java file and enable testing!
  I plan on releasing a second .jar file with this defaulted!
  
 ## Found a bug!
  Please report the bug under the issues tab with the bug label or tweet me on twitter @jackstockley_
 
 ## Have a question?
  You can leave questions under the issues tab with the question lable or tweet me on twitter @jackstockley_
  
 ## Commonly Asked Questions?
  - How do I run the .jar file?
    - You will need to have java version 8 or newer installed on your computer and then all you need to do is to open a terminal or command prompt and type `java -jar *dirrectory to the jar file*`
 
 - What operating systems are supported?
   - Pretty much any operating system that supports java version 8 or newer. That includes Mac, Windows, Linux and possibly Andriod!
   
- When will the REST inteface and the AngularJS version of this be updated?
  - They have recently been update! You can check out the RESTful interface at http://jackstockleyiowa.ddns.net:8080/AddressBookREST and the AddressBookUI at http://jackstockleyiowa.ddns.net/AddressBookUI
  
- Will you continue to update this software with new features and bug fixes?
  - Yes of course, I do still have some features I plan on adding at some point. Right now I am planning on updating the backend on both the REST interface and CLI and converting the CLI into a maven project
  
- Do you have documention on the code?
  - Yes all the code I have written is public with comments as well as a java doc for the whole program. To view the javadoc you can go to https://jnstockley.github.io/AddressBook/
