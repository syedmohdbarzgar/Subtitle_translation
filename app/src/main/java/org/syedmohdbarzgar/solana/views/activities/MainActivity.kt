package org.syedmohdbarzgar.solana.views.activities

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import org.syedmohdbarzgar.solana.R
import org.syedmohdbarzgar.solana.adapters.SubtitleFileAdapter
import org.syedmohdbarzgar.solana.databinding.ActivityMainBinding
import org.syedmohdbarzgar.solana.model.SubtitleFile
import org.syedmohdbarzgar.solana.viewModel.MainViewModel

class MainActivity : AppCompatActivity(), SubtitleFileAdapter.OnSubtitleFileClickListener {
    private val TAG = "MainActivity"
    private lateinit var adapter: SubtitleFileAdapter
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setup()
    }

    private fun setup() {
        connectivityManager = getSystemService(ConnectivityManager::class.java)

        setupNetworkCallback()

        setupEdge()
        setupToolbar()
        setupHistoryProject()
        setupNewProject()
    }

    private fun setupNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                Log.d(TAG, "Internet is connected")
                // اینجا می‌توانید کدی برای مدیریت وضعیت اتصال اینترنت بنویسید
            }

            override fun onLost(network: android.net.Network) {
                Log.d(TAG, "Internet is disconnected")
                // اینجا می‌توانید کدی برای مدیریت قطع اتصال اینترنت بنویسید
            }
        }
    }

    private fun setupNewProject() {
        activityMainBinding.fabNewProject.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, TranslatorActivity::class.java)
            )
        }
    }

    private fun setupHistoryProject() {
        mainViewModel.getAllSubtitleFiles()

        mainViewModel.subtitleFilesLiveData.observe(this) { subtitleFiles ->

            activityMainBinding.txtEmpty.visibility =
                if (subtitleFiles.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            adapter = SubtitleFileAdapter(subtitleFiles, this)
            activityMainBinding.listSubtitleFile.adapter = adapter
            activityMainBinding.listSubtitleFile.layoutManager = LinearLayoutManager(this)

            // تنظیم ItemTouchHelper برای قابلیت Swipe to Delete
            val itemTouchHelper =
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        Log.e(TAG, "onSwiped: ${subtitleFiles.size}--$position")

                        if (subtitleFiles.isNotEmpty() && position in subtitleFiles.indices) {
                            val deletedFile = subtitleFiles[position]
                            mainViewModel.deleteSubtitleFile(deletedFile)
                            adapter.notifyItemRemoved(position)
                            subtitleFiles.removeAt(position)
                        }
                    }

                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {
                        val itemView = viewHolder.itemView
                        val icon: Drawable = ContextCompat.getDrawable(
                            recyclerView.context,
                            R.drawable.delete_trash
                        )!!

                        icon.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

                        val background = ColorDrawable()
                        val backgroundColor = Color.parseColor("#f44336")
                        background.color = backgroundColor

                        if (dX > 0) {
                            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                        } else {
                            background.setBounds(0, 0, 0, 0)
                        }
                        background.draw(c)

                        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + icon.intrinsicHeight

                        if (dX > 0) {
                            val iconLeft = itemView.left + iconMargin
                            val iconRight = iconLeft + icon.intrinsicWidth
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        } else {
                            icon.setBounds(0, 0, 0, 0)
                        }

                        icon.draw(c)
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }
                })

            itemTouchHelper.attachToRecyclerView(activityMainBinding.listSubtitleFile)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(activityMainBinding.mainToolbar)
        activityMainBinding.mainToolbar.isTitleCentered = true
        activityMainBinding.mainToolbar.setNavigationIcon(R.drawable.settings)
        activityMainBinding.mainToolbar.setNavigationOnClickListener {
            startActivity(
                Intent(this@MainActivity, SettingsActivity::class.java)
            )
        }
    }

    private fun setupEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(activityMainBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSubtitleFileClick(subtitleFile: SubtitleFile) {
        val subtitleFileString = GsonBuilder().create().toJson(subtitleFile)
        startActivity(
            Intent(this@MainActivity, SubtitleEditingActivity::class.java).apply {
                putExtra("subtitleFile", subtitleFileString)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onStop() {
        super.onStop()
//        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
