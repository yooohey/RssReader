package sample.application.rssreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ItemDetailActivity extends Activity {
	private TextView mTitle;
	private TextView mDescr;
	//private String mLink;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail);

		Intent intent = getIntent();

		String title = intent.getStringExtra("TITLE");
		mTitle = (TextView) findViewById(R.id.textView1);
		mTitle.setText(title);
		String descr = intent.getStringExtra("DESCRIPTION");
		mDescr = (TextView) findViewById(R.id.textView2);
		mDescr.setText(descr);
		//this.mLink = intent.getStringExtra("LINK");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String browser = getResources().getString(R.string.browser);
			menu.add(browser);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//String suburl = MainActivity.strUrl.substring(0,MainActivity.strUrl.lastIndexOf("."));
		Uri uri = Uri.parse(MainActivity.strLink);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);

		return super.onOptionsItemSelected(item);
	}


}