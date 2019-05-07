package com.guli.edu.controller;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author zay
 * @since 2019-04-22
 */
@RestController
@RequestMapping("/edu/statistics-daily")
@ComponentScan(basePackages={"com.guli.statistics","com.guli.common"})
public class StatisticsDailyController {

}

