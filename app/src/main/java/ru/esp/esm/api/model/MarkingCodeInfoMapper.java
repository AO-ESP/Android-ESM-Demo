package ru.esp.esm.api.model;

import androidx.annotation.NonNull;

final class MarkingCodeInfoMapper {

    /**
     * Маппинг офлайн ответа
     */
    static MarkingCodeInfo fillOfflineWithDefaults(@NonNull MarkingCodeInfo current) {
        if (current.isFromOfflineServer()) {
            MarkingCodeInfo.Builder builder = new MarkingCodeInfo.Builder(current.getCis());
            if (current.getFound() != null) builder.setFound(current.getFound());
            if (current.getValid() != null) builder.setValid(current.getValid());
            if (current.getPrintView() != null) builder.setPrintView(current.getPrintView());
            if (current.getGtin() != null) builder.setGtin(current.getGtin());
            if (current.getGreyGtin() != null) builder.setGreyGtin(current.getGreyGtin());
            if (current.getGroupId() != null) builder.setGroupId(current.getGroupId());
            if (current.getVerified() != null) builder.setVerified(current.getVerified());
            if (current.getRealizable() != null) builder.setRealizable(current.getRealizable());
            if (current.getUtilised() != null) builder.setUtilised(current.getUtilised());
            if (current.getExpireDate() != null) builder.setExpireDate(current.getExpireDate());
            if (current.getVariableExpirations() != null)
                builder.setVariableExpirations(current.getVariableExpirations());
            if (current.getProductionDate() != null)
                builder.setProductionDate(current.getProductionDate());
            if (current.getProductWeight() != null)
                builder.setProductWeight(current.getProductWeight());
            if (current.getPrVetDocument() != null)
                builder.setPrVetDocument(current.getPrVetDocument());
            if (current.getOwner() != null) builder.setOwner(current.getOwner());
            if (current.getBlocked() != null) builder.setBlocked(current.getBlocked());
            if (current.getOgvs() != null) builder.setOgvs(current.getOgvs());
            if (current.getMessage() != null) builder.setMessage(current.getMessage());
            if (current.getErrorCode() != null) builder.setErrorCode(current.getErrorCode());
            if (current.getTracking() != null) builder.setTracking(current.getTracking());
            if (current.getSold() != null) builder.setSold(current.getSold());
            if (current.getEliminationState() != null)
                builder.setEliminationState(current.getEliminationState());
            if (current.getMrp() != null) builder.setMrp(current.getMrp());
            if (current.getSmp() != null) builder.setSmp(current.getSmp());
            if (current.getGrayZone() != null) builder.setGrayZone(current.getGrayZone());
            if (current.getInnerUnitCount() != null)
                builder.setInnerUnitCount(current.getInnerUnitCount());
            if (current.getSoldUnitCount() != null)
                builder.setSoldUnitCount(current.getSoldUnitCount());
            if (current.getPackageType() != null) builder.setPackageType(current.getPackageType());
            if (current.getPackageQuantity() != null)
                builder.setPackageQuantity(current.getPackageQuantity());
            if (current.getParent() != null) builder.setParent(current.getParent());
            if (current.getProducerInn() != null) builder.setProducerInn(current.getProducerInn());
            if (current.getProductionSerialNumber() != null)
                builder.setProductionSerialNumber(current.getProductionSerialNumber());
            if (current.getProductionBatchNumber() != null)
                builder.setProductionBatchNumber(current.getProductionBatchNumber());
            if (current.getFactorySerialNumber() != null)
                builder.setFactorySerialNumber(current.getFactorySerialNumber());
            builder.setFromOnline(false);
            if (current.getDuration() != null) builder.setDuration(current.getDuration());
            return builder.build();
        } else return current;
    }
}
