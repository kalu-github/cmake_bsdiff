package com.kalu.bsdiff;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import lib.kalu.bsdiff.BsdiffUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String commonPath = getFilesDir().getPath() + File.separator;

        // 1. 拷贝公用基础包
        FileUtil.copyFile(FileUtil.openAssetInputStream(this, "old/old.apk"), commonPath + "oldF.apk");
        // 2. 拷贝做差分 diff 用的资源
        FileUtil.copyFile(FileUtil.openAssetInputStream(this, "new/new.apk"), commonPath + "newF.apk");
        // 3. 拷贝做差分包合并 merge 的资源
        FileUtil.copyFile(FileUtil.openAssetInputStream(this, "patch/patch"), commonPath + "patchF");

        // 生成差分包
        findViewById(R.id.make_diff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int diff = BsdiffUtil.diff(commonPath + "oldF.apk", commonPath + "newF.apk", commonPath + "patchN");
                        Log.e("kalu", "diff => " + diff);

                        String md51 = FileUtil.getFileMD5ToString(commonPath + "oldF.apk");
                        Log.e("kalu", "getFileMD5ToString[oldF.apk] => md51" + md51);

                        String md52 = FileUtil.getFileMD5ToString(commonPath + "newF.apk");
                        Log.e("kalu", "getFileMD5ToString[newF.apk] => md52" + md52);

                        String md53 = FileUtil.getFileMD5ToString(commonPath + "patchN");
                        Log.e("kalu", "getFileMD5ToString[patchN] => md53" + md53);

                        Looper.prepare();
                        Toast.makeText(MainActivity.this, diff == 0 ? "生成差分包成功" : "生成差分包失败" + diff, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });

        // 合并差分包
        findViewById(R.id.merge_diff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int diff = BsdiffUtil.patch(commonPath + "oldF.apk", commonPath + "patchF", commonPath + "newN.apk");
                        Log.e("kalu", "getFileMD5ToString[oldF.apk] => diff" + diff);

                        String md51 = FileUtil.getFileMD5ToString(commonPath + "oldF.apk");
                        Log.e("kalu", "getFileMD5ToString[oldF.apk] => md51" + md51);

                        String md52 = FileUtil.getFileMD5ToString(commonPath + "newN.apk");
                        Log.e("kalu", "getFileMD5ToString[newN.apk] => md52" + md52);

                        String md53 = FileUtil.getFileMD5ToString(commonPath + "patchF");
                        Log.e("kalu", "getFileMD5ToString[patchF] => md53" + md53);

                        Looper.prepare();
                        Toast.makeText(MainActivity.this, diff == 0 ? "合并差分包成功" : "合并差分包失败" + diff, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
}
