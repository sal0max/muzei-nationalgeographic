package de.msal.muzei.nationalgeographic

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NationalGeographicWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

   companion object {
      var isRandom = false

      internal fun enqueueLoad(random: Boolean) {
         this.isRandom = random
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
            // get a random photo of a month between October 2017 and now
            val now = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"))
            val randYear = getRand(2017, now.get(Calendar.YEAR))
            val randMonth =
                  if (randYear == now.get(Calendar.YEAR))
                     getRand(1, now.get(Calendar.MONTH) + 1).month()
                  else
                     getRand(if (randYear == 2017) 10 else 1, 12).month()
            NationalGeographicService.getPhotosOfTheDay(randYear, randMonth)?.random()
         } else {
            // get most recent photo
            NationalGeographicService.getPhotoOfTheDay()
         }
      } catch (e: IOException) {
         Log.w(javaClass.simpleName, "Error reading API", e)
         return Result.retry()
      }

      // check if successful
      if (photo == null) {
         Log.w(javaClass.simpleName, "No photo returned from API.")
         return Result.failure()
      } else if (photo.image?.url == null) {
         Log.w(javaClass.simpleName, "Photo url is null (photo id: ${photo.uuid}).")
         return Result.failure()
      }

      // success -> set Artwork
      val artwork = Artwork(
            title = photo.image.title, // title
            byline = photo.date?.let { SimpleDateFormat.getDateInstance().format(it.time)}, // date
            attribution = photo.credit, // photographer
            persistentUri = photo.image.url?.toUri(), // image url
            token = photo.caption, // caption as token, as it should normally be unique + the token is used to get the description in PhotoDescriptionActivity
            webUri = photo.date?.let {
               ("https://www.nationalgeographic.co.uk/photo-of-the-day/"
                     + photo.date?.get(Calendar.YEAR) // 2020
                     + "/"
                     + photo.date?.get(Calendar.MONTH)?.plus(1)?.month() // october
                     + "?image="
                     + photo.image.url?.substringAfter("/public/")?.substringBeforeLast(".")).toUri() // pod-14-03-2020-503080
            }
      )
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

   private fun Int?.month() : String? {
      return when {
         this == 1 -> "january"
         this == 2 -> "february"
         this == 3 -> "march"
         this == 4 -> "april"
         this == 5 -> "may"
         this == 6 -> "june"
         this == 7 -> "july"
         this == 8 -> "august"
         this == 9 -> "september"
         this == 10 -> "october"
         this == 11 -> "november"
         this == 12 -> "december"
         else -> null
      }
   }

}
