package com.colombosoft.ednasalespad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.libs.progress.ProgressWheel;

public class CustomProgressDialog extends Dialog {

    private TextView tvProgressText;
    private ProgressWheel progressWheel;
    private Context context;

    public CustomProgressDialog(Context context) {
        super(context, false, null);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_progress);

        tvProgressText = (TextView)findViewById(R.id.progress_dialog_progress_txt);
        progressWheel = (ProgressWheel)findViewById(R.id.progress_dialog_progress_wheel);

        progressWheel.setBarColor(context.getResources().getColor(R.color.light_400));
        progressWheel.spin();

    }

    public void setMessage(String message) {
        if(tvProgressText != null) {
            tvProgressText.setText(message);
        }
    }

    public void setProgress(float progress){
        progressWheel.setProgress(progress);
    }

    public void setIndeterminate(){
        progressWheel.spin();
    }

}
