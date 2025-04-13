package com.example.tma_semestralka

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tma_semestralka.databinding.ActivityMainBinding
import com.example.tma_semestralka.player.AddEditPlayerFragment
import com.example.tma_semestralka.player.Player
import com.example.tma_semestralka.player.PlayerDao
import com.example.tma_semestralka.player.PlayerDetailsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AddEditPlayerFragment.AddEditPlayerListener,
    PlayerDetailsAdapter.PlayerDetailsClickListener {

    private lateinit var binding: ActivityMainBinding
    private var dao: PlayerDao? = null
    private lateinit var adapter: PlayerDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        attachUiListener()
        subscribeDataStreams()
    }

    private fun subscribeDataStreams() {
        lifecycleScope.launch {
            dao?.getAllPlayers()?.collect { mList ->
                adapter.submitList(mList)
            }
        }
    }

    private fun initVars() {
        dao = AppDatabase.getDatabase(this).playerDao()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlayerDetailsAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    private fun attachUiListener() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet(player: Player? = null) {
        val bottomSheet = AddEditPlayerFragment.newInstance(player)
        bottomSheet.show(supportFragmentManager, AddEditPlayerFragment.TAG)
    }

    override fun onSaveBtnClicked(isUpdate: Boolean, player: Player) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (isUpdate)
                dao?.updatePlayer(player)
            else
                dao?.savePlayer(player)
        }
    }

    override fun onEditPlayerClick(player: Player) {
        showBottomSheet(player)
    }

    override fun onDeletePlayerClick(player: Player) {
        lifecycleScope.launch(Dispatchers.IO) {
            dao?.deletePlayerById(player.id)
        }
    }
}