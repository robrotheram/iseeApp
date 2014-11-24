package iseeapp.co.iseeapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFagment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class guess extends Fragment {


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private Uri file;

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private VideoView videoPreview;
    private RelativeLayout rl_Main;
    private Button btnCapturePicture, btnGoBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private iseeapp.co.iseeapp.ImageView imgView;
    private String name;
    private Point point;
    private String[] word;
    private String newWord = "";

    public void setArguments(Uri file){
        this.file = file;
    }
    public static TakePhotoFragment newInstance(String param1, String param2) {
        TakePhotoFragment fragment = new TakePhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Letter> letterList = new ArrayList<Letter>();
    private ArrayList<Letter> ROWList = new ArrayList<Letter>();
    private ArrayList<Letter> ROWTWOList = new ArrayList<Letter>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup cont,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container = cont;
        rootView = inflater.inflate(R.layout.fragment_guess, container, false);
        //imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);

        rl_Main = (RelativeLayout) rootView.findViewById(R.id.relPreview);
        imgView = new iseeapp.co.iseeapp.ImageView(getActivity());
        imgView.disable();


        imgView.setPoint(point);
        rl_Main.addView(imgView);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;
        if(file!=null) {
            imgView.setBitMap(BitmapFactory.decodeFile(file.getPath(), options));
        }
        btnGoBack = (Button) rootView.findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameActivity)getActivity()).gotToMenu();

            }
        });
        buildWord(container);
         return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(Point point) {
        this.point = point;
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





    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview

            //imgPreview.setVisibility(View.VISIBLE);
            //imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            imgView.setBitMap(BitmapFactory.decodeFile(fileUri.getPath(),options));

            //imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void buildWord(final ViewGroup container){

        letterList.clear();
        ROWList.clear();
        ROWTWOList.clear();
        word = new String[12];
        String alpherbet = "abcdefghijklmnopqrstuvwxyz";
        int i = 0;
        if(name!=null) {
            for (i = 0; i < name.length(); i++) {
                if (i == 0) {
                    letterList.add(getNewLetter("" + name.charAt(i)));
                    newWord+="" + name.charAt(i);
                } else {
                    letterList.add(getNewLetter("_"));
                }
                word[i] = "" + name.charAt(i);
            }
            Random r = new Random();

            for (int x = i; x < word.length; x++) {
                word[x] = "" + alpherbet.charAt(r.nextInt(alpherbet.length()));
            }

            for (int x = 0; x < 7; x++) {

                ROWList.add(getNewLetter(""));
                ROWTWOList.add(getNewLetter(""));
            }
            Random rnd = new Random();
            for (i = word.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                String a = word[index];
                word[index] = word[i];
                word[i] = a;
            }
            rebuild(container);
        }



    }

    private int x = 1;
    public void onchangeletter(String s){
        newWord+=s;
        if(x<letterList.size()) {
            letterList.get(0).enableView();
            letterList.get(x).enableView();
            letterList.get(x).setLetter(s);
            x++;
        }
        if(x>=letterList.size()){
            Log.d("test",name+"     |     "+newWord);
            if(newWord.equals(name)){
                Toast.makeText(getActivity(),"You Have Won!!!!",Toast.LENGTH_LONG).show();
                x = 1;
                newWord = "";
                ((GameActivity)getActivity()).gotToMenu();
            }else{
                Toast.makeText(getActivity(),"Looser",Toast.LENGTH_LONG).show();
                x = 1;
                newWord = "";
                ((GameActivity)getActivity()).gotToMenu();
            }

        }

        refresh();
    }

    public void refresh(){
        final LinearLayout findHeight = (LinearLayout)rootView.findViewById(R.id.guessLayout);
        final int height;
        findHeight.post(new Runnable() {
            @Override
            public void run() {
                Log.d("ddd","IT WORKS  RKS  RKS  RKS ");
                int height = findHeight.getHeight();
                LinearLayout guessLayout = (LinearLayout) rootView.findViewById(R.id.guessLayout);
                guessLayout.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height - 22, height - 22);
                params.setMargins(5, 5, 5, 5);
                for (int i = 0; i < letterList.size(); i++) {
                    guessLayout.addView(letterList.get(i).getView(),params);
                }
                guessLayout.refreshDrawableState();
            }
        });
    }
    private Letter getNewLetter(String s){
        final Letter l = new Letter(getActivity(), container,s);
        l.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l.isEnabled()) {
                    l.diableView();
                    onchangeletter(l.getLetter());
                }

            }
        });
        return l;

    }

    public void rebuild(final ViewGroup container){
        final LinearLayout findHeight = (LinearLayout)rootView.findViewById(R.id.guessLayout);
        final int height;
        findHeight.post(new Runnable() {
            @Override
            public void run() {
                int height = findHeight.getHeight();
                Toast.makeText(getActivity(), "height =" + height, Toast.LENGTH_LONG).show();

                LinearLayout guessLayout = (LinearLayout) rootView.findViewById(R.id.guessLayout);
                LinearLayout keys = (LinearLayout) rootView.findViewById(R.id.guessRow1);
                LinearLayout keys2 = (LinearLayout) rootView.findViewById(R.id.guessRow2);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height - 22, height - 22);
                params.setMargins(5, 5, 5, 5);

                guessLayout.removeAllViews();
                keys.removeAllViews();
                keys2.removeAllViews();

                for (int i = 0; i < letterList.size(); i++) {

                    guessLayout.addView(letterList.get(i).getView(),params);
                }
                for (int i = 0; i < ROWList.size(); i++) {
                    ROWList.get(i).setLetter(word[i]);
                    keys.addView(ROWList.get(i).getView(),params);
                }
                for (int i = 0; i < ROWTWOList.size(); i++) {
                    ROWTWOList.get(i).setLetter(word[i+5]);
                    keys2.addView(ROWTWOList.get(i).getView(),params);
                }



            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getActivity().getApplicationContext(),"It works",Toast.LENGTH_LONG).show();
        previewCapturedImage();
    }




}
