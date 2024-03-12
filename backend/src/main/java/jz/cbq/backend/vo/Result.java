package jz.cbq.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result 返回操作类
 *
 * @param <T>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private String code;
    /**
     * 数据
     */
    private T data;
    /**
     * msg
     */
    private String msg;

    public static <T> Result<T> success() {
        return new Result<>("20000", null, "success");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("20000", data, "success");
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>("20000", data, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>("20000", null, msg);
    }

    public static <T> Result<T> fail() {
        return new Result<>("20001", null, "fail");
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>("20001", null, msg);
    }
}
