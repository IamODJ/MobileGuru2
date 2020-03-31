package mobileguru.ganeshkhind.kvgk.mobileguru;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.*;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    String item;
    String common;
    String parsename;
    String part;
    String roll;
    long uid;
    long  rollfinal=0;
    EditText editname;
    EditText edituid;
    Spinner spin;

    Fragment frag_login, frag_dashboard;
    ProgressBar pbar;
    View button_login, button_label,button_icon,ic_menu1,ic_menu2;
    private DisplayMetrics dm;
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        pbar=(ProgressBar) findViewById(R.id.mainProgressBar1);
        button_icon=findViewById(R.id.button_icon);
        button_label=findViewById(R.id.button_label);

        dm=getResources().getDisplayMetrics();
        button_login=findViewById(R.id.button_login);
        button_login.setTag(0);
        pbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#683CB7"), PorterDuff.Mode.MULTIPLY);
        StatusBarUtil.immersive(this);
        final ValueAnimator va=new ValueAnimator();
        va.setDuration(1500);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator p1) {
                LinearLayout.LayoutParams button_login_lp = (LinearLayout.LayoutParams) button_login.getLayoutParams();
                button_login_lp.width = Math.round((Float) p1.getAnimatedValue());
                button_login.setLayoutParams(button_login_lp);
            }
        });
        button_login.animate().translationX(dm.widthPixels+button_login.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        button_login.animate().translationX(0).setStartDelay(0).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                if ((int) button_login.getTag() == 1) {
                    return;
                } else if ((int) button_login.getTag() == 2) {
                    button_login.animate().x(dm.widthPixels / 2).y(dm.heightPixels / 2).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).setListener(null).setDuration(1000).setStartDelay(0).start();
                    button_login.animate().setStartDelay(600).setDuration(1000).scaleX(40).scaleY(40).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN_OUT)).start();
                    button_icon.animate().alpha(0).rotation(90).setStartDelay(0).setDuration(800).start();
                    return;
                }
                button_login.setTag(1);
                va.setFloatValues(button_login.getMeasuredWidth(), button_login.getMeasuredHeight());
                va.start();
                pbar.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
                button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
                button_login.animate().setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(4000).setDuration(1000).scaleX(30).scaleY(30).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator p1) {
                        pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
                    }

                    @Override
                    public void onAnimationEnd(Animator p1) {
                        parsename = editname.getText().toString();
                        roll = edituid.getText().toString();

                        if (logincheck()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            if(parsename.contains(" ")) {
                                String[] parts = parsename.split(" ");
                                parsename = parts[0]; // 004
                            }
                            sp.edit().putString("username", parsename).apply();
                            sp.edit().putString("UID", roll).apply();
                            sp.edit().putString("class", String.valueOf(spin.getSelectedItem())).apply();

                            Intent intent = new Intent(MainActivity.this, PocketSphinxActivity.class);
                            sp.edit().putBoolean("logged", true).apply();
                            startActivity(intent);


                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            //   intent.putExtra("data",String.valueOf(spinner.getSelectedItem()));
                            ////    intent.putExtra("name",parsename);
                            //   intent.putExtra("id",rollfinal);
                            //sp.edit().putBoolean("logged",true).apply();
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator p1) {
                        // TODO: Implement this method
                    }

                    @Override
                    public void onAnimationRepeat(Animator p1) {
                        // TODO: Implement this method
                    }
                }).start();


            }
        });



         editname=findViewById(R.id.editName);
        edituid=findViewById(R.id.editEmail);

        // Spinner element
       spin = (Spinner) findViewById(R.id.spinner);
       Button button=(Button)findViewById(R.id.buttonRegister);

        sp = getSharedPreferences("formobguru",0);
        // Spinner click listener
      spin.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Class");
        categories.add("11th");
        categories.add("12th");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin.setAdapter(dataAdapter);

      if(sp.getBoolean("logged",false)){
            Intent x= new Intent(MainActivity.this,PocketSphinxActivity.class);
            startActivity(x);
        }

    /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsename=name.getText().toString();
                roll=uid.getText().toString();
                rollfinal=Long.parseLong(roll);


                if(logincheck()){
                    Intent intent= new Intent(MainActivity.this,PocketSphinxActivity.class);
                //    intent.putExtra("data",String.valueOf(spinner.getSelectedItem()));
                ////    intent.putExtra("name",parsename);
                 //   intent.putExtra("id",rollfinal);
                    sp.edit().putBoolean("logged",true).apply();
                    startActivity(intent);

                }


            }
        });
*/

    }

    @Override
    public void onBackPressed() {

    }
    public boolean logincheck(){
        boolean omkar;

try{

    common=roll.substring(0,6);
    part=roll.substring(6,15);
    uid= Long.parseLong(part);

}
  catch (NumberFormatException | IndexOutOfBoundsException e){
      part="0";
      uid=-1;
      common="0";



  }
        if(!(parsename.equals("")) && roll.length()<=15 && common.equals("051229") && (uid>=100000000 && uid<=999999999) && !(item.equals("Select Class"))){
            omkar=true;
        }
        else{
            if (parsename.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter your name", Toast.LENGTH_LONG).show();


            }

            if(item.equals("Select Class")){
                Toast.makeText(getApplicationContext(),"Please enter Class and try again.", Toast.LENGTH_LONG).show();

            }
            if(!(roll.length()<=16 && common.equals("051229")&& !(uid>=100000000 && uid<=999999999))) //todo updated
            {

                Toast.makeText(getApplicationContext(),"Please Enter valid UID.", Toast.LENGTH_LONG).show();


            }
            omkar=false;
        }
            return omkar;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item

    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }



}


