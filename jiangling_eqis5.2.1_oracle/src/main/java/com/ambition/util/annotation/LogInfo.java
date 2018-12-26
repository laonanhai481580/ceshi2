package com.ambition.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface LogInfo {
	/**操作日志详细的存储参数名**/
	public static final String MESSAGE_ATTRIBUTE = "ambition.log.message";
	/**操作类型**/
	public abstract String optType();
	
	/**操作描述**/
	public abstract String message() default "";
}
