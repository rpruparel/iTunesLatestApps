/* FullName: Rohit Pankaj Ruparel*/

package com.example.hw5;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AppAdapter extends ArrayAdapter < App > {
	Context context;
	ArrayList < App > objects;
	//DataManager dm;

	public AppAdapter(Context context, ArrayList < App > objects) {
		super(context, R.layout.app_item_layout, objects);
		this.context = context;
		this.objects = objects;
		//this.dm = dm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.app_item_layout, parent, false);
			holder = new ViewHolder();
			holder.textView1 = (TextView) convertView.findViewById(R.id.textViewAppTitle);
			holder.textView2 = (TextView) convertView.findViewById(R.id.textViewDevName);
			holder.textView3 = (TextView) convertView.findViewById(R.id.textViewReleaseDate);
			holder.textView4 = (TextView) convertView.findViewById(R.id.textViewPrice);
			holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		App app = objects.get(position);

		holder.textView1.setText(app.getTitle());
		holder.textView2.setText(app.getDeveloperName());
		holder.textView3.setText(app.getReleaseDate());
		holder.textView4.setText((app.getAppPrice()).substring(0, 4));

		Picasso.with(context).load(app.getSmallPhoto()).into(holder.imageView1);

		return convertView;
	}

	static class ViewHolder {
		TextView textView1;
		TextView textView2;
		TextView textView3;
		TextView textView4;
		ImageView imageView1;
	}
}