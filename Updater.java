package com.github.jnstockley.addressbookcli;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * This small part of the program helps check for version updated of the program and tells the user how to download them
 * @author jnstockley
 * @version 3.1
 *
 */

public class Updater {

	private FrontendHelper frontendHelper = new FrontendHelper();
	
	/**
	 * Checks a text file stored on my github repository and gets the current version number and checks if the program version is less then or equal to the newest version
	 * @param appVersion A variable stored in the main class of the program that has the current program version
	 * @return False is needs updating or true if up to date
	 */
	public boolean upToDate(double appVersion) {
		try{
			Document doc = Jsoup.connect("https://github.com/jnstockley/AddressBook/blob/master/version.txt").get(); //The URL to where the version text file is stored
			String currentVersion = doc.select("table").first().text(); //Find the string of text in the version.txt file
			if(appVersion<Double.parseDouble(currentVersion)) { //Checks if the version number on the github repository is greater then the program version
				System.out.println("App out of date!");
				System.out.println("You are running version: " + appVersion);
				System.out.println("The newest version is: " + currentVersion);
				System.out.println("Please update at https://github.com/jnstockley/AddressBook");
				System.out.println("You must update in order to continue!");
				return false;
			}else {
				return true;
				//System.out.println("App up to date!");
				//System.out.println("You are running version: " + appVersion);
			}
		}catch(Exception e) {
			System.out.println("Error when checking for updates!");
			frontendHelper.log(e, "Updater.java", "update()");
			return false;
		}
	}
}