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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.MenuItem;
import android.view.View;

import com.google.android.apps.muzei.api.provider.ProviderContract;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

   private SharedPreferences prefs;
   private boolean currentMode;
   private boolean showLegacy;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Display the fragment as the main content.
      getSupportFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new PrefsFragment())
            .commit();

      /* store the prefs values at the beginning to check in the end if a new update should be
         scheduled */
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      this.currentMode = prefs.getBoolean(getString(R.string.pref_randomMode_key), true);
      this.showLegacy = prefs.getBoolean(getString(R.string.pref_showLegacy_key), true);
   }

   public static class PrefsFragment extends PreferenceFragmentCompat {

      @Override
      public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
         addPreferencesFromResource(R.xml.preferences);

         /* show correct version name & copyright year */
         try {
            //noinspection ConstantConditions
            findPreference(getString(R.string.pref_about_key)).setSummary(
                  getString(
                        R.string.pref_about_summary,
                        getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName,
                        Calendar.getInstance().get(Calendar.YEAR)
                  )
            );
         } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
         }
      }

      @Override
      public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         view.setFitsSystemWindows(true);
      }
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == android.R.id.home) {
         onBackPressed();
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onBackPressed() {
      if (currentMode != prefs.getBoolean(getString(R.string.pref_randomMode_key), true)
            || showLegacy != prefs.getBoolean(getString(R.string.pref_showLegacy_key), false)) {
         // switched mode (random/newest) -> delete all artwork, which also requests a new load
         Context context = getApplicationContext();
         Uri contentUri = ProviderContract.getProviderClient(context, NationalGeographicArtProvider.class).getContentUri();
         context.getContentResolver().delete(contentUri, null, null);
      }
      super.onBackPressed();
   }

}
