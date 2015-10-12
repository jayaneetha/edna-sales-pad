package com.colombosoft.ednasalespad;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.colombosoft.ednasalespad.fragment.OutletDetailsCompetitorFragment;
import com.colombosoft.ednasalespad.fragment.OutletDetailsHistoryFragment;
import com.colombosoft.ednasalespad.fragment.OutletDetailsOutstandingFragment;
import com.colombosoft.ednasalespad.fragment.OutletDetailsPersonalFragment;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Outlet;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class DealerDetailsActivity extends ActionBarActivity {

    private CircleButton floatingActionsMenu;
    private CircleButton fabInvoice, fabUnproductive, fabMerchandising, fabPayment;
    private TextView labelInvoice, labelUnproductive, labelMerchandise, labelPayment;

    private Outlet outlet;

    private View overlay;

    private boolean famOpen = false;

    private DatabaseHandler dbHandler;
    private SharedPref sharedPref;

    private List<Integer> visitStatuses = new ArrayList<Integer>();

    private boolean invoiced = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_details);

        dbHandler = DatabaseHandler.getDbHandler(DealerDetailsActivity.this);
        sharedPref = SharedPref.getInstance(DealerDetailsActivity.this);

        final LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("outlet")) {
            outlet = (Outlet) dataIntent.getExtras().get("outlet");
            if (outlet == null) {
                Toast.makeText(DealerDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        } else {
            Toast.makeText(DealerDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.outlet_details_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(outlet.getOutletName());

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.outlet_details_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.outlet_details_viewpager);

        floatingActionsMenu = (CircleButton) findViewById(R.id.outlet_details_floating_action_menu);
        floatingActionsMenu.setColor(getResources().getColor(R.color.light_800));
        floatingActionsMenu.setImageDrawable(getResources().getDrawable(R.drawable.add));

        fabInvoice = (CircleButton) findViewById(R.id.outlet_details_fab_invoice);
        fabUnproductive = (CircleButton) findViewById(R.id.outlet_details_fab_unproductive);
        fabMerchandising = (CircleButton) findViewById(R.id.outlet_details_fab_merchandising);
        fabPayment = (CircleButton) findViewById(R.id.outlet_details_fab_payment);

        labelInvoice = (TextView) findViewById(R.id.outlet_details_label_invoice);
        labelUnproductive = (TextView) findViewById(R.id.outlet_details_label_unproductive);
        labelMerchandise = (TextView) findViewById(R.id.outlet_details_label_merchandising);
        labelPayment = (TextView) findViewById(R.id.outlet_details_label_payment);

        labelInvoice.setTextColor(getResources().getColor(R.color.white));
        labelUnproductive.setTextColor(getResources().getColor(R.color.white));
        labelMerchandise.setTextColor(getResources().getColor(R.color.white));
        labelPayment.setTextColor(getResources().getColor(R.color.white));

        // Make the label sized small so they can be scaled up.
        labelInvoice.setScaleX(0);
        labelInvoice.setScaleY(0);

        labelUnproductive.setScaleX(0);
        labelUnproductive.setScaleY(0);

        labelMerchandise.setScaleX(0);
        labelMerchandise.setScaleY(0);

        labelPayment.setScaleX(0);
        labelPayment.setScaleY(0);

        // Setting the expanded options button colors
        fabInvoice.setColor(getResources().getColor(R.color.light_50));
        fabUnproductive.setColor(getResources().getColor(R.color.light_50));
        fabMerchandising.setColor(getResources().getColor(R.color.light_50));
        fabPayment.setColor(getResources().getColor(R.color.light_50));

        // Setting the expanded options button drawables
        fabInvoice.setImageDrawable(getResources().getDrawable(R.drawable.bill));
        fabUnproductive.setImageDrawable(getResources().getDrawable(R.drawable.questions));
        fabMerchandising.setImageDrawable(getResources().getDrawable(R.drawable.shop));
        fabPayment.setImageDrawable(getResources().getDrawable(R.drawable.banknotes));

        // Scale down the invisible FAB options
        fabInvoice.setScaleX(0);
        fabInvoice.setScaleY(0);

        fabUnproductive.setScaleX(0);
        fabUnproductive.setScaleY(0);

        fabMerchandising.setScaleX(0);
        fabMerchandising.setScaleY(0);

        fabPayment.setScaleX(0);
        fabPayment.setScaleY(0);

        // Setting the FAM drawable
        floatingActionsMenu.setImageDrawable(getResources().getDrawable(R.drawable.add));

        // The overlay when showing expanding the menu
        overlay = findViewById(R.id.outlet_details_view_overlay);
        overlay.setAlpha(0);

        // Expanding/Collapsing the FAM when pressed on the FAM
        floatingActionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (famOpen) {
                    closeFAM();
                } else {
                    openFAM();
                }
            }
        });

        // Collapsing the FAM when pressed on the overlay
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (famOpen) {
                    closeFAM();
                }
            }
        });



        OutletDetailsPagerAdapter adapter = new OutletDetailsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);


        slidingTabStrip.setViewPager(viewPager);
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.color_dark));

        fabUnproductive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAM();
                Toast.makeText(DealerDetailsActivity.this, "Unproductive", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DealerDetailsActivity.this, UnproductiveCallActivity.class));
            }
        });

        fabInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAM();
                Toast.makeText(DealerDetailsActivity.this, "Invoice", Toast.LENGTH_SHORT).show();
            }
        });

        fabPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAM();
                Toast.makeText(DealerDetailsActivity.this, "Invoice", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Function to open the FAM with custom flowing animation
     */
    private void openFAM() {
        ViewCompat.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
//        ViewPropertyAnimator.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
        ViewCompat.animate(overlay).alpha(1).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                // Set the visibility to visible so the animation will be visible
                fabInvoice.setVisibility(View.VISIBLE);
                if (!invoiced) {
                    fabUnproductive.setVisibility(View.VISIBLE);
                }
                fabMerchandising.setVisibility(View.VISIBLE);
                fabPayment.setVisibility(View.VISIBLE);

                labelInvoice.setVisibility(View.VISIBLE);
                if (!invoiced) {
                    labelUnproductive.setVisibility(View.VISIBLE);
                }
                labelMerchandise.setVisibility(View.VISIBLE);
                labelPayment.setVisibility(View.VISIBLE);
                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        // Scale in the FABs one by one
        ViewCompat.animate(fabPayment).scaleX(1).scaleY(1).setDuration(200).setStartDelay(0);
        ViewCompat.animate(labelPayment).scaleX(1).scaleY(1).setDuration(200).setStartDelay(0);

        if (invoiced) {
            ViewCompat.animate(fabMerchandising).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);
            ViewCompat.animate(labelMerchandise).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);

//            ViewCompat.animate(fabUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);

            ViewCompat.animate(fabInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
            ViewCompat.animate(labelInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
        } else {
            ViewCompat.animate(fabMerchandising).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);
            ViewCompat.animate(labelMerchandise).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);

            ViewCompat.animate(fabUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
            ViewCompat.animate(labelUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);

            ViewCompat.animate(fabInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);
            ViewCompat.animate(labelInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);
        }

        famOpen = true;
    }

    /**
     * Function to close the FAM with custom flowing animation
     */
    private void closeFAM() {
        if (invoiced) {
            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(400);
        } else {
            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(500);
        }

        ViewCompat.animate(overlay).alpha(0).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                overlay.setVisibility(View.GONE);
                // Set the visibility to visible to the animation will show
                labelInvoice.setVisibility(View.GONE);
                labelUnproductive.setVisibility(View.GONE);
                labelMerchandise.setVisibility(View.GONE);
                labelPayment.setVisibility(View.GONE);
                fabInvoice.setVisibility(View.GONE);
                fabUnproductive.setVisibility(View.GONE);
                fabMerchandising.setVisibility(View.GONE);
                fabPayment.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        // Scale out the FABs one by one
        ViewCompat.animate(fabInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(0);
        ViewCompat.animate(labelInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(0);

        if (invoiced) {
//            ViewCompat.animate(fabUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);
//            ViewCompat.animate(labelUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);

            ViewCompat.animate(fabMerchandising).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);
            ViewCompat.animate(labelMerchandise).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);

            ViewCompat.animate(fabPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);
            ViewCompat.animate(labelPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);
        } else {
            ViewCompat.animate(fabUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);
            ViewCompat.animate(labelUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);

            ViewCompat.animate(fabMerchandising).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);
            ViewCompat.animate(labelMerchandise).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);

            ViewCompat.animate(fabPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);
            ViewCompat.animate(labelPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);
        }

        famOpen = false;
    }

    @Override
    public void onBackPressed() {
        if (famOpen) {
            closeFAM();
        } else {
            startActivity(new Intent(DealerDetailsActivity.this, SelectDealerActivity.class));
            finish();
        }
    }

    private class OutletDetailsPagerAdapter extends FragmentPagerAdapter {
        private final String[] titles = {"PERSONAL", "OUTSTANDING", "HISTORY", "COMPETITORS"};

        public OutletDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new OutletDetailsPersonalFragment();
                case 1:
                    return new OutletDetailsOutstandingFragment();
                case 2:
                    return new OutletDetailsHistoryFragment();
                case 3:
                    return new OutletDetailsCompetitorFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
