package cn.feng.skin.manager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public class ExampleBaseFragment extends Fragment {

    private IFragmentSkinHelper mFragmentSkinHelper = new FragmentSkinHelper();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFragmentSkinHelper.onAttach(context);
    }

    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mFragmentSkinHelper.dynamicAddView(view, pDAttrs);
    }

    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return getActivity().getLayoutInflater();
    }
}
