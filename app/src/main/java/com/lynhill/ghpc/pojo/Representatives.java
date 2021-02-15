package com.lynhill.ghpc.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Representatives implements Parcelable {

    public Representatives() {

    }

    protected Representatives(Parcel in) {
        name = in.readString();
        address = in.readString();
        dob = in.readString();
        project = in.readString();
        emali = in.createStringArrayList();
        phoneNumber = in.createStringArrayList();
        signature = in.readString();
        sampleImages = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(dob);
        dest.writeString(project);
        dest.writeStringList(emali);
        dest.writeStringList(phoneNumber);
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

    public ArrayList<String> getEmali() {
        return emali;
    }
    public void setEmali(ArrayList<String> emali) {
        this.emali = emali;
    }

    public ArrayList<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(ArrayList<String> phoneNumber) {
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    String project;
    ArrayList<String> emali;
    ArrayList<String> phoneNumber;
    String signature;
    ArrayList<String> sampleImages;
}
