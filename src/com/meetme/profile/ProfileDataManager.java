package com.meetme.profile;

import java.util.ArrayList;

import com.meetme.app.MeetMeDbAdapter;
import com.meetme.search.User;

public class ProfileDataManager {

    private MeetMeDbAdapter mDbHelper;

	 public boolean updateProfile(User user) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
			
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
	    	/**
	    	 * UPDATE NORMAL DE LA FILA DE LA TAULA PROFILES
	    	 */
		 
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======
>>>>>>> 3015c695c5eb378fa6e6252bdb151771c283969c
=======

>>>>>>> parent of 72f7056... emmerda per a solucionar l'error
	    	mDbHelper.updateProfile(user);
	    	
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
	    	
	    	for (int i = 0; i < emails.size(); ++i) {
	    		if (mDbHelper.createMail(username, emails.get(i)) < 0) return false;
	    	}
	    	
	    	for (int i = 0; i < webs.size(); ++i) {
	    		if (mDbHelper.createWeb(username, webs.get(i)) < 0) return false;
	    	}
	    	
	    	return true;
	    }
}
