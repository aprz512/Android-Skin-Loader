package cn.feng.skin.manager.loader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.listener.ISkinLoader;
import cn.feng.skin.manager.listener.ISkinUpdate;

/**
 * Skin Manager Instance
 * <p/>
 * <p/>
 * <ul>
 * <strong>global init skin manager, MUST BE CALLED FIRST ! </strong>
 * </ul>
 * <ul>
 * <strong>get single runtime instance</strong>
 * <li> {@link #getInstance()} </li>
 * </ul>
 * <ul>
 * <strong>attach a listener (Activity or fragment) to SkinManager</strong>
 * </ul>
 * <ul>
 * <strong>detach a listener (Activity or fragment) to SkinManager</strong>
 * <li> {@link #detach(ISkinUpdate observer)} </li>
 * </ul>
 * <ul>
 * <strong>load latest theme </strong>
 * <li> {@link #load()} </li>
 * <li> {@link #load(ILoaderListener callback)} </li>
 * </ul>
 * <ul>
 * <strong>load new theme with the giving skinPackagePath</strong>
 * <li> {@link #load(String skinPackagePath, ILoaderListener callback)} </li>
 * </ul>
 *
 * @author fengjun
 */
public class SkinManager implements ISkinLoader {

    private static final String NOT_INIT_ERROR = "SkinManager MUST init with Context first";
    private static Object synchronizedLock = new Object();
    private static SkinManager instance;

    private List<ISkinUpdate> skinObservers;
    private Context context;
    private String skinPackageName;
    private Resources mResources;   // 皮肤包的资源 使用默认皮肤时，与 context.getResource 一样
    private String skinPath;
    private boolean isDefaultSkin = false;

    /**
     * whether the skin being used is from external .skin file
     *
     * @return is external skin = true
     */
    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    /**
     * get current skin path
     *
     * @return current skin path
     */
    public String getSkinPath() {
        return skinPath;
    }

    /**
     * return a global static instance of {@link SkinManager}
     *
     * @return
     */
    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }

    public Resources getResources() {
        return mResources;
    }

    private SkinManager() {
    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public void restoreDefaultTheme() {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN);
        isDefaultSkin = true;
        mResources = context.getResources();
        notifySkinUpdate();
    }

    public void load() {
        String skin = SkinConfig.getCustomSkinPath(context);
        load(skin, null);
    }

    public void load(ILoaderListener callback) {
        String skin = SkinConfig.getCustomSkinPath(context);
        if (SkinConfig.isDefaultSkin(context)) {
            return;
        }
        load(skin, callback);
    }

    /**
     * Load resources from apk in asyc task
     *
     * @param skinPackagePath path of skin apk
     * @param callback        callback to notify user
     */
    public void load(String skinPackagePath, final ILoaderListener callback) {

        new AsyncTask<String, Void, Resources>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        String skinPkgPath = params[0];

                        File file = new File(skinPkgPath);
                        if (!file.exists()) {
                            return null;
                        }

                        PackageManager mPm = context.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;

                        // 这里应该是换肤的核心代码了，不同的皮肤包创建不同的 AssetManager
                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);

                        Resources superRes = context.getResources();
                        Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

                        SkinConfig.saveSkinPath(context, skinPkgPath);

                        skinPath = skinPkgPath;
                        isDefaultSkin = false;
                        return skinResource;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(Resources result) {
                mResources = result;

                if (mResources != null) {
                    if (callback != null) callback.onSuccess();
                    notifySkinUpdate();
                } else {
                    isDefaultSkin = true;
                    if (callback != null) callback.onFailed();
                }
            }

        }.execute(skinPackagePath);
    }

    @Override
    public void attach(ISkinUpdate observer) {
        if (skinObservers == null) {
            skinObservers = new ArrayList<ISkinUpdate>();
        }
        if (!skinObservers.contains(skinObservers)) {
            skinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (skinObservers == null) return;
        if (skinObservers.contains(observer)) {
            skinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (skinObservers == null) return;
        for (ISkinUpdate observer : skinObservers) {
            observer.onThemeUpdate();
        }
    }

    public int getColor(int resId) {
        int originColor = context.getResources().getColor(resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor = 0;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }

    public Drawable getDrawable(int resId) {
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);

        Drawable trueDrawable = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。</br>
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId resId
     * @return ColorStateList
     */
    @SuppressWarnings("deprecation")
    public ColorStateList convertToColorStateList(int resId) {

        boolean isExtendSkin = true;

        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false;
        }

        String resName = context.getResources().getResourceEntryName(resId);
        if (isExtendSkin) {
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            ColorStateList trueColorList;
            if (trueResId == 0) {
                try {
                    return context.getResources().getColorStateList(resId);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    trueColorList = mResources.getColorStateList(trueResId);
                    return trueColorList;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                return context.getResources().getColorStateList(resId);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        }

        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{context.getResources().getColor(resId)});
    }
}