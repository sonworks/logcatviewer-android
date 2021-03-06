package jp.co.brilliantservice.android.LogcatViewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LogcatViewerService extends Service {
    
    private static final int LOGCAT_SAVE_MAXLINES = 60000;//30000
    private static final int LOGCAT_DISP_MAXLINES = 1000;

    private IBinder mBinder = new LogcatViewerServiceBinder();
    private List<String> mList = new ArrayList<String>();
    
    private Thread mThread = null;
    private Object mObjThreadBreak = new Object();
    private boolean mThreadBreak = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        if( mThread == null ) {
            mThread = new Thread(new Runnable() {
                public void run()
                {
                    Process proc = null;
                    BufferedReader reader = null;
                    synchronized (mObjThreadBreak) {
                        mThreadBreak = false;
                    }
                    try {
                        proc = Runtime.getRuntime().exec(new String[] { "logcat", "-v", "time", "*:V"});
                        reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1024);
                        String line;
                        while ( true ) {
                           synchronized (mObjThreadBreak) {
                               if( mThreadBreak ) {
                                   break;
                               }
                           }
    
                           line = reader.readLine();
                           if (line.length() == 0) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                }
                                continue;
                           }
                            
                           synchronized (mList) {
                               if( mList.size() >= LOGCAT_SAVE_MAXLINES ) {
                                   mList.remove(0);
                               }
                               mList.add(line);
                           }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mThread = null;
                    }
                }
    
            });
            
            mThread.start();
        }
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private class LogcatViewerServiceBinder extends ILogcatViewerService.Stub {

        public List<String> getDispData() {

            synchronized (mList) {
                int listsize = mList.size(); 
                if( listsize < LOGCAT_DISP_MAXLINES ) {
                    return new ArrayList<String>(mList);
                } else {
                    int start = listsize - LOGCAT_DISP_MAXLINES;
                    return new ArrayList<String>(mList.subList(start, listsize));
                }
            }
        }
        
        public int saveLog( String FileName ){

            int ret = -1;
            try{
                String line;
                int iCnt;
                BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "/sdcard/" + FileName ), "UTF-8" ) );
                synchronized (mList) {
                    for( iCnt = 0; iCnt < mList.size(); iCnt++ ) {
                        line = mList.get(iCnt);
                        bw.write(line);
                        bw.newLine();
                    }
                }
                bw.flush();
                bw.close();
                ret = iCnt;
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return ret;
        }

        public void setLogBreak(){
            synchronized (mObjThreadBreak) {
                mThreadBreak = true;
            }
        }
    
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}

