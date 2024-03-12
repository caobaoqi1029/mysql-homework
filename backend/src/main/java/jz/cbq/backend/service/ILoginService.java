package jz.cbq.backend.service;

/**
 * ILoginService
 *
 * @author caobaoqi
 */
public interface ILoginService{
    /**
     * 退出登录
     * @param token token
     */
    void logout(String token);
}
