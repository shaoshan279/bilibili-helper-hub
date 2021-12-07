package io.cruii.bilibili.controller;

import cn.hutool.json.JSONObject;
import io.cruii.bilibili.component.BilibiliDelegate;
import io.cruii.bilibili.service.TempuseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

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
                                @CookieValue("biliJct") String biliJct) throws InterruptedException {
        BilibiliDelegate delegate = new BilibiliDelegate(dedeuserId, sessdata, biliJct);

//        File file = new File("C:\\直播间入粉丝团.txt");

        ArrayList<String> input = new ArrayList<>();
        try {
            FileReader fr = new FileReader("C:\\直播间入粉丝团.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            String pattern = "^[0-9]*$";
            // 按行读取字符串
            while ((str = bf.readLine()) != null&&str.matches(pattern)) {
                input.add(str);
            }
        }catch (Exception e){
            log.error("读取文件出错"+e.getMessage());
        }

        for (String roomOne : input ) {
            JSONObject liveRoomByRoomId = delegate.getLiveRoomInfoByRoomId(roomOne);
            JSONObject data = (JSONObject) liveRoomByRoomId.get("data");
            Integer uid2 = (Integer) data.get("uid");  //获取主播ID
            String uid = Integer.toString(uid2);
            JSONObject respDonate = delegate.sendGift(uid, roomOne, "0", "31164", 1);
            Thread.sleep(1000);
            if (respDonate.getInt("code") == 0) {
                String giftName = respDonate.getByPath("data.gift_list.0.gift_name", String.class);
                String giftNum = respDonate.getByPath("data.gift_list.0.gift_num", String.class);
                log.info("给直播间[{}]赠送了[{}]个[{}] ✔️", roomOne, giftNum, giftName);
            } else {
                log.error("给直播间[{}]赠送礼物失败：{} ❌", roomOne, respDonate.getStr("message"));
                break;
            }
        }
        return null;
    }

}
