package com.go.lesson3;

import cn.hutool.core.util.ObjectUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2021/10/24
 * Description(
 * 68宠物APP中的训练视频中，视频(Video)具有几个重要的属性：序号SeqNo（int类型），名称Name，点赞数(PraiseNum)等，视频呈现规则如下：
 * 1、系统中训练视频的总数为5万个（视频的SeqNo从1到50000）；
 * 2、视频最多被点赞次数为1000万（即用户量<=1000万，一个用户重复观看只算一次）；
 * 3、视频呈现时排序规则按照点赞数(PraiseNum)降序排列；
 * 4、用户浏览时每页显示10个视频，当页面滑到底部时，加载下面的10个视频；
 * 5、用户点赞一次则点赞数(PraiseNum)加一。
 * <p>
 * 请写算法完成以下题目：
 * 1、按照空间换时间的思想，在内存中定义一个按点赞数排序的数据结构，存储排序结果，名字为PraiseSortResult；
 * 2、采用桶排序实现对视频按照点赞数（PraiseNum）降序排列，方法名：VideoBucketSort；
 * 3、当视频点赞次数加一时，调整PraiseSortResult中的数据，使得该结构中的数据还是按照点赞数(PraiseNum)降序排列，该操作方法名为：PraiseVideo(int seqNo)，seqNo为视频序号；
 * )
 * 注意：请认真分析需求，注意边界条件，注意指定的数据结构和方法名称
 *
 * @author go
 */
public class Subject1 {

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    /**
     * 视频数 50000
     */
    private static final int VIDEO_NUM = 50000;

    private static final int BUCKET_NUM = 10000000 / VIDEO_NUM + 1;
    private static final int FILE_LINE_LENGTH = 50000;
    private static ArrayList<LinkedList<Video>> buckets = new ArrayList<>();

    private static String getFilePath(int idx) {
        if (idx <= 25000) {
            return "D:/lesson2/subject1/";
        } else {
            return "E:/lesson2/subject1/";
        }
    }

    public static void main(String[] args) {
        //前置造数据来分析处理（虚拟数据源）
        initData();
        System.out.println("over............... ");
        //initBucket();
        //for (int i = 0; i < VIDEO_NUM; i++) {
        //    int seqNo = i;
        //    fixedThreadPool.execute(() -> fillData(seqNo));
        //}
    }

    /**
     * 构建mock初始数据源
     *
     * @return
     */
    private static List<Video> initData() {
        List<Video> list = new ArrayList<>(VIDEO_NUM);
        for (int i = 47638; i <= VIDEO_NUM; i++) {
            int ran = new Random().nextInt(10000000);
            System.out.println("ran = " + ran + " ;seqNo = " + i);
            list.add(new Video(i, "vn_" + i, ran));
            boolean[] bytes = new boolean[10000000];
            int nowNum = 0;
            while (nowNum < ran) {
                int temp = new Random().nextInt(10000000);
                if (!bytes[temp]) {
                    bytes[temp] = true;
                    nowNum++;
                }
            }
            final int ic = i;
            fixedThreadPool.execute(() -> {
                storagePraise(ic, bytes, false);
            });
        }
        return list;
    }

    /**
     * 文件存储
     *
     * @param seqNo
     * @param bytes
     */
    private static void storagePraise(int seqNo, boolean[] bytes, boolean emptyFirst) {
        String filepath = getFilePath(seqNo) + seqNo + ".txt";
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
     * 组装桶数据
     *
     * @param data
     */
    private static void assembleData(List<Video> data) {
        ArrayList<LinkedList<Video>> buckets = new ArrayList<>();
        for (int i = 0; i < BUCKET_NUM; i++) {
            buckets.add(new LinkedList<Video>());
        }
        for (Video video : data) {
            int bukIdx = (video.getSeqNo() - 1) / BUCKET_NUM;
            insertSort(buckets.get(bukIdx), video);
        }
    }

    /**
     * 初始化桶-空桶
     */
    private static void initBucket() {
        for (int i = 0; i < BUCKET_NUM; i++) {
            buckets.add(new LinkedList<Video>());
        }
    }

    /**
     * 填充video数据
     *
     * @param seqNo
     */
    private static void fillData(int seqNo) {
        int praiseNum = praiseNum(seqNo);
        int bukIdx = (praiseNum - 1) / BUCKET_NUM;
        LinkedList<Video> list = buckets.get(bukIdx);
        synchronized (list) {
            insertSort(list, new Video(seqNo, "vn_" + seqNo, praiseNum));
        }
    }

    /**
     * 获取视频的点赞数
     *
     * @param seqNo
     * @return
     */
    private static int praiseNum(int seqNo) {
        String filepath = getFilePath(seqNo) + seqNo + ".txt";
        int praiseNum = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String s = br.readLine();
            String[] arr = s.split("");
            for (String tmp : arr) {
                if ("1".equals(tmp)) {
                    praiseNum++;
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
        String filepath = getFilePath(seqNo) + seqNo + ".txt";
        int praiseNum = 0;
        BufferedReader br = null;
        boolean[] bts = new boolean[10000000];
        try {
            br = new BufferedReader(new FileReader(filepath));
            String s = br.readLine();
            String[] arr = s.split("");
            int tmpIdx = 0;
            for (String tmp : arr) {
                if ("1".equals(tmp)) {
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
     * 插入排序
     *
     * @param bucket
     * @param data
     */
    private static void insertSort(List<Video> bucket, Video data) {
        ListIterator<Video> it = bucket.listIterator();
        boolean insertFlag = true;
        while (it.hasNext()) {
            if (data.getPraiseNum() <= it.next().getPraiseNum()) {
                // 把迭代器的位置偏移回上一个位置
                it.previous();
                // 把数据插入到迭代器的当前位置
                it.add(data);
                insertFlag = false;
                break;
            }
        }
        if (insertFlag) {
            // 否则把数据插入到链表末端
            bucket.add(data);
        }
    }

    /**
     * 点赞操作
     *
     * @param seqNo    视频序号
     * @param personNo 人员序号
     * @return 点赞数是否变化
     */
    private static boolean praiseVideo(int seqNo, int personNo) {
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
     * 视频点赞后的排序
     *
     * @param seqNo
     * @param personNo
     */
    private static void postPraiseSort(int seqNo, int personNo) {
        boolean add = praiseVideo(seqNo, personNo);
        if (!add) {
            return;
        }
        int praiseNum = praiseNum(seqNo);
        if (praiseNum % (BUCKET_NUM - 1) == 0) {
            //数据切换到右侧相邻桶中了

        } else {
            //还在原桶中排序

        }
    }

}
