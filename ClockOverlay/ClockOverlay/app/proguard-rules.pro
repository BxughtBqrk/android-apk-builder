# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep custom view
-keep public class com.cybernetyx.eyerisboard.clockoverlay.ClockView { *; }

# Keep service
-keep public class com.cybernetyx.eyerisboard.clockoverlay.ClockOverlayService { *; }
