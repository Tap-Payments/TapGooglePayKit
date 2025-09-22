# Keep Kotlin enum helpers
-keep class kotlin.enums.** { *; }

# Keep Kotlin Intrinsics (avoid runtime crashes)
-keep class kotlin.jvm.internal.** { *; }

# Keep Kotlin Metadata (needed for reflection + serialization)
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
