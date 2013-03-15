package org.zrest.db;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zrest.db.BidDao;
import org.zrest.model.Bid;
import org.zrest.model.BidStatus;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:./src/test/resources/zrest-test-spring.xml"})
public class BidDaoTest {

    @Autowired
    BidDao bidDao;
    
    @Test
    public void testBidDao() throws SQLException {
        String sourceId = "sourceId";
        String source = "wp";
        float b = 1.2345f;
        Timestamp ts = new Timestamp(new Date().getTime());
        Bid bid = new Bid(sourceId, source, b, ts);

        // insert a new bid
        BidStatus bidStatus = bidDao.insert(bid);
        assertEquals(BidStatus.SUCCESS,bidStatus.getStatus());
        // get the inserted bid
        Bid bid2 = bidDao.get(sourceId, source);
        assertEquals(bid, bid2);
        // update bid
        bid.setBid(2.3456f);
        bidStatus = bidDao.update(bid);
        assertEquals(BidStatus.SUCCESS,bidStatus.getStatus());
        // get updated bid
        bid2 = bidDao.get(sourceId, source);
        assertEquals(bid, bid2);        
    }
}
