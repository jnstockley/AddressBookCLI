# AddressBook

Wow its been a while since I posted an update here! First off I am currently working on a version 2.0 which will include some major changes. Ill list a few bellow. That list is subject to change! Also I am looking for some beta testers. I will release a beta version of this in the coming weeks. If youd like to be on the list tweet on @jackstockley_ or add your request to the beta tester issue!

Mini Update!!
   I have 90% of the backend written, need to test it out still! I am starting to write the frontend of the sotware now! 
   I will relase a beta jar of addressBook 2.0 when I have done some testing! Stay Tuned!

Mini Update #2!!
   I have the backend written, I have 90% of frontend written, I have done some basic testing to make sure backend works best    case senario. I have found some bugs, some I have fixed others I will fix. I was hoping to release a beta tonight but a        stupid bug got in the way. I will try to release a beat tomorrow. No promises. Also I have added some new features that I      will start to work on. Some will not be available in the first beta but will be by release!
   
Mini Update #3!!
   I have uploaded a very early verision of address book version 2.0! Please expect bugs and report them here on the issues tab!
   
Mini Update #4!!
   I have uploaded a new version which has bug fixes and improvements! Below is a list of the major changes in this new version! More to come!!
   
Mini Update #5!!
   I have uploaded a zip file containing a new jar file and a csv file. The csv file contains an encrypted login for you to play with. I have also re-done the save connections function to use salting. Please test this new file out!
   1. Fixed bug when getting address, occupations, people by a field that could have a space in it
   2. When getting address, occupation, or person by date or tiem field displayed format
   3. Added validation for getting by IDs and adding ID to people
   4. Capitalized words when added to database
   5. Fixed bug when adding multiple address, occupations, people where the user needed to hit the enter key to add another address, occupation, or person
   6. Changed remove similar to remove singular 
   7. When removing data it showed two success messages

Changes coming in version 2.0!
   1. Logging system ✔️
   2. Ability to add multiple addresses, occupations, and people at the same time ✔️
   3. Ability to remove multiple addresses, occupations, and people at the same time ✔️
   4. Ability to update multiple addresses, occupation, and people at the same time ✔️
   5. More fields for addresses, occupations, people,
      Ex. occupation now has Company Name, Job Title, Employment Type, Monthly Salary, and Industry ✔️
   6. Better formating for displaying data in cosnole.
   7. Possible way to run .jar in a web broweser (Havent looked into this just a possibility)
   8. Checking that data was succeffuly added, removed, updated from server ✔️
   9. New database name that reflect what the program does ✔️
   10. Ability to save connections ✔️
   11. Bug fixes and overall better improvements!
   12. AES Encryption
   13. Program Updater
   14. Check to see if entry already exists
   15. Ability to create new addresses and occupations when adding a new person ✔️
   
Address Book CLI Version 1.0.2!
   How to use this program:
   1. Run the program in command prompt or a terminal depending on which OS you are on
   2. When prompted type in IP of your SQL server
   3. Enter the user name and password
   4. Select which table and which method you want to use (Type 0 to exit program)

What's new in Version 1.0:
 1. New features:
  - Added ability to work with any SQL server
  - Added ability to enter personal username and password
  - When typing in the password, the password is hidden
  - Added ability to get multiple address, occupations, and people, with similar fields
  - Re-designed CLI
 2. Bug Fixes and many performance improvements
   - Re-designed back-end to handle new features and more to come in the future
   - The program should crash less often

Version 1.0.1
 1. Bug Fixes
   - Occupation now shows up when requesting data from person table
   - Address ID didn't show up correctly
   - When updating data it would override all previous data
   - Unable to leave field blank for keeping addressId and occupationId the same
   - Small typos fixes
 2. New feature
  - Better UI for updating addressId and occupationId

Version 1.0.2
 1. New features:
  - Better UI for closing the program
  - Better UI for going back to table selector
  - Added Credit
  - Added closing message
  - Added link to GitHub page

What's to come in later versions
  - RSA Encryption with a public and private key
  - More ways to manipulate the data on the MySQL server
  - Program Updater
  - The ability for the program to produce logs
  - Better UI for inputting field name
  - Ability to create new addresses and occupations when adding a new person
  - Better Handling of connection loss
  - Check for duplicate entries

Any other questions tweet me on twitter @jackstockley_ and Ill add it here

Find a bug please be sure to report it under the issues tab!
