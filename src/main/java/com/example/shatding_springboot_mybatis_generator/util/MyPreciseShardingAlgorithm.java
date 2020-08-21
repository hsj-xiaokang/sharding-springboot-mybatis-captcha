package com.example.shatding_springboot_mybatis_generator.util;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class MyPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long>, RangeShardingAlgorithm<Long> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPreciseShardingAlgorithm.class);
	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {		
		//availableTargetNames: t_user0,t_user1
		//shardingValue: {"logicTableName":"t_user","columnName":"id","value":396416249350848512}
		//collection:["t_user0","t_user1"],preciseShardingValue:{"logicTableName":"t_user","columnName":"id","value":396416249350848512}
		//name为两张订单表 t_user0 和 t_user1
		System.out.println(JSONUtils.toJSONString(availableTargetNames));
		for (String name : availableTargetNames) {
			//订单号取模加1 与 订单表t_user0 和 t_user1的尾号做比对，如相等，就直接返回t_user0 或 t_user1
			LOGGER.info(" id: " + shardingValue.getValue());
			long idLast = shardingValue.getValue() / 2L % 2L;
			LOGGER.info(" / %: " + idLast);
			if (name.endsWith(String.valueOf(idLast))) {
				LOGGER.info(" select: " + name);
				return name;
			}
		}
		return null;
	}
	@Override
	public Collection<String> doSharding(Collection<String> arg0, RangeShardingValue<Long> arg1) {
//		System.out.println(JSONUtils.toJSONString(arg0));
//		System.out.println(JSONUtils.toJSONString(arg1));
		return null;
	}
}