package com.chatkeeper.app.ui

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import com.chatkeeper.app.databinding.ActivityStatusSaverBinding

/**
 * WhatsApp status images/videos live under a hidden ".Statuses" folder that
 * apps can no longer read directly on modern Android (scoped storage).
 * Instead, we ask the user to pick that folder ONCE using Android's built-in
 * folder picker (Storage Access Framework). After that we remember the
 * permission and can list + copy files from it any time - no extra
 * permissions or WhatsApp modification needed.
 */
class StatusSaverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusSaverBinding
    private val prefs by lazy { getSharedPreferences("chatkeeper_prefs", MODE_PRIVATE) }

    private val folderPicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@registerForActivityResult
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        prefs.edit().putString(KEY_TREE_URI, uri.toString()).apply()
        loadStatuses(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusSaverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val adapter = StatusAdapter { item -> saveStatus(item) }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
        this.adapter = adapter

        binding.btnPickFolder.setOnClickListener { launchFolderPicker() }

        // If we already have permission from a previous run, load right away.
        prefs.getString(KEY_TREE_URI, null)?.let { saved ->
            loadStatuses(Uri.parse(saved))
        }
    }

    private lateinit var adapter: StatusAdapter

    private fun launchFolderPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Nudge the system picker to open inside WhatsApp's media folder if possible.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(
                "content://com.android.externalstorage.documents/document/primary:Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
            ))
        }
        folderPicker.launch(intent)
    }

    private fun loadStatuses(treeUri: Uri) {
        val tree = DocumentFile.fromTreeUri(this, treeUri)
        val files = tree?.listFiles()?.filter {
            val name = it.name ?: return@filter false
            (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".mp4"))
        } ?: emptyList()

        val items = files.map {
            StatusItem(uri = it.uri, name = it.name ?: "status", isVideo = it.name?.endsWith(".mp4") == true)
        }

        adapter.submitList(items)
        binding.tvEmpty.visibility = if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun saveStatus(item: StatusItem) {
        try {
            val resolver = contentResolver
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, item.name)
                put(MediaStore.MediaColumns.RELATIVE_PATH,
                    if (item.isVideo) "Movies/ChatKeeper" else "Pictures/ChatKeeper")
            }
            val collection = if (item.isVideo)
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val destUri = resolver.insert(collection, values) ?: return

            resolver.openInputStream(item.uri)?.use { input ->
                resolver.openOutputStream(destUri)?.use { output ->
                    input.copyTo(output)
                }
            }

            Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Could not save: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val KEY_TREE_URI = "status_tree_uri"
    }
}
