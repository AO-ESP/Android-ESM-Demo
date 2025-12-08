package ru.atol.os.tspiot.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Keep
public final class MarkingCodeInfo implements Parcelable {

    @NonNull
    private final String cis;

    @Nullable
    private final Boolean valid;

    @Nullable
    private final Boolean verified;

    @Nullable
    private final Boolean realizable;

    @Nullable
    private final Boolean found;

    @Nullable
    private final Boolean blocked;

    @Nullable
    private final Boolean owner;

    @Nullable
    private final Boolean sold;

    @Nullable
    private final Boolean grayZone;

    @Nullable
    private final String producerInn;

    @Nullable
    private final Integer smp;

    @Nullable
    private final String expireDate;

    @Nullable
    private final String message;

    @Nullable
    private final Integer errorCode;

    @Nullable
    private final Integer mrp;

    @Nullable
    private final Boolean isTracking;

    @Nullable
    private final String productionDate;

    @Nullable
    private final Boolean isUtilised;

    @Nullable
    private final String gtin;

    private final boolean isFromOnlineServer;

    @Nullable
    private Long duration;

    private MarkingCodeInfo(@Nullable Boolean valid, @Nullable Boolean verified, @Nullable Boolean realizable, @Nullable Boolean found, @Nullable Boolean blocked, @Nullable Boolean owner, @Nullable Boolean sold, @Nullable Boolean grayZone, @Nullable String producerInn, @Nullable Integer smp, @Nullable String expireDate, @NonNull String cis, @Nullable String message, @Nullable Integer errorCode, @Nullable Integer mrp, @Nullable Boolean isTracking, @Nullable String productionDate, @Nullable Boolean isUtilised, @Nullable String gtin, boolean isFromOnlineServer, @Nullable Long duration) {
        this.valid = valid;
        this.verified = verified;
        this.realizable = realizable;
        this.found = found;
        this.blocked = blocked;
        this.owner = owner;
        this.sold = sold;
        this.grayZone = grayZone;
        this.producerInn = producerInn;
        this.smp = smp;
        this.expireDate = expireDate;
        this.cis = cis;
        this.message = message;
        this.errorCode = errorCode;
        this.mrp = mrp;
        this.isTracking = isTracking;
        this.productionDate = productionDate;
        this.isUtilised = isUtilised;
        this.gtin = gtin;
        this.isFromOnlineServer = isFromOnlineServer;
        this.duration = duration;
    }

    private MarkingCodeInfo(Parcel in) {
        cis = Objects.requireNonNull(in.readString());
        byte tmpValid = in.readByte();
        valid = tmpValid == 0 ? null : tmpValid == 1;
        byte tmpVerified = in.readByte();
        verified = tmpVerified == 0 ? null : tmpVerified == 1;
        byte tmpRealizable = in.readByte();
        realizable = tmpRealizable == 0 ? null : tmpRealizable == 1;
        byte tmpFound = in.readByte();
        found = tmpFound == 0 ? null : tmpFound == 1;
        byte tmpBlocked = in.readByte();
        blocked = tmpBlocked == 0 ? null : tmpBlocked == 1;
        byte tmpOwner = in.readByte();
        owner = tmpOwner == 0 ? null : tmpOwner == 1;
        byte tmpSold = in.readByte();
        sold = tmpSold == 0 ? null : tmpSold == 1;
        byte tmpGrayZone = in.readByte();
        grayZone = tmpGrayZone == 0 ? null : tmpGrayZone == 1;
        producerInn = in.readString();
        if (in.readByte() == 0) {
            smp = null;
        } else {
            smp = in.readInt();
        }
        expireDate = in.readString();
        message = in.readString();
        if (in.readByte() == 0) {
            errorCode = null;
        } else {
            errorCode = in.readInt();
        }
        if (in.readByte() == 0) {
            mrp = null;
        } else {
            mrp = in.readInt();
        }
        byte tmpIsTracking = in.readByte();
        isTracking = tmpIsTracking == 0 ? null : tmpIsTracking == 1;
        productionDate = in.readString();
        byte tmpIsUtilised = in.readByte();
        isUtilised = tmpIsUtilised == 0 ? null : tmpIsUtilised == 1;
        gtin = in.readString();
        isFromOnlineServer = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cis);
        dest.writeByte((byte) (valid == null ? 0 : valid ? 1 : 2));
        dest.writeByte((byte) (verified == null ? 0 : verified ? 1 : 2));
        dest.writeByte((byte) (realizable == null ? 0 : realizable ? 1 : 2));
        dest.writeByte((byte) (found == null ? 0 : found ? 1 : 2));
        dest.writeByte((byte) (blocked == null ? 0 : blocked ? 1 : 2));
        dest.writeByte((byte) (owner == null ? 0 : owner ? 1 : 2));
        dest.writeByte((byte) (sold == null ? 0 : sold ? 1 : 2));
        dest.writeByte((byte) (grayZone == null ? 0 : grayZone ? 1 : 2));
        dest.writeString(producerInn);
        if (smp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(smp);
        }
        dest.writeString(expireDate);
        dest.writeString(message);
        if (errorCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(errorCode);
        }
        if (mrp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mrp);
        }
        dest.writeByte((byte) (isTracking == null ? 0 : isTracking ? 1 : 2));
        dest.writeString(productionDate);
        dest.writeByte((byte) (isUtilised == null ? 0 : isUtilised ? 1 : 2));
        dest.writeString(gtin);
        dest.writeByte((byte) (isFromOnlineServer ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarkingCodeInfo> CREATOR = new Creator<>() {
        @Override
        public MarkingCodeInfo createFromParcel(Parcel in) {
            return new MarkingCodeInfo(in);
        }

        @Override
        public MarkingCodeInfo[] newArray(int size) {
            return new MarkingCodeInfo[size];
        }
    };

    public boolean isFromOnlineServer() {
        return isFromOnlineServer;
    }

    public boolean isFromOfflineServer() {
        return !isFromOnlineServer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkingCodeInfo)) return false;
        MarkingCodeInfo that = (MarkingCodeInfo) o;
        return isFromOnlineServer == that.isFromOnlineServer && Objects.equals(cis, that.cis) && Objects.equals(valid, that.valid) && Objects.equals(verified, that.verified) && Objects.equals(realizable, that.realizable) && Objects.equals(found, that.found) && Objects.equals(blocked, that.blocked) && Objects.equals(owner, that.owner) && Objects.equals(sold, that.sold) && Objects.equals(grayZone, that.grayZone) && Objects.equals(producerInn, that.producerInn) && Objects.equals(smp, that.smp) && Objects.equals(expireDate, that.expireDate) && Objects.equals(message, that.message) && Objects.equals(errorCode, that.errorCode) && Objects.equals(mrp, that.mrp) && Objects.equals(isTracking, that.isTracking) && Objects.equals(productionDate, that.productionDate) && Objects.equals(isUtilised, that.isUtilised) && Objects.equals(gtin, that.gtin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cis, valid, verified, realizable, found, blocked, owner, sold, grayZone, producerInn, smp, expireDate, message, errorCode, mrp, isTracking, productionDate, isUtilised, gtin, isFromOnlineServer);
    }

    @NonNull
    @Override
    public String toString() {
        return "MarkingCodeInfo{" +
                "cis='" + cis + '\'' +
                ", valid=" + valid +
                ", verified=" + verified +
                ", realizable=" + realizable +
                ", found=" + found +
                ", blocked=" + blocked +
                ", owner=" + owner +
                ", sold=" + sold +
                ", grayZone=" + grayZone +
                ", producerInn='" + producerInn + '\'' +
                ", smp=" + smp +
                ", expireDate='" + expireDate + '\'' +
                ", message='" + message + '\'' +
                ", errorCode=" + errorCode +
                ", mrp=" + mrp +
                ", isTracking=" + isTracking +
                ", productionDate='" + productionDate + '\'' +
                ", isUtilised=" + isUtilised +
                ", gtin='" + gtin + '\'' +
                ", isFromOnlineServer=" + isFromOnlineServer +
                '}';
    }

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Nullable
    public Date getExpireDateAsDate() throws ParseException {
        if (expireDate != null) {
            return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.ROOT).parse(expireDate);
        }
        return null;
    }

    @Nullable
    public Integer getMrp() {
        return mrp;
    }

    @Nullable
    public Boolean isTracking() {
        return isTracking;
    }

    @Nullable
    public String getProductionDate() {
        return productionDate;
    }

    @Nullable
    public Date getProductionDateAsDate() throws ParseException {
        if (productionDate != null) {
            return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.ROOT).parse(productionDate);
        }
        return null;
    }

    @Nullable
    public Boolean isUtilised() {
        return isUtilised;
    }

    @Nullable
    public String getGtin() {
        return gtin;
    }

    public int getErrorCode() {
        if (errorCode == null) {
            return 0;
        }
        return errorCode;
    }

    @NonNull
    public String getCis() {
        return cis;
    }

    @Nullable
    public Boolean isValid() {
        return valid;
    }

    @Nullable
    public Boolean isVerified() {
        return verified;
    }

    @Nullable
    public Boolean isRealizable() {
        return realizable;
    }

    @Nullable
    public Boolean isFound() {
        return found;
    }

    @Nullable
    public Boolean isBlocked() {
        return blocked;
    }

    @Nullable
    public Boolean isOwner() {
        return owner;
    }

    @Nullable
    public Boolean isSold() {
        return sold;
    }

    @Nullable
    public Boolean isGrayZone() {
        return grayZone;
    }

    @Nullable
    public String getProducerInn() {
        return producerInn;
    }

    @Nullable
    public Integer getSmp() {
        return smp;
    }

    @Nullable
    public String getExpireDate() {
        return expireDate;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Nullable
    public Long getDuration() {
        return duration;
    }

    public static final class Builder {
        private final String cis;
        private String expireDate;
        private Integer smp;
        private Integer errorCode;
        private Integer mrp;
        private String producerInn;
        private String message;
        private String productionDate;
        private String gtin;
        private Boolean isGrayZone;
        private Boolean isSold;
        private Boolean isOwner;
        private Boolean isBlocked;
        private Boolean isFound;
        private Boolean isRealizable;
        private Boolean isVerified;
        private Boolean isValid;
        private Boolean isTracking;
        private Boolean utilised;
        private boolean isFromOnline;
        private Long duration;

        public Builder(@NonNull String cis) {
            Objects.requireNonNull(cis);
            this.cis = cis;
        }

        public Builder setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder setExpireDate(String expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public Builder setSmp(Integer smp) {
            this.smp = smp;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }


        public Builder setProducerInn(String producerInn) {
            this.producerInn = producerInn;
            return this;
        }

        public Builder setGrayZone(Boolean isGrayZone) {
            this.isGrayZone = isGrayZone;
            return this;
        }

        public Builder setSold(Boolean isSold) {
            this.isSold = isSold;
            return this;
        }

        public Builder setOwner(Boolean isOwner) {
            this.isOwner = isOwner;
            return this;
        }

        public Builder setBlocked(Boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder setFound(Boolean isFound) {
            this.isFound = isFound;
            return this;
        }

        public Builder setRealizable(Boolean isRealizable) {
            this.isRealizable = isRealizable;
            return this;
        }

        public Builder setVerified(Boolean isVerified) {
            this.isVerified = isVerified;
            return this;
        }

        public Builder setValid(Boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public Builder setMrp(Integer mrp) {
            this.mrp = mrp;
            return this;
        }

        public Builder setTracking(Boolean tracking) {
            this.isTracking = tracking;
            return this;
        }

        public Builder setProductionDate(String productionDate) {
            this.productionDate = productionDate;
            return this;
        }

        public Builder setUtilised(Boolean utilised) {
            this.utilised = utilised;
            return this;
        }

        public Builder setGtin(String gtin) {
            this.gtin = gtin;
            return this;
        }

        public Builder setFromOnline(boolean isFromOnline) {
            this.isFromOnline = isFromOnline;
            return this;
        }

        public Builder setDuration(Long duration) {
            this.duration = duration;
            return this;
        }

        public MarkingCodeInfo build() {
            return new MarkingCodeInfo(isValid, isVerified, isRealizable, isFound, isBlocked, isOwner, isSold, isGrayZone, producerInn, smp, expireDate, cis, message, errorCode, mrp, isTracking, productionDate, utilised, gtin, isFromOnline, duration);
        }
    }
}
