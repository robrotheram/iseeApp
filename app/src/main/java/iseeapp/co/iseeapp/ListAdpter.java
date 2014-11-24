package iseeapp.co.iseeapp;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.HashMap;

/**
 * Created by robertfletcher on 23/11/14.
 */
public class ListAdpter extends BaseAdapter
{
    public ArrayList<HashMap> list;
    LayoutInflater layoutInflater;

    public ListAdpter(LayoutInflater layoutInflater, ArrayList<HashMap> list) {
        super();
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        CircularImageView imgFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;


        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.fragment_row, null);
            holder = new ViewHolder();
            holder.imgFirst = (CircularImageView) convertView.findViewById(R.id.logo);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
            holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        map.get("FIRST_COLUMN");

        //Bitmap b =
        holder.imgFirst.setImageBitmap(((BitmapDrawable)parent.getResources().getDrawable(R.drawable.logo)).getBitmap());
        holder.txtSecond.setText(map.get("SECOND_COLUMN").toString());
        holder.txtThird.setText(map.get("THIRD_COLUMN").toString());

        return convertView;
    }

}