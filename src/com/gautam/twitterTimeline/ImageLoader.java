package com.gautam.twitterTimeline;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.drawable.Drawable;

class ImageLoader {
	private static HashMap<String, Drawable> imagesMap = new HashMap<String, Drawable>();

	public static Drawable imgLoader(final String url) {

		return imageLoader(url);

	}

	private static Drawable imageLoader(String url) {
		Collection<Drawable> c = imagesMap.values();
		Iterator<Drawable> itr = c.iterator();
		if (imagesMap.containsKey(url)) {
			return (Drawable) itr.next();
		} else {
			Drawable d = LoadImageFromWeb(url);
			imagesMap.put(url, d);
			return d;
		}
	}

	private static Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}
}
