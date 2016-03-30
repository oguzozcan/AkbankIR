package com.mallardduckapps.akbankir;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mallardduckapps.akbankir.adapters.NavDrawerListAdapter;
import com.mallardduckapps.akbankir.fragments.BaseFragment;
import com.mallardduckapps.akbankir.fragments.ItemDetailFragment;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by oguzemreozcan on 15/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinearLayout;
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
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.right_drawer);
        final ExpandableListView mDrawerList = (ExpandableListView) findViewById(R.id.drawer_menu);
        //TODO
        mDrawerList.setChildIndicator(null);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                 /* nav drawer icon to replace 'Up' caret */  //R.drawable.menu_icon,
                R.string.Cancel,  /* "open drawer" description */
                R.string.Ok  /* "close drawer" description */
        ){
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
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(false);


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
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener(mDrawerList));
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
                Log.d(TAG, "ON GROUP CLICK: " + position);
//                if(position == 4){
//                    Intent intent = new Intent(BaseActivity.this, CalendarActivity.class);
//                    //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                    BaseActivity.this.startActivity(intent);
//                }
                onTitleTextChange(menuItems[position]);
                selectItem(position, mDrawerList);
                mDrawerList.setItemChecked(position, true);
                //setTitle(mPlanetTitles[position]);
//                if (position != 4 && position != 8 ) {
//                    mDrawerLayout.closeDrawer(mDrawerLinearLayout);
//                }

                return true;
            }


        });

        //expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
//        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int position, int childPosition, long l) {
                Log.d(TAG, "ON GROUP CLICK: " + position + "- childPosition: " + childPosition);
                if(position == 6){
                    Intent intent = new Intent(BaseActivity.this, WebActivity.class);
                    switch (childPosition){
                        case 0:
                            intent.putExtra("file_name", "corporate_governance.html");
                            intent.putExtra("title", "Corporate Governance");
                            intent.putExtra("type", "web");
                            break;
                        case 1:
                            intent.putExtra("file_name", "corporate_governance.html");
                            intent.putExtra("title", "Articles of Association");
                            intent.putExtra("type", "web");
                            break;
                        case 2:
                            intent.putExtra("file_name", "renumeration-policy.pdf");
                            intent.putExtra("title", "Renumeration Policy");
                            intent.putExtra("type", "pdf");
                            break;
                        case 3:
                            intent.putExtra("title", "Annual General Meeting");
                            intent.putExtra("type", "web");
                            break;
                        case 4:
                            intent.putExtra("title", "About Suzan Sabancı");
                            intent.putExtra("type", "web");
                            break;
                        case 5:
                            intent.putExtra("title", "Capital and Trade Registry");
                            intent.putExtra("type", "web");
                            break;
                        case 6:
                            intent.putExtra("title", "Ethnical Principles");
                            intent.putExtra("type", "web");
                            break;
                        case 7:
                            intent.putExtra("title", "Compliance");
                            intent.putExtra("type", "web");
                            break;
                        case 8:
                            intent.putExtra("title", "Compensation Policy");
                            intent.putExtra("type", "web");
                            break;
                        case 9:
                            intent.putExtra("title", "Donation");
                            intent.putExtra("type", "web");
                            break;
                        case 10:
                            intent.putExtra("title", "Disclosure Policy");
                            intent.putExtra("type", "web");
                            break;
                        case 12:
                            intent.putExtra("title", "Divident Policy");
                            intent.putExtra("type", "web");
                            break;
                        case 11:
                            intent.putExtra("file_name", "anti-bribery-anti-corruption-policy.pdf");
                            intent.putExtra("title", "Anti Bribery Anti Corruption Policy");
                            intent.putExtra("type", "pdf");
                            break;

                    }
                    BaseActivity.this.startActivity(intent);
                }else if(position == 1){

                }
                return false;
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
        //fragmentManager = getSupportFragmentManager();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void drawerButtonHandler(View view){
        Log.d("BASE ACTIVITY", "DRAWER BUTTON CLICKED");
        if(mDrawerLayout.isDrawerOpen(mDrawerLinearLayout)) {
            mDrawerLayout.closeDrawer(mDrawerLinearLayout);
        }
        else {
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
        switch (position){
            case 0:
                break;
            case 1:
            case 6:
                if(mListView.isGroupExpanded(position)){
                    mListView.collapseGroup(position);
                }else{
                    mListView.expandGroup(position, true);
                }
                break;
            case 2:
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
                //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                this.startActivity(intentCalendar);
                break;
            case 8:
                mDrawerLayout.closeDrawer(mDrawerLinearLayout);
                Intent intentIR = new Intent(this, IRTeamActivity.class);
                //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                this.startActivity(intentIR);
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

    private ArrayList<Object> getSubMenuItems(){
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
