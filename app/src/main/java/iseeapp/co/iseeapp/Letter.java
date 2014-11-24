package iseeapp.co.iseeapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by robertfletcher on 22/11/14.
 */
public class Letter {
    private Context context;
    private View rootview;
    private TextView textView;
    private ViewGroup container;

    private boolean isEnabled = true;
    private String sletter;

    public Letter(Context c, ViewGroup container,String aLetter) {
        context = c;
        this.container = container;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootview = inflater.inflate(R.layout.letter_layout,container,false);
        textView = (TextView)rootview.findViewById(R.id.letterText);
        textView.setText(aLetter);
       // rootview.setBackgroundColor(Color.parseColor("#bdbdbd"));
        rootview.setLayoutParams(new ViewGroup.LayoutParams(30, 30));
        rootview.refreshDrawableState();
    }

    public boolean isEnabled(){
        return isEnabled;
    }
    public void enableView(){
        rootview.setBackgroundResource( R.drawable.innershadow );
        isEnabled = true;
    }
    public void diableView(){
        rootview.setBackgroundResource( R.drawable.darkinnershadow);
        isEnabled = false;
    }
    public View getView(){
        return rootview;
    }

    public void setLetter(String t) {
        sletter = t;
        textView.setText(t);
        rootview.refreshDrawableState();
    }

    public String getLetter() {
        return  sletter;
    }


}

