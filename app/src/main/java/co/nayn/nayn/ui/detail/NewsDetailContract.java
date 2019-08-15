package co.nayn.nayn.ui.detail;

import co.nayn.core.data.model.Posts;
import co.nayn.nayn.ui.main.MainPresenterImpl;
import co.nayn.nayn.ui.main.MainView;

public interface NewsDetailContract {

    interface View extends MainView {
        void init();
        void initPresenter();
        void initViews();
        void openInsideUrl();
        void sharePost(int buttonId,String title, String URL);
        void fillPost(Posts post);
    }

    abstract class Presenter extends MainPresenterImpl<View> {

        public Presenter(NewsDetailContract.View mainView) {
            super(mainView);
        }


        public abstract void sharePost(int postId,String title, String URL);
        public abstract void shareWithWhatsapp(String URL,String title);
        public abstract void shareWithTwitter(String URL,String title);
        public abstract void shareWithFacebook(String URL,String title);
        public abstract void shareWithMessenger(String URL,String title);
        public abstract void shareWithFAB(String URL,String title);



    }
}

