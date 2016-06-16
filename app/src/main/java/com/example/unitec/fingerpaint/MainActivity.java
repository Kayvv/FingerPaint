package com.example.unitec.fingerpaint;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.UUID;
/**
 * Created by Kay on 29/03/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Buttons and Views
    private RadioGroup shapeGroup;
    private RadioGroup colorGroup;
    private RadioGroup colorGroup1;
    private RadioGroup colorGroup2;
    private RadioButton radioColor;
    private DrawView drawView;
    private Button rightButton;
    private Button leftButton;
    private Button restButton;
    private LinearLayout menuLayout;
    private SeekBar sizeControl;
    //Some needed value
    public static ColorDrawable color;
    public static int colorId;
    public static String shape="Circle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView=(DrawView)findViewById(R.id.paint_view);

        //select color
        colorGroup=(RadioGroup)findViewById(R.id.colorGroup);
        colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioColor = (RadioButton) findViewById(checkedId);
                color = (ColorDrawable) radioColor.getBackground();
                colorId = color.getColor();
                drawView.paint.setColor(colorId);
            }
        });

        colorGroup1=(RadioGroup)findViewById(R.id.colorGroup1);
        colorGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioColor = (RadioButton) findViewById(checkedId);
                color = (ColorDrawable) radioColor.getBackground();
                colorId = color.getColor();
                drawView.paint.setColor(colorId);
            }
        });

        colorGroup2=(RadioGroup)findViewById(R.id.colorGroup2);
        colorGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioColor = (RadioButton) findViewById(checkedId);
                color = (ColorDrawable) radioColor.getBackground();
                colorId = color.getColor();
                drawView.paint.setColor(colorId);
            }
        });

        shapeGroup=(RadioGroup)findViewById(R.id.radioShape);
        shapeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.circle:
                        shape = "Circle";
                        break;
                    case R.id.square:
                        shape = "Square";
                        break;
                    case R.id.triangle:
                        shape = "Triangle";
                        break;
                }
            }
        });

        //Using seekbar to change the size
        sizeControl = (SeekBar) findViewById(R.id.seekBar);
        sizeControl.setMax(50);
        sizeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawView.shapeSize = progress+10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Control menu Button
        rightButton = (Button) findViewById(R.id.right_button);
        rightButton.setOnClickListener(this);

        //Hide the style menu
        leftButton = (Button) findViewById(R.id.left_button);
        leftButton.setOnClickListener(this);

        //Hide the style menu
        restButton = (Button) findViewById(R.id.rest_button);
        restButton.setOnClickListener(this);

        //The exit Button
        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(this);
        //Reset Button When clicked reset the canvas
        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(this);
        //Save Button When clicked
        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(this);

    }

    /**
     * Listen every click event and determine which view have been clicked by ID
     *
     * @param v    Current view that have been clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.right_button:
                menuLayout = (LinearLayout) findViewById(R.id.control_layout);
                menuLayout.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.GONE);
                drawView.setVisibility(View.GONE);
                break;
            case R.id.rest_button:
            case R.id.left_button:
                menuLayout = (LinearLayout) findViewById(R.id.control_layout);
                menuLayout.setVisibility(View.GONE);
                rightButton.setVisibility(View.VISIBLE);
                drawView.setVisibility(View.VISIBLE);
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.reset:
                //reset drawing
                AlertDialog.Builder resetDialog = new AlertDialog.Builder(MainActivity.this);
                resetDialog.setTitle("Reset painting");
                resetDialog.setMessage("Delete current painting?");
                resetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //reset drawing
                        drawView.clear();
                    }
                });
                resetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                resetDialog.show();
                break;
            case R.id.save:
                //save drawing
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(MainActivity.this);
                saveDialog.setTitle("Save painting");
                saveDialog.setMessage("Save current painting to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //save drawing
                        drawView.setDrawingCacheEnabled(true);

                        //attempt to save
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(),  drawView.getDrawingCache(),
                                UUID.randomUUID().toString() + ".png", "drawing");    //random name
                        //saving process is good
                        if (imgSaved != null) {
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "The painting is saved to Gallery successfully.", Toast.LENGTH_LONG);
                            savedToast.show();
                        }
                        //saving process is error
                        else {
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "The painting can not be saved.", Toast.LENGTH_LONG);
                            unsavedToast.show();
                        }
                        drawView.destroyDrawingCache();
                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                saveDialog.show();
                break;
        }
    }
}
