package io.cruii.bilibili.controller;

import cn.hutool.json.JSONObject;
import io.cruii.bilibili.component.BilibiliDelegate;
import io.cruii.bilibili.service.TempuseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("temp")
@Log4j2
public class TempuseController {
    private final TempuseService tempuseService;

    private  BilibiliDelegate bilibiliDelegate;

    public TempuseController(TempuseService tempuseService) {
        this.tempuseService = tempuseService;
    }

    @GetMapping("ces")
    public Object batchJoinFans(@CookieValue("dedeuserid") String dedeuserId,
                                @CookieValue("sessdata") String sessdata,
                                @CookieValue("biliJct") String biliJct, String roomId){
        BilibiliDelegate delegate = new BilibiliDelegate(dedeuserId, sessdata, biliJct);

        JSONObject liveRoomByRoomId = delegate.getLiveRoomInfoByRoomId(roomId);
        JSONObject data = (JSONObject) liveRoomByRoomId.get("data");
        String uid = (String) data.get("uid");  //获取主播ID

        JSONObject respDonate = delegate.sendGift(uid, roomId, "0", "31164", 1);
        if (respDonate.getInt("code") == 0) {
            String giftName = respDonate.getByPath("data.gift_list.0.gift_name", String.class);
            String giftNum = respDonate.getByPath("data.gift_list.0.gift_num", String.class);
            log.info("给直播间[{}]赠送了[{}]个[{}] ✔️", roomId, giftNum, giftName);
        } else {
            log.error("给直播间[{}]赠送礼物失败：{} ❌", roomId, respDonate.getStr("message"));
        }
        return null;
    }

}
