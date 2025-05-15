// 104. Fragment paket tanımı
package com.example.haberler.ui.fragment

// 105. Gerekli importlar
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haberler.R
import com.example.haberler.databinding.FragmentNewsBinding
import com.example.haberler.ui.adapter.NewsAdapter
import com.example.haberler.ui.viewmodel.NewsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

/**
 * Haber listesini gösteren fragment
 */
// 106. Hilt için Fragment işaretlemesi
@AndroidEntryPoint
class NewsFragment : Fragment() {

    // 107. View binding ve ViewModel tanımlamaları
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    // 108. Fragment view oluşturma
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 109. View oluşturulduktan sonraki işlemler
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("NewsFragment", "Fragment oluşturuldu")

        setupRecyclerView()
        setupSearch()
        setupCategories()
        setupSwipeRefresh()
        observeViewModel()
    }

    // 110. RecyclerView kurulumu
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            // Haberi Chrome Custom Tabs ile aç
            try {
                val builder = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                
                builder.launchUrl(requireContext(), Uri.parse(article.url))
            } catch (e: Exception) {
                Log.e("NewsFragment", "Haber açılırken hata oluştu", e)
                Toast.makeText(requireContext(), "Haber açılamadı", Toast.LENGTH_SHORT).show()
            }
        }

        // 112. RecyclerView ayarları
        binding.newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    // 113. Arama özelliği kurulumu
    private fun setupSearch() {
        binding.searchEditText.apply {
            // Metin değiştiğinde ara
            doOnTextChanged { text, _, _, _ ->
                viewModel.searchNews(text?.toString() ?: "")
            }

            // 115. Klavye arama tuşu dinleyicisi. Klavyeden ara tuşuna basıldığında ara
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchNews(text?.toString() ?: "")
                    true
                } else {
                    false
                }
            }
        }
    }

    // 116. Kategori seçimi kurulumu
    private fun setupCategories() {
        binding.categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            // 117. Seçilen kategoriye göre haberleri getir
            val category = when {
                checkedIds.isEmpty() -> "all"
                checkedIds[0] == binding.chipAll.id -> "all"
                checkedIds[0] == binding.chipBusiness.id -> "business"
                checkedIds[0] == binding.chipTechnology.id -> "technology"
                checkedIds[0] == binding.chipSports.id -> "sports"
                checkedIds[0] == binding.chipEntertainment.id -> "entertainment"
                checkedIds[0] == binding.chipHealth.id -> "health"
                checkedIds[0] == binding.chipScience.id -> "science"
                else -> "all"
            }
            
            Log.d("NewsFragment", "Seçilen kategori: $category")
            binding.searchEditText.setText("") // Kategori seçildiğinde arama kutusunu temizle
            viewModel.loadNews(query = "", category = if (category == "all") null else category)
        }

        // 118. Başlangıçta "Tümü" seçili olsun
        binding.chipAll.isChecked = true
    }

    // 119. Yenileme özelliği kurulumu
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshNews()
        }
    }

    // 120. ViewModel'dan gelen verileri gözlemle
    private fun observeViewModel() {
        // 121. Haber listesini gözlemle
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            newsAdapter.submitList(articles)
        }

        // 122. Yükleniyor durumunu gözlemle
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                Log.d("NewsFragment", "Yükleniyor durumu")
            }
        }

        // 123. Hata durumunu gözlemle
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                Log.e("NewsFragment", "Hata durumu: $it")
            }
        }
    }

    // 124. Fragment yok edildiğinde binding'i temizle
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 