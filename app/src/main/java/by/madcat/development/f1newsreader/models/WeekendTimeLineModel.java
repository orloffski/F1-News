package by.madcat.development.f1newsreader.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WeekendTimeLineModel implements Parcelable {

    private String mMessage;
    private String mDate;
    private WeekendItemStatus mStatus;

    public WeekendTimeLineModel() {
    }

    public WeekendTimeLineModel(String mMessage, String mDate, WeekendItemStatus mStatus) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void semMessage(String message) {
        this.mMessage = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public WeekendItemStatus getStatus() {
        return mStatus;
    }

    public void setStatus(WeekendItemStatus mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mDate);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
    }

    protected WeekendTimeLineModel(Parcel in) {
        this.mMessage = in.readString();
        this.mDate = in.readString();
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : WeekendItemStatus.values()[tmpMStatus];
    }

    public static final Parcelable.Creator<WeekendTimeLineModel> CREATOR = new Parcelable.Creator<WeekendTimeLineModel>() {
        @Override
        public WeekendTimeLineModel createFromParcel(Parcel source) {
            return new WeekendTimeLineModel(source);
        }

        @Override
        public WeekendTimeLineModel[] newArray(int size) {
            return new WeekendTimeLineModel[size];
        }
    };
}