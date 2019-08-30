package com.morrowbone.mafiacards.app.fragments

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.SectionsPagerAdapter
import com.morrowbone.mafiacards.app.application.MafiaApp
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.utils.Utils
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_take_cards.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field

class TakeCardsFragment : Fragment() {

    /**
     * The [PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [FragmentStatePagerAdapter].
     */
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private var scroller: FixedSpeedScroller? = null
    private var interstitial: InterstitialAd? = null

    private val isEnableShowGooglePlayReviewIs: Boolean
        get() {
            val gamesFinished = Utils.getPlayedGameCount(requireContext())
            val period = Utils.getRateAfterCount(requireContext())
            return gamesFinished!! % period!! == 0 && Utils.isEnableRateApp(requireContext())!! && gamesFinished > 0
        }

    private val isEnableShowingAds: Boolean
        get() = true

    private val args: TakeCardsFragmentArgs by navArgs()
    private val deckViewModel : DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext(), args.deckId)
    }

    private fun checkInternetConnection(): Boolean {
        val conMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val i = conMgr.activeNetworkInfo ?: return false
        if (!i.isConnected)
            return false
        return if (!i.isAvailable) false else true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_take_cards, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckViewModel.cards.observe(this, Observer { cards ->
            mSectionsPagerAdapter = SectionsPagerAdapter(requireFragmentManager(), cards)
            pager.adapter = mSectionsPagerAdapter
            val cardCount = cards.size
            val text = String.format(requireContext().getString(R.string.cards_in_deck_take_cards), cardCount)
            val finalText = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            cards_in_deck.text = finalText
        })

        fixViewPager()

        loadAd()
    }

    private fun loadAd() {
        // Create the interstitial.
        interstitial = InterstitialAd(requireContext())
        interstitial!!.adUnitId = "ca-app-pub-7668409826365482/6093438917"

        // Create ad request.
        val adRequest = AdRequest.Builder().build()
        if (isEnableShowingAds) {
            interstitial!!.loadAd(adRequest)
        }
    }

    private fun fixViewPager() {
        try {
            val scrollerField: Field = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val sInterpolator = DecelerateInterpolator()
            scroller = FixedSpeedScroller(pager.context, sInterpolator)
            scrollerField.set(pager, scroller)
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    fun displayInterstitial() {
        if (interstitial!!.isLoaded) {
            interstitial!!.show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isEnableShowGooglePlayReviewIs && checkInternetConnection()) {
            showGooglePlayReviewDialog()
        }
        SectionsPagerAdapter.cardShowListeners.add(this::showNextPage)
    }

    override fun onPause() {
        super.onPause()
        SectionsPagerAdapter.cardShowListeners.remove(this::showNextPage)
    }

    private fun showNextPage() {
        val childCount = mSectionsPagerAdapter.count
        val currItem = pager.currentItem
        if (currItem == childCount - 1) {
            saveLastUsedDeck()
            showLastCardDialog()
        } else {
            scroller!!.setFixedDuration(1000)
            pager.setCurrentItem(currItem + 1, true)
            scroller!!.setFixedDuration(null)
        }
    }

    private fun saveLastUsedDeck() {
        Utils.setLastUsedDeckId(args.deckId)
    }

    private fun showGooglePlayReviewDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.are_you_like_app)
        builder.setMessage(R.string.review_app)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.review_now) { arg0, arg1 ->
            val appPackageName = requireContext().packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
            }

            Utils.setIsEnableRateAppToFalse(requireContext())
        }
        builder.setNeutralButton(R.string.later_review) { dialog, which -> Utils.incrementRateAfter(requireContext()) }

        builder.setNegativeButton(R.string.never_review) { dialog, which ->
            Utils.setIsEnableRateAppToFalse(requireContext())
        }

        builder.show()
    }

    private fun showLastCardDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.final_dialog_title)
        builder.setMessage(R.string.final_dialog_message)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.final_dialog_go_to_main_menu) { arg0, arg1 ->
            if (isEnableShowingAds) {
                displayInterstitial()
            }
            Utils.incrementPlayedGameCount(requireContext())
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        builder.show()
    }

    inner class FixedSpeedScroller : Scroller {

        private var mDuration: Int? = null

        constructor(context: Context) : super(context) {}

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {}

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator, flywheel) {
        }


        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            // Ignore received duration, use fixed one instead
            if (mDuration == null) {
                super.startScroll(startX, startY, dx, dy, duration)
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration!!)
            }
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            // Ignore received duration, use fixed one instead
            if (mDuration == null) {
                super.startScroll(startX, startY, dx, dy)
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration!!)
            }
        }

        fun setFixedDuration(duration: Int?) {
            mDuration = duration
        }
    }
}