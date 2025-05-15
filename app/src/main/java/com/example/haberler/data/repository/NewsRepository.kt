// 61. Repository paket tanımı
package com.example.haberler.data.repository

// 62. Gerekli importlar
import com.example.haberler.data.api.NewsApiService
import com.example.haberler.data.model.NewsResponse
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

// 63. Singleton repository sınıfı tanımı
@Singleton
class NewsRepository @Inject constructor(
    // 64. API servisi bağımlılığı
    private val newsApiService: NewsApiService,
    // 65. API anahtarı bağımlılığı
    private val apiKey: String
) {
    // Son başarılı yanıtları önbellekte tutuyoruz
    private var cachedResponse: NewsResponse? = null
    private var lastCacheTime: Long = 0
    private val CACHE_DURATION = 10_000 // 10 saniye

    // 66. Haber getirme fonksiyonu
    /*
    Kotlin'de bir suspend function, bir coroutine içinde çalıştırılabilen
    ve asenkron işlemleri kolayca tanımlamak için kullanılan bir işlevdir.
    suspend anahtar kelimesi, bir fonksiyonun yürütülmesinin duraklatılabileceğini
    ve daha sonra yeniden başlatılabileceğini ifade eder.
    Bu, özellikle uzun süren veya bloklama gerektiren işlemleri
    (örneğin, ağ istekleri, veri tabanı işlemleri) verimli bir şekilde gerçekleştirmek için kullanılır.
     */
    suspend fun getNews(query: String = "", category: String? = null, forceRefresh: Boolean = false): NewsResponse {
        // 67. Log kaydı
        Log.d("NewsRepository", "getNews çağrıldı - query: $query, category: $category, forceRefresh: $forceRefresh")

        return try {
            // Kategori seçiliyse, kategori bazlı anahtar kelimelerle arama yap
            // 68. Kategori kontrolü
            if (category != null) {
                // 69. Kategori bazlı anahtar kelime ve filtre tanımları
                /*
                searchQuery: API'ye gönderilecek arama sorgusu
                keywords: Gelen haberleri filtrelemek için kullanılacak anahtar kelimeler seti
                 */
                val (searchQuery, keywords) = when (category.lowercase()) {
                    // Pair: İki değeri birlikte tutan bir veri yapısı
                    "business" -> Pair(
                        // İlk kısım (searchQuery): API'ye gönderilecek arama sorgusu
                        "ekonomi OR finans OR borsa OR şirket OR yatırım",
                        // İkinci kısım (keywords): Filtreleme için kullanılacak kelimeler
                        setOf("ekonomi", "finans", "borsa", "şirket", "yatırım", "dolar", "euro", "altın", "kripto", "bist", "merkez bankası", "faiz", "enflasyon", "ticaret")
                    )
                    "technology" -> Pair(
                        "teknoloji OR yazılım OR bilişim OR yapay zeka OR inovasyon",
                        setOf("teknoloji", "yazılım", "bilişim", "yapay zeka", "inovasyon", "robot", "uygulama", "telefon", "bilgisayar", "siber", "internet", "dijital", "mobil", "android", "ios")
                    )
                    "sports" -> Pair(
                        "spor OR futbol OR basketbol OR maç OR şampiyonluk",
                        setOf("spor", "futbol", "basketbol", "maç", "şampiyonluk", "lig", "turnuva", "transfer", "teknik direktör", "galatasaray", "fenerbahçe", "beşiktaş", "süper lig", "milli takım")
                    )
                    "entertainment" -> Pair(
                        "magazin OR sinema OR dizi OR sanat OR müzik",
                        setOf("magazin", "sinema", "dizi", "sanat", "müzik", "film", "konser", "ünlü", "oyuncu", "şarkıcı", "festival", "netflix", "oscar", "tiyatro", "gişe")
                    )
                    "science" -> Pair(
                        "bilim OR uzay OR araştırma OR keşif OR teknoloji",
                        setOf("bilim", "uzay", "araştırma", "keşif", "teknoloji", "nasa", "deney", "gezegen", "galaksi", "bilimsel", "tübitak", "mars", "teleskop", "yapay zeka", "genetik")
                    )
                    "health" -> Pair(
                        "sağlık OR hastane OR tedavi OR ilaç OR tıp",
                        setOf("sağlık", "hastane", "tedavi", "ilaç", "tıp", "doktor", "hastalık", "virüs", "aşı", "pandemi", "covid", "kanser", "ameliyat", "sağlık bakanlığı", "hasta")
                    )
                    else -> Pair(category, setOf(category))
                }
                
                Log.d("NewsRepository", "Kategori haberleri getiriliyor: $category, Arama: $searchQuery")
                // 70. API'den kategoriye göre haberleri getir
                val response = newsApiService.getEverything(
                    q = searchQuery,
                    language = "tr",
                    sortBy = "publishedAt",
                    pageSize = 100,
                    page = 1,
                    apiKey = apiKey
                )

                // 71. Haberleri filtrele
                val filteredArticles = response.articles.filter { article ->
                    val content = (article.title + " " + (article.description ?: "") + " " + (article.content ?: "")).lowercase()
                    keywords.any { keyword -> content.contains(keyword.lowercase()) }
                }

                // 72. Filtreleme sonuçlarını logla
                Log.d("NewsRepository", "Toplam haber: ${response.articles.size}, Filtrelenmiş haber: ${filteredArticles.size}")

                // 73. Filtrelenmiş haberleri döndür
                NewsResponse(
                    status = response.status,
                    totalResults = filteredArticles.size,
                    articles = filteredArticles
                )
            }
            // 74. Arama sorgusu kontrolü. Arama terimi varsa arama yap
            else if (query.isNotEmpty()) {
                // 75. Arama sonuçlarını logla
                Log.d("NewsRepository", "Arama sonuçları getiriliyor: $query")
                // 76. API'den arama sonuçlarını getir
                newsApiService.getEverything(
                    q = query,
                    language = "tr",
                    sortBy = "publishedAt",
                    pageSize = 100,
                    page = 1,
                    apiKey = apiKey
                )
            }
            // 77. Varsayılan durum - ana sayfa haberleri. Ne kategori ne de arama terimi yoksa ana sayfadaki haberleri getir
            else {
                // 78. Ana sayfa haberlerini logla
                Log.d("NewsRepository", "Ana sayfa haberleri getiriliyor")
                // 79. API'den ana sayfa haberlerini getir
                newsApiService.getTopHeadlines(
                    country = "tr",
                    pageSize = 100,
                    page = 1,
                    apiKey = apiKey
                )
            }
        } catch (e: Exception) {
            // 80. Hata durumunu logla
            Log.e("NewsRepository", "Haber getirme hatası", e)
            throw e
        }
    }
} 