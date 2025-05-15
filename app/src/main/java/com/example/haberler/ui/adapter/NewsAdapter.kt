// 125. Adapter paket tanımı
package com.example.haberler.ui.adapter

// 126. Gerekli importlar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.haberler.R
import com.example.haberler.data.model.Article
import com.example.haberler.databinding.ItemNewsBinding

/**
 * Haber listesi için RecyclerView adapter'ı
 */
// 127. Haber listesi adapter sınıfı - ListAdapter'dan türetilmiş
class NewsAdapter(private val onItemClick: (Article) -> Unit) : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    // 128. ViewHolder oluşturma metodu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // 129. View binding ile layout bağlama
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    // 130. ViewHolder'a veri bağlama metodu
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 131. ViewHolder iç sınıfı
    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 132. Tıklama olayı tanımlama
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        // 133. Haber verilerini görünüme bağlama
        fun bind(article: Article) {
            binding.apply {
                // 134. Başlık ve açıklama ayarlama
                newsTitle.text = article.title
                newsDescription.text = article.description

                // 135. Resim yükleme işlemi
                article.urlToImage?.let { imageUrl ->
                    Glide.with(newsImage)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.news_placeholder)
                        .error(R.drawable.news_placeholder)
                        .centerCrop()
                        .into(newsImage)
                } ?: run {
                    // 136. Resim URL'i yoksa varsayılan görsel
                    newsImage.setImageResource(R.drawable.news_placeholder)
                }
            }
        }
    }

    // 137. Liste değişikliklerini yönetmek için DiffUtil
    private class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
        // 138. İki öğenin aynı olup olmadığını kontrol et
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        // 139. İki öğenin içeriğinin aynı olup olmadığını kontrol et
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
} 