package com.example.hw5;

/* FullName: Rohit Pankaj Ruparel*/

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.SAXException;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AppsActivity extends Activity {
	final int REQ_CODE = 1001;
	final String IS_FAV_KEY = "fav";

	// DatabaseDataManager dm;
	boolean isFavShowing = false;
	boolean isSharedShowing = false;

	ListView lv;
	App app = null;
	ArrayList < App > appList = null;
	AppAdapter adapter;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps);

		// instantiate list view
		lv = (ListView) findViewById(R.id.listView1);

		// load top 100 apps
		getApps();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("appsActivity", "in result");
		if (resultCode == RESULT_OK) {
			Log.d("appsActivity", "result is ok");
			if (REQ_CODE == requestCode) {

				if (isFavShowing || isSharedShowing) {
					//clear list
					appList.clear();
					adapter.notifyDataSetChanged();

					//re-run query
					if (isFavShowing) {
						display(getResources().getString(R.string.parse_isFav));
					} else if (isSharedShowing) {
						display(getResources().getString(
						R.string.parse_isShared));
					}
				}

			}
		}
	}

	private void getApps() {
		new GetAppAsyncTask()
			.execute("http://itunes.apple.com/us/rss/topgrossingapplications/limit=100/xml");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.apps_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.view_all) {
			if (isFavShowing || isSharedShowing) {
				getApps();
				isFavShowing = false;
				isSharedShowing = false;
			} else {
				Toast.makeText(AppsActivity.this, "Already showing all apps",
				Toast.LENGTH_LONG).show();
			}
		} else if (id == R.id.view_fav) {
			isFavShowing = true;
			isSharedShowing = false;
			display(getResources().getString(R.string.parse_isFav));
		} else if (id == R.id.clear_fav) {
			clear(getResources().getString(R.string.parse_isFav));
		} else if (id == R.id.view_shared) {
			isSharedShowing = true;
			isFavShowing = false;
			display(getResources().getString(R.string.parse_isShared));
		} else if (id == R.id.clear_shared) {
			clear(getResources().getString(R.string.parse_isShared));
		} else if (id == R.id.logout) {
			ParseUser.getCurrentUser().logOut();
			Intent intent = new Intent(AppsActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();

		}
		return super.onOptionsItemSelected(item);
	}

	public class GetAppAsyncTask extends
	AsyncTask < String, Void, ArrayList < App >> {

		@Override
		protected ArrayList < App > doInBackground(String...params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();

				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream in = con.getInputStream();
					Log.d("output", in .toString());
					return AppUtil.AppsSAXParser.parseApps( in );
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList < App > result) {
			// TODO Auto-generated method stub
			appList = result;
			super.onPostExecute(result);
			if (result != null) {
				adapter = new AppAdapter(AppsActivity.this, result);
				adapter.setNotifyOnChange(true);
				lv.setAdapter(adapter);
				adapter.setNotifyOnChange(true);

				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView <? > parent, View view,
					int position, long id) {
						app = (App) appList.get(position);

						// dm.saveAppDao(app);

						Intent intent = new Intent(AppsActivity.this,
						PreviewActivity.class);
						intent.putExtra("App", app);
						startActivityForResult(intent, REQ_CODE);
					}
				});
			}

		}

	}

	private void clear(String queryField) {
		final String field = queryField;

		// clear list if displaying
		if ((isFavShowing && queryField.equals(getResources().getString(
		R.string.parse_isFav))) || isSharedShowing && queryField.equals(getResources().getString(
		R.string.parse_isShared))) {


			appList.clear();
			adapter.notifyDataSetChanged();
		}

		// query parse to delete items from parse
		ParseQuery < ParseObject > query = ParseQuery.getQuery(getResources()
			.getString(R.string.parseClass));
		query.whereEqualTo(queryField, true);
		query.findInBackground(new FindCallback < ParseObject > () {

			@Override
			public void done(List < ParseObject > objects, ParseException e) {
				// empty out app list

				// convert parseobjects to app objects
				if (e == null) {
					for (ParseObject o: objects) {

						// both shared and favorite
						if (o.getBoolean("isFavorite") && o.getBoolean("isSharedWithUser")) {

							if (field.equals(getResources().getString(
							R.string.parse_isFav))) {
								o.put("isFavorite", false);
							} else {
								o.put("isSharedWithUser", false);
							}

							// save it
							o.saveInBackground();

							// only shared or favorite
						} else {
							o.deleteInBackground();
						}
					}
				} else {
					// do nothing? no favorites
					Log.d("appsActivity", "query returned error");
				}

			}
		});

	}

	private void display(String queryField) {
		Log.d("AppsActivity", "update display for " + queryField);

		// query parse
		ParseQuery < ParseObject > query = ParseQuery.getQuery(getResources()
			.getString(R.string.parseClass));
		query.whereEqualTo(queryField, true);
		query.findInBackground(new FindCallback < ParseObject > () {

			@Override
			public void done(List < ParseObject > objects, ParseException e) {
				// empty out app list
				Log.d("appActivity", objects.size() + " found, updating listview");

				// convert parseobjects to app objects
				if (e == null) {
					appList.clear();
					for (ParseObject o: objects) {
						App a = new App(o);
						appList.add(a);
					}
				} else {
					// do nothing? no favorites
					Log.d("appsActivity", "query returned error");
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

}