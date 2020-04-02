package com.sapphire.HealthAssessmentPDMA.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSBroadcastReceiverAPI extends BroadcastReceiver {
    private OTPReceiveListener otpListener;
    /*public static Boolean isMainActivity= false;
    public static Boolean isMyProfile= false;
    public static Boolean isLoginAuthentication= false;*/
    /**
     * @param otpListener
     */
    public void setOTPListener(OTPReceiveListener otpListener) {
        this.otpListener = otpListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"hi", Toast.LENGTH_SHORT).show();
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.

                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/


                    //}
                    //Extract the OTP code and send to the listener
                    if (otpListener != null) {
                        otpListener.onOTPReceived(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:


                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    if (otpListener != null) {
                        otpListener.onOTPTimeOut();
                    }
                    break;
                case CommonStatusCodes.API_NOT_CONNECTED:

                    if (otpListener != null) {
                        //otpListener.onOTPReceivedError("API NOT CONNECTED");
                    }

                    break;

                case CommonStatusCodes.NETWORK_ERROR:

                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("NETWORK ERROR");
                    }

                    break;

                case CommonStatusCodes.ERROR:

                    if (otpListener != null) {
                        //otpListener.onOTPReceivedError("SOME THING WENT WRONG");
                    }

                    break;
            }
        }
    }
    public interface OTPReceiveListener {

        void onOTPReceived(String otp);

        void onOTPTimeOut();

        void onOTPReceivedError(String error);
    }
}
