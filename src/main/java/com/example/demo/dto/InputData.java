package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: liaoze
 * @date: 2020/8/26 8:38 上午
 * @version:
 */
//select id,create_time as createTime,CEMS_in_NOX as inNox,CEMS_in_so2 as inSo2,CEMS_in_flux as InFlux,CEMS_in_o2 as inO2,CEMS_in_temp as inTemp,now() as mysqlNowTime
//    from server_table_1 order by id desc limit 1
@Data
public class InputData {

    private long id;

    private Double inNox;

    private Double inSo2;

    private Double inFlux;

    private Double inO2;

    private Double inTemp;

    private Date createTime;

    private Date mysqlNowTime;


}
