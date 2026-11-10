package ru.esm.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

@Keep
public final class IsmNoticeInfo implements Parcelable {
    private final String issueDate;
    private final String sendDate;
    private final int noticeId;
    private final String receiptId;

    public IsmNoticeInfo(String issueDate, String sendDate, int noticeId, String receiptId) {
        this.issueDate = issueDate;
        this.sendDate = sendDate;
        this.noticeId = noticeId;
        this.receiptId = receiptId;
    }

    private IsmNoticeInfo(Parcel in) {
        issueDate = in.readString();
        sendDate = in.readString();
        noticeId = in.readInt();
        receiptId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(issueDate);
        dest.writeString(sendDate);
        dest.writeInt(noticeId);
        dest.writeString(receiptId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IsmNoticeInfo> CREATOR = new Creator<IsmNoticeInfo>() {
        @Override
        public IsmNoticeInfo createFromParcel(Parcel in) {
            return new IsmNoticeInfo(in);
        }

        @Override
        public IsmNoticeInfo[] newArray(int size) {
            return new IsmNoticeInfo[size];
        }
    };

    public int getNoticeId() {
        return noticeId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getSendDate() {
        return sendDate;
    }

    @Override
    public String toString() {
        return "IsmNoticeInfo{" +
                "issueDate='" + issueDate + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", noticeId=" + noticeId +
                ", receiptId='" + receiptId + '\'' +
                '}';
    }

    public static IsmNoticeInfo fromJson(String rawJson) {
        try {
            JSONObject object = new JSONObject(rawJson);
            String issueDate = object.optString("issue_date");
            String sendDate = object.optString("send_date");
            int noticeId = object.optInt("notice_id", -1);
            String receiptId = object.optString("receiptId");
            return new IsmNoticeInfo(issueDate, sendDate, noticeId, receiptId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
