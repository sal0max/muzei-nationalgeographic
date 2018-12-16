package de.msal.muzei.nationalgeographic;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

class HttpLogger {

   static Interceptor getLogger() {
      HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
      httpLogger.setLevel(HttpLoggingInterceptor.Level.BASIC);
      return httpLogger;
   }

}
