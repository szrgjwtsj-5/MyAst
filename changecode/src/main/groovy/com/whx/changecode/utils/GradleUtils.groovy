package com.whx.changecode.utils

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

import java.lang.reflect.Field

class GradleUtils {

    static final String sPluginMisConfiguredErrorMessage =
            "Plugin requires the 'android' or 'android-library' plugin to be configured."

    /**
     * get android variant list of the project
     * @param project the compiling project
     * @return android variants
     */
    static DomainObjectCollection<BaseVariant> getAndroidVariants(Project project) {
        if (project.getPlugins().hasPlugin(AppPlugin)) {
            return (DomainObjectCollection<BaseVariant>) ((AppExtension) ((AppPlugin) project.getPlugins().getPlugin(AppPlugin)).extension).applicationVariants
        } else if (project.getPlugins().hasPlugin(LibraryPlugin)) {
            return (DomainObjectCollection<BaseVariant>) ((LibraryExtension) ((LibraryPlugin) project.getPlugins().getPlugin(LibraryPlugin)).extension).libraryVariants
        } else {
            throw new ProjectConfigurationException(sPluginMisConfiguredErrorMessage, null)
        }
    }

    static String getJavaCompileTaskName(Project project, BaseVariant variant) {
        if (isGradle140orAbove(project)) {
            return "compile${variant.name.capitalize()}JavaWithJavac"
        } else {
            return variant.javaCompiler.name
        }
    }

    static boolean isGradle140orAbove(Project project) {
        Class<?> versionClazz = null
        try {
            versionClazz = Class.forName("com.android.builder.Version")
        } catch (Exception e) {
        }
        if (versionClazz == null) {
            return false
        } else {
            Field pluginVersionField = versionClazz.getField("ANDROID_GRADLE_PLUGIN_VERSION")
            pluginVersionField.setAccessible(true)
            String version = pluginVersionField.get(null)

            return versionCompare(version, "1.4.0") >= 0
        }
        //return versionCompare(com.android.builder.Version.ANDROID_GRADLE_PLUGIN_VERSION, "1.4.0") >= 0;
    }
    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    private static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("-")[0].split("\\.")
        String[] vals2 = str2.split("-")[0].split("\\.")
        int i = 0
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i] == vals2[i]) {
            i++
        }

        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]) <=> Integer.valueOf(vals2[i])
            return Integer.signum(diff)
        }

        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length)
        }
    }
}