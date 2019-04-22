package com.example.autoemergencyapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class ServicesActivity extends AppCompatActivity implements View.OnClickListener {


    private LocationManager locationManager;
    private TelephonyManager Tell;

    private static final int REQUEST_CALL=1;
    private static final int REQUEST_LOCATION = 2;
    private static final int REQUEST_SEND_SMS=3;
    private static final int REQUEST_PHONE_NUM=4;

    private RadioButton illnessEmergency, AccidenttEmergency, FireEmergency;
    private Button call, track;
    private static final String PhoneNumber = "0788097599";
    String  link, Msg = "My Location is : ";
    String lattitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        call = findViewById(R.id.callbtn);
        track=findViewById(R.id.trackbtn);
        illnessEmergency = findViewById(R.id.illness);
        AccidenttEmergency = findViewById(R.id.accident);
        FireEmergency = findViewById(R.id.fire);
        Tell = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        call.setOnClickListener(this);
        track.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.trackbtn:
                Toast.makeText(this, "Track", Toast.LENGTH_LONG).show();
                break;
            case R.id.callbtn:
                makeCall();
                break;
        }
    }

    private void makeCall() {
        if(ContextCompat.checkSelfPermission(ServicesActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
          ActivityCompat.requestPermissions(ServicesActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }
        else
        {
          String dial="tel:"+PhoneNumber;
          startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
        GetLocation();
        sendMsg();
    }

    private void GetLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getCurrentLocation();
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getCurrentLocation();
        }
    }

    private void sendMsg() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ServicesActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},REQUEST_SEND_SMS);
        }
        else {
            try {
                SmsManager smsmanage = SmsManager.getDefault();
                Msg += link + "\n";
                getType();
                smsmanage.sendTextMessage( PhoneNumber, null, Msg, null, null );
                Toast.makeText(this, "SMS SENT!", Toast.LENGTH_LONG ).show();
            } catch (Exception c) {
                Toast.makeText(this, "SMS NOT SENT!", Toast.LENGTH_LONG ).show();
            }
        }
    }

    private void buildAlertMessageNoGps() {
       final AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage("Please Turn ON your GPS Connection").setCancelable(false)
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id)
                   {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                   }
              }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
              });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
           && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
           ActivityCompat.requestPermissions(ServicesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
           Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

           if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("")
                    .appendQueryParameter("api", "1").appendQueryParameter("destination", lat + "," + lng);
            String url = builder.build().toString();
            link = "Directions" + url;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
          }
          else  if (location1 != null) {
            double latti = location1.getLatitude();
            double longi = location1.getLongitude();
            lattitude = String.valueOf(latti);
            longitude = String.valueOf(longi);
            Msg += "Caller current location is: " + lattitude + ", " + longitude;
          }
          else if (location2 != null) {
            double latti = location2.getLatitude();
            double longi = location2.getLongitude();
            lattitude = String.valueOf(latti);
            longitude = String.valueOf(longi);
            Msg += "Caller current location is: " + lattitude + ", " + longitude;
          }
          else
           Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
     }
    }

    private void makephoneCall(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_NUMBERS)!= PackageManager.PERMISSION_GRANTED)
        {
          ActivityCompat.requestPermissions(ServicesActivity.this, new String[]{Manifest.permission.READ_PHONE_NUMBERS},REQUEST_CALL);
        }
        else {
          makeCall();
        }
    }

    private void getPhoneNumber() {
      if(ContextCompat.checkSelfPermission(ServicesActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
      {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ServicesActivity.this, Manifest.permission.READ_PHONE_STATE))
            ActivityCompat.requestPermissions(ServicesActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        else
            ActivityCompat.requestPermissions(ServicesActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
      }
      else
      {
        //do nothing
      }
    }

    private void getType() {
      if (illnessEmergency.isChecked())
      {
        Msg += "I Need Illness Emergency";
      }
      else if(FireEmergency.isChecked())
      {
        Msg += "I Need Fire Emergency";
      }
      else if(AccidenttEmergency.isChecked())
      {
        Msg += "I Need Accident Emergency";
      }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
    if(requestCode==REQUEST_CALL)
    {
      if(grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
      {
        makeCall();
      }else
        Toast.makeText(this,"Permission Denited :(",Toast.LENGTH_SHORT).show();
    }
    if(requestCode==REQUEST_LOCATION){
      if(grantResults.length >0 && grantResults[1] ==PackageManager.PERMISSION_GRANTED)
      {
        GetLocation();
      }else
        Toast.makeText(this,"Permission Denited :(",Toast.LENGTH_SHORT).show();

        }
        if(requestCode==REQUEST_SEND_SMS){
            if(grantResults.length >0 && grantResults[2] ==PackageManager.PERMISSION_GRANTED)
            {
                sendMsg();
            }else
                Toast.makeText(this,"Permission Denited :(",Toast.LENGTH_SHORT).show();

        }
        if(requestCode==REQUEST_PHONE_NUM){
            if(grantResults.length >0 && grantResults[3] ==PackageManager.PERMISSION_GRANTED)
            {
                getPhoneNumber();
            }else
                Toast.makeText(this,"Permission Denited :(",Toast.LENGTH_SHORT).show();

        }
    }
}
