package com.lynhill.ghpc.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Representatives implements Parcelable {
    public Representatives(Parcel in) {
        name = in.readString();
        address = in.readString();
        dob = in.readString();
        emali = in.readString();
        phoneNumber = in.readString();
        signature = in.readString();
        sampleImages = in.createStringArrayList();
    }

    public Representatives() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(dob);
        dest.writeString(emali);
        dest.writeString(phoneNumber);
        dest.writeString(signature);
        dest.writeStringList(sampleImages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Representatives> CREATOR = new Creator<Representatives>() {
        @Override
        public Representatives createFromParcel(Parcel in) {
            return new Representatives(in);
        }

        @Override
        public Representatives[] newArray(int size) {
            return new Representatives[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmali() {
        return emali;
    }

    public void setEmali(String emali) {
        this.emali = emali;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ArrayList<String> getSampleImages() {
        return sampleImages;
    }

    public void setSampleImages(ArrayList<String> sampleImages) {
        this.sampleImages = sampleImages;
    }

    String name;
    String address;
    String dob;
    String emali;
    String phoneNumber;
    String signature;
    ArrayList<String> sampleImages;
}
