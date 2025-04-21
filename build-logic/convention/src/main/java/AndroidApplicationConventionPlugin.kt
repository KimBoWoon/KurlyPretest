import com.android.build.api.dsl.ApplicationExtension
import com.bowoon.convention.Config
import com.bowoon.convention.Config.getProp
import com.bowoon.convention.KurlyPretestAppBuildType
import com.bowoon.convention.configureKotlinAndroid
import com.bowoon.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    compileSdk = Config.Application.KurlyPretest.compileSdkVersion
                    minSdk = Config.Application.KurlyPretest.minSdkVersion
                    testInstrumentationRunner = Config.ApplicationSetting.testInstrumentationRunner
                    extensions.configure<ApplicationExtension> {
                        applicationId = Config.Application.KurlyPretest.applicationId
                        targetSdk = Config.Application.KurlyPretest.targetSdkVersion
                        versionName = Config.Application.KurlyPretest.versionName
                        versionCode = Config.Application.KurlyPretest.versionCode
                        testInstrumentationRunner = Config.ApplicationSetting.testInstrumentationRunner

                        signingConfigs {
                            register(Config.Application.KurlyPretest.Sign.Release.name) {
                                storeFile = file(getProp(Config.Application.KurlyPretest.Sign.Release.storeFile))
                                storePassword = getProp(Config.Application.KurlyPretest.Sign.Release.storePassword)
                                keyAlias = getProp(Config.Application.KurlyPretest.Sign.Release.keyAlias)
                                keyPassword = getProp(Config.Application.KurlyPretest.Sign.Release.keyPassword)
                            }
                            register(Config.Application.KurlyPretest.Sign.Debug.name) {
                                storeFile = file(getProp(Config.Application.KurlyPretest.Sign.Debug.storeFile))
                                storePassword = getProp(Config.Application.KurlyPretest.Sign.Debug.storePassword)
                                keyAlias = getProp(Config.Application.KurlyPretest.Sign.Debug.keyAlias)
                                keyPassword = getProp(Config.Application.KurlyPretest.Sign.Debug.keyPassword)
                            }
                        }
                    }
                }

                namespace = Config.Application.KurlyPretest.applicationId

                buildTypes {
                    debug {
                        applicationIdSuffix = KurlyPretestAppBuildType.DEBUG.applicationIdSuffix
                        isMinifyEnabled = false
                        isDebuggable = true
                        isJniDebuggable = true
                        signingConfig = signingConfigs.getByName(Config.Application.KurlyPretest.Sign.Debug.name)
                    }
                    release {
                        applicationIdSuffix = KurlyPretestAppBuildType.RELEASE.applicationIdSuffix
                        isMinifyEnabled = true
                        isShrinkResources = true
                        isDebuggable = false
                        isJniDebuggable = false
                        proguardFiles(
                            getDefaultProguardFile(Config.ApplicationSetting.defaultProguardFile),
                            Config.ApplicationSetting.proguardFile
                        )
                        signingConfig = signingConfigs.getByName(Config.Application.KurlyPretest.Sign.Release.name)
                    }
                }

                configureKotlinAndroid(this)
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("androidx.appcompat").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
            }
        }
    }
}