// 11. Ana paket tanımı - uygulamanın temel paketi
package com.example.haberler

// 12. Bundle import - aktivite yaşam döngüsü için
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
// 13. AppCompatActivity import - geriye dönük uyumlu aktivite için
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
// 14. NavHostFragment import - navigation yönetimi için
import androidx.navigation.fragment.NavHostFragment
// 15. ViewBinding import - layout bağlama için
import com.example.haberler.databinding.ActivityMainBinding
// 16. AndroidEntryPoint import - Hilt bağımlılık enjeksiyonu için
import dagger.hilt.android.AndroidEntryPoint

// 17. Hilt için aktivite işaretlemesi - bağımlılık enjeksiyonunu aktif eder
@AndroidEntryPoint
// 18. Ana aktivite sınıf tanımı - uygulamanın başlangıç noktası
class MainActivity : AppCompatActivity() {

    // 19. ViewBinding değişken tanımı - layout ile etkileşim için
    private lateinit var binding: ActivityMainBinding

    // 20. Aktivite yaşam döngüsü başlangıç metodu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.coffee_text)
        }

        // Navigation host fragment'ı bul
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
} 