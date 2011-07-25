//
// AquesTalk2 for Android sample program
//
// 2010/07/11 N.Yamazaki	Creation

package com.polysfactory.speechtest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import aquestalk2.AquesTalk2;

public class HelloAquesTalk2 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String koe = "わたしはいぬです。";
        int speed = 90; // 発話速度　50-300　default:100
        int phontResID = R.raw.aq_robo; // 声種
        try {
            // リソースからPhontデータを読み込み (デフォルトのPhontを使用するときは不要）
            InputStream in = getResources().openRawResource(phontResID);
            int size = in.available(); // リソースのデータサイズ
            byte[] phontDat = new byte[size];
            in.read(phontDat);

            // 音声合成
            AquesTalk2 aquestalk2 = new AquesTalk2();
            byte[] wav = aquestalk2.syntheWav(koe, speed, phontDat);
            // byte[] wav = aquestalk2.syntheWav(koe, speed, null); //< デフォルトのPhontを使用する場合
            // byte[] wav = AquesTalk2.synthe(koe, speed, phontDat); //< 静的メソッドもOK

            if (wav.length == 1) {// 生成エラー時には,長さ１で、先頭にエラーコードが返される
                Log.v("AQTKAPP", "AquesTalk2 Synthe ERROR:" + wav[0]);
            } else {
                PlayWav(wav);// 音声データを再生出力
            }
        } catch (IOException e) {
        }
        TextView textView = new TextView(this);
        textView.setText("音声記号列：" + koe + " speed:" + speed + " phont:" + getResources().getResourceEntryName(phontResID));
        setContentView(textView);
    }

    private void PlayWav(byte[] wav) {
        // 一旦ファイルに出力してから、MediaPlayerで再生
        String tmpfilename = "tmp_hello.wav";
        String packagename = removeSuffix(this.getClass().getName());

        try {
            FileOutputStream fileOutputStream = openFileOutput(tmpfilename, MODE_WORLD_READABLE);// MODE_PRIVATEはNG!
            fileOutputStream.write(wav);
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("AQTKAPP", "ERR: FileOutputStream");
        }
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource("/data/data/" + packagename + "/files/" + tmpfilename);
            mp.prepare(); // <<忘れると動かない　(-38,0)のようなエラーがでる
            mp.start();
        } catch (IOException e) {
        }
    }

    public static String removeSuffix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(0, point);
        }
        return fileName;
    }

}