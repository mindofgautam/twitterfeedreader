package com.gautam.twitterTimeline;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class cursorAdapter extends CursorAdapter {

	public cursorAdapter(Context context, Cursor c) {
		super(context, c);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView gautamb = (TextView) view.findViewById(R.id.gautamb);
		TextView wannabegeekboy = (TextView) view
				.findViewById(R.id.wannabegeekboy);
		TextView tweet = (TextView) view.findViewById(R.id.thisIsTestTweet);
		ImageView img = (ImageView) view.findViewById(R.id.imageView1);
		String values = cursor.getString(3) + "";
		gautamb.setText(cursor.getString(1) + "");
		wannabegeekboy.setText(cursor.getString(2) + "");
		tweet.setText(cursor.getString(4));
		String value3 = cursor.getString(3) + "";
		img.setImageDrawable(ImageLoader.imgLoader(value3));

	}

	private Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.listrow, parent, false);
		bindView(v, context, cursor);
		return v;
	}
}