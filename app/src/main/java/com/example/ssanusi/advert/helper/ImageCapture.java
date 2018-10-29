package com.example.ssanusi.advert.helper;

import java.io.Serializable;

public class ImageCapture implements Serializable {
    private int position;
    private String fileUrl;
    private String fileName;

    public ImageCapture(int position, String fileUrl, String fileName) {
        this.position = position;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String url) {
        this.fileName = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


//    public ImageCapture(){
//        super();
//    }

//    public ImageCapture(Parcel parcel){
//        this.position = parcel.readInt();
//        this.fileUrl  = parcel.readString();
//        this.fileName = parcel.readString();
//    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(this.position);
//        parcel.writeString(this.fileUrl);
//        parcel.writeString(this.fileName);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<ImageCapture> CREATOR = new Creator<ImageCapture>() {
//        @Override
//        public ImageCapture createFromParcel(Parcel parcel) {
//            return new ImageCapture(parcel);
//        }
//
//        @Override
//        public ImageCapture[] newArray(int i) {
//            return new ImageCapture[i];
//        }
//    };

}
