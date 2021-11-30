package com.go.lesson5;

import java.util.List;

/**
 * Description: （在两个数组中分别有前序和中序遍历序列，试建立该二叉树）
 * 由中序遍历的数组与前序遍历的数组反推出二叉树
 * Created on 2021/11/21.
 *
 * @author go
 */
public class Subject5 {

    protected static int[] listTrans(List<TreeNode> preList) {
        int[] arr = new int[preList.size()];
        for (int i = 0; i < preList.size(); i++) {
            arr[i] = Integer.parseInt(String.valueOf(preList.get(i).getData()));
        }
        return arr;
    }

    protected static int[] listToArr(List<Integer> midList) {
        int[] arr = new int[midList.size()];
        for (int i = 0; i < midList.size(); i++) {
            arr[i] = midList.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        TreeNode root = Subject1.assembleByArray(Subject1.arr);
        List<TreeNode> preList = Subject4.preOrderTraversalWithStack(root);
        List<Integer> midList = Subject4.midScanBinaryTreeByStack(root);
        int size = preList.size();
        int[] preArr = listTrans(preList);
        int[] midArr = listToArr(midList);
        TreeNode buildRoot = reConstructBinaryTree(preArr, 0, size - 1, midArr, 0, size - 1);
        System.out.println("buildRoot = " + buildRoot);
    }

    /**
     * 根据前序遍历与中序遍历数组重建二叉树
     *
     * @param pre
     * @param startPre
     * @param endPre
     * @param mid
     * @param startMid
     * @param endMid
     * @return
     */
    private static TreeNode reConstructBinaryTree(int[] pre, int startPre, int endPre,
                                                  int[] mid, int startMid, int endMid) {
        if (startPre > endPre || startMid > endMid) {
            return null;
        }
        TreeNode root = new TreeNode().setData(pre[startPre]);
        for (int i = startMid; i <= endMid; i++) {
            if (mid[i] != pre[startPre]) {
                continue;
            }
            root.setLeft(reConstructBinaryTree(pre, startPre + 1, startPre + i - startMid,
                    mid, startMid, i - 1));
            root.setRight(reConstructBinaryTree(pre, i - startMid + startPre + 1, endPre,
                    mid, i + 1, endMid));
            break;
        }
        return root;
    }

}
