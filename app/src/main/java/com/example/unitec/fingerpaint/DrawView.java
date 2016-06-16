package com.example.unitec.fingerpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kay on 29/03/2016.
 */
public class DrawView extends View {

    //--- Constants ---
    public final static int START_X = 0;
    public final static int START_Y = 0;

    //--- Fields ---
    public   Paint paint;                      // for drawing shapes on the screen
    private  Bitmap customBitmap;
    private  Canvas customCanvas;
    public int shapeSize = 20;

    //drawing path
    private Path path;
    //initial color
    private int paintColor = 0xffff0000; //red


    //--- Constructors ---
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    //setup drawing
    private void setupDrawing(){
        //create path object
        path = new Path();
        //create paint object
        paint = new Paint();
        //set initial color
        paint.setColor(paintColor);
    }

    //--- Overrides for View ---

    /**
     * Draw the bitmap on to the custom canvas with top left at (0,0).
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap( customBitmap, START_X, START_Y, paint);
    }

    /**
     * When the view size changes, create a new bitmap of the correct size
     * then create a canvas to enable us to draw on the bitmap.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        customBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        customCanvas = new Canvas(customBitmap);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (MainActivity.shape) {
            case "Circle":
                drawCircle(x,y);
                break;
            case "Triangle":
                drawTriangle(x,y);
                break;
            case "Square":
                drawSquare(x,y);
                break;
        }
        invalidate();
        return true;  // Signal that we are handling this touch
    }

    /**
     * Drawing circle when touch the screen.
     *
     * @param x    Current x of touch point.
     * @param y    Current y of touch point.
     */
    private void drawCircle(float x, float y){
        customCanvas.drawCircle(x, y, shapeSize, paint);
    }

    /**
     * Drawing triangle when touch the screen.
     *
     * @param x    Current x of touch point.
     * @param y    Current y of touch point.
     */
    private void drawTriangle(float x, float y){
        path = new Path();
        path.moveTo(x, y-shapeSize);
        path.lineTo(x-shapeSize, y+shapeSize);
        path.lineTo(x+shapeSize, y+shapeSize);
        path.close();
        customCanvas.drawPath(path, paint);
    }

    /**
     * Drawing square when touch the screen.
     *
     * @param x    Current x of touch point.
     * @param y    Current y of touch point.
     */
    private void drawSquare(float x, float y){
        customCanvas.drawRect(x - shapeSize, y - shapeSize, x + shapeSize, y + shapeSize, paint);
    }

    /**
     * Clean the canvas
     */
    public void clear(){
        customCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        shapeSize=20;
        invalidate();
    }
}
