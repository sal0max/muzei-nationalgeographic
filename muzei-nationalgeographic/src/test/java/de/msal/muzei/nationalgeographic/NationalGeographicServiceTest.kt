package de.msal.muzei.nationalgeographic

import org.junit.Test
import org.junit.Assert.*

class NationalGeographicServiceTest {

   @Test
   fun testWebservice() {
      // check current
      val currentItem = NationalGeographicService.getPhotoOfTheDay()
      assertNotNull(currentItem.uuid)

      // check march 2020
      val items2020 = NationalGeographicService.getPhotosOfTheDay(2020, "march")
      assertEquals(30, items2020.size)
      assertEquals("Snow Forever", items2020[0].image?.title)
      assertEquals("https://static.nationalgeographic.co.uk/files/styles/image_3200/public/pod-31-03-2020-617993.webp", items2020[0].image?.url)

      // check october 2017
      val items2017 = NationalGeographicService.getPhotosOfTheDay(2017, "october")
      assertEquals(17, items2017.size)
      assertEquals("The Lion Sleeps", items2017[0].image?.title)
      assertEquals("https://static.nationalgeographic.co.uk/files/styles/image_3200/public/fileYo7QhY", items2017[0].image?.url)
   }
}
