package com.nickpontiff.swipetofinish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ExampleActivity extends SwipeToFinishActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.activity_example_root);
        enableSwipeLeftToFinish(rootLayout);

        Button button = (Button) findViewById(R.id.activity_example_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExampleActivity.this, ExampleActivity.class));
            }
        });
    }


}
