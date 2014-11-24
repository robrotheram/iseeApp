package iseeapp.co.iseeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class GameActivity extends ActionBarActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 0;
    private TakePhotoFragment photoFragment;
    private LoginFagment loginFagment;
    private guess guessFagment;
    private ResultList listFragment;
    private MenuFragment menuFragmaent;
    private Register registerFragment;

    private FragmentTransaction transaction;
    private int fragID = 0;
    private Context con;
    private boolean isUserLogedIn = false;
    private Uri file;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        printkey();

        photoFragment = new TakePhotoFragment();
        loginFagment = new LoginFagment();
        registerFragment = new Register();
        guessFagment = new guess();

        listFragment = new ResultList();
        menuFragmaent = new MenuFragment();

        con = this.getApplicationContext();

        checklogin();
    }




    public void setFile(Uri f){
        file = f;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if((fragID==1)||(fragID==1)){
                return false;
            }else {
                gotToMenu();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        AppEventsLogger.activateApp(this);
        //Toast.makeText(this,"reusumed: ====>   "+fragID,Toast.LENGTH_SHORT).show();
        // Get the Camera in
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public void gotologin(){
        fragID = 1;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) loginFagment)
                .commit();


    }

    public void gotoTakePhoto(){

        fragID = 2;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) photoFragment)
                .commit();


    }

    public void gotToMenu(){

        fragID = 0;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) menuFragmaent)
                .commit();


    }

    public void gotoGuessPhoto(){

        fragID = 3;
        guessFagment.setArguments(file);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) guessFagment)
                .commit();


    }


    public void gotoResultsPhoto(){
        fragID = 4;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) listFragment)
                .commit();


    }

    public void gotoRegister() {
        fragID = 5;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (android.support.v4.app.Fragment) registerFragment)
                .commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        session = Session.getActiveSession();
        Log.d("test",""+requestCode);
        if(requestCode==64206){
        if(session !=null) {
            session.onActivityResult(this, requestCode, resultCode,
                    data);
            if (session.isOpened()) {
                loginResult(true);
            } else {
                loginResult(false);
            }
        }
        }else {

            photoFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void checklogin(){
        Session session = Session.getActiveSession();

        if(session==null){
            // try to restore from cache
            session = Session.openActiveSessionFromCache(this);
        }

        if(session!=null && session.isOpened()){
            gotToMenu();
        }
        else{
            gotologin();
        }
    }

    public void login() {
        isUserLogedIn = true;
    }

    public void setpoint(Point point) {
           guessFagment.setPoint(point);
    }

    public void setName(String name) {
        guessFagment.setName(name);
    }

    private void printkey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("iseeapp.co.iseeapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    // Facebook Login Result
    public void loginResult(boolean result) {
        // TODO Auto-generated method stub
        if (result) {
            Log.e("Facebook Login ", "Successful");
            gotToMenu();
        } else {
            Log.e("Facebook Logout", "Un Successful");
        }
    }


    public void fblogin() {
        session = Session.getActiveSession();

        if (session == null
                || session.getState() == SessionState.CLOSED_LOGIN_FAILED
                || session.getState() == SessionState.CLOSED) {
            session = new Session(this);
        }
        if (!session.isOpened()) {
            session.openForRead(new Session.OpenRequest(this));
        }
        Session.setActiveSession(session);

    }
}
