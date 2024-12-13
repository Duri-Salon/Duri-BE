package kr.com.duri.user.domain.Enum;

import java.time.DayOfWeek;

public enum Day {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    private final String day;

    // 생성자
    Day(String day) {
        this.day = day;
    }
    // 한글 요일 반환
    public String getDay() {
        return day;
    }
    // 변환
    public static Day from(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return MON;
            case TUESDAY:
                return TUE;
            case WEDNESDAY:
                return WED;
            case THURSDAY:
                return THU;
            case FRIDAY:
                return FRI;
            case SATURDAY:
                return SAT;
            case SUNDAY:
                return SUN;
            default:
                throw new IllegalArgumentException("잘못된 요일입니다.");
        }
    }
}
