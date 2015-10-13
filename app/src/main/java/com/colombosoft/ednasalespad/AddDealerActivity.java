package com.colombosoft.ednasalespad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.adapter.OutletClassAdapter;
import com.colombosoft.ednasalespad.adapter.OutletTypeAdapter;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Outlet;
import com.colombosoft.ednasalespad.model.OutletClass;
import com.colombosoft.ednasalespad.model.OutletType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddDealerActivity extends ActionBarActivity {

    final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    final int REQUEST_IMAGE_CAPTURE_SHOWCASE = 2;
    final int REQUEST_IMAGE_CAPTURE_PROMOTION_1 = 3;
    final int REQUEST_IMAGE_CAPTURE_PROMOTION_2 = 4;
    final String OUTLET_DIR = new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/EDNA/Outlets/").toString();
    Outlet outlet;
    EditText outletName, address, ownerName, land, mobile;
    TextView location_tv;
    Button add_btn;
    Spinner outletType, outletClass;
    List<OutletType> listOutletType;
    List<OutletClass> listOutletClass;
    Location finalLocation = null;
    LocationManager locationManager;
    Boolean gpsActive, networkActive;
    AddDealerLocationListener locationListener;
    int routeId;
    ImageView shopFront, showcase, promotion_1, promotion_2;
    RelativeLayout shopFrontNA, showcaseNA, promotion_1NA, promotion_2NA;
    String captured_image_name = null;
    private DatabaseHandler dbHandler;
    private SharedPref pref;
    private String LOG_TAG = AddDealerActivity.class.getSimpleName();
    private Runnable switchRunnable;
    private boolean locSwitch;
    private Handler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dealer);

        dbHandler = DatabaseHandler.getDbHandler(AddDealerActivity.this);
        pref = SharedPref.getInstance(AddDealerActivity.this);

//For Testing purpose only
        //TODO: Remove in production
        if (!dbHandler.isOutletTypesAvailable()) {
            OutletType outletT = new OutletType(1, "one");
            OutletType outletT1 = new OutletType(2, "two");
            OutletType outletT2 = new OutletType(3, "three");
            OutletType outletT3 = new OutletType(4, "four");

            List<OutletType> list = new ArrayList<OutletType>();
            list.add(outletT);
            list.add(outletT2);
            list.add(outletT3);

            dbHandler.storeOutletTypes(list);

            List<OutletClass> list2 = new ArrayList<OutletClass>();

            list2.add(new OutletClass(1, "A"));
            list2.add(new OutletClass(3, "C"));
            list2.add(new OutletClass(4, "D"));
            list2.add(new OutletClass(5, "E"));
            list2.add(new OutletClass(6, "F"));

            dbHandler.storeOutletClasses(list2);
        }


        outletName = (EditText) findViewById(R.id.add_dealer_personal_et_outlet);
        address = (EditText) findViewById(R.id.add_dealer_personal_et_address);
        ownerName = (EditText) findViewById(R.id.add_dealer_personal_et_owner);
        land = (EditText) findViewById(R.id.add_dealer_personal_et_contact_land);
        mobile = (EditText) findViewById(R.id.add_dealer_personal_et_contact_mob);
        mobile = (EditText) findViewById(R.id.add_dealer_personal_et_contact_mob);
        location_tv = (TextView) findViewById(R.id.add_dealer_personal_tv_location);
        outletType = (Spinner) findViewById(R.id.add_dealer_personal_sp_type);
        outletClass = (Spinner) findViewById(R.id.add_dealer_personal_sp_class);
        add_btn = (Button) findViewById(R.id.add_dealer_personal_btn_add);

        shopFront = (ImageView) findViewById(R.id.add_dealer_personal_iv_front_image_view);
        showcase = (ImageView) findViewById(R.id.add_dealer_personal_iv_show_image_view);
        promotion_1 = (ImageView) findViewById(R.id.add_dealer_personal_iv_promotion_1_view);
        promotion_2 = (ImageView) findViewById(R.id.add_dealer_personal_iv_promotion_2_view);

        shopFrontNA = (RelativeLayout) findViewById(R.id.add_dealer_personal_rel_front_image_view_na);
        showcaseNA = (RelativeLayout) findViewById(R.id.add_dealer_personal_rel_show_image_view_na);
        promotion_1NA = (RelativeLayout) findViewById(R.id.add_dealer_personal_rel_promotion_1_na);
        promotion_2NA = (RelativeLayout) findViewById(R.id.add_dealer_personal_rel_promotion_2_na);


        locationManager = (LocationManager) AddDealerActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new AddDealerLocationListener();
        locationHandler = new Handler();

        routeId = pref.getSelectedRoute().getRouteId();
        outlet = new Outlet();

        //Random Id for the outlet-id
        int tempOutletId;
        do {
            tempOutletId = new Random().nextInt(99999);
        } while (dbHandler.isOutletIdAvailable(tempOutletId));

        outlet.setOutletId(tempOutletId);
        outlet.setRouteId(routeId);

        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            gpsActive = false;
        }
        try {
            networkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            networkActive = false;
        }

        requestUpdates(); //request GPS Coordinates

        listOutletClass = dbHandler.getOutletClasses();
        listOutletType = dbHandler.getOutletTypes();

        outletType.setAdapter(new OutletTypeAdapter(this, listOutletType));
        outletClass.setAdapter(new OutletClassAdapter(this, listOutletClass));

        //Click Listeners
        shopFrontNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_front.jpg";
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_FRONT, imageName);
            }
        });

        showcaseNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_showcase.jpg";
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_SHOWCASE, imageName);
            }
        });

        promotion_1NA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_promotion_1.jpg";
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_PROMOTION_1, imageName);
            }
        });

        promotion_2NA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_promotion_2.jpg";
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_PROMOTION_2, imageName);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalLocation != null) {
                    outlet.setOutletName(outletName.getText().toString());
                    outlet.setAddress(address.getText().toString());
                    outlet.setOwnerName(ownerName.getText().toString());
                    outlet.setContactLand(land.getText().toString());
                    outlet.setOutletType((int) outletType.getSelectedItemId());
                    outlet.setOutletClass((int) outletClass.getSelectedItemId());
                    dbHandler.storeOutlet(outlet);
                    onBackPressed();

                } else {
                    Toast.makeText(AddDealerActivity.this, "Please wait. Application is still fetching the location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestUpdates() {
        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

        try {
            networkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        if (networkActive) {
            if (gpsActive) {
                // Request timed location update using GPS
                requestGPSLocation();

                if (switchRunnable == null) {
                    switchRunnable = new Runnable() {
                        @Override
                        public void run() {
                            locationManager.removeUpdates(locationListener);
                            requestNetworkLocation();
                            locSwitch = true;
                        }
                    };
                }

                // Stop getting the location from GPS  and get from network if taking too long
                locationHandler.postDelayed(switchRunnable, 10 * 1000);
            } else {
                // Request location update using network
                requestNetworkLocation();
            }
        } else {
            if (gpsActive) {
                // Only GPS active. Request location from GPS provider
                requestGPSLocation();
            } else {
                Toast.makeText(AddDealerActivity.this, "Please enable location service and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestGPSLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        location_tv.setText("Getting location (GPS)");
    }

    private void requestNetworkLocation() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        location_tv.setText("Getting location (Network)");
    }

    @Override
    public void onBackPressed() {
        if (switchRunnable != null && !locSwitch) {
            locationHandler.removeCallbacks(switchRunnable);
        }
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            Uri imageUri = Uri.fromFile(new File(OUTLET_DIR, captured_image_name));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE_FRONT:
                    //Front
                    outlet.setFrontImageURI(imageUri.toString());
                    shopFront.setImageBitmap(bitmap);
                    shopFront.setVisibility(View.VISIBLE);
                    shopFrontNA.setVisibility(View.GONE);
                    break;
                case REQUEST_IMAGE_CAPTURE_SHOWCASE:
                    //Showcase
                    outlet.setShowcaseImageUri(imageUri.toString());
                    showcase.setImageBitmap(bitmap);
                    showcase.setVisibility(View.VISIBLE);
                    showcaseNA.setVisibility(View.GONE);
                    break;
                case REQUEST_IMAGE_CAPTURE_PROMOTION_1:
                    //Promotion 1
                    outlet.setPromotion1ImageUri(imageUri.toString());
                    promotion_1.setImageBitmap(bitmap);
                    promotion_1.setVisibility(View.VISIBLE);
                    promotion_1NA.setVisibility(View.GONE);
                    break;
                case REQUEST_IMAGE_CAPTURE_PROMOTION_2:
                    //Promotion 2
                    outlet.setPromotion2ImageUri(imageUri.toString());
                    promotion_2.setImageBitmap(bitmap);
                    promotion_2.setVisibility(View.VISIBLE);
                    promotion_2NA.setVisibility(View.GONE);
                    break;
                default:
                    Log.e(LOG_TAG, "Invalid Request");
            }
            dbHandler.updateOutlet(outlet);
            outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());
        }
    }

    private void dispatchTakePictureIntent(int requestCode, String imageName) {
        captured_image_name = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(OUTLET_DIR, imageName)));
            //stores the image name captured.
            captured_image_name = imageName;
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private class AddDealerLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            finalLocation = location;
            locationManager.removeUpdates(this);
            location_tv.setText("Location: " + location.getLatitude() + ", " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (finalLocation == null) {
                Toast.makeText(AddDealerActivity.this, "Provider enabled. Accessing location", Toast.LENGTH_SHORT).show();
                requestUpdates();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(AddDealerActivity.this, "Provider disabled", Toast.LENGTH_SHORT).show();
        }
    }
}
