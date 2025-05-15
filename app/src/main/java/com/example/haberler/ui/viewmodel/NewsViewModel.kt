// 81. ViewModel paket tanımı
package com.example.haberler.ui.viewmodel

// 82. Gerekli importlar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haberler.data.model.Article
import com.example.haberler.data.model.NewsResponse
import com.example.haberler.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Haber listesi için ViewModel
 */
// 83. Hilt ViewModel tanımı
@HiltViewModel
class NewsViewModel @Inject constructor(
    // 84. Repository bağımlılığı
    private val repository: NewsRepository
) : ViewModel() {

    // 85. Haber listesi için LiveData tanımları
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    // 86. Yükleniyor durumu için LiveData tanımları
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 87. Hata durumu için LiveData tanımları
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // 88. Arama ve istek yönetimi için değişkenler
    private var searchJob: Job? = null
    private var currentQuery: String = ""
    private var currentCategory: String? = null
    private var lastRequestTime: Long = 0
    private val MIN_REQUEST_INTERVAL = 1000 // 1 saniye

    // 89. İlk yükleme
    init {
        loadNews()
    }

    // 90. Haberleri yenileme fonksiyonu
    fun refreshNews() {
        viewModelScope.launch {
            try {
                // 91. Yükleniyor durumunu güncelle
                _isLoading.value = true
                // Önbelleği yoksayarak yeni veri getir
                // 92. Repository'den haberleri getir
                val response = repository.getNews(
                    query = currentQuery,
                    category = currentCategory,
                    forceRefresh = true
                )

                // 93. Hata kontrolü
                if (response.status == "error") {
                    _error.value = "Haberler yüklenirken bir hata oluştu. Lütfen tekrar deneyin."
                    return@launch
                }

                // 94. Başarılı yanıtı işle
                processNewsResponse(response)
            } catch (e: Exception) {
                // 95. Hata durumunu işle
                handleError(e)
            } finally {
                // 96. Yükleniyor durumunu kapat
                _isLoading.value = false
            }
        }
    }

    /**
     * Haberleri repository'den al
     */
    // 97. Haberleri yükleme fonksiyonu
    fun loadNews(query: String = currentQuery, category: String? = currentCategory) {
        // 98. İstek sıklığı kontrolü
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime < MIN_REQUEST_INTERVAL) {
            Log.d("NewsViewModel", "İstek çok sık yapıldı, ${MIN_REQUEST_INTERVAL - (currentTime - lastRequestTime)} ms sonra tekrar denenecek")
            viewModelScope.launch {
                delay(MIN_REQUEST_INTERVAL - (currentTime - lastRequestTime))
                loadNews(query, category)
            }
            return
        }
        lastRequestTime = currentTime

        // 99. Mevcut sorgu ve kategoriyi güncelle
        currentQuery = query
        currentCategory = category

        // 100. Haberleri getir
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("NewsViewModel", "Haberler yükleniyor... (Kategori: $category, Arama: $query)")

                val response = repository.getNews(
                    query = if (category == null && query.isEmpty()) "türkiye" else query,
                    category = category,
                    forceRefresh = true
                )
                processNewsResponse(response)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 101. API yanıtını işleme fonksiyonu
    private fun processNewsResponse(response: NewsResponse) {
        if (response.status == "error") {
            _error.value = "Haberler yüklenirken bir hata oluştu. Lütfen tekrar deneyin."
            return
        }

        _articles.value = response.articles
        Log.d("NewsViewModel", "Haberler başarıyla yüklendi. Toplam: ${response.articles.size}")
    }

    // 102. Hata işleme fonksiyonu
    private fun handleError(e: Exception) {
        Log.e("NewsViewModel", "Haber yükleme hatası", e)
        _error.value = when {
            e.message?.contains("429") == true -> "Çok fazla istek yapıldı. Önbellekteki haberler gösteriliyor."
            e.message?.contains("426") == true -> "API limiti aşıldı. Önbellekteki haberler gösteriliyor."
            else -> "Haberler yüklenirken bir hata oluştu. Lütfen tekrar deneyin."
        }
    }

    // 103. Arama fonksiyonu
    fun searchNews(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // 300ms bekle
            currentCategory = null // Arama yapıldığında kategoriyi sıfırla
            loadNews(query = query)
        }
    }

    /**
     * Haber listesinin durumunu temsil eden sealed class
     */
    sealed class NewsState {
        object Loading : NewsState()
        data class Success(val articles: List<Article>) : NewsState()
        data class Error(val message: String) : NewsState()
    }
} 