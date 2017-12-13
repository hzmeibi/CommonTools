package com.tools.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by milo on 17/9/14.
 * 给TextView中的文字添加超链接
 * 设置颜色  监听点击回调
 */
public class TextLinkUtil {
    SpannableStringBuilder builder;

    public TextLinkUtil(Context context, TextView textView, int color, String contentStr, String[] linkStrs, final OnClickCallback onClickCallback) {
        builder = new SpannableStringBuilder(contentStr);
        for (final String linkStr : linkStrs) {
            int start = contentStr.indexOf(linkStr);//若内容不存在返回-1
            int end = start + linkStr.length();//结束的下表 小于总长度
            if (start < 0 || end > contentStr.length()) {
                return;
            }
            //文字颜色
            ForegroundColorSpan blueColor = new ForegroundColorSpan(color);
            builder.setSpan(blueColor, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //设置点击事件....
            builder.setSpan(new NoLineClickSpan(context) {
                @Override
                public void onClick(View widget) {
                    onClickCallback.onClick(linkStr);
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 无下划线超链接，
     * 使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
     */
    private class NoLineClickSpan extends ClickableSpan {
        private Context mContext;

        public NoLineClickSpan(Context context) {
            mContext = context;
        }

        public NoLineClickSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            /**set textColor**/
//        ds.setColor(mContext.getResources().getColor(R.color.app_blue));
            /**Remove the underline**/
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
        }
    }

    public interface OnClickCallback {
        void onClick(String linkUrl);
    }
}
