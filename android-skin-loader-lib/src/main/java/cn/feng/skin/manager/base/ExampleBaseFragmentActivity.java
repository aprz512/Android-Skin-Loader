package cn.feng.skin.manager.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public class ExampleBaseFragmentActivity extends FragmentActivity {

    private IActivitySkinHelper mSkinHelper = new FragmentActivitySkinHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSkinHelper.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSkinHelper.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSkinHelper.onDestroy();
    }

    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinHelper.dynamicAddView(view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        mSkinHelper.dynamicAddView(view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable) {
        mSkinHelper.enableResponseOnSkinChanging(enable);
    }

    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinHelper.dynamicAddView(view, pDAttrs);
    }
}
