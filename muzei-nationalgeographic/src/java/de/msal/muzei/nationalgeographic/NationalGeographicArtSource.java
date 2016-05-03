/*
 * Copyright 2014 Maximilian Salomon.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package de.msal.muzei.nationalgeographic;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class NationalGeographicArtSource extends RemoteMuzeiArtSource {

   private static final String TAG = "NGforMuzei";
   private static final String SOURCE_NAME = "NationalGeographicSource";

   private static final int USER_COMMAND_ID_SHARE = 1;
   private static final int USER_COMMAND_ID_PHOTO_DESCRIPTION = 2;
   public static final String ACTION_NEW_SETTINGS = "de.msal.muzei.nationalgeographic.action.NEW_SETTINGS";
   public static final String EXTRA_SHOULD_REFRESH = "de.msal.muzei.nationalgeographic.extra.SHOULD_REFRESH";

   /* preferences */
   private SharedPreferences prefs;
   private boolean isRandom;
   private boolean isRefreshOnWifiOnly;

   public NationalGeographicArtSource() {
      super(SOURCE_NAME);
   }

   @Override
   public void onCreate() {
      super.onCreate();

      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      isRandom = prefs.getBoolean(getString(R.string.pref_cyclemode_key), true);
      isRefreshOnWifiOnly = prefs.getBoolean(getString(R.string.pref_wifiswitch_key), false);

      List<UserCommand> userCommands = new ArrayList<>(2);
      if (isRandom) {
         userCommands.add(new UserCommand(BUILTIN_COMMAND_ID_NEXT_ARTWORK));
      }
      userCommands.add(new UserCommand(USER_COMMAND_ID_SHARE, getString(R.string.share_artwork_title)));
      userCommands.add(new UserCommand(USER_COMMAND_ID_PHOTO_DESCRIPTION, getString(R.string.photo_desc_open)));
      setUserCommands(userCommands);
   }

   @Override
   protected void onCustomCommand(int id) {
      super.onCustomCommand(id);
      switch (id) {
         case USER_COMMAND_ID_PHOTO_DESCRIPTION:
            if (getCurrentArtwork().getToken().length() < 32) {
               new Handler(Looper.getMainLooper()).post(new Runnable() {
                  @Override
                  public void run() {
                     Toast.makeText(getApplicationContext(), R.string.photo_desc_nothing_to_show, Toast.LENGTH_SHORT).show();
                  }
               });
               scheduleUpdate(System.currentTimeMillis() + 500);
            } else {
               Intent intent = new Intent(this, PhotoDescriptionActivity.class);
               intent.putExtra(PhotoDescriptionActivity.EXTRA_TITLE, getCurrentArtwork().getTitle());
               String desc = getCurrentArtwork().getToken().substring(32, getCurrentArtwork().getToken().length());
               intent.putExtra(PhotoDescriptionActivity.EXTRA_DESC, desc);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
            }
            break;
         case USER_COMMAND_ID_SHARE:
            Artwork currentArtwork = getCurrentArtwork();

            if (currentArtwork == null) {
               new Handler(Looper.getMainLooper()).post(new Runnable() {
                  @Override
                  public void run() {
                     Toast.makeText(getApplicationContext(), R.string.share_artwork_nothing_to_share,
                           Toast.LENGTH_SHORT).show();
                  }
               });
            } else {
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_artwork_message,
                     currentArtwork.getTitle(),
                     currentArtwork.getByline(),
                     currentArtwork.getViewIntent().getDataString()));
               shareIntent = Intent.createChooser(shareIntent, getString(R.string.share_artwork_title));
               shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(shareIntent);
            }
            break;
      }
   }

   @Override
   protected void onHandleIntent(Intent intent) {
      if (intent != null && intent.getAction().equals(ACTION_NEW_SETTINGS) && intent.getBooleanExtra(EXTRA_SHOULD_REFRESH, true)) {
         scheduleUpdate(System.currentTimeMillis() + 500); // returned from prefs - update now!
      }
      super.onHandleIntent(intent);
   }

   @Override
   protected void onTryUpdate(int i) throws RetryException {
      if (isRefreshOnWifiOnly && !isConnectedWifi()) {
         throw new RetryException();
      }

      String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

      List<NationalGeographicService.Photo> photos;
      try {
         photos = NationalGeographicService.getAdapter().getPhotoOfTheDayFeed().execute().body().getPhotos();
      } catch (IOException e) {
         throw new RetryException(e);
      }

      if (photos == null) {
         throw new RetryException();
      }

      if (photos.size() == 0) {
         Log.e(TAG, "No feed returned from API.");
         throw new RetryException();
      }

      NationalGeographicService.Photo photo;
      if (isRandom) {
         Random random = new Random();
         String token;
         while (true) {
            photo = photos.get(random.nextInt(photos.size()));
            token = photo.pubDate + "|" + photo.description;
            if (photos.size() <= 1 || !TextUtils.equals(token, currentToken)) {
               break;
            }
         }
      } else {
         photo = photos.get(0);
         String token = photo.pubDate + "|" + photo.description;
           /* try again later if no new photo is online, yet */
         if (TextUtils.equals(token, currentToken)) {
            throw new RetryException();
         }
      }

      String dateString;
      try {
         // ignore timezone to avoid day-shifted result in timezones < GMT-4/GMT-5
         SimpleDateFormat orgFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
         SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

         Date formatted = orgFormat.parse(photo.pubDate);
         dateString = newFormat.format(formatted);
      } catch (ParseException e) {
         dateString = photo.pubDate;
      }

      publishArtwork(new Artwork.Builder()
            .title(photo.title)
            .byline(dateString)
            //replace e.g. '360x270.jpg' to '0x0.jpg', to get the highes resolution available
            .imageUri(Uri.parse(photo.enclosure.url.replaceAll("\\d+x\\d+\\.jpg$", "0x0.jpg")))
            .token(photo.pubDate + "|" + photo.description)
            .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(photo.link)))
            .build());

      scheduleNextUpdate();
   }

   private void scheduleNextUpdate() {
      if (isRandom) { /* update with the specified interval */
         int intervalTimeMilis = Integer.parseInt(
               prefs.getString(getString(R.string.pref_intervalpicker_key), getString(R.string.pref_intervalpicker_defaultvalue)))
               * 60    // sec
               * 1000; // msec
         scheduleUpdate(System.currentTimeMillis() + intervalTimeMilis);

      } else { /* update every day at 0:10 */
         //NG apperantly uses EST/EDT timezone (e.g. used in New York)
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
         // NG posts @00:00:00 each day
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 10); // add 10mins for latency
         cal.set(Calendar.SECOND, 0);
         cal.set(Calendar.MILLISECOND, 0);
         while (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
         }
         scheduleUpdate(cal.getTimeInMillis());
      }
   }

   private boolean isConnectedWifi() {
      ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cm.getActiveNetworkInfo();
      return (info != null
            && info.isConnected()
            && info.getType() == ConnectivityManager.TYPE_WIFI);
   }

}
