package jz.cbq.bookdemobackend.entity;

import jz.cbq.bookdemobackend.enums.ResponseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result
 *
 * @author cbq
 * @date 2023/12/15 15:40
 * @since 1.0.0
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    /**
     * 创建一个成功的Result对象，其中data为给定的参数
     *
     * @param data 给定的数据
     * @param <T>  数据的类型
     * @return 成功的结果对象
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 创建一个成功的Result对象，其中code、message和data为给定的参数
     *
     * @param code    结果的code
     * @param message 结果的message
     * @param data    给定的数据
     * @param <T>     数据的类型
     * @return 成功的结果对象
     */
    public static <T> Result<T> ok(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 创建一个成功的Result对象，其中data为空
     *
     * @return 成功的结果对象
     */
    public static <T> Result<T> ok() {
        return new Result<>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), null);
    }

    /**
     * 创建一个失败的Result对象，其中data为空
     *
     * @return 失败的结果对象
     */
    public static <T> Result<T> error() {
        return new Result<>(ResponseStatus.ERROR.getCode(), ResponseStatus.ERROR.getMessage(), null);
    }

    /**
     * 创建一个失败的Result对象，其中message为给定的参数
     *
     * @param message 失败的message
     * @param <T>     数据的类型
     * @return 失败的结果对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResponseStatus.ERROR.getCode(), message, null);
    }

    /**
     * 创建一个失败的Result对象，其中code和message为给定的参数
     *
     * @param code    结果的code
     * @param message 失败的message
     * @param <T>     数据的类型
     * @return 失败的结果对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 将当前Result对象设置为失败，并设置message为给定的参数
     *
     * @param message 失败的message
     * @return 当前Result对象
     */
    public Result<T> errorWithMessage(String message) {
        this.code = ResponseStatus.ERROR.getCode();
        this.message = message;
        this.data = null;
        return this;
    }

}
