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

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoDescriptionActivity extends AppCompatActivity {

   public static final String EXTRA_TITLE = "de.msal.muzei.nationalgeographic.extra.TITLE";
   public static final String EXTRA_DESC = "de.msal.muzei.nationalgeographic.extra.DESC";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_photo_description);

      String title = getIntent().getStringExtra(EXTRA_TITLE);
      String desc = getIntent().getStringExtra(EXTRA_DESC);

      setTitle(title);

      TextView textViewDesc = findViewById(R.id.activityPhotoDescription_textView);
      textViewDesc.setFitsSystemWindows(true);
      textViewDesc.setMovementMethod(LinkMovementMethod.getInstance());
      textViewDesc.setText(fromHtml(desc));
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == android.R.id.home) {
         onBackPressed();
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   private static Spanned fromHtml(String html) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
      } else {
         //noinspection deprecation
         return Html.fromHtml(html);
      }
   }

}
