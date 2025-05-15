package com.example.haberler.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    // 28. Haber kaynağı bilgisi (örn: CNN, BBC)
    @SerializedName("source")
    val source: Source,
    
    // 29. Haber yazarı (opsiyonel)
    @SerializedName("author")
    val author: String?,
    
    // 30. Haber başlığı (zorunlu)
    @SerializedName("title")
    val title: String,
    
    // 31. Haber özeti (opsiyonel)
    @SerializedName("description")
    val description: String?,
    
    // 32. Haberin tam URL'i (zorunlu)
    @SerializedName("url")
    val url: String,
    
    // 33. Haber görseli URL'i (opsiyonel)
    @SerializedName("urlToImage")
    val urlToImage: String?,
    
    // 34. Yayınlanma tarihi (opsiyonel)
    @SerializedName("publishedAt")
    val publishedAt: String?,
    
    // 35. Haber içeriği (opsiyonel)
    @SerializedName("content")
    val content: String?
) : Parcelable

@Parcelize
data class Source(
    // 37. Kaynak ID'si (opsiyonel)
    @SerializedName("id")
    val id: String?,
    
    // 38. Kaynak adı (zorunlu)
    @SerializedName("name")
    val name: String
) : Parcelable