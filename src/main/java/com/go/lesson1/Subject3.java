package com.go.lesson1;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Created on 2021/10/1
 * Description(3、给定有两个日期date1,date2，求该两个日期之间的天数。
 * （1）日期格式为YYYYMMDD；
 * （2）如果两个日期是连续的，我们固定其天数为两天；
 * （3）闰年的2月份有29天，不是闰年的2月份为28天；
 * （4）注意闰年的判断规则：当年数不能被100整除但是能被4整除时为闰年，或者其能被400整除也是闰年。)
 *
 * @author go Yan
 */
public class Subject3 {

    private final static int TWELVE = 12;
    private final static int ONE = 1;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;
    private final static int SIX = 6;
    private final static int SEVEN = 7;
    private final static int EIGHT = 8;
    private final static int NINE = 9;
    private final static int TEN = 10;
    private final static int ELEVEN = 11;
    private final static int FOUR_HUNDRED = 400;
    private final static int HUNDRED = 100;

    private static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    private static int differentDays(Date date1, Date date2) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            throw new RuntimeException("日期不能不空");
        }
        Long until = date2LocalDate(date1).until(date2LocalDate(date2), ChronoUnit.DAYS);
        return until.intValue();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999999999; i++) {
            int year1 = 1900 + new Random().nextInt(122);
            int year2 = 1900 + new Random().nextInt(122);
            int month1 = 1 + new Random().nextInt(12);
            int month2 = 1 + new Random().nextInt(12);
            int day2 = 1;
            if (month2 == 2) {
                if (year2 % FOUR_HUNDRED == 0 || (year2 % HUNDRED != 0 && year2 % FOUR == 0)) {
                    day2 += new Random().nextInt(29);
                } else {
                    day2 += new Random().nextInt(28);
                }
            } else {
                if (FOUR == month2 || SIX == month2 || NINE == month2 || ELEVEN == month2) {
                    day2 += new Random().nextInt(30);
                } else {
                    day2 += new Random().nextInt(31);
                }
            }
            int day1 = 1;
            if (month1 == 2) {
                if (year1 % FOUR_HUNDRED == 0 || (year1 % HUNDRED != 0 && year1 % FOUR == 0)) {
                    day1 += new Random().nextInt(29);
                } else {
                    day1 += new Random().nextInt(28);
                }
            } else {
                if (FOUR == month1 || SIX == month1 || NINE == month1 || ELEVEN == month1) {
                    day1 = new Random().nextInt(30);
                } else {
                    day1 = new Random().nextInt(31);
                }
            }
            int date1 = year1 * 10000 + month1 * 100 + day1;
            int date2 = year2 * 10000 + month2 * 100 + day2;
            if (date1 > date2) {
                continue;
            }
            int days = days(date1, date2);
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                int days1 = differentDays(format.parse(date1 + ""), format.parse(date2 + ""));
                if (days != days1) {
                    System.out.println("i==" + i + " myself ...days = " + (days+1));
                    System.out.println("i==" + i + " their ... days1 = " + (days1+1));
                    System.err.println("this result is not same,need check it idx= " + i + " ..date1= " + date1 + " ..date2= " + date2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int days(int date1, int date2) {
        if (date2 < date1) {
            return -1;
        } else if (date1 == date2) {
            return 0;
        }
        int[] arr = betweenMonth(date1, date2);
        int res = arr[0] * 28 + 29 * arr[1] + 30 * arr[2] + 31 * arr[3] + arr[4];
        return res;
    }

    /**
     * arr[0]表示28天次数，arr[1]表示29天次数，arr[2]表示30天次数，arr[3]表示31天次数,arr[4]表示余下的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    private static int[] betweenMonth(int date1, int date2) {
        int[] arr = new int[5];
        if (isSameMonth(date1, date2)) {
            arr[4] = date2 % 100 - date1 % 100;
            return arr;
        }
        while (true) {
            if (date1 >= date2) {
                break;
            }
            arr[getMaxDays(date1)]++;
            date1 = addOneMonth(date1);
            if (isSameMonth(date1, date2)) {
                int remain2 = date2 % 100;
                int remain1 = date1 % 100;
                arr[4] += remain2 - remain1;
                break;
            }
        }
        return arr;
    }

    /**
     * 加一个月后的日期
     *
     * @param date
     * @return
     */
    private static int addOneMonth(int date) {
        int divide = date / 100;
        int remainder = date % 100;
        int curMonth = divide % 100;
        if (curMonth < TWELVE) {
            return 100 * (divide + 1) + remainder;
        } else {
            int year = divide / 100;
            year += 1;
            return year * 10000 + 100 + remainder;
        }
    }

    private static boolean isSameMonth(int date1, int date2) {
        return (date1 / 100) == (date2 / 100);
    }

    /**
     * 获取日期所在月的最大天数
     *
     * @param date
     * @return
     */
    private static int getMaxDays(int date) {
        int divide = date / 100;
        int month = divide % 100;
        if (ONE == month || THREE == month || FIVE == month || SEVEN == month || EIGHT == month || TEN == month || TWELVE == month) {
            return 3;
        }
        if (FOUR == month || SIX == month || NINE == month || ELEVEN == month) {
            return 2;
        }
        int year = divide / 100;
        if (year % FOUR_HUNDRED == 0) {
            return 1;
        }
        if (year % FOUR == 0 && year % HUNDRED != 0) {
            return 1;
        }
        return 0;
    }

}
