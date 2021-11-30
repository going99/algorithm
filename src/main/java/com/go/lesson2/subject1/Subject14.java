package com.go.lesson2.subject1;

import cn.hutool.core.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2021/10/27
 * Description()
 *
 * @author go
 */
public class Subject14 {

    private static final int VIDEO_NUM = 50000;
    private final static int PER_NUM = 10000000;
    private static Map<String, String> countMap = new ConcurrentHashMap<>(VIDEO_NUM);
    private static final int BUCKET_NUM = PER_NUM / VIDEO_NUM + 1;
    private static ArrayList<LinkedList<Video>> buckets = new ArrayList<>();
    private final static String countFile = "D:/lesson2/count.txt";

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

    public static void main(String[] args) {
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
        postPraiseSort(midList.get(200), 1);
        //测试边缘数据的排序
//        if (!edgeList.isEmpty()) {
//            postPraiseSort(edgeList.get(0), 13);
//        } else {
//            System.out.println("edgeList = " + edgeList);
//        }
        System.out.println("work finish!!!");
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
     * 视频点赞后的排序(如果并发点赞，需要加锁控制-锁定两个桶的数据)
     *
     * @param seqNo
     * @param personNo
     */
    private static void postPraiseSort(int seqNo, int personNo) {
        int praiseNum = Integer.parseInt(countMap.get(String.valueOf(seqNo))) + 1;
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
