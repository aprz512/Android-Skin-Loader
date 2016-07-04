package cn.feng.skin.manager.base;

import android.app.Activity;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.loader.SkinInflaterFactory;
import cn.feng.skin.manager.loader.SkinManager;

/**
 * 作者：aprz on 2016/6/30.
 * 邮箱：aprz512@163.com
 * <p>
 * 简化继承
 */
public class ActivitySkinHelper implements IActivitySkinHelper {
    /**
     * Whether response to skin changing after create
     */
    protected boolean isResponseOnSkinChanging = true;

    protected SkinInflaterFactory mSkinInflaterFactory;

    private WeakReference<Activity> mWeakReference;

    public ActivitySkinHelper(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    protected Activity getActivity() {
        if (mWeakReference == null) {
            return null;
        }

        return mWeakReference.get();
    }

    @Override
    public void onCreate() {
        mSkinInflaterFactory = new SkinInflaterFactory();
        Activity activity = getActivity();
        if (activity != null) {
            activity.getLayoutInflater().setFactory(mSkinInflaterFactory);
        }
    }

    @Override
    public void onResume() {
        SkinManager.getInstance().attach(this);
    }

    @Override
    public void onDestroy() {
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueResId) {
        Activity activity = getActivity();
        if (activity != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(activity, view, attrName, attrValueResId);
        }
    }

    @Override
    public void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }

    @Override
    public void onThemeUpdate() {
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        Activity activity = getActivity();
        if (activity != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(activity, view, pDAttrs);
        }
    }
}
