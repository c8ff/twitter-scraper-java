package dev.seeight.xeno.platform;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StandardExtension implements PlatformExtensions {
	@Override
	public boolean canAccessMethod(Method method, Object instance) {
		return method.canAccess(instance);
	}

	@Override
	public String encodeUriString(String str) {
		return URLEncoder.encode(str, StandardCharsets.UTF_8);
	}
}
