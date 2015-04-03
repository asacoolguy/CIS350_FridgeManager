package cis350.group6.fridgemanager;

import java.util.ArrayList;
        import java.util.List;
        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<shippingitem> stringlist = null;
    private ArrayList<shippingitem> arraylist;

    public ListViewAdapter(Context context,
                           List<shippingitem> stringlist) {
        this.context = context;
        this.stringlist = stringlist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<shippingitem>();
        this.arraylist.addAll(stringlist);
    }

    public class ViewHolder {
        TextView name;
        TextView quantity;
    }

    @Override
    public int getCount() {
        return stringlist.size();
    }

    @Override
    public Object getItem(int position) {
        return stringlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(stringlist.get(position).getname());
        holder.quantity.setText(Integer.toString(stringlist.get(position).getquantity()));
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ShoppingListActivity.increment((stringlist.get(position).getname()));
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                ShoppingListActivity.remove((stringlist.get(position).getname()));
                return true;
            }
        });
        return view;
    }

}