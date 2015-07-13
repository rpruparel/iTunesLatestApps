package com.example.multifragmentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements
		FirstFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getFragmentManager().beginTransaction()
				.add(R.id.container, new FirstFragment(), "first_frag")
				.commit();
	}

	@Override
	public void goToNextFragment() {
		// TODO Auto-generated method stub

		// replace fragment, add to back stack so back button goes back to
		// previous fragment
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new SecondFragment(), "second_frag")
				.addToBackStack(null).commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// not necessary pre version 5
		// check the back stack count, if > 0 pop the back stack
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {

			super.onBackPressed();
		}
	}

}
