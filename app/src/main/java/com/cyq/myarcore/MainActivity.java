package com.cyq.myarcore;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

public class MainActivity extends AppCompatActivity {
private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        whetherSupportARcore();//第一步：检查是否支持arcore
    }

    Session session = null;
    int INSTALLED = 0;
    int INSTALL_REQUESTED = 1;
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (session == null){
                ArCoreApk.InstallStatus installStatus = ArCoreApk.getInstance().requestInstall(this,true);
                switch (installStatus){
                    case INSTALLED:
                        session = new Session(this);
                        Toast.makeText(context,"当前设备已安装arcore",Toast.LENGTH_SHORT).show();
                        break;
                    case INSTALL_REQUESTED:
                        //提示用户安装或更新 ARCore
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 检查是否支持arcore
     */
    public void whetherSupportARcore(){
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(context);
        if (availability.isTransient()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    whetherSupportARcore();
                }
            }, 200);
        }

        if (availability.isSupported()){
            Toast.makeText(context,"当前设备支持arcore功能",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"当前设备不支持arcore功能",Toast.LENGTH_SHORT).show();
        }
    }
}
