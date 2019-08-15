package co.nayn.nayn

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.text.Html
import co.nayn.core.data.model.Posts
import co.nayn.core.data.remote.NaynDataSource
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news.*


class NewsActivity : WearableActivity() {

    private lateinit var dataSource: NaynDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = (application as App).dataSource
        setContentView(R.layout.activity_news)
        // Enables Always-on
        setAmbientEnabled()
        setContent()
        adjustInset()
    }

    fun setContent() {
        val post: Posts = intent.getStringExtra("post").toPosts()!!
        text?.text = Html.fromHtml(post.content, ImageGetter(), null)
        title_news?.text = post.title
        Picasso.get().load(post?.images?.medium).into(image)
    }

    class ImageGetter : Html.ImageGetter {
        override fun getDrawable(source: String): Drawable? {
            return BitmapDrawable()
        }
    }


    private fun adjustInset() {

        if (applicationContext.resources.configuration.isScreenRound) {

            val inset = (FACTOR * Resources.getSystem().displayMetrics.widthPixels).toInt()

            text.setPadding(inset, inset / 4, inset, inset / 4)
            title_news.setPadding(inset, inset, inset, inset / 4)


        }


    }


    companion object {

        private const val FACTOR = 0.146467f

    }


}
