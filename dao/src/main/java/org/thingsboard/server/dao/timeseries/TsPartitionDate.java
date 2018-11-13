package org.thingsboard.server.dao.timeseries;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

public enum TsPartitionDate {

    MINUTES("yyyy-MM-dd-HH-mm", ChronoUnit.MINUTES), HOURS("yyyy-MM-dd-HH", ChronoUnit.HOURS), DAYS("yyyy-MM-dd", ChronoUnit.DAYS), MONTHS("yyyy-MM", ChronoUnit.MONTHS), YEARS("yyyy", ChronoUnit.YEARS),INDEFINITE("",ChronoUnit.FOREVER);

    private final String pattern;
    private final transient TemporalUnit truncateUnit;
    public final static LocalDateTime EPOCH_START = LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC);

    TsPartitionDate(String pattern, TemporalUnit truncateUnit) {
        this.pattern = pattern;
        this.truncateUnit = truncateUnit;
    }


    public String getPattern() {
        return pattern;
    }

    public TemporalUnit getTruncateUnit() {
        return truncateUnit;
    }

    public LocalDateTime truncatedTo(LocalDateTime time) {
        switch (this){
            case MONTHS:
                return time.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
            case YEARS:
                return time.truncatedTo(ChronoUnit.DAYS).withDayOfYear(1);
            case INDEFINITE:
                 return EPOCH_START;
            default:
                return time.truncatedTo(truncateUnit);
        }
    }

    public static Optional<TsPartitionDate> parse(String name) {
        TsPartitionDate partition = null;
        if (name != null) {
            for (TsPartitionDate partitionDate : TsPartitionDate.values()) {
                if (partitionDate.name().equalsIgnoreCase(name)) {
                    partition = partitionDate;
                    break;
                }
            }
        }
        return Optional.of(partition);
    }
}
