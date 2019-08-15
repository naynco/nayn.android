package co.nayn.nayn.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import co.nayn.nayn.R;
import co.nayn.nayn.base.BaseActivity;
import co.nayn.nayn.ui.main.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static co.nayn.nayn.ui.main.MainActivity.EXTRA_POST_ID;

public class SplashActivity extends BaseActivity {

    @Inject
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getData() != null) {
            Log.i("SplashActivity", "URI" + getIntent().getData().toString());
            tryToGetShortLinkFromUri(getIntent().getData().toString());
        } else {
            changeNextActivity();
        }
    }


    private void tryToGetShortLinkFromUri(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> changeNextActivity());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        changeNextActivity(parseId(response.body().string()));
                    } catch (Exception e) {
                        changeNextActivity();
                    }
                });
            }
        });
    }

    private String parseId(String string) {
        final String regex = "(<link rel='shortlink' href='https:\\/\\/nayn\\.co\\/\\?p=([0-9]*))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        String id = null;
        while (matcher.find()) {
            Log.i("REGEX", "Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                Log.i("REGEX", "Group " + i + ": " + matcher.group(i));
                id = matcher.group(i);
            }
        }
        return id;
    }

    private void changeStatusBarColor() {
        changeStatusBarColor(R.color.toolbar_black_color);
    }

    private void changeNextActivity() {
        changeStatusBarColor();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1500);
    }

    private void changeNextActivity(String postId) {
        changeStatusBarColor();
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        i.putExtra(EXTRA_POST_ID, postId);
        startActivity(i);
        finish();
    }
}

