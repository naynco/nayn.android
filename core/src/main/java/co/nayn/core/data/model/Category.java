package co.nayn.core.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("color_code_start")
    @Expose
    private String colorCodeStart;
    @SerializedName("color_code_end")
    @Expose
    private String colorCodeEnd;
    @SerializedName("url")
    @Expose
    private String url;

    private int position;
    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCodeStart() {
        return "#"+colorCodeStart;
    }

    public void setColorCodeStart(String colorCodeStart) {
        this.colorCodeStart = colorCodeStart;
    }

    public String getColorCodeEnd() {
        return "#"+colorCodeEnd;
    }

    public void setColorCodeEnd(String colorCodeEnd) {
        this.colorCodeEnd = colorCodeEnd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.colorCodeStart);
        dest.writeString(this.colorCodeEnd);
        dest.writeString(this.url);
        dest.writeInt(this.position);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.colorCodeStart = in.readString();
        this.colorCodeEnd = in.readString();
        this.url = in.readString();
        this.position = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
