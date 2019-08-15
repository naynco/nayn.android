package co.nayn.nayn.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.nayn.core.data.model.Category;
import co.nayn.nayn.R;
import co.nayn.nayn.constants.NaynConstants;
import co.nayn.nayn.widgets.NavigationCardView;


public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private ArrayList<Category> categories = new ArrayList<>();
    private NavigationCardView.NavigationViewClickListener navigationViewClickListener;

    public NavigationAdapter() {
    }

    public void setOnNavigationViewClickListener(NavigationCardView.NavigationViewClickListener navigationViewClickListener) {
        this.navigationViewClickListener = navigationViewClickListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.navigation_card_view)
        NavigationCardView navigationCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (navigationViewClickListener != null) {
                navigationCardView.setOnNavigationViewClickListener(navigationViewClickListener);
            }

        }

        public void bind(Category category) {
            switch (category.getName()) {
                case "Tümü":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_ALL);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "DÜNYA":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_WORLD);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "GÜNDEM":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_AGENDA);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "SANAT":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_ART);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "SICAK":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_HOT);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "SOSYAL":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_SOCIAL);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "SPOR":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_SPORT);
                    navigationCardView.onNavigationClick(category);
                    break;
                case "Sonra Oku":
                    navigationCardView.setNavCardText(category.getName());
                    navigationCardView.setBackgroundResourceAsShape(category.getColorCodeStart(), category.getColorCodeEnd());
                    category.setPosition(NaynConstants.INDEX_SAVED);
                    navigationCardView.onNavigationClick(category);
                    break;

            }


        }

    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navigation_drawer_view_holder, parent, false);

        return new NavigationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public void setSelected(int position) {
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).setSelected(i == position);
        }

        notifyDataSetChanged();
    }

    public void addCategories(ArrayList<Category> categories) {
        if (this.categories != null && !this.categories.isEmpty()) {
            this.categories.clear();
        } else {
            this.categories.addAll(categories);
        }
        notifyDataSetChanged();
    }


}