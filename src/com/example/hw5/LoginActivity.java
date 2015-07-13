package com.example.hw5;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText emailField;
	EditText passwordField;
	static int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//prevents error from logging into Parse twice on the same app
		if (count == 0) {
			//setup parse
			Parse.enableLocalDatastore(this);
			Parse.initialize(this, "<your application id>", "<client id>");
			count++;
		}


		//*****AUTOMATIC LOGOUT DELETE THIS LATER*****
		//ParseUser.getCurrentUser().logOut();



		//pull in buttons
		emailField = (EditText) findViewById(R.id.editTextEmail);
		passwordField = (EditText) findViewById(R.id.editTextPassword);

		//check if current active user, if so, start to do activity
		if (ParseUser.getCurrentUser() != null) {
			Log.d("login", "there is current user, launching todo");
			Intent intent = new Intent(LoginActivity.this, AppsActivity.class);
			startActivity(intent);
			finish();
		}


		//login listener
		findViewById(R.id.buttonLogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = emailField.getText().toString();
				String password = passwordField.getText().toString();

				if (email.equals("") || password.equals("")) {
					Toast.makeText(LoginActivity.this, "Please complete both email and password fields", Toast.LENGTH_LONG).show();

				} else {
					ParseUser.logInInBackground(email, password, new LogInCallback() {

						@Override
						public void done(ParseUser user, ParseException e) {
							if (user != null) {
								Log.d("login", "login successful, launching todo");
								Intent intent = new Intent(LoginActivity.this, AppsActivity.class);
								startActivity(intent);
								finish();


							} else {
								Toast.makeText(LoginActivity.this, "Login was not successful", Toast.LENGTH_LONG).show();
								Log.d("login", "login failed");

							}
							// TODO Auto-generated method stub

						}
					});


				}



			}
		});



		//create new account on click listener
		findViewById(R.id.buttonCreateAcct).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
				finish();
			}
		});





	}
}