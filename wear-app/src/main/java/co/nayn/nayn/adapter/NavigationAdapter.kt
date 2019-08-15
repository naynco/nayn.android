package co.nayn.nayn.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.nayn.core.data.model.Category
import co.nayn.nayn.*
import co.nayn.nayn.widgets.NavigationCardView
import java.util.*


class NavigationAdapter : RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {

    val categories = ArrayList<Category>()

    var navigationViewClickListener: NavigationCardView.NavigationViewClickListener? = null


    class ViewHolder(itemView: View, navigationViewClickListener: NavigationCardView.NavigationViewClickListener?) : RecyclerView.ViewHolder(itemView) {
        var navigationCardView: NavigationCardView? = itemView as NavigationCardView

        init {
            navigationCardView?.setOnNavigationViewClickListener(navigationViewClickListener)
        }

        fun bind(category: Category) {
            when (category.name) {
                "Tümü" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_ALL
                    navigationCardView?.onNavigationClick(category)
                }
                "DÜNYA" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_ALL
                    navigationCardView?.onNavigationClick(category)
                }
                "GÜNDEM" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_AGENDA
                    navigationCardView?.onNavigationClick(category)
                }
                "SANAT" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_ART
                    navigationCardView?.onNavigationClick(category)
                }
                "SICAK" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_HOT
                    navigationCardView?.onNavigationClick(category)
                }
                "SOSYAL" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_SOCIAL
                    navigationCardView?.onNavigationClick(category)
                }
                "SPOR" -> {
                    navigationCardView?.setNavCardText(category.name)
                    navigationCardView?.setBackgroundResourceAsShape(category.colorCodeStart, category.colorCodeEnd)
                    category.position = INDEX_SPORT
                    navigationCardView?.onNavigationClick(category)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.navigation_drawer_view_holder, parent, false)

        return NavigationAdapter.ViewHolder(itemView, navigationViewClickListener)
    }

    override fun onBindViewHolder(holder: NavigationAdapter.ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setSelected(position: Int) {
        for (i in categories.indices) {
            categories[i].isSelected = i == position
        }
        notifyDataSetChanged()
    }

    fun addCategories(categories: ArrayList<Category>?) {
        this.categories.clear()
        this.categories.addAll(categories!!.asIterable())
        notifyDataSetChanged()
    }

}