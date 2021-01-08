package com.bimuo.easy.collection.personposition.core.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.base.Preconditions;

public class AssertUtils {
	public static void checkArgument(boolean expression, RuntimeException ex) {
		try {
			Preconditions.checkArgument(expression);
		} catch (IllegalArgumentException e) {
			throw ex;
		}
	}

	public static <T extends @NonNull Object> T checkNotNull(T reference, RuntimeException ex) {
		try {
			Preconditions.checkNotNull(reference);
		} catch (IllegalArgumentException e) {
			throw ex;
		}
		return reference;
	}
}
