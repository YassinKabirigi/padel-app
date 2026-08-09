package be.ephec.padel_backend.dto;

public class MesStatistiquesDto {
    private long matchsJoues;
    private long reservationsAVenir;
    private long matchsPrives;
    private long matchsPublics;

    public MesStatistiquesDto(long matchsJoues, long reservationsAVenir, long matchsPrives, long matchsPublics) {
        this.matchsJoues = matchsJoues;
        this.reservationsAVenir = reservationsAVenir;
        this.matchsPrives = matchsPrives;
        this.matchsPublics = matchsPublics;
    }

    public long getMatchsJoues() { return matchsJoues; }
    public long getReservationsAVenir() { return reservationsAVenir; }
    public long getMatchsPrives() { return matchsPrives; }
    public long getMatchsPublics() { return matchsPublics; }
}