package com.zero.simple.mapper;

import com.zero.simple.model.Country;

import java.util.List;

/**
 * @author zero
 * @description CountryMapper
 * @date 2022/3/21 9:23
 */
public interface CountryMapper {
    List<Country> selectAll();
}
