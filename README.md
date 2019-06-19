# AddressBook

Important!!!!! This software will stop working correctly in the next couple of days. I will eventually fix this once I am able to get SSL working correctly. For the time being. Please use the web server located at https://jackstockely.live/AddressBookUI/ to make any changes. It is currently the only functioning way to add data that is encrypted on the server and in an SSL connection. Thank You!

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
