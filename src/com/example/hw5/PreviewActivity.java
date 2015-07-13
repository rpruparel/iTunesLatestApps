/* FullName: Rohit Pankaj Ruparel*/

package com.example.hw5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class PreviewActivity extends Activity {

	TextView tv;
	ImageView iv;
	App app = null;
	ImageView favIcon;
	ImageView shareIcon;

	List < ParseUser > users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		tv = (TextView) findViewById(R.id.textView1);
		iv = (ImageView) findViewById(R.id.imageView1);
		favIcon = (ImageView) findViewById(R.id.imageView2);
		shareIcon = (ImageView) findViewById(R.id.imageView3);

		// reset favicon and tag at start
		Picasso.with(PreviewActivity.this).load(R.drawable.notimportant)
			.into(favIcon);
		favIcon.setTag(false);

		// get app information and launch query
		if (getIntent().getExtras() != null) {
			app = (App) getIntent().getExtras().getSerializable("App");
			String name = app.getTitle();
			tv.setText(name);
			Picasso.with(PreviewActivity.this).load(app.getLargePhoto())
				.into(iv);

			// determine if app is favorite and set icon
			initializeFavoriteIcon();
		}

		Log.d("preview", "app id: " + app.getId());
		Log.d("preview", app.toString());

		// click on image listener
		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(app.getUrl()));
				startActivity(intent);
			}
		});

		// favorite icon
		favIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if tag is true, need to set to not favorite
				if ((Boolean) favIcon.getTag()) {
					setFavoriteStatus(false);

				} else {
					setFavoriteStatus(true);
				}
			}
		});

		// share icon
		shareIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ParseQuery < ParseUser > query = ParseUser.getQuery();
				query.findInBackground(new FindCallback < ParseUser > () {

					@Override
					public void done(List < ParseUser > objects, ParseException e) {
						// make users persistent to be used in callback
						users = objects;

						// sort list
						Collections.sort(users, new Comparator < ParseUser > () {

							@Override
							public int compare(ParseUser lhs, ParseUser rhs) {
								// TODO Auto-generated method stub
								return lhs.get("lastName")
									.toString()
									.compareTo(
								rhs.get("lastName").toString());
							}
						});

						String[] userStrings = getUserStrings(users);

						AlertDialog.Builder builder = new AlertDialog.Builder(
						PreviewActivity.this);
						builder.setTitle("Users");
						builder.setItems(userStrings,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
							int which) {

								shareAppWithUser(users.get(which));

								users.get(which).getString("lastName")
									.toString();

								// close dialog
								dialog.cancel();
							}

						});

						AlertDialog dialog = builder.create();
						dialog.show();

					}

				});

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		setResult(AppsActivity.RESULT_OK);
		finish();
	}

	/** set favorite icon if favorite */
	private void initializeFavoriteIcon() {

		ParseQuery < ParseObject > query = ParseQuery.getQuery(getResources()
			.getString(R.string.parseClass));
		query.whereEqualTo("id", app.getId());
		query.findInBackground(new FindCallback < ParseObject > () {

			@Override
			public void done(List < ParseObject > objects, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					if (objects.size() != 0) {
						if (objects.get(0).getBoolean("isFavorite")) {
							// this is a favorite then
							Log.d("previw", "on load, is favorite");
							Picasso.with(PreviewActivity.this)
								.load(R.drawable.important).into(favIcon);
							favIcon.setTag(true);

							// log the objects
							for (ParseObject o: objects) {
								Log.d("init fav", o.getString("id"));
							}
						}
					} else {
						Log.d("preview", "on load is not favorite");
					}
				} else {
					Log.d("preview Activity", "error in query for app");
				}

			}
		});

	};

	/** switch favorite icon and adjust parse data */
	private void setFavoriteStatus(boolean changeToFav) {
		final boolean setToFav = changeToFav;

		// query to get object
		ParseQuery < ParseObject > query = ParseQuery.getQuery(getResources()
			.getString(R.string.parseClass));
		query.whereEqualTo("id", app.getId());
		query.findInBackground(new FindCallback < ParseObject > () {

			@Override
			public void done(List < ParseObject > objects, ParseException e) {

				if (e != null) {
					Log.d("parse error", e.toString());
				} else {
					if (!setToFav) {
						setToNotFavorite(objects.get(0));
					} else if (objects.size() > 0) {

						// app already exists in db, update the db
						setToFavorite(objects.get(0));

					} else {

						// app doesn't exist in db, needs to be created
						setToFavorite(null);

					}
				}
			}
		});
	}

	/** updates view and parse when not favorite */
	private void setToNotFavorite(ParseObject object) {
		Picasso.with(PreviewActivity.this).load(R.drawable.notimportant)
			.into(favIcon);

		favIcon.setTag(false);
		try {

			if (object.getBoolean("isSharedWithUser")) {
				Log.d("not favorite", "is shared, but not favorite, chnge favorite flag");
				object.put("isFavorite", false);
				object.saveInBackground();
			} else {
				// not shared and not favorite, so delete
				Log.d("not favorite", "not shared, no longer favorite, so delete");
				object.delete();
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/** updates view and parse when favorite */
	private void setToFavorite(ParseObject object) {
		Picasso.with(PreviewActivity.this).load(R.drawable.important)
			.into(favIcon);

		favIcon.setTag(true);

		// if object was null,make new object from app, else use object
		ParseObject o = null;
		if (object == null) {
			o = app.getParseObject();
			Log.d("set fav", "app id: " + app.getId());
			Log.d("set fav", "app id in object: " + o.getString("id"));
		} else {
			o = object;
		}

		// update parse for new favorite
		o.put(PreviewActivity.this.getResources().getString(
		R.string.parse_isFav), true);
		o.setACL(new ParseACL(ParseUser.getCurrentUser()));
		o.saveInBackground();
	}

	/** sets parse data to shared for given current app and user */
	private void shareAppWithUser(ParseUser parseUser) {
		Log.d("preview", "user = null?" + (parseUser == null));

		final ParseUser user = parseUser;

		ParseQuery < ParseObject > query = ParseQuery.getQuery(getResources()
			.getString(R.string.parseClass));
		query.whereEqualTo("objectId", app.getId());
		query.whereEqualTo("ACL", user.getACL());
		query.findInBackground(new FindCallback < ParseObject > () {

			@Override
			public void done(List < ParseObject > objects, ParseException e) {
				if (e == null) {
					ParseObject o = null;

					// no matches or not shared
					if (objects.size() == 0) {
						o = app.getParseObject();

					} else if (objects.get(0).getBoolean("isSharedWithUser") == false) {
						// set to shared
						o = objects.get(0);
					}

					if (o != null) {
						o.put("isSharedWithUser", true);
						o.setACL(new ParseACL(user));
						o.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								if (e == null) {
									Log.d("preiew",
										"sharing saved to parse succeeded");
									Toast.makeText(
									PreviewActivity.this,
										"Shared with " + user.getString("firstName") + " " + user.getString("lastName"),
									Toast.LENGTH_LONG).show();
								} else {
									Log.d("save", e.toString());
									Log.d("preview",
										"sharing saved to parse failed");
								}
							}
						});
					}

				} else {
					Log.d("Preview", "error in shared query");
				}
			}

		});

	}

	/** take list of ParseUsers, return arrayList of strings for user name */
	private String[] getUserStrings(List < ParseUser > users) {

		String[] stringArray = new String[users.size()];

		for (int i = 0; i < users.size(); i++) {
			stringArray[i] = users.get(i).getString("firstName") + " " + users.get(i).getString("lastName");
		}

		return stringArray;
	}

}