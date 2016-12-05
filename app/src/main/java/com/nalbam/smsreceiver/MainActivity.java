package com.nalbam.smsreceiver;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.needSMSWithCheck(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.RECEIVE_SMS)
    void needsSMSPermission() {
        Toast.makeText(this, "SMS 수신 권환 허용", Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.RECEIVE_SMS)
    void showRationaleForSMS(PermissionRequest request) {
        showRationaleDialog("SMS 수신 권한 요청", request);
    }

    @OnPermissionDenied(Manifest.permission.RECEIVE_SMS)
    void onSMSPermissionDenied() {
        Toast.makeText(this, "SMS 수신 거부", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.RECEIVE_SMS)
    void onSMSNeverAskAgain() {
        Toast.makeText(this, "SMS 수신 다시 묻지 않음", Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(String message, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("SMS 수신 허용 요청", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("SMS 수신 거부", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

}
