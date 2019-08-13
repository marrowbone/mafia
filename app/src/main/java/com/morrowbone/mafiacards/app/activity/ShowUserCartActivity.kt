package com.morrowbone.mafiacards.app.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Scroller
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.SectionsPagerAdapter
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.fragments.AdsFragment
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.utils.Utils
import com.morrowbone.mafiacards.app.views.NonSwipeableViewPager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Field


class ShowUserCartActivity : FragmentActivity() {
    /**
     * The [PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [FragmentStatePagerAdapter].
     */
    internal lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: NonSwipeableViewPager? = null
    private var scroller: FixedSpeedScroller? = null
    private var mCardCountTextView: TextView? = null
    private var interstitial: InterstitialAd? = null
    private var mAdsView: View? = null

    private val isEnableShowGooglePlayReviewIs: Boolean
        get() {
            val gamesFinished = Utils.getPlayedGameCount(this)
            val period = Utils.getRateAfterCount(this)
            return gamesFinished!! % period!! == 0 && Utils.isEnableRateApp(this)!! && gamesFinished > 0
        }

    private val isEnableShowingAds: Boolean
        get() = true

    private fun checkInternetConnection(): Boolean {
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val i = conMgr.activeNetworkInfo ?: return false
        if (!i.isConnected)
            return false
        return if (!i.isAvailable) false else true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user_cart)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, this, mDeck!!)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.pager) as NonSwipeableViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        try {
            val mScroller: Field
            mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            val sInterpolator = DecelerateInterpolator()
            scroller = FixedSpeedScroller(mViewPager!!.context, sInterpolator)
            mScroller.set(mViewPager, scroller)
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }

        mCardCountTextView = findViewById<View>(R.id.card_count_textview) as TextView
        mCardCountTextView!!.text = mDeck!!.getCards().size.toString()
        mAdsView = findViewById(R.id.layout_for_fragment)

        val adsFragment = AdsFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.layout_for_fragment, adsFragment).commit()


        // Create the interstitial.
        interstitial = InterstitialAd(this)
        interstitial!!.adUnitId = "ca-app-pub-7668409826365482/6093438917"

        // Create ad request.
        val adRequest = AdRequest.Builder().build()
        if (isEnableShowingAds) {
            interstitial!!.loadAd(adRequest)
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
        // Begin loading your interstitial.


        if (isEnableShowGooglePlayReviewIs && checkInternetConnection()) {
            showGooglePlayReviewDialog()
        }
    }

    fun showNextPage() {
        val childCount = mSectionsPagerAdapter.count
        val currItem = mViewPager!!.currentItem
        if (currItem == childCount - 1) {
            PreviousGameInfoActivity.mDeck = null
            showLastCardDialog()
        } else {
            scroller!!.setFixedDuration(1000)
            mViewPager!!.setCurrentItem(currItem + 1, true)
            scroller!!.setFixedDuration(null)
        }
    }

    private fun showGooglePlayReviewDialog() {
        mAdsView!!.visibility = View.INVISIBLE
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.are_you_like_app)
        builder.setMessage(R.string.review_app)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.review_now) { arg0, arg1 ->
            val appPackageName = packageName // getPackageName() from Context or Activity object
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
            }

            Utils.setIsEnableRateAppToFalse(this@ShowUserCartActivity)
        }
        builder.setNeutralButton(R.string.later_review) { dialog, which -> Utils.incrementRateAfter(this@ShowUserCartActivity) }

        builder.setNegativeButton(R.string.never_review) { dialog, which ->
            Utils.setIsEnableRateAppToFalse(this@ShowUserCartActivity)
            mAdsView!!.visibility = View.VISIBLE
        }

        builder.show()
    }

    private fun showLastCardDialog() {
        GlobalScope.launch(IO) {
            InjectorUtils.getDeckRepository(this@ShowUserCartActivity).insertLastUsedDeck(mDeck!!)
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.final_dialog_title)
        builder.setMessage(R.string.final_dialog_message)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.final_dialog_go_to_main_menu) { arg0, arg1 ->
            if (isEnableShowingAds) {
                displayInterstitial()
            }
            Utils.incrementPlayedGameCount(this@ShowUserCartActivity)
            finish()
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

    companion object {

        private var mDeck: Deck? = null

        fun getIntent(context: Context, deck: Deck): Intent {
            mDeck = deck
            return Intent(context, ShowUserCartActivity::class.java)
        }
    }
}
