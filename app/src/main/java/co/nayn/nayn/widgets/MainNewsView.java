package co.nayn.nayn.widgets;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.nayn.nayn.R;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Images;
import co.nayn.core.data.model.Posts;

public class MainNewsView extends CardView {

    @BindView(R.id.main_news_root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.main_news_text)
    CustomFontTextView mainNewsText;
    @BindView(R.id.main_news_time_text)
    CustomFontTextView mainNewsTimeText;
    @BindView(R.id.main_news_image)
    AppCompatImageView mainNewsImage;
    @BindView(R.id.main_text_category_button)
    NaynGradientButton categoryButton;

    private Unbinder unbinder;
    private MainNewsViewClickListener mainNewsViewClickListener;

    public MainNewsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MainNewsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainNewsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.main_news_card_item, this);
        bind();
    }

    private void bind(){
        unbinder = ButterKnife.bind(this);
    }

    public void setCategoryButton(GradientDrawable.Orientation gradientOrientation, String text, String firstColor, String secondColor, float cornerRadius){
        categoryButton.setCornerRadius(cornerRadius);
        categoryButton.setFirstColor(firstColor);
        categoryButton.setSecondColor(secondColor);
        categoryButton.setText(text);
        categoryButton.setGradientOrientation(gradientOrientation);
        categoryButton.setGradientColorsWithRadii();
    }

    public void setMainNewsViewClickListener(MainNewsViewClickListener mainNewsViewClickListener){
        this.mainNewsViewClickListener = mainNewsViewClickListener;
    }

    public void onMainNewsClick(Posts post){
        if(rootLayout != null){
            rootLayout.setOnClickListener(view -> {
                this.mainNewsViewClickListener.onMainNewsClick(post);
            });

            if(categoryButton != null){
                categoryButton.setOnClickListener(view -> {
                    this.mainNewsViewClickListener.onCategoryButtonClicked(post.getCategories());
                });
            }
        }


    }

    public void setMainNewsText(String text){
        if(mainNewsText != null){
            mainNewsText.setText(text);
        }
    }

    public void setMainNewsTimeText(String text){
        if(mainNewsTimeText != null){
            mainNewsTimeText.setText(text);
        }
    }


    public void setMainNewsImage(Images images){
        if(mainNewsImage != null && images != null){
            Picasso .get()
                    .load(images.getLarge())
                    .into(mainNewsImage);
        }
    }


    public interface MainNewsViewClickListener{
        void onMainNewsClick(Posts post);
        void onCategoryButtonClicked(ArrayList<Category> categories);

    }



}
