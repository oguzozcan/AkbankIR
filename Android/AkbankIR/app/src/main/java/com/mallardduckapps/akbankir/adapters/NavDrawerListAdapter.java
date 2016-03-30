package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.R;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 15/02/16.
 */
public class NavDrawerListAdapter extends BaseExpandableListAdapter {
    //private final Activity context;
    private final String[] names;
    private final Integer[] imageId;
    //private final AkbankApp app;
    private final ArrayList<Object> subMenus;
    private final LayoutInflater inflater;

    public NavDrawerListAdapter(Activity context,
                                String[] names, Integer[] imageId, ArrayList<Object> subMenus) {
        //super(context, R.layout.row_navigation_drawer, names);
        //this.context = context;
        this.names = names;
        this.imageId = imageId;
        this.subMenus = subMenus;
        inflater = context.getLayoutInflater();
        //app = (AkbankApp) context.getApplication();
    }

    @Override
    public int getGroupCount() {
        return names.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return ((String[])subMenus.get(i)).length;
    }

    @Override
    public Object getGroup(int i) {
        return names[i];
    }

    @Override
    public Object getChild(int i, int childPosition) {
        return ((String[])subMenus.get(i))[childPosition];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {

        if(view == null){
            view = inflater.inflate(R.layout.row_navigation_drawer, null);
        }
        TextView txtTitle = (TextView) view.findViewById(R.id.text);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        ImageView downArrow = (ImageView) view.findViewById(R.id.downArrow);
        if(position == 6 || position == 1){
            downArrow.setImageResource(R.drawable.menu_down_arrow_icon);
        }else{
            downArrow.setImageResource(android.R.color.transparent);
        }
        txtTitle.setText(names[position]);
        //txtTitle.setTypeface(FTUtils.loadFont(context.getAssets(),context.getString(R.string.font_helvatica_lt)));
        imageView.setImageResource(imageId[position]);
        imageView.setFocusable(false);
        view.setTag(names[position]);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        //tempChild = (ArrayList<String>) subMenus.get(groupPosition);
        final String child = (String)getChild(groupPosition,childPosition);
        Log.d("NAV DRAWER CHILD", "CHILD: " + child);
        if(child == null){
            return null;
        }
        if(view == null){
            view = inflater.inflate(R.layout.row_sub_menu_navigation_drawer, null);//viewgroup
        }

        TextView text = (TextView)view;
        text.setText(child);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, child,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        view.setTag(child);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int childPosition) {
        return true;
    }
}