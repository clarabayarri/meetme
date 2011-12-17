package com.meetme.login;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.meetme.app.MeetMeActivity;
import com.meetme.app.R;
import com.meetme.app.User;
import com.meetme.search.UsersAdapter;

public class LoginActivity extends Activity {

	ViewFlipper flipper;
	Button login;
	Button create;
	TextView errorText;
	EditText usernameBox;
	EditText passwordBox;
	EditText password2Box;
	ListView usersList;
	LoginManager lm;
	UsersAdapter adapter;
	ArrayList<User> users;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        lm = new LoginManager(this);
        
        //load login.xml from layout
        setContentView(R.layout.login);
        
        //tool that let us exchange between login and register
        flipper = (ViewFlipper)findViewById(R.id.login_flip);
        
        fillLogin();
        
        login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String username = usernameBox.getEditableText().toString();
				String password = passwordBox.getEditableText().toString();
				if (lm.correctUserAndPassword(username, password)) {
					//posar el username de l'usuari actual a les shared
					lm.setActiveUser(username);
					//Iniciar l'aplicaci�
					changeToApp();
				}
				else
					errorText.setText("ERROR: username or password are not correct " + username + " " + password);
			}
		});
        
        create.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String username = usernameBox.getEditableText().toString();
				String password = passwordBox.getEditableText().toString();
				String cPass = password2Box.getEditableText().toString();
				if (password.equals(cPass)) { //passwords iguals
					if (lm.existsUser(username)) errorText.setText("ERROR CREATING NEW USER: The user already exists");
					else {
						lm.registerUser(username, password);
						lm.setActiveUser(username);
						changeToApp();
					}
				}
				else errorText.setText("ERROR: Passwords do not match");
			}
        });
        
	}
	
	
	@Override
	protected void onResume() {
		fillLogin();
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		lm.closeDb();
		super.onDestroy();
	}

	private void fillLogin() {
        users = lm.getUsers();
        
        usersList = (ListView) findViewById(R.id.users_list);
        adapter = new UsersAdapter(this, android.R.layout.simple_list_item_1, users);
        usersList.setAdapter(adapter);

        login = (Button) findViewById(R.id.login_button);
        create = (Button)findViewById(R.id.create_button);
        if (usernameBox == null) usernameBox = (EditText) findViewById(R.id.username_box);
        else usernameBox.setText("");
        if (passwordBox == null) passwordBox = (EditText) findViewById(R.id.password_box);
        else passwordBox.setText("");
        if (password2Box == null) password2Box = (EditText) findViewById(R.id.password_confirm_box);
        else password2Box.setText("");
        if (errorText == null) errorText = (TextView) findViewById(R.id.login_error_message);
        else errorText.setText("");
	}
	
	/**
	 * Funci� per quan premen el bot� de Register, que carrega el layout corresponent.
	 * @param view
	 */
	public void changeToRegister(View view) {
		//aixo carrega el segon layout dins el flipper
		flipper.showNext();
	}
	
	/**
	 * Funci� per quan estan a register i volen tornar al login
	 * @param view
	 */
	public void changeToLogin(View view) {
		//aixo torna al primer layout dins el flipper
		flipper.showPrevious();
	}
	
	private void changeToApp() {
		Intent mainIntent = new Intent(this, MeetMeActivity.class);
		startActivity(mainIntent);
	}
}