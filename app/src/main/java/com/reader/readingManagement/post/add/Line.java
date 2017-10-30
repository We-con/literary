package com.reader.readingManagement.post.add;

import android.graphics.Paint;

/**
 * Created by naver on 2017. 2. 19..
 */

public class Line {
    public Line(float firstX, float firstY, float lastX, float lastY) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.lastX = lastX;
        this.lastY = lastY;
    }

    float firstX;
    float firstY;
    float lastX;
    float lastY;

    Paint linePaint;

    public Line(Line line) {
        this.firstX = line.firstX;
        this.firstY = line.firstY;
        this.lastX = line.lastX;
        this.lastY = line.lastY;
    }

    public float getFirstX() {
        return firstX;
    }

    public void setFirstX(float firstX) {
        this.firstX = firstX;
    }

    public float getFirstY() {
        return firstY;
    }

    public void setFirstY(float firstY) {
        this.firstY = firstY;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }
}
