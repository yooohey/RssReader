package sample.application.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.ListView;

//RssParserTask.java
public class RssParserTask extends AsyncTask<String, Integer, RssListAdapter> {

	private MainActivity mActivity;
	private RssListAdapter mAdapter;
	private ProgressDialog mProgressDialog;
	private ListView mListView;

	// コンストラクタ
	public RssParserTask(MainActivity activity, RssListAdapter adapter,
			ListView _listView) {
		mActivity = activity;
		mListView = _listView;
		mAdapter = adapter;
	}

	// タスクを実行した直後にコールされる
	@Override
	protected void onPreExecute() {
		// プログレスバーを表示する
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Now Loading...");
		mProgressDialog.show();
	}

	// バックグラウンドにおける処理を担う。タスク実行時に渡された値を引数とする
	@Override
	protected RssListAdapter doInBackground(String... params) {
		RssListAdapter result = null;
		try {
			// HTTP経由でアクセスし、InputStreamを取得する
			URL url = new URL(params[0]);
			InputStream is = url.openConnection().getInputStream();
			result = parseXml(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ここで返した値は、onPostExecuteメソッドの引数として渡される
		return result;
	}

	// メインスレッド上で実行される
	@Override
	protected void onPostExecute(RssListAdapter result) {
		mProgressDialog.dismiss();
		mListView.setAdapter(result);
	}

	// XMLをパースする
	public RssListAdapter parseXml(InputStream is) throws IOException,
			XmlPullParserException {
		// パーサーを作成する。
		XmlPullParser parser = Xml.newPullParser();
		try {
			// パーサーにinputStraemをセットする。これはURLを読み込んでいるStream
			parser.setInput(is, null);

			// 読み込みの進捗をここから取得ができる。（値はSTARTなどが存在する。）
			int eventType = parser.getEventType();
			Item currentItem = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = null;
				switch (eventType) {
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("item")) {
						currentItem = new Item();
					} else if (currentItem != null) {
						if (tag.equals("title")) {
							// タイトルタグの中身の文字の事
							currentItem.setTitle(parser.nextText());
						} else if (tag.equals("description")) {
							// 説明タグの中身の文字の事
							currentItem.setDescription(parser.nextText());
						}else if (tag.equals("link")){
							currentItem.setLink(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("item")) {
						mAdapter.add(currentItem);
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mAdapter;
	}
}

/*

else if (tag.equals("pubDate")){
	currentItem.setPubdate(parser.nextText());
}
*/