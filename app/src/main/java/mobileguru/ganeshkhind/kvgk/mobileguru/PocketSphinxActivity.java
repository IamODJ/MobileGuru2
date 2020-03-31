package mobileguru.ganeshkhind.kvgk.mobileguru;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.skyfishjy.library.RippleBackground;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class PocketSphinxActivity extends AppCompatActivity implements
        RecognitionListener,NavigationView.OnNavigationItemSelectedListener {
    View ch;
    ArrayList<String> mlist;

    AlertDialog pro;
    int chcode = -1;
    View vies; //todo change this to drawerlayout
    String[] subj = {"Chemistry", "Biotechnology"};
    String[] subject = {"chem", "bt"};

ImageView imageView;
    String anych;
    Display display;
    boolean chchoosen = false;

    int width;
    int height;
    String listening;
    TSnackbar snackbar;
    TSnackbar snack;

    int querycode = -1;
    int onchselect = -1;
    String[] chemch = {"ch1chem", "ch2chem"};
    String[] biotechch = {"ch1biotech", "ch2biotech"};
    int[] chemall = {};
    int[] btall = {R.array.bt0};

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private static final String FORECAST_SEARCH = "forecast";
    private static final String DIGITS_SEARCH = "digits";
    private static final String PHONE_SEARCH = "phones";
    private static final String MENU_SEARCH = "menu";
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView sub;
    TextView chname;
    Toolbar toolbar = null;
    int substate = 0;
    SharedPreferences sp;

    public int index;

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "oh mighty computer";

    /* Used to handle permission request */

    MediaPlayer q = new MediaPlayer();
    TextToSpeech t1;
    /* Keyword we are looking for to activate menu */
    public String text;
    /* Used to handle permission request */
    ArrayList<String> arraylist;
    AlertDialog progressDialog;
    AlertDialog progressDialogNew;

    ArrayList<String> conlist;
    ImageButton omkar;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    //private HashMap<String, Integer> captions;
    int chap = -1;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> con;
    ArrayAdapter<String> forch;
    int[] idchem = {R.array.chem0}; //todo array of arrays :)
    int[] chidall = {R.array.chemistry, R.array.btech}; //todo array of arrays :)
    //int[] chidbt={R.array.chemistry,R.array.chemistry}; //todo array of arrays :)
    View snackbarView;
    int[] idbiotech = {R.array.bt0, R.array.bt1,R.array.bt2,R.array.bt3,R.array.bt4,R.array.bt5}; // add all ids of the string arrays of concepts of chs todo edit
    String[] chchem;
    String[] chbt;
TextView textView;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.finalhome);
        vies = findViewById(R.id.content); // can switch to len
        sp = getSharedPreferences("formobguru", 0);
        if (sp.getBoolean("once", false)) {
            substate = sp.getInt("subject", 0);
        }
chbt=getResources().getStringArray(R.array.btech);
chchem=getResources().getStringArray(R.array.chemistry);
        if (substate==0) {

            arraylist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.chemistry)));
            snackbar =TSnackbar.make(vies, "Chemistry-Class XII. Choose a chapter to proceed.",TSnackbar.LENGTH_INDEFINITE);

            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();

        }
        else{
                arraylist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.btech)));
            snackbar =TSnackbar.make(vies, "Biotechnology-Class XII. Choose a chapter to proceed.",TSnackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();


        }
        conlist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.pom)));

        adapter = new ArrayAdapter<String>(this,
                R.layout.acitivity_listview, arraylist);
        con = new ArrayAdapter<String>(this,
                R.layout.acitivity_listview, conlist);
        progressDialog = new SpotsDialog(PocketSphinxActivity.this,R.style.Custom);
        progressDialogNew = new SpotsDialog(PocketSphinxActivity.this,R.style.play);
      /*  UpdateMe.with(this, 30).setDialogVisibility(true)
                .continueButtonVisibility(true)
                .setPositiveButtonText("Update Now(Imp)")
                .setNegativeButtonText("Later")
                .setDialogIcon(R.drawable.common_google_signin_btn_icon_dark)
                .onNegativeButtonClick(new OnNegativeButtonClickListener() {

                    @Override
                    public void onClick(LovelyStandardDialog dialog) {

                        Log.d(UpdateMe.TAG, "Later Button Clicked");
                        dialog.dismiss();
                    }
                })
                .onPositiveButtonClick(new OnPositiveButtonClickListener() {

                    @Override
                    public void onClick(LovelyStandardDialog dialog) {
                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                        }
                        Log.d(UpdateMe.TAG, "Update Button Clicked");
                        dialog.dismiss();
                    }
                })
                .check(); */
        //sub=findViewById(R.id.textView);


        //  chname=findViewById(R.id.textView6);
        //  chname.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ch = getWindow().getDecorView().findViewById(R.id.toolbar);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        FloatingActionButton.LayoutParams mains = new FloatingActionButton.LayoutParams(width / 4, width / 4);
        FloatingActionButton.LayoutParams ins = new FloatingActionButton.LayoutParams(width / 5, width / 5);
        //  LinearLayout.LayoutParams ripp = new LinearLayout.LayoutParams(width/2, width/2);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.plus);
        icon.setLayoutParams(mains);


        final FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setLayoutParams(mains)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setLayoutParams(ins);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.chp);
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.men);
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.concp);
        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.drawable.ic_action_name);
        SubActionButton chchoose = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton conchoose = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton menuchoose = itemBuilder.setContentView(itemIcon3).build();
        SubActionButton downch = itemBuilder.setContentView(itemIcon4).build();
        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(chchoose)
                .addSubActionView(conchoose)
                .addSubActionView(menuchoose)
                .addSubActionView(downch)

                .setStartAngle(-1)
                .setEndAngle(-179)
                .setRadius(width / 4)
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

        q.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                omkar.setImageResource(R.mipmap.micfinal);
                snackbar = TSnackbar.make(vies, "Try Something else :)", TSnackbar.LENGTH_INDEFINITE); //todo put username
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
        });
        q.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                omkar.setImageResource(R.mipmap.micfinal);
                snackbar =TSnackbar.make(vies, "Try Something else :)" ,TSnackbar.LENGTH_INDEFINITE); //todo put username
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
        });
        q.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                snackbar =TSnackbar.make(vies, "Error While Syncing. Please Check your Internet Connection Speed." ,TSnackbar.LENGTH_INDEFINITE); //todo put username
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                return false;
            }
        });
        menuchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        downch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PocketSphinxActivity.this, downloadpage.class);
                startActivity(i);
            }
        });
        chchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (substate != -1) {

                    final DialogPlus dialog = DialogPlus.newDialog(PocketSphinxActivity.this)
                            .setAdapter(adapter)
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    chchoosen = true;

                                    chap = position;
                                    anych = item.toString();
                                    String s=adapter.getItem(position);
                                    int pos=adapter.getPosition(s);
                                    if (pos<=idchem.length-1 ){
                                    alg(pos, substate);
                                    }
                                    else{
                                        ArrayList<String> k= new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.pom)));

                                        conlist.clear();
                                        conlist.addAll(k);  //todo 1st update
                                        con.notifyDataSetChanged();

                                        snackbar = TSnackbar.make(vies, "We are working on the audio files, will publish the 1st update soon", TSnackbar.LENGTH_LONG); //todo put username
                                        View snackbarView = snackbar.getView();
                                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                                        textView.setTextColor(Color.WHITE);
                                        snackbar.show();
                                    }

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
                            .setExpanded(true)
                            .setCancelable(false)
                            .create();
                    dialog.show();

                    TextView title = (TextView) findViewById(R.id.had);
                    title.setText("Select a chapter from " + subj[substate]);

                    Button down = (Button) findViewById(R.id.footer_close_button);
                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
                    inputSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                            PocketSphinxActivity.this.adapter.getFilter().filter(cs);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                } else {
                     snackbar =TSnackbar.make(vies, "Please choose a subject first",TSnackbar.LENGTH_LONG); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                }
            }

        });

        conchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chchoosen) {
                    final DialogPlus dialog = DialogPlus.newDialog(PocketSphinxActivity.this)
                            .setAdapter(con)
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    String s=con.getItem(position);
                                    int pos=con.getPosition(s);
                                    querycode = pos;
                                    dialog.dismiss();
                                    Omkarsalgorithm2(position); //concepts in ch

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
                            .setExpanded(true)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    TextView title = (TextView) findViewById(R.id.had);
                    title.setText("Concepts in " + anych);
                    Button down = (Button) findViewById(R.id.footer_close_button);
                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
                    inputSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                            PocketSphinxActivity.this.con.getFilter().filter(cs);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                    //      TextView title=(TextView) findViewById(R.id.had);
                    //      title.setText("his ch is new");
                } else {
                     snackbar =TSnackbar.make(vies, "Please choose a chapter first",TSnackbar.LENGTH_LONG); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }

        });




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        // rippleBackground.setLayoutParams(ripp);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.user);
        TextView nav_cls = (TextView) hView.findViewById(R.id.cls);

        String s = sp.getString("username", "Unknown user");
        int len = s.length();
        if (len > 12) {
            nav_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
        nav_user.setText("Hello, " + sp.getString("username", "Unknown user"));
        nav_cls.setText("Class-" + sp.getString("class", "Unknown user"));

        // Prepare the data for UI

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        int permissionCheck5 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute(); //todo why 2 times
         omkar = (ImageButton) findViewById(R.id.push_button);

        omkar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (q.isPlaying()) {
                    q.stop();
                    omkar.setImageResource(R.mipmap.micfinal);
                     snackbar =TSnackbar.make(vies, "Audio Player stopped",TSnackbar.LENGTH_LONG); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }


                else {
                    if (!chchoosen) {

                         snackbar =TSnackbar.make(vies, "Please select a chapter first",TSnackbar.LENGTH_LONG); //todo put username
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }

                    else{

                    switch (motionEvent.getAction()) {

                        case MotionEvent.ACTION_DOWN:


                                snackbar = TSnackbar.make(vies, "Which Concept,"+sp.getString("username","default"), TSnackbar.LENGTH_LONG); //todo put username
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();

                            FireRecognition();
                            rippleBackground.startRippleAnimation();

                            return true;
                        case MotionEvent.ACTION_UP:
                            // ((TextView) findViewById(R.id.caption_text))
                            //        .setText("This is a test");
                            rippleBackground.stopRippleAnimation();

                            recognizer.stop();

                            return true;

                    }
                }
                }
                return false;

            }


        });


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        View snackbarView;
        TextView textView;

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            case R.id.chem:
                sp.edit().putBoolean("once", true).apply();
                substate = 0;
                prepareListAudio(substate);
                chchoosen=false;
                sp.edit().putInt("subject", 0);
                snackbar =TSnackbar.make(vies, "Chemistry-Class XII. Choose a chapter to proceed.",TSnackbar.LENGTH_INDEFINITE);
                 snackbarView = snackbar.getView();
                 textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                // chname.setVisibility(View.INVISIBLE);
                //  sub.setText("Chemistry");
                return true;
            case R.id.biotech:
                sp.edit().putBoolean("once", true).apply();
                substate = 1;
                prepareListAudio(substate);
                chchoosen=false;

                sp.edit().putInt("subject", 1);
                snackbar =TSnackbar.make(vies, "Biotechnology-Class XII. Choose a chapter to proceed.",TSnackbar.LENGTH_INDEFINITE);
                 snackbarView = snackbar.getView();
                 textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                //    chname.setVisibility(View.INVISIBLE);

                // sub.setText("Biotechnology");


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_home:
                Intent h = new Intent(PocketSphinxActivity.this, PocketSphinxActivity.class);
                startActivity(h);
                break;
            case R.id.nav_download:
                Intent i = new Intent(PocketSphinxActivity.this, downloadpage.class);
                startActivity(i);
                break;
            case R.id.nav_howto:
                Intent j = new Intent(PocketSphinxActivity.this, howtouse.class);
                startActivity(j);
                break;
            case R.id.nav_about_us:
                Intent k = new Intent(PocketSphinxActivity.this, abtus.class);
                startActivity(k);
                break;
            case R.id.nav_privacy_policy:
                Intent l = new Intent(PocketSphinxActivity.this, privacypol.class);
                startActivity(l);
                break;
            case R.id.cpp:
                Intent p = new Intent(PocketSphinxActivity.this, cpsir.class);
                startActivity(p);
                break;
            case R.id.rateus:
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                break;
            case R.id.logout:
                sp.edit().putBoolean("logged", false).apply();
                Intent m = new Intent(PocketSphinxActivity.this, MainActivity.class);
                startActivity(m);
                break;


        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<PocketSphinxActivity> activityReference;
        SetupTask(PocketSphinxActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {

                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                // ((TextView) activityReference.get().findViewById(R.id.caption_text))
                //      .setText("Failed to init recognizer " + result);
            } else {


               activityReference.get().progressDialog.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {


            //  activityReference.get().progressDialog.build();
            //  activityReference.get().progressDialog.

            File root = Environment.getExternalStorageDirectory();
           File dir = new File(root + "/MobileGuru");
            if (!dir.exists()) {
                dir.mkdirs();}
            activityReference.get().progressDialog.setCancelable(false);
            activityReference.get().progressDialog.setCanceledOnTouchOutside(false);

           activityReference.get().progressDialog.show();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
            q.release();
            q=null;
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;


    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.e("recog",text);

            Omkarsalgorithm1(text, listening);
        }
        else{
            t1.speak("", TextToSpeech.QUEUE_FLUSH, null);


            snackbar =TSnackbar.make(vies, "I didn't understand that. Please try again or choose from concept list.",TSnackbar.LENGTH_LONG); //todo put username
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();

        }
    }

    public void FireRecognition(){
        Log.d("Recognition", "Recognition Started");
        // recognizer_state.setText("Recognition Started!");
        //conteo = 0;

          if(!(substate==0 && chap>0)){               //todo update 1st time
            recognizer.startListening(subject[substate]+chap);
           listening=recognizer.getSearchName();

          }



    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {

    }


    private void setupRecognizer(File assetsDir) throws IOException {

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "1984.dic"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

       // File chemgram = new File(assetsDir, "chem"+0+".gram");
      //  recognizer.addGrammarSearch("chem"+0, chemgram);

    /*   for(int i=0;i<chemch.length;i++){

            File menuGrammar = new File(assetsDir, "chem"+i+".gram");
            recognizer.addGrammarSearch("chem"+i, menuGrammar);

        } */

        for(int i=0;i<idbiotech.length;i++){

            File menuGrammar = new File(assetsDir, "bt"+i+".gram");
            recognizer.addGrammarSearch("bt"+i, menuGrammar);

        }
        for(int i=0;i<idchem.length;i++){

            File menuGrammar = new File(assetsDir, "chem"+i+".gram");
            recognizer.addGrammarSearch("chem"+i, menuGrammar);

        }


    }

    @Override
    public void onError(Exception error) {
        // ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {

    }

    public void Omkarsalgorithm1(String omk,String what) {
        boolean count = false;
        String[] chcontent;
        if(substate==0){

            chcontent=getResources().getStringArray(idchem[chap]);
        }
        else{

            chcontent=getResources().getStringArray(idbiotech[chap]);

        }
            boolean me=false;
String byparts="x";

        for (int i = 0; i < chcontent.length; i++) {
            if(chcontent[i].contains("/")){
                String s= String.valueOf(i);
                String[] parts = chcontent[i].split("/");
                byparts = parts[0].toUpperCase().trim(); // 004
                if (byparts.equals(String.valueOf(omk))) {
                    count = true;
                    index = i;
                    break;
                }

            }
            if (chcontent[i].equalsIgnoreCase(String.valueOf(omk))) {
                count = true;
                index = i;
                break;
            }


               /**/

            }
            if (count) {
                File root = Environment.getExternalStorageDirectory();
                String val=Environment.getExternalStorageDirectory()+"/MobileGuru/"+subject[substate]+"/" + chap + "/" + index + ".mp3";

                File dir = new File(root + "/MobileGuru/"+subject[substate]+"/" + chap + "/" + index + ".mp3");
                       Log.e("thedir",dir.toString());
                if (dir.exists()) {
                    try {
                        q.reset();
                        q.setDataSource(val);
                        q.setLooping(false);
                        q.prepare();
                        q.start();
                        omkar.setImageResource(R.mipmap.pau);
                        snackbar =TSnackbar.make(vies, "Playing:- "+omk ,TSnackbar.LENGTH_INDEFINITE); //todo put username
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (haveNetworkConnection()) {

                        try {
                            q.reset();
                            if (substate==1 && chap>=1){
                                t1.speak(omk, TextToSpeech.QUEUE_FLUSH, null);
                                snackbar = TSnackbar.make(vies, "We are working on the audio files, will publish the 1st update soon", TSnackbar.LENGTH_LONG); //todo put username
                                View snackbarView = snackbar.getView();
                                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();                                                              //todo edit for 1st update #1
                            }
                            else {
                                if (substate == 1 && chap == 0 && index == 7) {
                                    q.setDataSource("https://tinyurl.com/mobgkvgkpune-" + subject[substate] + "-" + chap + "-" + 99);
                                } else {
                                    q.setDataSource("https://tinyurl.com/mobgkvgkpune-" + subject[substate] + "-" + chap + "-" + index);
                                }
                                q.setLooping(false);

                                q.prepare();

                                snackbar = TSnackbar.make(vies, "Playing:- " + omk, TSnackbar.LENGTH_INDEFINITE); //todo put username
                                View snackbarView = snackbar.getView();
                                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                q.start();
                                omkar.setImageResource(R.mipmap.pau);

                          /*  new Timer().schedule(
                                    new TimerTask() {

                                        @Override
                                        public void run() {
                                            if(!(q.isPlaying())){
                                                q.stop();
                                                q.reset();
                                                snackbar =TSnackbar.make(vies, "Internet connection too slow. Download the chapterfiles later for best results.",TSnackbar.LENGTH_LONG); //todo put username
                                                snackbar.show();
                                                snackbar =TSnackbar.make(vies, "Stopping Audio player",TSnackbar.LENGTH_SHORT); //todo put username
                                                snackbar.show();
                                            }

                                        }

                                    }, 10000); *///todo check avg time

                            } } catch (IOException e) {
                            snackbar =TSnackbar.make(vies, "Failed to initialise player. :( Try again later",TSnackbar.LENGTH_INDEFINITE); //todo put username
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                            e.printStackTrace();
                        }



                    } else {

                        t1.speak("Internet connection not available. Download the files for best results", TextToSpeech.QUEUE_FLUSH, null);
                        snackbar =TSnackbar.make(vies, "Network error. Download the files later",TSnackbar.LENGTH_INDEFINITE); //todo put username
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();

                    }
                }
            } else {


            }


        }


    public void Omkarsalgorithm2(int om)
    {
        String chcontent[];
        if(substate==0){

            chcontent=getResources().getStringArray(idchem[chap]);
        }
        else{

            chcontent=getResources().getStringArray(idbiotech[chap]);

        }
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root + "/MobileGuru/"+subject[substate]+"/" + chap + "/" + om + ".mp3");
            String val=Environment.getExternalStorageDirectory()+"/MobileGuru/"+subject[substate]+"/" + chap + "/" + om + ".mp3";
            if (dir.exists()) {
                try {
                    q.reset();
                    q.setDataSource(val);
                    q.setLooping(false);
                    q.prepare();
                    q.start();
                    omkar.setImageResource(R.mipmap.pau);

                    snackbar =TSnackbar.make(vies, "Playing- "+chcontent[om].toUpperCase(),TSnackbar.LENGTH_INDEFINITE); //todo put username
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (haveNetworkConnection()) {
                    try {
                        // add progress bar or tts voice
                        if (substate==1 && chap>=1){
                            t1.speak(chcontent[om].toUpperCase(), TextToSpeech.QUEUE_FLUSH, null);
                            snackbar = TSnackbar.make(vies, "We are working on the audio files, will publish the 1st update soon", TSnackbar.LENGTH_LONG); //todo put username
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();                                                              //todo edit for 1st update #1
                        }
                        else {
                            q.reset();
                            if (substate == 1 && chap == 0 && om == 7) {
                                q.setDataSource("https://tinyurl.com/mobgkvgkpune-" + subject[substate] + "-" + chap + "-" + 99);
                            } else {
                                q.setDataSource("https://tinyurl.com/mobgkvgkpune-" + subject[substate] + "-" + chap + "-" + om);
                            }
                            q.setLooping(false);
                            q.prepare();
                            q.start();
                            omkar.setImageResource(R.mipmap.pau);

                            snackbar = TSnackbar.make(vies, "Playing- " + chcontent[om].toUpperCase(), TSnackbar.LENGTH_INDEFINITE); //todo put username
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                    /*    new Timer().schedule(
                                new TimerTask() {

                                    @Override
                                    public void run() {
                                        if (!(q.isPlaying())) {
                                            q.stop();
                                            q.reset();
                                            snackbar =TSnackbar.make(vies, "Internet connection too slow. Download the chapterfiles later for best results.",TSnackbar.LENGTH_LONG); //todo put username
                                            snackbar.show();
                                            snackbar =TSnackbar.make(vies, "Stopping Audio player",TSnackbar.LENGTH_SHORT); //todo put username
                                            snackbar.show();
                                        }

                                    }

                                }, 10000); */ //todo check avg time

                        }} catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    t1.speak("Internet connection not available. Download the files for best results", TextToSpeech.QUEUE_FLUSH, null);


                }
            }


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

    public void prepareListAudio(int me) {

        ArrayList<String> mlist;

// add all ids of the string arrays of concepts of chs todo edit
        mlist= new ArrayList<String>(Arrays.asList(getResources().getStringArray(chidall[me])));
        arraylist.clear();
        arraylist.addAll(mlist);
        adapter.notifyDataSetChanged();




    }

  public void alg(int code,int subject){
      ArrayList<String> mlist;

// add all ids of the string arrays of concepts of chs todo edit
      if(subject==0){
      mlist= new ArrayList<String>(Arrays.asList(getResources().getStringArray(idchem[code])));
           snackbar = TSnackbar.make(vies, "Chapter Selected- " + chchem[code], TSnackbar.LENGTH_INDEFINITE);
          View snackbarView = snackbar.getView();
          TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
          textView.setTextColor(Color.WHITE);
          snackbar.show();
      }
      else{

          mlist= new ArrayList<String>(Arrays.asList(getResources().getStringArray(idbiotech[code])));
           snackbar =TSnackbar.make(vies, "Chapter Selected- "+chbt[code],TSnackbar.LENGTH_INDEFINITE); //todo put username
          View snackbarView = snackbar.getView();
          TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
          textView.setTextColor(Color.WHITE);
          snackbar.show();
      }
      conlist.clear();
      conlist.addAll(mlist);
      con.notifyDataSetChanged();


   //   chname.setText(chchem[code]);
    //  chname.setVisibility(View.VISIBLE);



    }

}


