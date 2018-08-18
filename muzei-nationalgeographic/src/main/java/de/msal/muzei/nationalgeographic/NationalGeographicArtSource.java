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
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import de.msal.muzei.nationalgeographic.model.Feed;
import de.msal.muzei.nationalgeographic.model.Item;

public class NationalGeographicArtSource extends RemoteMuzeiArtSource {

   private static final String SOURCE_NAME = "NationalGeographicSource";

   private static final int USER_COMMAND_ID_SHARE = 1;
   private static final int USER_COMMAND_ID_PHOTO_DESCRIPTION = 2;
   private static final int USER_COMMAND_ID_UPDATE_NOW = 3;

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
      if (!isRandom)
         userCommands.add(new UserCommand(USER_COMMAND_ID_UPDATE_NOW, getString(R.string.photo_reload)));
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
               intent.putExtra(PhotoDescriptionActivity.EXTRA_DESC, getCurrentArtwork().getToken());
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
         case USER_COMMAND_ID_UPDATE_NOW:
            // Don't use scheduleUpdate(System.currentTimeMillis()) here! Will cause an infinite loop.
            try {
               onTryUpdate(UPDATE_REASON_USER_NEXT);
            } catch (RetryException ignored) {
               scheduleNextUpdate();
            }
            break;
      }
   }

   @Override
   protected void onHandleIntent(Intent intent) {
      if (intent != null
              && intent.getAction() != null
              && intent.getAction().equals(ACTION_NEW_SETTINGS) && intent.getBooleanExtra(EXTRA_SHOULD_REFRESH, true)) {
         scheduleUpdate(System.currentTimeMillis() + 100); // returned from prefs - update now!
      }
      super.onHandleIntent(intent);
   }

   @Override
   protected void onTryUpdate(int i) throws RetryException {
      if (isRefreshOnWifiOnly && !isConnectedWifi()) {
         throw new RetryException();
      }

      String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

      List<Item> photos;
      try {
         NationalGeographicService.Service service = NationalGeographicService.getAdapter();
         if (isRandom) {
            // get the all the photos of a month between January 2011 and now
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            int randYear = getRand(2011, cal.get(Calendar.YEAR));
            int randMonth = randYear == cal.get(Calendar.YEAR) ? getRand(1, cal.get(Calendar.MONTH) + 1) : getRand(1, 12);
            Feed body = service.getPhotoOfTheDayFeed(randYear, randMonth).execute().body();
            if (body == null)
               throw new RetryException();
            else
               photos = body.getItems();
         } else {
            // get the all the photos of the current month
            Feed body = service.getPhotoOfTheDayFeed().execute().body();
            if (body == null)
               throw new RetryException();
            else
               photos = body.getItems();
         }
      } catch (IOException e) {
         throw new RetryException(e);
      }

      if (photos == null || photos.size() == 0) {
         throw new RetryException();
      }

      Item photo;
      if (isRandom) {
         Random random = new Random();
         String token;
         while (true) {
            photo = photos.get(random.nextInt(photos.size()));
            token = photo.getCaption();
            if (photos.size() <= 1 || !TextUtils.equals(token, currentToken)) {
               break;
            }
         }
      } else {
         photo = photos.get(0);
         String token = photo.getCaption();
         /* try again later if no new photo is online, yet */
         if (TextUtils.equals(token, currentToken)) {
            throw new RetryException();
         }
      }

      publishArtwork(new Artwork.Builder()
            .title(photo.getTitle())
            .byline(photo.getPublishDate())
            .imageUri(Uri.parse(photo.getSizes() != null ? photo.getSizes().get2048() : photo.getUrl()))
            .token(photo.getCaption())
            .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(photo.getPageUrl())))
            .build());

      scheduleNextUpdate();
   }

   private void scheduleNextUpdate() {
      if (isRandom) { /* update with the specified interval */
         int intervalTimeMillis = Integer.parseInt(
               prefs.getString(getString(R.string.pref_intervalpicker_key), getString(R.string.pref_intervalpicker_defaultvalue)))
               * 60    // sec
               * 1000; // millis
         scheduleUpdate(System.currentTimeMillis() + intervalTimeMillis);

      } else { /* update every day at 0:10 */
         //NG apparently uses EST/EDT timezone (e.g. used in New York)
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
         // NG posts @00:00:00 each day
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 10); // add 10min for latency
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
      return (cm != null
              && cm.getActiveNetworkInfo() != null
              && cm.getActiveNetworkInfo().isConnected()
              && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
   }

   /**
    * @param min inclusive
    * @param max inclusive
    * @return the random number
    */
   private int getRand(int min, int max) {
      Random rand = new Random();
      return rand.nextInt((max - min) + 1) + min;
   }

}
