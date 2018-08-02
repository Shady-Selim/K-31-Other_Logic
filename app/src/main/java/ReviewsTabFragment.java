import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trabiza.app.Adapters.ReviewsAdapter;
import com.trabiza.app.Helpers.AppConfigHelper.AppConfigHelper;
import com.trabiza.app.Helpers.DialogsHelper.DialogsHelper;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndPoint;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndpointInterface;
import com.trabiza.app.Models.ReviewsAPI;
import com.trabiza.app.Models.ReviewsModel;
import com.trabiza.app.R;
import com.trabiza.app.Views.CustomTextView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hussein Kamal on 15/02/2018.
 */

public class ReviewsTabFragment extends Fragment implements View.OnClickListener,View.OnTouchListener,SeekBar.OnSeekBarChangeListener,SwipeRefreshLayout.OnRefreshListener,View.OnScrollChangeListener {
    private View objViewReviews;
    private OnReviewsTabFragmentInitiateListner objReviewsFragment;
    private Intent objIntent;
    public LayerDrawable stars;
    private ArrayList<ReviewsModel> reviewsModelList=new ArrayList<ReviewsModel>();
    private ReviewsAdapter adapter;
    private ReviewsModel modelData;
    private GridLayoutManager mLayoutManager;
    @BindView(R.id.tvRateValue)
    CustomTextView tvRateValue;
    @BindView(R.id.barRate)
    RatingBar barRate;
    @BindView(R.id.tvReviews)
    CustomTextView tvReviews;

    @BindView(R.id.tvVal1)
    CustomTextView tvVal1;

    @BindView(R.id.tvVal2)
    CustomTextView tvVal2;

    @BindView(R.id.tvVal3)
    CustomTextView tvVal3;

    @BindView(R.id.tvVal4)
    CustomTextView tvVal4;

    @BindView(R.id.tvVal5)
    CustomTextView tvVal5;


    @BindView(R.id.tvTitleVal1)
    CustomTextView tvTitleVal1;

    @BindView(R.id.tvTitleVal2)
    CustomTextView tvTitleVal2;

    @BindView(R.id.tvTitleVal3)
    CustomTextView tvTitleVal3;

    @BindView(R.id.tvTitleVal4)
    CustomTextView tvTitleVal4;

    @BindView(R.id.tvTitleVal5)
    CustomTextView tvTitleVal5;

    @BindView(R.id.seekVal1)
    SeekBar seekVal1;

    @BindView(R.id.seekVal2)
    SeekBar seekVal2;

    @BindView(R.id.seekVal3)
    SeekBar seekVal3;

    @BindView(R.id.seekVal4)
    SeekBar seekVal4;

    @BindView(R.id.seekVal5)
    SeekBar seekVal5;

    @BindView(R.id.lyVal1)
    LinearLayout lyVal1;

    @BindView(R.id.lyVal2)
    LinearLayout lyVal2;

    @BindView(R.id.lyVal3)
    LinearLayout lyVal3;

    @BindView(R.id.lyVal4)
    LinearLayout lyVal4;

    @BindView(R.id.lyVal5)
    LinearLayout lyVal5;

    @BindView(R.id.lyReviewData)
    LinearLayout lyReviewData;

    @BindView(R.id.swipeLoad)
    SwipeRefreshLayout swipeLoad;

    @BindView(R.id.pbarLoad)
    ProgressBar pbarLoad;

    @BindView(R.id.rlReviewsComments)
    RelativeLayout rlReviewsComments;


    @BindView(R.id.listItemsReviews)
    RecyclerView listItemsReviews;

   /* @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.lySheetComments)
    LinearLayout lySheetComments;

    @BindView(R.id.tvSheetComments)
    CustomTextView tvSheetComments;

    @BindView(R.id.ivSheetComments)
    ImageView ivSheetComments;

    BottomSheetBehavior sheetBehavior;
*/
    int progressChangedValue = 0,page=0,pageCountValue;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading=false;

    private String restaurantID;


    public ReviewsTabFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ReviewsTabFragment(String restaurantID) {
        // Required empty public constructor
        this.restaurantID=restaurantID;
    }
    public void onClick(View view) {
        switch (view.getId())
        {/*
            case R.id.lySheetComments:
                try {
                    //toggle comments and reviews
                    toggleBottomSheet();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;*/
        }
    }

   /* public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }*/
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId())
        {
            case R.id.seekVal1:
                progressChangedValue = progress;
                seekVal1.setProgress(progressChangedValue);
                tvVal1.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal2:
                progressChangedValue = progress;
                seekVal2.setProgress(progressChangedValue);
                tvVal2.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal3:
                progressChangedValue = progress;
                seekVal3.setProgress(progressChangedValue);
                tvVal3.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal4:
                progressChangedValue = progress;
                seekVal4.setProgress(progressChangedValue);
                tvVal4.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal5:
                progressChangedValue = progress;
                seekVal5.setProgress(progressChangedValue);
                tvVal5.setText(String.valueOf(progressChangedValue)+"%");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId())
        {
            case R.id.seekVal1:
                break;
            case R.id.seekVal2:
                break;
            case R.id.seekVal3:
                break;
            case R.id.seekVal4:
                break;
            case R.id.seekVal5:
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId())
        {
            case R.id.seekVal1:
                seekVal1.setProgress(progressChangedValue);
                tvVal1.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal2:
                seekVal2.setProgress(progressChangedValue);
                tvVal2.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal3:
                seekVal3.setProgress(progressChangedValue);
                tvVal3.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal4:
                seekVal4.setProgress(progressChangedValue);
                tvVal4.setText(String.valueOf(progressChangedValue)+"%");
                break;
            case R.id.seekVal5:
                seekVal5.setProgress(progressChangedValue);
                tvVal5.setText(String.valueOf(progressChangedValue)+"%");
                break;
        }
    }

    @Override
    public void onRefresh() {
        try {
            getReviewsComments();
            swipeLoad.setRefreshing(false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
      /*  totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        if (!isLoading==true && totalItemCount <= (lastVisibleItem + visibleThreshold)&&page<=pageCountValue) {
            if (reviewsModelList.size()>0 &&listItemsReviews!= null) {
                getMoreReviewsComments(page+1);
            }
            pageCountValue--;//to not allow user to scroll again after the num of reviews finished
            isLoading = true;
        }*/
    }

    public interface OnReviewsTabFragmentInitiateListner {
        void onReviewsTabFragmentInitiated();
    }

    public static ReviewsTabFragment newInstance(OnReviewsTabFragmentInitiateListner objReviewsTabFragment) {
        ReviewsTabFragment reviewTabFragment = new ReviewsTabFragment();
        reviewTabFragment.objReviewsFragment= objReviewsTabFragment;
        Bundle args = new Bundle();
        reviewTabFragment.setArguments(args);
        return reviewTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            objViewReviews = inflater.inflate(R.layout.layout_reviews_tab_fragment, container, false);
            //initiate Butter Knife
            ButterKnife.bind(this, objViewReviews);
            initViews(objViewReviews);
            //getReviewsComments();
            return objViewReviews;
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
            return objViewReviews;
        }
    }
    private void initViews(View objView)
    {
        try {
            //to make seek bars not changable for user and make OnTouch Method return true
            /*barRate.setRating(4.0f);
            stars = (LayerDrawable) barRate.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.argb(255, 255, 182, 69), PorterDuff.Mode.SRC_ATOP);*/
            swipeLoad.setOnRefreshListener(this);
            seekVal1.setOnSeekBarChangeListener(this);//execllent
            seekVal2.setOnSeekBarChangeListener(this);//very good
            seekVal3.setOnSeekBarChangeListener(this);//average
            seekVal4.setOnSeekBarChangeListener(this);//Poor
            seekVal5.setOnSeekBarChangeListener(this);//Terrible

            seekVal1.setEnabled(false);
            seekVal2.setEnabled(false);
            seekVal3.setEnabled(false);
            seekVal4.setEnabled(false);
            seekVal5.setEnabled(false);

            getReviewsData();

        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }

  /*  private void getReviews()
    {
        try {
            seekVal1.setProgress(0);
            seekVal2.setProgress(0);
            seekVal3.setProgress(0);
            seekVal4.setProgress(0);
            seekVal5.setProgress(0);
            lyReviewData.setVisibility(View.VISIBLE);
            AppConfigHelper.setLayoutAnim_slidedownfromtop(lyReviewData,getContext());
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getReviews(restaurantID,page);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        ReviewsAPI objData = new Gson().fromJson(response.body(), ReviewsAPI.class);
                        if (objData != null&&objData.request==true) {
                            if(objData.data.rating.all_rating.isEmpty()||objData.data.rating.all_rating.equals("")||objData.data.rating.all_rating.length()<=0)
                            {
                                tvRateValue.setVisibility(View.GONE);
                                barRate.setVisibility(View.GONE);
                            }
                            else
                            {
                                tvRateValue.setText(objData.data.rating.all_rating);
                                barRate.setRating(Float.parseFloat(objData.data.rating.all_rating));
                                stars = (LayerDrawable) barRate.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(Color.argb(255,228,138,0), PorterDuff.Mode.SRC_ATOP);
                            }
                            if(objData.data.rating.all_review.isEmpty()||objData.data.rating.all_review.equals("")||objData.data.rating.all_review.length()<=0)
                            {
                                tvReviews.setVisibility(View.GONE);
                            }
                            else
                            {
                                //tvReviews.setText(objData.data.rating.all_review + " " + getResources().getString(R.string.reviews));
                                reviewsTVInitData(Integer.parseInt(objData.data.rating.all_review));
                            }


                            if(!objData.data.rating.percentage.Excellent.isEmpty()&&!objData.data.rating.percentage.Excellent.equals("")&&objData.data.rating.percentage.Excellent.length()>0) {
                                seekBarInitData(seekVal1,Integer.parseInt(objData.data.rating.percentage.Excellent.replaceAll("%","")));
                                tvTitleVal1.setText(getResources().getString(R.string.excellent));
                            }
                            else
                            {
                                lyVal1.setVisibility(View.GONE);
                            }
                            if(!objData.data.rating.percentage.Very_good.isEmpty()&&!objData.data.rating.percentage.Very_good.equals("")&&objData.data.rating.percentage.Very_good.length()>0) {
                                seekBarInitData(seekVal2,Integer.parseInt(objData.data.rating.percentage.Very_good.replaceAll("%","")));
                                tvTitleVal2.setText(getResources().getString(R.string.Very_good));
                            }
                            else
                            {
                                lyVal2.setVisibility(View.GONE);
                            }
                            if(!objData.data.rating.percentage.Average.isEmpty()&&!objData.data.rating.percentage.Average.equals("")&&objData.data.rating.percentage.Average.length()>0) {
                                seekBarInitData(seekVal3,Integer.parseInt(objData.data.rating.percentage.Average.replaceAll("%","")));
                                tvTitleVal3.setText(getResources().getString(R.string.Average));
                            }
                            else
                            {
                                lyVal3.setVisibility(View.GONE);
                            }
                            if(!objData.data.rating.percentage.Poor.isEmpty()&&!objData.data.rating.percentage.Poor.equals("")&&objData.data.rating.percentage.Poor.length()>0) {
                                seekBarInitData(seekVal4,Integer.parseInt(objData.data.rating.percentage.Poor.replaceAll("%","")));
                                tvTitleVal4.setText(getResources().getString(R.string.Poor));
                            }
                            else
                            {
                                lyVal4.setVisibility(View.GONE);
                            }
                            if(!objData.data.rating.percentage.Terrible.isEmpty()&&!objData.data.rating.percentage.Terrible.equals("")&&objData.data.rating.percentage.Terrible.length()>0) {
                                seekBarInitData(seekVal5,Integer.parseInt(objData.data.rating.percentage.Terrible.replaceAll("%","")));
                                tvTitleVal5.setText(getResources().getString(R.string.Terrible));
                            }
                            else
                            {
                                lyVal5.setVisibility(View.GONE);
                            }


                        }
                    } catch (Exception objException) {
                        // pbTimeList.setVisibility(View.GONE);
                        objException.printStackTrace();
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    //pbTimeList.setVisibility(View.GONE);
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();

                    }
                }
            });
        }
        catch (Exception ex)
        {
            // pbTimeList.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }*/
    private void seekBarInitData(SeekBar seekBarView,int value)
    {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                seekBarView.setProgress(0);
                // will update the "progress" propriety of seekbar until it reaches progress
                ObjectAnimator animation = ObjectAnimator.ofInt(seekBarView, "progress", value);
                animation.setDuration(500); // 1.5 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            } else {
                seekBarView.setProgress(value); // no animation on Gingerbread or lower
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void reviewsTVInitData(final int value)
    {
        try {
            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0, value);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    tvReviews.setText(String.valueOf(animation.getAnimatedValue())+ " " + getResources().getString(R.string.reviews));
                }
            });
            animator.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator.setDuration(500);
            animator.start();
           /* final int counter = 0;
            new Thread(new Runnable() {

                public void run() {
                    while (counter < value) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        tvData.post(new Runnable() {

                            public void run() {
                                tvData.setText("" + counter);

                            }

                        });
                        counter++;
                    }

                }

            }).start();*/
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            listItemsReviews.setOnScrollChangeListener(this);
            if (isVisibleToUser) {
                //do something  //Load or Refresh Data
                try {
                    //getReviews();
                    getReviewsData();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void getReviewsData()
    {
        tvRateValue.setText("4.0");
        barRate.setRating(Float.parseFloat("4.0"));
        stars = (LayerDrawable) barRate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.argb(255,228,138,0), PorterDuff.Mode.SRC_ATOP);

        reviewsTVInitData(80);


        seekBarInitData(seekVal1,40);
        tvTitleVal1.setText(getResources().getString(R.string.excellent));

        seekBarInitData(seekVal2,30);
        tvTitleVal2.setText(getResources().getString(R.string.Very_good));

        seekBarInitData(seekVal3,20);
        tvTitleVal3.setText(getResources().getString(R.string.Average));

        seekBarInitData(seekVal4,5);
        tvTitleVal4.setText(getResources().getString(R.string.Poor));

        seekBarInitData(seekVal5,5);
        tvTitleVal5.setText(getResources().getString(R.string.Terrible));

        for (int i = 0; i < 20; i++) {
            modelData=new ReviewsModel("Demo Name1","2018-08-01","20:00 PM","This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text","Demo Title1");
            reviewsModelList.add(modelData);

            modelData=new ReviewsModel("Demo Name2","2018-08-02","15:00 PM","This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text","Demo Title2");
            reviewsModelList.add(modelData);

            modelData=new ReviewsModel("Demo Name3","2018-08-02","10:00 AM","This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text","Demo Title3");
            reviewsModelList.add(modelData);

            modelData=new ReviewsModel("Demo Name4","2018-07-31","12:00 PM","This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text This is a demo text","Demo Title4");
            reviewsModelList.add(modelData);
        }

        adapter = new ReviewsAdapter(getActivity(), reviewsModelList);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);//may you can change num of column here
        listItemsReviews.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(20), true));
        listItemsReviews.setItemAnimator(new DefaultItemAnimator());
        listItemsReviews.setAdapter(adapter);
        listItemsReviews.setVisibility(View.VISIBLE);
    }

    private void getReviewsComments()
    {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getReviews(restaurantID,page);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        ReviewsAPI objData = new Gson().fromJson(response.body(), ReviewsAPI.class);
                        reviewsModelList.clear();
                        pbarLoad.setVisibility(View.GONE);
                        listItemsReviews.setVisibility(View.VISIBLE);
                        rlReviewsComments.setVisibility(View.VISIBLE);
                        if (objData != null&&objData.request==true&&objData.data.rating.reviews.size()>0) {
                            pageCountValue=(int) (Math.ceil(Double.parseDouble(objData.data.rating.all_review.trim())/ AppConfigHelper.REVIEWS_SHOW_NUMBER));
                            for (int i = 0; i < objData.data.rating.reviews.size(); i++) {
                                String[] date=objData.data.rating.reviews.get(i).date.split("-");
                                modelData=new ReviewsModel(objData.data.rating.reviews.get(i).user_name,objData.data.rating.reviews.get(i).rate,date[1], objData.data.rating.reviews.get(i).feedback,objData.data.rating.reviews.get(i).title);
                                reviewsModelList.add(modelData);
                            }
                            adapter = new ReviewsAdapter(getActivity(), reviewsModelList);
                            mLayoutManager = new GridLayoutManager(getActivity(), 1);//may you can change num of column here
                            listItemsReviews.setLayoutManager(mLayoutManager);
                            //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(20), true));
                            listItemsReviews.setItemAnimator(new DefaultItemAnimator());
                            listItemsReviews.setAdapter(adapter);
                        }
                        else
                        {
                            rlReviewsComments.setVisibility(View.GONE);
                        }
                    } catch (Exception objException) {
                        pbarLoad.setVisibility(View.GONE);
                        rlReviewsComments.setVisibility(View.GONE);
                        objException.printStackTrace();
                        //DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    pbarLoad.setVisibility(View.GONE);
                    rlReviewsComments.setVisibility(View.GONE);
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();

                    }
                }
            });
        }
        catch (Exception ex)
        {
            pbarLoad.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }
    private void getMoreReviewsComments(int pageCount)
    {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getReviews(restaurantID,pageCount);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        ReviewsAPI objData = new Gson().fromJson(response.body(), ReviewsAPI.class);
                        pbarLoad.setVisibility(View.GONE);
                        rlReviewsComments.setVisibility(View.VISIBLE);
                        listItemsReviews.setVisibility(View.VISIBLE);
                        if(response.body().has("reviews")) {
                            if (objData != null && objData.request == true && objData.data.rating.reviews.size() > 0) {
                                for (int i = 0; i < objData.data.rating.reviews.size(); i++) {
                                    String[] date = objData.data.rating.reviews.get(i).date.split("-");
                                    modelData = new ReviewsModel(objData.data.rating.reviews.get(i).user_name, objData.data.rating.reviews.get(i).rate, date[1], objData.data.rating.reviews.get(i).feedback, objData.data.rating.reviews.get(i).title);
                                    reviewsModelList.add(modelData);
                                }
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            /*adapter = new ReviewsAdapter(getActivity(), reviewsModelList);
                            mLayoutManager = new GridLayoutManager(getActivity(), 1);//may you can change num of column here
                            listItemsReviews.setLayoutManager(mLayoutManager);
                            //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(20), true));
                            listItemsReviews.setItemAnimator(new DefaultItemAnimator());
                            listItemsReviews.setAdapter(adapter);*/
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(),getResources().getString(R.string.load_data_complete),Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception objException) {
                        pbarLoad.setVisibility(View.GONE);
                        objException.printStackTrace();
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    pbarLoad.setVisibility(View.GONE);
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();

                    }
                }
            });
        }
        catch (Exception ex)
        {
            pbarLoad.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }
}
