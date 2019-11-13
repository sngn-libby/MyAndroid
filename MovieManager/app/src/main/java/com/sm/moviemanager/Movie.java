package com.sm.moviemanager;

public class Movie {
    private String mTitle, mContent;
    private int mImg; //resource Item 이기때문에 --> R.img.drawable 이런식으로

    public Movie(String mTitle, String mContent, int mImg) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mImg = mImg;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public int getmImg() {
        return mImg;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public void setmImg(int mImg) {
        this.mImg = mImg;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mImg=" + mImg +
                '}';
    }
}
