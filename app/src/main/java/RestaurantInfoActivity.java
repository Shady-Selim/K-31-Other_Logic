import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.trabiza.app.Adapters.RestaurantTabPagerAdapter;
import com.trabiza.app.Dialogs.ReviewDialog;
import com.trabiza.app.Helpers.AppConfigHelper.AppConfigHelper;
import com.trabiza.app.Helpers.DialogsHelper.DialogsHelper;
import com.trabiza.app.Helpers.LanguageHelper.LanguageHelper;
import com.trabiza.app.Helpers.SharedPrefrenceHelper.SharedPrefHelper;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndPoint;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndpointInterface;
import com.trabiza.app.Models.ReservationsModel;
import com.trabiza.app.Models.RestaurantAPI;
import com.trabiza.app.Models.User;
import com.trabiza.app.R;
import com.trabiza.app.Views.CirclePageIndicator;
import com.trabiza.app.Views.CustomAppButton;
import com.trabiza.app.Views.CustomTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back_arrow, ivRestaurant;
    private ViewPager viewPagerImage, mViewPagerTab;
    private CirclePageIndicator mIndicator;
    private ResutaurantImagePagerAdapter adapter;
    private TabLayout tabLayout;
    private ImageView ivFav, ivShare,ivImages;
    private String restaurantID, restaurantName;
    public CustomTextView tvName,tvImages, tvPay1, tvPay2, tvPay3, tvPay4, tvName1;
    private CustomAppButton btnBook;
    private NestedScrollView nestScroll;
    private ReservationsModel reservationsModel = new ReservationsModel();
    private RestaurantAPI objData;
    private ImageLoader objImageLoader = ImageLoader.getInstance();
    private ArrayList<String> restaurantGallery = new ArrayList<String>();
    private String menu, token, isOnline = "";
    private ProgressBar pbar;
    private boolean isFav = false, favStatus = false;
    //private LinearLayout lyPay;
    private int imageCountPager = 0;
    private Timer timerImageSlider = new Timer();
    private AppBarLayout appbar;
    private ImagePopup imagePopup;
    private RelativeLayout rlHeader;
    private CollapsingToolbarLayout collapsingToolbar;
    private Bitmap bitmapResultImage;
    private ImageView ivRestaurantGallery;
    private Toolbar maintoolbar,maintoolbar2;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.6f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private View customActionBarView;
    private LinearLayout lyContainTools,lyImages;
    //private Bundle extras;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//we use it to support svg imagr in android 4
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this, true);//set the selected language settings
        setContentView(R.layout.activity_restaurant_info);
       // supportPostponeEnterTransition();
        initViews();
        setViewsNumber();//set number of views of place(restaurant-coffeshop) now
    }

    private void initViews() {
        try {
            //init Views
           // iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);
            // ivRestaurant=(ImageView)findViewById(R.id.ivRestaurant);

            viewPagerImage = (ViewPager) findViewById(R.id.pager);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            lyImages=(LinearLayout)findViewById(R.id.lyImages);
            tvImages=(CustomTextView) findViewById(R.id.tvImgaes);
            ivImages=(ImageView) findViewById(R.id.ivImages);
           /* ivFav = (ImageView) findViewById(R.id.ivFav);
            ivFav.setOnClickListener(this);
            ivShare = (ImageView) findViewById(R.id.ivShare);
            ivShare.setOnClickListener(this);
            tvName = (TextView) findViewById(R.id.tvName);*/
           tvName1 = (CustomTextView) findViewById(R.id.tvName1);
            //tvCoisneType = (TextView) findViewById(R.id.tvCoisneType);
           // tvPay1 = (TextView) findViewById(R.id.tvPay1);
           // tvPay2 = (TextView) findViewById(R.id.tvPay2);
            //tvPay3 = (TextView) findViewById(R.id.tvPay3);
            //tvPay4 = (TextView) findViewById(R.id.tvPay4);
            btnBook = (CustomAppButton) findViewById(R.id.btnBook);
            btnBook.setOnClickListener(this);
            pbar = (ProgressBar) findViewById(R.id.pbar);
            //lyPay = (LinearLayout) findViewById(R.id.lyPay);
            appbar = (AppBarLayout) findViewById(R.id.appbar);
            rlHeader=(RelativeLayout)findViewById(R.id.rlHeader);
            maintoolbar=(Toolbar)findViewById(R.id.maintoolbar);
            maintoolbar2=(Toolbar)findViewById(R.id.maintoolbar2);
            setSupportActionBar(maintoolbar2);
            //getSupportActionBar().setHomeButtonEnabled(true);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //set Custom toolBar
            //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //getSupportActionBar().setDisplayShowCustomEnabled(true);

            //getSupportActionBar().setCustomView(R.layout.custom_toolbar_info_layout);
            //customActionBarView=getSupportActionBar().getCustomView();
            iv_back_arrow = (ImageView)findViewById(R.id.iv_back_arrow);
            ivFav = (ImageView)findViewById(R.id.ivFav);

            ivShare = (ImageView) findViewById(R.id.ivShare);
            lyContainTools=(LinearLayout)findViewById(R.id.lyContainTools);

            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
           // collapsingToolbar.setTitle(getString(R.string.app_name));
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);

            //init click Listeners
            iv_back_arrow.setOnClickListener(this);
            ivFav.setOnClickListener(this);
            ivShare.setOnClickListener(this);

            //get user token
            token = SharedPrefHelper.getSharedString(getApplicationContext(), SharedPrefHelper.SHARED_PREFERENCE_USER_TOKEN);

            //in arabic and English Language
            if (Locale.getDefault().getLanguage().equals("ar")) {
                //change arrow_back Image view in arabic Language
                iv_back_arrow.setImageResource(R.drawable.ic_keyboard_right_arrow_button);
                //tvName.setGravity(Gravity.RIGHT);
                lyContainTools.setGravity(Gravity.LEFT);

            } else {
                //change arrow_back Image view in english Language
                iv_back_arrow.setImageResource(R.drawable.ic_left_arrow_key);
                //tvName.setGravity(Gravity.LEFT);
                lyContainTools.setGravity(Gravity.RIGHT);


            }
            if(getIntent().hasExtra(AppConfigHelper.REVIEW_DIALOG_INTENT))
            {
                restaurantID = getIntent().getExtras().getString(AppConfigHelper.RESTAURANT_ID);
                getRestaurantProfileData(Integer.parseInt(restaurantID));
                showReviewDialog(restaurantID);

            }

            else {
                //set Fav Icon
                if (getIntent().getExtras().getBoolean(AppConfigHelper.RESTAURANT_FAV) == true) {
                    ivFav.setImageResource(R.drawable.ic_favoritewhite);

                } else {
                    ivFav.setImageResource(R.drawable.ic_heart_01);
                }
                restaurantID = getIntent().getExtras().getString(AppConfigHelper.RESTAURANT_ID);

                if (getIntent().hasExtra(AppConfigHelper.FAVOURITE_STATUS)) {
                    //to make favourite/unfavourite for user after back from login
                    if (getIntent().getExtras().getBoolean(AppConfigHelper.FAVOURITE_STATUS) == true) {
                        checkFavourite();
                    }
                }
                //get Restaurant Profile Data
                getRestaurantProfileData(Integer.parseInt(restaurantID));
            }

            btnBook.setEnabled(true);
            isOnline = restaurantID;

            //to change restaurant name direction on scroll AppBarLayout
            appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    try {
                        if (Math.abs(verticalOffset) < (appbar.getTotalScrollRange())) {
                            // Make your TextView Visible here
                           /* tvName.setVisibility(View.VISIBLE);
                            tvName1.setVisibility(View.INVISIBLE);*/
                            collapsingToolbar.setTitle(objData.data.get(0).restaurant_info.name);
                        } else {
                            // Make your TextView Invisible here
                           /* tvName.setVisibility(View.GONE);
                            tvName1.setVisibility(View.VISIBLE);*/
                            if(objData.data.get(0).restaurant_info.name.length()<=15) {
                                collapsingToolbar.setTitle(objData.data.get(0).restaurant_info.name);
                            }
                            else
                            {
                                collapsingToolbar.setTitle(objData.data.get(0).restaurant_info.name.substring(0,15)+"...");
                            }

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });

           //make status bar transparent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow(); // in Activity's onCreate() for instance
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }

            /* supportPostponeEnterTransition();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Fade fade = new Fade();
                fade.excludeTarget(R.id.appbar, true);
                fade.excludeTarget(android.R.id.statusBarBackground, true);
                fade.excludeTarget(android.R.id.navigationBarBackground, true);

                getWindow().setEnterTransition(fade);
                getWindow().setExitTransition(fade);
            }

            extras = getIntent().getExtras();
            String animalItem = extras.getString(AppConfigHelper.RESTAURANT_ID);
            String name = extras.getString(AppConfigHelper.RESTAURANT_NAME);
            collapsingToolbar.setTitle(name);

            String imageUrl = animalItem;*/


        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_arrow:
                //to change
                try {
                    if (!isOnline.equals("") && !isOnline.isEmpty() && isOnline.length() > 0 && isOnline != null) {
                        setPlaceOFFLINE();//set place(restaurant-coffeshop) views offline
                        isOnline = "";
                    }
                    if (getIntent().hasExtra(AppConfigHelper.FAVOURITE_STATUS)) {
                        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                        objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(objIntent);
                    } else {
                        finish();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.tvName1:
                try {
                    if (!isOnline.equals("") && !isOnline.isEmpty() && isOnline.length() > 0 && isOnline != null) {
                        setPlaceOFFLINE();//set place(restaurant-coffeshop) views offline
                        isOnline = "";
                    }
                    if (getIntent().hasExtra(AppConfigHelper.FAVOURITE_STATUS)) {
                        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                        objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(objIntent);
                    } else {
                        finish();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ivFav:
                try {
                    checkFavourite();
                } catch (Exception objException) {
                    objException.printStackTrace();
                }
                //validateData();
                break;
            case R.id.ivShare:
                try {
                    share();
                } catch (Exception objException) {
                    objException.printStackTrace();
                }
                //validateData();
                break;
            case R.id.btnBook:
                try {
                    //must put serialze data because of EditReservationDetails put ReservationsModel object in intent
                    //reservationsModel.setRestaurantID(restaurantID);
                    Intent objIntent = new Intent(RestaurantInfoActivity.this, ReserveTrabizaActivity.class);
                    objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                  /*  Bundle objBundle = new Bundle();
                    objBundle.putSerializable(AppConfigHelper.RESERVATION_DETAILS,reservationsModel);
                    objIntent.putExtras(objBundle);
                    objIntent.putExtra(AppConfigHelper.RESERVATION_DETAILS_STATUS,false);*/
                    objIntent.putExtra(AppConfigHelper.RESTAURANT_NAME, restaurantName);
                    objIntent.putExtra(AppConfigHelper.RESTAURANT_ID, restaurantID);
                    startActivity(objIntent);
                } catch (Exception objException) {
                    objException.printStackTrace();
                }
                break;
        }
    }


    private void setUpTabLayoutItems() {
        try {

            tabLayout = (TabLayout) findViewById(R.id.tabsRestaurant);
            //Add Tabs Items
            //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_reserve)));
        /*    if(!menu.equals("N")) {
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_info)));
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_menu)));
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_reviews)));
            }
            else
            {*/
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_info)));
            // tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_menu)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.restaurant_reviews)));
            // }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setSelected(true);

            mViewPagerTab = (ViewPager) findViewById(R.id.tabPager);
            RestaurantTabPagerAdapter adapter = new RestaurantTabPagerAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount(), restaurantID);
            mViewPagerTab.setAdapter(adapter);
           // tabLayout.setupWithViewPager(mViewPagerTab);
            mViewPagerTab.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    mViewPagerTab.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mViewPagerTab.setCurrentItem(tab.getPosition());
                }
            });
        } catch (Exception objException) {
            objException.printStackTrace();
        }

    }

    public class ResutaurantImagePagerAdapter extends PagerAdapter {

        private ArrayList<String> mImages;

        Context mContext;
        LayoutInflater mLayoutInflater;

        public ResutaurantImagePagerAdapter(ArrayList<String> images, Context context) {
            mImages = images;
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Context context = RestaurantInfoActivity.this;

            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ivRestaurantGallery = (ImageView) itemView.findViewById(R.id.ivRestaurant);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String imageTransitionName = extras.getString(AppConfigHelper.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
                ivRestaurantGallery.setTransitionName(imageTransitionName);
                String imageTransitionName2 = extras.getString(AppConfigHelper.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME2);
                collapsingToolbar.setTransitionName(imageTransitionName2);
            }*/
            objImageLoader.displayImage(mImages.get(position), ivRestaurantGallery);//logo photo

           /* Picasso.with(RestaurantInfoActivity.this)
                    .load(mImages.get(position))
                    .noFade()
                    .into(ivRestaurantGallery, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }

                    });*/

          /*  objImageLoader.setDefaultLoadingListener(new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    supportStartPostponedEnterTransition();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    supportStartPostponedEnterTransition();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });*/


            imagePopup = new ImagePopup(mContext);
            imagePopup.setWindowHeight(500); // Optional
            imagePopup.setWindowWidth(500); // Optional
            imagePopup.setBackgroundColor(Color.argb(255, 8, 8, 8));  // Optional
            imagePopup.setFullScreen(true); // Optional
            imagePopup.setHideCloseIcon(false);  // Optional
            imagePopup.setImageOnClickClose(true);  // Optional
            imagePopup.initiatePopup(ivRestaurantGallery.getDrawable());
            container.addView(itemView);
            ivRestaurantGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppConfigHelper.isOnline(RestaurantInfoActivity.this) == true) {
                        imagePopup.initiatePopup(ivRestaurantGallery.getDrawable());
                        imagePopup.viewPopup();
                    }
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void getRestaurantProfileData(int restID) {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getRestaurantProfileData(token, restID);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        objData = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objData != null && objData.response == true) {
                            restaurantGallery.clear();//clear restaurant gallery list
                           // lyPay.setVisibility(View.VISIBLE);//show paymethod layout
                            if (!Locale.getDefault().getLanguage().equals("ar")) {
                                //get English data
                                for (int i = 0; i < objData.data.size(); i++) {
                                    collapsingToolbar.setTitle(objData.data.get(i).restaurant_info.name);
                                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
                                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
                                    restaurantName = objData.data.get(i).restaurant_info.name;
                                    if (objData.data.get(i).restaurant_menu.size() > 0 & objData.data.get(i).restaurant_menu != null && !objData.data.get(i).restaurant_menu.isEmpty()) {
                                        //for (int k = 0; k < objData.data.get(i).restaurant_menu.size(); k++) {
                                        menu = objData.data.get(i).restaurant_menu.get(0).link;
                                        //}
                                    } else {
                                        menu = "N";
                                    }

                                    //get restaurant gallery
                                    for (int j = 0; j < objData.data.get(i).restaurant_pictures.size(); j++) {
                                        restaurantGallery.add(AppConfigHelper.PHOTO_URL + objData.data.get(i).restaurant_pictures.get(j).link);
                                    }
                                }
                            } else {
                                //get Arabic data
                                for (int i = 0; i < objData.data.size(); i++) {
                                    collapsingToolbar.setTitle(objData.data.get(i).restaurant_info.name_ar);
                                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
                                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
                                    restaurantName = objData.data.get(i).restaurant_info.name_ar;
                                    //price code
                                    if (objData.data.get(i).restaurant_menu.size() > 0 & objData.data.get(i).restaurant_menu != null && !objData.data.get(i).restaurant_menu.isEmpty()) {
                                        //for (int k = 0; k < objData.data.get(i).restaurant_menu.size(); k++) {
                                        menu = objData.data.get(i).restaurant_menu.get(0).link;
                                        //}
                                    } else {
                                        menu = "N";

                                    }

                                    //get restaurant gallery
                                    for (int j = 0; j < objData.data.get(i).restaurant_pictures.size(); j++) {
                                        restaurantGallery.add(AppConfigHelper.PHOTO_URL + objData.data.get(i).restaurant_pictures.get(j).link);
                                    }
                                }
                            }

                            //check if restaurant is favourite or not to change Favourite icon
                            favStatus = objData.favorite;
                            if (objData.favorite == true) {
                                ivFav.setImageResource(R.drawable.ic_favoritewhite);
                            } else {
                                ivFav.setImageResource(R.drawable.ic_heart_01);
                            }
                            //fill Restaurant Gallrey Images
                            getRestaurantGallery(restaurantGallery);
                            //get TabsLayout Items
                            setUpTabLayoutItems();
                            //make image slider
                            //timerImageSlider.scheduleAtFixedRate(new myTask(), 3000, 3000);//change every 3 seconds and started after 3 second of open this Acrivity

                        }
                    } catch (Exception objException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getRestaurantGallery(final ArrayList<String> imagesURL) {
        try {
            tvImages.setText("1/"+imagesURL.size()+"");//set First image item count
            lyImages.setVisibility(View.VISIBLE);
            adapter = new ResutaurantImagePagerAdapter(imagesURL, this);
            viewPagerImage.setAdapter(adapter);
            mIndicator.setViewPager(viewPagerImage);
            (mIndicator).setSnap(true);
            //if restaurant has one image hide indicator
            if (imagesURL.size() <= 1) {
                mIndicator.setVisibility(View.GONE);
            }
            //hide progress bar loading
            pbar.setVisibility(View.GONE);
            viewPagerImage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int imageNum=imagesURL.indexOf(imagesURL.get(position))+1;
                    tvImages.setText(imageNum+"/"+imagesURL.size());
                }

                @Override
                public void onPageSelected(int position) {
                    tvImages.setText(imagesURL.indexOf(imagesURL.get(position))+"");
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void checkFavourite() {
        try {
            if (token.isEmpty() || token == null) {
                showFavouriteAlertMessage();//if user want to make login
            } else {
                checkFavouriteStatus(Integer.parseInt(restaurantID));

               /* if(ivFav.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_favoritewhite).getConstantState())
                {
                    unFavourite();
                }
                else
                {
                    favourite();
                }*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkFavouriteStatus(int restID) {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getRestaurantProfileData(token, restID);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        RestaurantAPI favData = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (favData != null && favData.response == true) {
                            favStatus = favData.favorite;

                            if (favStatus == true) {
                                unFavourite();
                            } else {
                                favourite();
                            }
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void favourite() {
        try {
            //ivFav.setClickable(false);
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.favourite(token, Integer.parseInt(restaurantID));//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                        RestaurantAPI objDataFav = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objDataFav != null && objDataFav.response == true) {
                            ivFav.setImageResource(R.drawable.ic_favoritewhite);
                            Toast.makeText(getApplicationContext(), getString(R.string.favourite_done), Toast.LENGTH_LONG).show();
                            ivFav.setClickable(true);
                            //update favourite count
                            SharedPrefHelper.setSharedString(getApplicationContext(), SharedPrefHelper.SHARED_PREFERENCE_FAVOURITES_KEY, objDataFav.favorite_count.toString());

                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void unFavourite() {
        try {
            //ivFav.setClickable(false);
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.unfavourite(token, Integer.parseInt(restaurantID));//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                        RestaurantAPI objDataUnFav = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objDataUnFav != null) {
                            if (objDataUnFav.response == true) {
                                ivFav.setImageResource(R.drawable.ic_heart_01);
                                Toast.makeText(getApplicationContext(), getString(R.string.unfavourite_done), Toast.LENGTH_LONG).show();
                                ivFav.setClickable(true);
                                //update favourite count
                                SharedPrefHelper.setSharedString(getApplicationContext(), SharedPrefHelper.SHARED_PREFERENCE_FAVOURITES_KEY, objDataUnFav.favorite_count.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.unfavourite_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showFavouriteAlertMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_login_messsage))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finish();
                        Intent objIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        objIntent.putExtra(AppConfigHelper.FAVOURITE_STATUS, true);
                        objIntent.putExtra(AppConfigHelper.FAVOURITE_STATUS_RESTAURANT_ID, restaurantID);
                        startActivity(objIntent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void setViewsNumber() {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.setPlaceViewsNumber(Integer.parseInt(restaurantID));//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        RestaurantAPI objDataUnFav = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objDataUnFav != null) {
                            if (objDataUnFav.response == true) {
                            }
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPlaceOFFLINE() {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.setPlaceOfflineView(Integer.parseInt(restaurantID));//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        RestaurantAPI objDataUnFav = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objDataUnFav != null) {
                            if (objDataUnFav.response == true) {
                            }
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(RestaurantInfoActivity.this, getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        try {
            if (!isOnline.equals("") && !isOnline.isEmpty() && isOnline.length() > 0 && isOnline != null) {
                setPlaceOFFLINE();//set place(restaurant-coffeshop) views offline
                isOnline = "";
            }
            if (getIntent().hasExtra(AppConfigHelper.FAVOURITE_STATUS)) {
                Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(objIntent);
            } else {
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //make slider image of place photos garllery
    public class myTask extends TimerTask {

        @Override
        public void run() {
            RestaurantInfoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //make transforamtion animation
                        viewPagerImage.setPageTransformer(true, new ZoomOutPageTransformer());

                        if (imageCountPager == restaurantGallery.size()) {
                            imageCountPager = 0;
                        }
                        viewPagerImage.setCurrentItem(imageCountPager);
                        imageCountPager++;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    public static class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static float MIN_SCALE = 0.85f;
        private static float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (!isOnline.equals("") && !isOnline.isEmpty() && isOnline.length() > 0 && isOnline != null) {
                setPlaceOFFLINE();//set place(restaurant-coffeshop) views offline
                isOnline = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (!isOnline.equals("") && !isOnline.isEmpty() && isOnline.length() > 0 && isOnline != null) {
                setPlaceOFFLINE();//set place(restaurant-coffeshop) views offline
                isOnline = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void share() {
        try {

            String photoURL = AppConfigHelper.PHOTO_URL + objData.data.get(0).restaurant_pictures.get(0).link;
            final String APP_LINK = "http://play.google.com/store/apps/details?id=" + getPackageName();
            //share app
            //Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            //sharingIntent.setType("text/plain");

            String restaurantName = objData.data.get(0).restaurant_info.name;
            String restaurantDescription = objData.data.get(0).restaurant_info.description;
           /* sharingIntent.putExtra(Intent.EXTRA_STREAM, Android.Net.Uri.FromFile(new Java.IO.File(fileModel.Path)));
            sharingIntent.setType("image*//*");
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, restaurantName + ": " + restaurantDescription + "\n" + APP_LINK);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));*/
            String photoName="";
            Bitmap bitmap =getBitmapFromView(ivRestaurantGallery);
            try {
                if(restaurantName.length()>5)
                {
                    photoName=restaurantName.substring(0,5);
                }
                else
                {
                    photoName=restaurantName;
                }
                File file = new File(this.getExternalCacheDir(),photoName+".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.putExtra(Intent.EXTRA_SUBJECT, restaurantName + ": " + restaurantDescription + "\n" + APP_LINK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.putExtra(Intent.EXTRA_TEXT, restaurantName + ": " + restaurantDescription + "\n" + APP_LINK);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
            } catch (Exception e) {
                e.printStackTrace();
            }
           // new downloadImage(this,photoURL).execute();
        } catch (Exception objException) {
            objException.printStackTrace();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_data_share),Toast.LENGTH_LONG).show();
        }
    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void showReviewDialog(final String restaurantID) {
        try {
            ReviewDialog objReviewDialog;
            FragmentTransaction objFragmentTransaction = getFragmentManager().beginTransaction();
            Fragment prevFragment = getFragmentManager().findFragmentByTag(AppConfigHelper.REVIEW_DIALOG_TAG);
            if (prevFragment != null) {
                objFragmentTransaction.remove(prevFragment);
            }
            objReviewDialog = ReviewDialog.newInstance(restaurantID,restaurantName, new ReviewDialog.OnClickReviewDailog() {
                @Override
                public void onReviewClicked(String comment, String value, String waiting, float foodRate, float serviceRate, float atmosphereRate) {
                    double foodVal,servieVal,atomsphereVal;
                    int waitingVal,valueVal;
                    if(waiting.length()>0)
                    {
                        waitingVal=Integer.parseInt(waiting);
                    }
                    else
                    {
                        waitingVal=0;
                    }
                    if(value.length()>0)
                    {
                        valueVal=Integer.parseInt(value);
                    }
                    else
                    {
                        valueVal=0;
                    }
                    addReview(token,foodRate,atmosphereRate,serviceRate,valueVal,waitingVal,comment,Integer.parseInt(restaurantID));
                }
            });
            objReviewDialog.show(objFragmentTransaction, AppConfigHelper.REVIEW_DIALOG_TAG);
        }
        catch (Exception objExcption)
        {
            objExcption.printStackTrace();
        }
    }

    private void addReview(String token,float food,float atmosphere,float service,int value_for_mony,int waiting_time,String feedback,int restID)
    {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.addReview(token,restID,food,atmosphere,service,value_for_mony,waiting_time,feedback);
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        User objUser = new Gson().fromJson(response.body(), User.class);
                        if (objUser.response==true) {
                            Toast.makeText(getApplicationContext(), getString(R.string.success_review), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), objUser.message /*getString(R.string.couldnt_complete_process)*/, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        objThrowable.printStackTrace();

                    } else {
                        objThrowable.printStackTrace();

                    }
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

