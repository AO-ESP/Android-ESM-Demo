package ru.atol.os.tspiot.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Keep
public final class MarkingCodeInfo implements Parcelable {
    @NonNull
    private final String cis;
    @Nullable
    private final Boolean found;
    @Nullable
    private final Boolean valid;
    @Nullable
    private final String printView;
    @Nullable
    private final String gtin;
    @Nullable
    private final Boolean isGreyGtin;
    @Nullable
    private final List<String> groupId;
    @Nullable
    private final Boolean verified;
    @Nullable
    private final Boolean realizable;
    @Nullable
    private final Boolean utilised;
    @Nullable
    private final String expireDate;
    @Nullable
    private final Map<String, String> variableExpirations;
    @Nullable
    private final String productionDate;
    @Nullable
    private final Long productWeight;
    @Nullable
    private final String prVetDocument;
    @Nullable
    private final Boolean isOwner;
    @Nullable
    private final Boolean isBlocked;
    @Nullable
    private final List<String> ogvs;
    @Nullable
    private final String message;
    @Nullable
    private final Integer errorCode;
    @Nullable
    private final Boolean isTracking;
    @Nullable
    private final Boolean sold;
    @Nullable
    private final Integer eliminationState;
    @Nullable
    private final Integer mrp;
    @Nullable
    private final Integer smp;
    @Nullable
    private final Boolean grayZone;
    @Nullable
    private final Integer innerUnitCount;
    @Nullable
    private final Integer soldUnitCount;
    @Nullable
    private final String packageType;
    @Nullable
    private final Integer packageQuantity;
    @Nullable
    private final String parent;
    @Nullable
    private final String producerInn;
    @Nullable
    private final String productionSerialNumber;
    @Nullable
    private final String productionBatchNumber;
    @Nullable
    private final String factorySerialNumber;

    private final boolean isFromOnlineServer;
    @Nullable
    private final Long duration;

    private MarkingCodeInfo(
            @NonNull String cis,
            @Nullable Boolean found,
            @Nullable Boolean valid,
            @Nullable String printView,
            @Nullable String gtin,
            @Nullable Boolean isGreyGtin,
            @Nullable List<String> groupId,
            @Nullable Boolean verified,
            @Nullable Boolean realizable,
            @Nullable Boolean utilised,
            @Nullable String expireDate,
            @Nullable Map<String, String> variableExpirations,
            @Nullable String productionDate,
            @Nullable Long productWeight,
            @Nullable String prVetDocument,
            @Nullable Boolean isOwner,
            @Nullable Boolean isBlocked,
            @Nullable List<String> ogvs,
            @Nullable String message,
            @Nullable Integer errorCode,
            @Nullable Boolean isTracking,
            @Nullable Boolean sold,
            @Nullable Integer eliminationState,
            @Nullable Integer mrp,
            @Nullable Integer smp,
            @Nullable Boolean grayZone,
            @Nullable Integer innerUnitCount,
            @Nullable Integer soldUnitCount,
            @Nullable String packageType,
            @Nullable Integer packageQuantity,
            @Nullable String parent,
            @Nullable String producerInn,
            @Nullable String productionSerialNumber,
            @Nullable String productionBatchNumber,
            @Nullable String factorySerialNumber,
            boolean isFromOnlineServer,
            @Nullable Long duration
    ) {
        this.cis = cis;
        this.found = found;
        this.valid = valid;
        this.printView = printView;
        this.gtin = gtin;
        this.isGreyGtin = isGreyGtin;
        this.groupId = groupId;
        this.verified = verified;
        this.realizable = realizable;
        this.utilised = utilised;
        this.expireDate = expireDate;
        this.variableExpirations = variableExpirations;
        this.productionDate = productionDate;
        this.productWeight = productWeight;
        this.prVetDocument = prVetDocument;
        this.isOwner = isOwner;
        this.isBlocked = isBlocked;
        this.ogvs = ogvs;
        this.message = message;
        this.errorCode = errorCode;
        this.isTracking = isTracking;
        this.sold = sold;
        this.eliminationState = eliminationState;
        this.mrp = mrp;
        this.smp = smp;
        this.grayZone = grayZone;
        this.innerUnitCount = innerUnitCount;
        this.soldUnitCount = soldUnitCount;
        this.packageType = packageType;
        this.packageQuantity = packageQuantity;
        this.parent = parent;
        this.producerInn = producerInn;
        this.productionSerialNumber = productionSerialNumber;
        this.productionBatchNumber = productionBatchNumber;
        this.factorySerialNumber = factorySerialNumber;
        this.isFromOnlineServer = isFromOnlineServer;
        this.duration = duration;
    }

    private MarkingCodeInfo(Parcel in) {
        ParcelUtils parcelUtils = ParcelUtils.getInstance();
        cis = Objects.requireNonNull(in.readString());
        found = parcelUtils.readNullableBoolean(in);
        valid = parcelUtils.readNullableBoolean(in);
        printView = parcelUtils.readNullableString(in);
        gtin = parcelUtils.readNullableString(in);
        isGreyGtin = parcelUtils.readNullableBoolean(in);
        groupId = parcelUtils.readNullableStringList(in);
        verified = parcelUtils.readNullableBoolean(in);
        realizable = parcelUtils.readNullableBoolean(in);
        utilised = parcelUtils.readNullableBoolean(in);
        expireDate = parcelUtils.readNullableString(in);
        variableExpirations = parcelUtils.readNullableStringMap(in);
        productionDate = parcelUtils.readNullableString(in);
        productWeight = parcelUtils.readNullableLong(in);
        prVetDocument = parcelUtils.readNullableString(in);
        isOwner = parcelUtils.readNullableBoolean(in);
        isBlocked = parcelUtils.readNullableBoolean(in);
        ogvs = parcelUtils.readNullableStringList(in);
        message = parcelUtils.readNullableString(in);
        errorCode = parcelUtils.readNullableInt(in);
        isTracking = parcelUtils.readNullableBoolean(in);
        sold = parcelUtils.readNullableBoolean(in);
        eliminationState = parcelUtils.readNullableInt(in);
        mrp = parcelUtils.readNullableInt(in);
        smp = parcelUtils.readNullableInt(in);
        grayZone = parcelUtils.readNullableBoolean(in);
        innerUnitCount = parcelUtils.readNullableInt(in);
        soldUnitCount = parcelUtils.readNullableInt(in);
        packageType = parcelUtils.readNullableString(in);
        packageQuantity = parcelUtils.readNullableInt(in);
        parent = parcelUtils.readNullableString(in);
        producerInn = parcelUtils.readNullableString(in);
        productionSerialNumber = parcelUtils.readNullableString(in);
        productionBatchNumber = parcelUtils.readNullableString(in);
        factorySerialNumber = parcelUtils.readNullableString(in);
        isFromOnlineServer = in.readByte() != 0;
        duration = parcelUtils.readNullableLong(in);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        ParcelUtils parcelUtils = ParcelUtils.getInstance();
        dest.writeString(cis);
        parcelUtils.writeNullableBoolean(dest, found);
        parcelUtils.writeNullableBoolean(dest, valid);
        parcelUtils.writeNullableString(dest, printView);
        parcelUtils.writeNullableString(dest, gtin);
        parcelUtils.writeNullableBoolean(dest, isGreyGtin);
        parcelUtils.writeNullableStringList(dest, groupId);
        parcelUtils.writeNullableBoolean(dest, verified);
        parcelUtils.writeNullableBoolean(dest, realizable);
        parcelUtils.writeNullableBoolean(dest, utilised);
        parcelUtils.writeNullableString(dest, expireDate);
        parcelUtils.writeNullableStringMap(dest, variableExpirations);
        parcelUtils.writeNullableString(dest, productionDate);
        parcelUtils.writeNullableLong(dest, productWeight);
        parcelUtils.writeNullableString(dest, prVetDocument);
        parcelUtils.writeNullableBoolean(dest, isOwner);
        parcelUtils.writeNullableBoolean(dest, isBlocked);
        parcelUtils.writeNullableStringList(dest, ogvs);
        parcelUtils.writeNullableString(dest, message);
        parcelUtils.writeNullableInt(dest, errorCode);
        parcelUtils.writeNullableBoolean(dest, isTracking);
        parcelUtils.writeNullableBoolean(dest, sold);
        parcelUtils.writeNullableInt(dest, eliminationState);
        parcelUtils.writeNullableInt(dest, mrp);
        parcelUtils.writeNullableInt(dest, smp);
        parcelUtils.writeNullableBoolean(dest, grayZone);
        parcelUtils.writeNullableInt(dest, innerUnitCount);
        parcelUtils.writeNullableInt(dest, soldUnitCount);
        parcelUtils.writeNullableString(dest, packageType);
        parcelUtils.writeNullableInt(dest, packageQuantity);
        parcelUtils.writeNullableString(dest, parent);
        parcelUtils.writeNullableString(dest, producerInn);
        parcelUtils.writeNullableString(dest, productionSerialNumber);
        parcelUtils.writeNullableString(dest, productionBatchNumber);
        parcelUtils.writeNullableString(dest, factorySerialNumber);
        dest.writeByte((byte) (isFromOnlineServer ? 1 : 0));
        parcelUtils.writeNullableLong(dest, duration);
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

    @NonNull
    public String getCis() {
        return cis;
    }

    @Nullable
    public Boolean getFound() {
        return found;
    }

    @Nullable
    public Boolean getValid() {
        return valid;
    }

    @Nullable
    public String getPrintView() {
        return printView;
    }

    @Nullable
    public String getGtin() {
        return gtin;
    }

    @Nullable
    public Boolean getGreyGtin() {
        return isGreyGtin;
    }

    @Nullable
    public List<String> getGroupId() {
        return groupId;
    }

    @Nullable
    public Boolean getVerified() {
        return verified;
    }

    @Nullable
    public Boolean getRealizable() {
        return realizable;
    }

    @Nullable
    public Boolean getUtilised() {
        return utilised;
    }

    @Nullable
    public String getExpireDate() {
        return expireDate;
    }

    @Nullable
    public Map<String, String> getVariableExpirations() {
        return variableExpirations;
    }

    @Nullable
    public String getProductionDate() {
        return productionDate;
    }

    @Nullable
    public Long getProductWeight() {
        return productWeight;
    }

    @Nullable
    public String getPrVetDocument() {
        return prVetDocument;
    }

    @Nullable
    public Boolean getOwner() {
        return isOwner;
    }

    @Nullable
    public Boolean getBlocked() {
        return isBlocked;
    }

    @Nullable
    public List<String> getOgvs() {
        return ogvs;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }

    @Nullable
    public Boolean getTracking() {
        return isTracking;
    }

    @Nullable
    public Boolean getSold() {
        return sold;
    }

    @Nullable
    public Integer getEliminationState() {
        return eliminationState;
    }

    @Nullable
    public Integer getMrp() {
        return mrp;
    }

    @Nullable
    public Integer getSmp() {
        return smp;
    }

    @Nullable
    public Boolean getGrayZone() {
        return grayZone;
    }

    @Nullable
    public Integer getInnerUnitCount() {
        return innerUnitCount;
    }

    @Nullable
    public Integer getSoldUnitCount() {
        return soldUnitCount;
    }

    @Nullable
    public String getPackageType() {
        return packageType;
    }

    @Nullable
    public Integer getPackageQuantity() {
        return packageQuantity;
    }

    @Nullable
    public String getParent() {
        return parent;
    }

    @Nullable
    public String getProducerInn() {
        return producerInn;
    }
    @Nullable
    public String getProductionSerialNumber() {
        return productionSerialNumber;
    }

    @Nullable
    public String getProductionBatchNumber() {
        return productionBatchNumber;
    }

    @Nullable
    public String getFactorySerialNumber() {
        return factorySerialNumber;
    }

    public boolean isFromOnlineServer() {
        return isFromOnlineServer;
    }

    public boolean isFromOfflineServer() {
        return !isFromOnlineServer;
    }

    @Nullable
    public Long getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MarkingCodeInfo)) return false;

        MarkingCodeInfo that = (MarkingCodeInfo) o;
        return isFromOnlineServer == that.isFromOnlineServer && cis.equals(that.cis) && Objects.equals(found, that.found) && Objects.equals(valid, that.valid) && Objects.equals(printView, that.printView) && Objects.equals(gtin, that.gtin) && Objects.equals(isGreyGtin, that.isGreyGtin) && Objects.equals(groupId, that.groupId) && Objects.equals(verified, that.verified) && Objects.equals(realizable, that.realizable) && Objects.equals(utilised, that.utilised) && Objects.equals(expireDate, that.expireDate) && Objects.equals(variableExpirations, that.variableExpirations) && Objects.equals(productionDate, that.productionDate) && Objects.equals(productWeight, that.productWeight) && Objects.equals(prVetDocument, that.prVetDocument) && Objects.equals(isOwner, that.isOwner) && Objects.equals(isBlocked, that.isBlocked) && Objects.equals(ogvs, that.ogvs) && Objects.equals(message, that.message) && Objects.equals(errorCode, that.errorCode) && Objects.equals(isTracking, that.isTracking) && Objects.equals(sold, that.sold) && Objects.equals(eliminationState, that.eliminationState) && Objects.equals(mrp, that.mrp) && Objects.equals(smp, that.smp) && Objects.equals(grayZone, that.grayZone) && Objects.equals(innerUnitCount, that.innerUnitCount) && Objects.equals(soldUnitCount, that.soldUnitCount) && Objects.equals(packageType, that.packageType) && Objects.equals(packageQuantity, that.packageQuantity) && Objects.equals(parent, that.parent) && Objects.equals(producerInn, that.producerInn) && Objects.equals(productionSerialNumber, that.productionSerialNumber) && Objects.equals(productionBatchNumber, that.productionBatchNumber) && Objects.equals(factorySerialNumber, that.factorySerialNumber) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        int result = cis.hashCode();
        result = 31 * result + Objects.hashCode(found);
        result = 31 * result + Objects.hashCode(valid);
        result = 31 * result + Objects.hashCode(printView);
        result = 31 * result + Objects.hashCode(gtin);
        result = 31 * result + Objects.hashCode(isGreyGtin);
        result = 31 * result + Objects.hashCode(groupId);
        result = 31 * result + Objects.hashCode(verified);
        result = 31 * result + Objects.hashCode(realizable);
        result = 31 * result + Objects.hashCode(utilised);
        result = 31 * result + Objects.hashCode(expireDate);
        result = 31 * result + Objects.hashCode(variableExpirations);
        result = 31 * result + Objects.hashCode(productionDate);
        result = 31 * result + Objects.hashCode(productWeight);
        result = 31 * result + Objects.hashCode(prVetDocument);
        result = 31 * result + Objects.hashCode(isOwner);
        result = 31 * result + Objects.hashCode(isBlocked);
        result = 31 * result + Objects.hashCode(ogvs);
        result = 31 * result + Objects.hashCode(message);
        result = 31 * result + Objects.hashCode(errorCode);
        result = 31 * result + Objects.hashCode(isTracking);
        result = 31 * result + Objects.hashCode(sold);
        result = 31 * result + Objects.hashCode(eliminationState);
        result = 31 * result + Objects.hashCode(mrp);
        result = 31 * result + Objects.hashCode(smp);
        result = 31 * result + Objects.hashCode(grayZone);
        result = 31 * result + Objects.hashCode(innerUnitCount);
        result = 31 * result + Objects.hashCode(soldUnitCount);
        result = 31 * result + Objects.hashCode(packageType);
        result = 31 * result + Objects.hashCode(packageQuantity);
        result = 31 * result + Objects.hashCode(parent);
        result = 31 * result + Objects.hashCode(producerInn);
        result = 31 * result + Objects.hashCode(productionSerialNumber);
        result = 31 * result + Objects.hashCode(productionBatchNumber);
        result = 31 * result + Objects.hashCode(factorySerialNumber);
        result = 31 * result + Boolean.hashCode(isFromOnlineServer);
        result = 31 * result + Objects.hashCode(duration);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "MarkingCodeInfo{" +
                "cis='" + cis + '\'' +
                ", found=" + found +
                ", valid=" + valid +
                ", printView='" + printView + '\'' +
                ", gtin='" + gtin + '\'' +
                ", isGreyGtin=" + isGreyGtin +
                ", groupId=" + groupId +
                ", verified=" + verified +
                ", realizable=" + realizable +
                ", utilised=" + utilised +
                ", expireDate='" + expireDate + '\'' +
                ", variableExpirations=" + variableExpirations +
                ", productionDate='" + productionDate + '\'' +
                ", productWeight=" + productWeight +
                ", prVetDocument='" + prVetDocument + '\'' +
                ", isOwner=" + isOwner +
                ", isBlocked=" + isBlocked +
                ", ogvs=" + ogvs +
                ", message='" + message + '\'' +
                ", errorCode=" + errorCode +
                ", isTracking=" + isTracking +
                ", sold=" + sold +
                ", eliminationState=" + eliminationState +
                ", mrp=" + mrp +
                ", smp=" + smp +
                ", grayZone=" + grayZone +
                ", innerUnitCount=" + innerUnitCount +
                ", soldUnitCount=" + soldUnitCount +
                ", packageType='" + packageType + '\'' +
                ", packageQuantity=" + packageQuantity +
                ", parent='" + parent + '\'' +
                ", producerInn='" + producerInn + '\'' +
                ", productionSerialNumber='" + productionSerialNumber + '\'' +
                ", productionBatchNumber='" + productionBatchNumber + '\'' +
                ", factorySerialNumber='" + factorySerialNumber + '\'' +
                ", isFromOnlineServer=" + isFromOnlineServer +
                ", duration=" + duration +
                '}';
    }

    public static final class Builder {
        private final String cis;
        private Boolean found;
        private Boolean valid;
        private String printView;
        private String gtin;
        private Boolean isGreyGtin;
        private List<String> groupId;
        private Boolean verified;
        private Boolean realizable;
        private Boolean utilised;
        private String expireDate;
        private Map<String, String> variableExpirations;
        private String productionDate;
        private Long productWeight;
        private String prVetDocument;
        private Boolean isOwner;
        private Boolean isBlocked;
        private List<String> ogvs;
        private String message;
        private Integer errorCode;
        private Boolean isTracking;
        private Boolean sold;
        private Integer eliminationState;
        private Integer mrp;
        private Integer smp;
        private Boolean grayZone;
        private Integer innerUnitCount;
        private Integer soldUnitCount;
        private String packageType;
        private Integer packageQuantity;
        private String parent;
        private String producerInn;
        private String productionSerialNumber;
        private String productionBatchNumber;
        private String factorySerialNumber;
        private boolean isFromOnlineServer;
        private Long duration;

        public Builder(@NonNull String cis) {
            Objects.requireNonNull(cis);
            this.cis = cis;
        }

        public Builder setFound(Boolean found) {
            Objects.requireNonNull(found);
            this.found = found;
            return this;
        }

        public Builder setValid(Boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder setPrintView(String printView) {
            this.printView = printView;
            return this;
        }

        public Builder setGtin(String gtin) {
            this.gtin = gtin;
            return this;
        }

        public Builder setGreyGtin(Boolean isGreyGtin) {
            this.isGreyGtin = isGreyGtin;
            return this;
        }

        public Builder setGroupId(List<String> groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder setVerified(Boolean verified) {
            this.verified = verified;
            return this;
        }

        public Builder setRealizable(Boolean realizable) {
            this.realizable = realizable;
            return this;
        }

        public Builder setUtilised(Boolean utilised) {
            this.utilised = utilised;
            return this;
        }

        public Builder setExpireDate(String expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public Builder setVariableExpirations(Map<String, String> variableExpirations) {
            this.variableExpirations = variableExpirations;
            return this;
        }

        public Builder setProductionDate(String productionDate) {
            this.productionDate = productionDate;
            return this;
        }

        public Builder setProductWeight(Long productWeight) {
            this.productWeight = productWeight;
            return this;
        }

        public Builder setPrVetDocument(String prVetDocument) {
            this.prVetDocument = prVetDocument;
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

        public Builder setOgvs(List<String> ogvs) {
            this.ogvs = ogvs;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder setTracking(Boolean tracking) {
            this.isTracking = tracking;
            return this;
        }

        public Builder setSold(Boolean sold) {
            this.sold = sold;
            return this;
        }

        public Builder setEliminationState(Integer eliminationState) {
            this.eliminationState = eliminationState;
            return this;
        }

        public Builder setMrp(Integer mrp) {
            this.mrp = mrp;
            return this;
        }

        public Builder setSmp(Integer smp) {
            this.smp = smp;
            return this;
        }

        public Builder setGrayZone(Boolean grayZone) {
            this.grayZone = grayZone;
            return this;
        }

        public Builder setInnerUnitCount(Integer innerUnitCount) {
            this.innerUnitCount = innerUnitCount;
            return this;
        }

        public Builder setSoldUnitCount(Integer soldUnitCount) {
            this.soldUnitCount = soldUnitCount;
            return this;
        }

        public Builder setPackageType(String packageType) {
            this.packageType = packageType;
            return this;
        }

        public Builder setPackageQuantity(Integer packageQuantity) {
            this.packageQuantity = packageQuantity;
            return this;
        }

        public Builder setParent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder setProducerInn(String producerInn) {
            this.producerInn = producerInn;
            return this;
        }

        public Builder setProductionSerialNumber(String productionSerialNumber) {
            this.productionSerialNumber = productionSerialNumber;
            return this;
        }

        public Builder setProductionBatchNumber(String productionBatchNumber) {
            this.productionBatchNumber = productionBatchNumber;
            return this;
        }

        public Builder setFactorySerialNumber(String factorySerialNumber) {
            this.factorySerialNumber = factorySerialNumber;
            return this;
        }

        public Builder setFromOnline(boolean isFromOnline) {
            this.isFromOnlineServer = isFromOnline;
            return this;
        }

        public Builder setDuration(Long duration) {
            this.duration = duration;
            return this;
        }

        public MarkingCodeInfo build() {
            return new MarkingCodeInfo(
                    Objects.requireNonNull(cis),
                    found,
                    valid,
                    printView,
                    gtin,
                    isGreyGtin,
                    groupId,
                    verified,
                    realizable,
                    utilised,
                    expireDate,
                    variableExpirations,
                    productionDate,
                    productWeight,
                    prVetDocument,
                    isOwner,
                    isBlocked,
                    ogvs,
                    message,
                    errorCode,
                    isTracking,
                    sold,
                    eliminationState,
                    mrp,
                    smp,
                    grayZone,
                    innerUnitCount,
                    soldUnitCount,
                    packageType,
                    packageQuantity,
                    parent,
                    producerInn,
                    productionSerialNumber,
                    productionBatchNumber,
                    factorySerialNumber,
                    isFromOnlineServer,
                    duration
            );
        }
    }

    private static class ParcelUtils {
        private static final ParcelUtils INSTANCE = new ParcelUtils();

        private ParcelUtils() {
        }

        private static ParcelUtils getInstance() {
            return INSTANCE;
        }

        private void writeNullableString(@NonNull Parcel dest, @Nullable String value) {
            dest.writeByte((byte) (value == null ? 0 : 1));
            if (value != null) {
                dest.writeString(value);
            }
        }

        @Nullable
        private String readNullableString(@NonNull Parcel in) {
            return in.readByte() == 0 ? null : in.readString();
        }

        private void writeNullableInt(@NonNull Parcel dest, @Nullable Integer value) {
            dest.writeByte((byte) (value == null ? 0 : 1));
            if (value != null) {
                dest.writeInt(value);
            }
        }

        @Nullable
        private Integer readNullableInt(@NonNull Parcel in) {
            return in.readByte() == 0 ? null : in.readInt();
        }

        private void writeNullableLong(@NonNull Parcel dest, @Nullable Long value) {
            dest.writeByte((byte) (value == null ? 0 : 1));
            if (value != null) {
                dest.writeLong(value);
            }
        }

        @Nullable
        private Long readNullableLong(@NonNull Parcel in) {
            return in.readByte() == 0 ? null : in.readLong();
        }

        private void writeNullableBoolean(@NonNull Parcel dest, @Nullable Boolean value) {
            if (value == null) {
                dest.writeByte((byte) 2);
            } else {
                dest.writeByte((byte) (value ? 1 : 0));
            }
        }

        @Nullable
        private Boolean readNullableBoolean(@NonNull Parcel in) {
            byte b = in.readByte();
            return b == 2 ? null : b != 0;
        }

        private void writeNullableStringList(@NonNull Parcel dest, @Nullable List<String> list) {
            dest.writeByte((byte) (list == null ? 0 : 1));
            if (list != null) {
                dest.writeStringList(list);
            }
        }

        @Nullable
        private List<String> readNullableStringList(@NonNull Parcel in) {
            return in.readByte() == 0 ? null : in.createStringArrayList();
        }

        private void writeNullableStringMap(@NonNull Parcel dest, @Nullable Map<String, String> map) {
            dest.writeByte((byte) (map == null ? 0 : 1));
            if (map != null) {
                dest.writeInt(map.size());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    dest.writeString(entry.getKey());
                    dest.writeString(entry.getValue());
                }
            }
        }

        @Nullable
        private Map<String, String> readNullableStringMap(@NonNull Parcel in) {
            if (in.readByte() == 0) {
                return null;
            }
            int size = in.readInt();
            Map<String, String> map = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                String key = in.readString();
                String value = in.readString();
                map.put(key, value);
            }
            return map;
        }
    }
}