package com.example.spinnerdemo_and4_0;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.spinnerdemo_and4_0.widget.SpinnerButton;
import com.example.spinnerdemo_and4_0.widget.SpinnerButton.Gravity;
import com.example.spinnerdemo_and4_0.widget.SpinnerButton.ISpinnerButtonListener;

public class MainActivity extends Activity implements ISpinnerButtonListener {

	SpinnerButton mSpinnerBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinnerBtn = (SpinnerButton) findViewById(R.id.spinner_btn);
        List<String> data = new ArrayList<String>();
        data.add("left");
        data.add("center");
        data.add("right");
        mSpinnerBtn.setAdapter(data);
        mSpinnerBtn.setISpinnerButtonListener(this);
//        mSpinnerBtn.setGravity(Gravity.left);
    }

	@Override
	public void setupView(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickItem(int position) {
		Toast.makeText(this, "click: " + position, 0).show();
		if (position == 0) {
			mSpinnerBtn.setGravity(Gravity.left);
		} else if (position == 1) {
			mSpinnerBtn.setGravity(Gravity.center);
		} else {
			mSpinnerBtn.setGravity(Gravity.right);
		}
	}

}
