package de.msal.muzei.nationalgeographic

import com.github.sisyphsu.dateparser.DateParserUtils
import org.junit.Assert
import org.junit.Test
import java.util.*

class DateParserTest {

   init {
      DateParserUtils.preferMonthFirst(false)
      // allow date fields be separated by dash
      DateParserUtils.registerStandardRule("(?<dayOrMonth>\\d{1,2}-\\d{1,2})-(?<year>\\d{2})$")
   }

   @Test
   fun testDateParser() {
      Assert.assertEquals(
            GregorianCalendar(2017, Calendar.OCTOBER, 2),
            DateParserUtils.parseCalendar("2-10-17"))
      Assert.assertEquals(
            GregorianCalendar(2017, Calendar.DECEMBER, 4),
            DateParserUtils.parseCalendar("4/12/17"))
      Assert.assertEquals(
            GregorianCalendar(2018, Calendar.JUNE, 30),
            DateParserUtils.parseCalendar("2018-06-30"))
      Assert.assertEquals(
            GregorianCalendar(2018, Calendar.MARCH, 31),
            DateParserUtils.parseCalendar("31.03.18"))
      Assert.assertEquals(
            GregorianCalendar(2020, Calendar.OCTOBER, 30),
            DateParserUtils.parseCalendar("30-10-2020"))
      Assert.assertEquals(
            GregorianCalendar(0, Calendar.NOVEMBER, 30),
            DateParserUtils.parseCalendar("30 Nov"))
      Assert.assertEquals(
            GregorianCalendar(0, Calendar.JANUARY, 31),
            DateParserUtils.parseCalendar("Jan 31"))
   }
}
