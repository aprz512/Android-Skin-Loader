package cn.feng.skin.manager.base;

import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;
import cn.feng.skin.manager.listener.ISkinUpdate;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public interface IActivitySkinHelper extends ISkinHelper, ISkinUpdate, IDynamicNewView {

    void onCreate();

    void onResume();

    void onPause();

    void onDestroy();


    //
    void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId);

    //
    void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs);

    //
    void enableResponseOnSkinChanging(boolean enable);
//
//    void onThemeUpdate();
//
//    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);

}
