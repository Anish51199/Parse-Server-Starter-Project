/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.TooManyListenersException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

  boolean SignUpMode=true;
  TextView LogIn;
  EditText UsernameText;
  EditText PasswordText;

  public void showUserList(){
      Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
      startActivity(intent);
  }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            SignUp(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
    Button SignUpButton=findViewById(R.id.SignUp);
     if(view.getId()==R.id.LogInView){

       if(SignUpMode){
         SignUpButton.setText("Log In");
         LogIn.setText("or Sign Up");
         SignUpMode=false;
       }
       else{
         SignUpButton.setText("SignUp");
         LogIn.setText("or LogIn");
         SignUpMode=true;
       }
     }
     else if(view.getId()==R.id.background||view.getId()==R.id.logoImage){
         InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
         inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
     }
  }

  public void SignUp(View view){


    if(UsernameText.getText().toString().matches("")|| PasswordText.getText().toString().matches("")){
      Toast.makeText(this, "A UserName and Password Required.", Toast.LENGTH_SHORT).show();
    }
    else {
      ParseUser user=new ParseUser();
      user.setUsername(UsernameText.getText().toString());
      user.setPassword(PasswordText.getText().toString());

      if(SignUpMode){
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e== null){
          Log.i("Msg->","DONE!!!");
          showUserList();  }
        else
          Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
         }
      });
    }else
        ParseUser.logInInBackground(UsernameText.getText().toString(), PasswordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null){
                    //User exist and authenticated, send user to Welcome.class
                    Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    showUserList();
                } else {
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


  } }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("InstaClone");
    setContentView(R.layout.activity_main);
    LogIn=findViewById(R.id.LogInView);
    LogIn.setOnClickListener(this);

    UsernameText=findViewById(R.id.UserName);
    PasswordText=findViewById(R.id.Password);
    PasswordText.setOnKeyListener(this);

    ImageView logo=findViewById(R.id.logoImage);
    RelativeLayout background=findViewById(R.id.background);
    logo.setOnClickListener(this);
    background.setOnClickListener(this);

    if(ParseUser.getCurrentUser()!=null){
        showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}