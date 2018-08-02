import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trabiza.app.Helpers.AppConfigHelper.AppConfigHelper;
import com.trabiza.app.Helpers.DialogsHelper.DialogsHelper;
import com.trabiza.app.Helpers.SharedPrefrenceHelper.SharedPrefHelper;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndPoint;
import com.trabiza.app.Helpers.WebServiceHelper.ApiEndpointInterface;
import com.trabiza.app.Models.DistanceAPI;
import com.trabiza.app.Models.RestaurantAPI;
import com.trabiza.app.R;
import com.trabiza.app.Views.CustomTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hussein Kamal on 15/02/2018.
 */

public class InfoTabFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback/*, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Observer*/ ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    private View objViewInfo;
    private OnInfoTabFragmentInitiateListner objInfoTabFragment;
    private Intent objIntent;
    //private TextView tvPhone, tvPayMethod, tvCuisine, tvDateTime, tvDescription;
    private GoogleMap mGoogleMap;
    private MapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LatLng latLng;
    private String restaurantID;
    private RestaurantAPI objData;
    private Double lat, lng;
    private String fromTime,toTime,breakFrom,breakTo,PdfURL;

    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private final static int REQUEST_ID_SEARCH_LOCATION=10;
    private Location mylocation;
    ArrayList<String> routes=new ArrayList<String>();
    ArrayList<String> routesList=new ArrayList<String>();

    @BindView(R.id.tvPhone)
    CustomTextView tvPhone;
    @BindView(R.id.tvPayMethod)
    CustomTextView tvPayMethod;
    @BindView(R.id.tvCuisine)
    CustomTextView tvCuisine;
    @BindView(R.id.tvDateTime)
    CustomTextView tvDateTime;
    @BindView(R.id.tvDescription)
    CustomTextView tvDescription;
    @BindView(R.id.tvAddress)
    CustomTextView tvAddress;

    @BindView(R.id.tvPayment)
    CustomTextView tvPayment;

    @BindView(R.id.tvDressCode)
    CustomTextView tvDressCode;

    @BindView(R.id.tvParking)
    CustomTextView tvParking;

    @BindView(R.id.tvPets)
    CustomTextView tvPets;

    @BindView(R.id.tvSmoking)
    CustomTextView tvSmoking;

    @BindView(R.id.tvOutDoor)
    CustomTextView tvOutDoor;

    @BindView(R.id.tvParty)
    CustomTextView tvParty;

    @BindView(R.id.tvAlcohol)
    CustomTextView tvAlcohol;

    @BindView(R.id.tvCurrency)
    CustomTextView tvCurrency;

    @BindView(R.id.tvKidsArea)
    CustomTextView tvKidsArea;

    @BindView(R.id.pbarLoad)
    ProgressBar pbarLoad;

    @BindView(R.id.lyContain)
    LinearLayout lyContain;

    @BindView(R.id.lyPayment)
    LinearLayout lyPayment;

    @BindView(R.id.lyParking)
    LinearLayout lyParking;

    @BindView(R.id.lyDressCode)
    LinearLayout lyDressCode;

    @BindView(R.id.lyPets)
    LinearLayout lyPets;

    @BindView(R.id.lySmoking)
    LinearLayout lySmoking;

    @BindView(R.id.lyOutDoor)
    LinearLayout lyOutDoor;

    @BindView(R.id.lyParty)
    LinearLayout lyParty;

    @BindView(R.id.lyAlcohol)
    LinearLayout lyAlcohol;

    @BindView(R.id.lyCurrency)
    LinearLayout lyCurrency;

    @BindView(R.id.lyKidsArea)
    LinearLayout lyKidsArea;

    @BindView(R.id.lyMenu)
    LinearLayout lyMenu;

    @BindView(R.id.tvMenu)
    CustomTextView tvMenu;

    @BindView(R.id.ivMenu)
    ImageView ivMenu;

    @BindView(R.id.tvFindUs)
    CustomTextView tvFindUs;

    @BindView(R.id.tvMenuTitle)
    CustomTextView tvMenuTitle;

    @BindView(R.id.tvTimeWork)
    CustomTextView tvTimeWork;

    @BindView(R.id.tvOtherDetails)
    CustomTextView tvOtherDetails;

    @BindView(R.id.rlContainerData)
    RelativeLayout rlContainerData;

    @BindView(R.id.tvDuration)
    CustomTextView tvDuration;

    @BindView(R.id.tvDistance)
    CustomTextView tvDistance;

    @BindView(R.id.rlDistance)
    LinearLayout rlDistance;


    MapView map;
    private HashMap<String, String> mapWeekDays = new HashMap<String, String>();


    //private boolean statusFirstTimeOpenApp=false;


    public InfoTabFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public InfoTabFragment(String restaurantID) {
        // Required empty public constructor
        this.restaurantID = restaurantID;
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvMenu:
                try {
                    PdfURL = AppConfigHelper.PHOTO_URL + objData.data.get(0).restaurant_menu.get(0).link;
                    objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(Uri.parse(PdfURL), "application/pdf");
                    startActivity(objIntent);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
            case R.id.ivMenu:
                try {
                    PdfURL = AppConfigHelper.PHOTO_URL + objData.data.get(0).restaurant_menu.get(0).link;
                    objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(Uri.parse(PdfURL), "application/pdf");
                    startActivity(objIntent);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
            case R.id.lyMenu:
                try {
                    PdfURL = AppConfigHelper.PHOTO_URL + objData.data.get(0).restaurant_menu.get(0).link;
                    objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(Uri.parse(PdfURL), "application/pdf");
                    startActivity(objIntent);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
        }
    }

    public interface OnInfoTabFragmentInitiateListner {
        void onInfoTabFragmentInitiated();
    }

    public static InfoTabFragment newInstance(OnInfoTabFragmentInitiateListner objInfoTabFragment) {
        InfoTabFragment infTabFragment = new InfoTabFragment();
        infTabFragment.objInfoTabFragment = objInfoTabFragment;
        Bundle args = new Bundle();
        infTabFragment.setArguments(args);
        return infTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(InfoTabFragment.this.getActivity().getBaseContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            objViewInfo = inflater.inflate(R.layout.layout_info_tab_fragment, container, false);
            initMapWeekDays();//days of week map
            //initiate Butter Knife
            ButterKnife.bind(this, objViewInfo);
            map = (MapView) objViewInfo.findViewById(R.id.map);
            //show bar loading
            pbarLoad.setVisibility(View.VISIBLE);
            lyContain.setVisibility(View.GONE);
            lyMenu.setOnClickListener(this);
            tvPhone.setOnClickListener(this);
            ivMenu.setOnClickListener(this);
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                ivMenu.setImageResource(R.drawable.ic_left_arrow_gray);
                tvFindUs.setGravity(Gravity.RIGHT);
                tvMenuTitle.setGravity(Gravity.RIGHT);
                tvTimeWork.setGravity(Gravity.RIGHT);
                tvOtherDetails.setGravity(Gravity.RIGHT);
            }
            else
            {
                ivMenu.setImageResource(R.drawable.ic_right_arrow_gray);
                tvFindUs.setGravity(Gravity.LEFT);
                tvMenuTitle.setGravity(Gravity.LEFT);
                tvTimeWork.setGravity(Gravity.LEFT);
                tvOtherDetails.setGravity(Gravity.LEFT);
            }

            map.onCreate(savedInstanceState);
            map.getMapAsync(this);
            setUpGClient();

            return objViewInfo;
        } catch (Exception objException) {
            objException.printStackTrace();
            return objViewInfo;
        }
    }

    private void initMapWeekDays() {
        mapWeekDays.put("saturday", "السبت");
        mapWeekDays.put("sunday", "الأحد");
        mapWeekDays.put("monday", "الأثنين");
        mapWeekDays.put("tuesday", "الثلاثاء");
        mapWeekDays.put("wednesday", "الأربعاء");
        mapWeekDays.put("thursday", "الخميس");
        mapWeekDays.put("friday", "الجمعه");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            //get profile restaurant data
            mGoogleMap = googleMap;
            //get restaurant profile data info
            getRestaurantProfileData(Integer.parseInt(restaurantID));
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    private void getRestaurantProfileData(int restID) {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClient().create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getRestaurantProfileData(SharedPrefHelper.getSharedString(getActivity(), SharedPrefHelper.SHARED_PREFERENCE_USER_TOKEN), restID);//get citites of country
            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        rlContainerData.setVisibility(View.VISIBLE);
                        objData = new Gson().fromJson(response.body(), RestaurantAPI.class);
                        if (objData != null && objData.response == true) {
                            if (!Locale.getDefault().getLanguage().equals("ar")) {
                                //get English data
                                for (int i = 0; i < objData.data.size(); i++) {
                                    tvPhone.setText("(" + objData.data.get(i).restaurant_info.country_code + ")" + objData.data.get(i).restaurant_info.phone);
                                    tvPayMethod.setText(objData.data.get(i).restaurant_info.price.price);
                                    tvCuisine.setText(objData.data.get(i).restaurant_info.cuisines);
                                    tvDescription.setText(objData.data.get(i).restaurant_info.description);
                                    tvAddress.setText(objData.data.get(i).restaurant_info.address);

                                    //payment method
                                    if (objData.data.get(i).restaurant_info.payment_method != null) {
                                        tvPayment.setText(tvPayment.getText().toString() + ": " + objData.data.get(i).restaurant_info.payment_method);
                                    } else {
                                        lyPayment.setVisibility(View.GONE);
                                    }

                                    //parking
                                    if (objData.data.get(i).restaurant_info.parking != null) {
                                        if (objData.data.get(i).restaurant_info.parking.equals("Y")) {
                                            tvParking.setText(tvParking.getText().toString());
                                        } else {
                                            lyParking.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyParking.setVisibility(View.GONE);
                                    }

                                    //dress code
                                    if (objData.data.get(i).restaurant_info.dress_code != null) {
                                        if (objData.data.get(i).restaurant_info.dress_code.equals("Y")) {
                                            tvDressCode.setText(tvDressCode.getText().toString() + ": " + getString(R.string.formal_dress));
                                        } else {
                                            lyDressCode.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyDressCode.setVisibility(View.GONE);
                                    }

                                    //pets
                                    if (objData.data.get(i).restaurant_info.pets != null) {
                                        if (objData.data.get(i).restaurant_info.pets.equals("Y")) {
                                            tvPets.setText(tvPets.getText().toString());
                                        } else {
                                            lyPets.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyPets.setVisibility(View.GONE);
                                    }

                                    //smoking
                                    if (objData.data.get(i).restaurant_info.smoking != null) {
                                        if (objData.data.get(i).restaurant_info.smoking.equals("Y")) {
                                            tvSmoking.setText(tvSmoking.getText().toString());
                                        } else {
                                            lySmoking.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lySmoking.setVisibility(View.GONE);
                                    }

                                    //out door
                                    if (objData.data.get(i).restaurant_info.outdoor != null) {
                                        if (objData.data.get(i).restaurant_info.outdoor.equals("Y")) {
                                            tvOutDoor.setText(tvOutDoor.getText().toString());
                                        } else {
                                            lyOutDoor.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyOutDoor.setVisibility(View.GONE);
                                    }

                                    //party
                                    if (objData.data.get(i).restaurant_info.party != null) {
                                        if (objData.data.get(i).restaurant_info.party.equals("Y")) {
                                            tvParty.setText(tvParty.getText().toString());
                                        } else {
                                            lyParty.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyParty.setVisibility(View.GONE);
                                    }


                                    //alcohol
                                    if (objData.data.get(i).restaurant_info.alcohol != null) {
                                        if (objData.data.get(i).restaurant_info.alcohol.equals("Y")) {
                                            tvAlcohol.setText(tvAlcohol.getText().toString());
                                        } else {
                                            lyAlcohol.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyAlcohol.setVisibility(View.GONE);
                                    }

                                    //currency
                                    if (objData.data.get(i).restaurant_info.currency != null) {
                                        tvCurrency.setText(tvCurrency.getText().toString() + ": " + objData.data.get(i).restaurant_info.currency);

                                    } else {
                                        lyCurrency.setVisibility(View.GONE);
                                    }

                                    //kids area
                                    if (objData.data.get(i).restaurant_info.kids_area != null) {
                                        if (objData.data.get(i).restaurant_info.kids_area.equals("Y")) {
                                            tvKidsArea.setText(tvKidsArea.getText().toString());
                                        } else {
                                            lyKidsArea.setVisibility(View.GONE);
                                        }
                                    } else {
                                        lyKidsArea.setVisibility(View.GONE);
                                    }


                                    String[] loc = objData.data.get(i).restaurant_info.location.split(",");
                                    lat = Double.parseDouble(loc[0]);
                                    lng = Double.parseDouble(loc[1]);

                                    if (objData.data.get(i).restaurant_setting.availability.equals("Y")) {
                                        //English language available days
                                        StringBuilder stringBuilder = new StringBuilder();
                                        if (!Locale.getDefault().getLanguage().equals("ar")) {

                                            for (int j = 0; j < objData.data.get(i).restaurant_setting.availability_days.size(); j++) {
                                                String strDay = objData.data.get(i).restaurant_setting.availability_days.get(j).day;
                                                fromTime=objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.length()-3);
                                                fromTime=from24hour(fromTime);
                                                toTime = objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.length()-3);
                                                toTime=from24hour(toTime);
                                                stringBuilder.append(strDay + " " + getString(R.string.from) + fromTime + "\t" + getString(R.string.to) + toTime + "\n");
                                            }
                                            breakFrom=objData.data.get(i).restaurant_setting.break_from.substring(0,objData.data.get(i).restaurant_setting.break_from.length()-3);
                                            breakFrom=from24hour(breakFrom);
                                            breakTo=objData.data.get(i).restaurant_setting.break_to.substring(0,objData.data.get(i).restaurant_setting.break_to.length()-3);
                                            breakTo=from24hour(breakTo);
                                            tvDateTime.setText(stringBuilder.toString() + getString(R.string.break_time) + getString(R.string.from) + "\t" + breakFrom+ getString(R.string.to) + breakTo);
                                        }
                                        //Arabic language available days
                                        else {
                                            for (int j = 0; j < objData.data.get(i).restaurant_setting.availability_days.size(); j++) {
                                                String strDay = mapWeekDays.get(objData.data.get(i).restaurant_setting.availability_days.get(j).day.trim().toString().toLowerCase());

                                                //get From Time
                                                fromTime=objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.length()-3);
                                                fromTime=from24hour(fromTime);
                                                toTime = objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.length()-3);
                                                toTime=from24hour(toTime);
                                                stringBuilder.append(strDay + " " + getString(R.string.from) + fromTime + "\t" + getString(R.string.to) + toTime + "\n");
                                            }

                                            //check inf there is break time or not
                                            if(objData.data.get(i).restaurant_setting.break_from!=null&&objData.data.get(i).restaurant_setting.break_to!=null) {
                                                breakFrom = objData.data.get(i).restaurant_setting.break_from.substring(0, objData.data.get(i).restaurant_setting.break_from.length() - 3);
                                                breakFrom = from24hour(breakFrom);
                                                breakTo = objData.data.get(i).restaurant_setting.break_to.substring(0, objData.data.get(i).restaurant_setting.break_to.length() - 3);
                                                breakTo = from24hour(breakTo);
                                                tvDateTime.setText(stringBuilder.toString() + getString(R.string.break_time) + getString(R.string.from) + "\t" + breakFrom + getString(R.string.to) + breakTo);
                                            }
                                        }
                                    } else {
                                        fromTime=objData.data.get(i).restaurant_setting.open_at.substring(0, objData.data.get(i).restaurant_setting.open_at.length()-3);
                                        fromTime=from24hour(fromTime);

                                        toTime=objData.data.get(i).restaurant_setting.close_at.substring(0, objData.data.get(i).restaurant_setting.close_at.length()-3);
                                        toTime=from24hour(toTime);

                                        tvDateTime.setText(getString(R.string.every_day) + "\t\t" + getString(R.string.from) + "\t" + fromTime + "\t\t" + getString(R.string.to) + "\t" + toTime + "\n");
                                        String str = tvDateTime.getText().toString();

                                        //check inf there is break time or not
                                        if(objData.data.get(i).restaurant_setting.break_from!=null&&objData.data.get(i).restaurant_setting.break_to!=null) {
                                            breakFrom = objData.data.get(i).restaurant_setting.break_from.substring(0, objData.data.get(i).restaurant_setting.break_from.length() - 3);
                                            breakFrom = from24hour(breakFrom);
                                            breakTo = objData.data.get(i).restaurant_setting.break_to.substring(0, objData.data.get(i).restaurant_setting.break_to.length() - 3);
                                            breakTo = from24hour(breakTo);
                                            String breakTime = getString(R.string.break_time) + "\t\t" + getString(R.string.from) + "\t" + breakFrom + "\t\t" + getString(R.string.to) + breakTo;
                                            tvDateTime.setText(str + "" + breakTime);
                                        }
                                    }
                                }
                            } else {
                                //get Arabic data
                                for (int i = 0; i < objData.data.size(); i++) {
                                    tvPhone.setText("(" + objData.data.get(i).restaurant_info.country_code + ")" + objData.data.get(i).restaurant_info.phone);
                                    tvPayMethod.setText(objData.data.get(i).restaurant_info.price.price);
                                    tvCuisine.setText(objData.data.get(i).restaurant_info.cuisines);
                                    tvCuisine.setGravity(Gravity.END);
                                    tvDateTime.setText(objData.data.get(i).restaurant_info.cuisines);
                                    tvDescription.setText(objData.data.get(i).restaurant_info.description_ar);
                                    tvAddress.setText(objData.data.get(i).restaurant_info.address_ar);
                                    String[] loc = objData.data.get(i).restaurant_info.location.split(",");
                                    lat = Double.parseDouble(loc[0]);
                                    lng = Double.parseDouble(loc[1]);

                                    if (objData.data.get(i).restaurant_setting.availability.equals("Y")) {
                                        //English language available days
                                        StringBuilder stringBuilder = new StringBuilder();
                                        if (!Locale.getDefault().getLanguage().equals("ar")) {

                                            for (int j = 0; j < objData.data.get(i).restaurant_setting.availability_days.size(); j++) {
                                                String strDay = objData.data.get(i).restaurant_setting.availability_days.get(j).day;
                                                //get From Time
                                                fromTime=objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.length()-3);
                                                fromTime=from24hour(fromTime);
                                                toTime = objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.length()-3);
                                                toTime=from24hour(toTime);

                                                stringBuilder.append(strDay + " " + getString(R.string.from) + fromTime + "\t" + getString(R.string.to) + toTime + "\n");
                                            }
                                            tvDateTime.setText(stringBuilder.toString() + getString(R.string.break_time) +"\t"+ getString(R.string.from) + "\t" + objData.data.get(i).restaurant_setting.break_from + getString(R.string.to) + objData.data.get(i).restaurant_setting.break_to);
                                        }
                                        //Arabic language available days
                                        else {
                                            for (int j = 0; j < objData.data.get(i).restaurant_setting.availability_days.size(); j++) {
                                                String strDay = mapWeekDays.get(objData.data.get(i).restaurant_setting.availability_days.get(j).day.trim().toString().toLowerCase());
                                                //get From Time
                                                fromTime=objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).open_at.length()-3);
                                                fromTime=from24hour(fromTime);
                                                toTime = objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.substring(0,objData.data.get(i).restaurant_setting.availability_days.get(j).close_at.length()-3);
                                                toTime=from24hour(toTime);
                                                stringBuilder.append(strDay + " " + getString(R.string.from) + fromTime + "\t" + getString(R.string.to) + toTime + "\n");
                                            }
                                            tvDateTime.setText(stringBuilder.toString() + getString(R.string.break_time) + getString(R.string.from) + "\t" + objData.data.get(i).restaurant_setting.break_from + getString(R.string.to) + objData.data.get(i).restaurant_setting.break_to);
                                        }
                                    } else {
                                        fromTime=objData.data.get(i).restaurant_setting.open_at.substring(0, objData.data.get(i).restaurant_setting.open_at.length()-3);
                                        fromTime=from24hour(fromTime);

                                        toTime=objData.data.get(i).restaurant_setting.close_at.substring(0, objData.data.get(i).restaurant_setting.close_at.length()-3);
                                        toTime=from24hour(toTime);

                                        tvDateTime.setText(getString(R.string.every_day) + "\t\t" + getString(R.string.from) + "\t" + fromTime + "\t\t" + getString(R.string.to) + "\t" + toTime + "\n");
                                        String str = tvDateTime.getText().toString();

                                        breakFrom=objData.data.get(i).restaurant_setting.break_from.substring(0,objData.data.get(i).restaurant_setting.break_from.length()-3);
                                        breakFrom=from24hour(breakFrom);
                                        breakTo=objData.data.get(i).restaurant_setting.break_to.substring(0,objData.data.get(i).restaurant_setting.break_to.length()-3);
                                        breakTo=from24hour(breakTo);
                                        String breakTime = getString(R.string.break_time) + "\t\t" + getString(R.string.from) + "\t" + breakFrom + "\t\t" + getString(R.string.to) + breakTo;
                                        tvDateTime.setText(str + "" + breakTime);
                                    }

                                }
                            }
                            if(objData.data.get(0).restaurant_menu.size()>0&objData.data.get(0).restaurant_menu!=null&&!objData.data.get(0).restaurant_menu.isEmpty()) {
                                lyMenu.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lyMenu.setVisibility(View.GONE);
                            }
                            showLocationMap(objData);
                        }
                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
                    if (objThrowable instanceof IOException) {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    } else {
                        DialogsHelper.getAlert(getActivity(), getString(R.string.error), getString(R.string.couldnt_complete_process), getString(R.string.ok), "", null, null).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String addLeftzer0(String number) {
        if (number.length() == 1)
            return "0" + number;
        else
            return number;
    }

    private String from24hour(String timeValue)
    {
        String Value;
        String[] arrTime = timeValue.split(":");
        if (arrTime[0].toString().equals("00")) {
            Value = "12"+":"+arrTime[1] + " "+getString(R.string.am);
        }
        else if(Integer.parseInt(arrTime[0].toString()) == 12)
        {
            Value = timeValue + " "+getString(R.string.pm);
        }
        else if(Integer.parseInt(arrTime[0].toString()) > 12)
        {
            int valueHour = Integer.parseInt(arrTime[0]) - 12;
            Value=addLeftzer0(String.valueOf(valueHour)) + ":" + arrTime[1] + " "+getString(R.string.pm);
        }
        else
        {
            Value=timeValue+" "+getString(R.string.am);
        }
        return Value;
    }
    private void showLocationMap(final RestaurantAPI objRestaurantAPI) {

        //add style for google map
      /*  mGoogleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));*/
        //hide loading bar
        pbarLoad.setVisibility(View.GONE);
        lyContain.setVisibility(View.VISIBLE);
        String[] loc = objRestaurantAPI.data.get(0).restaurant_info.location.split(",");
        lat = Double.parseDouble(loc[0]);
        lng = Double.parseDouble(loc[1]);
        latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (Locale.getDefault().getLanguage().equals("ar")) {

            markerOptions.title(objRestaurantAPI.data.get(0).restaurant_info.name_ar);
            markerOptions.snippet(objRestaurantAPI.data.get(0).restaurant_info.title_ar);

        }
        else
        {
            markerOptions.title(objRestaurantAPI.data.get(0).restaurant_info.name);
            markerOptions.snippet(objRestaurantAPI.data.get(0).restaurant_info.title);
        }
        // markerOptions.flat(false);
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin01));
        BitmapDescriptor iconMarker = bitmapDescriptorFromVector(getResources().getDrawable(R.drawable.ic_pin_t));
        // Drawable drawable=getResources().getDrawable(R.drawable.pin)
        markerOptions.icon(iconMarker);
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

        //check GPS permission
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            //show GPS Dialod Enable
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);//show or hide Current Location Button
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                latLng = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                if (Locale.getDefault().getLanguage().equals("ar")) {

                    markerOptions.title(objRestaurantAPI.data.get(0).restaurant_info.name_ar);
                    markerOptions.snippet(objRestaurantAPI.data.get(0).restaurant_info.title_ar);

                }
                else
                {
                    markerOptions.title(objRestaurantAPI.data.get(0).restaurant_info.name);
                    markerOptions.snippet(objRestaurantAPI.data.get(0).restaurant_info.title);
                }

                BitmapDescriptor iconMarker = bitmapDescriptorFromVector(getResources().getDrawable(R.drawable.ic_pin_t));
                // Drawable drawable=getResources().getDrawable(R.drawable.pin)
                markerOptions.icon(iconMarker);
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                return true;
            }
        });
        if(mylocation!=null) {
            //geDistance(String.valueOf(mylocation.getLatitude()) + "," + String.valueOf(mylocation.getLongitude()), String.valueOf(lat) + "," + String.valueOf(lng));
            geDistance(String.valueOf(mylocation.getLatitude()) + "," + String.valueOf(mylocation.getLongitude()), "21.6432469" + "," + "39.1307988");

        }
        else
        {
            setUpGClient();
            if(mylocation!=null) {
                geDistance(String.valueOf(mylocation.getLatitude()) + "," + String.valueOf(mylocation.getLongitude()), "21.6432469" + "," + "39.1307988");
            }
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void geDistance(String origin,String dest)
    {
        try {
            ApiEndpointInterface objApiEndpointInterface = ApiEndPoint.getClientAPI("").create(ApiEndpointInterface.class);
            Call<JsonObject> objCall = objApiEndpointInterface.getDistance(origin,dest);

            objCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        DistanceAPI objData = new Gson().fromJson(response.body(), DistanceAPI.class);
                        if(objData!=null&&objData.rows.size()>0) {
                              //  for (int i = 0; i < objData.rows.size(); i++) {
                                    String distance=objData.rows.get(0).elements.get(0).distance.text;
                                    String time=objData.rows.get(0).elements.get(0).duration.text;
                                    tvDistance.setText(distance);
                                    tvDuration.setText(time);
                                    rlDistance.setVisibility(View.VISIBLE);
                            if (Locale.getDefault().getLanguage().equals("ar")) {
                                AppConfigHelper.setLayoutAnim_slideLeftfromRight(rlDistance,getActivity());

                            }
                            else
                            {
                                AppConfigHelper.setLayoutAnim_slideRightfromLeft(rlDistance,getActivity());

                            }
                                // }
                        }
                        else
                        {
                            rlDistance.setVisibility(View.GONE);
                        }

                    } catch (Exception objException) {
                        objException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable objThrowable) {
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
            ex.printStackTrace();
        }
    }

    private synchronized void setUpGClient() {
        try {

            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
       /* if (mylocation != null) {
            Double latitude=mylocation.getLatitude();
            Double longitude=mylocation.getLongitude();
           * latitudeTextView.setText("Latitude : "+latitude);
            longitudeTextView.setText("Longitude : "+longitude);
            //Or Do whatever you want with your location
        }*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation(){
        try {

            if (googleApiClient != null) {
                if (googleApiClient.isConnected()) {
                    int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(3000);
                        locationRequest.setFastestInterval(3000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);
                        builder.setAlwaysShow(true);
                        LocationServices.FusedLocationApi
                                .requestLocationUpdates(googleApiClient, locationRequest, this);
                        PendingResult<LocationSettingsResult> result =
                                LocationServices.SettingsApi
                                        .checkLocationSettings(googleApiClient, builder.build());
                        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                            @Override
                            public void onResult(LocationSettingsResult result) {
                                final Status status = result.getStatus();
                                switch (status.getStatusCode()) {
                                    case LocationSettingsStatusCodes.SUCCESS:
                                        // All location settings are satisfied.
                                        // You can initialize location requests here.
                                        int permissionLocation = ContextCompat
                                                .checkSelfPermission(getActivity(),
                                                        Manifest.permission.ACCESS_FINE_LOCATION);
                                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                            mylocation = LocationServices.FusedLocationApi
                                                    .getLastLocation(googleApiClient);
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        // Location settings are not satisfied.
                                        // But could be fixed by showing the user a dialog.
                                        try {
                                            // Show the dialog by calling startResolutionForResult(),
                                            // and check the result in onActivityResult().
                                            // Ask to turn on GPS automatically
                                            status.startResolutionForResult(getActivity(),
                                                    REQUEST_CHECK_SETTINGS_GPS);
                                        } catch (IntentSender.SendIntentException e) {
                                            // Ignore the error.
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        // Location settings are not satisfied.
                                        // However, we have no way
                                        // to fix the
                                        // settings so we won't show the dialog.
                                        // finish();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                   *//* case Activity.RESULT_CANCELED:
                        finish();
                        break;*//*
                }
                break;
        }
    }*/

    private void checkPermissions(){
        try {

            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(getActivity(),
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            } else {
                getMyLocation();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {

            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
