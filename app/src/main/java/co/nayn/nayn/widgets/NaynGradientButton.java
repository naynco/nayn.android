package co.nayn.nayn.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.Dimension;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import co.nayn.nayn.R;

public class NaynGradientButton extends AppCompatButton{

    private GradientDrawable.Orientation gradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT;

    @Dimension
    float cornerRadius;

    String firstColor;
    String secondColor;

    public NaynGradientButton(Context context) {
        super(context);
    }

    public NaynGradientButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NaynGradientButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet parserAttrs){
        if (parserAttrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(parserAttrs, R.styleable.NaynGradientButton);
                a.recycle();
            }
    }

    public void setGradientOrientation(GradientDrawable.Orientation gradientOrientation){
        this.gradientOrientation = gradientOrientation;
    }

    public void setCornerRadius(@Dimension float cornerRadius){
        this.cornerRadius = cornerRadius;
    }


   public void setFirstColor(String firstColor){
        this.firstColor = firstColor;
   }

   public void setSecondColor(String secondColor){
        this.secondColor = secondColor;
   }

   public void setGradientColorsWithRadii(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                }
            });
        }
        GradientDrawable shape = new GradientDrawable();
        shape.setOrientation(gradientOrientation);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColors(new int[]{parseColor(firstColor),parseColor(secondColor)});
        shape.setCornerRadii(new float[] { cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius });
        this.setBackground(shape);
    }

    int parseColor(String color){
        try{
            return Color.parseColor(color);
        }catch(Exception e){
            return 0;
        }
    }

    public void setGradientColorsWithRadii(@Dimension float cornerRadius, String firstColor, String secondColor, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                }
            });
        }
        setText(text);
        GradientDrawable shape = new GradientDrawable();
        shape.setOrientation(gradientOrientation);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColors(new int[]{parseColor(firstColor),parseColor(secondColor)});
        shape.setCornerRadii(new float[] { cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius });
        this.setBackground(shape);
    }
}
