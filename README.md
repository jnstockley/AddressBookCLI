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
   I have uploaded a new version which has bug fixes and improvements! Below is a list of the major changes in this new version! More to come!
   1. Fixed bug when getting address, occupations, people by a field that could have a space in it
   2. When getting address, occupation, or person by date or tiem field displayed format
   3. Added validation for getting by IDs and adding ID to people
   4. Capitalized words when added to database
   5. Fixed bug when adding multiple address, occupations, people where the user needed to hit the enter key to add another address, occupation, or person
   6. Changed remove similar to remove singular 
   7. When removing data it showed two success messages
  
Mini Update #5!!
   I have uploaded a zip file containing a new jar file and a csv file. The csv file contains an encrypted login for you to play with. I have also re-done the save connections function to use salting. Please test this new file out!
   
Mini Update #6!!
   I have uploaded a new zip file containing a new beta version of addressbook v2 and a new csv file. In the new version I have redone the handleing of everything to do with connections and the csv file and added a handy testing mode to allow the tetser to connect to my database while allowing them to use the full program!
   
Mini Update #7!!
   I have made some small improvments to the next version! There are some known bugs that I need to fix! I have deleted the changes to come in version 2.0 section and replaced it with a list of changes I have made and improvements/bugs that I am still working on! I have also deleted the change logs for older version of address book expcept version 1.0.2

Improvements Since V 1.0.2 and during development of V2.0:
1. Logging system
2. Ability to add multiple addresses, occupations, and people at the same time
3. Ability to remove multiple addresses, occupations, and people at the same time
4. Ability to update multiple addresses, occupation, and people at the same time
5. More fields for addresses, occupations, people, Ex. occupation now has Company Name, Job Title, Employment Type, Monthly Salary, and Industry
6. Checking that data was succeffuly added, removed, updated from server
7. New database name that reflect what the program does
8. Ability to save connections
9. Ability to create new addresses and occupations when adding a new person
10. Saved connection doesn’t check for duplicate entries
11. Change next to nextLine in get similar
12. Add ability to remove a save connection
13. Capitalize words when entered into database
14. When inserting multiple addresses after first address is added need to press enter to be able to create next address
15. Change remove similar to remove singular show full address not just id
16. When removing address it shows two different removed messages 
17. Bug with database doesn’t show 0 in house number or zip code 
18. Added space in between address, occupation, person
19. Added ability to continuously go through program
20. Hashed passwords
21. Used CSV file for saving connections

Imporvements that are still needed/ know bugs:
1. Validation checking for ids
2. Add ability to close program or go back a page
3. Add timer for 10 sec if no response maybe???
4. Add salting to passwords
5. getSimilar doesn’t print anything!!!!
6. Better formatting when printing ID for getSingular
7. Better formatting for printing person
8. Better formating for displaying data in cosnole.
9. Bug fixes and overall better improvements!
10. AES Encryption
11. Program Updater?
12. Check to see if entry already exists
13. Form validation when updaing/creating entries
14. Printing formating new line, colors, punctuation marks
15. Add abilty to change addressId and occupationId and show addresses and occupations when updating person
16. Add backend update multiple
17. Create separate functions for adding addressId and occupationId
18. Split fileds likeFistName into two words when displaying field
19. Add regular expression check on IP
20. Add option to change default database name

Mini Update #8!!
I have uploaded a new version! I know its been a while and this version has a fair amount of changes! Please expect many bugs and report them under the issues tab! 
Improvments since last version:
1. Added ability to change addressId and occupationId and show addresses and occupations when updating person
2. Added ability to create new occupation/address when updating person
3. Added regular expression check on IP	
4. Fixed getSimilar()
5. Added new way of reading data in to improve reliability
6. Added option to change default database name
7. Added salting to passwords
8. Added ability to close program or go back a page
9. Check to see if entry already exists
10. Split fields likeFistName into two words when displaying field
11. Added backend update multiple
	
   
Address Book CLI Version 1.0.2!
   How to use this program:
   1. Run the program in command prompt or a terminal depending on which OS you are on
   2. When prompted type in IP of your SQL server
   3. Enter the user name and password
   4. Select which table and which method you want to use (Type 0 to exit program)

Version 1.0.2
 1. New features:
  - Better UI for closing the program
  - Better UI for going back to table selector
  - Added Credit
  - Added closing message
  - Added link to GitHub page

Any other questions tweet me on twitter @jackstockley_ and Ill add it here

Find a bug please be sure to report it under the issues tab!
