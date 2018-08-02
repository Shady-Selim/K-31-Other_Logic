import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.trabiza.app.Helpers.GifHelper.GifWebView;
import com.trabiza.app.Helpers.SharedPrefrenceHelper.SharedPrefHelper;
import com.trabiza.app.R;
import com.trabiza.app.Views.RevealAnimation;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    private Intent objIntent;
    private ImageView /*ivLogo,*/ivAds;
    private WebView webAds;
    private String photoURL;
    private LinearLayout animationContainer;
    private final String htmlbegin = "<div style=\"color:#e48a00 ;  \">";
    private final String htmlend = " </div>";
    private LinearLayout rlContainer;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//we use it to support svg imagr in android 4
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        //ivLogo=(ImageView)findViewById(R.id.ivLogo);
        webAds=(WebView)findViewById(R.id.webAds);
        ivAds=(ImageView)findViewById(R.id.ivAds);
        rlContainer=(LinearLayout)findViewById(R.id.rlContainer);
        //ivLogo.setImageResource(R.drawable.ic_logosplash);

        //MultiDex.install(this);
        animationContainer = (LinearLayout) findViewById(R.id.animationContainer);
        GifWebView view = new GifWebView(this, "file:///android_asset/splashAnim.gif");
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        animationContainer.addView(view);

        //getSplash();
       new Handler().postDelayed(new Runnable() {
            public void run() {
                try {

                    String token = SharedPrefHelper.getSharedString(getApplicationContext(), SharedPrefHelper.SHARED_PREFERENCE_USER_TOKEN);
                    if (token != null && token.length() > 0 && !token.isEmpty())
                    {
                      /*  objIntent = new Intent(getApplicationContext(), MainActivity.class);
                        objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(objIntent);*/
                        startRevealActivity(rlContainer);

                    }
                    else
                    {
                        startRevealActivity(rlContainer);
                        /*objIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(objIntent);
                        finish();*/
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, 3000);
    }

   /* private void getSplash()
    {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getSplash();
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        SplashAPI objData = new Gson().fromJson(response.body(), SplashAPI.class);
                        if (objData != null&&objData.response==true) {
                            photoURL= AppConfigHelper.PHOTO_URL + objData.data;

                            if(photoURL.isEmpty()||photoURL==null||photoURL.equals("")||photoURL.length()<=0)
                            {
                                ivAds.setVisibility(View.GONE);
                                webAds.setVisibility(View.GONE);
                            }
                            else {
                                String path=photoURL.substring(photoURL.length() - 4, photoURL.length()).toLowerCase();
                                if (path.equals(".svg")) {
                                    webAds.setVisibility(View.VISIBLE);//show WebView
                                    ivAds.setVisibility(View.GONE);//hide ImageView

                                    webAds.getSettings().setJavaScriptEnabled(true); // enable javascript
                                    webAds.setWebViewClient(new WebViewClient() {
                                        @SuppressWarnings("deprecation")
                                        @Override
                                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                            Toast.makeText(SplashActivity.this, description, Toast.LENGTH_SHORT).show();
                                        }

                                        @TargetApi(android.os.Build.VERSION_CODES.M)
                                        @Override
                                        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                                            // Redirect to deprecated method, so you can use it in all SDK versions
                                            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                                        }
                                    });

                                    webAds.setBackgroundColor(0);

                                    // body.setDef
                                    webAds.loadDataWithBaseURL(photoURL, htmlbegin + htmlend,
                                            "text/html", "utf-8", photoURL);
                                    webAds.loadUrl(photoURL);
                                }
                                else
                                {
                                    ivAds.setVisibility(View.VISIBLE);//show ImageView
                                    webAds.setVisibility(View.GONE);//hide WebView
                                    Picasso.with(getApplicationContext()).load(AppConfigHelper.PHOTO_URL + objData.data).into(ivAds);
                                }
                            }
                        }
                        else
                        {
                            ivAds.setVisibility(View.GONE);
                            webAds.setVisibility(View.GONE);
                        }

                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        objThrowable.printStackTrace();
                        //DialogsHelper.getAlert(SplashActivity.this, getString(R.string.error), getString(R.string.check_internet_connection), getString(R.string.ok), "", null, null).show();
                    } else {
                        objThrowable.printStackTrace();
                        //DialogsHelper.getAlert(SplashActivity.this, getString(R.string.error), getString(R.string.check_internet_connection), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/
    private void startRevealActivity(View v) {
        try {
            //calculates the center of the View v you are passing
            int revealX = (int) (v.getX() + v.getWidth() / 2);
            int revealY = (int) (v.getY() + v.getHeight() / 2);

            //create an intent, that launches the second activity and pass the x and y coordinates
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

            //just start the activity as an shared transition, but set the options bundle to null
            ActivityCompat.startActivity(this, intent, null);

            //to prevent strange behaviours override the pending transitions
            overridePendingTransition(0, 0);
            finish();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
