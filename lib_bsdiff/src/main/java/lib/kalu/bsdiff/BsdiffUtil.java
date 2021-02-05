package lib.kalu.bsdiff;

import android.text.TextUtils;

import androidx.annotation.Keep;

import java.io.File;

/**
 * description: 差分合并生成工具
 * created by kalu on 2021-02-05
 */
@Keep
public final class BsdiffUtil {

    static {
        System.loadLibrary("bsdiff");
    }

    /**
     * 生成差分包
     *
     * @param oldFile   旧文件
     * @param newFile   生成新文件
     * @param patchFile 差分包
     * @return
     */
    @Keep
    public static int diff(String oldFile, String newFile, String patchFile) {

        try {

            if (TextUtils.isEmpty(oldFile) || TextUtils.isEmpty(newFile) || TextUtils.isEmpty(patchFile))
                return -1;

            File old_file = new File(oldFile);
            if (null == old_file || !old_file.exists())
                return -1;

            File new_file = new File(newFile);
            if (null == new_file || !new_file.exists())
                return -1;

            File patch_file = new File(patchFile);
            if (null != patch_file && patch_file.exists()) {
                patch_file.delete();
            }
            patch_file.createNewFile();

            return jnidiff(oldFile, newFile, patchFile);

        } catch (Exception e) {

            return -1;
        }
    }

    /**
     * 合并差分包
     *
     * @param oldFile   旧文件
     * @param patchFile 差分包
     * @param newFile   生成新文件
     * @return
     */
    @Keep
    public static int patch(String oldFile, String patchFile, String newFile) {

        try {

            if (TextUtils.isEmpty(oldFile) || TextUtils.isEmpty(patchFile) || TextUtils.isEmpty(newFile))
                return -1;

            File old_file = new File(oldFile);
            if (null == old_file || !old_file.exists())
                return -1;

            File patch_file = new File(patchFile);
            if (null == patch_file || !patch_file.exists())
                return -1;

            File new_file = new File(newFile);
            if (null != new_file && new_file.exists()) {
                new_file.delete();
            }
            new_file.createNewFile();

            return jnipatch(oldFile, patchFile, newFile);

        } catch (Exception e) {

            return -1;
        }
    }

    @Keep
    private static native int jnipatch(String oldFile, String patchFile, String newFile);

    @Keep
    private static native int jnidiff(String oldFile, String newFile, String patchFile);
}
