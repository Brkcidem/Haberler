// 7. Paket tanımı
package com.example.haberler

// 8. Gerekli importlar
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
// 10. Application sınıfından türetme
class HaberlerApplication : Application()

//Bu kod HaberlerApplication sınıfını tanımlıyor ve bu sınıf Android uygulamanızın başlangıç noktasıdır.
