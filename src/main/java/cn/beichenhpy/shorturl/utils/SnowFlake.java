package cn.beichenhpy.shorturl.utils;

/**
 * @author beyondfengyu
 **/
public class SnowFlake {


    /**
     * 起始的时间戳 2021-01-01 00:00:00
     */
    private final static long START_TMP = 1609430400000L;

    /**
     * 每一部分占用的位数
     * 序列号占用的位数
     * 机器标识占用的位数
     * 数据中心占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    private final static long MACHINE_BIT = 5;
    private final static long DATACENTER_BIT = 5;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIME_TMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
    /**数据中心*/
    private long datacenterId;
    /**机器标识*/
    private long machineId;
    /**序列号*/
    private long sequence = 0L;
    /**上一次时间戳*/
    private long lastTmp = -1L;

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currTmp = getNews();
        if (currTmp < lastTmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currTmp == lastTmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currTmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTmp = currTmp;

        return (currTmp - START_TMP) << TIME_TMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNews();
        while (mill <= lastTmp) {
            mill = getNews();
        }
        return mill;
    }

    private long getNews() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(1, 1);
        for (int i = 0; i < 5000; i++) {
            System.out.println(snowFlake.nextId());
        }
    }
}
