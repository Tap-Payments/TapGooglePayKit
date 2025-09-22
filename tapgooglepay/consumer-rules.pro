# Keep Kotlin enum helpers
-keep class kotlin.enums.** { *; }

# Keep Kotlin Intrinsics (avoid runtime crashes)
-keep class kotlin.jvm.internal.** { *; }

# Keep Kotlin Metadata (needed for reflection + serialization)
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
-keep enum company.tap.google.pay.open.enums.AllowedMethods
-keep enum company.tap.google.pay.open.enums.GooglePayButtonType
-keep enum company.tap.google.pay.open.enums.SDKMode

# Keep Kotlin runtime (especially enums)
-keep class kotlin.** { *; }
-keep class kotlin.enums.** { *; }
-keep class kotlin.jvm.** { *; }
-dontwarn kotlin.**
