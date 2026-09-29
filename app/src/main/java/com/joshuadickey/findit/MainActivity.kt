package com.joshuadickey.findit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshuadickey.findit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding; private lateinit var store: ItemStore; private lateinit var adapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); binding = ActivityMainBinding.inflate(layoutInflater); setContentView(binding.root)
        store = ItemStore(this); adapter = ItemAdapter(emptyList()) { store.delete(it.id); refresh() }; binding.itemList.layoutManager = LinearLayoutManager(this); binding.itemList.adapter = adapter
        binding.addButton.setOnClickListener { startActivity(Intent(this, AddItemActivity::class.java)) }
        binding.searchInput.addTextChangedListener { refresh(it?.toString().orEmpty()) }
    }
    override fun onResume() { super.onResume(); refresh() }
    private fun refresh(query: String = binding.searchInput.text?.toString().orEmpty()) { val filtered = store.all().filter { "${it.name} ${it.location} ${it.note}".contains(query, true) }; adapter.update(filtered); binding.itemCount.text = "${filtered.size} saved item${if (filtered.size == 1) "" else "s"}"; binding.emptyState.visibility = if (filtered.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE }
}
