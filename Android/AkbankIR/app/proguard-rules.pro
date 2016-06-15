# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/oguzemreozcan/Library/Developer/Xamarin/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn org.joda.**
-dontwarn org.codehaus.mojo.**
-dontwarn de.hdodenhof.circleimageview
#-dontwarn org.apache.http.**
-dontwarn com.google.android.gms.**
-dontwarn com.github.citux.datetimepicker.**
-dontwarn java.nio.file.**
-dontwarn rx.**

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes *Annotation* # Needed by google-api-client and Otto
-keepattributes Signature # Needed by google-api-client and Retrofit

# Needed by google-api-client to keep generic types and @Key annotations accessed via reflection
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# Needed by Guava (google-api-client)
-dontwarn sun.misc.Unsafe

#Otto
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

#Picasso
-dontwarn com.squareup.okhttp.**

-keep interface com.squareup.okhttp.**{*;}
-keep class com.squareup.okhttp.**{*;}

#Calligraphy
-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

#Jodatime
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

#GraphView
-keep class com.jjoe64.graphview.** { *; }


  -keepnames class * implements android.os.Parcelable {
      public static final ** CREATOR;
  }

  #Citux DateTimePicker
  -keep class com.github.citux.**{*;}

  ##---------------Begin: proguard configuration for Gson  ----------
  # Gson uses generic type information stored in a class file when working with fields. Proguard
  # removes such information by default, so configure it to keep all of it.
  -keepattributes Signature

  # For using GSON @Expose annotation
  -keepattributes *Annotation*

  # Gson specific classes
  -keep class sun.misc.Unsafe { *; }
  #-keep class com.google.gson.stream.** { *; }

  # Application classes that will be serialized/deserialized over Gson
  -keep class com.google.gson.examples.android.model.** { *; }

  ##---------------End: proguard configuration for Gson  ----------
