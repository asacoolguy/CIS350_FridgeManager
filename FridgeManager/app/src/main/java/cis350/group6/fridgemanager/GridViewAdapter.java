package cis350.group6.fridgemanager;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private Activity activity;

    public GridViewAdapter(Activity activity,ArrayList<Recipe> recipes) {
        super();
        this.recipes = recipes;
        this.activity = activity;
    }

    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return recipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        // TODO Auto-generated method stub
        return recipes.get(position);
    }

    public String getRecipeName(int position) {
        return recipes.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgView;
        public TextView txtView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);

            view.txtView = (TextView) convertView.findViewById(R.id.textView1);
            view.imgView = (ImageView) convertView.findViewById(R.id.imageView1);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtView.setText(recipes.get(position).getName());
        view.imgView.setImageDrawable(recipes.get(position).getSmallImg());

        return convertView;
    }
}
