package com.zero.simple.plugins;

/**
 * @author zero
 * @description DesensitizeStrategy
 * @date 2022/4/1 10:34
 */
public enum DesensitizeStrategy {
    // 名称脱敏
    NAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
    ;

    private final Desensitizer desensitizer;
    DesensitizeStrategy(Desensitizer desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Desensitizer getDesensitizer() {
        return desensitizer;
    }
}
