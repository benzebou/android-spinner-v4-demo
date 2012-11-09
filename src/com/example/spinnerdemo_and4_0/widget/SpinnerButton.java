package com.example.spinnerdemo_and4_0.widget;

import java.util.List;

import com.example.spinnerdemo_and4_0.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SpinnerButton extends Button {

	private SpinnerDropDownWindow mDropDownWindow;
	
	private ISpinnerButtonListener mListener;
	
	private Context mContext;
	
	private int mXOff;
	private int mYOff = 0;
	
	/** adapter gravity SpinnerButton type */
	public Gravity mGravity;
	
	public enum Gravity {
		left, right, center
	}
	
	public void setISpinnerButtonListener(ISpinnerButtonListener listener) {
		this.mListener = listener;
	}
	
	public SpinnerButton(Context context) {
		super(context);
		this.mContext = context;
		initButton();
	}

	public SpinnerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initButton();
	}

	public SpinnerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initButton();
	}
	
	private void initButton() {
		setMinWidth(300);
		setOnClickListener(new SpinnerButtonOnClickListener());
		mDropDownWindow = new SpinnerDropDownWindow(mContext);
		setGravity(Gravity.right);
	}
	
	public void setAdapter(int resId, int txtId, List<String> data) {
		mResId = resId;
		mTxtId = txtId;
		setupData(data);
	}
	
	public void setAdapter(List<String> data) {
		mResId = R.layout.spinner_dropdown_item_text;
		mTxtId = R.id.txt;
		setupData(data);
	}
	
	private void setupData(List<String> data) {
		if (mDropDownWindow != null && data != null && data.size() > 0) {
			mDropDownWindow.setupAdapter(data);
		}
	}

	public void setGravity(Gravity gravity) {
		this.mGravity = gravity;
		updateDropWindowOff();
	}
	
	private void updateDropWindowOff() {
		if (mDropDownWindow != null) {
			int width = mDropDownWindow.getmViewWidth();
			int left = SpinnerButton.this.getLeft();
			if (mGravity == Gravity.left) {
				mXOff = 0;
			} else if (mGravity == Gravity.center) {
				mXOff = (SpinnerButton.this.getWidth() - width) / 2;
				if (mXOff < 0 && Math.abs(mXOff) > Math.abs(left)) {
					mXOff = -Math.abs(left);
				}
			} else if (mGravity == Gravity.right) {
				mXOff = SpinnerButton.this.getWidth() - width;
			}
		}
	}

	class SpinnerButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (mDropDownWindow != null) {
				if (!mDropDownWindow.isShowing()) {
					updateDropWindowOff();
					mDropDownWindow.showAsDropDown(SpinnerButton.this, mXOff, mYOff);
				}
			}
		}
		
	}
	
	private int mTxtId = -1;
	private int mResId = -1;
	
	class SpinnerDropDownWindow extends PopupWindow {
		
		private Context mContext;
		
		private int mViewWidth;
		private int mViewHeight;
		
		private LinearLayout layContent;
		
		public SpinnerDropDownWindow(Context context) {
			super(context);
			this.mContext = context;
			setAnimationStyle(R.style.Animation_dropdown);
			initWidget();
		}

		public void setupAdapter(List<String> data) {
			layContent.removeAllViews();
			LayoutInflater lInflater = LayoutInflater.from(mContext);
			for (int i=0;i<data.size();i++) {
				View v = lInflater.inflate(mResId, null);
				TextView txt = (TextView) v.findViewById(mTxtId);
				txt.setText(data.get(i));
				AdapterData aData = new AdapterData();
				aData.position = i;
				aData.value = data.get(i);
				txt.setTag(aData);
				txt.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AdapterData adata = (AdapterData) v.getTag();
						SpinnerButton.this.setText(adata.value);
						dismiss();
						if (mListener != null) {
							mListener.onClickItem(adata.position);
						}
					}
				});
				layContent.addView(v);
			}
		}

		class AdapterData {
			int position;
			String value;
		}

		private void initWidget() {
			View v = LayoutInflater.from(mContext).inflate(R.layout.spinner_dropdown_items, null);
			layContent = (LinearLayout) v.findViewById(R.id.lay_content);
			
			onMeasured(v);
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setContentView(v);
			setFocusable(true);
			setBackgroundDrawable(new BitmapDrawable());
			
			if (mListener != null) {
				mListener.setupView(v);
			}
		}
		
		/**
		 * @Description: TODO 计算View长宽
		 * @param @param v
		 */
		private void onMeasured(View v) {
			int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			v.measure(width, height);
			mViewWidth = v.getMeasuredWidth();
			mViewHeight = v.getMeasuredHeight();
		}
		
		

		public int getmViewWidth() {
			return mViewWidth;
		}

		public int getmViewHeight() {
			return mViewHeight;
		}
	}
	
	public interface ISpinnerButtonListener {
		
		/**
		 * 用于实例化 Spinner 的下拉列表布局。
		 * @param v
		 */
		void setupView(View v);
		
		void onClickItem(int position);
	}

}