package fr.the__glacier.gcore.util;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
    public static Calendar calendar = Calendar.getInstance();
    public static long getRawTime(){
        return System.currentTimeMillis();
    }
    public String getDateFormated(){
        return getDateFormated(System.currentTimeMillis());
    }
    public static String getDateFormatedWithoutHMS(Long time){
        return String.format(getDayOfMonthFormated(time) + "-" + getMonthFormated(time) + "-" + getYear(time));
    }
    public static String getDateFormated(Long time){
        return String.format(getDayOfMonthFormated(time) + "-" + getMonthFormated(time) + "-" + getYear(time) + " " + getHourOfDayFormated(time) + ":" + getMinuteFormated(time) + ":" + getSecondFormated(time));
    }

    public static int getYear(Long time){
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR);
    }

    public static String getMonthFormated(Long time){
        return String.format("%02d", getMonth(time));
    }
    public static int getMonth(Long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static String getDayOfMonthFormated(Long time) {
        return String.format("%02d", getDayOfMonth(time));
    }
    public static int getDayOfMonth(Long time){
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getHourOfDayFormated(Long time) {
        return String.format("%02d", getHourOfDay(time));
    }
    public static int getHourOfDay(Long time){
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getMinuteFormated(Long time) {
        return String.format("%02d", getMinute(time));
    }
    public static int getMinute(Long time){
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MINUTE);
    }

    public static String getSecondFormated(Long time) {
        return String.format("%02d", getSecond(time));
    }
    public static int getSecond(Long time){
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.SECOND);
    }


    public static String getDurationFormated(Duration duration, String day, String hour, String minute, String second){
        String d = getDurationDayFormated(duration) + day;
        String h = getDurationHourFormated(duration) + hour;
        String m = getDurationMinuteFormated(duration) + minute;
        String s = getDurationSecondFormated(duration) + second;
        if (!d.equals("00" + day)){
            return d + h + m + s;
        } else if (!h.equals("00" + hour)){
            return h + m + s;
        } else if (!m.equals("00" + minute)){
            return m + s;
        } else return s;
    }
    public static String getDurationFormated(Duration duration){
        return getDurationFormated(duration, " days, ", " hours, ", " minutes, ", " seconds.");
    }
    public static String getDurationFormated(Long milliseconds){
        return getDurationFormated(Duration.ofMillis(milliseconds), " days, ", " hours, ", " minutes, ", " seconds");
    }

    public static String getDurationDayFormated(Duration duration) {
        return String.format("%02d", getDurationDay(duration));
    }
    public static long getDurationDay(Duration duration){
        return duration.toDays();
    }

    public static String getDurationHourFormated(Duration duration) {
        return String.format("%02d", getDurationHour(duration));
    }
    public static int getDurationHour(Duration duration){
        return duration.toHoursPart();
    }

    public static String getDurationMinuteFormated(Duration duration) {
        return String.format("%02d", getDurationMinute(duration));
    }
    public static int getDurationMinute(Duration duration){
        return duration.toMinutesPart();
    }

    public static String getDurationSecondFormated(Duration duration) {
        return String.format("%02d", getDurationSecond(duration));
    }
    public static long getDurationSecond(Duration duration){
        return duration.toSecondsPart();
    }

    public static class CooldownManager {
        public Map<Object, Long> cooldownMap = new HashMap<>();
        public boolean isOnCooldown(@NotNull Object o){
            if (o instanceof Entity){
                return isOnCooldown(((Entity) o).getUniqueId());
            }
            if (cooldownMap.containsKey(o)){
                if (cooldownMap.get(o) > System.currentTimeMillis()){
                    return true;
                } else {
                    cooldownMap.remove(o);
                    return false;
                }
            }
            return false;
        }
        public long timeUntilEndCooldown(Object o){
            if (o instanceof Entity e){
                return timeUntilEndCooldown(e.getUniqueId());
            }
            if (!cooldownMap.containsKey(o)) return 0L;
            return cooldownMap.get(o) - System.currentTimeMillis();
        }
        public void addCooldown(Object o, long duration){
            if (o instanceof Entity e) {
                addCooldown(e.getUniqueId(), duration);
                return;
            }
            if (isOnCooldown(o)) return;
            cooldownMap.put(o, duration + System.currentTimeMillis());
        }
    }
}
