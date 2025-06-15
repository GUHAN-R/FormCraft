package com.guhan.formcraft.utilities;
import java.util.UUID;

public class UUIDGenerator {

	public static String getUUID () {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
