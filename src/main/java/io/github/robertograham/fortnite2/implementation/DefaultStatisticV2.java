package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


final class DefaultStatisticV2 implements Statistic {

    private final long wins;
    private final long matches;
    private final long kills;
    private final long score;
    private final long deaths;
    private final long timesPlacedTop10;
    private final long timesPlacedTop25;
    private final long timesPlacedTop5;
    private final long timesPlacedTop12;
    private final long timesPlacedTop3;
    private final long timesPlacedTop6;
    private final LocalDateTime timeLastModified;
    private final long playersOutlived;
    private final long minutesPlayed;
    private final Set<RawStatisticV2> rawStatistics;

    DefaultStatisticV2(final Set<RawStatisticV2> rawStatistics) {
        final Map<String, Long> summedValuesGroupedByStatType =
            rawStatistics.stream()
                .filter((final var rawStatistic) -> !"lastmodified".equals(rawStatistic.type()))
                .collect(Collectors.groupingBy(
                    RawStatisticV2::type,
                    Collectors.summingLong(RawStatisticV2::value)
                ));
        this.rawStatistics = rawStatistics;
        this.wins = summedValuesGroupedByStatType.getOrDefault("placetop1", 0L);
        this.matches = summedValuesGroupedByStatType.getOrDefault("matchesplayed", 0L);
        this.kills = summedValuesGroupedByStatType.getOrDefault("kills", 0L);
        this.score = summedValuesGroupedByStatType.getOrDefault("score", 0L);
        this.deaths = Math.abs(matches - wins);
        this.timesPlacedTop10 = summedValuesGroupedByStatType.getOrDefault("placetop10", 0L);
        this.timesPlacedTop25 = summedValuesGroupedByStatType.getOrDefault("placetop25", 0L);
        this.timesPlacedTop5 = summedValuesGroupedByStatType.getOrDefault("placetop5", 0L);
        this.timesPlacedTop12 = summedValuesGroupedByStatType.getOrDefault("placetop12", 0L);
        this.timesPlacedTop3 = summedValuesGroupedByStatType.getOrDefault("placetop3", 0L);
        this.timesPlacedTop6 = summedValuesGroupedByStatType.getOrDefault("placetop6", 0L);
        this.timeLastModified = rawStatistics.stream()
            .filter(rawStatistic -> "lastmodified".equals(rawStatistic.type()))
            .max(Comparator.comparingLong(RawStatisticV2::value))
            .map((final var rawStatistic) ->
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(rawStatistic.value()),
                    ZoneOffset.UTC
                )
            )
            .orElse(LocalDateTime.MIN);
        this.playersOutlived = summedValuesGroupedByStatType.getOrDefault("playersoutlived", 0L);
        this.minutesPlayed = summedValuesGroupedByStatType.getOrDefault("minutesplayed", 0L);
    }

    public Set<RawStatisticV2> getRawStatistics() {
        return rawStatistics;
    }

    @Override
    public long deaths() {
        return deaths;
    }

    @Override
    public long wins() {
        return wins;
    }

    @Override
    public long matches() {
        return matches;
    }

    @Override
    public long kills() {
        return kills;
    }

    @Override
    public long score() {
        return score;
    }

    @Override
    public long timesPlacedTop10() {
        return timesPlacedTop10;
    }

    @Override
    public long timesPlacedTop25() {
        return timesPlacedTop25;
    }

    @Override
    public long timesPlacedTop5() {
        return timesPlacedTop5;
    }

    @Override
    public long timesPlacedTop12() {
        return timesPlacedTop12;
    }

    @Override
    public long timesPlacedTop3() {
        return timesPlacedTop3;
    }

    @Override
    public long timesPlacedTop6() {
        return timesPlacedTop6;
    }

    @Override
    public LocalDateTime timeLastModified() {
        return timeLastModified;
    }

    @Override
    public long playersOutlived() {
        return playersOutlived;
    }

    @Override
    public long minutesPlayed() {
        return minutesPlayed;
    }

    @Override
    public String toString() {
        return "DefaultStatisticV2{" +
            "wins=" + wins +
            ", matches=" + matches +
            ", kills=" + kills +
            ", score=" + score +
            ", timesPlacedTop10=" + timesPlacedTop10 +
            ", timesPlacedTop25=" + timesPlacedTop25 +
            ", timesPlacedTop5=" + timesPlacedTop5 +
            ", timesPlacedTop12=" + timesPlacedTop12 +
            ", timesPlacedTop3=" + timesPlacedTop3 +
            ", timesPlacedTop6=" + timesPlacedTop6 +
            ", timeLastModified=" + timeLastModified +
            ", playersOutlived=" + playersOutlived +
            ", minutesPlayed=" + minutesPlayed +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultStatisticV2))
            return false;
        final var defaultStatisticV2 = (DefaultStatisticV2) object;
        return wins == defaultStatisticV2.wins &&
            matches == defaultStatisticV2.matches &&
            kills == defaultStatisticV2.kills &&
            score == defaultStatisticV2.score &&
            timesPlacedTop10 == defaultStatisticV2.timesPlacedTop10 &&
            timesPlacedTop25 == defaultStatisticV2.timesPlacedTop25 &&
            timesPlacedTop5 == defaultStatisticV2.timesPlacedTop5 &&
            timesPlacedTop12 == defaultStatisticV2.timesPlacedTop12 &&
            timesPlacedTop3 == defaultStatisticV2.timesPlacedTop3 &&
            timesPlacedTop6 == defaultStatisticV2.timesPlacedTop6 &&
            playersOutlived == defaultStatisticV2.playersOutlived &&
            minutesPlayed == defaultStatisticV2.minutesPlayed &&
            timeLastModified.equals(defaultStatisticV2.timeLastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wins, matches, kills, score, timesPlacedTop10, timesPlacedTop25, timesPlacedTop5, timesPlacedTop12, timesPlacedTop3, timesPlacedTop6, timeLastModified, playersOutlived, minutesPlayed);
    }
}