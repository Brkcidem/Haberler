// 1. Temel Android uygulama geliştirme pluginleri
plugins {
    // Temel Android uygulama geliştirme desteği sağlar
    id("com.android.application")
    // Kotlin dilini Android'de kullanmamızı sağlar
    id("org.jetbrains.kotlin.android")
    // Kotlin Annotation Processing Tool - Dagger Hilt için gerekli
    id("kotlin-kapt")
    // Navigation Component'te ekranlar arası veri geçişini güvenli hale getirir
    id("androidx.navigation.safeargs.kotlin")
    // Dependency Injection (Bağımlılık Enjeksiyonu) için Hilt desteği
    id("dagger.hilt.android.plugin")
    // Parcelable implementasyonları için Kotlin plugin
    id("kotlin-parcelize")
}

// 2. Android uygulama konfigürasyonu
android {
    // Uygulamamızın benzersiz paket adı
    namespace = "com.example.haberler"
    // Uygulamamızın derlendiği Android SDK versiyonu
    compileSdk = 34

    // 3. Uygulama temel ayarları
    defaultConfig {
        // Google Play Store'daki benzersiz uygulama kimliği
        applicationId = "com.example.haberler"
        // Uygulamamızın çalışacağı minimum Android sürümü (Android 7.0)
        minSdk = 24
        // Uygulamamızın hedeflediği Android sürümü
        targetSdk = 34
        // Uygulama güncellemelerinde kullanılan versiyon numarası
        versionCode = 1
        // Kullanıcıya gösterilen versiyon ismi
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 4. Release ayarları
    buildTypes {
        release {
            // Release versiyonunda kod küçültme özelliği
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // 5. ViewBinding aktivasyonu
    buildFeatures {
        viewBinding = true
    }

    // 6. Java ve Kotlin versiyon ayarları
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Kotlin derleyici ayarları
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
    }
}

// 7. Uygulama bağımlılıkları
dependencies {
    // 8. Android Core ve UI Bileşenleri
    // -----------------------------
    // Kotlin ile Android geliştirme için temel fonksiyonlar sağlar
    implementation("androidx.core:core-ktx:1.12.0")
    
    // Eski Android sürümlerinde yeni özellikleri kullanmamızı sağlar
    // Örneğin: Dark Mode, yeni menü tasarımları gibi
    implementation("androidx.appcompat:appcompat:1.6.1")
    
    // Material Design bileşenleri - Modern UI elemanları
    // Örnek: Özel butonlar, kartlar, bottom navigation bar
    implementation("com.google.android.material:material:1.11.0")
    
    // Gelişmiş layout sistemi - Karmaşık ekran tasarımları için
    // XML'de elemanları konumlandırmayı kolaylaştırır
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // SwipeRefreshLayout - Pull to refresh özelliği için
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    
    // 9. ViewModel ve LiveData - MVVM Mimarisi
    // ------------------------------------
    // Ekran döndürme gibi durumlarda verileri korur
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // Veri değişikliklerini canlı olarak takip etmeyi sağlar
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // 10. Coroutines - Asenkron İşlemler
    // -----------------------------
    // Arka planda çalışan işlemler için (örn: API çağrıları)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // 11. Navigation Component - Ekranlar Arası Geçiş
    // -----------------------------------------
    // Fragment'lar arası geçişleri yönetir
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // 12. Retrofit ve Network - API İstekleri
    // --------------------------------
    // API'lerle iletişim kurmak için HTTP client
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // JSON verilerini Kotlin sınıflarına dönüştürür
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // API isteklerini loglamak için
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // 13. Glide - Resim İşlemleri
    // ----------------------
    // İnternetten resim indirme ve gösterme işlemleri için
    // Resimleri önbelleğe alır, hafıza yönetimini optimize eder
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // 14. Dagger Hilt - Dependency Injection
    // -------------------------------
    // Bağımlılıkları otomatik olarak yönetir
    // Örnek: API servisi, veritabanı gibi sınıfların oluşturulması
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // 15. Android Test bağımlılıkları
    // --------------------------
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    
    // 16. Hilt Test bağımlılıkları
    // -----------------------
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    
    // 17. Unit Test bağımlılıkları
    // ----------------------
    testImplementation("junit:junit:4.13.2")

    // 18. Chrome Custom Tabs
    implementation("androidx.browser:browser:1.7.0")
}