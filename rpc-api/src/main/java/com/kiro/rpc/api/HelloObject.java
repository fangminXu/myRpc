package com.kiro.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Xufangmin
 * @create 2021-08-10-17:05
 */

@Data
@AllArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String massage;
}
