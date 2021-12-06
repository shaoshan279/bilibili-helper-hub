package io.cruii.bilibili.task;

import cn.hutool.json.JSONObject;
import io.cruii.bilibili.component.BilibiliDelegate;
import io.cruii.bilibili.entity.TaskConfig;
import io.cruii.bilibili.exception.BilibiliCookieExpiredException;
import lombok.extern.log4j.Log4j2;

/**
 * 登录校验
 *
 * @author cruii
 * Created on 2021/9/15
 */
@Log4j2
public class CheckCookieTask extends AbstractTask {

    private final TaskConfig config;

    public CheckCookieTask(BilibiliDelegate delegate) {
        super(delegate);
        this.config = delegate.getConfig();
    }

    @Override
    public void run() {
        if (controlSwitch()) {
            checkAttemptsAndChangeProxy();
            addAttempts();
        }

        JSONObject resp = delegate.checkCookie();
        if (resp.getInt("code") == 0 &&
                Boolean.TRUE.equals(resp.getByPath("data.isLogin"))) {
            log.info("账号[{}]登陆成功 ✔️", config.getDedeuserid());
        } else {
            log.error("账号[{}]登录失败，请更新Cookie ❌", config.getDedeuserid());
            throw new BilibiliCookieExpiredException(config.getDedeuserid());
        }
    }

    @Override
    public String getName() {
        return "Cookie有效性验证";
    }
}
