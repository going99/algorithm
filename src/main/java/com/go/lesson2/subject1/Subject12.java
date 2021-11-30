package com.go.lesson2.subject1;

import java.util.*;

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
 * 2、采用桶排序实现对视频按照点赞数（PraiseNum）降序排列，方法名：VideoBucketSort；
 * )
 * 注意：请认真分析需求，注意边界条件，注意指定的数据结构和方法名称)
 *
 * @author go
 */
public class Subject12 {

    /**
     * 视频数 50000
     */
    private static final int VIDEO_NUM = 50000;
    private final static int PER_NUM = 10000000;

    private static final int BUCKET_NUM = PER_NUM / VIDEO_NUM + 1;
    private static ArrayList<LinkedList<Video>> buckets = new ArrayList<>();

    public static void main(String[] args) {
        initBucket();
        for (int i = 1; i <= VIDEO_NUM; i++) {
            fillData(i);
        }
        List<Video> sortList = new ArrayList<>(VIDEO_NUM);
        for (int i = buckets.size() - 1; i >= 0; i--) {
            LinkedList<Video> videos = buckets.get(i);
            if (null != videos && videos.size() > 0) {
                sortList.addAll(videos);
            }
        }
        System.out.println("sortList = " + sortList);
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
        int praiseNum = new Random().nextInt(PER_NUM + 1);
        int bukIdx = praiseNum / VIDEO_NUM;
        LinkedList<Video> list = buckets.get(bukIdx);
        insertSort(list, new Video(seqNo, "vn_" + seqNo, praiseNum));
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
            if (data.getPraiseNum() >= it.next().getPraiseNum()) {
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

}
