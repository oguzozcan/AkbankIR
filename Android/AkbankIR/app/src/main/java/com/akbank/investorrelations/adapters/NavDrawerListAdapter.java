package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akbank.investorrelations.R;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 15/02/16.
 */
public class NavDrawerListAdapter extends BaseExpandableListAdapter {

    private final String[] names;
    private final Integer[] imageId;
    private final ArrayList<Object> subMenus;
    private final LayoutInflater inflater;
    //English Menu has one more item (investor presentation)
    private final boolean isEnglish;

    public NavDrawerListAdapter(Activity context,
                                String[] names, Integer[] imageId, ArrayList<Object> subMenus, final boolean isEnglish) {
        //super(context, R.layout.row_navigation_drawer, names);
        this.names = names;
        this.imageId = imageId;
        this.subMenus = subMenus;
        this.isEnglish = isEnglish;
        inflater = context.getLayoutInflater();
//        if(!isEnglish){
//
//        }

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

//        if(position == 1 && !isEnglish){
//            txtTitle.setTextSize(0);
//        }



        TextView text = (TextView)view;
        if(!isEnglish){
            if(groupPosition == 1 && childPosition == 1){
               // view.setVisibility(View.GONE);
                text.setTextSize(0);
                text.setPadding(0,0,0,0);
            }
        }
        text.setText(child);
        view.setTag(child);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int childPosition) {
        return true;
    }
}
