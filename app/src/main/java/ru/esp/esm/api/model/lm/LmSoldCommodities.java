package ru.esp.esm.api.model.lm;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LmSoldCommodities implements Parcelable {
    @Nullable
    private final Integer totalCount;
    @Nullable
    private final List<String> cisList; // List может содержать null элементы

    public LmSoldCommodities(@Nullable Integer totalCount, @Nullable List<String> cisList) {
        this.totalCount = totalCount;
        this.cisList = cisList != null ? new ArrayList<>(cisList) : null; // защитная копия
    }

    protected LmSoldCommodities(Parcel in) {
        // Чтение totalCount
        if (in.readByte() == 1) {
            totalCount = in.readInt();
        } else {
            totalCount = null;
        }

        // Чтение cisList
        if (in.readByte() == 1) {
            int size = in.readInt();
            List<String> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (in.readByte() == 1) {
                    list.add(in.readString());
                } else {
                    list.add(null);
                }
            }
            cisList = list;
        } else {
            cisList = null;
        }
    }

    @Nullable
    public Integer getTotalCount() {
        return totalCount;
    }

    @Nullable
    public List<String> getCisList() {
        return cisList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Запись totalCount
        if (totalCount != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(totalCount);
        } else {
            dest.writeByte((byte) 0);
        }

        // Запись cisList
        if (cisList != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(cisList.size());
            for (String item : cisList) {
                if (item != null) {
                    dest.writeByte((byte) 1);
                    dest.writeString(item);
                } else {
                    dest.writeByte((byte) 0);
                }
            }
        } else {
            dest.writeByte((byte) 0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LmSoldCommodities> CREATOR = new Creator<LmSoldCommodities>() {
        @Override
        public LmSoldCommodities createFromParcel(Parcel in) {
            return new LmSoldCommodities(in);
        }

        @Override
        public LmSoldCommodities[] newArray(int size) {
            return new LmSoldCommodities[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmSoldCommodities that = (LmSoldCommodities) o;

        if (totalCount != null ? !totalCount.equals(that.totalCount) : that.totalCount != null)
            return false;
        return cisList != null ? cisList.equals(that.cisList) : that.cisList == null;
    }

    @Override
    public int hashCode() {
        int result = totalCount != null ? totalCount.hashCode() : 0;
        result = 31 * result + (cisList != null ? cisList.hashCode() : 0);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "LmSoldCommodities{" +
                "totalCount=" + totalCount +
                ", cisList=" + cisList +
                '}';
    }
}