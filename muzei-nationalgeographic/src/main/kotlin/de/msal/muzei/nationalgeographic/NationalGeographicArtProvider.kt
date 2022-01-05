package de.msal.muzei.nationalgeographic

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.RemoteActionCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.preference.PreferenceManager
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider
import kotlin.random.Random

class NationalGeographicArtProvider : MuzeiArtProvider() {

   companion object {
      private const val USER_COMMAND_ID_SHARE = 2
   }

   override fun onLoadRequested(initial: Boolean) {
      val isRandom = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context?.getString(R.string.pref_randomMode_key), true)
      NationalGeographicWorker.enqueueLoad(isRandom)
   }

   /* This is the new API for Muzei 3.4+ that works on all API levels */
   override fun getCommandActions(artwork: Artwork): List<RemoteActionCompat> {
      val context = context ?: return super.getCommandActions(artwork)
      return listOf(
              createRemoteActionCompat(context, artwork,
                      ::createShareIntent, R.string.share_artwork_title))
   }

   /* kept for backward compatibility with Muzei 3.3 */
   @Suppress("OverridingDeprecatedMember", "DEPRECATION", "SameParameterValue")
   override fun getCommands(artwork: Artwork) = listOf(
           com.google.android.apps.muzei.api.UserCommand(USER_COMMAND_ID_SHARE,
                   context?.getString(R.string.share_artwork_title)))

   /* kept for backward compatibility with Muzei 3.3 */
   @Suppress("OverridingDeprecatedMember")
   override fun onCommand(artwork: Artwork, id: Int) {
      val context = context ?: return
      when (id) {
         USER_COMMAND_ID_SHARE -> {
            createShareIntent(context, artwork).apply {
               addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.takeIf { it.resolveActivity(context.packageManager) != null }?.run {
               context.startActivity(this)
            }
         }
      }
   }

   // user clicked on the title/byline/attribution: open a new activity with the image description
   override fun getArtworkInfo(artwork: Artwork): PendingIntent? {
      val context = context ?: return null
      val intent = createPhotoDescriptionIntent(context, artwork)
      return PendingIntent.getActivity(context, Random.nextInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
   }

   private fun createPhotoDescriptionIntent(
           context: Context,
           artwork: Artwork
   ) = Intent(context, PhotoDescriptionActivity::class.java).apply {
      putExtra(PhotoDescriptionActivity.EXTRA_TITLE, artwork. title)
      putExtra(PhotoDescriptionActivity.EXTRA_DESC, artwork.metadata)
   }

   private fun createShareIntent(
           context: Context,
           artwork: Artwork
   ): Intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_artwork_message,
              artwork.title,
              artwork.attribution ?: "?",
              artwork.persistentUri ?: ""))
   }, context.getString(R.string.share_artwork_title))

   /**
    * Construct a new [RemoteActionCompat] instance. Here, we use
    * [RemoteActionCompat.setShouldShowIcon] with `false` to continue to show
    * the icon in the overflow menu.
    *
    * If you wanted the icon to appear as an icon, that line should be removed
    * and a custom icon should be added in place of `R.drawable.muzei_launch_command`.
    */
   @Suppress("SameParameterValue")
   private fun createRemoteActionCompat(
           context: Context,
           artwork: Artwork,
           intentProvider: (context: Context, artwork: Artwork) -> Intent,
           @StringRes titleRes: Int
   ) = RemoteActionCompat(
           IconCompat.createWithResource(context, R.drawable.muzei_launch_command),
           context.getString(titleRes),
           context.getString(titleRes),
           PendingIntent.getActivity(context, artwork.id.toInt(),
                   intentProvider.invoke(context, artwork), 0)).apply {
      setShouldShowIcon(false)
   }

}
