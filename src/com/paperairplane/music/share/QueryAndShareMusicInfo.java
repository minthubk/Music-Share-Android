package com.paperairplane.music.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class QueryAndShareMusicInfo extends Thread {
	final private int MUSIC = 0, ARTWORK = 1, ARTIST = 2, ALBUM = 3,
			VERSION = 4;
	final private int SEND_WEIBO = 4;
	final private int WEIBO = 0, OTHERS = 1;
	private int means;
	private String artist, title, album;
	private Context context;
	private Handler handler;

	public void run() {
		String[] info = Utilities.getMusicAndArtworkUrl(title, artist, context,
				handler);
		String content;
		content = genContent(info);

		String artworkUrl = null;
		if (info[ARTWORK] != null) {
			artworkUrl = info[ARTWORK].replace("spic", "lpic");
		}
		Bundle bundle = new Bundle();
		bundle.putString("content", content);
		bundle.putString("artworkUrl", artworkUrl);
		switch (means) {
		case OTHERS:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT,
					context.getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_TEXT, content);
			context.startActivity(Intent.createChooser(intent,
					context.getString(R.string.how_to_share)).addFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK));
			break;
		case WEIBO:
			Message m = handler.obtainMessage(SEND_WEIBO, bundle);
			handler.sendMessage(m);
			break;
		}

	}

	private String genContent(String[] info) {
		boolean isSingle = ((info[VERSION] != null) && info[VERSION]
				.equals(context.getString(R.string.single)));
		String content = context.getString(R.string.share_by)
				+ " "
				+ ((artist.equals("")) ? info[ARTIST] : artist)
				+ " "
				+ (isSingle ? context.getString(R.string.music_single)
						: context.getString(R.string.music_artist))
				+ " "
				+ title
				+ " "
				+ (isSingle ? "" : context.getString(R.string.music_album)
						+ " " + ((album.equals("")) ? info[ALBUM] : album)
						+ " ") + context.getString(R.string.before_url)
				+ info[MUSIC] + " ";
		return content;
	}

	public QueryAndShareMusicInfo(String _title, String _artist, String _album,
			int _means, Context _context, Handler _handler) {
		title = _title;
		artist = _artist;
		album = _album;
		means = _means;
		context = _context;
		handler = _handler;
	}

}
