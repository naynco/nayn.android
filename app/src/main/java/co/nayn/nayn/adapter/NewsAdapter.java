package co.nayn.nayn.adapter;

import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.nayn.nayn.R;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Posts;
import co.nayn.nayn.widgets.MainNewsView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<Posts> newsPosts = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private MainNewsView.MainNewsViewClickListener mainNewsViewClickListener;

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_news_card_item_view_holder, parent, false);

        return new NewsAdapter.NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
            Posts post = newsPosts.get(position);
            Category category = newsPosts.get(position).getCategories().get(0);
            holder.bind(post,category);
    }

    @Override
    public int getItemCount() {
        return  newsPosts == null ? 0 : newsPosts.size();
    }

    public void setMainNewsViewClickListener(MainNewsView.MainNewsViewClickListener mainNewsViewClickListener){
        this.mainNewsViewClickListener = mainNewsViewClickListener;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.main_news_view)
        MainNewsView mainNewsView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Posts post, Category category){
            if(mainNewsViewClickListener != null){
                mainNewsView.setMainNewsViewClickListener(mainNewsViewClickListener);
                mainNewsView.onMainNewsClick(post);
            }
            String colorCodeStart = category.getColorCodeStart();
            String colorCodeEnd = category.getColorCodeEnd();
            String categoryName = category.getName();
            if(!colorCodeEnd.contains("null") || !colorCodeStart.contains("null")){
                mainNewsView.setCategoryButton(GradientDrawable.Orientation.LEFT_RIGHT,categoryName, colorCodeStart,colorCodeEnd,itemView.getResources().getDimensionPixelSize(R.dimen.button_radius));

            }
            mainNewsView.setMainNewsTimeText(post.getCreatedAt());
            mainNewsView.setMainNewsImage(post.getImages());
            mainNewsView.setMainNewsText(post.getTitle());
        }
    }

    public void addAllPosts(ArrayList<Posts> posts){
        if(newsPosts != null){
            if(this.newsPosts.isEmpty()){
                this.newsPosts.addAll(posts);
                notifyDataSetChanged();
            }else if(!this.newsPosts.isEmpty() && !this.newsPosts.containsAll(posts)){
               this.newsPosts.addAll(posts);
               this.notifyDataSetChanged();
            }

        }

    }

    public void addAllCategories(ArrayList<Category> categories){
        Category category = new Category();
        category.setColorCodeStart("1F1F1F");
        category.setColorCodeEnd("1F1F1F");
        category.setName("Tümü");
        category.setPosition(0);
        category.setId(10);
        this.categories.add(category);
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void clearPosts(){
        newsPosts.clear();
        notifyDataSetChanged();
    }




}
