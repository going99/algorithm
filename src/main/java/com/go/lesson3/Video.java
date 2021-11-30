package com.go.lesson3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created on 2021/10/24
 * Description()
 *
 * @author JimGo Yan
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    private int SeqNo;
    private String name;
    private int PraiseNum;

}
