package com.meetme.profile;

import java.util.ArrayList;

import com.meetme.app.MeetMeDbAdapter;

public class ProfileDataManager {

    private MeetMeDbAdapter mDbHelper;

<<<<<<< HEAD
	 public boolean updateProfile(User user) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
			
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
=======
	 public boolean updateProfile(String username, String name,
	    		String company, String position, String image, String twitter, String twitterpass,
	    		ArrayList<String> phones, ArrayList<String> mails, ArrayList<String> webs) {
			
>>>>>>> parent of c5728a9... updateProfile ara té de paràmetre un User
	    	/**
	    	 * UPDATE NORMAL DE LA FILA DE LA TAULA PROFILES
	    	 */
		 
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
	    	mDbHelper.updateProfile(user);
=======
	    	mDbHelper.updateProfile(username, name, company, position, image, twitter, twitterpass);
>>>>>>> parent of c5728a9... updateProfile ara té de paràmetre un User
	    	
	    	/**
	    	 * PER A SIMPLIFICAR-HO FAR� QUE S'ESBORRIN TOTES LES FILES DE L'USUARI
	    	 * A LES TAULES AUXILIARS I QUE S'INSEREIXIN LES DELS ARRAYS
	    	 */
	    	
	    	String username = user.getUsername();
	    	ArrayList<String> phones = user.getPhones();
	    	ArrayList<String> emails = user.getEmails();	    	
	    	ArrayList<String> webs = user.getWebs();

	    	
	    	// ESBORRAT
	    	if (!mDbHelper.deletePhonesOfUser(username)) return false;
	    	if (!mDbHelper.deleteMailsOfUSer(username)) return false;
	    	if (!mDbHelper.deleteWebsOfUser(username)) return false;
	    	
	    	
	    	//INSERCI�
	    	for (int i = 0; i < phones.size(); ++i) {
	        	if (mDbHelper.createPhone(username, phones.get(i)) < 0) return false;	
	    	}
	    	
	    	for (int i = 0; i < mails.size(); ++i) {
	    		if (mDbHelper.createMail(username, mails.get(i)) < 0) return false;
	    	}
	    	
	    	for (int i = 0; i < webs.size(); ++i) {
	    		if (mDbHelper.createWeb(username, webs.get(i)) < 0) return false;
	    	}
	    	
	    	return true;
	    }
}
