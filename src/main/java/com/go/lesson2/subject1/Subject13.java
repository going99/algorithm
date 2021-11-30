package com.go.lesson2.subject1;

import cn.hutool.core.util.ObjectUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created on 2021/10/26
 * Description(
 * 68宠物APP中的训练视频中，视频(Video)具有几个重要的属性：序号SeqNo（int类型），名称Name，点赞数(PraiseNum)等，视频呈现规则如下：
 * 1、系统中训练视频的总数为5万个（视频的SeqNo从1到50000）；
 * 2、视频最多被点赞次数为1000万（即用户量<=1000万，一个用户重复观看只算一次）；
 * 3、视频呈现时排序规则按照点赞数(PraiseNum)降序排列；
 * 4、用户浏览时每页显示10个视频，当页面滑到底部时，加载下面的10个视频；
 * 5、用户点赞一次则点赞数(PraiseNum)加一。
 * <p>
 * 请写算法完成以下题目：
 * 3、当视频点赞次数加一时，调整PraiseSortResult中的数据，使得该结构中的数据还是按照点赞数(PraiseNum)降序排列，该操作方法名为：PraiseVideo(int seqNo)，seqNo为视频序号；
 * )
 * 注意：请认真分析需求，注意边界条件，注意指定的数据结构和方法名称)
 *
 * @author go
 */
public class Subject13 {

    /**
     * 视频数 50000
     */
    private static final int VIDEO_NUM = 50000;
    private final static int PER_NUM = 10000000;

    private static final int BUCKET_NUM = PER_NUM / VIDEO_NUM + 1;
    private static final int FILE_LINE_LENGTH = 50000;
    private static ArrayList<LinkedList<Video>> buckets = new ArrayList<>();
    private static CountDownLatch latch = new CountDownLatch(VIDEO_NUM);

    private static Map<String, String> countMap = new ConcurrentHashMap<>(VIDEO_NUM);

    private static final int SORT_TIMES = 10;
    private static ExecutorService fixedThreadPool = new ThreadPoolExecutor(10, VIDEO_NUM, 1L, TimeUnit.DAYS, new LinkedBlockingDeque<>());

    private final static String countFile = "E:/lesson2/count.txt";

    private static String getFilePath(int seqNo) {
        if (seqNo <= 25000) {
            return "D:/lesson2/subject1/" + seqNo + ".txt";
        }
        return "E:/lesson2/subject1/" + seqNo + ".txt";
    }

    public static void main(String[] args) throws Exception {
        initBucket();
        for (int i = 1; i <= VIDEO_NUM; i++) {
            int seqNo = i;
            fixedThreadPool.execute(() -> fillData(seqNo));
        }
        latch.await();
        System.out.println("run at latch.await after ");
        writeCount(countMap);
        System.out.println("run finish! ");
        for (LinkedList<Video> bucket : buckets) {
            quickSort(bucket, 0, bucket.size() - 1);
        }
        for (int i = 0; i < SORT_TIMES; i++) {
            postPraiseSort(new Random().nextInt(VIDEO_NUM) + 1, new Random().nextInt(PER_NUM) + 1);
        }
    }

    /**
     * 文件存储
     *
     * @param seqNo
     * @param bytes
     */
    private static void storagePraise(int seqNo, boolean[] bytes, boolean emptyFirst) {
        String filepath = getFilePath(seqNo);
        if (emptyFirst) {
            boolean delete = new File(filepath).delete();
            System.out.println(filepath + " 的文件删除 = " + (delete ? "成功" : "失败"));
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(bytes[i] ? "1" : "0");
            if (i != 0 && (i % FILE_LINE_LENGTH == 0 || i == bytes.length - 1)) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(
                        new FileWriter(filepath, true))) {
                    bufferedWriter.write(buffer + "\n");
                    buffer = new StringBuffer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化桶-空桶
     */
    private static void initBucket() {
        for (int i = 0; i < BUCKET_NUM; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    /**
     * 填充video数据
     *
     * @param seqNo
     */
    private static void fillData(int seqNo) {
        int praiseNum = praiseNum(seqNo);
        int bukIdx = praiseNum / VIDEO_NUM;
        appendData(bukIdx, seqNo, praiseNum);
    }

    /**
     * 装载数据至桶（需同步）
     *
     * @param bukIdx
     * @param seqNo
     * @param praiseNum
     */
    private synchronized static void appendData(int bukIdx, int seqNo, int praiseNum) {
        LinkedList<Video> list = buckets.get(bukIdx);
        list.add(new Video(seqNo, "vn_" + seqNo, praiseNum));
        System.out.println("seqNo = " + seqNo + "; praiseNum = " + praiseNum);
        countMap.put(String.valueOf(seqNo), String.valueOf(praiseNum));
        latch.countDown();
    }

    /**
     * 将总数写入文件
     *
     * @param map
     */
    private static void writeCount(Map<String, String> map) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(countFile, true))) {
            for (String s : map.keySet()) {
                bufferedWriter.write(s + " " + map.get(s) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件到map
     */
    private static void readCount() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(countFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(" ");
                countMap.put(arr[0], arr[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ObjectUtil.isNotEmpty(br)) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取视频的点赞数
     *
     * @param seqNo
     * @return
     */
    private static int praiseNum(int seqNo) {
        String filepath = getFilePath(seqNo);
        int praiseNum = 0;
        if (!new File(filepath).exists()) {
            return praiseNum;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("");
                for (String tmp : arr) {
                    if (tmp.equals("1")) {
                        praiseNum++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ObjectUtil.isNotEmpty(br)) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return praiseNum;
    }

    /**
     * 视频的点赞人明细
     *
     * @param seqNo
     * @return
     */
    private static boolean[] praisePerson(int seqNo) {
        String filepath = getFilePath(seqNo);
        int praiseNum = 0;
        BufferedReader br = null;
        boolean[] bts = new boolean[10000000];
        try {
            br = new BufferedReader(new FileReader(filepath));
            String s = br.readLine();
            String[] arr = s.split("");
            int tmpIdx = 0;
            for (String tmp : arr) {
                if (tmp.equals("1")) {
                    bts[tmpIdx] = true;
                }
                tmpIdx++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ObjectUtil.isNotEmpty(br)) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bts;
    }

    /**
     * 点赞操作
     *
     * @param seqNo    视频序号
     * @param personNo 人员序号
     * @return 点赞数是否变化
     */
    private static boolean PraiseVideo(int seqNo, int personNo) {
        boolean[] person = praisePerson(seqNo);
        if (!person[personNo]) {
            //用户之前未对该视频点赞过
            person[personNo] = true;
            //数据保存到文件
            storagePraise(seqNo, person, true);
            return true;
        }
        return false;
    }

    /**
     * 视频点赞后的排序(如果并发点赞，需要加锁控制-锁定两个桶的数据)
     *
     * @param seqNo
     * @param personNo
     */
    private static void postPraiseSort(int seqNo, int personNo) {
        boolean add = PraiseVideo(seqNo, personNo);
        if (!add) {
            return;
        }
        int praiseNum = praiseNum(seqNo);
        //获取数据未点赞时的桶序号
        int preIdx = (praiseNum - 1) / VIDEO_NUM;
        //获取当前数据的桶序号
        int bukIdx = praiseNum / VIDEO_NUM;
        if (preIdx != bukIdx) {
            //数据转移到右侧相邻桶中了（桶内是倒序的，右侧的桶点赞数据大于左侧桶的点赞数）
            Video video = buckets.get(preIdx).removeLast();
            video.setPraiseNum(praiseNum);
            buckets.get(bukIdx).addFirst(video);
        } else {
            //对数据所在桶进行排序调整
            LinkedList<Video> videos = buckets.get(bukIdx);
            int idx = binarySearch(videos, 0, videos.size() - 1, praiseNum - 1);
            buckSort(videos, idx);
            //quickSort(videos, 0, videos.size() - 1);
        }
    }

    /**
     * 桶内调整一下点赞数的顺序
     *
     * @param videos
     * @param idx
     */
    private static void buckSort(LinkedList<Video> videos, int idx) {
        Video video = videos.get(idx);
        video.setPraiseNum(video.getPraiseNum() + 1);
        int change = -1;
        int len = videos.size();
        for (int i = idx + 1; i < len; i++) {
            if (videos.get(i).getPraiseNum() >= video.getPraiseNum()) {
                break;
            }
            change = i;
        }
        if (change >= 0) {
            //交换idx 与 change 位置
            videos.set(idx, videos.get(change));
            videos.set(change, video);
        }
    }

    /**
     * 二分查找点赞视频的下标
     *
     * @param list
     * @param low
     * @param high
     * @param praiseNum
     * @return
     */
    private static int binarySearch(LinkedList<Video> list, int low, int high, int praiseNum) {
        int mid = (low + high) / 2;
        if (low > high) {
            return -1;
        }
        if (list.get(mid).getPraiseNum() == praiseNum) {
            return mid;
        }
        if (list.get(mid).getPraiseNum() > praiseNum) {
            return binarySearch(list, low, mid - 1, praiseNum);
        } else {
            return binarySearch(list, mid + 1, high, praiseNum);
        }
    }

    /**
     * 快速排序
     *
     * @param list
     * @param low
     * @param high
     */
    private static void quickSort(List<Video> list, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivot = getPivot(list, low, high);
        quickSort(list, low, pivot - 1);
        quickSort(list, pivot + 1, high);
    }

    /**
     * 获取排序支点
     *
     * @param list
     * @param low
     * @param high
     * @return
     */
    private static int getPivot(List<Video> list, int low, int high) {
        Video baseVal = list.get(low);
        while (low < high) {
            while (low < high && list.get(high).getPraiseNum() >= baseVal.getPraiseNum()) {
                high--;
            }
            list.set(low, list.get(high));
            while (low < high && list.get(low).getPraiseNum() <= baseVal.getPraiseNum()) {
                low++;
            }
            list.set(high, list.get(low));
        }
        list.set(low, baseVal);
        return low;
    }
}
