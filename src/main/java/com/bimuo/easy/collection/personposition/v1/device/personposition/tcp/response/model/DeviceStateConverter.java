package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.model;

import javax.persistence.AttributeConverter;

public class DeviceStateConverter implements AttributeConverter<DeviceState, Integer> {

	@Override
	public Integer convertToDatabaseColumn(DeviceState attribute) {
		switch (attribute) {
		case Online:
			return 1;
		case Offline:
		default:
			return 0;
		}
	}

	@Override
	public DeviceState convertToEntityAttribute(Integer dbData) {
		switch (dbData) {
		case 1:
			return DeviceState.Online;
		case 0:
		default:
			return DeviceState.Offline;
		}
	}
}
