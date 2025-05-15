// 50. DI modülü paket tanımı
package com.example.haberler.di

// 51. Gerekli importlar
import com.example.haberler.data.api.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// 52. Hilt modül tanımı. Bu sınıfın bir Hilt modülü olduğunu belirtir
@Module
// 53. Singleton bileşenine kurulum. Bu modülün uygulama yaşam döngüsü boyunca tek bir örneğinin olacağını belirtir
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 54. API'nin temel URL'i
    private const val BASE_URL = "https://newsapi.org/"

    // 55. NewsAPI.org API anahtarı
    private const val API_KEY = "3065d3fe9f8e4e508f1cb5f38d302f94" // NewsAPI.org'dan aldığınız API anahtarını buraya ekleyin

    // 56. API anahtarı sağlayıcı metod
    @Provides //Hilt'e "bu metod bir bağımlılık sağlıyor" der
    @Singleton //Bu bağımlılığın uygulama yaşam döngüsü boyunca tek bir örneğinin olacağını belirtir
    fun provideApiKey(): String = API_KEY
    /*
    Bu metod uygulamada API anahtarı gerektiğinde çağrılır
    Singleton olduğu için her yerde aynı API anahtarı kullanılır
     */

    // 57. HTTP log interceptor sağlayıcı. API isteklerini ve yanıtlarını loglamak için kullanılır.
    // Debugging sürecinde API iletişimini görmemizi sağlar.
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 58. OkHttpClient sağlayıcı. API istekleri için HTTP istemcisi oluşturur.
    // loggingInterceptor parametresi otomatik olarak Hilt tarafından enjekte edilir
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // 59. Retrofit instance sağlayıcı. API ile iletişim için Retrofit örneği oluşturur.
    // JSON dönüşümleri için Gson converter'ı kullanır
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 60. NewsApiService sağlayıcı. NewsApiService interface'ini implement eden bir nesne oluşturur.
    // Bu nesne API çağrıları yapmak için kullanılır
    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}

/*
Bağımlılık Zinciri:
Hilt, bu bağımlılıkları otomatik olarak yönetir
Örneğin, NewsApiService'e ihtiyaç olduğunda:
Önce Retrofit nesnesini oluşturur
Retrofit için OkHttpClient'a ihtiyaç duyar ve onu oluşturur
OkHttpClient için LoggingInterceptor'a ihtiyaç duyar ve onu oluşturur
Bu zincir otomatik olarak yönetilir

Bu modül sayesinde:
Ağ istekleri için gerekli tüm bileşenler merkezi bir yerden yönetilir
Bileşenler arasındaki bağımlılıklar otomatik olarak çözülür
Singleton yapısı sayesinde bellek kullanımı optimize edilir
Kod tekrarı önlenir ve bakım kolaylaşır
 */