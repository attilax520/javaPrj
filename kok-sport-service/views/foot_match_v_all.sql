﻿DROP VIEW
IF EXISTS `foot_match_v_all`;
        CREATE
        OR
        REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `foot_match_v_all` AS
        SELECT
                `m`.`id`                                             AS `id`                           ,
                `m`.`match_event_id`                                 AS `match_event_id`               ,
                `m`.`saishi`                                         AS `saishi`                       ,
                `m`.`match_status`                                   AS `match_status`                 ,
                `m`.`match_time`                                     AS `match_time`                   ,
                `m`.`tee_time`                                       AS `tee_time`                     ,
                `m`.`home_id`                                        AS `home_id`                      ,
                `m`.`away_id`                                        AS `away_id`                      ,
                `m`.`match_detail`                                   AS `match_detail`                 ,
                `m`.`which_round`                                    AS `which_round`                  ,
                `m`.`neutral_site`                                   AS `neutral_site`                 ,
                `m`.`animation`                                      AS `animation`                    ,
                `m`.`intelligence`                                   AS `intelligence`                 ,
                `m`.`squad`                                          AS `squad`                        ,
                `m`.`video`                                          AS `video`                        ,
                `m`.`create_time`                                    AS `create_time`                  ,
                `m`.`delete_flag`                                    AS `delete_flag`                  ,
                `m`.`name_zh`                                        AS `name_zh`                      ,
                `m`.`fav`                                            AS `fav`                          ,
                `m`.`id`                                             AS `match_id`                     ,
                '---------------zhudui socre'                        AS `---------------zhudui socre`  ,
                `score_byteamidMatchid`(`m`.`id`,`m`.`home_id`,1)    AS `score`                        ,
                `scoreOf_league_ranking`(`m`.`id`,`m`.`home_id`,1)   AS `league_ranking`               ,
                `scoreOf_half_court_score`(`m`.`id`,`m`.`home_id`,1) AS `half_court_score`             ,
                `scoreOf_red_card`(`m`.`id`,`m`.`home_id`,1)         AS `red_card`                     ,
                `scoreOf_yellow_card`(`m`.`id`,`m`.`home_id`,1)      AS `yellow_card`                  ,
                `scoreOf_corner_kick`(`m`.`id`,`m`.`home_id`,1)      AS `corner_kick`                  ,
                `scoreOf_point_score`(`m`.`id`,`m`.`home_id`,1)      AS `point_score`                  ,
                '-----------------zhudui odds'                       AS `-----------------zhudui odds` ,
                `oddsOf_home_odds`(`m`.`id`,`m`.`home_id`,1)         AS `home_odds`                    ,
                `oddsOf_tie_odds`(`m`.`id`,`m`.`home_id`,1)          AS `tie_odds`                     ,
                `oddsOf_away_odds`(`m`.`id`,`m`.`home_id`,1)         AS `away_odds`                    ,
                '------------------kedui  odds'                      AS `------------------kedui  odds`,
                `oddsOf_home_odds`(`m`.`id`,`m`.`away_id`,2)         AS `home_odds2`                   ,
                `oddsOf_tie_odds`(`m`.`id`,`m`.`away_id`,2)          AS `tie_odds2`                    ,
                `oddsOf_away_odds`(`m`.`id`,`m`.`away_id`,2)         AS `away_odds2`                   ,
                '-----------kedui score----'                         AS `-----------kedui score----`   ,
                `score_byteamidMatchid`(`m`.`id`,`m`.`away_id`,2)    AS `kedui_bifen`                  ,
                `scoreOf_league_ranking`(`m`.`id`,`m`.`away_id`,2)   AS `league_ranking_kedui`         ,
                `scoreOf_half_court_score`(`m`.`id`,`m`.`away_id`,2) AS `half_court_score_kedui`       ,
                `scoreOf_red_card`(`m`.`id`,`m`.`away_id`,2)         AS `red_card_kedui`               ,
                `scoreOf_yellow_card`(`m`.`id`,`m`.`away_id`,2)      AS `yellow_card_kedui`            ,
                `scoreOf_corner_kick`(`m`.`id`,`m`.`away_id`,2)      AS `corner_kick_kedui`            ,
                `scoreOf_point_score`(`m`.`id`,`m`.`away_id`,2)      AS `point_score_kedui`            ,
                '------------teaminfo---'                            AS `------------teaminfo---`      ,
                `teamname_byid`(`m`.`home_id`)                       AS `teamname_zhudui`              ,
                `teamname_byid`(`m`.`away_id`)                       AS `kedui`                        ,
                `teamlogo`(`m`.`home_id`)                            AS `logo`                         ,
                `teamlogo`(`m`.`away_id`)                            AS `logo_kedui`                   ,
                '----------------env---'                             AS `----------------env---`       ,
                `env`.`pressure`                                     AS `pressure`                     ,
                `env`.`temperature`                                  AS `temperature`                  ,
                `env`.`wind`                                         AS `wind`                         ,
                `env`.`humidity`                                     AS `humidity`                     ,
                `env`.`weather`                                      AS `weather`                      ,
                `m`.`saishi`                                         AS `event_saishi`
        FROM
                (

                `foot_match_exV2` `m`
        LEFT JOIN
                `football_environment_t` `env`
        ON
                ((
                                `env`.`id` = `m`.`id`) and m.delete_flag=0)



  ) where m.delete_flag=0;