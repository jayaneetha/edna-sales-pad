package com.colombosoft.ednasalespad.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Outlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class OutletDetailsPersonalFragment extends Fragment {


    final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    final int REQUEST_IMAGE_CAPTURE_SHOWCASE = 2;
    final int REQUEST_IMAGE_CAPTURE_PROMOTION_1 = 3;
    final int REQUEST_IMAGE_CAPTURE_PROMOTION_2 = 4;
    final String OUTLET_DIR = new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/EDNA/Outlets/").toString();
    private final String LOG_TAG = OutletDetailsPersonalFragment.class.getSimpleName();
    DatabaseHandler dbHandler;
    SharedPref pref;
    Outlet outlet;
    TextView tvOutletName, tvOutletAddress, tvOwnerName, tvOwnerDOB, tvContactLand, tvContactMob, tvAssistantName, tvAssistantDOB;
    ImageView shopFront, showcase, promotion_1, promotion_2;
    RelativeLayout shopFrontNA, showcaseNA, promotion_1NA, promotion_2NA;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_outlet_details_personal, container, false);


        dbHandler = DatabaseHandler.getDbHandler(getActivity());
        pref = SharedPref.getInstance(getActivity());

        tvOutletName = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_outlet);
        tvOutletAddress = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_address);
        tvOwnerName = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_owner);
        tvOwnerDOB = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_dob);

        outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());
        if (outlet == null) {
            getActivity().onBackPressed();
            Toast.makeText(getActivity(), "Invalid outlet", Toast.LENGTH_SHORT).show();
        } else {
            tvOutletName.setText(outlet.getOutletName());
            tvOutletAddress.setText(outlet.getAddress());
            tvOwnerName.setText(outlet.getOwnerName());
        }

        tvContactLand = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_contact_land);
        tvContactMob = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_contact_mob);
        tvAssistantName = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_assistant);
        tvAssistantDOB = (TextView) rootView.findViewById(R.id.fragment_out_det_personal_tv_as_dob);

        shopFront = (ImageView) rootView.findViewById(R.id.fragment_out_det_personal_iv_front_image_view);
        showcase = (ImageView) rootView.findViewById(R.id.fragment_out_det_personal_iv_show_image_view);
        promotion_1 = (ImageView) rootView.findViewById(R.id.fragment_out_det_personal_iv_promotion_1_view);
        promotion_2 = (ImageView) rootView.findViewById(R.id.fragment_out_det_personal_iv_promotion_2_view);

        shopFrontNA = (RelativeLayout) rootView.findViewById(R.id.fragment_out_det_personal_rel_front_image_view_na);
        showcaseNA = (RelativeLayout) rootView.findViewById(R.id.fragment_out_det_personal_rel_show_image_view_na);
        promotion_1NA = (RelativeLayout) rootView.findViewById(R.id.fragment_out_det_personal_rel_promotion_1_na);
        promotion_2NA = (RelativeLayout) rootView.findViewById(R.id.fragment_out_det_personal_rel_promotion_2_na);

        tvContactLand.setText("Land : " + outlet.getContactLand());

        if (outlet.getFrontImageURI() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(outlet.getFrontImageURI()));
                if (bitmap != null) {
                    shopFront.setImageBitmap(bitmap);
                    shopFront.setVisibility(View.VISIBLE);
                    shopFrontNA.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outlet.getShowcaseImageUri() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(outlet.getShowcaseImageUri()));
                if (bitmap != null) {
                    showcase.setImageBitmap(bitmap);
                    showcase.setVisibility(View.VISIBLE);
                    showcaseNA.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outlet.getPromotion1ImageUri() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(outlet.getPromotion1ImageUri()));
                if (bitmap != null) {
                    promotion_1.setImageBitmap(bitmap);
                    promotion_1.setVisibility(View.VISIBLE);
                    promotion_1NA.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outlet.getPromotion2ImageUri() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(outlet.getPromotion2ImageUri()));
                if (bitmap != null) {
                    promotion_2.setImageBitmap(bitmap);
                    promotion_2.setVisibility(View.VISIBLE);
                    promotion_2NA.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        shopFrontNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_FRONT);
            }
        });

        showcaseNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_SHOWCASE);
            }
        });

        promotion_1NA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_PROMOTION_1);
            }
        });

        promotion_2NA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_PROMOTION_2);
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        String imageName;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE_FRONT:
                //Front
                imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_front.jpg";
                File bitmapFileFront = saveBitmap(imageBitmap, imageName);
                if (bitmapFileFront != null) {
                    outlet.setFrontImageURI(Uri.fromFile(bitmapFileFront).toString());
                    shopFront.setImageBitmap(imageBitmap);
                    shopFront.setVisibility(View.VISIBLE);
                    shopFrontNA.setVisibility(View.GONE);
                }
                break;
            case REQUEST_IMAGE_CAPTURE_SHOWCASE:
                //Showcase
                imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_showcase.jpg";
                File bitmapFileShowcase = saveBitmap(imageBitmap, imageName);
                if (bitmapFileShowcase != null) {
                    outlet.setShowcaseImageUri(Uri.fromFile(bitmapFileShowcase).toString());
                    showcase.setImageBitmap(imageBitmap);
                    showcase.setVisibility(View.VISIBLE);
                    showcaseNA.setVisibility(View.GONE);
                }
                break;
            case REQUEST_IMAGE_CAPTURE_PROMOTION_1:
                //Promotion 1
                imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_promotion_1.jpg";
                File bitmapFilePromotion1 = saveBitmap(imageBitmap, imageName);
                if (bitmapFilePromotion1 != null) {
                    outlet.setPromotion1ImageUri(Uri.fromFile(bitmapFilePromotion1).toString());
                    promotion_1.setImageBitmap(imageBitmap);
                    promotion_1.setVisibility(View.VISIBLE);
                    promotion_1NA.setVisibility(View.GONE);
                }
                break;
            case REQUEST_IMAGE_CAPTURE_PROMOTION_2:
                //Promotion 2
                imageName = "Outlet_" + String.valueOf(outlet.getOutletId()) + "_promotion_2.jpg";
                File bitmapFilePromotion2 = saveBitmap(imageBitmap, imageName);
                if (bitmapFilePromotion2 != null) {
                    outlet.setPromotion2ImageUri(Uri.fromFile(bitmapFilePromotion2).toString());
                    promotion_2.setImageBitmap(imageBitmap);
                    promotion_2.setVisibility(View.VISIBLE);
                    promotion_2NA.setVisibility(View.GONE);
                }
                break;
            default:
                Log.e(LOG_TAG, "Invalid Request");
        }
        dbHandler.updateOutlet(outlet);
        outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());

    }

    private void dispatchTakePictureIntent(int request_code) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, request_code);
        }
    }

    private File saveBitmap(Bitmap bitmap, String name) {
        File file = new File(OUTLET_DIR);
        boolean flag = true;
        if (!file.exists()) {
            flag = file.mkdirs();
        }
        File image_file = null;
        if (flag) {
            //Saving the image
            image_file = new File(OUTLET_DIR, name);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(image_file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(LOG_TAG, "Saved image: " + OUTLET_DIR + name);
        return image_file;
    }



}
