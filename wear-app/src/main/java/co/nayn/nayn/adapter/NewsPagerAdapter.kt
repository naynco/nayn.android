package co.nayn.nayn.adapter

import android.graphics.drawable.GradientDrawable
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.nayn.core.data.model.Category
import co.nayn.core.data.model.Posts
import co.nayn.nayn.R
import co.nayn.nayn.widgets.CustomFontTextView
import co.nayn.nayn.widgets.NaynGradientButton
import com.squareup.picasso.Picasso
import org.jetbrains.anko.layoutInflater

class NewsPagerAdapter : ViewPagerAdapter() {

    var posts: ArrayList<Posts>? = ArrayList()

    override fun getView(position: Int, pager: ViewPager?): View? {
        val v = pager?.context?.layoutInflater?.inflate(R.layout.news_item, pager, false)
        var category: Category? = posts?.get(position)?.categories?.get(0)
        var post: Posts? = posts?.get(position)

        Picasso.get().load(post?.images?.medium).into(v?.findViewById<ImageView>(R.id.main_news_image))
        v?.findViewById<CustomFontTextView>(R.id.main_news_text)?.text = post?.title
        v?.findViewById<TextView>(R.id.main_news_time_text)?.text = post?.createdAt

        val colorCodeStart = category?.colorCodeStart
        val colorCodeEnd = category?.colorCodeEnd
        val categoryName = category?.name
        if (!colorCodeEnd!!.contains("null") || !colorCodeStart!!.contains("null")) {
            var gradientButton = v?.findViewById<NaynGradientButton>(R.id.main_text_category_button)
            gradientButton?.setCornerRadius(pager?.resources?.getDimensionPixelSize(R.dimen.button_radius)!!.toFloat())
            gradientButton?.setFirstColor(colorCodeStart)
            gradientButton?.setSecondColor(colorCodeEnd)
            gradientButton?.text = categoryName
            gradientButton?.setGradientOrientation(GradientDrawable.Orientation.LEFT_RIGHT)
            gradientButton?.setGradientColorsWithRadii()
        }

        return v
    }


    fun setNews(posts: ArrayList<Posts>?) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun addNews(posts: ArrayList<Posts>?) {
        this.posts?.addAll(posts!!.asIterable())
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return posts!!.size
    }


}