// 21. API servisi paket tanımı
package com.example.haberler.data.api

// 22. NewsResponse modeli import
import com.example.haberler.data.model.NewsResponse
// 23. Retrofit HTTP metodları için importlar
import retrofit2.http.GET
import retrofit2.http.Query

// 24. NewsAPI.org servisi için Retrofit interface tanımı
interface NewsApiService {

    // 25. Top headlines endpoint tanımı - kategoriye göre haber getirme
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        // 26. Ülke parametresi - varsayılan TR
        @Query("country") country: String = "tr",
        // 27. Kategori parametresi - isteğe bağlı
        @Query("category") category: String? = null,
        // 28. Sayfa başına haber sayısı
        @Query("pageSize") pageSize: Int = 100,
        // 29. Sayfa numarası
        @Query("page") page: Int = 1,
        // 30. API anahtarı - kimlik doğrulama için
        @Query("apiKey") apiKey: String
    ): NewsResponse

    // 31. Everything endpoint tanımı - anahtar kelimeye göre arama
    @GET("v2/everything")
    suspend fun getEverything(
        // 32. Arama terimi - zorunlu
        @Query("q") q: String,
        // 33. Dil filtresi - varsayılan TR
        @Query("language") language: String = "tr",
        // 34. Sıralama kriteri - varsayılan en yeni
        @Query("sortBy") sortBy: String = "publishedAt",
        // 35. Sayfa başına haber sayısı
        @Query("pageSize") pageSize: Int = 100,
        // 36. Sayfa numarası
        @Query("page") page: Int = 1,
        // 37. API anahtarı - kimlik doğrulama için
        @Query("apiKey") apiKey: String
    ): NewsResponse
} 