package dev.seeight.xeno.platform;

import java.lang.reflect.Method;

public interface PlatformExtensions {
	boolean canAccessMethod(Method method, Object instance);

	String encodeUriString(String str);

	class Instance {
		public static PlatformExtensions impl = new StandardExtension();
	}
}
