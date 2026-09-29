package com.joshuadickey.findit

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class ItemStore(context: Context) {
    private val prefs = context.getSharedPreferences("find_it_items", Context.MODE_PRIVATE)
    fun all(): MutableList<SavedItem> {
        val saved = JSONArray(prefs.getString("items", "[]"))
        return MutableList(saved.length()) { index ->
            val obj = saved.getJSONObject(index)
            SavedItem(obj.getLong("id"), obj.getString("name"), obj.getString("location"), obj.optString("note"), obj.optString("photo").ifBlank { null })
        }.sortedByDescending { it.id }.toMutableList()
    }
    fun save(item: SavedItem) { val items = all(); items.removeAll { it.id == item.id }; items.add(item); write(items) }
    fun delete(id: Long) { write(all().filterNot { it.id == id }) }
    private fun write(items: List<SavedItem>) {
        val array = JSONArray()
        items.forEach { item -> array.put(JSONObject().apply { put("id", item.id); put("name", item.name); put("location", item.location); put("note", item.note); put("photo", item.photoPath ?: "") }) }
        prefs.edit().putString("items", array.toString()).apply()
    }
}
