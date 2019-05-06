/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private CircularLinkedList listA = new CircularLinkedList();
    private CircularLinkedList listB = new CircularLinkedList();
    private CircularLinkedList listC = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(10);

        Point lastPoint = null;
        for (Point p : list) {
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if(lastPoint != null){
                canvas.drawLine(lastPoint.x, lastPoint.y, p.x, p.y, linePaint);
            }
            lastPoint = p;
        }

        Paint pointPaintA = new Paint();
        pointPaintA.setColor(Color.RED);
        Paint linePaintA = new Paint();
        linePaintA.setColor(Color.RED);
        linePaintA.setStrokeWidth(10);

        Paint pointPaintB = new Paint();
        pointPaintB.setColor(Color.BLUE);
        Paint linePaintB = new Paint();
        linePaintB.setColor(Color.BLUE);
        linePaintB.setStrokeWidth(10);

        Paint pointPaintC = new Paint();
        pointPaintC.setColor(Color.GREEN);
        Paint linePaintC = new Paint();
        linePaintC.setColor(Color.GREEN);
        linePaintC.setStrokeWidth(10);

        Point lastPointA = null;
        for (Point p : listA) {
            canvas.drawCircle(p.x, p.y, 20, pointPaintA);
            if(lastPointA != null){
                canvas.drawLine(lastPointA.x, lastPointA.y, p.x, p.y, linePaintA);
            }
            lastPointA = p;
        }
        Point lastPointB = null;
        for (Point p : listB) {
            canvas.drawCircle(p.x, p.y, 20, pointPaintB);
            if(lastPointB != null){
                canvas.drawLine(lastPointB.x, lastPointB.y, p.x, p.y, linePaintB);
            }
            lastPointB = p;
        }
        Point lastPointC = null;
        for (Point p : listC) {
            canvas.drawCircle(p.x, p.y, 20, pointPaintC);
            if(lastPointC != null){
                canvas.drawLine(lastPointC.x, lastPointC.y, p.x, p.y, linePaintC);
            }
            lastPointC = p;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Compare")) {
                    listA.insertBeginning(p);
                    listB.insertNearest(p);
                    listC.insertSmallest(p);
                }
                else if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    if (insertMode.equals("Compare")) {
                        message.setText(
                                String.format("Tour A is now %.2f", listA.totalDistance()) + "\n" +
                                String.format("Tour B is now %.2f", listB.totalDistance()) + "\n" +
                                String.format("Tour C is now %.2f", listC.totalDistance())
                        );
                    }
                    else{
                        message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                    }
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        if(mode.equals("Compare")){
            reset();
        }
        insertMode = mode;
    }
}
