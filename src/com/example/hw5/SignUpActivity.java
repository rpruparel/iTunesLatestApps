package com.example.hw5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	EditText firstNameField;
	EditText lastNameField;
	EditText emailField;
	EditText passwordField;
	EditText passwordConfirmField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);


		//assign fields
		firstNameField = (EditText) findViewById(R.id.editTextFirstName);
		lastNameField = (EditText) findViewById(R.id.editTextLastName);
		emailField = (EditText) findViewById(R.id.editTextEmail);
		passwordField = (EditText) findViewById(R.id.editTextPasswordSignUp);
		passwordConfirmField = (EditText) findViewById(R.id.editTextConfirmPassword);




		//sign up button to send to parse.com
		findViewById(R.id.buttonSignUp).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//get strings
				String firstName = firstNameField.getText().toString();
				String lastName = lastNameField.getText().toString();
				String email = emailField.getText().toString();
				String password = passwordField.getText().toString();
				String passwordConfirm = passwordConfirmField.getText().toString();


				//Verify fields are not blank
				if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || passwordConfirm.equals("")) {
					Toast.makeText(SignUpActivity.this, "Create logon faild. Do not leave any fields blank", Toast.LENGTH_LONG).show();
				} else if (!password.equals(passwordConfirm)) {
					Toast.makeText(SignUpActivity.this, "Passwords do not match. Please try again", Toast.LENGTH_LONG).show();
				} else {

					Log.d("signup", "creating logon");
					//make user object
					ParseUser user = new ParseUser();
					user.setUsername(email);
					user.setPassword(password);
					user.setEmail(email);
					user.put("firstName", firstName);
					user.put("lastName", lastName);


					//send to parse, will return error if already chosen
					user.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								Log.d("signup", "successful sign up ");
								Toast.makeText(SignUpActivity.this, "Login successful", Toast.LENGTH_LONG).show();
								Intent intent = new Intent(SignUpActivity.this, AppsActivity.class);
								startActivity(intent);
								finish();

							} else {
								//email already taken
								Toast.makeText(SignUpActivity.this, "Email already taken. Please select another email address.", Toast.LENGTH_LONG).show();
							}



						}
					});

				}


			}
		});


		//cancel button
		findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();


			}
		});


	}
}