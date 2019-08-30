package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.utils.Utils
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), NavController.OnDestinationChangedListener {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prev_game.isVisible = Utils.hasLastUsedDeck()
    }

    override fun onResume() {
        super.onResume()
        initClickListeners()
        findNavController().addOnDestinationChangedListener(this)
    }

    private fun initClickListeners() {
        initPrevGameBtn()
        initPlayBtn()
        initCreatorBtn()
    }

    override fun onPause() {
        findNavController().removeOnDestinationChangedListener(this)
        super.onPause()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (controller.graph.startDestination != destination.id) {
            clearClickListeners()
        }
    }

    private fun clearClickListeners() {
        play_btn.setOnClickListener(null)
        creator_btn.setOnClickListener(null)
        prev_game.setOnClickListener(null)
    }

    private fun initPrevGameBtn() {
        prev_game.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_prevGameFragment)
        }
    }

    private fun initCreatorBtn() {
        creator_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deckFragment)
        }
    }

    private fun initPlayBtn() {
        play_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_defaultDecksFragment)
        }
    }
}