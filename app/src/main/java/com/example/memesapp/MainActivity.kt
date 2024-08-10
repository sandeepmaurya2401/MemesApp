package com.example.memesapp


import android.app.DownloadManager
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.memesapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        setSupportActionBar(binding.toolBar)

        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            Picasso.get().load(viewModel.liveData.value).into(binding.imageView)
            binding.progressBar.visibility = View.GONE
        })


        binding.nextButton.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            viewModel.loadMeme()

        }

        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        binding.downloadButton.setOnClickListener {
            val downloadViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
            val fileName = "Meme_${Calendar.getInstance().timeInMillis}_.png"
            val request = DownloadManager.Request(Uri.parse(downloadViewModel.liveData.value)).apply {
                title = "Meme Image"
                setDescription("This is very funny meme.")
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Meme app/Image/$fileName")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            }
            dm.enqueue(request)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.tool_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            if (item.itemId == R.id.share){
                shareMeme()
            }else if (item.itemId==R.id.changeTheme){
                Toast.makeText(this, "theme option clicked", Toast.LENGTH_LONG).show()
            }
        return super.onOptionsItemSelected(item)
    }

     private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sendViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        intent.putExtra(Intent.EXTRA_TEXT, sendViewModel.liveData.value)
        val chooser = Intent.createChooser(intent, "Share this meme using....")
        startActivity(chooser)
    }
}