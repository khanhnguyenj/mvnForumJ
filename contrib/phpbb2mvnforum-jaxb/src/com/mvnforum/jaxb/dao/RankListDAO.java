package com.mvnforum.jaxb.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.MvnforumType;
import com.mvnforum.jaxb.db.ObjectFactory;
import com.mvnforum.jaxb.db.RankType;
import com.mvnforum.jaxb.db.MvnforumType.RankListType;
import com.mvnforum.jaxb.util.XMLUtil;

public class RankListDAO {
    
    public RankListType getRankListType(Collection rankTypes) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MvnforumType.RankListType rankListType = objectFactory.createMvnforumTypeRankListType();
        for (Iterator iter = rankTypes.iterator(); iter.hasNext(); ) {
            rankListType.getRank().add(iter.next());
        }
        return rankListType;
    }
    
    public RankType getRankType (int rankMinPosts, int rankLevel, String rankTitle, String rankImage, int rankType, int rankOption) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        RankType rank = objectFactory.createRankType();
        rank.setRankMinPosts(rankMinPosts);
        rank.setRankLevel(rankLevel);
        rank.setRankTitle(rankTitle);
        rank.setRankImage(rankImage);
        rank.setRankType(rankType);
        rank.setRankOption(rankOption);
        return rank;
    }
    
    public List importRankType () throws JAXBException {
        Mvnforum mvnforum = XMLUtil.getMvnforum();
        RankListType rankListType = mvnforum.getRankList();
        return rankListType.getRank();
    }

}
