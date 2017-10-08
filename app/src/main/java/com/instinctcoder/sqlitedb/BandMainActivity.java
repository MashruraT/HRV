package com.instinctcoder.sqlitedb;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.tiles.BandTile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;


public class BandMainActivity extends Activity {


    private BandClient client = null;
    private Button  btnConsent;
    private Button  btnStart;
    private Button btnLog;
    private Button btnEdit;
    private TextView txtStatus;
    private static final UUID tileId = UUID.fromString("aa0D508F-70A3-47D4-BBA3-812BADB1F8Ab");

    /* private BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
         @Override
         public void onBandHeartRateChanged(final BandHeartRateEvent event) {
             if (event != null) {
                 appendToUI(String.format("Heart Rate = %d beats per minute\n"
                         + "Quality = %s\n", event.getHeartRate(), event.getQuality()));
             }
         }
     };*/
    public int i = 0;
    public double HRV = 0;
    public double RRinterval[] = new double[2];
    public double sum_Dev = 0;
    public double reading_count = 0;
    public double depression_score = 0;
    public double cumulative_count = 0;
    public String depressionState;
    public String emergency_phn;
    public String self_name;

    StudentRepo repo = new StudentRepo(this);
    Student student = new Student();

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //dateFormatter.setLenient(false);


    private BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null) {
                RRinterval[i % 2] = event.getInterval() * 1000;
                Log.v("BandApp1", "Value " + (i % 2) + ": " + RRinterval[i % 2]);
                if (i > 0) {
                    sum_Dev += Math.pow((RRinterval[1] - RRinterval[0]), 2);
                    Log.v("BandApp1", "Interval " + (i) + ": " + (RRinterval[1] - RRinterval[0]) + "\n\n");
                }
                i++;
//                if (i == 100) {
                if (i == 50) {
                    HRV = Math.log10(Math.sqrt(sum_Dev / 49));
                    Log.v("BandApp1", "Depression score: " + depression_score + "  Happiness score: " + (reading_count - depression_score));
                    if (HRV < 1.78) {
                        depression_score++;
                        cumulative_count++;
                        if (cumulative_count >= 10) {
                            appendToUI("Long depressive episode");
                            Log.v("BandApp1", "!!!    Long depressive episode   !!!");
                        }
                    } else {
                        cumulative_count = 0;
                    }
                    sum_Dev = 0;
                    RRinterval[0] = 0;
                    RRinterval[1] = 0;
                    i = 0;
                    reading_count++;
                    Log.v("Reading count: ","Printing reading count: "+reading_count);
//                    if (reading_count == 50) {
                    if (reading_count == 20) {
                        Date today = new Date();
                        student.time = dateFormatter.format(today);
                        student.score = depression_score;
                        student.subjName = repo.getFirstStudentSelfName();

                        if (depression_score >= 4) {
                            student.decision = 1;
                            depressionState = "Depressed ?";
                            String SMS = "Depression Detection Application has detected some signs of depression in " + self_name +". You are requested to take necessary steps";

                            sendSMS(emergency_phn, SMS);

                            appendToUI("Depressed");
                            Log.v("BandApp1", "!!!    Depressed   !!!");
                            Log.v("Depression", "Time: " + student.time+" Score: "+ student.score+ " Decision: " + student.decision+ "\n");
                        } else {
                            depressionState = "Good Mood !!";
                            student.decision = 0;
                            appendToUI("Good Mood :)");
                            Log.v("BandApp1", "!!!    Good Mood   !!!");
                            Log.v("Depression", "Time: " + student.time+" Score: "+ student.score+ " Decision: " + student.decision+ "\n");
                        }
                        Log.v("Depression", "Time: " + student.time+" Score: "+ student.score+ " Decision: " + student.decision+ "\n");
                        repo.saveLog(student);

                        reading_count = 0;
                        depression_score = 0;

                    }


                }
                appendToUI(String.format("HRV(log RMSSD) = %.3f ms after %.0f counts\nDepression score = %.0f, Happiness score = %.0f", HRV, reading_count,depression_score,reading_count-depression_score));
            }
        }
    };

    private class RRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    if (doesTileExist()) {
                        sendMessage("Stay Connected");
                    } else {
                        if (addTile()) {
                            sendMessage("Welcome to happiness !!");
                        }
                    }
                    int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
                    if (hardwareVersion >= 20) {
                        if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                            client.getSensorManager().registerRRIntervalEventListener(mRRIntervalEventListener);
                        } else {
                            appendToUI("You have not given this application consent to access heart rate data yet."
                                    + " Please press the Heart Rate Consent button.\n");
                        }
                    } else {
                        appendToUI("The RR Interval sensor is not supported with your Band version. Microsoft Band 2 is required.\n");
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            }catch (Exception e)
            {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {

                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                            }
                        });
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        txtStatus = (TextView) findViewById(R.id.textView);
        emergency_phn =  getIntent().getExtras().getString("Emergency_phone");
        self_name =  getIntent().getExtras().getString("Self_name");
 //       btnStart = (Button) findViewById(R.id.btnStart);
 //       btnStart.setOnClickListener(new OnClickListener() {
 //           @Override
 //           public void onClick(View v) {
 //               txtStatus.setText("");
 //              new RRIntervalSubscriptionTask().execute();
 //           }

   //         });
        btnStart = (Button) findViewById(R.id.btnStart);
//        btnStart.setOnClickListener((OnClickListener) this);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                new RRIntervalSubscriptionTask().execute();
            }
        });


        btnConsent = (Button) findViewById(R.id.btnConsent);
        final WeakReference<Activity> reference = new WeakReference<Activity>(this);
        btnConsent.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
             public void onClick(View v) {
                new HeartRateConsentTask().execute(reference);
            }
        });

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
                objIndent.putExtra("student_Id", 1);
                startActivity(objIndent);
            }
        });
        btnLog = (Button) findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),viewLog.class);
                startActivity(intent);
            }
        });
//        btnLog.setOnClickListener((OnClickListener) this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (client != null) {
            try {
                client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
            } catch (BandIOException e) {
                appendToUI(e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("HRV(log RMSSD) = "+ HRV+" ms");
    }

    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();
    }

    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(string);
            }
        });
    }

    private boolean doesTileExist() throws BandIOException, InterruptedException, BandException {
        List<BandTile> tiles = client.getTileManager().getTiles().await();
        for (BandTile tile : tiles) {
            if (tile.getTileId().equals(tileId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addTile() throws Exception {
        /* Set the options */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.icon_large, options);
        Bitmap badgeIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.icon_small,
                options);

        BandTile tile = new BandTile.Builder(tileId, "MessageTile", tileIcon)
                .setTileSmallIcon(badgeIcon).build();
        appendToUI("Message Tile is adding ...\n");
        if (client.getTileManager().addTile(this, tile).await()) {
            appendToUI("Message Tile is added.\n");
            return true;
        } else {
            appendToUI("Unable to add message tile to the band.\n");
            return false;
        }
    }


    private void sendMessage(String message) throws BandIOException {
        client.getNotificationManager().sendMessage(tileId, "Tile Message", message, new Date(), MessageFlags.SHOW_DIALOG);
        appendToUI(message + "\n");
    }

    private void handleBandException(BandException e) {
        String exceptionMessage = "";
        switch (e.getErrorType()) {
            case DEVICE_ERROR:
                exceptionMessage = "Please make sure bluetooth is on and the band is in range.\n";
                break;
            case UNSUPPORTED_SDK_VERSION_ERROR:
                exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                break;
            case SERVICE_ERROR:
                exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                break;
            case BAND_FULL_ERROR:
                exceptionMessage = "Band is full. Please use Microsoft Health to remove a tile.\n";
                break;
            default:
                exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                break;
        }
        appendToUI(exceptionMessage);

    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
    public void sendSMS(String phoneNumber, String text){
        //  String phoneNo = textPhoneNo.getText().toString();
        //String sms = textSMS.getText().toString();
        Log.v("Message Sending","Message to be sent");
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
           // Toast.makeText(getApplicationContext(), "SMS Sent!",
             //       Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Log.v("Message Sending","Message sent!");


    }
}

