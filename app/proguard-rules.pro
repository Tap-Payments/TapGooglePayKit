# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep Kotlin enum helpers
-keep class kotlin.enums.** { *; }

# Keep Kotlin Intrinsics (avoid runtime crashes)
-keep class kotlin.jvm.internal.** { *; }

# Keep Kotlin Metadata (needed for reflection + serialization)
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
# Keep Kotlin runtime (especially enums)
-keep class kotlin.** { *; }
-keep class kotlin.enums.** { *; }
-keep class kotlin.jvm.** { *; }
-dontwarn kotlin.**
-keep enum company.tap.google.pay.open.enums.AllowedMethods
-keep enum company.tap.google.pay.open.enums.GooglePayButtonType
-keep enum company.tap.google.pay.open.enums.SDKMode

# ============================
# Retrofit
# ============================
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep Retrofit method annotations (e.g., @GET, @POST, etc.)
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# ============================
# OkHttp
# ============================
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# OkHttp internal (avoid warnings)
-dontwarn javax.annotation.**

# ============================
# Gson
# ============================
-keep class com.google.gson.** { *; }
-keep class com.google.gson.annotations.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Keep fields marked with @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Prevent R8 from stripping interfaces used for custom TypeAdapters
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ============================
# General serialization safety
# ============================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object readResolve();
}
