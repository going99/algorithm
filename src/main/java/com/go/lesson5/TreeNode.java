package com.go.lesson5;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created on 2021/11/17
 * Description()
 *
 * @author go
 */
@Data
@ToString
@Accessors(chain = true)
public class TreeNode<T> {

    private T data;
    private TreeNode left;
    private TreeNode right;

}
