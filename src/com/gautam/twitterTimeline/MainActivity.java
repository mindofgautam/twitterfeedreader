package com.gautam.twitterTimeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GAUTAM
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	private TextView mTimeLineView;
	private TextView mSearchView;
	private EditText searchText;
	private SQLiteAdapter mDatabase;
	private String TAG = "MAIN ACTIVITY";
	private boolean flag = true;
	private ListView mListView;
	String searchJonUrl;
	Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
		listener();
		autoRefresh();

	}

	private void autoRefresh() {

	}

	AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {

		protected void onPostExecute(Void result) {

		};

		@Override
		protected Void doInBackground(Void... params) {

			return null;
		}
	};

	private void createDatabase() {
		cursor = mDatabase.queueAll();
		try {

			JSONArray jsonArray = new JSONArray(readTwitterFeed());

			Log.v("Total = ", "Number of entries " + jsonArray.length());

			for (int i = jsonArray.length() - 1; i >= 0; i--) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				String value1 = jsonObject.getJSONObject("user").getString(
						"screen_name");

				String value2 = jsonObject.getJSONObject("user").getString(
						"name");

				String value3 = jsonObject.getJSONObject("user").getString(
						"profile_image_url_https");
				String value4 = jsonObject.getString("text");
				String value5 = jsonObject.getString("created_at");
				mDatabase.insert(value1, value2, value3, value4, value5);
				ImageLoader.imgLoader(value3);
			}
		} catch (Exception ee) {
		}

	}

	/**
	 * Adding listerns to the view
	 */
	private void listener() {
		mTimeLineView.setOnClickListener(this);
		mSearchView.setOnClickListener(this);
	}

	/**
	 * Initializing the Views
	 */

	private void initialize() {

		mTimeLineView = (TextView) findViewById(R.id.textView1);
		mSearchView = (TextView) findViewById(R.id.textView2);
		mListView = (ListView) findViewById(R.id.listView1);
		searchText = (EditText) findViewById(R.id.editText1);
		mDatabase = new SQLiteAdapter((Activity) MainActivity.this);
		mDatabase.openToWrite();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mTimeLineView.getId()) {
			flag = true;
			timeLine();
		} else if (v.getId() == mSearchView.getId()) {
			flag = false;
			if (searchText.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "Enter the values",
						Toast.LENGTH_SHORT).show();
			} else {
				searchJonUrl = "http://twitter.com/statuses/user_timeline/9to5mac.json"
						// +
						// searchText.getText().toString().toLowerCase().trim()
						+ ".json";
				mDatabase.close();
				Log.v(TAG, "searching for "
						+ searchText.getText().toString().toLowerCase().trim());
				// timeLine();
			}
		}
	}

	/**
	 * Search hash tag Function
	 */

	/**
	 * Timeline Funtion
	 */
	private void timeLine() {
		Log.v(TAG, "Clicked TimeLine");
		asyncTaskClass async = null;
		async = new asyncTaskClass();
		async.execute();
	}

	public String readTwitterFeed() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;
		if (flag == true)
			httpGet = new HttpGet(
					"http://twitter.com/statuses/user_timeline/wannabegeekboy.json");
		else {
			httpGet = new HttpGet(searchJonUrl);
			Log.v(TAG, "search URL" + searchJonUrl);
		}
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.v(TAG, "Resonse Code" + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(TAG, "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	class asyncTaskClass extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {

			cursor = mDatabase.queueAll();
			cursor.moveToLast();
			cursorAdapter corsoradapter = new cursorAdapter(MainActivity.this,
					cursor);
			mListView.setAdapter(corsoradapter);
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			createDatabase();

			return null;
		}
	}
}
