package com.mallardduckapps.akbankir;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.adapters.NavDrawerListAdapter;
import com.mallardduckapps.akbankir.fragments.BaseFragment;
import com.mallardduckapps.akbankir.utils.Constants;
import com.mallardduckapps.akbankir.utils.Utils;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by oguzemreozcan on 15/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerLinearLayout;
    protected FrameLayout mContent;
    private ActionBarDrawerToggle mDrawerToggle;
    protected AkbankApp app;
    protected TextView toolbarTitle;
    protected String TAG = "BaseActivity";

    protected abstract void setTag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        setTag();
        mContent = (FrameLayout) findViewById(R.id.content);
        app = (AkbankApp) getApplication();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (RelativeLayout) findViewById(R.id.right_drawer);
        final ExpandableListView mDrawerList = (ExpandableListView) findViewById(R.id.drawer_menu);
        //TODO
        mDrawerList.setChildIndicator(null);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                 /* nav drawer icon to replace 'Up' caret */  //R.drawable.menu_icon,
                R.string.Cancel,  /* "open drawer" description */
                R.string.Ok  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        final String[] menuItems = new String[]{
                getString(R.string.Menu_Dashboard),
                getString(R.string.Menu_InvestorToolkit),
                getString(R.string.Menu_AnnouncementsAndNews),
                getString(R.string.Menu_Stocks),
                getString(R.string.Menu_Calendar),
                getString(R.string.Menu_AnalystCoverage),
                getString(R.string.Menu_CorporateGovernance),
                getString(R.string.Menu_AboutTurkey),
                getString(R.string.Menu_IRTeam),
                getString(R.string.Menu_SavedDocuments)
        };

        Integer[] imageId = {
                R.drawable.menu_dashboard_icon,
                R.drawable.menu_investor_toolkit_icon,
                R.drawable.menu_news_icon,
                R.drawable.menu_stocks_icon,
                R.drawable.menu_calendar_icon,
                R.drawable.menu_analyst_coverage_icon,
                R.drawable.menu_corporate_governance_icon,
                R.drawable.menu_about_turkey_icon,
                R.drawable.menu_ir_team_icon,
                R.drawable.menu_saved_documents_icon
        };
        init();

        NavDrawerListAdapter adapter = new NavDrawerListAdapter(this, menuItems, imageId, getSubMenuItems());
        mDrawerList.setAdapter(adapter); //new ArrayAdapter<String>(this,         R.layout.row_navigation_drawer, menuItems)
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener(mDrawerList));
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
                Log.d(TAG, "ON GROUP CLICK: " + position);
                selectItem(position, mDrawerList);
                onTitleTextChange(menuItems[position]);
                mDrawerList.setItemChecked(position, true);
                //setTitle(mPlanetTitles[position]);
                return true;
            }


        });
        //expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
//        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int position, int childPosition, long l) {
                Log.d(TAG, "ON GROUP CLICK: " + position + "- childPosition: " + childPosition);
                if (position == 6) {
                    Intent intent = new Intent(BaseActivity.this, WebActivity.class);
                    switch (childPosition) {
                        case 0:
                            intent.putExtra("file_name", "corporate_governance.html");
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Corparate));
                            intent.putExtra("type", "web");
                            break;
                        case 1:
                            intent.putExtra("file_name", "articles_of_association.html");
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Articles));
                            intent.putExtra("type", "web");
                            break;
                        case 2:
                            intent.putExtra("file_name", "renumeration-policy.pdf");
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Renumuration));
                            intent.putExtra("type", "pdf");
                            break;
                        case 3:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Annual));
                            intent.putExtra("type", "web");
                            break;
                        case 4:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_About));
                            intent.putExtra("type", "web");
                            break;
                        case 5:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Capital));
                            intent.putExtra("type", "web");
                            break;
                        case 6:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Ethnical));
                            intent.putExtra("type", "web");
                            break;
                        case 7:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Compliance));
                            intent.putExtra("type", "web");
                            break;
                        case 8:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Compensation));
                            intent.putExtra("type", "web");
                            break;
                        case 9:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Donation));
                            intent.putExtra("type", "web");
                            break;
                        case 10:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Disclosure));
                            intent.putExtra("type", "web");
                            break;
                        case 12:
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Divident_Policy));
                            intent.putExtra("type", "web");
                            break;
                        case 11:
                            intent.putExtra("file_name", "anti-bribery-anti-corruption-policy.pdf");
                            intent.putExtra("title", getString(R.string.Sub_Menu_2_Anti_Bribery));
                            intent.putExtra("type", "pdf");
                            break;

                    }
                    intent.putExtra("position", childPosition);
                    BaseActivity.this.startActivity(intent);
                } else if (position == 1) {
                    switch (childPosition) {
                        case 0:
                            Intent earningIntent = new Intent(BaseActivity.this, EarningPresentationActivity.class);
                            BaseActivity.this.startActivity(earningIntent);
                            break;
                        case 1:
                            Intent investorIndent = new Intent(BaseActivity.this, InvestorPresentationActivity.class);
                            BaseActivity.this.startActivity(investorIndent);
                            break;
                        case 2:
                            Intent daysIndent = new Intent(BaseActivity.this, InvestorDaysActivity.class);
                            BaseActivity.this.startActivity(daysIndent);
                            break;
                        case 3:
                            Intent aReportIntent = new Intent(BaseActivity.this, AnnualReportsActivity.class);
                            BaseActivity.this.startActivity(aReportIntent);
                            break;
                        case 4:
                            Intent webcastsIntent = new Intent(BaseActivity.this, WebcastsActivity.class);
                            BaseActivity.this.startActivity(webcastsIntent);
                            break;
                        case 5:
                            Intent sReportIntent = new Intent(BaseActivity.this, SustainabilityReportActivity.class);
                            BaseActivity.this.startActivity(sReportIntent);
                            break;
                    }
                }
                return false;
            }
        });

        ImageView contactEmail = (ImageView) findViewById(R.id.menuEmailIcon);
        contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sendMail("", Constants.CONTACT_MAIL, getString(R.string.ContactUs), null, BaseActivity.this);
            }
        });
        ImageView fbButton = (ImageView) findViewById(R.id.fbIcon);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FB_LINK));
                startActivity(browserIntent);
            }
        });
        ImageView twitterButton = (ImageView) findViewById(R.id.twitterIcon);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TWITTER_LINK));
                startActivity(browserIntent);
            }
        });

        ImageView languageButton = (ImageView) findViewById(R.id.menuLanguageIcon);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termsIntent = new Intent(BaseActivity.this, TermsActivity.class);
                BaseActivity.this.startActivity(termsIntent);
            }
        });
    }

    void init() {
        //Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mainToolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            Log.d(TAG, "ACTIONBAR NOT NULL");
            assert actionBar != null;
            actionBar.setTitle(""); //        actionBar.setHomeButtonEnabled(true);
        }

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        onTitleTextChange("");
        //fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void drawerButtonHandler(View view) {
        Log.d("BASE ACTIVITY", "DRAWER BUTTON CLICKED");
        if (mDrawerLayout.isDrawerOpen(mDrawerLinearLayout)) {
            mDrawerLayout.closeDrawer(mDrawerLinearLayout);
        } else {
            // open the drawer
            mDrawerLayout.openDrawer(mDrawerLinearLayout);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTitleTextChange(String text) {
        if(toolbarTitle != null)
            toolbarTitle.setText(text);
    }

//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        final ExpandableListView mListView;
//
//        public DrawerItemClickListener(ExpandableListView mListView){
//            this.mListView = mListView;
//        }
//        @Override
//        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            selectItem(position, mListView);
//        }
//    }

    private void selectItem(int position, ExpandableListView mListView) {
        // Highlight the selected item, update the title, and close the drawer
        switch (position) {
            case 0:
                Intent dashboardIntent = new Intent(BaseActivity.this, ItemListActivity.class);
                BaseActivity.this.startActivity(dashboardIntent);
                break;
            case 1:
            case 6:
                if (mListView.isGroupExpanded(position)) {
                    mListView.collapseGroup(position);
                } else {
                    mListView.expandGroup(position, true);
                }
                break;
            case 2:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent newsIntent = new Intent(this, AnnouncementAndNewsActivity.class);
                this.startActivity(newsIntent);
                break;
            case 3:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intent = new Intent(this, ItemDetailActivity.class);
                //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                this.startActivity(intent);

                break;
            case 4:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intentCalendar = new Intent(this, CalendarActivity.class);
                this.startActivity(intentCalendar);
                break;
            case 5:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent covarageIntent = new Intent(this, AnalystCovarageActivity.class);
                this.startActivity(covarageIntent);
                break;
            case 7:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intentAboutTurkey = new Intent(this, AboutTurkeyActivity.class);
                this.startActivity(intentAboutTurkey);
                break;
            case 8:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intentIR = new Intent(this, IRTeamActivity.class);
                this.startActivity(intentIR);
                break;
            case 9:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intentSD = new Intent(this, SavedDocumentsActivity.class);
                this.startActivity(intentSD);
                break;


        }
        //mListView.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
//        if(mDrawerLayout != null)
//            mDrawerLayout.closeDrawer(mListView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private ArrayList<Object> getSubMenuItems() {
        ArrayList<Object> childItems = new ArrayList<>();
        String[] emptyArray = new String[0];
        childItems.add(emptyArray);
        String[] investorMenu = new String[]{
                getString(R.string.Sub_Menu_1_Financial),
                getString(R.string.Sub_Menu_1_Investor),
                getString(R.string.Sub_Menu_1_Akbank_Analyst),
                getString(R.string.Sub_Menu_1_Annual_Reports),
                getString(R.string.Sub_Menu_1_Webcasts),
                getString(R.string.Sub_Menu_1_Sustainability)
        };
        String[] corparateMenu = new String[]{
                getString(R.string.Sub_Menu_2_Corparate),
                getString(R.string.Sub_Menu_2_Articles),
                getString(R.string.Sub_Menu_2_Renumuration),
                getString(R.string.Sub_Menu_2_Annual),
                getString(R.string.Sub_Menu_2_About),
                getString(R.string.Sub_Menu_2_Capital),
                getString(R.string.Sub_Menu_2_Ethnical),
                getString(R.string.Sub_Menu_2_Compliance),
                getString(R.string.Sub_Menu_2_Compensation),
                getString(R.string.Sub_Menu_2_Donation),
                getString(R.string.Sub_Menu_2_Disclosure),
                getString(R.string.Sub_Menu_2_Anti_Bribery),
                getString(R.string.Sub_Menu_2_Divident_Policy)
        };
        childItems.add(investorMenu);
        childItems.add(emptyArray);
        childItems.add(emptyArray);
        childItems.add(emptyArray);
        childItems.add(emptyArray);
        childItems.add(corparateMenu);
        childItems.add(emptyArray);
        childItems.add(emptyArray);
        return childItems;
    }
}
