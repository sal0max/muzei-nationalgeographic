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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

public class SettingsActivity extends Activity {

   private SharedPreferences prefs;
   private boolean pref_cyclemode_start;
   private String pref_intervalpicker_start;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Display the fragment as the main content.
      getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new PrefsFragment())
            .commit();

      /* store the prefs values at the beginning to check in the end if a new update should be
         scheduled */
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      this.pref_cyclemode_start = prefs.getBoolean(getString(R.string.pref_cyclemode_key), true);
      this.pref_intervalpicker_start = prefs.getString(
            getString(R.string.pref_intervalpicker_key),
            getString(R.string.pref_intervalpicker_defaultvalue));
   }

   public static class PrefsFragment extends PreferenceFragment {

      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.preferences);

         /* show correct version name & copyright year */
         try {
            findPreference(getString(R.string.pref_about_key))
                  .setSummary(getString(R.string.pref_about_summary,
                        getActivity().getPackageManager()
                              .getPackageInfo(getActivity().getPackageName(), 0).versionName,
                        Calendar.getInstance().get(Calendar.YEAR)));
         } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
         }
      }

      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         view.setFitsSystemWindows(true);
      }
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   @Override
   public void onBackPressed() {
      boolean shouldRefresh = false;
      if (pref_cyclemode_start != prefs.getBoolean(getString(R.string.pref_cyclemode_key), true)
            || !(pref_intervalpicker_start.equals(prefs.getString(
            getString(R.string.pref_intervalpicker_key),
            getString(R.string.pref_intervalpicker_defaultvalue))))) {
         shouldRefresh = true;
      }

      startService(new Intent(SettingsActivity.this, NationalGeographicArtSource.class)
            .putExtra(NationalGeographicArtSource.EXTRA_SHOULD_REFRESH, shouldRefresh)
            .setAction(NationalGeographicArtSource.ACTION_NEW_SETTINGS));
      super.onBackPressed();
   }
}
