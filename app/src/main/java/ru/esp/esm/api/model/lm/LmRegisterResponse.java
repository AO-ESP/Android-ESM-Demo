package ru.esp.esm.api.model.lm;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LmRegisterResponse implements Parcelable {
    @Nullable
    private final List<Result> results; // List может содержать null элементы

    public LmRegisterResponse(@Nullable List<Result> results) {
        this.results = results; // защитная копия
    }

    protected LmRegisterResponse(Parcel in) {
        // Чтение results
        if (in.readByte() == 1) {
            int size = in.readInt();
            List<Result> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (in.readByte() == 1) {
                    list.add(in.readParcelable(Result.class.getClassLoader()));
                } else {
                    list.add(null);
                }
            }
            results = list;
        } else {
            results = null;
        }
    }

    @Nullable
    public List<Result> getResults() {
        return results;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // Запись results
        if (results != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(results.size());
            for (Result item : results) {
                if (item != null) {
                    dest.writeByte((byte) 1);
                    dest.writeParcelable(item, flags);
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

    public static final Creator<LmRegisterResponse> CREATOR = new Creator<LmRegisterResponse>() {
        @Override
        public LmRegisterResponse createFromParcel(Parcel in) {
            return new LmRegisterResponse(in);
        }

        @Override
        public LmRegisterResponse[] newArray(int size) {
            return new LmRegisterResponse[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmRegisterResponse that = (LmRegisterResponse) o;

        return results != null ? results.equals(that.results) : that.results == null;
    }

    @Override
    public int hashCode() {
        return results != null ? results.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LmRegisterResponse{" +
                "results=" + results +
                '}';
    }

    /**
     * Вложенный класс Result
     */
    public static class Result implements Parcelable {
        @Nullable
        private final String cis;
        @Nullable
        private final Boolean success;

        public Result(@Nullable String cis, @Nullable Boolean success) {
            this.cis = cis;
            this.success = success;
        }

        protected Result(Parcel in) {
            // Чтение cis
            if (in.readByte() == 1) {
                cis = in.readString();
            } else {
                cis = null;
            }
            // Чтение success (Boolean nullable)
            if (in.readByte() == 1) {
                success = in.readByte() == 1;
            } else {
                success = null;
            }
        }

        @Nullable
        public String getCis() {
            return cis;
        }

        @Nullable
        public Boolean getSuccess() {
            return success;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // Запись cis
            if (cis != null) {
                dest.writeByte((byte) 1);
                dest.writeString(cis);
            } else {
                dest.writeByte((byte) 0);
            }
            // Запись success
            if (success != null) {
                dest.writeByte((byte) 1);
                dest.writeByte((byte) (success ? 1 : 0));
            } else {
                dest.writeByte((byte) 0);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Result result = (Result) o;

            if (cis != null ? !cis.equals(result.cis) : result.cis != null) return false;
            return success != null ? success.equals(result.success) : result.success == null;
        }

        @Override
        public int hashCode() {
            int result = cis != null ? cis.hashCode() : 0;
            result = 31 * result + (success != null ? success.hashCode() : 0);
            return result;
        }

        @NonNull
        @Override
        public String toString() {
            return "Result{" +
                    "cis='" + cis + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
}