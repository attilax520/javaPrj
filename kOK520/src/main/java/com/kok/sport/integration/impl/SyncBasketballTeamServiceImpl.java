package com.kok.sport.integration.impl;

import com.alibaba.fastjson.JSONObject;
import com.kok.sport.dao.BasketballTeamDao;
import com.kok.sport.integration.SyncBasketballTeamService;
import com.kok.sport.service.BasketballTeamService;
import com.kok.sport.utils.constant.HttpRequestUtil;
import com.kok.sport.vo.BasketballTeamVO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncBasketballTeamServiceImpl implements SyncBasketballTeamService {

    private BasketballTeamService basketballTeamService;

    /**
     * 篮球球队信息
     * @return
     */
    @Override
    public boolean basketballTeamList() {
        String url = "http://www.skrsport.live/?service=Basketball.Basic.Team_list&username=sport_api&secret=0gclkqzK";
        String result = HttpRequestUtil.doGet(url);
        //解析返回结果
        JSONObject resultObj = JSONObject.parseObject(result);
        if(resultObj.getLong("ret").equals(200)) {
            ArrayList<JSONObject> dataArr = resultObj.getObject("data", ArrayList.class);
            List<BasketballTeamVO> teamList = new ArrayList();
            for (JSONObject jsonObject : dataArr) {
                BasketballTeamVO teamVO = new BasketballTeamVO();
                teamVO.setId(jsonObject.getLong("id"));
                teamVO.setMatcheventId(jsonObject.getLong("matchevent_id"));
                teamVO.setConferenceId(jsonObject.getLong("conference_id"));
                teamVO.setNameZh(jsonObject.getString("name_zh"));
                teamVO.setNameZht(jsonObject.getString("name_zht"));
                teamVO.setNameEn(jsonObject.getString("name_en"));
                teamVO.setShortNameZh(jsonObject.getString("short_name_zh"));
                teamVO.setShortNameZht(jsonObject.getString("short_name_zht"));
                teamVO.setShortNameEn(jsonObject.getString("short_name_en"));
                teamVO.setLogo(jsonObject.getString("logo"));
                teamVO.setCreateTime(LocalTime.now());
                teamVO.setDeleteFlag('0');
                teamList.add(teamVO);
            }
            basketballTeamService.saveBasketballTeam(teamList);
            return true;
        }
        return false;
    }
}
