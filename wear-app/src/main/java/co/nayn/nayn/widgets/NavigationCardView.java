package co.nayn.nayn.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.nayn.core.data.model.Category;
import co.nayn.nayn.R;

public class NavigationCardView extends CardView{

    LinearLayout mainLayout;
    TextView navCardText;
    Button iconButton;


    @ColorInt
    private int firstColor;
    @ColorInt
    private int secondColor;
    GradientDrawable.Orientation gradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT;

    private String title;
    private NavigationViewClickListener navigationViewClickListener;


    public NavigationCardView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NavigationCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parserAttrs(context,attrs);
        init(context);
    }

    public NavigationCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parserAttrs(context,attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.navigation_drawer_card_item, this);
        bind();
    }

    private void bind(){
        mainLayout = findViewById(R.id.nav_drawer_main_layout);
        navCardText = findViewById(R.id.nav_card_textview);
        iconButton = findViewById(R.id.icon_button);
        navCardText.setText(title);
        setBackgroundResourceAsShape(firstColor,secondColor);
    }

    private void parserAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NavigationCardView,
                0, 0);

        try {
            firstColor = a.getColor(R.styleable.NavigationCardView_firstColor, Color.BLACK);
            secondColor = a.getColor(R.styleable.NavigationCardView_secondColor, Color.BLACK);
            title = a.getString(R.styleable.NavigationCardView_titleText);
        } finally {
            a.recycle();
        }
    }

    public void setGradientDrawableBackgroundColor(@ColorInt int firstColor, @ColorInt int secondColor){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {firstColor,secondColor});

        mainLayout.setBackground(gradientDrawable);
    }

    public void setGradientBackgroundDrawable(@DrawableRes int drawableRes){
        if(mainLayout != null){
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mainLayout.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), drawableRes));
            } else {
                mainLayout.setBackground(ContextCompat.getDrawable(this.getContext(),drawableRes));
            }
        }
    }

    public void setBackgroundResourceAsShape(@ColorInt int firstColor, @ColorInt int secondColor){
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        GradientDrawable shape = new GradientDrawable();
        shape.setOrientation(gradientOrientation);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColors(new int[]{firstColor,secondColor});
        mainLayout.setBackground(shape);
    }

    public void setBackgroundResourceAsShape(String firstColor, String secondColor){
        GradientDrawable shape = new GradientDrawable();
        shape.setOrientation(gradientOrientation);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColors(new int[]{Color.parseColor(firstColor),Color.parseColor(secondColor)});
        mainLayout.setBackground(shape);
    }

    public void setNavCardTextColor(@ColorInt int color){
        if(navCardText != null){
            navCardText.setTextColor(color);
        }

    }

    public void setNavCardText(String text){
        if(navCardText != null)
        navCardText.setText(text);
    }


    public void setOnNavigationViewClickListener(NavigationViewClickListener navigationViewClickListener){
        this.navigationViewClickListener = navigationViewClickListener;
    }


    public void onNavigationClick(Category category){
        if(mainLayout != null){
            mainLayout.setOnClickListener(view -> {
                navigationViewClickListener.onNavigationClick(category);
            });
        }

    }

    public interface NavigationViewClickListener{
        void onNavigationClick(Category category);
    }


    public void setIconButtonVisibility(int visibilityCode){
        iconButton.setVisibility(visibilityCode);
    }

}
