package co.nayn.nayn.data.local.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import co.nayn.core.data.model.Attachment;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Images;
import co.nayn.core.data.model.Posts;

@Entity
public class SavedNew implements Parcelable {

    @PrimaryKey
    private int id;
    private String title;
    private String summary;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String url;
    private ArrayList<Category> categories;
    private Images images;
    private ArrayList<Attachment> attachments;

    public SavedNew(){

    }

    public SavedNew(Posts post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.summary = post.getSummary();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.url = post.getUrl();
        this.categories = post.getCategories();
        this.images = post.getImages();
        this.attachments = post.getAttachments();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }



    public Posts toPost(){
        Posts posts = new Posts();
        posts.setId(id);
        posts.setTitle(title);
        posts.setSummary(summary);
        posts.setContent(content);
        posts.setCreatedAt(createdAt);
        posts.setUpdatedAt(updatedAt);
        posts.setUrl(url);
        posts.setImages(images);
        posts.setAttachments(attachments);
        posts.setCategories(categories);
        return posts;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.content);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.url);
        dest.writeList(this.categories);
        dest.writeSerializable(this.images);
        dest.writeList(this.attachments);
    }

    protected SavedNew(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.summary = in.readString();
        this.content = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.url = in.readString();
        this.categories = new ArrayList<>();
        in.readList(this.categories, Category.class.getClassLoader());
        this.images = (Images) in.readSerializable();
        this.attachments = new ArrayList<>();
        in.readList(this.attachments, Attachment.class.getClassLoader());
    }

    public static final Parcelable.Creator<SavedNew> CREATOR = new Parcelable.Creator<SavedNew>() {
        @Override
        public SavedNew createFromParcel(Parcel source) {
            return new SavedNew(source);
        }

        @Override
        public SavedNew[] newArray(int size) {
            return new SavedNew[size];
        }
    };
}
