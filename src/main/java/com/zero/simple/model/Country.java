package com.zero.simple.model;

import com.zero.simple.annotation.Desensitize;
import com.zero.simple.plugins.DesensitizeStrategy;
import lombok.Data;

/**
 * @author zero
 * @description Country
 * @date 2022/3/21 9:15
 */
@Data
public class Country {
    private Long id;
    @Desensitize(strategy = DesensitizeStrategy.NAME)
    private String countryName;
    private String countryCode;
}
