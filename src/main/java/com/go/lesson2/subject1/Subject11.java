package com.go.lesson2.subject1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * 1、按照空间换时间的思想，在内存中定义一个按点赞数排序的数据结构，存储排序结果，名字为 PraiseSortResult；
 * )
 * 注意：请认真分析需求，注意边界条件，注意指定的数据结构和方法名称)
 *
 * @author go
 */
public class Subject11 {

    private final static int VIDEO_NUM = 50000;
    private final static int PER_NUM = 10000000;

    public static void main(String[] args) {
        List<Video>[] arr = new List[PER_NUM + 1];
        for (int i = 1; i <= VIDEO_NUM; i++) {
            int praiseNum = new Random().nextInt(PER_NUM + 1);
            List<Video> videos = arr[praiseNum];
            if (null == videos) {
                videos = new ArrayList<>();
            }
            videos.add(new Video(i, "vn_" + i, praiseNum));
            arr[praiseNum] = videos;
        }
        List<Video> PraiseSortResult = new ArrayList<>(VIDEO_NUM);
        for (int i = arr.length - 1; i >= 0; i--) {
            List<Video> videos = arr[i];
            if (null != videos && videos.size() > 0) {
                PraiseSortResult.addAll(videos);
            }
        }
        System.out.println("PraiseSortResult = " + PraiseSortResult);
    }

}
