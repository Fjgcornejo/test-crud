package com.minsait.onesait.microservice.repository;

import java.util.List;

import com.minsait.onesait.microservice.model.TestCrudWrapper;
import com.minsait.onesait.platform.client.springboot.aspect.IoTBrokerQuery;
import com.minsait.onesait.platform.client.springboot.aspect.IoTBrokerRepository;
import com.minsait.onesait.platform.comms.protocol.enums.SSAPQueryType;

@IoTBrokerRepository("TestCrud")
public interface TestCrudRepository {

	@IoTBrokerQuery(value = "SELECT r FROM TestCrud as r", queryType = SSAPQueryType.SQL)
	List<TestCrudWrapper> findAll();
}
