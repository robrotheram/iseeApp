package iseeapp.co.iseeapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.pkmmte.view.CircularImageView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFagment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFagment extends Fragment implements NetworkInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String Username = "startup";
    private String Password = "sws2014";
    private View rootview;
    private EditText userText;
    private EditText passText;
    private Button regBtn;
    private android.widget.ImageView logo;


    public static LoginFagment newInstance(String param1, String param2) {
        LoginFagment fragment = new LoginFagment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        return fragment;
    }

    public LoginFagment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = inflater.inflate(R.layout.fragment_login_fagment, container, false);

        LoginButton fb =  (LoginButton)rootview.findViewById(R.id.connectWithFbButton);
        userText = (EditText)rootview.findViewById(R.id.etUserName);
        passText = (EditText)rootview.findViewById(R.id.etPass);



        regBtn = (Button)rootview.findViewById(R.id.btnReg);

        final Context con = getActivity();





        final Button login = (Button) rootview.findViewById(R.id.btnSingIn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameActivity)getActivity()).gotoRegister();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login();
                sendMessage();
            }
        });
        return rootview;
    }

    private void login(){

        String testUser = userText.getText().toString();
        if(((userText.getText().toString()).equals(Username))&&((passText.getText().toString()).equals(Password))){
            ((GameActivity)getActivity()).login();
            ((GameActivity)getActivity()).gotToMenu();
        }else{
            Toast.makeText(getActivity(),"Inncorrect Login",Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void sendMessage() {
        //Intent i = new Intent(rootView.getContext(), Game.class);
        // startActivity(i);

        HttpClient client = new DefaultHttpClient();


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("request_type", "LOGIN"));;
        nameValuePairs.add(new BasicNameValuePair("USERNAME", userText.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("PASSWORD", passText.getText().toString()));

        BackgroundTask bt = new BackgroundTask(this,nameValuePairs,client);
        bt.execute("ddd");
    }

    @Override
    public void onPostComplete(JSONObject s) {

        try {
            String status = s.getString("status");
            if(status.equals("success")){
                ((GameActivity)getActivity()).login();
                ((GameActivity)getActivity()).gotToMenu();


            }else{
                Toast.makeText(getActivity(),"Invalid Logon",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("test", s.toString());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



}
