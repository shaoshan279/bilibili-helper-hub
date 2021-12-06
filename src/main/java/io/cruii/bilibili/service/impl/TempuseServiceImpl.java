package io.cruii.bilibili.service.impl;

import cn.hutool.json.JSONObject;
import io.cruii.bilibili.component.BilibiliDelegate;
import io.cruii.bilibili.service.TempuseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class TempuseServiceImpl implements TempuseService {

    private BilibiliDelegate bilibiliDelegate;


    @Override
    public JSONObject batchFan(String dedeuserId, String sessdata, String biliJct,String userId, String roomId,
                           String bagId, String giftId, int giftNum) {
        bilibiliDelegate=new BilibiliDelegate(dedeuserId,sessdata,biliJct);
        JSONObject jsonObject = bilibiliDelegate.donateGift(userId, roomId, bagId, giftId, giftNum);
        return jsonObject;
    }

    @Override
    public JSONObject getLiveRoomByRoomId(String roomId) {
        JSONObject liveRoomInfoByRoomId = bilibiliDelegate.getLiveRoomInfoByRoomId(roomId);
        return liveRoomInfoByRoomId;
    }

    @Override
    public JSONObject getLiveRoomByuId(String uid) {

        return null;
    }
}
