package de.msal.muzei.nationalgeographic

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import java.io.IOException
import java.util.*

class NationalGeographicWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

   companion object {
      var isRandom = false
      var shouldShowLegacy = false

      internal fun enqueueLoad(random: Boolean, shouldShowLegacy: Boolean) {
         this.isRandom = random
         this.shouldShowLegacy = shouldShowLegacy
         WorkManager
               .getInstance()
               .enqueue(OneTimeWorkRequestBuilder<NationalGeographicWorker>()
                     .setConstraints(Constraints.Builder()
                           .setRequiredNetworkType(NetworkType.CONNECTED)
                           .build())
                     .build())
      }
   }

   override fun doWork(): Result {
      // fetch photo
      val photo = try {
         if (isRandom) {
            // get a random photo of a month between January 2011 and now
            val cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))
            val randYear = getRand(if (shouldShowLegacy) 2011 else 2016, cal.get(Calendar.YEAR))
            val randMonth =
                  if (randYear == cal.get(Calendar.YEAR))
                     getRand(1, cal.get(Calendar.MONTH) + 1)
                  else
                     getRand(if (!shouldShowLegacy && randYear == 2016) 9 else 1, 12)
            NationalGeographicService.getPhotosOfTheDay(randYear, randMonth)?.random()
         } else {
            // get most recent photo
            NationalGeographicService.getPhotosOfTheDay()?.first()
         }
      } catch (e: IOException) {
         Log.w(javaClass.simpleName, "Error reading API", e)
         return Result.retry()
      }

      // check if successful
      if (photo == null) {
         Log.w(javaClass.simpleName, "No photo returned from API.")
         return Result.failure()
      } else if (photo.image?.renditions?.last()?.uri == null) {
         Log.w(javaClass.simpleName, "Photo url is null (${photo.publishDate}).")
         return Result.failure()
      }

      // success -> set Artwork
      val artwork = Artwork(
         title = photo.image.title,
         byline = photo.publishDate,
         attribution = photo.image.credit,
         persistentUri = photo.image.renditions.last().uri.toUri(),
         token = photo.image.caption ?: photo.image.renditions.last().uri,
         webUri = photo.pageUrl?.toUri())
      val providerClient = ProviderContract.getProviderClient<NationalGeographicArtProvider>(applicationContext)
      if (isRandom) {
         providerClient.addArtwork(artwork)
      } else {
         providerClient.setArtwork(artwork)
      }
      return Result.success()
   }

   /**
    * @param min inclusive
    * @param max inclusive
    * @return the random number
    */
   private fun getRand(min: Int, max: Int): Int {
      val rand = Random()
      return rand.nextInt(max - min + 1) + min
   }

}
