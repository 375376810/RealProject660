package com.project.mobilesafe660.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * ����ǿ�ƻ�ȡ�����TextView
 * @author Ԭ����
 */
public class FocusedTextView extends TextView {

	//�������ļ��о������Ժ���ʽstyleʱ,ϵͳ�ײ����ʱ,�ͻ��ߴ˹��췽��
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		System.out.println("FocusedTextView�����˴���ʽ�Ĺ��췽��");
	}

	//�������ļ��о�������ʱ,ϵͳ�ײ����ʱ,�ͻ��ߴ˹��췽��
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		System.out.println("FocusedTextView�����˴����ԵĹ��췽��");
	}

	//�Ӵ�����new����
	public FocusedTextView(Context context) {
		super(context);
		System.out.println("FocusedTextView���������κ�������ʽ�Ĺ��췽��");
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;//ǿ����TextView���н���
	}

}
