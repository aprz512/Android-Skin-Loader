package cn.feng.skin.manager.base;

import android.app.Activity;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

import cn.feng.skin.manager.loader.SkinInflaterFactory;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public class FragmentActivitySkinHelper extends ActivitySkinHelper {

    public FragmentActivitySkinHelper(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate() {
        try {

            Activity activity = getActivity();
            if (activity != null) {
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(activity.getLayoutInflater(), false);

                mSkinInflaterFactory = new SkinInflaterFactory();
                activity.getLayoutInflater().setFactory(mSkinInflaterFactory);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
