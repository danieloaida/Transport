package ro.ratt.transport;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by baby on 4/19/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listChild;
    private List<MapStation> mapStationList;

    public ExpandableListAdapter(Context context, List<String> listHeader, HashMap<String, List<String>> listChild, List<MapStation> mapStationList) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
        this.mapStationList = mapStationList;
    }



    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChild.get(this.listHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }


        CheckedTextView txtListChild = (CheckedTextView) convertView
                .findViewById(R.id.ctvItem);
        if (mapStationList.contains(new MapStation(childText))){
            txtListChild.setBackgroundColor(Color.CYAN);
        } else{
            txtListChild.setBackgroundColor(Color.WHITE);
        }
        int color;
        if (childText.contains("E")) color = Color.argb(200,0,180,0); else
        if (childText.contains("Tb")) color = Color.argb(200,145,25,255); else
        if (childText.contains("Tv")) color = Color.argb(200,30,155,255); else
            color = Color.argb(200,255,0,0);
        txtListChild.setTextColor(color);
        txtListChild.setText(childText);
        return convertView;
    }



    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView imgListHeader = (ImageView) convertView.findViewById(R.id.imgListHeader);
        switch (headerTitle){
            case "Tramvai":
                imgListHeader.setImageResource(R.drawable.tram);
                break;
            default:
                imgListHeader.setImageResource(R.drawable.bus);
                break;

        }
        int color;
        if (headerTitle.contains("Expres")) color = Color.argb(170,0,180,0); else
        if (headerTitle.contains("Trolebuz")) color = Color.argb(170,145,25,255); else
        if (headerTitle.contains("Tramvai")) color = Color.argb(170,30,155,255); else
            color = Color.argb(170,255,0,0);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(color);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
