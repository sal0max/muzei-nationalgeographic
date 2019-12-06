package de.msal.muzei.nationalgeographic

import android.content.Intent
import androidx.preference.PreferenceManager
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

   /*
    * Not working properly in Android 10. See:
    * - https://github.com/romannurik/muzei/issues/644
    * - https://developer.android.com/guide/components/activities/background-starts
    */
   override fun onCommand(artwork: Artwork, id: Int) {
      val context = context ?: return
      when (id) {
         USER_COMMAND_ID_PHOTO_DESCRIPTION -> {
            Intent(context, PhotoDescriptionActivity::class.java).apply {
               putExtra(PhotoDescriptionActivity.EXTRA_TITLE, artwork. title)
               putExtra(PhotoDescriptionActivity.EXTRA_DESC, artwork.token)
               flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }.takeIf { it.resolveActivity(context.packageManager) != null }?.run {
               context.startActivity(this)
            }
         }
         USER_COMMAND_ID_SHARE -> {
            Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
               type = "text/plain"
               putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_artwork_message,
                     artwork.title,
                     artwork.byline,
                     artwork.webUri))
            }, context.getString(R.string.share_artwork_title))?.apply {
               addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }?.takeIf { it.resolveActivity(context.packageManager) != null }?.run {
               context.startActivity(this)
            }
         }
      }
   }

}
