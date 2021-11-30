package com.go.lesson2.subject1;

import cn.hutool.core.util.ObjectUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description: Subject15
 * Created on 2021/10/28.
 *
 * @author go
 */
public class Subject15 {
    private static final int VIDEO_NUM = 50000;
    private final static int PER_NUM = 10000000;
    private static final int FILE_LINE_LENGTH = 50000;
    private static Map<String, String> countMap = new ConcurrentHashMap<>(VIDEO_NUM);
    private static final int BUCKET_NUM = PER_NUM / VIDEO_NUM + 1;
    private static ArrayList<LinkedList<Video>> buckets = new ArrayList<>();
    private final static String countFile = "E:/lesson2/count.txt";

    private static void initBucket() {
        for (int i = 0; i < BUCKET_NUM; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    private static void fillData(int seqNo, int praiseNum) {
        int bukIdx = praiseNum / VIDEO_NUM;
        LinkedList<Video> list = buckets.get(bukIdx);
        list.add(new Video(seqNo, "vn_" + seqNo, praiseNum));
    }

    private static String getFilePath(int seqNo) {
        if (seqNo <= 25000) {
            return "D:/lesson2/subject1/" + seqNo + ".txt";
        }
        return "E:/lesson2/subject1/" + seqNo + ".txt";
    }

    /**
     * 文件存储
     *
     * @param seqNo
     * @param bytes
     */
    private static void storagePraise(int seqNo, boolean[] bytes, boolean emptyFirst) {
        String filepath = getFilePath(seqNo);
        File file = new File(filepath);
        if (emptyFirst && file.exists()) {
            boolean delete = file.delete();
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

    public static void main(String[] args) {
        scanLoss();
        prepareData();
        //run();
        //614943
        int praiseNum = praiseNum(12389);
        System.out.println("praiseNum = " + praiseNum);
    }

    /**
     * test 制造一些特别的数据来处理
     */
    private static void scanLoss() {
        readCount();
        int scanCount = 0;
        for (int i = 1; i <= VIDEO_NUM; i++) {
            String filePath = getFilePath(i);
            if (!new File(filePath).exists()) {
                countMap.put(String.valueOf(i), "0");
                scanCount++;
            }
        }
        System.out.println("scanCount = " + scanCount);
        writeCount(countMap);
    }

    /**
     * 数据准备
     */
    private static void prepareData() {
        readCount();
        int pc=0;
        for (String s : countMap.keySet()) {
            if ("0".equals(String.valueOf(countMap.get(s)))) {
                //new Random().nextInt(PER_NUM + 1);
                //VIDEO_NUM * (new Random().nextInt(200) + 1) - 1
                int praiseNum = new Random().nextInt(PER_NUM + 1);
                countMap.put(s, String.valueOf(praiseNum));
                boolean[] bytes = new boolean[PER_NUM];
                int nowNum = 0;
                while (nowNum < praiseNum) {
                    int temp = new Random().nextInt(PER_NUM);
                    if (!bytes[temp]) {
                        bytes[temp] = true;
                        nowNum++;
                    }
                }
                storagePraise(Integer.parseInt(s), bytes, true);
                pc++;
            }
        }
        System.out.println("pc = " + pc);
        writeCount(countMap);
    }

    /**
     * work
     */
    private static void run() {
        readCount();
        //数据入桶
        initBucket();
        List<Integer> edgeList = new ArrayList<>();
        List<Integer> midList = new ArrayList<>(1000);
        int count = 0;
        for (String s : countMap.keySet()) {
            int praiseNum = Integer.parseInt(countMap.get(s));
            if ((praiseNum + 1) % VIDEO_NUM == 0) {
                edgeList.add(Integer.parseInt(s));
            } else {
                if (count < 1000) {
                    count++;
                    midList.add(Integer.parseInt(s));
                }
            }
            fillData(Integer.parseInt(s), praiseNum);
        }
        for (LinkedList<Video> bucket : buckets) {
            quickSort(bucket, 0, bucket.size() - 1);
        }
        Integer midSeqNo = midList.get(200);
        int minPerNo = new Random().nextInt(PER_NUM) + 1;
        //postPraiseSort(midSeqNo, minPerNo);
        //测试边缘数据的排序
        if (!edgeList.isEmpty()) {
            Integer edgeSeqNo = edgeList.get(0);
            int edgePerNo = new Random().nextInt(PER_NUM) + 1;
            //postPraiseSort(edgeSeqNo, edgePerNo);
        } else {
            System.out.println("edgeList = " + edgeList);
        }
        System.out.println("homework finish!!!");
    }

    /**
     * 将总数写入文件
     *
     * @param map
     */
    private static void writeCount(Map<String, String> map) {
        File file = new File(countFile);
        if (file.exists()) {
            boolean delete = file.delete();
            System.out.println(countFile + " 的文件删除 = " + (delete ? "成功" : "失败"));
        }
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
            int afterPraiseNum = praiseNum(seqNo);
            countMap.put(String.valueOf(seqNo), String.valueOf(afterPraiseNum));
            return true;
        }
        return false;
    }

    private static int rightCount(boolean[] bts) {
        int count = 0;
        for (boolean bt : bts) {
            if (bt) {
                count++;
            }
        }
        return count;
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
            String s;
            int tmpIdx = 0;
            while ((s = br.readLine()) != null) {
                String[] arr = s.split("");
                for (String tmp : arr) {
                    if ("1".equals(tmp)) {
                        bts[tmpIdx] = true;
                    }
                    tmpIdx++;
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
        return bts;
    }

    /**
     * 视频点赞后的排序(如果并发点赞，需要加锁控制-锁定两个桶的数据)
     *
     * @param seqNo
     * @param personNo
     */
    private static void postPraiseSort(int seqNo, int personNo) {
        System.out.println("postPraiseSort.... seqNo = " + seqNo + "; personNo = " + personNo);
        boolean add = PraiseVideo(seqNo, personNo);
        if (!add) {
            return;
        }
        int praiseNum = praiseNum(seqNo);
        System.out.println("postPraiseSort.... seqNo = " + seqNo + "; personNo = " + personNo + "; praiseNum = " + praiseNum);
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
            if (idx > 0) {
                buckSort(videos, idx);
            }
            //quickSort(videos, 0, videos.size() - 1);
        }
        writeCount(countMap);
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
