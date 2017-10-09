package com.jmm.csg.helper;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.jmm.csg.widget.ActionSheet;
import com.jmm.csg.widget.CommonDialog;
import com.jmm.csg.widget.ListDialog;
import com.jmm.csg.widget.LoadingDialog;
import com.jmm.csg.widget.SingleButtonDialog;

public class DialogHelper {

    public static CommonDialog.Builder getDialog(Context context) {
        return new CommonDialog.Builder(context);
    }

    public static SingleButtonDialog getSingleButtonDialog(Context context, String message,
                                                           String text, View.OnClickListener listener) {
        return new SingleButtonDialog(context).setTitle("温馨提示")
                .setMessage(message).setOnClickListener(text, listener);
    }

    public static SingleButtonDialog getSingleButtonDialog(Context context, String message) {
        return new SingleButtonDialog(context).setTitle("温馨提示")
                .setMessage(message);
    }

    public static SingleButtonDialog getSingleButtonDialog(Context context, String title, String message,
                                                           String text, View.OnClickListener listener) {
        return new SingleButtonDialog(context).setTitle(title).setMessage(message)
                .setOnClickListener(text, listener);
    }

    public static CommonDialog.Builder getMessageDialog(
            Context context,
            String message,
            DialogInterface.OnClickListener listener) {
        return getDialog(context).setMessage(message)
                .setTitle("温馨提示")
                .setNegativeButtonText("确定", listener)
                .setPositiveButtonText("取消", null);
    }

    public static CommonDialog.Builder getMessageDialog(Context context, String title, String message,
                                                        DialogInterface.OnClickListener listener) {
        return getDialog(context).setTitle(title).setMessage(message)
                .setNegativeButtonText("确定", listener)
                .setPositiveButtonText("取消", null);
    }

    public static ActionSheet.Builder getActionDialog(
            Context context,
            FragmentManager fm,
            ActionSheet.ActionSheetListener listener,
            String... titles) {
        return ActionSheet.createBuilder(context, fm)
                .setCancelableOnTouchOutside(true)
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles(titles)
                .setListener(listener);
    }

    public static LoadingDialog getLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    public static <T> ListDialog getListDialog(Context context) {
        return new ListDialog<T>(context);
    }
}
