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



# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# ==========================================
# UI & ARCHITECTURE COMPONENTS
# ==========================================

# AndroidX Fragment / Activity result APIs
-keepclassmembers class * extends androidx.fragment.app.Fragment {
    public <init>(...);
}
-keepclassmembers class * extends androidx.appcompat.app.AppCompatActivity {
    public <init>(...);
}

# Navigation Safe Args
-keep class **Args { *; }
-keep class **Directions { *; }

# Parcelize
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# ==========================================
# MEDIA & CAMERA
# ==========================================

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# CameraX
-keep class androidx.camera.core.impl.** { *; }
-keep class * implements androidx.camera.core.impl.Config { *; }
-keep class * extends androidx.camera.core.impl.Config$Option { *; }
-keep class androidx.camera.camera2.impl.** { *; }

# ==========================================
# NETWORKING & API PARSING (CRITICAL)
# ==========================================

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class retrofit2.converter.gson.** { *; }

# Gson
-dontwarn sun.misc.Unsafe
-keep class com.google.gson.** { *; }

# 🚨 CRITICAL: Keep your API response Data Classes safe from obfuscation!
# (Make sure this path perfectly matches where your data classes are stored)
-keep class com.futurecode.scarymonstercallchat.model.** { *; }

# ==========================================
# ADS & MONETIZATION
# ==========================================

# Google AdMob Core
-keep public class com.google.android.gms.ads.** {
   public *;
}

# Facebook Audience Network & Mediation
-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe
-keep class com.facebook.ads.** { *; }
-keep class com.google.ads.mediation.facebook.** { *; }

# Google Play Billing
-keep class com.android.billingclient.** { *; }

# ==========================================
# UTILITIES
# ==========================================

# Guava (Prevents missing ClassValue warnings during build)
-dontwarn com.google.common.**
-dontwarn java.lang.ClassValue



# Gson specific rules to safeguard reflection sweeps
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Explicitly keep your notification data structure package safe
-keep class com.futurecode.scarymonstercallchat.notification.NotificationModel { *; }


