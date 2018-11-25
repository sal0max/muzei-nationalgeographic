package de.msal.muzei.nationalgeographic

import android.content.Intent
import android.preference.PreferenceManager
import com.google.android.apps.muzei.api.UserCommand
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider


class NationalGeographicArtProvider : MuzeiArtProvider() {

   companion object {
      private const val USER_COMMAND_ID_PHOTO_DESCRIPTION = 1
      private const val USER_COMMAND_ID_SHARE = 2
   }

   override fun onLoadRequested(initial: Boolean) {
      val isRandom = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context?.getString(R.string.pref_randomMode_key), true)
      val shouldShowLegacy = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context?.getString(R.string.pref_showLegacy_key), false)
      NationalGeographicWorker.enqueueLoad(isRandom, shouldShowLegacy)
   }

   override fun getCommands(artwork: Artwork) = listOf(
         UserCommand(USER_COMMAND_ID_PHOTO_DESCRIPTION, context?.getString(R.string.photo_desc_open)),
         UserCommand(USER_COMMAND_ID_SHARE, context?.getString(R.string.share_artwork_title)))

   override fun onCommand(artwork: Artwork, id: Int) {
      when (id) {
         USER_COMMAND_ID_PHOTO_DESCRIPTION -> {
            val intent = Intent(context, PhotoDescriptionActivity::class.java)
            intent.putExtra(PhotoDescriptionActivity.EXTRA_TITLE, artwork.title)
            intent.putExtra(PhotoDescriptionActivity.EXTRA_DESC, artwork.token)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
         }
         USER_COMMAND_ID_SHARE -> {
            var shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, context?.getString(R.string.share_artwork_message,
                  artwork.title,
                  artwork.byline,
                  artwork.webUri))
            shareIntent = Intent.createChooser(shareIntent, context?.getString(R.string.share_artwork_title))
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(shareIntent)
         }
      }
   }

}
