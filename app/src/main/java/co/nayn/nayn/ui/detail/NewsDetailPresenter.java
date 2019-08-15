package co.nayn.nayn.ui.detail;

import android.app.Activity;
import androidx.lifecycle.LifecycleObserver;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import co.nayn.nayn.R;

public class NewsDetailPresenter extends NewsDetailContract.Presenter implements LifecycleObserver {

    private Context context;
    private ShareDialog shareDialog;

    public NewsDetailPresenter(NewsDetailContract.View mainView, Context context) {
        super(mainView);
        this.context = context;
        shareDialog = new ShareDialog((Activity) this.context);
    }

    @Override
    public void sharePost(int postId,String title, String URL) {

        switch (postId){

            case R.id.share_fab:
                shareWithFAB(URL,title);
                break;
            case R.id.share_whatsapp_button:
                shareWithWhatsapp(URL,title);
                break;
            case R.id.share_facebook_button:
                shareWithFacebook(URL,title);
                break;
            case R.id.share_twitter_button:
                shareWithTwitter(URL,title);
                break;
            case R.id.share_messenger_button:
                shareWithMessenger(URL,title);
                break;

        }

    }

    @Override
    public void shareWithFAB(String URL, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, URL +"\n" + title);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, ""));
    }

    @Override
    public void shareWithMessenger(String URL, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL + "\n" + title);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        try {
            context.startActivity(sendIntent);
        }catch (ActivityNotFoundException anfe){
            Log.d("MessengerNFE",anfe.getMessage());
        }

    }

    @Override
    public void shareWithWhatsapp(String URL, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL + "\n" + title);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        try {
            context.startActivity(sendIntent);
        }catch (ActivityNotFoundException anfe){
            Log.d("WhatsappNFE",anfe.getMessage());
        }
    }

    @Override
    public void shareWithFacebook(String URL, String title) {
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(URL))
                .setContentTitle(title)
                .setContentDescription(title)
                .build();
        if(ShareDialog.canShow(ShareLinkContent.class)){
            shareDialog.show(shareLinkContent);
        }
    }


    @Override
    public void shareWithTwitter(String URL, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL + "\n" + title);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.twitter.android");
        try {
            context.startActivity(sendIntent);
        }catch (ActivityNotFoundException anfe){
            Log.d("TwitterNFE",anfe.getMessage());
        }
    }

}
