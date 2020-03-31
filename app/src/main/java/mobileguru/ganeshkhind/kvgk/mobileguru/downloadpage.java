package mobileguru.ganeshkhind.kvgk.mobileguru;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class downloadpage extends AppCompatActivity {
    ArrayAdapter<String> adapter;
Display display;
    int[] chidall = {R.array.chemistry, R.array.btech};
    int width;
    ArrayList<String> arraylist;
    private DownloadManager downloadManager;
    DialogPlus dialog;
    int[] idchem={R.array.chem0}; //todo array of arrays :)
    int[] idbiotech={R.array.bt0,R.array.bt1}; // add all ids of the string arrays of concepts of chs todo edit
String[] chfiles;
    int height;
    int x;
    AlertDialog progressDialog;
    View v;
    int chcode;

    ArrayList<String> mlist;
    TextToSpeech t1;
    ImageButton img;
     long refid;
    ArrayList<Long> list = new ArrayList<>();
int state=-1;
    String[] subj={"Chemistry","Biotechnology"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        progressDialog = new SpotsDialog(downloadpage.this,R.style.Down);
v=findViewById(R.id.toolbar);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        int permissionCheck3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            return;
        }
        int permissionCheck4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        arraylist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.chemistry)));

        adapter = new ArrayAdapter<String>(this,
                R.layout.down, arraylist);
         display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
         width = size. x;
         height = size. y;
        FloatingActionButton.LayoutParams mains = new FloatingActionButton.LayoutParams(width/3, width/3);
        FloatingActionButton.LayoutParams ins = new FloatingActionButton.LayoutParams(width/4, width/4);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.plus);
        icon.setLayoutParams(mains);

        img=(ImageButton)findViewById(R.id.omkars);
      img.setLayoutParams(new LinearLayout.LayoutParams(width / 2, width / 2));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state==-1){

                    t1.speak("Please choose a subject first", TextToSpeech.QUEUE_FLUSH, null);
                    TSnackbar snackbar = TSnackbar.make(v, "Please choose a subject", TSnackbar.LENGTH_LONG); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                else{
                     dialog = DialogPlus.newDialog(downloadpage.this)
                            .setAdapter(adapter)
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    if(state==0 && position<idchem.length)   // todo change for update #2
                                    checkdown(position);

                                    if (state==1 && position<idbiotech.length)
                                        checkdown(position);

                                //    TSnackbar snackbar = TSnackbar.make(v, "Biotechnology Selected, Download the files now.", TSnackbar.LENGTH_LONG); //todo put username
                                  //  snackbar.show();
                                  dialog.dismiss();
                                }
                            })
                            .setOnBackPressListener(new OnBackPressListener() {
                                @Override
                                public void onBackPressed(DialogPlus dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel(DialogPlus dialog) {
                                    dialog.dismiss();

                                }
                            })

                            .setFooter(R.layout.footer)

                            .setHeader(R.layout.header)
                            .setExpanded(false)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                      TextView title=(TextView) findViewById(R.id.had);
                      title.setText("Download chapter files for "+subj[state]);
                    Button down=(Button)findViewById(R.id.footer_close_button);
                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });


                }
            }
        });
        final FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setLayoutParams(mains)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setLayoutParams(ins);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.bios);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.flask);


        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)

                .setStartAngle(-15)
                .setEndAngle(-165)
                .setRadius(width / 3)
                .attachTo(actionButton)
                .build();
        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                actionButton.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(actionButton, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                actionButton.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(actionButton, pvhR);
                animation.start();
            }
        });
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       state=1;
       mlist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.btech))); // todo change to biotech
      adapter.clear();
      //  adapter.addAll(arraylist);
        adapter.addAll(mlist);
        adapter.notifyDataSetChanged();

        t1.speak("Biotechnology selected. Proceed to download the chapter files", TextToSpeech.QUEUE_FLUSH, null);
        TSnackbar snackbar = TSnackbar.make(v, "Biotechnology Selected, Download the files now.", TSnackbar.LENGTH_LONG); //todo put username
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
});

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                state = 0;
                mlist= new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.chemistry)));
                arraylist.clear();
                arraylist.addAll(mlist);
                adapter.notifyDataSetChanged();


                t1.speak("Chemistry selected. Proceed to download the chapter files", TextToSpeech.QUEUE_FLUSH, null);
                TSnackbar snackbar = TSnackbar.make(v, "Chemistry Selected, Download the files now.", TSnackbar.LENGTH_LONG); //todo put username
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
        });




    }

    public void checkdown(int index){
String[] child;
        File root = Environment.getExternalStorageDirectory();
        int check=0;
        String sub;
        if (state==0){
            sub="chem";
            chfiles=getResources().getStringArray(idchem[index]);

        }
        else{
            sub="bt";
            chfiles=getResources().getStringArray(idbiotech[index]);

        }
        String[] urls=new String[100];
            for(int i=0;i<chfiles.length;i++) {                 /// todo get size of array
                File dir = new File(root + "/MobileGuru/" + sub + "/" + index + "/" + i + ".mp3");

                    check = check + 1;
                    if(state==1 && index==0 && i==7){
                        urls[i] = "https://tinyurl.com/mobgkvgkpune-" +sub + "-" + index + "-" + 99;

                    }
                    else{
                        urls[i] = "https://tinyurl.com/mobgkvgkpune-" + sub + "-" + index + "-" + i;
                    }
                    Log.e("this", urls[i]);



            }
    /*    File nx = new File(root + "/MobileGuru/"+"/"+sub+"/"+index);  //todo 1st update 3 delete if other files there

        if (nx.isDirectory()) //i suspect
        {
             child = nx.list();

        }*/
                //if(!(check==chfiles.length)){           // change

                    if(haveNetworkConnection()){
                        progressDialog.show();
                        int count=0;
                    for(int k = 0; k<chfiles.length; k++)
                    {
                        File dir = new File(root + "/MobileGuru/" + sub + "/" + index + "/" + k + ".mp3");
                        if (!(dir.exists())) {
                            Uri Download_Uri;
                            count=count+1;
                            Log.e("ch", String.valueOf(k));
                            Download_Uri = Uri.parse(urls[k]);
                            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setAllowedOverRoaming(false);
                            request.setTitle("Downloading files for " + getResources().getStringArray(chidall[state])[chcode]);
                            request.setVisibleInDownloadsUi(true);
                            request.setDestinationInExternalPublicDir("/MobileGuru/" + sub + "/" + index + "/", k + ".mp3");

                            refid = downloadManager.enqueue(request);
                            list.add(refid);

                        }



                    }
                        if(count==0){
                            t1.speak("Chapter files already exist in the Storage", TextToSpeech.QUEUE_FLUSH, null);

                            TSnackbar snackbar = TSnackbar.make(v, "Chapter files already exist in the memory", TSnackbar.LENGTH_INDEFINITE); //todo put username
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                            progressDialog.dismiss();


                        }
                    }
                    else{
                        TSnackbar snackbar = TSnackbar.make(v, "Please check your Internet connection", TSnackbar.LENGTH_INDEFINITE); //todo put username
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                        t1.speak("Internet connection not avaiable. Please try later", TextToSpeech.QUEUE_FLUSH, null);

                    }
              //  }
              /*  else{
                    t1.speak("Chapter files already exist in the Storage", TextToSpeech.QUEUE_FLUSH, null);

                    TSnackbar snackbar = TSnackbar.make(v, "Chapter files already exist in the memory", TSnackbar.LENGTH_INDEFINITE); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();                }

*/


    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()||ni.isConnectedOrConnecting())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()||ni.isConnectedOrConnecting())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            // get the refid from the download manager
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

// remove it from our list
            list.remove(referenceId);

// if list is empty means all downloads completed
            if (list.isEmpty())
            {
progressDialog.dismiss();
                t1.speak("All files for "+getResources().getStringArray(chidall[state])[chcode]+" have been downloaded",TextToSpeech.QUEUE_FLUSH, null);



            }

        }
    };



}



