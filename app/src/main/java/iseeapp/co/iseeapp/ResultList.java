package iseeapp.co.iseeapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;




public class ResultList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListAdpter adapter;
    private LayoutInflater inflater;
    private ListView lview;
    private View rootView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultList.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultList newInstance(String param1, String param2) {
        ResultList fragment = new ResultList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen

        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_result_list, container, false);
        lview = (ListView) rootView.findViewById(R.id.listview);
        populateList();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private void populateList() {

        ArrayList<HashMap> list = new ArrayList<HashMap>();

        for (int i = 0; i < 10; i++) {

            HashMap temp = new HashMap();

            temp.put("FIRST_COLUMN", ""+i);
            temp.put("SECOND_COLUMN", "Bob");
            temp.put("THIRD_COLUMN", 2000-(i*5));
            list.add(temp);
        }
        adapter = new ListAdpter(inflater, list);
        lview.setAdapter(adapter);
        lview.refreshDrawableState();

    }
}
