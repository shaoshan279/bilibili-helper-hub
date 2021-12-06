package io.cruii.bilibili.service;

import cn.hutool.json.JSONObject;


public interface TempuseService {
    /**
     * 批量关注直播(12.5-9)  0.1加粉丝团
     * @return
     */
    JSONObject batchFan(String dedeuserId, String sessdata, String biliJct,String userId, String roomId,
                    String bagId, String giftId, int giftNum);

    /**
     * 直播间号获取信息
     */
    JSONObject getLiveRoomByRoomId(String roomId);

    JSONObject getLiveRoomByuId(String uid);
}
