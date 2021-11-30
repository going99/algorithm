package com.go.lesson3;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Description: SubResult
 * Created on 2021/10/26.
 *
 * @author go
 */
@Data
@Accessors(chain = true)
@ToString
public class SubResult {

    private int begin1;
    private int end1;
    private int begin2;
    private int end2;

}
