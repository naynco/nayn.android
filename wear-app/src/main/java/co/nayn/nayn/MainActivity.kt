package co.nayn.nayn

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import co.nayn.core.NaynConstants
import co.nayn.core.data.model.Posts
import co.nayn.core.data.model.ResponseModel
import co.nayn.core.data.remote.NaynDataSource
import co.nayn.nayn.adapter.NewsPagerAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response
import java.lang.Math.abs

class MainActivity : WearableActivity(), ViewPager.OnPageChangeListener {
    private lateinit var dataSource: NaynDataSource
    private lateinit var pagerAdapter: NewsPagerAdapter
    private val CATEGORY_SELECT: Int = 123;

    private var categoryID: Int? = 10
    private var pageNumber = 1
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = (application as App).dataSource
        setContentView(R.layout.activity_main)
        // Enables Always-on
        setAmbientEnabled()
        refreshNews()

        pager.addOnPageChangeListener(this)

        pager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x1 = p1.x
                        y1 = p1.y
                        x1_time = System.currentTimeMillis()
                    }
                    MotionEvent.ACTION_UP -> {
                        x2 = p1.x
                        y2 = p1.y
                        x2_time = System.currentTimeMillis()
                        val deltaTime = x2_time - x1_time
                        val deltaX = x2 - x1
                        val deltaY = y2 - y1
                        if (Math.abs(deltaX) > 120) { //swipe left to right
                            onClickDrawer(null)
                        } else {
                            Log.i("DELTA", "y:$deltaY,x:$deltaX,deltaTime:$deltaTime")
                            if (deltaTime > 60 && abs(deltaY) <= 3) { //onClick
                                startNewsDetail(pagerAdapter.posts?.get(pager.currentItem))
                            }
                        }

                    }
                }
                return false
            }
        })

    }

    fun startNewsDetail(post: Posts?) {
        val intent = Intent(this, NewsActivity::class.java)
        intent.putExtra("post", post.toJson())
        startActivity(intent)
    }

    fun onClickDrawer(v: View?) {
        startActivityForResult(Intent(this, CategoriesActivity::class.java), CATEGORY_SELECT, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CATEGORY_SELECT && resultCode == Activity.RESULT_OK) {
            categoryID = data?.getIntExtra("category", 0)
            refreshNews()
        }
    }

    private fun refreshNews() {
        pageNumber = 0
        progress.visibility = VISIBLE
        hideConnectionError()
        doAsync {
            try {
                var response: Response<ResponseModel<ArrayList<Posts>>> = if (categoryID == 10) {
                    dataSource.getAllPosts(pageNumber, limit).execute()
                } else {
                    dataSource.getPostsByCategory(NaynConstants.POSTS_BY_CATEGORY + categoryID.toString(), pageNumber).execute()
                }
                uiThread {
                    progress.visibility = GONE
                    if (response.isSuccessful) {
                        setPager(response.body()?.data)
                    } else {
                        Toast.makeText(this@MainActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    progress.visibility = GONE
                    showConnectionError()
                }
            }
        }
    }

    private fun showConnectionError(){
        error_section.visibility = VISIBLE
        try_again.setOnClickListener {
            hideConnectionError()
            progress.visibility = VISIBLE
            Handler().postDelayed({
                refreshNews()
            },2000)

        }
    }

    private fun hideConnectionError(){
        error_section.visibility = GONE
        try_again.setOnClickListener {
            refreshNews()
        }
    }

    private fun moreNews() {
        pageNumber++
        progress.visibility = VISIBLE;
        doAsync {

            var response: Response<ResponseModel<ArrayList<Posts>>> = if (categoryID == 10) {
                dataSource.getAllPosts(pageNumber, limit).execute()
            } else {
                dataSource.getPostsByCategory(NaynConstants.POSTS_BY_CATEGORY + categoryID.toString(), pageNumber).execute()
            }

            uiThread {
                progress.visibility = GONE;
                if (response.isSuccessful) {
                    addNews(response.body()?.data)
                }
            }
        }

    }

    private fun setPager(posts: ArrayList<Posts>?) {
        pagerAdapter = NewsPagerAdapter()
        pagerAdapter.setNews(posts)
        pager.adapter = pagerAdapter
    }

    private fun addNews(posts: ArrayList<Posts>?) {
        pagerAdapter.addNews(posts)
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
        if (pagerAdapter.count - 1 == p0) {
            moreNews()
        }
    }

    var x1: Float = 0f
    var y1: Float = 0f
    var x2: Float = 0f
    var y2: Float = 0f
    var x1_time: Long = 0L
    var x2_time: Long = 0L

}

fun Posts?.toJson(): String? {
    return Gson().toJson(this)
}

fun String?.toPosts(): Posts? {
    return Gson().fromJson(this, Posts::class.java)
}
