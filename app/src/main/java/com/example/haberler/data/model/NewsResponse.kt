// 38. Model sınıfları paket tanımı
package com.example.haberler.data.model

// 39. Gson anotasyonları için import
import com.google.gson.annotations.SerializedName

// 40. API'den gelen haber yanıtı için veri sınıfı
data class NewsResponse(
    // 41. API yanıt durumu (success/error)
    @SerializedName("status")
    val status: String,

    // 42. Toplam sonuç sayısı
    @SerializedName("totalResults")
    val totalResults: Int,

    // 43. Haber listesi
    @SerializedName("articles")
    val articles: List<Article>,

    // 44. Hata durumunda mesaj (opsiyonel)
    @SerializedName("message")
    val message: String? = null // Hata durumunda API'den gelen mesaj
)

// 45. Sayfalama bilgileri için veri sınıfı
data class Pagination(
    // 46. Sayfa başına maksimum kayıt sayısı
    @SerializedName("limit")
    val limit: Int,

    // 47. Başlangıç konumu
    @SerializedName("offset")
    val offset: Int,

    // 48. Mevcut sayfadaki kayıt sayısı
    @SerializedName("count")
    val count: Int,

    // 49. Toplam kayıt sayısı
    @SerializedName("total")
    val total: Int
) 