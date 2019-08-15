package co.nayn.nayn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import co.nayn.core.data.model.Category
import co.nayn.core.data.remote.NaynDataSource
import co.nayn.nayn.adapter.NavigationAdapter
import co.nayn.nayn.widgets.NavigationCardView
import kotlinx.android.synthetic.main.activity_categories.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CategoriesActivity : WearableActivity(), NavigationCardView.NavigationViewClickListener {

    private lateinit var dataSource: NaynDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = (application as App).dataSource
        setContentView(R.layout.activity_categories)
        // Enables Always-on
        setAmbientEnabled()
        setNavigation()
    }

    fun setNavigation() {
        progress.visibility = VISIBLE

        var adapter = NavigationAdapter()
        adapter.navigationViewClickListener = this
        navigation.layoutManager = LinearLayoutManager(this)
        navigation.adapter = adapter
        navigation.setHasFixedSize(true)
        doAsync {
            try {
                val response = dataSource.newsCategories.execute()
                uiThread {
                    if (response.isSuccessful) {
                        adapter.addCategories(response.body()?.data);
                    } else {
                        Toast.makeText(this@CategoriesActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                    progress.visibility = GONE
                }
            }catch (e:Exception){
                finish()
            }
        }

    }

    override fun onNavigationClick(category: Category) {
        var intent: Intent = Intent()
        intent.putExtra("category", category.id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
