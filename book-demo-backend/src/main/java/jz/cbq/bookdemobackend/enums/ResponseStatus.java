package jz.cbq.bookdemobackend.enums;


import lombok.Getter;

/**
 * ResponseStatus
 *
 * @author cbq
 * @date 2023/12/15 15:43
 * @since 1.0.0
 */
@Getter
public enum ResponseStatus {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败");

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
