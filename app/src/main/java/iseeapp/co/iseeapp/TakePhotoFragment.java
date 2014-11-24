package iseeapp.co.iseeapp;

        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.text.InputFilter;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.Toast;
        import android.widget.VideoView;

        import com.facebook.Session;
        import com.facebook.SessionState;

        import java.io.File;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFagment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakePhotoFragment extends Fragment {


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

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
    private Button btnSend ;
    private Session session;


    public static TakePhotoFragment newInstance(String param1, String param2) {
        TakePhotoFragment fragment = new TakePhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TakePhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        //imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        btnCapturePicture = (Button) rootView.findViewById(R.id.btnCapturePicture);
        btnGoBack = (Button) rootView.findViewById(R.id.btnGoBack);



        rl_Main = (RelativeLayout) rootView.findViewById(R.id.relPreview);
        imgView = new iseeapp.co.iseeapp.ImageView(getActivity());
        rl_Main.addView(imgView);


        /**
         * Capture image button click event
         * */

        btnGoBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                ((GameActivity)getActivity()).gotToMenu();
            }
        });

        btnSend = (Button) rootView.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                if(imgView.getName()!=null){
                    if(imgView.getName().length()>2){
                        GameActivity gm = ((GameActivity)getActivity());

                        gm.setpoint(imgView.getPoint());
                        gm.setName(imgView.getName());
                        gm.gotoGuessPhoto();
                    }else{
                        Toast.makeText(getActivity(),"Please Enter a word or length greater then 3", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Please Enter a word or length greater then 3", Toast.LENGTH_LONG).show();
                }

            }
        });


        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

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

        Log.d("test",mediaFile.getAbsolutePath());
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Toast.makeText(getActivity().getApplicationContext(),"It works",Toast.LENGTH_LONG).show();
        ((GameActivity)getActivity()).setFile(fileUri);
        previewCapturedImage();
    }






}
