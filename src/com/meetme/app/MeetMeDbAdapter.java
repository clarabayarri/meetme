package com.meetme.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.meetme.search.User;

//TODO reanomenar tot el que �s mail a Email

//TODO pillar el username de les shared preferences sempre que s'hagin d'agafar coses del user o crear-le/updatejar-les
//TODO segur que val la pena? no implica un acoblament i perd reusabilitat a sac?

//TODO quan s'esborra un User s'esborren totes les coses user-related? 
public class MeetMeDbAdapter {
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_NAME = "name";
	private static final String KEY_COMPANY = "company";
	private static final String KEY_POSITION = "position";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_TWITTER = "twitter";
	private static final String KEY_CONTACT = "contact";
	private static final String KEY_COMMENT = "comment";
	private static final String KEY_LOCATION = "location";
	private static final String KEY_PHONE = "phonenumber";
	private static final String KEY_WEB = "webpage";
	private static final String KEY_MAIL = "mail";
	
	private static final String KEY_ROWID = "_id";
	
	private static final String TAG = "MeetMeDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	
	private static final String DATABASE_CREATE_USERS = 
		"create table users (username text primary key, password text not null)";

	
	private static final String DATABASE_CREATE_PROFILES = 
		"create table profiles (username primary key, name text, company text, "
		+ "position text, image blob, twitter text)";
	
	private static final String DATABASE_CREATE_PHONES =
		"create table phones (_id integer primary key autoincrement, "
		+ "username text not null, phonenumber text not null)";
	
	private static String DATABASE_CREATE_WEBS = 
		"create table webs (_id integer primary key autoincrement, "
		+ "username text not null, webpage text not null)";
	
	private static final String DATABASE_CREATE_MAILS = 
		"create table mails (_id integer primary key autoincrement, "
		+ "username text not null, mail text not null)";
	
	private static final String DATABASE_CREATE_CONTACTS = 
		"create table contacts (_id integer primary key autoincrement, "
		+ "username text not null, contact text not null, comment text, location text";
	
	private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_USERS = "users";
    private static final String DATABASE_TABLE_PROFILES = "profiles";
    private static final String DATABASE_TABLE_PHONES = "phones";
	private static final String DATABASE_TABLE_WEBS = "webs";
	private static final String DATABASE_TABLE_MAILS = "mails";
	private static final String DATABASE_TABLE_CONTACTS = "mails";

    private static final int DATABASE_VERSION = 1;
	
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	DatabaseHelper(Context context) {
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}
    	
    	@Override
    	public void onCreate(SQLiteDatabase db) {
    		db.execSQL(DATABASE_CREATE_USERS);
    		db.execSQL(DATABASE_CREATE_PROFILES);
    		db.execSQL(DATABASE_CREATE_PHONES);
    		db.execSQL(DATABASE_CREATE_WEBS);
    		db.execSQL(DATABASE_CREATE_MAILS);
    		db.execSQL(DATABASE_CREATE_CONTACTS);
    	}
    	

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
		}
    	
    }
    
    public MeetMeDbAdapter(Context ctx) {
    	mCtx = ctx;
    }
    
    public MeetMeDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
 
    public void close() {
        mDbHelper.close();
    }
    
    
    
    
    
    
    
    
    /**
     * TAULA USERS: USERNAME I PASSWORD
     */
    
    /**
     * Crea un nou usuari.
     * @param username el nom d'usuari �nic
     * @param password la contrassenya associada al nom d'usuari
     * @return rowId o -1 si ha fallat
     */
    public boolean createUser(String username, String password) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, username);
    	initialValues.put(KEY_PASSWORD, password);
    	return mDb.insert(DATABASE_TABLE_USERS, null, initialValues) > -1;
    }
    
    public boolean updateUser(String username, String password) {
    	ContentValues args = new ContentValues();
    	args.put(KEY_USERNAME, username);
    	args.put(KEY_PASSWORD, password);
    	return mDb.update(DATABASE_TABLE_USERS, args, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
    public boolean deleteUser(String username) {
        return mDb.delete(DATABASE_TABLE_USERS, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
    /**
     * Retorna un cursor amb la password de l'usuari username.
     * @param username identificador de l'usuari
     * @return nom�s retorna la password perqu� el username ja el sabem
     * @throws SQLException
     */
    public User fetchUser(String username)  throws SQLException {
		User user = new User();
    	Cursor cursor = mDb.query(true, DATABASE_TABLE_USERS, new String[] {KEY_PASSWORD}, KEY_USERNAME + "=" + "'" + username + "'",
				null, null, null, null, null);
    	
        if (cursor != null) {
            cursor.moveToFirst();
            user.setUsername(username);
        	user.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        	cursor.close();
        }
        return user;
    }
    
    public boolean existsUser(String username) {
    	Cursor cursor = mDb.query(true, DATABASE_TABLE_USERS, new String[] {KEY_PASSWORD}, KEY_USERNAME + "=" + "'" + username + "'",
				null, null, null, null, null);
    	
        if (cursor != null) {
            return cursor.moveToFirst();
        }
        return false;
    }
    
    
    
    
    
    /**
     * TAULA PROFILES: emmagatzema la informaci� de perfil dels usuaris via username
     */
    
    
    public boolean createProfile(User user) {
    	
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, user.getUsername());
    	initialValues.put(KEY_NAME, user.getName());
    	initialValues.put(KEY_COMPANY, user.getCompany());
    	initialValues.put(KEY_POSITION, user.getPosition());
    	initialValues.put(KEY_IMAGE, user.getImage());
    	initialValues.put(KEY_TWITTER, user.getTwitter());
    	return mDb.insert(DATABASE_TABLE_PROFILES, null, initialValues) > -1;
    }
    
    public boolean updateProfile(User user) {
    	ContentValues args = new ContentValues();
    	args.put(KEY_USERNAME, user.getUsername());
    	args.put(KEY_NAME, user.getName());
    	args.put(KEY_COMPANY, user.getCompany());
    	args.put(KEY_POSITION, user.getPosition());
    	args.put(KEY_IMAGE, user.getImage());
    	args.put(KEY_TWITTER, user.getTwitter());
    	return mDb.update(DATABASE_TABLE_PROFILES, args, KEY_USERNAME + "=" + "'" + user.getUsername() + "'", null) > 0;
    }
    
    
    public boolean deleteProfile(String username) {
        return mDb.delete(DATABASE_TABLE_PROFILES, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
    public User fetchProfile(String username) throws SQLException {
    	User user = new User();
    	user.setUsername(username);
    	Cursor cursor =
            mDb.query(true, DATABASE_TABLE_PROFILES, new String[] {KEY_NAME, KEY_COMPANY, KEY_POSITION,
            		KEY_IMAGE, KEY_TWITTER}, KEY_USERNAME + "=" + "'" + username + "'",
            		null, null, null, null, null);
        if (cursor != null) {
           cursor.moveToFirst();
           user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
           user.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
           user.setPosition(cursor.getString(cursor.getColumnIndex(KEY_POSITION)));
           user.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));
           user.setTwitter(cursor.getString(cursor.getColumnIndex(KEY_TWITTER)));
           cursor.close();
           fetchPhonesOf(user);
           fetchMailsOf(user);
           fetchWebsOf(user);
        }
        return user;
    }
    
    
    
    
    
    /**
     * TAULA PHONES: username + phone
     */
    
    
    /**
     * Es crea una fila a la taula phones per a representar que
     * l'usuari userId t� el tel�fone phoneNumber.
     * @param userId la rowId de l'usuari
     * @param phoneNumber un dels n�meros de tel�fon de l'usuari
     * @return rowId o -1 si ha fallat
     */
    public boolean createPhone(String username, String phoneNumber) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, username);
    	initialValues.put(KEY_PHONE, phoneNumber);
    	return mDb.insert(DATABASE_TABLE_PHONES, null, initialValues) > -1;
    	
    }
    
    
    /**
     * Esborra la fila de la taula phones amb id rowId
     * @param rowId id de la fila
     * @return cert si s'ha esborrat correctament
     */
    public boolean deletePhone(long rowId) {
        return mDb.delete(DATABASE_TABLE_PHONES, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * Esborra el tel�fon phoneNumber de l'usuari username.
     * @param phoneNumber n�mero de tel�fon a esborrar
     * @param username identificador de l'usuari associat al mail
     * @return cert si s'ha esborrat la fila correctament
     */
    public boolean deletePhoneOfUser(String phoneNumber, String username) {
    	return mDb.delete(DATABASE_TABLE_PHONES, KEY_USERNAME + "=" + "'" + username + "'" + 
    			" and " + KEY_PHONE + "=" + phoneNumber, null) > 0;
    }
    
    
    public boolean deletePhonesOfUser(String username) {
    	return mDb.delete(DATABASE_TABLE_PHONES, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    
    }
    
    /**
     * Retorna un cursor sobre els tel�fons d'un usuari.
     * @param userId id de la fila de l'usuari a la taula users
     * @return cursor sobre els tel�fons de l'usuari amb id userId
     * @throws SQLException
     */
    public void fetchPhonesOf(User user) throws SQLException {
    	Cursor cursor = mDb.query(true, DATABASE_TABLE_PHONES, new String[] {KEY_ROWID,
    		KEY_PHONE}, KEY_USERNAME + "=" + "'" + user.getUsername() + "'", null, null, null, null, null);
    	for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
    		user.addPhone(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_PHONE)));
    	}
    	cursor.close();   
    }	
    	

    	
    	
    	
    	
    /**
     * TAULA MAILS: username + mail	
     */
    	
	/**
     * Es crea una fila a la taula mails per a representar que
     * l'usuaru userId t� el mail mail.
     * @param userId la rowId de l'usuari
     * @param mail un dels mails de l'usuari
     * @return rowId o -1 si ha fallat
     */
    public boolean createMail(String username, String mail) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, username);
    	initialValues.put(KEY_MAIL, mail);
    	return mDb.insert(DATABASE_TABLE_MAILS, null, initialValues) > -1;  	
    }
    
    /**
     * Esborra la fila de la taula mails amb id rowId
     * @param rowId id de la fila
     * @return cert si s'ha esborrat correctament
     */
    public boolean deleteMail(long rowId) {
        return mDb.delete(DATABASE_TABLE_MAILS, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Esborra el mail mail de l'usuari username.
     * @param mail mail a esborrar
     * @param username identificador de l'usuari associat al mail
     * @return cert si s'ha esborrat la fila correctament
     */
    public boolean deleteMailOfUser(String mail, String username) {
    	return mDb.delete(DATABASE_TABLE_MAILS, KEY_USERNAME + "=" + "'" + username + "'" + 
    			" and " + KEY_MAIL + "=" + mail, null) > 0;
    }
    
    
    public boolean deleteMailsOfUSer(String username) {
    	return mDb.delete(DATABASE_TABLE_MAILS, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
    
    /**
     * Retorna un cursor sobre els mails d'un usuari.
     * @param userId id de la fila de l'usuari a la taula users
     * @return cursor sobre els mails de l'usuari amb id userId
     * @throws SQLException
     */
    private void fetchMailsOf(User user) throws SQLException {
    	Cursor cursor = mDb.query(true, DATABASE_TABLE_MAILS, new String[] {KEY_ROWID,
    		KEY_MAIL}, KEY_USERNAME + "=" + "'" + user.getUsername() + "'", null, null, null, null, null);  
    	for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
    		user.addEmail(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_MAIL)));
    	}
    	cursor.close();
    }
    
    
    
    
    
    
    
    
    
    /**
     * TAULA WEBS: username + web
     */
    
    
    /**
     * Es crea una fila a la taula webs per a representar que
     * l'usuari userId t� la web webPage.
     * @param userId la rowId de l'usuari
     * @param webPage una de les webs de l'usuari
     * @return rowId o -1 si ha fallat
     */
    public boolean createWeb(String username, String webPage) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, username);
    	initialValues.put(KEY_WEB, webPage);
    	return mDb.insert(DATABASE_TABLE_WEBS, null, initialValues) > -1;
    	
    }
    
    /**
     * Esborra la fila de la taula webs amb id rowId
     * @param rowId id de la fila
     * @return cert si s'ha esborrat correctament
     */
    public boolean deleteWeb(long rowId) {
        return mDb.delete(DATABASE_TABLE_WEBS, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * Esborra la web webPage de l'usuari username.
     * @param webPage web a esborrar
     * @param username identificador de l'usuari associat al mail
     * @return cert si s'ha esborrat la fila correctament
     */
    public boolean deleteWebOfUser(String webPage, String username) {
    	return mDb.delete(DATABASE_TABLE_WEBS, KEY_USERNAME + "=" + "'" + username + "'" + 
    			" and " + KEY_WEB + "=" + webPage, null) > 0;
    }
    
    
    public boolean deleteWebsOfUser(String username) {
    	return mDb.delete(DATABASE_TABLE_WEBS, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
    /**
     * Retorna un cursor sobre les webs d'un usuari.
     * @param userId id de la fila de l'usuari a la taula users
     * @return cursor sobre les webs de l'usuari amb id userId
     * @throws SQLException
     */
    private void fetchWebsOf(User user) throws SQLException {
    	//TODO distinct true?
    	Cursor cursor = mDb.query(true, DATABASE_TABLE_WEBS, new String[] {KEY_ROWID,
    		KEY_WEB}, KEY_USERNAME + "=" + "'" + user.getUsername() + "'", null, null, null, null, null);   
    	for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
    		user.addWeb(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_WEB)));
    	}
    	cursor.close();    
    }
  
    
    /**
     * TAULA CONTACTS: username, contacte, comment, location
     */
    
    public boolean createContact(String username, String contact, String comment, String location) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_USERNAME, username);
    	initialValues.put(KEY_CONTACT, contact);
    	initialValues.put(KEY_COMMENT, comment);
    	initialValues.put(KEY_LOCATION, location);
    	
    	return mDb.insert(DATABASE_TABLE_CONTACTS, null, initialValues) > -1;
    }
    
    public boolean updateContact(String username, String contact, String comment, String location) {
    	ContentValues args = new ContentValues();
    	args.put(KEY_USERNAME, username);
    	args.put(KEY_CONTACT, contact);
    	args.put(KEY_COMMENT, comment);
    	args.put(KEY_LOCATION, location);
    	
    	return mDb.update(DATABASE_TABLE_CONTACTS, args, KEY_USERNAME + "=" + "'" + username + "'" + " and " + KEY_CONTACT + "=" + "'" + contact + "'", null) > 0;
    }
    
    public ArrayList<User> fetchContacts(String username) {
    	ArrayList<User> result = new ArrayList<User>();
    	Cursor cursor = mDb.query(DATABASE_TABLE_CONTACTS, new String[] {KEY_CONTACT, KEY_COMMENT, KEY_LOCATION}, KEY_USERNAME + "=" + "'" + username + "'", null, null, null, null); 
    	for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
    		User auxUser = new User();
    		auxUser.setUsername(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_CONTACT)));
    		auxUser.setComment(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_COMMENT)));
    		auxUser.setLocation(cursor.getString(cursor.getColumnIndex(MeetMeDbAdapter.KEY_LOCATION)));
    		result.add(auxUser);
    	}
    	return result;
    }
    
    public boolean deleteContact(String username, String contact) {
    	return mDb.delete(DATABASE_TABLE_CONTACTS, KEY_USERNAME + "=" + "'" + username + "'" + " and " + KEY_CONTACT + "=" + "'" + contact + "'", null) > 0;
    }
    
    public boolean deleteContactsOfUser(String username) {
    	return mDb.delete(DATABASE_TABLE_CONTACTS, KEY_USERNAME + "=" + "'" + username + "'", null) > 0;
    }
    
/*
    
    
    /**
     * Esborra un usuari per complet: la fila de la taula d'usuaris i les files
     * associades al mateix usuari de les taules phones, mails i webs.
     * @param rowId id de l'usuari que volem esborrar
     * @return cert si s'ha esborrat tota la informaci� relativa a l'usuari
     */
    /*
    public boolean deleteUser(long rowId) {
        if (mDb.delete(DATABASE_TABLE_MAILS, KEY_USERID + "=" + rowId, null) <= 0) return false;
    	if (mDb.delete(DATABASE_TABLE_PHONES, KEY_USERID + "=" + rowId, null) <= 0) return false;
    	if (mDb.delete(DATABASE_TABLE_WEBS, KEY_USERID + "=" + rowId, null) <= 0) return false;
    	return mDb.delete(DATABASE_TABLE_USERS, KEY_ROWID + "=" + rowId, null) > 0;
    }

*/
        
}
