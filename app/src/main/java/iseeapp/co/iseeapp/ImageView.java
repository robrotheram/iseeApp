package iseeapp.co.iseeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by robertfletcher on 22/11/14.
 */
public class ImageView extends View{


        Paint paint = new Paint();
        Point point = new Point();
        boolean hasPress = false;
        Bitmap rawimage;

        private String m_Text = "";

        private Context context;
    private boolean enable = true;

    public void setPoint(Point point){
            this.point = point;
        }

    public String getName(){
        return m_Text;
    }


        public Point getPoint(){
            return point;
        }
        public ImageView(Context context) {
            super(context);
            this.context = context;
            paint.setColor(Color.RED);
            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);
        }


        public void setBitMap(Bitmap bit){
            rawimage = bit;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            //Drawable d = getResources().getDrawable(R.drawable.lon);

            Bitmap b;
            if(rawimage!=null){
                b  = rawimage;
                Rect src = new Rect(0,0,b.getWidth()-1, b.getHeight()-1);
                Rect dest = new Rect(0,0,getWidth()-1, getHeight()-1);

                canvas.drawBitmap(b, src, dest, null);
            }else {
                b = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
                int width = (this.getWidth()-b.getWidth())/2;
                int height = (this.getHeight()-b.getHeight())/2;
                canvas.drawBitmap(b, width, height, paint);

            }

            if(hasPress) {
                if(enable) {
                    canvas.drawCircle(point.x, point.y, 100, paint);
                }
            }
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(enable) {
                        point.x = event.getX();
                        point.y = event.getY();
                        hasPress = true;
                        showbox();
                    }
            }
            invalidate();
            return true;

        }

    private  void showbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("What Is this");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }
}
    class Point {
        float x, y;
    }

