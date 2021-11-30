package com.go.lesson5;

import java.util.List;

/**
 * Created on (由中序遍历的数组与后序遍历的数组反推出二叉树）
 * Description()
 *
 * @author go
 */
public class Subject5A {

    public static void main(String[] args) {
        TreeNode root = Subject1.assembleByArray(Subject1.arr);
        List midList = Subject4.midScanBinaryTreeByStack(root);
        List postList = Subject4.postScanBinaryTreeByStack(root);
        int size = midList.size();
        int[] postArr = Subject5.listTrans(postList);
        int[] midArr = Subject5.listToArr(midList);
        TreeNode buildRoot = reBuildBinaryTree(postArr, 0, size - 1, midArr, 0, size - 1);
        System.out.println("buildRoot = " + buildRoot);
    }

    /**
     * 根据后序遍历与中序遍历数组重建二叉树
     *
     * @param post
     * @param startPost
     * @param endPost
     * @param mid
     * @param startMid
     * @param endMid
     * @return
     */
    private static TreeNode reBuildBinaryTree(int[] post, int startPost, int endPost,
                                              int[] mid, int startMid, int endMid) {
        if (startPost > endPost || startMid > endMid) {
            return null;
        }
        TreeNode root = new TreeNode().setData(post[endPost]);
        for (int i = startMid; i <= endMid; i++) {
            if (mid[i] != post[endPost]) {
                continue;
            }
            root.setLeft(reBuildBinaryTree(post, startPost, startPost + i - startMid - 1,
                    mid, startMid, i - 1));
            root.setRight(reBuildBinaryTree(post, startPost + i - startMid, endPost - 1,
                    mid, i + 1, endMid));
            break;
        }
        return root;
    }

}
