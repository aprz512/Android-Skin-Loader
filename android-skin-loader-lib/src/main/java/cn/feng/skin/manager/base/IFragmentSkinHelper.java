package cn.feng.skin.manager.base;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public interface IFragmentSkinHelper {

    void onAttach(Context context);

    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);

}
